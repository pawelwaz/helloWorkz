/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.controller;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Discussion;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Post;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author A20111
 */
public class DiscussionWindowController extends HelloUI {
    
    @FXML private TextArea input;
    @FXML private WebView web;
    private Discussion disc;
    private String htmlContent = "";
    private int msgNumber = 0;
    private Long maxId;
    
    public URI getHtmlAvatar(HelloUser user) {
        File tmpAvatar = new File("tmp/" + user.getLogin() + ".png");
        if(!tmpAvatar.exists()) {
            user.prepareAvatar();
            HelloUI.saveTmpImage(user.getReadyAvatar(), user.getLogin());
        }
        URI htmlAvatar = tmpAvatar.toURI();
        return htmlAvatar;
    }
    
    public void getPosts() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select p from Post p join p.sender s where p.discussion = " + this.disc.getId());
        List<Post> results = q.getResultList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm");
        for(Post p : results) {
            String color = "#BFCCCE";
            if(this.msgNumber % 2 == 1) color = "#CAD5D7";
            this.htmlContent += HelloUI.prepareMessageStripe(p.getSender(), p.getContent(), df.format(p.getCreated()), color, this.getHtmlAvatar(p.getSender()));
            this.msgNumber++;
        }
        if(!results.isEmpty()) this.maxId = results.get(results.size() - 1).getId();
        else this.maxId = 0L;
        em.close();
    }
    
    public void showPosts() {
        String color = "#BFCCCE";
        if(this.msgNumber % 2 == 1) color = "#CAD5D7";
        StringBuilder output = new StringBuilder("<body style=\"font-family: System; margin: 0px;\"><table cellpadding=\"5\" cellspacing=\"0\" style=\"font-size: 12px; width: 100%; border: 0px;\">");
        output.append(this.htmlContent);
        output.append("</table>");
        output.append("</body>");
        this.web.getEngine().loadContent(output.toString());
    }
    
    @FXML public void sendPost(KeyEvent event) {
        if(input.getText().length() > 0 && event.getCode().equals(KeyCode.ENTER)) {
            Post p = new Post();
            p.setContent(this.input.getText());
            p.setCreated(new Date());
            p.setDiscussion(this.disc.getId());
            p.setHellouser(HelloSession.getUser().getId());
            String color = "#BFCCCE";
            if(this.msgNumber % 2 == 1) color = "#CAD5D7";
            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm");
            this.htmlContent += HelloUI.prepareMessageStripe(HelloSession.getUser(), p.getContent(), df.format(p.getCreated()), color, HelloSession.getHtmlAvatar());
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
            this.maxId = p.getId();
            Query q = em.createQuery("select d from Discussion d where d.id = " + this.disc.getId());
            List<Discussion> result = q.getResultList();
            Discussion d = result.get(0);
            em.getTransaction().begin();
            d.setPosts(d.getPosts() + 1);
            d.setLastPost(p.getCreated());
            em.persist(d);
            em.getTransaction().commit();
            em.close();
            this.input.setText("");
            event.consume();
            this.msgNumber++;
            this.showPosts();
        }
    }
    
    public void checkPosts() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select p from Post p join p.sender s where p.discussion = " + this.disc.getId() + " and p.id > " + this.maxId);
        List<Post> posts = q.getResultList();
        if(!posts.isEmpty()) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm");
            for(Post p : posts) {
                String color = "#BFCCCE";
                if(this.msgNumber % 2 == 1) color = "#CAD5D7";
                this.htmlContent += HelloUI.prepareMessageStripe(p.getSender(), p.getContent(), df.format(p.getCreated()), color, this.getHtmlAvatar(p.getSender()));
            }
            this.maxId = posts.get(posts.size() - 1).getId();
            this.showPosts();
        }
        em.close();
    }
    
    public void initTimeline() {
        Timeline msgTimeline = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkPosts();
            }
        }));
        msgTimeline.setCycleCount(Animation.INDEFINITE);
        msgTimeline.play();
    }
    
    public void toFront() {
        Stage thisStage = (Stage) this.ap.getScene().getWindow();
        if(thisStage.isIconified()) {
            thisStage.setIconified(false);
        }
        thisStage.toFront();
    }

    /**
     * @return the disc
     */
    public Discussion getDisc() {
        return disc;
    }

    /**
     * @param disc the disc to set
     */
    public void setDisc(Discussion disc) {
        this.disc = disc;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.web.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                web.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight)");
            }
        });
    }
    
}
