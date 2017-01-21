package org.pawelwaz.helloworkz.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "invitation")
public class Invitation implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long hellouser;
    private Long sender;
    private Long workgroup;

    @OneToOne
    @JoinColumn(name = "workgroup")
    private Group groupJoin;
    
    @OneToOne
    @JoinColumn(name = "hellouser")
    private HelloUser receiverJoin;
    
    @OneToOne
    @JoinColumn(name = "sender")
    private HelloUser senderJoin;
    
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
     * @return the hellouser
     */
    public Long getHellouser() {
        return hellouser;
    }

    /**
     * @param hellouser the hellouser to set
     */
    public void setHellouser(Long hellouser) {
        this.hellouser = hellouser;
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
     * @return the workgroup
     */
    public Long getWorkgroup() {
        return workgroup;
    }

    /**
     * @param workgroup the workgroup to set
     */
    public void setWorkgroup(Long workgroup) {
        this.workgroup = workgroup;
    }

    /**
     * @return the groupJoin
     */
    public Group getGroupJoin() {
        return groupJoin;
    }

    /**
     * @param groupJoin the groupJoin to set
     */
    public void setGroupJoin(Group groupJoin) {
        this.groupJoin = groupJoin;
    }

    /**
     * @return the senderJoin
     */
    public HelloUser getSenderJoin() {
        return senderJoin;
    }

    /**
     * @param senderJoin the senderJoin to set
     */
    public void setSenderJoin(HelloUser senderJoin) {
        this.senderJoin = senderJoin;
    }

    /**
     * @return the receiverJoin
     */
    public HelloUser getReceiverJoin() {
        return receiverJoin;
    }

    /**
     * @param receiverJoin the receiverJoin to set
     */
    public void setReceiverJoin(HelloUser receiverJoin) {
        this.receiverJoin = receiverJoin;
    }
    
}
