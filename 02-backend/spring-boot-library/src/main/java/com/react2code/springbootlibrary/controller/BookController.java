package com.react2code.springbootlibrary.controller;

import com.react2code.springbootlibrary.entity.Book;
import com.react2code.springbootlibrary.service.BookService;
import com.react2code.springbootlibrary.utils.ExtractJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    public static final String email = "\"sub\"";

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;

    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestParam Long bookId,
                             @RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJwt.extractJwtPayload(token, email);
       return bookService.checkoutBook(userEmail, bookId);
    }

    @GetMapping("/secure/isBookCheckedOutByUser")
    public Boolean isBookCheckedOutByUser(@RequestParam Long bookId,
                                          @RequestHeader(value = "Authorization") String token)  {
        String userEmail = ExtractJwt.extractJwtPayload(token, email);
        return bookService.isBookCheckedOutByUser(userEmail, bookId);
    }

    @GetMapping("/secure/currentLoanCount")
    public int currentLoanCountByUser(@RequestHeader(value = "Authorization") String token)  {
        String userEmail = ExtractJwt.extractJwtPayload(token, email);
        return bookService.currentLoanCountOfUser(userEmail);
    }

}
