package org.pawelwaz.helloworkz.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Message;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;
import org.pawelwaz.helloworkz.util.MessageButton;

/**
 *
 * @author pawelwaz
 */
public class MainViewController extends HelloUI {
    
    @FXML private Label loginLabel;
    @FXML private ImageView avatar;
    @FXML private AnchorPane subAp;
    @FXML private AnchorPane msgNotificationBox;
    private boolean msgNotificationExists = false;
    private HelloUser msgNotificationSender = null;
    
    public void showMsgNotification(HelloUser user) {
        this.setMsgNotificationSender(user);
        HBox hb = new HBox();
        hb.setCursor(Cursor.HAND);
        hb.setStyle("-fx-background-color: rgb(255, 255, 255); -fx-padding: 10 10 10 10;");
        ImageView msgIcon = new ImageView();
        msgIcon.setImage(HelloUI.messageButton);
        hb.getChildren().add(msgIcon);
        VBox vb = new VBox();
        Label l1 = new Label("nowa wiadomość od: ");
        vb.getChildren().add(l1);
        Label l2 = new Label(user.getLogin());
        l2.getStyleClass().add("blueLabel");
        l2.setStyle("-fx-text-fill: rgb(0, 0, 0);");
        vb.getChildren().add(l2);
        vb.setAlignment(Pos.CENTER);
        hb.getChildren().add(vb);
        AnchorPane.setTopAnchor(hb, 12.0);
        AnchorPane.setRightAnchor(hb, 15.0);
        this.msgNotificationBox.getChildren().add(hb);
        this.msgNotificationExists = true;
        hb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MessageButton tmp = new MessageButton(HelloUI.messageButton, HelloSession.getMainController(), getMsgNotificationSender());
                tmp.openMessageWindow();
            }
        });
    }
    
    public void removeMsgNotification() {
        this.msgNotificationBox.getChildren().clear();
        this.msgNotificationExists = false;
        this.setMsgNotificationSender(null);
    }
    
    private void checkMessages() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery q = builder.createQuery();
        Root<Message> root = q.from(Message.class);
        q.select(root);
        List<Predicate> preds = new ArrayList();
        preds.add(builder.equal(root.get("receiver"), HelloSession.getUser().getId()));
        preds.add(builder.equal(root.get("seen"), 0));
        q.where(preds.toArray(new Predicate[preds.size()]));
        List<Message> messages = em.createQuery(q).getResultList();
        if(!messages.isEmpty()) {
            for(Message msg : messages) {
                int index = HelloSession.getMsgWindowsIds().indexOf(msg.getSender());
                if(index != -1) {
                    MessageWindowController controller = HelloSession.getMsgWindows().get(index);
                    controller.showMessage(msg, controller.getUser(), controller.getHtmlAvatar());
                    em.getTransaction().begin();
                    msg.setSeen(1);
                    em.getTransaction().commit();
                    controller.blink();
                }
                else if(!this.hasMsgNotification()) {
                    HelloUser sender = em.find(HelloUser.class, msg.getSender());
                    this.showMsgNotification(sender);
                }
            }
        }
        em.close();
    }
    
    public void setMsgNotifiactionExists(boolean msgNotificationExists) {
        this.msgNotificationExists = msgNotificationExists;
    }
    
    public boolean hasMsgNotification() {
        return this.msgNotificationExists;
    }
    
    private void refreshAvatar() {
        avatar.setImage(SwingFXUtils.toFXImage(HelloSession.getUser().getReadyAvatar(), null));
    }
    
    private void refreshLogin() {
        loginLabel.setText(HelloSession.getUser().getLogin());
    }
    
    private void openSub(String fxml) {
        try {
            this.subAp.getChildren().clear();
            this.subAp.getChildren().add((AnchorPane) FXMLLoader.load(this.getClass().getResource("/fxml/" + fxml + ".fxml")));
        }
        catch(Exception e) {
            this.showError("Wystąpił problem z działaniem aplikacji i zostanie ona zamknięta");
            System.exit(1);
        }
    }
    
    @FXML private void goSearchContacts() {
        this.openSub("SearchContacts");
    }
    
    @FXML private void goAvatar() {
        this.openPopup("Avatar", "zmiana awatara");
        this.refreshAvatar();
    }
    
    @FXML private void goLoginChange() {
        this.openPopup("LoginChange", "zmiana loginu");
        this.refreshLogin();
    }
    
    @FXML private void goAccount() {
        this.openPopup("Account", "Dane dodatkowe konta");
    }
    
    @FXML private void goPassword() {
        this.openPopup("Password", "Zmiana hasła");
    }
    
    @FXML private void showAbout() {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle("info");
        a.setHeaderText("helloWorkz");
        a.setContentText("Autor aplikacji: Paweł Wąż\ne-mail: pawelwazit@gmail.com\nInformatyka Inżynierska\nWydział Informatyki i Nauki o Materiałach\nUniwersytet Śląski\n2016");
        a.showAndWait();
    }
    
    private void prepareButtons() {
        try {
            File file = new File("classes/img/messageButton.png");
            BufferedImage bufferedImage = ImageIO.read(file);
            MainViewController.messageButton = SwingFXUtils.toFXImage(bufferedImage, null);
            file = new File("classes/img/addContact.png");
            bufferedImage = ImageIO.read(file);
            MainViewController.addContactButton = SwingFXUtils.toFXImage(bufferedImage, null);
            file = new File("classes/img/removeContact.png");
            bufferedImage = ImageIO.read(file);
            MainViewController.removeContactButton = SwingFXUtils.toFXImage(bufferedImage, null);
        }
        catch(Exception ex) {
            MainViewController.showError("Brak części plików aplikacji. Zostanie ona zamknięta");
            System.exit(1);
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.refreshLogin();
        this.refreshAvatar();
        this.prepareButtons();
        Timeline msgTimeline = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkMessages();
            }
        }));
        msgTimeline.setCycleCount(Animation.INDEFINITE);
        msgTimeline.play();
    }

    /**
     * @return the msgNotificationSender
     */
    public HelloUser getMsgNotificationSender() {
        return msgNotificationSender;
    }

    /**
     * @param msgNotificationSender the msgNotificationSender to set
     */
    public void setMsgNotificationSender(HelloUser msgNotificationSender) {
        this.msgNotificationSender = msgNotificationSender;
    }
    
}
