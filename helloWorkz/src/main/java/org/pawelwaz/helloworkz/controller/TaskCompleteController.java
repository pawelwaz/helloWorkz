/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.controller;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Task;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author A20111
 */
public class TaskCompleteController extends HelloUI {
    
    @FXML private TextArea comment;
    
    @FXML private void cancel() {
        this.closeWindow();
    }
    
    @FXML private void completeTask() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select t from Task t where t.id = " + HelloSession.getTaskEdit());
        List<Task> result = q.getResultList();
        Task t = result.get(0);
        em.getTransaction().begin();
        t.setAnnotation(this.comment.getText() + " (zamknięte przez: " + HelloSession.getUser().getLogin() + ")");
        t.setStatus(1);
        em.persist(t);
        em.getTransaction().commit();
        em.close();
        this.closeWindow();
    }
    
}
