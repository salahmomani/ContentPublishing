package com.project.ContentPublishing.repository;

import com.project.ContentPublishing.model.Like;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Like l WHERE l.article.id = :articleId AND l.user.id = :userId")
    boolean existsByArticleIdAndUserId(Long articleId, Long userId);
}
