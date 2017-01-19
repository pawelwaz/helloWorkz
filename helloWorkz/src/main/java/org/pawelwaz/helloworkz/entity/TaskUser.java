/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "taskuser")
public class TaskUser implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long hellouser;
    private Long task;
    
    @ManyToOne
    @JoinColumn(name="task")
    private Task taskJoin;
    
    @OneToOne
    @JoinColumn(name="hellouser")
    private HelloUser worker;
    
    public TaskUser(Long task, Long hellouser) {
        this.task = task;
        this.hellouser = hellouser;
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
     * @return the task
     */
    public Long getTask() {
        return task;
    }

    /**
     * @param task the task to set
     */
    public void setTask(Long task) {
        this.task = task;
    }

    /**
     * @return the worker
     */
    public HelloUser getWorker() {
        return worker;
    }

    /**
     * @param worker the worker to set
     */
    public void setWorker(HelloUser worker) {
        this.worker = worker;
    }

    /**
     * @return the taskJoin
     */
    public Task getTaskJoin() {
        return taskJoin;
    }

    /**
     * @param taskJoin the taskJoin to set
     */
    public void setTaskJoin(Task taskJoin) {
        this.taskJoin = taskJoin;
    }
    
}