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
    private int not1;
    private int not2;
    private int not3;
    
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

    /**
     * @return the not1
     */
    public int getNot1() {
        return not1;
    }

    /**
     * @param not1 the not1 to set
     */
    public void setNot1(int not1) {
        this.not1 = not1;
    }

    /**
     * @return the not2
     */
    public int getNot2() {
        return not2;
    }

    /**
     * @param not2 the not2 to set
     */
    public void setNot2(int not2) {
        this.not2 = not2;
    }

    /**
     * @return the not3
     */
    public int getNot3() {
        return not3;
    }

    /**
     * @param not3 the not3 to set
     */
    public void setNot3(int not3) {
        this.not3 = not3;
    }
    
}