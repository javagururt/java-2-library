package com.javaguru.library.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDto {

    @NotBlank
    @Length(min = 3, max = 10)
    private String author;
    @NotBlank
    @Length(min = 3, max = 10)
    private String title;
    @NotNull
    @Range(min = 1, max = 200)
    private BigDecimal price;

}
