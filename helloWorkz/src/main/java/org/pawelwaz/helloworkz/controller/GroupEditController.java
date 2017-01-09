package org.pawelwaz.helloworkz.controller;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
                Membership member = new Membership(gr.getId(), HelloSession.getUser().getId(), "Właściciel", "Właściciel grupy");
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
    }
    
    @FXML private void cancel() {
        this.closeWindow();
    }
}
