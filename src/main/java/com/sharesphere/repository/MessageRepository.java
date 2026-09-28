package com.sharesphere.repository;

import com.sharesphere.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation.id = :convId AND m.sender.id != :userId AND m.read = false")
    long countUnreadMessages(@Param("convId") Long convId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Message m SET m.read = true WHERE m.conversation.id = :convId AND m.sender.id != :userId")
    void markAllAsRead(@Param("convId") Long convId, @Param("userId") Long userId);
}
