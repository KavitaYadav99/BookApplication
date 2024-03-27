package com.books.entities;

import com.books.dto.BookDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "books")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {

    @Id
    //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AuthorEntity authorEntity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;


    @Column(name = "isbn")
    private Long isbn;

    @Column(name = "publication_year")
    private String publicationYear;





}
