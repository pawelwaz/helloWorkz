/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.util;

import java.util.List;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.Task;
import org.pawelwaz.helloworkz.entity.TaskUser;

public class TaskDescriptor {
    
    private Long id;
    private String workers;
    private String content;
    private String deadline;
    private String creator;
    private String status;
    private String annotation;
    private String number;
    private String created;
    private String closed;
    private int statusCode;
    private boolean editable = false;
    private Group workgroup;
    private String workgroupName;
    
    public TaskDescriptor(Task t) {
        this.id = t.getId();
        this.workers = this.prepareWorkers(t.getTaskusers());
        this.content = t.getContent();
        this.deadline = t.getDeadline() + " " + t.getDeadlinehour();
        this.creator = t.getCreatorJoin().getLogin();
        this.annotation = t.getAnnotation();
        this.status = this.parseStatus(t.getStatus());
        this.statusCode = t.getStatus();
        this.created = t.getCreated();
        this.closed = t.getClosed();
        this.number = new Integer(t.getNumber()).toString();
        this.workgroup = t.getWorkgroupJoin();
        this.workgroupName = this.workgroup.getGroup_name();
    }
    
    private String parseStatus(int s) {
        switch(s) {
            case 1: return "zamknięte";
            case 2: return "zamknięte (po terminie)";
            default: return "oczekujące";
        }
    }
    
    private String prepareWorkers(List<TaskUser> userList) {
        String result = "";
        for(TaskUser tu : userList) {
            if(tu.getHellouser().equals(HelloSession.getUser().getId())) this.editable = true;
            result += tu.getWorker().getLogin() + ", ";
        }
        if(result.length() > 0) result = result.substring(0, result.length() - 2);
        return result;
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
     * @return the workers
     */
    public String getWorkers() {
        return workers;
    }

    /**
     * @param workers the workers to set
     */
    public void setWorkers(String workers) {
        this.workers = workers;
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
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * @return the closed
     */
    public String getClosed() {
        return closed;
    }

    /**
     * @param closed the closed to set
     */
    public void setClosed(String closed) {
        this.closed = closed;
    }

    /**
     * @return the workgroup
     */
    public Group getWorkgroup() {
        return workgroup;
    }

    /**
     * @param workgroup the workgroup to set
     */
    public void setWorkgroup(Group workgroup) {
        this.workgroup = workgroup;
    }

    /**
     * @return the workgroupName
     */
    public String getWorkgroupName() {
        return workgroupName;
    }

    /**
     * @param workgroupName the workgroupName to set
     */
    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }
    
}
