package com.react2code.springbootlibrary.controller;


import com.react2code.springbootlibrary.entity.Message;
import com.react2code.springbootlibrary.service.MessagesService;
import com.react2code.springbootlibrary.utils.ExtractJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin("http://localhost:3000")
public class MessagesController {


    private MessagesService messagesService;

    public static final String email = "\"sub\"";

    @Autowired
    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }


    @PostMapping("/secure/add/message")
    public void createMessages(@RequestHeader(value = "Authorization") String token,
                               @RequestBody Message message) {
        String userEmail = ExtractJwt.extractJwtPayload(token, email);
        messagesService.postMessage(message, userEmail);

    }
}
