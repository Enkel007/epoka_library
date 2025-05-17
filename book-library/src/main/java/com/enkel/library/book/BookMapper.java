package com.enkel.library.book;

import com.enkel.library.history.BookRentingHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .isbn(request.isbn())
                .authors(request.authors())
                .description(request.description())
                .bookCover(request.bookCover())
                .quantity(request.quantity())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .authors(book.getAuthors())
                .description(book.getDescription())
                // todo implement book cover
                //.bookCover(book.getBookCover())
                .quantity(book.getQuantity())
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookRentingHistory history) {
        return BorrowedBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .isbn(history.getBook().getIsbn())
                .authors(history.getBook().getAuthors())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
