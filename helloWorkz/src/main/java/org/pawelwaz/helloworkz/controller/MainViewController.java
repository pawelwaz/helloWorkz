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
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Message;
import org.pawelwaz.helloworkz.entity.Notification;
import org.pawelwaz.helloworkz.entity.Task;
import org.pawelwaz.helloworkz.entity.TaskUser;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloTime;
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
    @FXML private Label notificationLabel;
    
    public void setNotificationNumber(int n) {
        if(n > 0) this.notificationLabel.setText("Powiadomienia (" + n + ")");
        else this.notificationLabel.setText("Powiadomienia");
    }
    
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
        if(HelloSession.isNotificationActive()) {
            ((NotificationController) HelloSession.getSubController()).stopTimeline();
            HelloSession.setNotificationActive(false);
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxml + ".fxml"));
            Parent root = loader.load();
            HelloSession.setSubController((HelloUI) loader.getController());
            this.subAp.getChildren().clear();
            this.subAp.getChildren().add((AnchorPane) root);
        }
        catch(Exception e) {
            MainViewController.showError("Wystąpił problem z działaniem aplikacji i zostanie ona zamknięta" + e.getMessage());
            System.exit(1);
        }
    }
    
    @FXML public void goInvitations() {
        this.openSub("Invitations");
    }
    
    @FXML public void goUserTasks() {
        this.openSub("UserTasks");
    }
    
    @FXML public void goNotifications() {
        this.openSub("Notification");
        HelloSession.setNotificationActive(true);
    }
    
    @FXML public void goMessages() {
        this.openSub("Messages");
    }
    
    @FXML public void goContacts() {
        this.openSub("Contacts");
    }
    
    @FXML public void goUserGroups() {
        this.openSub("UserGroups");
    }
    
    @FXML public void goSearchGroups() {
        this.openSub("SearchGroups");
    }
    
    @FXML public void goGroupView() {
        this.openSub("GroupView");
    }
    
    @FXML public void goSearchContacts() {
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
    
    @FXML public void goGroupEdit() {
        if(HelloSession.getGroupId() == null) this.openPopup("GroupEdit", "Tworzenie grupy roboczej");
        else this.openPopup("GroupEdit", "Edycja grupy roboczej");
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
            file = new File("classes/img/accept.png");
            bufferedImage = ImageIO.read(file);
            MainViewController.acceptButton = SwingFXUtils.toFXImage(bufferedImage, null);
            file = new File("classes/img/decline.png");
            bufferedImage = ImageIO.read(file);
            MainViewController.declineButton = SwingFXUtils.toFXImage(bufferedImage, null);
            file = new File("classes/img/settings.png");
            bufferedImage = ImageIO.read(file);
            MainViewController.settingsButton = SwingFXUtils.toFXImage(bufferedImage, null);
            file = new File("classes/img/search.png");
            bufferedImage = ImageIO.read(file);
            MainViewController.viewButton = SwingFXUtils.toFXImage(bufferedImage, null);
            file = new File("classes/img/invitationButton.png");
            bufferedImage = ImageIO.read(file);
            MainViewController.invitationButton = SwingFXUtils.toFXImage(bufferedImage, null);
        }
        catch(Exception ex) {
            MainViewController.showError("Brak części plików aplikacji. Zostanie ona zamknięta");
            System.exit(1);
        }
    }
    
    public void checkNots() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select n from Notification n where n.received is null and n.hellouser = " + HelloSession.getUser().getId());
        List<Notification> result = q.getResultList();
        if(!result.isEmpty()) {
            this.setNotificationNumber(result.size());
        }
        em.close();
    }
    
    public void checkTasks() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select tu from TaskUser tu join tu.taskJoin t where tu.hellouser = " + HelloSession.getUser().getId() + " and t.status = 0");
        List<TaskUser> result = q.getResultList();
        for(TaskUser tu : result) {
            Task t = tu.getTaskJoin();
            if(tu.getNot3() == 0 && t.isAfterDeadline()) {
                em.getTransaction().begin();
                Notification n = new Notification(HelloSession.getUser().getId(), "Upłynął termin twojego zadania nr " + t.getNumber() + " w grupie " + t.getWorkgroupJoin().getGroup_name() + ".<br />Opis zadania: " + t.getContent());
                tu.setNot3(1);
                em.persist(tu);
                em.persist(n);
                em.getTransaction().commit();
            }
            if(tu.getNot3() == 0 && tu.getNot2() == 0 && t.isOnDeadline()) {
                em.getTransaction().begin();
                Notification n = new Notification(HelloSession.getUser().getId(), "Dziś o godz. " + t.getDeadlinehour() + " upływa termin twojego zadania nr " + t.getNumber() + " w grupie " + t.getWorkgroupJoin().getGroup_name() + ".<br />Opis zadania: " + t.getContent());
                tu.setNot2(1);
                em.persist(tu);
                em.persist(n);
                em.getTransaction().commit();
            }
            if(tu.getNot3() == 0 && tu.getNot2() == 0 && tu.getNot1() == 0 && t.isDayBeforeDeadline()) {
                em.getTransaction().begin();
                Notification n = new Notification(HelloSession.getUser().getId(), "Jutro o godz. " + t.getDeadlinehour() + " upływa termin twojego zadania nr " + t.getNumber() + " w grupie " + t.getWorkgroupJoin().getGroup_name() + ".<br />Opis zadania: " + t.getContent());
                tu.setNot1(1);
                em.persist(tu);
                em.persist(n);
                em.getTransaction().commit();
            }
        }
        em.close();
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
        Timeline notsTimeline = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkNots();
            }
        }));
        notsTimeline.setCycleCount(Animation.INDEFINITE);
        notsTimeline.play();
        Timeline tasksTimeline = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkTasks();
            }
        }));
        tasksTimeline.setCycleCount(Animation.INDEFINITE);
        tasksTimeline.play();
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
