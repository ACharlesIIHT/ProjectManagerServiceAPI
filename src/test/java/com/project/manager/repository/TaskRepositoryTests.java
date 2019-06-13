package com.project.manager.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.manager.model.Task;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
@TestPropertySource("/app-mock.properties")
public class TaskRepositoryTests {
	
	
	@Autowired
	private Environment env;
	@Autowired
	private TaskRepository taskRepository;
	private Task task;
	
	@Before
	public void setInit() throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		this.task = mapper.readValue(this.env.getProperty("task.json"), Task.class);
	}
	
	@Test
	public void testAddTask() {
		Task taskObj = this.taskRepository.save(this.task);
		assertThat(taskObj.getTask().equalsIgnoreCase(this.task.getParent().getTask()));
	}
	
	
	@Test
	public void testFindByAll() {
		this.taskRepository.save(this.task);
		Iterable<Task> tasks = this.taskRepository.findAll();
		boolean[] pass = { false };
		tasks.forEach(c ->{
			if(c.getTask().equalsIgnoreCase(this.task.getTask())) {
				pass [0] = true;
			}
		});
		assertThat(pass [0]);
	}
	
	@Test
	public void testFindById() {
		Task taskObjSaved = this.taskRepository.save(this.task);
		Task taskObj = this.taskRepository.findById(taskObjSaved.getId()).get();
		assertThat(taskObj.getTask().equalsIgnoreCase(this.task.getTask()));
	}
		
}
