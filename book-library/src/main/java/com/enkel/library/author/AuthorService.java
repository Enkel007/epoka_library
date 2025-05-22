package com.enkel.library.author;

import com.enkel.library.book.*;
import com.enkel.library.common.PageResponse;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    public Integer save(AuthorRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Author author = authorMapper.toAuthor(request);
        return authorRepository.save(author).getId();
    }

    @Transactional
    public void associateAuthorWithBook(Integer authorId, Integer bookId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with ID: " + authorId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));

        author.getBooks().add(book);
        book.getAuthors().add(author);

        bookRepository.save(book);
    }

    public AuthorResponse findAuthorById(Integer authorId) {
        return authorRepository.findById(authorId)
                .map(authorMapper::toAuthorResponse)
                .orElseThrow(() -> new EntityNotFoundException("Author not found!"));
    }

    public PageResponse<AuthorResponse> findAllAuthors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Author> authors = authorRepository.findAll(pageable);
        List<AuthorResponse> authorResponse = authors.stream()
                .map(authorMapper::toAuthorResponse)
                .toList();
        return new PageResponse<>(
                authorResponse,
                authors.getNumber(),
                authors.getSize(),
                authors.getTotalElements(),
                authors.getTotalPages(),
                authors.isFirst(),
                authors.isLast()
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
}
