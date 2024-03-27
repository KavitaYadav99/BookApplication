package com.books.service.impl;

import java.util.*;

import com.books.dto.BookDTO;
import com.books.dto.CategoryDTO;
import com.books.entities.AuthorEntity;
import com.books.entities.BookEntity;
import com.books.entities.CategoryEntity;
import com.books.repository.AuthorRepository;
import com.books.repository.BookRepository;
import com.books.repository.CategoryRepository;
import com.books.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookApplicationServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookApplicationService bookApplicationService;


    private BookEntity createMockBookData() {
        return BookEntity.builder().bookId(1L)
                .title("test")
                .isbn(1213L)
                .authorEntity(AuthorEntity.builder().authorName("abc").build())
                .categoryEntity(CategoryEntity.builder().categoryName("xyz").build())
                .publicationYear("1999")
                .build();
    }

    private AuthorEntity createMockAuthorData() {
        return AuthorEntity.builder()
                .authorId(1L)
                .authorName("Robert Cecil Martin")
                .build();
    }

    private CategoryEntity createMockCategoryData() {
        return CategoryEntity.builder()
                .categoryId(1L)
                .categoryName("Cooking")
                .build();
    }


    @Test
    void getBookById() {
        BookEntity mockData = createMockBookData();
        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockData));
        assertEquals(mockData.getBookId(), bookApplicationService.getBookById(1L).getBookId());
    }

    @Test
    void getAllBooks() {
        BookEntity mockData = createMockBookData();
        List<BookEntity> mockList = Collections.singletonList(createMockBookData());
        when(bookRepository.findAll()).thenReturn(mockList);
        List<BookDTO> result = bookApplicationService.getAllBooks();
        assertEquals(mockData.getBookId(), result.get(0).getBookId());
    }


    @Test
    void saveBooks() {
        BookEntity mockBookData = createMockBookData();

        when(authorRepository.save(Mockito.any(AuthorEntity.class))).thenReturn(createMockAuthorData());
        when(categoryRepository.save(Mockito.any(CategoryEntity.class))).thenReturn(createMockCategoryData());
        when(bookRepository.save(Mockito.any(BookEntity.class))).thenReturn(mockBookData);

        List<BookDTO> bookDTOList = Collections.singletonList(
                BookDTO.builder()
                        .title("Test Book")
                        .isbn(1256L)
                        .authorName("Robert Cecil Martin")
                        .categoryName("Cooking")
                        .publicationYear("2022")
                        .build()
        );

        List<BookDTO> savedBook = bookApplicationService.saveAndUpdateBooks(bookDTOList);
        assertEquals(mockBookData.getTitle(), savedBook.get(0).getTitle());
        assertEquals(mockBookData.getIsbn(), savedBook.get(0).getIsbn());
        assertEquals(mockBookData.getAuthorEntity().getAuthorName(), savedBook.get(0).getAuthorName());
        assertEquals(mockBookData.getCategoryEntity().getCategoryName(), savedBook.get(0).getCategoryName());
        assertEquals(mockBookData.getPublicationYear(), savedBook.get(0).getPublicationYear());
    }


    @Test
    void updateBooks() {
        // Create mock data for an old book record
        BookEntity existingBookData = createMockBookData()
                .toBuilder().build();
        // Modify existing book data
        existingBookData = existingBookData.toBuilder()
                .title("Updated Title")
                .isbn(9876L)
                .publicationYear("2023")
                .build();
        //find existing  entity by ID or name
        when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(existingBookData));
        when(authorRepository.findByAuthorName(Mockito.anyString())).thenReturn(existingBookData.getAuthorEntity());
        when(categoryRepository.findByCategoryName(Mockito.anyString())).thenReturn(existingBookData.getCategoryEntity());

        //saving entity
        when(bookRepository.save(Mockito.any(BookEntity.class))).thenReturn(existingBookData);
        when(authorRepository.save(Mockito.any(AuthorEntity.class))).thenReturn(existingBookData.getAuthorEntity());
        when(categoryRepository.save(Mockito.any(CategoryEntity.class))).thenReturn(existingBookData.getCategoryEntity());

        List<BookDTO> bookDTOList = Collections.singletonList(
                BookDTO.builder()
                        .bookId(existingBookData.getBookId())
                        .title("Updated Title")
                        .isbn(9876L)
                        .authorName(existingBookData.getAuthorEntity().getAuthorName())
                        .categoryName(existingBookData.getCategoryEntity().getCategoryName())
                        .publicationYear("2023")
                        .build()
        );

        List<BookDTO> updatedBooks = bookApplicationService.saveAndUpdateBooks(bookDTOList);

        assertEquals(1, updatedBooks.size());
        assertEquals(existingBookData.getBookId(), updatedBooks.get(0).getBookId());
        assertEquals("Updated Title", updatedBooks.get(0).getTitle());
        assertEquals(Long.valueOf(9876L), updatedBooks.get(0).getIsbn());
        assertEquals(existingBookData.getAuthorEntity().getAuthorName(), updatedBooks.get(0).getAuthorName());
        assertEquals(existingBookData.getCategoryEntity().getCategoryName(), updatedBooks.get(0).getCategoryName());
        assertEquals("2023", updatedBooks.get(0).getPublicationYear());
    }

}












