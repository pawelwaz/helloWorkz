package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import org.pawelwaz.helloworkz.util.HelloUI;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class GroupViewController extends HelloUI {
    
    @FXML private Label header;
    @FXML private Label groupDesc;
    @FXML private Label memberTitle;
    @FXML private Label memberDesc;
    @FXML private VBox vb;
    private Group group;
    private Membership membership;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query query = em.createQuery("select g from Group g where g.id = " + HelloSession.getGroupView());
        List<Group> groupResult = query.setMaxResults(1).getResultList();
        this.group = groupResult.get(0);
        query = em.createQuery("select m from Membership m where m.hellouser = " + HelloSession.getUser().getId() + " and m.workgroup = " + HelloSession.getGroupView());
        List<Membership> memberResult = query.setMaxResults(1).getResultList();
        this.membership = memberResult.get(0);
        this.header.setText(this.header.getText() + group.getGroup_name());
        this.groupDesc.setText(this.groupDesc.getText() + group.getDescription());
        if(group.getDescription().length() == 0) this.groupDesc.setText(this.groupDesc.getText() + "brak");
        this.memberTitle.setText(this.memberTitle.getText() + this.membership.getTitle());
        this.memberDesc.setText(this.memberDesc.getText() + this.membership.getDescription());
        if(this.membership.getDescription().length() == 0) this.memberDesc.setText(this.memberDesc.getText() + "brak");
    }
    
}
