/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Notification;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author A20111
 */
public class NotificationController extends HelloUI {
    
    @FXML private WebView web;
    private String notsHtml = "";
    private Long currentNot = null;
    private int notsNumber = 0;
    private Timeline notsTimeline;
    
    private void getNots() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select n from Notification n where n.hellouser = " + HelloSession.getUser().getId() + " order by n.id asc");
        List<Notification> nots = q.getResultList();
        em.getTransaction().begin();
        for(Notification n : nots) {
            String color = "#BFCCCE";
            if(this.notsNumber % 2 == 1) color = "#CAD5D7";
            if(n.getReceived() == null) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                n.setReceived(df.format(new Date()));
                em.persist(n);
                this.notsHtml = HelloUI.prepareNotificationStripeBold(n.getContent(), n.getReceived(), color) + this.notsHtml;
            }
            else this.notsHtml = HelloUI.prepareNotificationStripe(n.getContent(), n.getReceived(), color) + this.notsHtml;
            this.notsNumber++;
        }
        em.getTransaction().commit();
        em.close();
    }
    
    private void showNots() {
        StringBuilder output = new StringBuilder("<body style=\"font-family: System; margin: 0px;\"><table cellpadding=\"5\" cellspacing=\"0\" style=\"font-size: 12px; width: 100%; border: 0px;\">");
        output.append("<tr><td></td><td style='font-weight: bold; padding-left: 15px;'>Odebrano:</td></tr>");
        output.append(this.notsHtml);
        output.append("</table>");
        output.append("</body>");
        this.web.getEngine().loadContent(output.toString());
    }
    
    public void checkNewNots() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select n from Notification n where n.received is null and n.hellouser = " + HelloSession.getUser().getId() + " order by n.id asc");
        List<Notification> nots = q.getResultList();
        em.getTransaction().begin();
        String color = "#BFCCCE";
        if(this.notsNumber % 2 == 1) color = "#CAD5D7";
        for(Notification n : nots) {
            if(n.getReceived() == null) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                n.setReceived(df.format(new Date()));
                em.persist(n);
                this.notsHtml = HelloUI.prepareNotificationStripeBold(n.getContent(), n.getReceived(), color) + this.notsHtml;
            }
            else this.notsHtml = HelloUI.prepareNotificationStripe(n.getContent(), n.getReceived(), color) + this.notsHtml;
            this.notsNumber++;
        }
        em.getTransaction().commit();
        if(!nots.isEmpty()) this.showNots();
        em.close();
        HelloSession.getMainController().setNotificationNumber(0);
    }
    
    public void stopTimeline() {
        this.notsTimeline.stop();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.getNots();
        this.showNots();
        this.notsTimeline = new Timeline(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkNewNots();
            }
        }));
        this.notsTimeline.setCycleCount(Animation.INDEFINITE);
        this.notsTimeline.play();
    }
    
}
