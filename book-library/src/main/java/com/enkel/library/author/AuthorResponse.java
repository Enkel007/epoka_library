package com.enkel.library.author;

import com.enkel.library.book.Book;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private Set<Book> books;
}
