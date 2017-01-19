/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.util;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.WindowEvent;
import org.pawelwaz.helloworkz.controller.DiscussionWindowController;
import org.pawelwaz.helloworkz.entity.Discussion;

/**
 *
 * @author A20111
 */
public class DiscussionButton extends ImageView {
    
    private HelloUI source;
    private DiscussionWindowController controller = null;
    private Discussion disc;
    
    public DiscussionButton() {
    }
    
    public DiscussionButton(WritableImage icon, HelloUI source, Discussion disc) {
        this.setImage(icon);
        this.source = source;
        this.disc = disc;
    }
    
    public void openDiscussionWindow() {
        int index = HelloSession.getDiscWindowsIds().indexOf(this.disc.getId());
        if(index != -1) {
            HelloSession.getDiscWindows().get(index).toFront();
        }
        else if(this.controller == null || index == -1) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DiscussionWindow.fxml"));
                Parent messageRoot = loader.load();
                this.controller = (DiscussionWindowController) loader.getController();
                this.controller.setDisc(this.disc);
                this.controller.getPosts();
                this.controller.showPosts();
                this.controller.initTimeline();
                Scene scene = new Scene(messageRoot);
                DiscStage stage = new DiscStage();
                stage.setController(this.controller);
                stage.setTitle(this.disc.getTitle());
                stage.setScene(scene);
                HelloSession.getDiscWindows().add(this.controller);
                HelloSession.getDiscWindowsIds().add(this.disc.getId());
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        DiscStage stg = ((DiscStage) event.getSource());
                        HelloSession.getDiscWindows().remove(stg.getController());
                        HelloSession.getDiscWindowsIds().remove(disc.getId());
                        stg.getController().closeWindow();
                    }
                });
                stage.show();
            }
            catch(Exception e) {
                HelloUI.showError("Wystąpił błąd działania aplikacji i zostanie ona zamknięta");
                System.exit(1);
            }
            
        }
    }
    
}
