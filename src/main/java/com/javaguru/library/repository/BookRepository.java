package com.javaguru.library.repository;

import com.javaguru.library.domain.BookEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, String> {
}
