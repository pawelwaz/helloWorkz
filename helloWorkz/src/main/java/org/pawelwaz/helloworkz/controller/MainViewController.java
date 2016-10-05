package org.pawelwaz.helloworkz.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;

/**
 *
 * @author pawelwaz
 */
public class MainViewController extends HelloUI {
    
    @FXML private Label loginLabel;
    @FXML private ImageView avatar;
    @FXML private AnchorPane subAp;
    
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
    }
    
}
