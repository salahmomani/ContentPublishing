package com.project.ContentPublishing.repository;

import com.project.ContentPublishing.model.Article;
import com.project.ContentPublishing.model.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByStatus(ArticleStatus articleStatus);

    List<Article> findByAuthorId(Long authorId);
}
