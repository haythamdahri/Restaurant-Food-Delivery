package org.restaurant.salado.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Haytham DAHRI
 */
@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "content")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "read")
    private boolean read;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private ChatMessageType messageType;

    @PrePersist
    private void prePersist() {
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "ChatMessage(id=" + this.id + ", sender=" + this.sender.getId() + ", receiver=" + this.receiver.getId() + ", content="
                + this.content +", timestamp=" + this.timestamp + ", read=" + this.read + ")";
    }

}
