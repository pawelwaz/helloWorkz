package org.pawelwaz.helloworkz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
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
    private Long userId;
    
    @FXML private void sendMessage() {
        if(input.getText().length() > 0) {
            Message msg = new Message();
            msg.setContent(input.getText());
            msg.setSender(HelloSession.getUser().getId());
            msg.setReceiver(userId);
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(msg);
            em.getTransaction().commit();
            em.close();
            input.setText("");
        }
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getUserId() {
        return this.userId;
    }
    
}
