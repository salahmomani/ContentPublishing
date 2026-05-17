package com.project.ContentPublishing.repository;

import com.project.ContentPublishing.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByArticleIdAndUserId(Long articleId, Long userId);
}
