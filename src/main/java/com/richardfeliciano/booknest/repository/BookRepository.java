package com.richardfeliciano.booknest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.richardfeliciano.booknest.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) = LOWER(:title)")
    Optional<Book> findByTitleSQL(@Param("title") String title);
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findTop10ByOrderByDownloadCountDesc();
    List<Book> findByGenres(String genre);
    List<Book> findBookByLanguages(String languages);

}
