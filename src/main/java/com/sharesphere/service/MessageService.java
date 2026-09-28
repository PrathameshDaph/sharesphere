package com.sharesphere.service;

import com.sharesphere.dto.request.MessageRequest;
import com.sharesphere.dto.response.ConversationResponse;
import com.sharesphere.dto.response.MessageResponse;
import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(MessageRequest request, Long senderId);
    List<MessageResponse> getMessages(Long conversationId, Long currentUserId);
    List<ConversationResponse> getMyConversations(Long userId);
    ConversationResponse getOrCreateConversation(Long userId1, Long userId2);
}
