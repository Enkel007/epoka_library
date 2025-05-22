package com.enkel.library.author;

import com.enkel.library.book.Book;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record AuthorRequest(
        Integer id,

        @NotNull(message = "200")
        @NotEmpty(message = "200")
        String firstName,

        @NotNull(message = "201")
        @NotEmpty(message = "201")
        String lastName,

        Set<Book> books
){}
