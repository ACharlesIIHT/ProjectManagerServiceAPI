package com.project.manager.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.manager.ProjectManagerServiceApiApplication;
import com.project.manager.model.Parent;
import com.project.manager.model.Project;
import com.project.manager.model.Task;
import com.project.manager.model.User;
import com.project.manager.service.ProjectManagerService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= ProjectManagerServiceApiApplication.class)
@WebAppConfiguration
@ContextConfiguration
@TestPropertySource("/app-mock.properties")
public class ProjectManagerControllerTests {
	
	private MockMvc mockmvc;
	@Autowired
	private WebApplicationContext webapplicationcontext;
	@Autowired
	private Environment env;
	@MockBean
	private ProjectManagerService projectManagerService;
	private ObjectMapper mapper;
	private Task task;
	private Parent parent;
	private User user;
	private Project project;
	
	@Before
	public void setInit() throws JsonParseException, JsonMappingException, IOException{
		
		this.mapper = new ObjectMapper();
		this.mapper.registerModule(new JavaTimeModule());
		
		this.parent = this.mapper.readValue(this.env.getProperty("parent.json"), Parent.class);
		this.task = this.mapper.readValue(this.env.getProperty("task.json"), Task.class);
		this.user = this.mapper.readValue(this.env.getProperty("user.json"), User.class);
		this.project = this.mapper.readValue(this.env.getProperty("project.json"), Project.class);
		
		List<Task> taskList = new ArrayList<Task>();
		List<Parent> parentList = new ArrayList<Parent>();
		List<User> userList = new ArrayList<User>();
		List<Project> projectList = new ArrayList<Project>();
		
		taskList.add(this.task);
		parentList.add(this.parent);
		userList.add(this.user);
		projectList.add(this.project);
		
		this.parent.setTasks(taskList);
		
		Mockito.when(this.projectManagerService.getAllTasks()).thenReturn(taskList);
		Mockito.when(this.projectManagerService.saveTask(this.task)).thenReturn(this.task);
		Mockito.when(this.projectManagerService.getTaskById(Mockito.anyString())).thenReturn(this.task);
		Mockito.when(this.projectManagerService.endTask(Mockito.anyString())).thenReturn(this.task);
		
		Mockito.when(this.projectManagerService.getAllParent()).thenReturn(parentList);
		Mockito.when(this.projectManagerService.getProjectList()).thenReturn(projectList);
		Mockito.when(this.projectManagerService.getAllUsers()).thenReturn(userList);
		
		this.mockmvc = MockMvcBuilders.webAppContextSetup(this.webapplicationcontext).build();
		
	}
	
	@Test
	public void testGetAllProjects() throws Exception{
		String uri = "/projectmanager/projects";
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testGetAllUsers() throws Exception{
		String uri = "/userservice/users";
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testGetAllUsersFail() throws Exception{
		Mockito.when(this.projectManagerService.getAllUsers()).thenReturn(new ArrayList<User>());
		String uri = "/userservice/users";
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);
	}
	
	@Test
	public void testGetAllTasks() throws Exception{
		String uri = "/taskservice/tasks";
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testGetAllParent() throws Exception{
		String uri = "/taskservice/parent";
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testGetAllTasksFail() throws Exception{
		Mockito.when(this.projectManagerService.getAllTasks()).thenReturn(new ArrayList<Task>());
		String uri = "/taskservice/tasks";
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);
	}
	
	@Test
	public void testGetAllParentFail() throws Exception{
		Mockito.when(this.projectManagerService.getAllParent()).thenReturn(new ArrayList<Parent>());
		String uri = "/taskservice/parent";
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);
	}
	
	@Test
	public void testSaveProject() throws Exception{
		String uri = "/projectmanager/save";
		String inputJson = this.env.getProperty("project.json");
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testSaveOrUpdateUser() throws Exception{
		String uri = "/userservice/save";
		String inputJson = this.env.getProperty("user.json");
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testUpdateTask() throws Exception{
		String uri = "/taskservice/update";
		String inputJson = this.env.getProperty("task.json");
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testAddTask() throws Exception{
		String uri = "/taskservice/add";
		String inputJson = this.env.getProperty("task.json");
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void testAddParentTask() throws Exception{
		String uri = "/taskservice/add/parent";
		String inputJson = this.env.getProperty("parent.json");
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testAddParentTaskFail() throws Exception{
		String uri = "/taskservice/add/parent";
		String inputJson = "";
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}
	
	@Test
	public void testGetTask() throws Exception{
		String uri = "/taskservice/task/1";
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	public void testEndtask() throws Exception{
		String uri = "/taskservice/endtask/1";
		MvcResult mvcResult = this.mockmvc.perform(MockMvcRequestBuilders.put(uri).accept(MediaType.APPLICATION_JSON_UTF8)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
}
