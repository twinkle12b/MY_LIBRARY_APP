package com.react2code.springbootlibrary.responseModels;

import com.react2code.springbootlibrary.entity.Book;
import lombok.Data;

@Data
public class ShelfCurrentLoanResponse {

    private Book book;
    private int daysLeft;

    public ShelfCurrentLoanResponse(Book book, int daysLeft) {
        this.book = book;
        this.daysLeft = daysLeft;
    }
}
