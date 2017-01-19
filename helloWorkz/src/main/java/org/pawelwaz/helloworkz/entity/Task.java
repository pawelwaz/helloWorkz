/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "task")
public class Task implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long creator;
    private Long workgroup;
    private String deadline;
    private String deadlinehour;
    private String content;
    private String annotation;
    private int status;
    
    @OneToOne
    @JoinColumn(name="creator")
    private HelloUser creatorJoin;
    
    @OneToMany(mappedBy = "taskJoin")
    private List<TaskUser> taskusers;
    
    public Task(Long creator, Long workgroup, String deadline, String deadlinehour, String content, String annotation, int status) {
        this.content = content;
        this.creator = creator;
        this.workgroup = workgroup;
        this.deadline = deadline;
        this.deadlinehour = deadlinehour;
        this.annotation = annotation;
        this.status = status;
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
     * @return the creator
     */
    public Long getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(Long creator) {
        this.creator = creator;
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
     * @return the deadline
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     * @param deadline the deadline to set
     */
    public void setDeadline(String deadline) {
        this.deadline = deadline;
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
     * @return the annotation
     */
    public String getAnnotation() {
        return annotation;
    }

    /**
     * @param annotation the annotation to set
     */
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the deadlinehour
     */
    public String getDeadlinehour() {
        return deadlinehour;
    }

    /**
     * @param deadlinehour the deadlinehour to set
     */
    public void setDeadlinehour(String deadlinehour) {
        this.deadlinehour = deadlinehour;
    }

    /**
     * @return the taskusers
     */
    public List<TaskUser> getTaskusers() {
        return taskusers;
    }

    /**
     * @param taskusers the taskusers to set
     */
    public void setTaskusers(List<TaskUser> taskusers) {
        this.taskusers = taskusers;
    }

    /**
     * @return the creatorJoin
     */
    public HelloUser getCreatorJoin() {
        return creatorJoin;
    }

    /**
     * @param creatorJoin the creatorJoin to set
     */
    public void setCreatorJoin(HelloUser creatorJoin) {
        this.creatorJoin = creatorJoin;
    }
    
}
