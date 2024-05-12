package com.react2code.springbootlibrary.service;


import com.react2code.springbootlibrary.dao.ReviewRepository;
import com.react2code.springbootlibrary.entity.Review;
import com.react2code.springbootlibrary.entity.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {


    private ReviewRepository reviewRepository;


    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, reviewRequest.getBookId());

        if(validateReview != null) {
            throw new Exception("Review already been created");
        }
        Review reviewBE = new Review();
        reviewBE.setBookId(reviewRequest.getBookId());
        reviewBE.setRating(reviewRequest.getRating());
        reviewBE.setUserEmail(userEmail);
        if(reviewRequest.getReviewDescription().isPresent()) {
            reviewBE.setReviewDescription(reviewRequest.getReviewDescription()
                    .map(Object::toString).orElse(null));
        }
        reviewBE.setDate(Date.valueOf(LocalDate.now()));
        reviewRepository.save(reviewBE);
    }

    public Boolean userReviewListed(String userEmail, Long bookId) {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);
        return validateReview != null;
    }
}
