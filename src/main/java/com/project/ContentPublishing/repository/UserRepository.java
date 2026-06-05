package com.project.ContentPublishing.repository;

import com.project.ContentPublishing.model.Roles;
import com.project.ContentPublishing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByRole(Roles roles);

    Optional<User> findByEmail(String email);
}
