package com.project.ContentPublishing.repository;

import com.project.ContentPublishing.model.Article;
import com.project.ContentPublishing.model.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    List<Article> findByStatus(ArticleStatus articleStatus);
}
