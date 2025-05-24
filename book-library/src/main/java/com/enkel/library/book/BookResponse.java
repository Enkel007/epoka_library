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
    private String author;
    private String isbn;
    private String description;
    private Set<Category> categories;
    private byte[] cover;
    private boolean available;
}
