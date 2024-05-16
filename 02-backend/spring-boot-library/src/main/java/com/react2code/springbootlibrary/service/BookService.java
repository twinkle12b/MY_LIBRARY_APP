package com.react2code.springbootlibrary.service;

import com.react2code.springbootlibrary.dao.BookRepository;
import com.react2code.springbootlibrary.dao.CheckoutRepository;
import com.react2code.springbootlibrary.dao.HistoryRepository;
import com.react2code.springbootlibrary.dao.PaymentRepository;
import com.react2code.springbootlibrary.entity.Book;
import com.react2code.springbootlibrary.entity.Checkout;
import com.react2code.springbootlibrary.entity.History;
import com.react2code.springbootlibrary.entity.Payment;
import com.react2code.springbootlibrary.responseModels.ShelfCurrentLoanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;

    private PaymentRepository paymentRepository;

    private CheckoutRepository checkoutRepository;

    private HistoryRepository historyRepository;


    @Autowired
    public BookService(BookRepository bookRepository,
                       CheckoutRepository checkoutRepository, HistoryRepository historyRepository,
                       PaymentRepository paymentRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository = historyRepository;
        this.paymentRepository=paymentRepository;
    }

    //When user tries to check out a book
    public Book checkoutBook(String userEmail, Long bookId) throws Exception {
        //Find Book by ID, check if it is already checked out by user , if yes throw exception, if no reduce book copies
        // available by one and create new check out object for user
        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (book.isEmpty() || validateCheckout != null || book.get().getCopiesAvailable() <= 0) {
            throw new Exception("Book doesn't exist or already checked out by user");
        }

        List<Checkout> booksCheckOutByUser = checkoutRepository.findByUserEmail(userEmail);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean booksNeedToBeReturned = false;


        for(Checkout checkout : booksCheckOutByUser) {
         Date d1 = sdf.parse(checkout.getReturnDate());
         Date d2 = sdf.parse(LocalDate.now().toString());
         TimeUnit timeUnit = TimeUnit.DAYS;
            long difference_in_time = timeUnit.convert(d1.getTime() - d2.getTime(),
                    TimeUnit.MILLISECONDS);
            if(difference_in_time < 0) {
                booksNeedToBeReturned = true;
                break;
            }
        }
        Payment payment = paymentRepository.findByUserEmail(userEmail);
        if((payment != null && payment.getAmount() > 0) || (payment!= null && booksNeedToBeReturned)) {
            throw new Exception("OutStanding Fees");
        }
        if(payment== null) {
            Payment payment1 = new Payment();
            payment1.setAmount(00.00);
            payment1.setUserEmail(userEmail);
            paymentRepository.save(payment1);

        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

        Checkout checkout = new Checkout(userEmail, LocalDate.now().toString(), LocalDate.now().plusDays(7).toString(), bookId);
        checkoutRepository.save(checkout);
        return book.get();
    }

    public boolean isBookCheckedOutByUser(String userEmail, Long bookId) {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        return validateCheckout != null;
    }

    //countBooksCheckedByUser
    public int currentLoanCountOfUser(String userEmail) {
        return checkoutRepository.findByUserEmail(userEmail).size();
    }


    public List<ShelfCurrentLoanResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoanResponse> shelfCurrentLoanResponseList = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findByUserEmail(userEmail);

        List<Long> bookIdList = new ArrayList<>();
        checkoutList.forEach(checkout ->
                bookIdList.add(checkout.getBookId()));
        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Book book : books) {
            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(checkout1 -> checkout1.getBookId().equals(book.getId()))
                    .findFirst();
            if (checkout.isPresent()) {
                Date d1 = simpleDateFormat.parse(checkout.get().getReturnDate());
                Date d2 = simpleDateFormat.parse(LocalDate.now().toString());

                TimeUnit timeUnit = TimeUnit.DAYS;
                long difference_in_time = timeUnit.convert(d1.getTime() - d2.getTime(),
                        TimeUnit.MILLISECONDS);
                shelfCurrentLoanResponseList.add(new ShelfCurrentLoanResponse(book, (int) difference_in_time));
            }
        }
        return shelfCurrentLoanResponseList;
    }

    public void returnBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (book.isEmpty() || validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        bookRepository.save(book.get());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(validateCheckout.getReturnDate());
        Date d2 = sdf.parse(LocalDate.now().toString());

        TimeUnit timeUnit = TimeUnit.DAYS;
        long difference_in_time = timeUnit.convert(d1.getTime() - d2.getTime(),
                TimeUnit.MILLISECONDS);
        if(difference_in_time < 0) {
            Payment payment = paymentRepository.findByUserEmail(userEmail);
            payment.setAmount(payment.getAmount() + (difference_in_time * -1));
            paymentRepository.save(payment);
        }

        checkoutRepository.deleteById(validateCheckout.getId());
        History history = new History(userEmail,
                validateCheckout.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg());
        historyRepository.save(history);

    }

    public void renewLoan(String userEmail, Long bookId) throws Exception {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(validateCheckout.getReturnDate());
        Date d2 = sdf.parse(LocalDate.now().toString());

        if (d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0) {
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validateCheckout);
        }
    }


}
