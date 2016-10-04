package org.pawelwaz.helloworkz.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author pawelwaz
 */
public class MessageButton extends ImageView {
    
    private Parent root;
    private Window window;
    
    public MessageButton() {
    }
    
    public MessageButton(WritableImage icon, Parent root, Window window) {
        this.setImage(icon);
        this.root = root;
        this.window = window;
    }
    
    public void openMessageWindow() {
        try {
            Scene scene = new Scene(this.root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e) {
            HelloUI.showError("Wystąpił błąd działania programu.");
        }
    }
    
}
