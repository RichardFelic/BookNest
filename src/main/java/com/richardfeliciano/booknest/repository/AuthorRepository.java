package com.richardfeliciano.booknest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.richardfeliciano.booknest.model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) = LOWER(:name)")
    Optional<Author> findByNameSQL(@Param("name") String name);

    Optional<Author> findByName(String name);

    List<Author> findByBirthYearBetween(Integer start, Integer end);
}
