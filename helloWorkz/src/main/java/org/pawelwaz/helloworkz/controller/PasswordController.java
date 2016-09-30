package org.pawelwaz.helloworkz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javax.persistence.EntityManager;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class PasswordController extends HelloUI {
    
    @FXML private PasswordField old;
    @FXML private PasswordField newPass;
    @FXML private PasswordField retype;
    
    @FXML
    private void passwordAction() {
        if(old.getText().length() == 0 || newPass.getText().length() == 0 || retype.getText().length() == 0) {
            this.showError("Wszystkie pola musza być wypełnione");
        }
        else if(!newPass.getText().equals(retype.getText())) {
            this.showError("Nowe hasło zostało przepisane niepoprawnie");
        }
        else {
            StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
            if(encryptor.checkPassword(old.getText(), HelloSession.getUser().getPassword())) {
                HelloSession.getUser().setPassword(encryptor.encryptPassword(newPass.getText()));
                EntityManager em = JpaUtil.getFactory().createEntityManager();
                HelloUser tmp = em.find(HelloUser.class, HelloSession.getUser().getId());
                em.getTransaction().begin();
                tmp.setPassword(HelloSession.getUser().getPassword());
                em.getTransaction().commit();
                em.close();
                this.showInfo("Hasło zostało zmienione");
                this.closeWindow();
            }
            else {
                this.showError("Błędne stare hasło");
            }
        }
    }
    
    @FXML
    private void cancel() {
        this.closeWindow();
    }
    
}
