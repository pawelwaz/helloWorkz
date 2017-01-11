package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import org.pawelwaz.helloworkz.util.HelloUI;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.HelloUser;
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
    @FXML private Label membersHeader;
    @FXML private Label editHeader;
    private GridPane membersGrid;
    private Group group;
    private Membership membership;
    
    @FXML private void editGroup() {
        HelloSession.setGroupId(this.group.getId());
        HelloSession.getMainController().goGroupEdit();
        HelloSession.setGroupId(null);
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select g from Group g where g.id = " + this.group.getId());
        List<Group> result = q.getResultList();
        this.group = result.get(0);
        this.groupDesc.setText(this.group.getDescription());
        this.header.setText("Grupa " + this.group.getGroup_name());
        em.close();
    }
    
    public void showHideMembers() {
        if(this.membersGrid.isVisible()) {
            this.membersGrid.setVisible(false);
            this.membersHeader.setText("Pokaż członków grupy");
        }
        else {
            this.membersGrid.setVisible(true);
            this.membersHeader.setText("Ukryj członków grupy");
        }
    }
    
    public void getMembers() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query query = em.createQuery("select m from Membership m join m.memberUser u where m.workgroup = " + this.group.getId());
        List<Membership> memberships = query.getResultList();
        this.membersGrid = new GridPane();
        int i = 0;
        for(Membership mem : memberships) {
            String styleClass = "stripeOdd";
            if(i % 2 == 0) styleClass = "stripeEven";
            HBox userDesc = new HBox();
            userDesc.getChildren().add(HelloUI.prepareUserAvatar(mem.getMemberUser()));
            VBox desc = new VBox();
            Label userName = new Label(mem.getMemberUser().getLogin());
            if(mem.getMemberUser().getName().length() > 0 && mem.getMemberUser().getSurname().length() > 0) {
                userName.setText(userName.getText() + " (" + mem.getMemberUser().getName() + " " + mem.getMemberUser().getSurname() + ")");
            }
            userName.getStyleClass().add("smallHeader");
            desc.getChildren().add(userName);
            Label memberTitle = new Label("Stanowisko: " + mem.getTitle());
            memberTitle.getStyleClass().add("description");
            desc.getChildren().add(memberTitle);
            Label memberDesc = new Label("Opis stanowiska: ");
            if(mem.getDescription().length() > 0) memberDesc.setText(memberDesc.getText() + mem.getDescription());
            else memberDesc.setText(memberDesc.getText() + "brak");
            memberDesc.getStyleClass().add("description");
            desc.getChildren().add(memberDesc);
            userDesc.getChildren().add(desc);
            this.membersGrid.add(HelloUI.wrapNode(userDesc, styleClass, 0.0), 0, i);
            this.membersGrid.add(HelloUI.insertEmptyCell(styleClass), 1, i);
            this.membersGrid.add(this.insertMessageButton(styleClass, mem.getMemberUser()), 2, i);
        }
        this.vb.getChildren().add(this.membersGrid);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        cc.setFillWidth(true);
        this.membersGrid.getColumnConstraints().addAll(new ColumnConstraints(), cc);
        AnchorPane.setLeftAnchor(this.membersGrid, 0.0);
        AnchorPane.setRightAnchor(this.membersGrid, 0.0);
        this.membersGrid.setVisible(false);
    }
    
    public void prepareHeaders() {
        this.membersHeader.setCursor(Cursor.HAND);
        this.editHeader.setCursor(Cursor.HAND);
    }
    
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
        this.getMembers();
        this.prepareHeaders();
    }
    
}
