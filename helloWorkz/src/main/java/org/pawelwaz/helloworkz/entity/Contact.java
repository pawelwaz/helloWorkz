package org.pawelwaz.helloworkz.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contact")
public class Contact {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long owner;
    private Long person;

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
    
}
