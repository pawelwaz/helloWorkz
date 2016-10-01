package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;

/**
 *
 * @author pawelwaz
 */
public class MainViewController extends HelloUI {
    
    @FXML private Label loginLabel;
    @FXML private ImageView avatar;
    
    private void refreshAvatar() {
        avatar.setImage(SwingFXUtils.toFXImage(HelloSession.getUser().getReadyAvatar(), null));
    }
    
    @FXML private void goSearchContacts() {
        
    }
    
    @FXML private void goAvatar() {
        this.openPopup("Avatar", "zmiana awatara");
    }
    
    @FXML private void goLoginChange() {
        this.openPopup("LoginChange", "zmiana loginu");
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginLabel.setText(HelloSession.getUser().getLogin());
        this.refreshAvatar();
    }
    
}
