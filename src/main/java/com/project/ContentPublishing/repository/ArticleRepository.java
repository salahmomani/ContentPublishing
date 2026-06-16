package com.project.ContentPublishing.repository;

import com.project.ContentPublishing.model.Article;
import com.project.ContentPublishing.model.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByStatus(ArticleStatus status);

    List<Article> findByAuthorId(Long authorId);

    @Query("SELECT l FROM Article l WHERE l.id = :articleId AND l.author.id = :authorId")
    Optional<Article> findByAuthorIdAndArticleId(
            @Param("articleId") Long articleId,
            @Param("authorId") Long authorId
    );

    Optional<Article> findByIdAndStatus(Long id, ArticleStatus status);

    boolean existsBySlug(String slug);
}
