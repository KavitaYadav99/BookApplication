package com.books.dto;

import com.books.entities.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long bookId;
    private String title;
    private String authorName;
    private String categoryName;
    private Long isbn;
    private String publicationYear;

    public static BookDTO toBookDto(BookEntity bookEntity) {
        BookDTO.BookDTOBuilder builder = BookDTO.builder()
                .bookId(bookEntity.getBookId())
                .title(bookEntity.getTitle())
                .isbn(bookEntity.getIsbn())
                .publicationYear(bookEntity.getPublicationYear())
                .authorName(bookEntity.getAuthorEntity() != null ? bookEntity.getAuthorEntity().getAuthorName() : null)
                .categoryName(bookEntity.getCategoryEntity() != null ? bookEntity.getCategoryEntity().getCategoryName() : null);

        return builder.build();
    }

}
