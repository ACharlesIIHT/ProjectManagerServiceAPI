package com.project.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.manager.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{

}
