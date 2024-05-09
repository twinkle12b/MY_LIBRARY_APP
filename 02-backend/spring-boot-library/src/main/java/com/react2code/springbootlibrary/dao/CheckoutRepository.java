package com.react2code.springbootlibrary.dao;

import com.react2code.springbootlibrary.entity.Checkout;
import org.hibernate.annotations.Check;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    Checkout findByUserEmailAndBookId(String userEmail, Long bookId);


    List<Checkout> findByUserEmail(String userEmail);
}
