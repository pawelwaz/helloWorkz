package org.pawelwaz.helloworkz.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javax.imageio.ImageIO;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.Notification;
import org.pawelwaz.helloworkz.entity.TaskUser;

/**
 *
 * @author pawelwaz
 */
public class UserGroupsController extends HelloUI {
    
    @FXML private VBox vb;
    private WritableImage viewIcon;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            File file = new File("classes/img/search.png");
            BufferedImage bufferedImage = ImageIO.read(file);
            this.viewIcon = SwingFXUtils.toFXImage(bufferedImage, null);
        }
        catch(Exception e) {
            HelloUI.showError("Wystąpił błąd podczas ładowania plików programu");
        }
        this.getGroups();
    }
    
    public void getGroups() {
        this.vb.getChildren().clear();
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query query = em.createQuery("select m from Membership m join m.memberGroup g where m.active = 1 and m.hellouser = " + HelloSession.getUser().getId());
        List<Membership> userMemberships = query.getResultList();
        if(userMemberships.isEmpty()) {
            Label noResults = new Label("Obecnie nie posiadasz członkostwa w żadnej grupie");
            noResults.getStyleClass().add("blueLabelSm");
            vb.getChildren().add(noResults);
        }
        else {
            GridPane gp = new GridPane();
            int n = 0;
            for(Membership mbm : userMemberships) {
                this.prepareRow(mbm, gp, n);
                n++;
            }
            vb.getChildren().add(gp);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            gp.getColumnConstraints().addAll(new ColumnConstraints(), cc);
            AnchorPane.setLeftAnchor(gp, 0.0);
            AnchorPane.setRightAnchor(gp, 0.0);
        }
    }
    
    private void prepareRow(Membership mbm, GridPane grid, int i) {
        String styleClass = "stripeOdd";
        if(i % 2 == 0) styleClass = "stripeEven";
        grid.add(HelloUI.wrapNode(this.prepareGroupDescription(mbm), styleClass, 0.0), 0, i);
        grid.add(HelloUI.insertEmptyCell(styleClass), 1, i);
        grid.add(this.insertViewButton(styleClass, mbm.getMemberGroup().getId()), 2, i);
        grid.add(this.insertLeaveButton(styleClass, mbm.getMemberGroup().getId()), 3, i);
    }
    
    private void leaveGroup(Long id) {
        if(HelloUI.showConfirmation("Czy na pewno chcesz opuścić tę grupę?")) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select m from Membership m where m.active = 1 and m.workgroup = " + id + " and m.hellouser = " + HelloSession.getUser().getId());
            List<Membership> result = q.getResultList();
            Membership m = result.get(0);
            q = em.createQuery("select tu from TaskUser tu join tu.taskJoin tj where tj.status = 0 and tu.hellouser = " + HelloSession.getUser().getId());
            List<TaskUser> userTasks = q.getResultList();
            q = em.createQuery("select g from Group g where g.id = " + id);
            List<Group> gResult = q.getResultList();
            Group g = gResult.get(0);
            em.getTransaction().begin();
            m.setActive(0);
            em.persist(m);
            em.getTransaction().commit();
            q = em.createQuery("select m from Membership m where m.active = 1 and m.workgroup = " + id);
            List<Membership> memberships = q.getResultList();
            em.getTransaction().begin();
            for(TaskUser tu : userTasks) em.remove(tu);
            for(Membership mem : memberships) {
                Notification n = new Notification(mem.getHellouser(), "Użytkownik " + HelloSession.getUser().getLogin() + " opuścił grupę " + g.getGroup_name());
                em.persist(n);
            }
            em.getTransaction().commit();
            em.close();
            this.getGroups();
        }
    }
    
    private AnchorPane insertLeaveButton(String styleClass, final Long groupId) {
        final Long gid = groupId;
        ImageView result = new ImageView();
        result.setImage(HelloUI.declineButton);
        result.setCursor(Cursor.HAND);
        Tooltip.install(result, new Tooltip("Odejdź z grupy"));
        result.setOnMouseClicked(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent event) {
                leaveGroup(groupId);
            }
        });
        return this.wrapNode(result, styleClass, 15.0, 15.0, 0.0, 5.0);
    }
    
    private AnchorPane insertViewButton(String styleClass, Long groupId) {
        final Long gid = groupId;
        ImageView result = new ImageView();
        result.setImage(viewIcon);
        result.setCursor(Cursor.HAND);
        Tooltip.install(result, new Tooltip("Pokaż grupę"));
        result.setOnMouseClicked(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent event) {
                HelloSession.setGroupView(gid);
                HelloSession.getMainController().goGroupView();
            }
        });
        return this.wrapNode(result, styleClass, 15.0, 15.0, 0.0, 5.0);
    }
    
    private VBox prepareGroupDescription(Membership mbm) {
        VBox result = new VBox();
        Label groupName = new Label(mbm.getMemberGroup().getGroup_name());
        groupName.getStyleClass().add("smallHeader");
        result.getChildren().add(groupName);
        Label memberType = new Label("Stanowisko: " + mbm.getTitle());
        memberType.getStyleClass().add("description");
        result.getChildren().add(memberType);
        Label memberDesc = new Label("Opis stanowiska: " + mbm.getDescription());
        memberDesc.getStyleClass().add("description");
        if(mbm.getDescription().length() == 0) memberDesc.setText(memberDesc.getText() + "brak");
        result.getChildren().add(memberDesc);
        Label groupDesc = new Label("Opis grupy: " + mbm.getMemberGroup().getDescription());
        groupDesc.getStyleClass().add("description");
        if(mbm.getMemberGroup().getDescription().length() == 0) groupDesc.setText(groupDesc.getText() + "brak");
        result.getChildren().add(groupDesc);
        return result;
    }
    
}