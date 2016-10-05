package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Message;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class MessageWindowController extends HelloUI {
    
    @FXML private VBox messageBox;
    @FXML private TextArea input;
    @FXML private AnchorPane userHeader;
    private HelloUser user;
    
    @FXML private void sendMessage() {
        if(input.getText().length() > 0) {
            Message msg = new Message();
            msg.setContent(input.getText());
            msg.setSender(HelloSession.getUser().getId());
            msg.setReceiver(user.getId());
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(msg);
            em.getTransaction().commit();
            em.close();
            input.setText("");
        }
    }
    
    public void addHeader() {
        this.userHeader.getChildren().add(HelloUI.prepareUserDescription(this.user));
    }
    
    public void setUser(HelloUser user) {
        this.user = user;
    }
    
    public HelloUser getUserId() {
        return this.user;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
}
