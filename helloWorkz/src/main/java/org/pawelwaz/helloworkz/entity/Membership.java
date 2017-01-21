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
@Table(name = "membership")
public class Membership implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long workgroup;
    private Long hellouser;
    private int managment;
    private int users;
    private int tasks;
    private int discussions;
    private String title;
    private String description;
    private int active;
    
    @OneToOne
    @JoinColumn(name = "workgroup")
    private Group memberGroup;
    
    @OneToOne
    @JoinColumn(name = "hellouser")
    private HelloUser memberUser;

    public Membership() {
    }
    
    public Membership(Long workgroup, Long hellouser, String title, String description, int managment, int users, int tasks, int discussions) {
        this.workgroup = workgroup;
        this.hellouser = hellouser;
        this.title = title;
        this.description = description;
        this.managment = managment;
        this.users = users;
        this.tasks = tasks;
        this.discussions = discussions;
        this.active = 1;
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

    /**
     * @return the memberUser
     */
    public HelloUser getMemberUser() {
        return memberUser;
    }

    /**
     * @param memberUser the memberUser to set
     */
    public void setMemberUser(HelloUser memberUser) {
        this.memberUser = memberUser;
    }

    /**
     * @return the managment
     */
    public int getManagment() {
        return managment;
    }

    /**
     * @param managment the managment to set
     */
    public void setManagment(int managment) {
        this.managment = managment;
    }

    /**
     * @return the users
     */
    public int getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(int users) {
        this.users = users;
    }

    /**
     * @return the tasks
     */
    public int getTasks() {
        return tasks;
    }

    /**
     * @param tasks the tasks to set
     */
    public void setTasks(int tasks) {
        this.tasks = tasks;
    }

    /**
     * @return the discussions
     */
    public int getDiscussions() {
        return discussions;
    }

    /**
     * @param discussions the discussions to set
     */
    public void setDiscussions(int discussions) {
        this.discussions = discussions;
    }

    /**
     * @return the active
     */
    public int getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(int active) {
        this.active = active;
    }
}
