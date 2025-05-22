package com.enkel.library.author;

import com.enkel.library.book.Book;
import com.enkel.library.book.BookResponse;
import com.enkel.library.file.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthorMapper {
    public Author toAuthor(AuthorRequest request){
        return Author.builder()
                .id(request.id())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .books(request.books())
                .build();
    }

    public AuthorResponse toAuthorResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .books(author.getBooks())
                .build();
    }

}
