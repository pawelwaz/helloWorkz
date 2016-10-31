package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class MessagesController extends HelloUI {
    
    @FXML VBox leftVB;
    @FXML VBox rightVB;
    
    private List<HelloUser> getConversations() {
        List<HelloUser> persons = new ArrayList();
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("SELECT distinct u FROM Message m JOIN m.senderUser u where m.receiver = :user");
        q.setParameter("user", HelloSession.getUser().getId());
        List<HelloUser> senders = q.getResultList();
        for(HelloUser sender : senders) {
            if(!persons.contains(sender)) persons.add(sender);
        }
        q = em.createQuery("select distinct u from Message m join m.receiverUser u where m.sender = :user");
        q.setParameter("user", HelloSession.getUser().getId());
        List<HelloUser> receivers = q.getResultList();
        for(HelloUser receiver : receivers) {
            if(!persons.contains(receiver)) persons.add(receiver);
        }
        em.close();
        Collections.sort(persons, new Comparator<HelloUser>() {
            @Override
            public int compare(HelloUser o1, HelloUser o2) {
                return o1.getLogin().compareTo(o2.getLogin());
            }
        });
        return persons;
    }
    
    private void showConversations() {
        List<HelloUser> persons = this.getConversations();
        this.leftVB.getChildren().clear();
        Label header = new Label("lista rozmów");
        header.getStyleClass().add("blueLabelSm");
        this.leftVB.getChildren().add(header);
        int n = 0;
        for(HelloUser person : persons) {
            HBox row = new HBox();
            if(n % 2 == 0) row.getStyleClass().add("stripeEven");
            else row.getStyleClass().add("stripeOdd");
            row.setCursor(Cursor.HAND);
            row.setPadding(new Insets(5, 5, 5, 5));
            person.prepareAvatar();
            ImageView userAvatar = new ImageView();
            userAvatar.setImage(SwingFXUtils.toFXImage(person.getReadyAvatar(), null));
            userAvatar.setFitWidth(60.0);
            userAvatar.setFitHeight(60.0);
            row.getChildren().add(userAvatar);
            Label userName = new Label(person.getLogin());
            userName.getStyleClass().add("smallHeader");
            row.getChildren().add(userName);
            this.leftVB.getChildren().add(row);
            n++;
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.showConversations();
    }
    
}
