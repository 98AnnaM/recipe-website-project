package bg.example.recepeWebsite.model.entity;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message extends BaseEntity {

    //id - Accepts UUID String or Long values
    //
    private LocalDateTime dateTime;
    private String textContent;
    private UserEntity author;
    private UserEntity recipient;

    public Message() {
    }

    @Column(nullable = false)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Lob
    @Column(nullable = false)
    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    @ManyToOne
    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    @ManyToOne
    public UserEntity getRecipient() {
        return recipient;
    }

    public void setRecipient(UserEntity recipient) {
        this.recipient = recipient;
    }
}
