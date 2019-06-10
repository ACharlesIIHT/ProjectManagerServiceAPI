package com.project.manager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.manager.model.Parent;
import com.project.manager.model.Project;
import com.project.manager.model.Task;
import com.project.manager.model.User;

@Service
public interface ProjectManagerService {

	List<Task> getAllTasks();

	List<Parent> getAllParent();

	Task saveTask(Task task);

	Task getTaskById(String id);

	Task endTask(String taskId);

	Project saveOrUpdateProject(Project project);

	List<Project> getProjectList();

	User saveOrUpdateUser(User user);

	List<User> getAllUsers();

	Parent saveParentTask(Task task);
}
