package com.enkel.library.book;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> withAuthorId(Integer authorId) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Author> authorJoin = root.join("authors");
            return criteriaBuilder.equal(authorJoin.get("author").get("id"), authorId);
        };
    }
}
