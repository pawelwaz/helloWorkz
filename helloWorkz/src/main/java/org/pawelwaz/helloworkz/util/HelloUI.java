package org.pawelwaz.helloworkz.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Discussion;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Membership;

/**
 *
 * @author pawelwaz
 */
public class HelloUI implements Initializable {
    
    @FXML protected AnchorPane ap;
    protected static WritableImage messageButton;
    protected static WritableImage addContactButton;
    protected static WritableImage removeContactButton;
    protected static WritableImage declineButton;
    protected static WritableImage acceptButton;
    protected static WritableImage settingsButton;
    protected static WritableImage viewButton;
    protected static WritableImage invitationButton;
    
    public void goToPopup(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxml + ".fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show(); 
            Stage thisWindow = (Stage) ap.getScene().getWindow();
            thisWindow.close();
        }
        catch(Exception e) {
            this.showError("Wystąpił błąd działania programu i nastąpi jego zamknięcie. Treść błędu: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public static void inviteUser(Long id) {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select m from Membership m where m.active = 1 and m.users = 1 and m.hellouser = " + HelloSession.getUser().getId());
        List<Membership> result = q.getResultList();
        if(result.isEmpty()) HelloUI.showInfo("Nie posiadasz w żadnej grupie członkostwa uprawniającego do zapraszania nowych użytkowników");
        else {
            HelloSession.setInviteId(id);
            HelloSession.getMainController().openPopup("InviteUser", "Wysyłanie zaproszenia do grupy");
            HelloSession.setInviteId(null);
        }
        em.close();
    }
    
