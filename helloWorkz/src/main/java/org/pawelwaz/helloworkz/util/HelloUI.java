package org.pawelwaz.helloworkz.util;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

/**
 *
 * @author pawelwaz
 */
public class HelloUI implements Initializable {
    
    @FXML protected AnchorPane ap;
    
    public void goToPopup(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxml + ".fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show(); 
            Stage thisWindow = (Stage) ap.getScene().getWindow();
            thisWindow.close();
        }
        catch(Exception e) {
            this.showError("Wystąpił błąd działania programu i nastąpi jego zamknięcie. Treść błędu: " + e.getMessage());
            System.exit(1);
        }
    }
    
    public void showError(String content) {
        Alert a = new Alert(AlertType.ERROR, content);
        a.setTitle("");
        a.initModality(Modality.APPLICATION_MODAL);
        a.initOwner(ap.getScene().getWindow());
        a.setHeaderText("Błąd");
        a.showAndWait();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
}
