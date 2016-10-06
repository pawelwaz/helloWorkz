package org.pawelwaz.helloworkz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "message")
public class Message {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sender;
    private Long receiver;
    private int seen;
    @Column(name = "sendtime", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp sendTime;
    private String content;

    @PrePersist
    protected void onCreate() {
        this.sendTime = new Timestamp(0);
    }
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the sender
     */
    public Long getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(Long sender) {
        this.sender = sender;
    }

    /**
     * @return the receiver
     */
    public Long getReceiver() {
        return receiver;
    }

    /**
     * @param receiver the receiver to set
     */
    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the seen
     */
    public int getSeen() {
        return seen;
    }

    /**
     * @param seen the seen to set
     */
    public void setSeen(int seen) {
        this.seen = seen;
    }

    /**
     * @return the sendTime
     */
    public Timestamp getSendTime() {
        return sendTime;
    }

    /**
     * @param sendTime the sendTime to set
     */
    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }
    
}
