package org.pawelwaz.helloworkz.util;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

/**
 *
 * @author pawelwaz
 */
public class HelloUI implements Initializable {
    
    @FXML protected AnchorPane ap;
    
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
            stage.show(); 
        }
        catch(Exception e) {
            this.showError("Wystąpił błąd działania programu i nastąpi jego zamknięcie. Treść błędu: " + e.getMessage());
            System.exit(1);
        }
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
}
