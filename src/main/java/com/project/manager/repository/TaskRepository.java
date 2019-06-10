package com.project.manager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.manager.model.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {

}
