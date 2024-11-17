package com.hemanshu.slackmessage.controller;

import com.hemanshu.slackmessage.dto.UserDetailsDTO;
import com.hemanshu.slackmessage.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/get-saved-details/{phoneNumber}")
    public ResponseEntity<UserDetailsDTO> getMessages(@PathVariable String phoneNumber) {
       return ResponseEntity.ok(messageService.getSavedFormDetails(phoneNumber));
    }

    @PostMapping("/send-message/{channelName}")
    public ResponseEntity<Void> sendMessage(@PathVariable String channelName, @RequestBody UserDetailsDTO userDetails) {
        messageService.sendMessage(channelName,userDetails);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
