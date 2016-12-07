package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Message;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class MessagesController extends HelloUI {
    
    @FXML private VBox leftVB;
    @FXML private WebView rightView;
    private HelloUser msgPerson = null;
    private StringBuilder messagesHtml = new StringBuilder("");
    private int msgCount = 0;
    
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
    
    private void loadMessages() {
        
    }
    
    private void showMessages() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select m from Message m where (m.sender = :user1 and m.receiver = :user2) or (m.sender = :user2 and m.receiver = :user1) order by m.sendTime desc");
        q.setParameter("user1", HelloSession.getUser().getId());
        q.setParameter("user2", this.msgPerson.getId());
        q.setFirstResult(msgCount);
        q.setMaxResults(30);
        List<Message> messages = q.getResultList();
        msgCount += messages.size();
        int n = 0;
        for(Message msg : messages) {
            String color = "#BFCCCE";
            if(n % 2 == 1) color = "#CAD5D7";
            HelloUser user;
            if(Objects.equals(msg.getSender(), HelloSession.getUser().getId())) user = HelloSession.getUser();
            else user = this.msgPerson;
            messagesHtml.insert(0, HelloUI.prepareMessageStripe(user, msg.getContent(), new SimpleDateFormat("yyyy-MM-dd HH:mm").format(msg.getSendTime()), color, HelloSession.getHtmlAvatar()));
            n++;
        }
        StringBuilder output = new StringBuilder(messagesHtml.toString());
        output.insert(0, "<body style=\"font-family: System; margin: 0px;\"><table cellpadding=\"5\" cellspacing=\"0\" style=\"font-size: 12px; width: 100%; border: 0px;\">");
        output.append("</table>");
        output.append("</body>");
        this.rightView.getEngine().loadContent(output.toString());
        em.close();
    }
    
    private void showConversations() {
        List<HelloUser> persons = this.getConversations();
        this.leftVB.getChildren().clear();
        Label header = new Label("lista rozmów");
        header.getStyleClass().add("blueLabelSm");
        this.leftVB.getChildren().add(header);
        if(persons.isEmpty()) {
            Label noResults = new Label("(Brak rozmów do wyświetlenia)");
            noResults.getStyleClass().add("blueLabelSm");
            this.leftVB.getChildren().add(noResults);
        }
        else {
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
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        showMessages();
                    }
                });
                this.leftVB.getChildren().add(row);
                n++;
            }
            if(this.msgPerson == null) {
                this.msgPerson = persons.get(0);
            }
        }
        this.rightView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(msgCount <= 30) rightView.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight)");
            }
        });
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.showConversations();
    }
    
}
