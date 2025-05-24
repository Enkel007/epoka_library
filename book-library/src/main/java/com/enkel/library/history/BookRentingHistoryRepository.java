package com.enkel.library.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

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
    Page<BookRentingHistory> findAllReturnedBooks(Pageable pageable);

    @Query("""
            SELECT
            (COUNT(*) > 0) AS isBorrowed
            FROM BookRentingHistory bookRentingHistory
            WHERE bookRentingHistory.user.id = :userId
            AND bookRentingHistory.book.id = :bookId
            AND bookRentingHistory.returnApproved = false
    """)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

    @Query("""
           SELECT transaction 
           FROM BookRentingHistory transaction
           WHERE transaction.user.id = :userId
           AND transaction.book.id = :bookId
           AND transaction.returned = false
           AND transaction.returnApproved = false
           """)
    Optional<BookRentingHistory> findByBookIdAndUserId(Integer bookId, Integer userId);

    @Query("""
             SELECT transaction 
           FROM BookRentingHistory transaction
           WHERE transaction.user.id = :librarianId
           AND transaction.book.id = :bookId
           AND transaction.returned = true
           AND transaction.returnApproved = false
          """)
    Optional<BookRentingHistory> findByBookIdAndLibrarianId(Integer bookId, Integer librarianId);

    @Query("""
            SELECT transaction
            FROM BookRentingHistory transaction
            WHERE transaction.book.id = :bookId
            AND transaction.returned = false
            AND transaction.returnApproved = false
            """)
    List<BookRentingHistory> findByBookIdAndReturnedApprovedFalseAndReturnedFalse(Integer bookId);
}
