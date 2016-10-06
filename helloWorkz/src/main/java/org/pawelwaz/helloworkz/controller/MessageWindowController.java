package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Message;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;
import javafx.stage.Stage;

/**
 *
 * @author pawelwaz
 */
public class MessageWindowController extends HelloUI {
    
    @FXML private VBox messageBox;
    @FXML private TextArea input;
    @FXML private AnchorPane userHeader;
    private HelloUser user;
    
    @FXML private void sendMessage(KeyEvent event) {
        if(input.getText().length() > 0 && event.getCode().equals(KeyCode.ENTER)) {
            Message msg = new Message();
            msg.setContent(input.getText());
            msg.setSender(HelloSession.getUser().getId());
            msg.setReceiver(user.getId());
            msg.setSeen(0);
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(msg);
            em.getTransaction().commit();
            em.close();
            em = JpaUtil.getFactory().createEntityManager();
            msg = em.find(Message.class, msg.getId());
            this.showMessage(msg);
            em.close();
            input.setText("");
            event.consume();
        }
    }
    
    @FXML public void showMessage(Message msg) {
        
    }
    
    public void addHeader() {
        this.userHeader.getChildren().add(HelloUI.wrapNode(HelloUI.prepareUserDescription(this.user, "rgb(255, 255, 255)"), null, 0.0));
    }
    
    public void setUser(HelloUser user) {
        this.user = user;
    }
    
    public HelloUser getUserId() {
        return this.user;
    }
    
    public void toFront() {
        Stage thisStage = (Stage) this.ap.getScene().getWindow();
        if(thisStage.isIconified()) {
            thisStage.setIconified(false);
        }
        thisStage.toFront();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
}
