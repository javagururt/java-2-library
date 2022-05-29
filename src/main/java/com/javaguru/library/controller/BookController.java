package com.javaguru.library.controller;

import com.javaguru.library.dto.BookDto;
import com.javaguru.library.dto.CreateBookDto;
import com.javaguru.library.dto.ErrorResponseDto;
import com.javaguru.library.service.BookService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/books")
class BookController {

    private final BookService bookService;

    @PostMapping
    ResponseEntity<BookDto> create(@RequestBody @Valid CreateBookDto bookDto, UriComponentsBuilder builder) {
        log.info("Received create book request: {}", bookDto);
        var result = bookService.save(bookDto);
        var uri = builder
                .path("/books/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(result);
    }

    @GetMapping("/{id}")
    BookDto findById(@PathVariable String id) {
        log.info("Received find book by id request, id: {}", id);
        return bookService.findBookById(id);
    }

    @GetMapping
    List<BookDto> findAll() {
        log.info("Received find all books request");
        return bookService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        log.info("Received delete book by id request, id: {}", id);
        bookService.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @RequestBody @Valid BookDto bookDto) {
        log.info("Received update book request, id: {}, request: {}", id, bookDto);
        bookService.update(id, bookDto);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handle(IllegalArgumentException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handle(MethodArgumentNotValidException e) {
        var errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(field -> String.format("Field validation failed. Field: %s, message: %s",
                        field.getField(), field.getDefaultMessage()))
                .toList();
        return new ErrorResponseDto(errorMessages.toString());
    }
}
