package com.enkel.library.book;

import com.enkel.library.common.PageResponse;
import com.enkel.library.history.BookRentingHistory;
import com.enkel.library.history.BookRentingHistoryRepository;
import com.enkel.library.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookRentingHistoryRepository bookRentingHistoryRepository;

    public Integer save( BookRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(request);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(pageable);
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByAuthor(Integer authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withAuthorId(authorId), pageable);
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByCategory(Category category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withCategory(category), pageable);
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookRentingHistory> allBorrowedBooks = bookRentingHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size) {
        // todo check if the user is an admin/librarian as only them can access this
        // todo throw OperationNotPermittedException if the user is not an admin/librarian
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookRentingHistory> allBorrowedBooks = bookRentingHistoryRepository.findAllReturnedBooks(pageable);
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateAvailability(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        // todo check if the user is an admin/librarian as only them can access this
        // todo throw OperationNotPermittedException if the user is not an admin/librarian
        User user = ((User) connectedUser.getPrincipal());
        book.setAvailable(!book.isAvailable());
        bookRepository.save(book);
        return bookId;
    }
}
