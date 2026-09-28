package com.sharesphere.controller;

import com.sharesphere.dto.request.MessageRequest;
import com.sharesphere.dto.response.*;
import com.sharesphere.entity.User;
import com.sharesphere.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> send(@Valid @RequestBody MessageRequest req,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(messageService.sendMessage(req, ((User)ud).getId()));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponse>> myConversations(@AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(messageService.getMyConversations(((User)ud).getId()));
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(messageService.getMessages(id, ((User)ud).getId()));
    }

    @GetMapping("/conversations/with/{userId}")
    public ResponseEntity<ConversationResponse> getOrCreate(@PathVariable Long userId,
            @AuthenticationPrincipal UserDetails ud) {
        return ResponseEntity.ok(messageService.getOrCreateConversation(((User)ud).getId(), userId));
    }
}
