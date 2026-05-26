package com.project.ContentPublishing.repository;

import com.project.ContentPublishing.model.ArticleCategory;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<ArticleCategory, Long> {
    boolean existsByName(String name);
}
