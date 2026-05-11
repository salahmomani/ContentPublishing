package com.project.ContentPublishing.repository;

import com.project.ContentPublishing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

}
