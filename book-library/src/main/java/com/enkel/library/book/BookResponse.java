package com.enkel.library.book;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private Set<Author> authors;
    private String isbn;
    private String description;
    private byte[] cover;
    private Integer quantity;
}
