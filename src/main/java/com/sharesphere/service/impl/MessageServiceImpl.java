package com.sharesphere.service.impl;

import com.sharesphere.dto.request.MessageRequest;
import com.sharesphere.dto.response.*;
import com.sharesphere.entity.*;
import com.sharesphere.exception.*;
import com.sharesphere.repository.*;
import com.sharesphere.service.MessageService;
import com.sharesphere.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final UserServiceImpl userService;

    @Override @Transactional
    public MessageResponse sendMessage(MessageRequest req, Long senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User receiver = userRepository.findById(req.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));
        Conversation conv = conversationRepository.findByParticipants(senderId, req.getReceiverId())
                .orElseGet(() -> conversationRepository.save(
                        Conversation.builder().participant1(sender).participant2(receiver).build()));
        Message msg = Message.builder().conversation(conv).sender(sender).content(req.getContent()).build();
        msg = messageRepository.save(msg);
        conv.setLastMessageAt(LocalDateTime.now());
        conversationRepository.save(conv);
        notificationService.sendNotification(req.getReceiverId(),
                "New Message from " + sender.getName(), req.getContent().length() > 50
                        ? req.getContent().substring(0, 50) + "..." : req.getContent(),
                NotificationType.NEW_MESSAGE, conv.getId(), "CONVERSATION");
        return toMsgResponse(msg);
    }

    @Override @Transactional
    public List<MessageResponse> getMessages(Long conversationId, Long currentUserId) {
        messageRepository.markAllAsRead(conversationId, currentUserId);
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId)
                .stream().map(this::toMsgResponse).toList();
    }

    @Override
    public List<ConversationResponse> getMyConversations(Long userId) {
        return conversationRepository.findByParticipantId(userId)
                .stream().map(c -> toConvResponse(c, userId)).toList();
    }

    @Override
    public ConversationResponse getOrCreateConversation(Long userId1, Long userId2) {
        User u1 = userRepository.findById(userId1).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User u2 = userRepository.findById(userId2).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Conversation conv = conversationRepository.findByParticipants(userId1, userId2)
                .orElseGet(() -> conversationRepository.save(Conversation.builder().participant1(u1).participant2(u2).build()));
        return toConvResponse(conv, userId1);
    }

    private MessageResponse toMsgResponse(Message m) {
        return MessageResponse.builder()
                .id(m.getId()).conversationId(m.getConversation().getId())
                .sender(userService.toResponse(m.getSender()))
                .content(m.getContent()).read(m.isRead()).sentAt(m.getSentAt()).build();
    }

    private ConversationResponse toConvResponse(Conversation c, Long currentUserId) {
        List<Message> msgs = messageRepository.findByConversationIdOrderBySentAtAsc(c.getId());
        MessageResponse lastMsg = msgs.isEmpty() ? null : toMsgResponse(msgs.get(msgs.size() - 1));
        long unread = messageRepository.countUnreadMessages(c.getId(), currentUserId);
        return ConversationResponse.builder()
                .id(c.getId())
                .participant1(userService.toResponse(c.getParticipant1()))
                .participant2(userService.toResponse(c.getParticipant2()))
                .lastMessage(lastMsg).unreadCount(unread)
                .lastMessageAt(c.getLastMessageAt()).createdAt(c.getCreatedAt()).build();
    }
}
