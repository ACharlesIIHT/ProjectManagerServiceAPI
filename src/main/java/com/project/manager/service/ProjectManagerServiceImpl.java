package com.project.manager.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.project.manager.model.Parent;
import com.project.manager.model.Project;
import com.project.manager.model.Task;
import com.project.manager.model.User;
import com.project.manager.repository.ParentTaskRepository;
import com.project.manager.repository.ProjectRepository;
import com.project.manager.repository.TaskRepository;
import com.project.manager.repository.UserRepository;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ParentTaskRepository ParentTaskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Task> getAllTasks() {
		List<Task> tasks = (List<Task>) this.taskRepository.findAll();
		tasks.forEach(task -> {
			if (task.getEndDate() != null) {
				task.setStatus((task.getEndDate().isBefore(LocalDateTime.now().toLocalDate())
						|| task.getEndDate().isEqual(LocalDateTime.now().toLocalDate())) ? "Completed" : "In-Progress");
			} else {
				task.setStatus("In-Progress");
			}
		});
		return tasks;
	}

	@Override
	public Task saveTask(Task task) {
		Task savedTask = this.taskRepository.save(task);
		User user = task.getUser();
		if (user != null && user.getFirstName() != null) {
			user.setTaskId(String.valueOf(task.getId()));
			this.userRepository.save(user);
		}
		return savedTask;
	}

	@Override
	public Task getTaskById(String id) {
		Optional<Task> opTask = this.taskRepository.findById(Integer.parseInt(id));
		if (!opTask.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		return opTask.get();
	}

	@Override
	public Task endTask(String taskId) {
		Task task = getTaskById(taskId);
		LocalDate currentDate = LocalDate.now();
		task.setEndDate(currentDate);
		Optional<Parent> parent = this.ParentTaskRepository.findById(task.getId());
		if (parent.isPresent()) {
			parent.get().getTasks().forEach(t -> {
				t.setEndDate(currentDate);
				this.taskRepository.save(t);
			});
		}
		return this.taskRepository.save(task);
	}

	@Override
	public Project saveOrUpdateProject(Project project) {
		this.projectRepository.save(project);
		User user = project.getUser();
		user.setProjectId(String.valueOf(project.getProjectId()));
		this.userRepository.save(user);
		return project;
	}

	@Override
	public List<Project> getProjectList() {
		List<Project> projects = this.projectRepository.findAll();
		projects.forEach(p -> {
			List<User> users = this.getUserListByProjectId(p.getProjectId());
			if (users != null && !users.isEmpty()) {
				p.setUser(users.get(0));
			}
			p.setStatus((p.getEndDate().isBefore(LocalDateTime.now().toLocalDate())
					|| p.getEndDate().isEqual(LocalDateTime.now().toLocalDate())) ? "Completed" : "In-Progress");
		});
		return projects;
	}

	@Override
	public User saveOrUpdateUser(User user) {
		return this.userRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}

	public List<User> getUserListByProjectId(Long projectId) {
		return this.userRepository.findOneUserByProjectId(String.valueOf(projectId));
	}

	@Override
	public List<Parent> getAllParent() {
		return (List<Parent>) this.ParentTaskRepository.findAll();
	}

	@Override
	public Parent saveParentTask(Task task) {
		Parent parent = new Parent();
		parent.setTask(task.getTask());
		return this.ParentTaskRepository.save(parent);
	}
}
