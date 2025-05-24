package com.enkel.library.book;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBookResponse {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private boolean returned;
    private boolean returnApproved;
}
