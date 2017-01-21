package org.pawelwaz.helloworkz.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "workgroup")
public class Group implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String group_name;
    private String description;
    private int tasknumber;

    public Group(String group_name, String description) {
        this.group_name = group_name;
        this.description = description;
        this.tasknumber = 0;
    }
    
    @Override
    public String toString() {
        return this.group_name;
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
     * @return the group_name
     */
    public String getGroup_name() {
        return group_name;
    }

    /**
     * @param group_name the group_name to set
     */
    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the tasknumber
     */
    public int getTasknumber() {
        return tasknumber;
    }

    /**
     * @param tasknumber the tasknumber to set
     */
    public void setTasknumber(int tasknumber) {
        this.tasknumber = tasknumber;
    }
}
