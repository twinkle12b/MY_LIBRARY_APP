package com.react2code.springbootlibrary.entity;

import lombok.Data;

import java.util.Optional;

@Data
public class ReviewRequest {

    private Double rating;

    private Long bookId;

    private Optional<String> reviewDescription;
}
