package com.enkel.library.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRentingHistoryRepository extends JpaRepository<BookRentingHistory, Integer> {
    @Query("""
            SELECT history
            FROM BookRentingHistory history
            WHERE history.user.id = :userId
            """)
    Page<BookRentingHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("""
            SELECT history
            FROM BookRentingHistory history
            WHERE history.returned = true
            """)
    Page<BookRentingHistory> findAllReturnedBooks(Pageable pageable, Integer userId);
}
