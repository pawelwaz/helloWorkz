package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class GroupEditController extends HelloUI {
    @FXML private TextField groupName;
    @FXML private TextArea description;
    
    @FXML private void saveGroup() {
        if(this.groupName.getText().isEmpty()) {
            HelloUI.showError("Nazwa grupy nie może być pusta");
        }
        else if(HelloSession.getGroupId() == null) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query query = em.createQuery("select g from Group g where g.group_name = '" + this.groupName.getText() + "'");
            query.setMaxResults(1);
            List<Group> check = query.getResultList();
            if(check.isEmpty()) {
                Group gr = new Group(this.groupName.getText(), this.description.getText());
                em.getTransaction().begin();
                em.persist(gr);
                em.getTransaction().commit();
                Membership member = new Membership(gr.getId(), HelloSession.getUser().getId(), "Właściciel", "Właściciel grupy", 1, 1, 1, 1);
                em.getTransaction().begin();
                em.persist(member);
                em.getTransaction().commit();
                em.close();
                this.showInfo("Grupa została utworzona. Przejdź do swoich grup, aby zarządzać utworzoną grupą.");
                this.closeWindow();
                
            }
            else {
                this.showError("Wybrana nazwa grupy jest już zajęta");
                em.close();
            }
        }
        else {
            boolean success = false;
            boolean check = false;
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select g from Group g where g.id = " + HelloSession.getGroupId());
            List<Group> result = q.getResultList();
            Group g = result.get(0);
            if(!g.getGroup_name().equals(this.groupName.getText())) {
                q = em.createQuery("select g from Group g where g.group_name = '" + this.groupName.getText() + "'");
                result = q.getResultList();
                if(result.isEmpty()) check = true;
            }
            else check = true;
            if(check == false) this.showError("Wybrana nazwa grupy jest już zajęta");
            else {
                em.getTransaction().begin();
                g.setGroup_name(this.groupName.getText());
                g.setDescription(this.description.getText());
                em.getTransaction().commit();
                success = true;
                HelloUI.showInfo("Dane zostały zapisane");
            }
            em.close();
            if(success) this.closeWindow();
        }
    }
    
    @FXML private void cancel() {
        this.closeWindow();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(HelloSession.getGroupId() != null) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select g from Group g where g.id = " + HelloSession.getGroupId());
            List<Group> result = q.getResultList();
            Group g = result.get(0);
            this.groupName.setText(g.getGroup_name());
            this.description.setText(g.getDescription());
            em.close();
        }
    }
}
