/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.entity.Notification;
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
        if(t.isAfterDeadline()) t.setStatus(2);
        else t.setStatus(1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        t.setClosed(df.format(new Date()));
        t.setAnnotation("(zamknięte przez: " + HelloSession.getUser().getLogin() + ") " + this.comment.getText());
        em.persist(t);
        q = em.createQuery("select g from Group g where g.id = " + HelloSession.getGroupView());
        List<Group> gResult = q.getResultList();
        Group g = gResult.get(0);
        q = em.createQuery("select m from Membership m where m.active = 1 and m.workgroup = " + g.getId());
        List<Membership> memberships = q.getResultList();
        for(Membership m : memberships) {
            if(!m.getHellouser().equals(HelloSession.getUser().getId())) {
                Notification n = new Notification(m.getHellouser(), "Użytkownik " + HelloSession.getUser().getLogin() + " zamknął zadanie nr " + t.getNumber() + " w grupie " + g.getGroup_name());
                em.persist(n);
            }
        }
        em.getTransaction().commit();
        em.close();
        this.closeWindow();
    }
    
}
