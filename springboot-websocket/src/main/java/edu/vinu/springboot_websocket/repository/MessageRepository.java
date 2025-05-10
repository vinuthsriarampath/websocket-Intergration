package edu.vinu.springboot_websocket.repository;

import edu.vinu.springboot_websocket.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity,Long> {

    @Query("SELECT m FROM MessageEntity m WHERE " + "(m.sender = :user1 AND m.recipient = :user2) OR " + "(m.sender = :user2 AND m.recipient = :user1) " + "ORDER BY m.timestamp ASC")
    List<MessageEntity> findMessagesBetweenUsers(@Param("user1") String user1, @Param("user2") String user2);
}
