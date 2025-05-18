package com.enkel.library.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @Query("""
            SELECT book from Book book
            JOIN book.favouritedByUsers users
            WHERE users.id = :userId
            """)
    Page<Book> findAllFavouritedByUserId(@Param("userId") Integer userId, Pageable pageable);
}
