/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Discussion;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.entity.Notification;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author A20111
 */
public class DiscussionAddController extends HelloUI {
    
    @FXML private TextField title;
    
    @FXML private void cancel() {
        this.closeWindow();
    }
    
    @FXML private void addDiscussion() {
        if(this.title.getText().length() == 0) HelloUI.showError("Tytuł dyskusji nie może być pusty.");
        else {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select g from Group g where g.id = " + HelloSession.getGroupView());
            List<Group> gResult = q.getResultList();
            Group g = gResult.get(0);
            q = em.createQuery("select m from Membership m where m.active = 1 and m.workgroup = " + g.getId() + " and m.hellouser <> " + HelloSession.getUser().getId());
            List<Membership> memberships = q.getResultList();
            Discussion dis = new Discussion();
            dis.setHellouser(HelloSession.getUser().getId());
            dis.setPosts(0);
            dis.setTitle(this.title.getText());
            dis.setWorkgroup(HelloSession.getGroupView());
            dis.setLastPost(new Date());
            em.getTransaction().begin();
            em.persist(dis);
            for(Membership m : memberships) {
                Notification n = new Notification(m.getHellouser(), "Użytkownik " + HelloSession.getUser().getLogin() + " utworzył w grupie " + g.getGroup_name() + " dyskusję pod tytułem " + dis.getTitle());
                em.persist(n);
            }
            em.getTransaction().commit();
            em.close();
            HelloUI.showInfo("Dyskusja została utworzona");
            this.closeWindow();
        }
    }
    
}
