package com.javaguru.library.service;

import com.javaguru.library.domain.BookEntity;
import com.javaguru.library.dto.BookDto;
import com.javaguru.library.dto.CreateBookDto;
import com.javaguru.library.repository.BookRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookDto save(CreateBookDto bookDto) {
        var entity = convert(bookDto);
        bookRepository.save(entity);
        return convert(entity);
    }

    public BookDto findBookById(String id) {
        return bookRepository.findById(id)
                .map(this::convert)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Book with id '%s' is not found", id)));
    }

    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(this::convert)
                .toList();
    }

    public void delete(String id) {
        bookRepository.deleteById(id);
    }

    public void update(String id, BookDto dto) {
        bookRepository.findById(id)
                .map(entity -> updateFields(entity, dto))
                .map(bookRepository::save)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Book with id '%s' is not found", id)));
    }

    private BookEntity convert(CreateBookDto dto) {
        return BookEntity.builder()
                .id(UUID.randomUUID().toString())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .price(dto.getPrice())
                .build();
    }

    private BookDto convert(BookEntity entity) {
        return new BookDto(entity.getId(), entity.getAuthor(), entity.getTitle(), entity.getPrice());
    }

    private BookEntity updateFields(BookEntity entity, BookDto dto) {
        entity.setAuthor(dto.getAuthor());
        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        return entity;
    }

}
