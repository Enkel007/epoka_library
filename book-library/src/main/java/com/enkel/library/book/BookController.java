package com.enkel.library.book;

import com.enkel.library.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {
    private final BookService service;

    @PostMapping
    public ResponseEntity<Integer> saveBook(@Valid @RequestBody BookRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Integer bookId) {
        return ResponseEntity.ok(service.findById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "0", required = false) int size
    ) {
        return ResponseEntity.ok(service.findAllBooks(page, size));
    }

    @GetMapping("/author/{author-id}")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByAuthor(
            @PathVariable("author-id") Integer authorId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "0", required = false) int size
    ){
        return ResponseEntity.ok(service.findAllBooksByAuthor(page, size, authorId));
    }

    @GetMapping("/category/{category-name}")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByCategory(
            @PathVariable("category-name") String categoryName,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "0", required = false) int size
    ){
       try {
           Category category = Category.valueOf(categoryName.toUpperCase().replace("-", "_"));
           return ResponseEntity.ok(service.findAllBooksByCategory(category, page, size));
       } catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().build();
       }
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "0", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "0", required = false) int size
    ){
        return ResponseEntity.ok(service.findAllReturnedBooks(page, size));
    }

    @PatchMapping("/available/{book-id}")
    public ResponseEntity<Integer> updateAvailability(
            @PathVariable ("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.updateAvailability(bookId, connectedUser));
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(@PathVariable("book-id") Integer bookId, Authentication connectedUser) {
        return ResponseEntity.ok(service.borrowBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBorrowedBook(
            @PathVariable("book-id") Integer bookId, Authentication connectedUser) {
        return ResponseEntity.ok(service.returnBorrowedBook(bookId, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approveReturnedBorrowedBook(
            @PathVariable("book-id") Integer bookId, Authentication connectedUser) {
        return ResponseEntity.ok(service.approveReturnedBorrowedBook(bookId, connectedUser));
    }
}
