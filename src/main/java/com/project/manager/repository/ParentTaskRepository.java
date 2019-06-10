package com.project.manager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.manager.model.Parent;

@Repository
public interface ParentTaskRepository extends CrudRepository<Parent, Integer> {

}
