package org.pawelwaz.helloworkz.controller;

import java.util.List;
import javafx.event.EventHandler;
import org.pawelwaz.helloworkz.util.HelloUI;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.entity.MembershipRequest;
import org.pawelwaz.helloworkz.entity.Notification;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class SearchGroupsController extends HelloUI {
    
    @FXML private TextField searchField;
    @FXML private VBox scrollVB;
    @FXML private Label resultHeader;
    @FXML private Label resultCount;
    
    @FXML private void searchAction() {
        this.resultHeader.setVisible(true);
        this.resultCount.setVisible(true);
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query query = em.createQuery("select g from Group g where g.group_name like '%" + this.searchField.getText() + "%'");
        List<Group> groups = query.getResultList();
        if(this.scrollVB.getChildren().size() > 2) this.scrollVB.getChildren().remove(2);
        if(groups.isEmpty()) {
            this.resultCount.setText("nie znaleziono grup spełniających kryteria");
        }
        else {
            Query q = em.createQuery("select r.workgroup from MembershipRequest r where r.hellouser = " + HelloSession.getUser().getId());
            List<Long> requestGroups = q.getResultList();
            q = em.createQuery("select m.workgroup from Membership m where m.hellouser = " + HelloSession.getUser().getId());
            List<Long> memberships = q.getResultList();
            this.resultCount.setText("ilość znalezionych grup: " + groups.size());
            GridPane results = new GridPane();
            int i = 0;
            for(Group g : groups) {
                String styleClass = "stripeOdd";
                if(i % 2 == 0) styleClass = "stripeEven";
                VBox groupDesc = new VBox();
                Label title = new Label(g.getGroup_name());
                title.getStyleClass().add("smallHeader");
                groupDesc.getChildren().add(title);
                String desc = "Opis: ";
                if(g.getDescription().length() == 0) desc += "brak";
                else desc += g.getDescription();
                Label descLabel = new Label(desc);
                descLabel.getStyleClass().add("description");
                groupDesc.getChildren().add(descLabel);
                Label groupOption = this.prepareOption(g, requestGroups, memberships);
                results.add(HelloUI.wrapNode(groupDesc, styleClass, 10.0), 0, i);
                results.add(HelloUI.insertEmptyCell(styleClass), 1, i);
                results.add(HelloUI.wrapNode(groupOption, styleClass, 0.0), 2, i);
                i++;
            }
            this.scrollVB.getChildren().add(results);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            results.getColumnConstraints().addAll(new ColumnConstraints(), cc);
            AnchorPane.setLeftAnchor(results, 0.0);
            AnchorPane.setRightAnchor(results, 0.0);
        }
        em.close();
    }
    
    public Label prepareOption(final Group g, List<Long> requestGroups, List<Long> memberships) {
        Label groupOption = new Label("Dołącz");
        boolean requestSent = false;
        boolean isMember = false;
        if(memberships.contains(g.getId())) {
            isMember = true;
            groupOption.setText("Należysz do tej grupy (otwórz)");
        }
        else if(requestGroups.contains(g.getId())) {
            requestSent = true;
            groupOption.setText("wysłałeś prośbę o dołączenie");
        }
        groupOption.setAlignment(Pos.BASELINE_RIGHT);
        groupOption.getStyleClass().add("smallHeader");
        groupOption.setUnderline(true);
        if(isMember) {
            groupOption.setCursor(Cursor.HAND);
            groupOption.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    HelloSession.setGroupView(g.getId());
                    HelloSession.getMainController().goGroupView();
                }
            });
        }
        else if(!requestSent) {
            groupOption.setCursor(Cursor.HAND);
            groupOption.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    boolean result = sendRequest(g.getId(), g.getGroup_name());
                    if(result == true) {
                        Label source = (Label) event.getSource();
                        source.setText("wysłałeś prośbę o dołączenie");
                        source.setOnMouseClicked(null);
                        source.setCursor(Cursor.DEFAULT);
                    }
                }
            });
        }
        return groupOption;
    }
    
    public boolean sendRequest(Long groupId, String name) {
        boolean result = HelloUI.showConfirmation("Czy chcesz wysłać prośbę o dołączenie do grupy " + name + "?");
        if(result == true) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            MembershipRequest request = new MembershipRequest(groupId, HelloSession.getUser().getId());
            Query q = em.createQuery("select m from Membership m where m.users = 1 and m.workgroup = " + groupId);
            List<Membership> memberships = q.getResultList();
            q = em.createQuery("select g from Group g where g.id = " + groupId);
            List<Group> gResult = q.getResultList();
            Group g = gResult.get(0);
            em.getTransaction().begin();
            for(Membership m : memberships) {
                 Notification n = new Notification(m.getHellouser(), "Użytkownik " + HelloSession.getUser().getLogin() + " wysłał prośbę o dołączenie do grupy " + g.getGroup_name());
                 em.persist(n);
            }
            em.persist(request);
            em.getTransaction().commit();
            em.close();
        }
        return result;
    }
    
}
