package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.persistence.*;
import org.pawelwaz.helloworkz.entity.HelloUser;
import static org.pawelwaz.helloworkz.util.FieldType.LOGIN;
import org.pawelwaz.helloworkz.util.FormField;
import org.pawelwaz.helloworkz.util.HelloForm;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class LoginChangeController extends HelloUI {
    
    @FXML private TextField login;
    @FXML private Label header;
    
    @FXML private void loginChangeAction() {
        FormField[] fields = {new FormField("login", LOGIN, login.getText(), true)};
        HelloForm form = new HelloForm(fields);
        if(form.isValid()) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select u from HelloUser u where u.login = '" + login.getText() + "'");
            List<HelloUser> check = q.getResultList();
            if(check.isEmpty()) {
                HelloUser user = em.find(HelloUser.class, HelloSession.getUser().getId());
                em.getTransaction().begin();
                user.setLogin(login.getText());
                user.prepareAvatar();
                HelloUI.saveTmpImage(user.getReadyAvatar(), user.getLogin());
                em.getTransaction().commit();
                HelloSession.setUser(user);
                em.close();
                this.showInfo("Login został zmieniony");
                this.closeWindow();
                return;
            }
            this.showError("Wpisany login jest już zajęty");
            em.close();
        }
    }
    
    @FXML private void cancel() {
        this.closeWindow();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        header.setText(header.getText() + HelloSession.getUser().getLogin());
    }
    
}
