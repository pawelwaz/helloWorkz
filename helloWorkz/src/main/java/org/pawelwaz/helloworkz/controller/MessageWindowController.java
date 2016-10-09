package org.pawelwaz.helloworkz.controller;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javax.persistence.EntityManager;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Message;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author pawelwaz
 */
public class MessageWindowController extends HelloUI {
    
    @FXML private TextArea input;
    @FXML private AnchorPane userHeader;
    @FXML private WebView web;
    private HelloUser user;
    private String htmlCode = "";
    private int msgNumber = 0;
    private URI htmlAvatar = null;
    
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
            this.showMessage(msg, HelloSession.getUser(), HelloSession.getHtmlAvatar());
            em.close();
            input.setText("");
            event.consume();
        }
    }
    
    @FXML public void showMessage(Message msg, HelloUser user, URI htmlAvatar) {
        String color = "#BFCCCE";
        if(this.msgNumber % 2 == 1) color = "#CAD5D7";
        StringBuilder output = new StringBuilder("<body style=\"font-family: System; margin: 0px;\"><table cellpadding=\"5\" cellspacing=\"0\" style=\"font-size: 12px; width: 100%; border: 0px;\">");
        this.htmlCode = this.htmlCode + HelloUI.prepareMessageStripe(user, msg.getContent(), new SimpleDateFormat("yyyy-MM-dd HH:mm").format(msg.getSendTime()), color, htmlAvatar);
        output.append(this.htmlCode);
        output.append("</table>");
        output.append("</body>");
        this.web.getEngine().loadContent(output.toString());
        this.msgNumber++;
    }
    
    public void addHeader() {
        this.userHeader.getChildren().add(HelloUI.wrapNode(HelloUI.prepareUserDescription(this.user, "rgb(255, 255, 255)"), null, 0.0));
    }
    
    public void setUser(HelloUser user) {
        this.user = user;
        this.user.prepareAvatar();
        HelloUI.saveTmpImage(this.user.getReadyAvatar(), this.user.getLogin());
        File file = new File("tmp/" + this.user.getLogin() + ".png");
        this.htmlAvatar = file.toURI();
    }
    
    public HelloUser getUser() {
        return this.user;
    }
    
    public URI getHtmlAvatar() {
        return this.htmlAvatar;
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
        this.web.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                web.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight)");
            }
        });
    }
    
}
