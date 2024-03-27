package com.books.repository;

import com.books.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    AuthorEntity findByAuthorName(String authorName);
}