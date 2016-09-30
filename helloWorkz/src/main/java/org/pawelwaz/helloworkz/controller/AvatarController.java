package org.pawelwaz.helloworkz.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.pawelwaz.helloworkz.util.HelloUI;

/**
 *
 * @author pawelwaz
 */
public class AvatarController extends HelloUI {
    
    @FXML private ImageView avatar;
    
    @FXML private void chooseFile() {
        /*FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Pliki PNG (.png)", "*.PNG");
        chooser.getExtensionFilters().addAll(pngFilter);
        File file = chooser.showOpenDialog(ap.getScene().getWindow());
        if(file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                avatar.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
                byte[] bytes;
                ByteArrayOutputStream out = new ByteArrayOutputStream(262144);
                ImageIO.write(bufferedImage, "png", out);
                out.flush();
                bytes = out.toByteArray();
            }
            catch(IOException ex) {
                this.showError(ex.getMessage());
            }
        }*/
        try {
            File file = new File("classes/img/logosm.png");
            BufferedImage bufferedImage = ImageIO.read(file);
            avatar.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        }
        catch(Exception ex) {
            this.showError(ex.getMessage());
        }
    }
    
}
