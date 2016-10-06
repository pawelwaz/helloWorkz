package org.pawelwaz.helloworkz.util;

import java.net.URL;
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
import org.pawelwaz.helloworkz.controller.MessageWindowController;
import org.pawelwaz.helloworkz.entity.HelloUser;

/**
 *
 * @author pawelwaz
 */
public class HelloUI implements Initializable {
    
    @FXML protected AnchorPane ap;
    protected static WritableImage messageButton;
    
    
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
            if(user.getPhone() != null) phoneEmail.setText("tel. " + user.getPhone() + " ");
            if(user.getEmail() != null) phoneEmail.setText(phoneEmail.getText() + "e-mail: " + user.getEmail());
            phoneEmail.getStyleClass().add("description");
            desc.getChildren().add(phoneEmail);
        }
        if(user.getOrganisation() != null || user.getJob() != null) {
            Label orgJob = new Label("");
            if(color != null) orgJob.setStyle("-fx-text-fill: " + color);
            if(user.getOrganisation() != null) orgJob.setText("organizacja: " + user.getOrganisation() + " ");
            if(user.getJob() != null) orgJob.setText(orgJob.getText() + "stanowisko: " + user.getJob());
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
        AnchorPane.setLeftAnchor(img, 0.0);
        return result;
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
            if(fxml.equals("MainView")) {
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Platform.exit();
                    }
                });
            }
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
}
