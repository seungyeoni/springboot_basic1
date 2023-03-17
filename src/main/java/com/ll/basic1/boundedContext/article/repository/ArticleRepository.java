package com.ll.basic1.boundedContext.article.repository;

import com.ll.basic1.boundedContext.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

// 이 클래스에는 @Repository 어노테이션을 생략해도 된다.
public interface ArticleRepository extends JpaRepository<Article, Long> {
}