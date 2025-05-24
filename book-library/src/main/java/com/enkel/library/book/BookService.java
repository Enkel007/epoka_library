package com.enkel.library.book;

import com.enkel.library.common.PageResponse;
import com.enkel.library.exception.OperationNotPermittedException;
import com.enkel.library.file.FileStorageService;
import com.enkel.library.history.BookRentingHistory;
import com.enkel.library.history.BookRentingHistoryRepository;
import com.enkel.library.user.User;
import com.enkel.library.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookRentingHistoryRepository bookRentingHistoryRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

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

//    public PageResponse<BookResponse> findAllBooksByAuthor(Integer authorId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
//        Page<Book> books = bookRepository.findAll(BookSpecification.withAuthorId(authorId), pageable);
//        List<BookResponse> bookResponse = books.stream()
//                .map(bookMapper::toBookResponse)
//                .toList();
//        return new PageResponse<>(
//                bookResponse,
//                books.getNumber(),
//                books.getSize(),
//                books.getTotalElements(),
//                books.getTotalPages(),
//                books.isFirst(),
//                books.isLast()
//        );
//    }

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

    public PageResponse<BookResponse> findFavouriteBooksByUser(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> favouriteBooksPage = bookRepository.findAllFavouritedByUserId(user.getId(), pageable);
        List<BookResponse> bookResponse = favouriteBooksPage.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                favouriteBooksPage.getNumber(),
                favouriteBooksPage.getSize(),
                favouriteBooksPage.getTotalElements(),
                favouriteBooksPage.getTotalPages(),
                favouriteBooksPage.isFirst(),
                favouriteBooksPage.isLast()
        );
    }

    @Transactional
    public Integer addBookToFavourites(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        User user = ((User) connectedUser.getPrincipal());
        user.getFavouriteBooks().add(book);
        userRepository.save(user);
        return bookId;
    }

    @Transactional
    public Integer removeBookFromFavourites(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        User user = ((User) connectedUser.getPrincipal());
        user.getFavouriteBooks().remove(book);
        userRepository.save(user);
        return bookId;
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

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if(!book.isAvailable()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed as it is not currently available");
        }
        User user = ((User) connectedUser.getPrincipal());
        final boolean isAlreadyBorrowed = bookRentingHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book is already borrowed.");
        }
        BookRentingHistory bookRentingHistory = BookRentingHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookRentingHistoryRepository.save(bookRentingHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if(!book.isAvailable()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed as it is not currently available");
        }
        User user = ((User) connectedUser.getPrincipal());
        BookRentingHistory bookRentingHistory = bookRentingHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book!"));
        bookRentingHistory.setReturned(true);
        return bookRentingHistoryRepository.save(bookRentingHistory).getId();
    }


    public Integer approveReturnedBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        if(!book.isAvailable()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed as it is not currently available");
        }
        User user = ((User) connectedUser.getPrincipal());
        BookRentingHistory bookRentingHistory = bookRentingHistoryRepository.findByBookIdAndLibrarianId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book has not been returned yet! You cannot approve the return."));
        bookRentingHistory.setReturnApproved(true);
        return bookRentingHistoryRepository.save(bookRentingHistory).getId();
    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        User user = ((User) connectedUser.getPrincipal());
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }
}
