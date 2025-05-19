package com.enkel.library.author;


import com.enkel.library.book.BookResponse;
import com.enkel.library.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authors")
@RequiredArgsConstructor
@Tag(name = "Author")
public class AuthorController {
    private final AuthorService service;

    @PostMapping
    public ResponseEntity<Integer> saveAuthor(@Valid @RequestBody AuthorRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @PostMapping("/{author-id}/books/{book-id}")
    public ResponseEntity<Void> associateAuthorWithBook(
            @PathVariable("author-id") Integer authorId,
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        service.associateAuthorWithBook(authorId, bookId, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<AuthorResponse>> findAllAuthors(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "0", required = false) int size
    ) {
        return ResponseEntity.ok(service.findAllAuthors(page, size));
    }


    @GetMapping("/{author-id}")
    public ResponseEntity<AuthorResponse> findAuthorById(@PathVariable("author-id") Integer authorId) {
        return ResponseEntity.ok(service.findAuthorById(authorId));
    }
}
