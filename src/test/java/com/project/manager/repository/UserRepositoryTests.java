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
import com.project.manager.model.Task;
import com.project.manager.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration
@TestPropertySource("/app-mock.properties")
public class UserRepositoryTests {
	
	
	@Autowired
	private Environment env;
	@Autowired
	private UserRepository userRepository;
	private User user;
	
	@Before
	public void setInit() throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		this.user = mapper.readValue(this.env.getProperty("user.json"), User.class);
	}
	
	@Test
	public void testSaveUpdate() {
		User user = this.userRepository.save(this.user);
		assertThat(this.user.getFirstName().equalsIgnoreCase(user.getFirstName()));
	}
	

	@Test
	public void testFindById() {
		User userSaved = this.userRepository.save(this.user);
		User user = this.userRepository.findById(userSaved.getId()).get();
		assertThat(user.getFirstName().equalsIgnoreCase(this.user.getFirstName()));
	}
		
}
