package com.react2code.springbootlibrary.controller;

import com.react2code.springbootlibrary.requestModel.AddBookRequest;
import com.react2code.springbootlibrary.service.AdminService;
import com.react2code.springbootlibrary.utils.ExtractJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;


    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/secure/add/book")
    public void createBook(@RequestHeader(value = "Authorization") String token,
                           @RequestBody AddBookRequest addBookRequest) throws Exception {
        String userType = ExtractJwt.extractJwtPayload(token, "\"userType\"");
        assert userType != null;
        if (userType == null || !userType.equals("admin")) {
            throw new Exception("Administration Page only");
        }
        adminService.postBook(addBookRequest);
    }

    @PutMapping("/secure/increase/bookQuantity")
    public void increaseBookQuantity(@RequestHeader(value = "Authorization") String token,
                         @RequestParam Long bookId) throws Exception {
        String userType = ExtractJwt.extractJwtPayload(token, "\"userType\"");
        assert userType != null;
        if (userType == null || !userType.equals("admin")) {
            throw new Exception("Administration Page only");
        }
        adminService.increaseBookQuantity(bookId);
    }

    @PutMapping("/secure/decrease/bookQuantity")
    public void decreaseBookQuantity(@RequestHeader(value = "Authorization") String token,
                                     @RequestParam Long bookId) throws Exception {
        String userType = ExtractJwt.extractJwtPayload(token, "\"userType\"");
        assert userType != null;
        if (userType == null || !userType.equals("admin")) {
            throw new Exception("Administration Page only");
        }
        adminService.decreaseBookQuantity(bookId);
    }

    @DeleteMapping("/secure/delete/book")
    public void deleteBook(@RequestHeader(value = "Authorization") String token,
                                     @RequestParam Long bookId) throws Exception {
        String userType = ExtractJwt.extractJwtPayload(token, "\"userType\"");
        assert userType != null;
        if (userType == null || !userType.equals("admin")) {
            throw new Exception("Administration Page only");
        }
        adminService.deleteBook(bookId);
    }
}

