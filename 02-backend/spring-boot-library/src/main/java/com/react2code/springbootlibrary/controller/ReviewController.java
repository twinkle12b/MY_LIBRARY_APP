package com.react2code.springbootlibrary.controller;

import com.react2code.springbootlibrary.entity.ReviewRequest;
import com.react2code.springbootlibrary.service.ReviewService;
import com.react2code.springbootlibrary.utils.ExtractJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;
    public static final String email = "\"sub\"";

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/secure/postReviewByUser")
    public void postReview(@RequestHeader(value="Authorization") String token,
                          @RequestBody ReviewRequest reviewRequest) throws Exception {
        String userEmail = ExtractJwt.extractJwtPayload(token, email);
        if(userEmail == null) {
            throw new Exception("User Email Missing");
        }
        reviewService.postReview(userEmail, reviewRequest);
    }

    @GetMapping("/secure/isReviewListed")
    public Boolean isUserReviewListed(@RequestParam Long bookId,
                                          @RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJwt.extractJwtPayload(token, email);
        if(userEmail == null) {
            throw new Exception("User Email Missing");
        }
        return reviewService.userReviewListed(userEmail, bookId);
    }
}
