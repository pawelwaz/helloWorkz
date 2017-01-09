package org.pawelwaz.helloworkz.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "membership")
public class Membership {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long workgroup;
    private Long hellouser;
    private String title;
    private String description;
    
    @OneToOne
    @JoinColumn(name = "workgroup")
    private Group memberGroup;
    
    public Membership(Long workgroup, Long hellouser, String title, String description) {
        this.workgroup = workgroup;
        this.hellouser = hellouser;
        this.title = title;
        this.description = description;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
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
     * @return the memberGroup
     */
    public Group getMemberGroup() {
        return memberGroup;
    }

    /**
     * @param memberGroup the memberGroup to set
     */
    public void setMemberGroup(Group memberGroup) {
        this.memberGroup = memberGroup;
    }
}
