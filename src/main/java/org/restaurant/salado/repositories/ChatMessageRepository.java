package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm from ChatMessage as cm where (cm.sender.id = :senderId and cm.receiver.id = :receiverId) or " +
            "(cm.sender.id = :receiverId and cm.receiver.id = :senderId)")
    List<ChatMessage> searchRoomChatMessages(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

}
