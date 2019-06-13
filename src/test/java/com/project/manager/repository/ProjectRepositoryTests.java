package com.project.manager.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.project.manager.model.Project;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration
@TestPropertySource("/app-mock.properties")
public class ProjectRepositoryTests {
	
	
	@Autowired
	private Environment env;
	@Autowired
	private ProjectRepository projectRepository;

	private Project project;
	
	@Before
	public void setInit() throws JsonParseException, JsonMappingException, IOException{
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		this.project = mapper.readValue(this.env.getProperty("project.json"), Project.class);
	}
	
	@Test
	public void testSaveorUpdate() {
		Project projectSave = this.projectRepository.save(this.project);
		assertThat(project.getName().equalsIgnoreCase(projectSave.getName()));
	}
	
	@Test
	public void testFindByID() {
		Project projectSave = this.projectRepository.save(this.project);
		Project project = this.projectRepository.findById(projectSave.getProjectId()).get();
		assertThat(project.getName().equalsIgnoreCase(this.project.getName()));
	}
		
}
