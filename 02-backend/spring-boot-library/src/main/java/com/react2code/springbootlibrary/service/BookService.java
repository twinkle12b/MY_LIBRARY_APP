package com.react2code.springbootlibrary.service;

import com.react2code.springbootlibrary.dao.BookRepository;
import com.react2code.springbootlibrary.dao.CheckoutRepository;
import com.react2code.springbootlibrary.entity.Book;
import com.react2code.springbootlibrary.entity.Checkout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    @Autowired
    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
    }

    //When user tries to check out a book
    public Book checkoutBook(String userEmail, Long bookId) throws Exception {
        //Find Book by ID, check if it is already checked out by user , if yes throw exception, if no reduce book copies
        // available by one and create new check out object for user
        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(book.isEmpty() || validateCheckout != null || book.get().getCopiesAvailable() <=0) {
            throw new Exception("Book doesn't exist or already checked out by user");
        }

       book.get().setCopiesAvailable( book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

        Checkout checkout = new Checkout(userEmail, LocalDate.now().toString(), LocalDate.now().plusDays(7).toString(), bookId);
        checkoutRepository.save(checkout);
        return book.get();
    }

    public  boolean isBookCheckedOutByUser(String userEmail, Long bookId) {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        return validateCheckout != null;
    }

    //countBooksCheckedByUser
    public int currentLoanCountOfUser(String userEmail) {
        return checkoutRepository.findByUserEmail(userEmail).size();
    }
}
