package com.books.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.books.dto.AuthorDTO;
import com.books.dto.BookDTO;
import com.books.entities.AuthorEntity;
import com.books.entities.BookEntity;
import com.books.entities.CategoryEntity;
import com.books.repository.AuthorRepository;
import com.books.repository.BookRepository;
import com.books.repository.CategoryRepository;
import com.books.service.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookApplicationControllerTest {
    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private BookApplicationController bookController;

    @Test
    void testGetAllBooks() throws Exception {
        List<BookEntity> books = Collections.singletonList(new BookEntity().builder()
                .bookId(1L)
                .title("raman")
                .authorEntity(AuthorEntity.builder().authorName("abc").build())
                .categoryEntity(CategoryEntity.builder().categoryName("Fun").build())
                .isbn(1887L)
                .publicationYear("1980")
                .build());
        Mockito.when(bookRepository.findAll()).thenReturn(books);
        //convert book entity to book dto
        List<BookDTO> bookDTOs = books.stream()
                .map(BookDTO::toBookDto)
                .collect(Collectors.toList());
        MvcResult res = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(HttpStatus.OK.value(), res.getResponse().getStatus());
        //convert object to string
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(bookDTOs);
        Assertions.assertEquals(200, res.getResponse().getStatus());
        assertEquals(expectedResponse, res.getResponse().getContentAsString());
    }


    @Test
    void testGetAllBooksWhenEmpty() throws Exception {
        List<BookEntity> emptyBooks = Collections.emptyList();

        Mockito.when(bookRepository.findAll()).thenReturn(emptyBooks);

        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    void testGetBookById() throws Exception {
        // Create a sample book entity
        BookEntity book = new BookEntity().builder()
                .bookId(1L)
                .title("Demo")
                .authorEntity(AuthorEntity.builder().authorName("Hamid").build())
                .categoryEntity(CategoryEntity.builder().categoryName("Spiritual").build())
                .isbn(1230L)
                .publicationYear("2022")
                .build();
        // Mock the behavior of findById method to return the sample book
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Convert book entity to book DTO
        BookDTO bookDTO = BookDTO.toBookDto(book);

        MvcResult result = mockMvc.perform(
                        get("/books/{bookId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        //convert object to string
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponse = objectMapper.writeValueAsString(bookDTO);
        assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }

    @Test
    void testGetBookByIdNotFound() throws Exception {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(
                        get("/books/{bookId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals("Book not found for ID: 1", result.getResponse().getContentAsString());
    }

    @Test
    void testSaveBooks() throws Exception {
        List<BookEntity> bookEntities = Stream.of(
                new BookEntity().builder()
                        .bookId(1L)
                        .title("demo1")
                        .authorEntity(AuthorEntity.builder().authorName("Aman").build())
                        .categoryEntity(CategoryEntity.builder().categoryName("Fun").build())
                        .isbn(12890L)
                        .publicationYear("2021")
                        .build(),
                new BookEntity().builder()
                        .bookId(2L)
                        .title("Demo2")
                        .authorEntity(AuthorEntity.builder().authorName("Rajan").build())
                        .categoryEntity(CategoryEntity.builder().categoryName("Spiritual").build())
                        .isbn(9870L)
                        .publicationYear("2022")
                        .build()
        ).collect(Collectors.toList());

        Mockito.when(bookRepository.saveAll(Mockito.anyList())).thenReturn(bookEntities);

        // Convert book entities to book DTOs
        List<BookDTO> bookDTOs = bookEntities.stream()
                .map(BookDTO::toBookDto)
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(bookDTOs);

        //save the book using post method
        ResultActions result = mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));
        result.andReturn();
    }

    @Test
    void testUpdateBook() throws Exception {
        BookEntity bookToUpdate = new BookEntity().builder()
                .bookId(1L)
                .title("Demo")
                .authorEntity(AuthorEntity.builder().authorName("Ram").build())
                .categoryEntity(CategoryEntity.builder().categoryName("Science Fiction").build())
                .isbn(1390L)
                .publicationYear("2023")
                .build();

        // Convert the book entity to book DTO
        BookDTO bookDTO = BookDTO.toBookDto(bookToUpdate);

        // Mock the behavior of findById and save methods
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(bookToUpdate));
        Mockito.when(bookRepository.save(Mockito.any(BookEntity.class))).thenReturn(bookToUpdate);

        // Convert the updated book entity to a list of book DTOs
        List<BookDTO> bookDTOs = Collections.singletonList(bookDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(bookDTOs);

        //  POST request to update the book
        ResultActions result = mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        assertEquals(HttpStatus.OK.value(), result.andReturn().getResponse().getStatus());
    }



}