    public static AnchorPane insertInvitationButton(String styleClass, final Long id) {
        ImageView btn = new ImageView();
        btn.setImage(HelloUI.invitationButton);
        btn.setCursor(Cursor.HAND);
        Tooltip.install(btn, new Tooltip("Zaproś do grupy"));
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                HelloUI.inviteUser(id);
            }
        });
        return HelloUI.wrapNode(btn, styleClass,  15.0, 15.0, 0.0, 5.0);
    }
    
    public static AnchorPane insertEmptyCell(String styleClass) {
        AnchorPane result = new AnchorPane();
        HelloUI.setAnchors(result, 0.0);
        result.getStyleClass().add(styleClass);
        return result;
    }
    
    public void openPopup(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxml + ".fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(ap.getScene().getWindow());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.showAndWait();
        }
        catch(Exception e) {
            this.showError("Wystąpił błąd działania programu i nastąpi jego zamknięcie. Treść błędu: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public static HBox prepareUserDescription(HelloUser user, String color) {
        HBox result = new HBox();
        result.getChildren().add(HelloUI.prepareUserAvatar(user));
        VBox desc = new VBox();
        Label userLabel = new Label(user.getLogin());
        userLabel.getStyleClass().add("smallHeader");
        if(color != null) userLabel.setStyle("-fx-text-fill: " + color);
        desc.getChildren().add(userLabel);
        if(user.getName() != null || user.getSurname() != null) {
            Label nameSurname = new Label("");
            if(color != null) nameSurname.setStyle("-fx-text-fill: " + color);
            if(user.getName() != null) nameSurname.setText(user.getName() + " ");
            if(user.getSurname() != null) nameSurname.setText(nameSurname.getText() + user.getSurname());
            nameSurname.getStyleClass().add("description");
            desc.getChildren().add(nameSurname);
        }
        if(user.getPhone() != null || user.getEmail() != null) {
            Label phoneEmail = new Label("");
            if(color != null) phoneEmail.setStyle("-fx-text-fill: " + color);
            if(user.getPhone().length() > 0) phoneEmail.setText("tel. " + user.getPhone() + " ");
            if(user.getEmail().length() > 0) phoneEmail.setText(phoneEmail.getText() + "e-mail: " + user.getEmail());
            phoneEmail.getStyleClass().add("description");
            desc.getChildren().add(phoneEmail);
        }
        if(user.getOrganisation() != null || user.getJob() != null) {
            Label orgJob = new Label("");
            if(color != null) orgJob.setStyle("-fx-text-fill: " + color);
            if(user.getOrganisation().length() > 0) orgJob.setText("organizacja: " + user.getOrganisation() + " ");
            if(user.getJob().length() > 0) orgJob.setText(orgJob.getText() + "stanowisko: " + user.getJob());
            orgJob.getStyleClass().add("description");
            desc.getChildren().add(orgJob);
        }
        result.getChildren().add(HelloUI.wrapNode(desc, null, 0.0));
        return result;
    }
    
    public static AnchorPane prepareUserAvatar(HelloUser user) {
        ImageView userAvatar = new ImageView();
        user.prepareAvatar();
        userAvatar.setImage(SwingFXUtils.toFXImage(user.getReadyAvatar(), null));
        userAvatar.setFitWidth(70.0);
        userAvatar.setFitHeight(70.0);
        return HelloUI.wrapNode(userAvatar, null, 5.0);
    }
    
    protected AnchorPane insertMessageButton(String styleClass, HelloUser user) {
        MessageButton img = new MessageButton(HelloUI.messageButton, this, user);
        img.setCursor(Cursor.HAND);
        Tooltip.install(img, new Tooltip("wyślij wiadomość"));
        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((MessageButton)(event.getSource())).openMessageWindow();
            }
        });
        AnchorPane result = this.wrapNode(img, styleClass, 15.0, 15.0, 0.0, 5.0);
        return result;
    }
    
    protected AnchorPane insertDiscussionButton(String styleClass, Discussion disc) {
        DiscussionButton img = new DiscussionButton(HelloUI.viewButton, this, disc);
        img.setCursor(Cursor.HAND);
        Tooltip.install(img, new Tooltip("otwórz dyskusję"));
        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((DiscussionButton)(event.getSource())).openDiscussionWindow();
            }
        });
        AnchorPane result = this.wrapNode(img, styleClass, 5.0, 5.0, 0.0, 5.0);
        return result;
    }
    
    protected AnchorPane insertContactButton(String styleClass, HelloUser user, boolean add, boolean swap) {
        ContactButton img = new ContactButton(user, add, swap);
        img.setCursor(Cursor.HAND);
        if(add) Tooltip.install(img, new Tooltip("dodaj do kontaktów"));
        else Tooltip.install(img, new Tooltip("usuń z kontaktów"));
        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ContactButton parent = (ContactButton) event.getSource();
                parent.handleContact();
            }
        });
        AnchorPane result = this.wrapNode(img, styleClass, 15.0, 15.0, 0.0, 5.0);
        return result;
    }
    
    public static String prepareNotificationStripe(String content, String received, String color) {
        StringBuilder result = new StringBuilder("<tr style = \"background-color: " + color + "; margin: 0px;\">");
        result.append("<td style='font-size: 12px; padding: 15px;'>" + content + "</td>");
        result.append("<td style='font-size: 12px; padding: 15px; width: 100px;'>" + received + "</td>");
        result.append("</tr>");
        return result.toString();
    }
    
    public static String prepareNotificationStripeBold(String content, String received, String color) {
        StringBuilder result = new StringBuilder("<tr style = \"background-color: " + color + "; margin: 0px;\">");
        result.append("<td style='font-size: 12px; padding: 15px;'><b>" + content + "</b></td>");
        result.append("<td style='font-size: 12px; padding: 15px; width: 100px;'>" + received + "</td>");
        result.append("</tr>");
        return result.toString();
    }
    
    public static String prepareMessageStripe(HelloUser user, String content, String time, String color, URI avatar) {
        StringBuilder result = new StringBuilder("<tr style = \"background-color: " + color + "; margin: 0px;\">");
        result.append("<td valign=\"top\" width=\"50\"><img src=\"" + avatar + "\" width=\"50\" height=\"50\" /></td>");
        result.append("<td><b>" + user.getLogin() + "</b> (" + time + ")<br />" + content + "</td>");
        result.append("</tr>");
        return result.toString();
    }
    
    public static void saveTmpImage(BufferedImage img, String name) {
        try {
            File file = new File("tmp/" + name + ".png");
            file.mkdirs();
            ImageIO.write(img, "png", file);
        }
        catch(Exception ex) {
            HelloUI.showError("Wystąpił błąd działania aplikacji. Kod błędu: " + ex.getMessage());
        }
    }
    
    public static AnchorPane wrapNode(Node node, String styleClass, double anchors) {
        AnchorPane container = new AnchorPane();
        if(styleClass != null) container.getStyleClass().add(styleClass);
        container.getChildren().add(node);
        HelloUI.setAnchors(node, anchors);
        HelloUI.setAnchors(container, 0.0);
        return container;
    }
    
    public static AnchorPane wrapNode(Node node, String styleClass, double top, double bottom, double left, double right) {
        AnchorPane container = new AnchorPane();
        if(styleClass != null) container.getStyleClass().add(styleClass);
        container.getChildren().add(node);
        HelloUI.setAnchors(node, top, bottom, left, right);
        HelloUI.setAnchors(container, 0.0);
        return container;
    }
    
    public static void setAnchors(Node node, double value) {
        AnchorPane.setBottomAnchor(node, value);
        AnchorPane.setTopAnchor(node, value);
        AnchorPane.setLeftAnchor(node, value);
        AnchorPane.setRightAnchor(node, value);
    }
    
    public static void setAnchors(Node node, double top, double bottom, double left, double right) {
        AnchorPane.setBottomAnchor(node, bottom);
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setRightAnchor(node, right);
    }
    
    public void goTo(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxml + ".fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show(); 
            Stage thisWindow = (Stage) ap.getScene().getWindow();
            thisWindow.close();
        }
        catch(Exception e) {
            this.showError("Wystąpił błąd działania programu i nastąpi jego zamknięcie. Treść błędu: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public void closeWindow() {
        Stage thisWindow = (Stage) ap.getScene().getWindow();
        thisWindow.close();
    }
    
    public static void showError(String content) {
        Alert a = new Alert(Alert.AlertType.ERROR, content);
        a.setTitle("");
        a.setHeaderText("Błąd");
        a.showAndWait();
    }
    
    public static void showInfo(String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, content);
        a.setTitle("");
        a.setHeaderText("Informacja");
        a.showAndWait();
    }
    
    public static boolean showConfirmation(String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("Potwierdzenie");
        alert.setContentText(content);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOk) return true;
        else return false;
    }
    
    public void refresh() {
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
}
