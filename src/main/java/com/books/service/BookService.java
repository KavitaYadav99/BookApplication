package com.books.service;

import com.books.dto.AuthorDTO;
import com.books.dto.BookDTO;
import com.books.dto.CategoryDTO;
import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();

    BookDTO getBookById(Long bookId);

    List<AuthorDTO> getAllAuthors();

    AuthorDTO getAuthorById(Long authorId);

    List<CategoryDTO> getAllCategoriesWithBooks();

    List<BookDTO> getBooksByCategory(String categoryName);

    List<BookDTO> saveAndUpdateBooks(List<BookDTO> bookDTOs);

}

