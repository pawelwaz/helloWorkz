package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.Invitation;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.entity.Notification;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class InviteUserController extends HelloUI {
    
    @FXML private ComboBox<Group> groupCB;
    
    @FXML private void inviteAction() {
        if(this.isMember() || this.hasInvitation()) HelloUI.showInfo("Użytkownik już jest członkiem tej grupy lub otrzymał zaproszenie do niej.");
        else {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Invitation in = new Invitation();
            in.setSender(HelloSession.getUser().getId());
            in.setHellouser(HelloSession.getInviteId());
            in.setWorkgroup(this.groupCB.getSelectionModel().getSelectedItem().getId());
            Notification n = new Notification(HelloSession.getInviteId(), "Użytkownik " + HelloSession.getUser().getLogin() + " wysłał Ci zaproszenie do grupy " + this.groupCB.getSelectionModel().getSelectedItem().getGroup_name());
            em.getTransaction().begin();
            em.persist(n);
            em.persist(in);
            em.getTransaction().commit();
            em.close();
            HelloUI.showInfo("Zaproszenie zostało wysłane");
            this.closeWindow();
        }
    }
    
    @FXML private void cancel() {
        this.closeWindow();
    }
    
    private boolean isMember() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Long gid = this.groupCB.getSelectionModel().getSelectedItem().getId();
        Query q = em.createQuery("select m from Membership m where m.active = 1 and m.hellouser = " + HelloSession.getInviteId() + " and m.workgroup = " + gid);
        List<Membership> result = q.getResultList();
        em.close();
        if(result.isEmpty()) return false;
        else return true;
    }
    
    private boolean hasInvitation() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Long gid = this.groupCB.getSelectionModel().getSelectedItem().getId();
        Query q = em.createQuery("select i from Invitation i where i.hellouser = " + HelloSession.getInviteId() + " and i.workgroup = " + gid);
        List<Invitation> result = q.getResultList();
        em.close();
        if(result.isEmpty()) return false;
        else return true;
    }
    
    private void populateList() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select m from Membership m join m.memberGroup g where m.active = 1 and m.users = 1 and m.hellouser = " + HelloSession.getUser().getId());
        List<Membership> results = q.getResultList();
        ObservableList<Group> groupList = FXCollections.observableArrayList();
        for(Membership m : results) {
            groupList.add(m.getMemberGroup());
        }
        this.groupCB.setItems(groupList);
        this.groupCB.getSelectionModel().select(0);
        em.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.populateList();
    }
    
}
