package com.sharesphere.repository;

import com.sharesphere.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
        SELECT c FROM Conversation c
        WHERE (c.participant1.id = :userId OR c.participant2.id = :userId)
        ORDER BY c.lastMessageAt DESC NULLS LAST
        """)
    List<Conversation> findByParticipantId(@Param("userId") Long userId);

    @Query("""
        SELECT c FROM Conversation c
        WHERE (c.participant1.id = :user1Id AND c.participant2.id = :user2Id)
           OR (c.participant1.id = :user2Id AND c.participant2.id = :user1Id)
        """)
    Optional<Conversation> findByParticipants(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);
}
