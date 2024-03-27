package com.books.service.impl;

import com.books.dto.AuthorDTO;
import com.books.dto.BookDTO;
import com.books.dto.CategoryDTO;
import com.books.entities.AuthorEntity;
import com.books.entities.BookEntity;
import com.books.entities.CategoryEntity;
import com.books.repository.AuthorRepository;
import com.books.repository.BookRepository;
import com.books.repository.CategoryRepository;
import com.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookApplicationService implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO::toBookDto)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(BookDTO::toBookDto)
                .orElse(null);
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorEntity -> {
                    List<String> titles = authorEntity.getBooks().stream()
                            .map(BookEntity::getTitle)
                            .collect(Collectors.toList());
                    return AuthorDTO.builder()
                            .authorId(authorEntity.getAuthorId())
                            .authorName(authorEntity.getAuthorName())
                            .titles(titles)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .map(authorEntity -> {
                    List<String> titles = authorEntity.getBooks().stream()
                            .map(BookEntity::getTitle)
                            .collect(Collectors.toList());
                    return AuthorDTO.builder()
                            .authorId(authorEntity.getAuthorId())
                            .authorName(authorEntity.getAuthorName())
                            .titles(titles)
                            .build();
                })
                .orElse(null);
    }

    public List<CategoryDTO> getAllCategoriesWithBooks() {
        return categoryRepository.findAll().stream()
                .map(this::convertCategoryEntityToDtoWithBooks)
                .collect(Collectors.toList());
    }

    public CategoryDTO convertCategoryEntityToDtoWithBooks(CategoryEntity categoryEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName(categoryEntity.getCategoryName());

        List<String> bookTitles = categoryEntity.getBooks().stream()
                .map(BookEntity::getTitle)
                .collect(Collectors.toList());

        categoryDTO.setTitles(bookTitles);

        return categoryDTO;
    }

    public List<BookDTO> getBooksByCategory(String categoryName) {
        CategoryEntity category = categoryRepository.findByCategoryName(categoryName);
        if (category != null) {
            return category.getBooks().stream()
                    .map(BookDTO::toBookDto)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }


    private AuthorEntity getOrCreateAuthor(String authorName) {
        return Optional.ofNullable(authorRepository.findByAuthorName(authorName))
                .orElseGet(() -> authorRepository.save(AuthorEntity.builder()
                        .authorName(authorName)
                        .build()));
    }

    private CategoryEntity getOrCreateCategory(String categoryName) {
        return Optional.ofNullable(categoryRepository.findByCategoryName(categoryName))
                .orElseGet(() -> categoryRepository.save(CategoryEntity.builder()
                        .categoryName(categoryName)
                        .build()));
    }


    public List<BookDTO> saveAndUpdateBooks(List<BookDTO> bookDTOs) {
        return bookDTOs.stream()
                .map(bookDto -> {
                    Long bookId = bookDto.getBookId();
                    BookEntity bookEntity = (bookId != null) ?
                            bookRepository.findById(bookId).orElse(BookEntity.builder().bookId(bookId).build()) :
                            BookEntity.builder().build();

                    AuthorEntity authorEntity = getOrCreateAuthor(bookDto.getAuthorName());
                    CategoryEntity categoryEntity = getOrCreateCategory(bookDto.getCategoryName());

                    //  null check on bookEntity before accessing its methods
                    if (bookEntity != null) {
                        bookEntity = bookEntity.toBuilder()
                                .isbn(bookDto.getIsbn())
                                .publicationYear(bookDto.getPublicationYear())
                                .title(bookDto.getTitle())
                                .authorEntity(authorEntity)
                                .categoryEntity(categoryEntity)
                                .build();

                        return bookRepository.save(bookEntity);
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(BookDTO::toBookDto)
                .collect(Collectors.toList());
    }

}





