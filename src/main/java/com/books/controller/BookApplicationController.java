package com.books.controller;

import com.books.dto.AuthorDTO;
import com.books.dto.BookDTO;
import com.books.dto.CategoryDTO;
import com.books.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Books Application Endpoints")
public class BookApplicationController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Get all the books")
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        if (books != null && !books.isEmpty()) {
            return new ResponseEntity<>(books, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Get book by ID")
    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable Long bookId) {
        BookDTO bookDTO = bookService.getBookById(bookId);
        if (bookDTO != null) {
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } else {
            String errorMessage = "Book not found for ID: " + bookId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }


    @Operation(summary = "Get all the authors ")
    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = bookService.getAllAuthors();
        if (authors != null && !authors.isEmpty()) {
            return new ResponseEntity<>(authors, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get author by ID")
    @GetMapping("/authors/{authorId}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long authorId) {
        AuthorDTO authorDTO = bookService.getAuthorById(authorId);
        if (authorDTO != null) {
            return new ResponseEntity<>(authorDTO, HttpStatus.OK);
        } else {
            String errorMessage = "author not found for ID: " + authorId;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }


    @Operation(summary = "Get categories")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategoriesWithBooks() {
        List<CategoryDTO> categories = bookService.getAllCategoriesWithBooks();
        if (categories != null && !categories.isEmpty()) {
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Get books by category name")
    @GetMapping("/categories/{categoryName}")
    public ResponseEntity<?> getBooksByCategory(
            @Parameter(description = "Category name", in = ParameterIn.PATH, required = true,
                    schema = @Schema(type = "string", allowableValues = {"COOKING", "FUN", "HORROR", "SCIENCE FICTION", "SPIRITUAL"}))
            @PathVariable String categoryName) {
        List<BookDTO> books = bookService.getBooksByCategory(categoryName);
        if (books != null && !books.isEmpty()) {
            return new ResponseEntity<>(books, HttpStatus.OK);
        } else {
            String errorMessage = "Books are not available for category name: " + categoryName;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @Operation(summary = "save or update book")
    @PostMapping
    public ResponseEntity<List<BookDTO>> saveAndUpdateBooks(@RequestBody List<BookDTO> bookDTOs) {
        List<BookDTO> savedBooks = bookService.saveAndUpdateBooks(bookDTOs);
        return ResponseEntity.ok(savedBooks);
    }

}
