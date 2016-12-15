package org.pawelwaz.helloworkz.controller;

import java.io.File;
import java.net.URI;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Message;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;
import org.pawelwaz.helloworkz.util.MsgStage;

/**
 *
 * @author pawelwaz
 */
public class MessagesController extends HelloUI {
    
    @FXML private VBox leftVB;
    @FXML private WebView rightView;
    @FXML private Label header;
    private HelloUser msgPerson = null;
    private StringBuilder messagesHtml = new StringBuilder("");
    private URI htmlAvatar = null;
    
    @FXML private void openChat() {
        if(this.msgPerson != null) {
            int index = HelloSession.getMsgWindowsIds().indexOf(this.msgPerson.getId());
            if(index != -1) {
                HelloSession.getMsgWindows().get(index).toFront();
            }
            else if(index == -1) {
                try {
                    final HelloUser user = msgPerson;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MessageWindow.fxml"));
                    Parent messageRoot = loader.load();
                    final MessageWindowController controller = (MessageWindowController) loader.getController();
                    controller.setUser(user);
                    controller.addHeader();
                    Scene scene = new Scene(messageRoot);
                    MsgStage stage = new MsgStage();
                    stage.setController(controller);
                    stage.setTitle(user.getLogin());
                    stage.setScene(scene);
                    HelloSession.getMsgWindows().add(controller);
                    HelloSession.getMsgWindowsIds().add(user.getId());
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            MsgStage stg = ((MsgStage) event.getSource());
                            HelloSession.getMsgWindows().remove(stg.getController());
                            HelloSession.getMsgWindowsIds().remove(user.getId());
                            stg.getController().closeWindow();
                        }
                    });
                    stage.show();
                    if(HelloSession.getMainController().hasMsgNotification()) {
                        if(user.getId().equals(HelloSession.getMainController().getMsgNotificationSender().getId())) {
                            HelloSession.getMainController().removeMsgNotification();
                        }
                    }
                }
                catch(Exception e) {
                    HelloUI.showError("Wystąpił błąd działania aplikacji i zostanie ona zamknięta");
                    System.exit(1);
                }

            }
        }
    }
    
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
    
    private void showMessages() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select m from Message m where (m.sender = :user1 and m.receiver = :user2) or (m.sender = :user2 and m.receiver = :user1) order by m.sendTime desc");
        q.setParameter("user1", HelloSession.getUser().getId());
        q.setParameter("user2", this.msgPerson.getId());
        List<Message> messages = q.getResultList();
        this.messagesHtml = new StringBuilder("");
        int n = 0;
        for(Message msg : messages) {
            String color = "#BFCCCE";
            if(n % 2 == 1) color = "#CAD5D7";
            HelloUser user;
            URI msgAvatar;
            if(Objects.equals(msg.getSender(), HelloSession.getUser().getId())) {
                user = HelloSession.getUser();
                msgAvatar = HelloSession.getHtmlAvatar();
            }
            else {
                user = this.msgPerson;
                msgAvatar = htmlAvatar;
            }
            messagesHtml.insert(0, HelloUI.prepareMessageStripe(user, msg.getContent(), new SimpleDateFormat("yyyy-MM-dd HH:mm").format(msg.getSendTime()), color, msgAvatar));
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
        Label headerLeft = new Label("lista rozmów");
        headerLeft.getStyleClass().add("blueLabelSm");
        this.leftVB.getChildren().add(headerLeft);
        if(persons.isEmpty()) {
            Label noResults = new Label("(Brak rozmów do wyświetlenia)");
            noResults.getStyleClass().add("blueLabelSm");
            this.leftVB.getChildren().add(noResults);
            this.header.setVisible(false);
        }
        else {
            int n = 0;
            for(final HelloUser person : persons) {
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
                        msgPerson = person;
                        msgPerson.prepareAvatar();
                        HelloUI.saveTmpImage(msgPerson.getReadyAvatar(), msgPerson.getLogin());
                        File file = new File("tmp/" + msgPerson.getLogin() + ".png");
                        htmlAvatar = file.toURI();
                        header.setText("Rozmowa z użytkownikiem " + msgPerson.getLogin() + " (kliknij, aby otworzyć czat)");
                        showMessages();
                    }
                });
                this.leftVB.getChildren().add(row);
                n++;
            }
        }
        this.rightView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                rightView.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight)");
            }
        });
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.showConversations();
    }
    
}
