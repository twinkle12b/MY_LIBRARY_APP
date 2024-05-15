package com.react2code.springbootlibrary.service;

import com.react2code.springbootlibrary.dao.BookRepository;
import com.react2code.springbootlibrary.dao.CheckoutRepository;
import com.react2code.springbootlibrary.dao.ReviewRepository;
import com.react2code.springbootlibrary.entity.Book;
import com.react2code.springbootlibrary.requestModel.AddBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AdminService {

    private BookRepository bookRepository;
    private ReviewRepository reviewRepository;
    private CheckoutRepository checkoutRepository;

    @Autowired
    public AdminService(BookRepository bookRepository, ReviewRepository reviewRepository, CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.checkoutRepository = checkoutRepository;
    }


    public void increaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);
        if(book.isEmpty()) {
            throw new Exception("Book is not present");
        }
        book.get().setCopies(book.get().getCopies() + 1);
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        bookRepository.save(book.get());

    }

    public void decreaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);
        if(book.isEmpty() || book.get().getCopies() <= 0 || book.get().getCopiesAvailable() <=  0) {
            throw new Exception("Book is not present");
        }
        book.get().setCopies(book.get().getCopies() - 1);
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

    }

    public void deleteBook(Long bookId) throws Exception {
       Optional<Book> book = bookRepository.findById(bookId);
        if(book.isEmpty() ) {
            throw new Exception("Book is not present");
        }
        bookRepository.delete(book.get());
        checkoutRepository.deleteAllByBookId(bookId);
        reviewRepository.deleteAllByBookId(bookId);


    }




    public void postBook(AddBookRequest addBookRequest)  {

        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setCategory(addBookRequest.getCategory());
        book.setImg(addBookRequest.getImg());
        bookRepository.save(book);

    }
}
