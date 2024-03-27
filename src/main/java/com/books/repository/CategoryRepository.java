package com.books.repository;


import com.books.entities.AuthorEntity;
import com.books.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
   CategoryEntity findByCategoryName(String categoryName);
}