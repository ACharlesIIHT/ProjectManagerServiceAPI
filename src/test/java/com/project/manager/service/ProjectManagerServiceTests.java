package com.project.manager.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.manager.model.Parent;
import com.project.manager.model.Project;
import com.project.manager.model.Task;
import com.project.manager.model.User;
import com.project.manager.repository.ParentTaskRepository;
import com.project.manager.repository.ProjectRepository;
import com.project.manager.repository.TaskRepository;
import com.project.manager.repository.UserRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource("/app-mock.properties")
public class ProjectManagerServiceTests {
	
	
	@Autowired
	private Environment env;
	private ObjectMapper mapper;
	private Task task;
	private Parent parent;
	private User user;
	private Project project;
	
	
	@TestConfiguration
	static class ProjectManagerServiceTestsConfig {
		@Bean
		public ProjectManagerService projectManagerService() {
			return new ProjectManagerServiceImpl();
		}
	}
	
	@Autowired
	private ProjectManagerService projectManagerService;
	@MockBean
	private TaskRepository taskRepository;
	@MockBean
	private ParentTaskRepository parentTaskRepository;
	@MockBean
	private ProjectRepository projectRepository;
	@MockBean
	private UserRepository userRepository;

	
	
	@Before
	public void setInit() throws JsonParseException, JsonMappingException, IOException{
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
			
		this.parent = mapper.readValue(this.env.getProperty("parent.json"), Parent.class);
		this.task = mapper.readValue(this.env.getProperty("task.json"), Task.class);
		this.user = mapper.readValue(this.env.getProperty("user.json"), User.class);
		this.project = mapper.readValue(this.env.getProperty("project.json"), Project.class);
		
		
		List<Task> taskList = new ArrayList<Task>();
		List<Parent> parentList = new ArrayList<Parent>();
		List<User> userList = new ArrayList<User>();
		List<Project> projectList = new ArrayList<Project>();
		
		taskList.add(this.task);
		parentList.add(this.parent);
		userList.add(this.user);
		projectList.add(this.project);
		
		this.parent.setTasks(taskList);
		
		Mockito.when(this.taskRepository.findAll()).thenReturn(taskList);
		Mockito.when(this.taskRepository.save(this.task)).thenReturn(this.task);
		Mockito.when(this.taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(this.task));
		
		Mockito.when(this.parentTaskRepository.findAll()).thenReturn(parentList);
		Mockito.when(this.parentTaskRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(this.parent));
		
		Mockito.when(this.projectRepository.findAll()).thenReturn(projectList);
		
		Mockito.when(this.userRepository.findOneUserByProjectId(Mockito.anyString())).thenReturn(userList);
		Mockito.when(this.userRepository.findAll()).thenReturn(userList);
		
	}
	
	@Test
	public void testGetAllTasks() {
		List<Task> tasks = this.projectManagerService.getAllTasks();
		boolean[] pass = { false };
		tasks.forEach(a ->{
			if(a.getTask().equalsIgnoreCase(this.task.getTask())) {
				pass [0] = true;
			}
		});
		assertThat(pass [0]);
	}
	
	@Test
	public void testGetAllParent() {
		List<Parent> parents = this.projectManagerService.getAllParent();
		boolean[] pass = { false };
		parents.forEach(b ->{
			if(b.getTask().equalsIgnoreCase(this.parent.getTask())) {
				pass [0] = true;
			}
		});
		assertThat(pass [0]);
	}
	
	@Test
	public void testGetAllProject() {
		List<Project> projects = this.projectManagerService.getProjectList();
		boolean[] pass = { false };
		projects.forEach(c ->{
			if(c.getName().equalsIgnoreCase(this.project.getName())) {
				pass [0] = true;
			}
		});
		assertThat(pass [0] == true);
	}
	
	@Test
	public void testGetAllUsers() {
		List<User> users = this.projectManagerService.getAllUsers();
		boolean[] pass = { false };
		users.forEach(d ->{
			if(d.getEmployeeId().equalsIgnoreCase(this.user.getEmployeeId())) {
				pass [0] = true;
			}
		});
		assertThat(pass [0] == true);
	}
	
	@Test
	public void testgetTaskById() {
		Task taskObjSaved = this.projectManagerService.saveTask(this.task);
		Task taskObj = this.projectManagerService.getTaskById(String.valueOf(taskObjSaved.getId()));
		assertThat(taskObj.getTask().equalsIgnoreCase(this.task.getTask()));
	}
	
	@Test
	public void testSaveTask() {
		Task taskObjSaved = this.projectManagerService.saveTask(this.task);
		assertThat(taskObjSaved.getTask().equalsIgnoreCase(this.task.getTask()));
	}
	
	@Test
	public void testEndTask() {
		Task taskObjSaved = this.projectManagerService.saveTask(this.task);
		Task taskObjEnded = this.projectManagerService.endTask(String.valueOf(taskObjSaved.getId()));
		LocalDate currentDate = LocalDate.now();
		assertThat(currentDate.compareTo(taskObjEnded.getEndDate()) == 0);
	}
	
	@Test (expected = Test.None.class)
	public void testParentSave() {
		this.projectManagerService.saveParentTask(this.task);
	}
	
	@Test (expected = Test.None.class)
	public void testSaveOrUpdateUser() {
		this.projectManagerService.saveOrUpdateUser(this.user);
	}
	
	@Test (expected = Test.None.class)
	public void testSaveOrUpdateProject() {
		this.projectManagerService.saveOrUpdateProject(this.project);
	}
	
	
}
