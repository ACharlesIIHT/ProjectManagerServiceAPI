package com.project.manager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.manager.model.User;
import com.project.manager.service.ProjectManagerService;

@RestController
@RequestMapping("/userservice/")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private ProjectManagerService projectManagerService;

	@GetMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> getAllUsers(Model model) {
		logger.info("<-- Inside getAllUsers -->");
		List<User> users = this.projectManagerService.getAllUsers();
		if (users == null || users.isEmpty()) {
			return new ResponseEntity<List<User>>(users, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<User> saveOrUpdate(@RequestBody User user) {
		return new ResponseEntity<User>(this.projectManagerService.saveOrUpdateUser(user), HttpStatus.OK);
	}
}
