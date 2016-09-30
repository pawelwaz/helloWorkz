package org.pawelwaz.helloworkz.controller;

import java.util.List;
import org.pawelwaz.helloworkz.util.HelloUI;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.persistence.*;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class LoginController extends HelloUI {
    
    @FXML private TextField login;
    @FXML private PasswordField password;
    
    @FXML private void loginAction() {
        if(login.getText().length() == 0 || password.getText().length() == 0) {
            this.showError("Pola logowania muszą być wypełnione");
        }
        else {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("Select u from HelloUser u where u.login = '" + login.getText() + "'");
            q.setMaxResults(1);
            List<HelloUser> check = q.getResultList();
            if(!check.isEmpty()) {
                StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
                HelloUser checkPass = check.get(0);
                if(encryptor.checkPassword(password.getText(), checkPass.getPassword())) {
                    em.close();
                    HelloSession.setUser(checkPass);
                    this.goTo("MainView", "helloWorkz");
                    return;
                }
            }
            this.showError("Błędne dane logowania");
            em.close();
        }
    }
    
    @FXML private void goRegister() {
        this.goToPopup("Register", "rejestracja");
    }
    
}
