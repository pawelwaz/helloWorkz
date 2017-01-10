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
@Table(name = "contact")
public class Contact implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long owner;
    private Long person;
    
    @OneToOne
    @JoinColumn(name = "person")
    private HelloUser contactPerson;

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
     * @return the owner
     */
    public Long getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Long owner) {
        this.owner = owner;
    }

    /**
     * @return the person
     */
    public Long getPerson() {
        return person;
    }

    /**
     * @param person the person to set
     */
    public void setPerson(Long person) {
        this.person = person;
    }

    /**
     * @return the contactPerson
     */
    public HelloUser getContactPerson() {
        return contactPerson;
    }

    /**
     * @param contactPerson the contactPerson to set
     */
    public void setContactPerson(HelloUser contactPerson) {
        this.contactPerson = contactPerson;
    }
    
}
