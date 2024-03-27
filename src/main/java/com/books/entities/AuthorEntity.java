package com.books.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "author")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "author_name")
    private String authorName;

    @OneToMany(mappedBy = "authorEntity")
    private List<BookEntity> books;

}
