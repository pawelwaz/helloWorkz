/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.util;

import java.util.List;
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
    private int statusCode;
    private boolean editable = false;
    
    public TaskDescriptor(Task t) {
        this.id = t.getId();
        this.workers = this.prepareWorkers(t.getTaskusers());
        this.content = t.getContent();
        this.deadline = t.getDeadline() + " " + t.getDeadlinehour();
        this.creator = t.getCreatorJoin().getLogin();
        this.annotation = t.getAnnotation();
        this.status = this.parseStatus(t.getStatus());
        this.statusCode = t.getStatus();
    }
    
    private String parseStatus(int s) {
        switch(s) {
            case 1: return "zamknięte";
            default: return "oczekujące";
        }
    }
    
    private String prepareWorkers(List<TaskUser> userList) {
        String result = "";
        for(TaskUser tu : userList) {
            if(tu.getHellouser().equals(HelloSession.getUser().getId())) this.editable = true;
            result += tu.getWorker().getLogin() + ", ";
        }
        result = result.substring(0, result.length() - 2);
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
    
}
