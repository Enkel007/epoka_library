package com.enkel.library.book;

import com.enkel.library.author.Author;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> withAuthorId(Integer authorId) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Author> authorJoin = root.join("authors");
            return criteriaBuilder.equal(authorJoin.get("author").get("id"), authorId);
        };
    }

    public static Specification<Book> withCategory(Category category) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isMember(category, root.get("category"));
        };
    }
}
