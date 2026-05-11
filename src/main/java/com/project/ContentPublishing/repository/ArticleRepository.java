package com.project.ContentPublishing.repository;

import com.project.ContentPublishing.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
