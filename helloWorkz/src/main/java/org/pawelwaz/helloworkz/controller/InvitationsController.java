/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Invitation;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.entity.MembershipRequest;
import org.pawelwaz.helloworkz.entity.Notification;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author A20111
 */
public class InvitationsController extends HelloUI {
    
    private void getInvitations() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();  
        Query q = em.createQuery("select i from Invitation i where i.hellouser = " + HelloSession.getUser().getId());
        List<Invitation> result = q.getResultList();
        this.ap.getChildren().clear();
        if(result.isEmpty()) {
            Label noResult = new Label("brak zaproszeń do wyświetlenia");
            noResult.getStyleClass().add("blueLabelSm");
            this.ap.getChildren().add(noResult);
        }
        else {
            GridPane invGrid = new GridPane();
            int i = 0;
            for(Invitation in : result) {
                String styleClass = "stripeOdd";
                if(i % 2 == 0) styleClass = "stripeEven";
                invGrid.add(this.prepareInvitationDescription(in.getGroupJoin(), in.getSenderJoin(), styleClass), 0, i);
                invGrid.add(HelloUI.insertEmptyCell(styleClass), 1, i);
                invGrid.add(this.insertAcceptInvButton(styleClass, in.getId()), 2, i);
                invGrid.add(this.insertDeclineInvButton(styleClass, in.getId()), 3, i);
                i++;
            }
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            invGrid.getColumnConstraints().addAll(new ColumnConstraints(), cc);
            AnchorPane.setLeftAnchor(invGrid, 0.0);
            AnchorPane.setRightAnchor(invGrid, 0.0);
            this.ap.getChildren().add(invGrid);
        }
        em.close();
    }
    
    private void declineInvitation(Long id) {
        if(HelloUI.showConfirmation("Czy na pewno odrzucić to zaproszenie?")) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select i from Invitation i where i.id = " + id);
            List<Invitation> result = q.getResultList();
            Invitation inv = result.get(0);
            Notification n = new Notification(inv.getSender(), "Użytkownik " + inv.getReceiverJoin().getLogin() + " odrzucił twoje zaproszenie do grupy " + inv.getGroupJoin().getGroup_name());
            em.getTransaction().begin();
            em.persist(n);
            em.remove(inv);
            em.getTransaction().commit();
            em.close();
            this.getInvitations();
        }
    }
    
    private void acceptInvitation(Long id) {
        if(HelloUI.showConfirmation("Czy na pewno zaakceptować to zaproszenie?")) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select i from Invitation i where i.id = " + id);
            List<Invitation> result = q.getResultList();
            Invitation inv = result.get(0);
            q = em.createQuery("select r from MembershipRequest r where r.hellouser = " + inv.getHellouser() + " and r.workgroup = " + inv.getWorkgroup());
            List<MembershipRequest> requestResult = q.getResultList();
            Membership mem = new Membership();
            mem.setActive(1);
            mem.setHellouser(inv.getHellouser());
            mem.setWorkgroup(inv.getWorkgroup());
            mem.setTitle("Pracownik");
            mem.setDescription("");
            Notification n = new Notification(inv.getSender(), "Użytkownik " + inv.getReceiverJoin().getLogin() + " przyjął twoje zaproszenie do grupy " + inv.getGroupJoin().getGroup_name());
            em.getTransaction().begin();
            em.persist(n);
            em.persist(mem);
            if(!requestResult.isEmpty()) {
                MembershipRequest request = requestResult.get(0);
                em.remove(request);
            }
            em.remove(inv);
            em.getTransaction().commit();
            em.close();
            this.getInvitations();
        }
    }
    
    private AnchorPane insertDeclineInvButton(String styleClass, final Long id) {
        ImageView btn = new ImageView();
        btn.setImage(HelloUI.declineButton);
        btn.setCursor(Cursor.HAND);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                declineInvitation(id);
            }
        });
        Tooltip.install(btn, new Tooltip("odrzuć zaproszenie"));
        return HelloUI.wrapNode(btn, styleClass,  15.0, 15.0, 0.0, 5.0);
    }
    
    private AnchorPane insertAcceptInvButton(String styleClass, final Long id) {
        ImageView btn = new ImageView();
        btn.setImage(HelloUI.acceptButton);
        btn.setCursor(Cursor.HAND);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                acceptInvitation(id);
            }
        });
        Tooltip.install(btn, new Tooltip("akceptuj zaproszenie"));
        return HelloUI.wrapNode(btn, styleClass,  15.0, 15.0, 0.0, 5.0);
    }
    
    private AnchorPane prepareInvitationDescription(Group g, HelloUser sender, String styleClass) {
        VBox result = new VBox();
        Label gLabel = new Label("Grupa: " + g.getGroup_name());
        gLabel.getStyleClass().add("smallHeader");
        Label senderLabel = new Label("Wysyłający: " + sender.getLogin());
        senderLabel.getStyleClass().add("smallHeader");
        result.getChildren().add(gLabel);
        result.getChildren().add(senderLabel);
        return HelloUI.wrapNode(result, styleClass, 10);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.getInvitations();
    }
    
}
