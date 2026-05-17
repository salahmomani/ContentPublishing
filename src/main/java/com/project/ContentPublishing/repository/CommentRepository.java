package com.project.ContentPublishing.repository;

import com.project.ContentPublishing.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
