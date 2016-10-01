package org.pawelwaz.helloworkz.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class AvatarController extends HelloUI {
    
    @FXML private ImageView avatar;
    private BufferedImage readyToUse;
    private byte[] byteImage;
    
    @FXML private void chooseFile() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Pliki PNG (.png)", "*.PNG");
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Pliki JPG (.jpg)", "*.jpg");
        chooser.getExtensionFilters().addAll(pngFilter, jpgFilter);
        File file = chooser.showOpenDialog(ap.getScene().getWindow());
        if(file != null) {
            try {
                this.readyToUse = ImageIO.read(file);
                ByteArrayOutputStream out = new ByteArrayOutputStream(262144);
                ImageIO.write(this.readyToUse, "jpg", out);
                out.flush();
                this.byteImage = out.toByteArray();
                this.refresh();
            }
            catch(IOException ex) {
                this.showError("Wystąpił problem z wczytaniem obrazu");
            }
        }
    }
    
    private void refresh() {
        this.avatar.setImage(SwingFXUtils.toFXImage(this.readyToUse, null));
    }
    
    @FXML private void avatarAction() {
        try {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            HelloUser user = em.find(HelloUser.class, HelloSession.getUser().getId());
            em.getTransaction().begin();
            user.setAvatar(this.byteImage);
            user.setReadyAvatar(this.readyToUse);
            em.getTransaction().commit();
            HelloSession.setUser(user);
            em.close();
            this.closeWindow();
        }
        catch(Exception ex) {
            this.showError("Rozmiar wybranego obrazu jest zbyt duży");
        }
    }
    
    @FXML private void cancel() {
        this.closeWindow();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.readyToUse = HelloSession.getUser().getReadyAvatar();
        this.byteImage = HelloSession.getUser().getAvatar();
        this.refresh();
    }
    
}
