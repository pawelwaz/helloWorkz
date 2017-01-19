/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author A20111
 */
public class MembershipController extends HelloUI {
    
    @FXML private Label header;
    @FXML private TextField title;
    @FXML private TextArea description;
    @FXML private CheckBox managment;
    @FXML private CheckBox users;
    @FXML private CheckBox discussions;
    @FXML private CheckBox tasks;
    private Membership mem;
    
    @FXML private void cancel() {
        this.closeWindow();
    }
    
    @FXML private void membershipAction() {
        if(this.title.getText().length() == 0) HelloUI.showError("Nazwa stanowiska nie może być pusta.");
        else {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select m from Membership m where m.id = " + this.mem.getId());
            List<Membership> result = q.getResultList();
            Membership m = result.get(0);
            em.getTransaction().begin();
            m.setTitle(this.title.getText());
            m.setDescription(this.description.getText());
            if(this.managment.isSelected()) m.setManagment(1);
            else m.setManagment(0);
            if(this.users.isSelected()) m.setUsers(1);
            else m.setUsers(0);
            if(this.discussions.isSelected()) m.setDiscussions(1);
            else m.setDiscussions(0);
            if(this.tasks.isSelected()) m.setTasks(1);
            else m.setTasks(0);
            em.getTransaction().commit();
            em.close();
            this.closeWindow();
        }
    }
    
    @FXML private void removeMembership() {
        if(HelloUI.showConfirmation("Czy na pewno usunąć tego użytkownika z grupy?")) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select m from Membership m where m.id = " + this.mem.getId());
            List<Membership> result = q.getResultList();
            Membership m = result.get(0);
            em.getTransaction().begin();
            em.remove(m);
            em.getTransaction().commit();
            em.close();
            this.closeWindow();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select m from Membership m where m.id = " + HelloSession.getMembershipId());
        List<Membership> result = q.getResultList();
        this.mem = result.get(0);
        q = em.createQuery("select u from HelloUser u where u.id = " + this.mem.getHellouser());
        List<HelloUser> userResult = q.getResultList();
        HelloUser user = userResult.get(0);
        this.header.setText("Edycja członkostwa użytkownika " + user.getLogin());
        this.title.setText(this.mem.getTitle());
        this.description.setText(this.mem.getDescription());
        if(this.mem.getManagment() == 1) this.managment.setSelected(true);
        if(this.mem.getUsers() == 1) this.users.setSelected(true);
        if(this.mem.getDiscussions() == 1) this.discussions.setSelected(true);
        if(this.mem.getTasks() == 1) this.tasks.setSelected(true);
        em.close();
    }
    
}
