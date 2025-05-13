package com.enkel.library.book;

import com.enkel.library.common.BaseEntity;
import com.enkel.library.history.BookRentingHistory;
import com.enkel.library.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String isbn;

    private String description;
    private String bookCover;
    private Integer quantity;


    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @ElementCollection(targetClass = Category.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "book_categories", joinColumns = @JoinColumn(name = "book_id"))
    @Enumerated(EnumType.STRING)
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(mappedBy = "favouriteBooks", fetch = FetchType.LAZY)
    private Set<User> favoritedByUsers = new HashSet<>();

    @OneToMany(mappedBy = "book")
    private List<BookRentingHistory> histories;
}
