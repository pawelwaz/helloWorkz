package org.pawelwaz.helloworkz.util;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.WindowEvent;
import org.pawelwaz.helloworkz.controller.MessageWindowController;
import org.pawelwaz.helloworkz.entity.HelloUser;

/**
 *
 * @author pawelwaz
 */
public class MessageButton extends ImageView {
    
    private HelloUI source;
    private MessageWindowController controller = null;
    private HelloUser user;
    
    public MessageButton() {
    }
    
    public MessageButton(WritableImage icon, HelloUI source, HelloUser user) {
        this.setImage(icon);
        this.source = source;
        this.user = user;
    }
    
    public void openMessageWindow() {
        int index = HelloSession.getMsgWindowsIds().indexOf(this.user.getId());
        if(index != -1) {
            HelloSession.getMsgWindows().get(index).toFront();
        }
        else if(this.controller == null || index == -1) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MessageWindow.fxml"));
                Parent messageRoot = loader.load();
                this.controller = (MessageWindowController) loader.getController();
                this.controller.setUser(this.user);
                this.controller.addHeader();
                Scene scene = new Scene(messageRoot);
                MsgStage stage = new MsgStage();
                stage.setController(this.controller);
                stage.setTitle(this.user.getLogin());
                stage.setScene(scene);
                HelloSession.getMsgWindows().add(this.controller);
                HelloSession.getMsgWindowsIds().add(this.user.getId());
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        MsgStage stg = ((MsgStage) event.getSource());
                        HelloSession.getMsgWindows().remove(stg.getController());
                        HelloSession.getMsgWindowsIds().remove(user.getId());
                        stg.getController().closeWindow();
                    }
                });
                stage.show();
                if(HelloSession.getMainController().hasMsgNotification()) {
                    if(this.user.getId().equals(HelloSession.getMainController().getMsgNotificationSender().getId())) {
                        HelloSession.getMainController().removeMsgNotification();
                    }
                }
            }
            catch(Exception e) {
                HelloUI.showError("Wystąpił błąd działania aplikacji i zostanie ona zamknięta");
                System.exit(1);
            }
            
        }
    }
    
}
