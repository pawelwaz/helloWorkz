package org.pawelwaz.helloworkz.controller;

import java.util.List;
import org.pawelwaz.helloworkz.util.HelloUI;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.persistence.*;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class RegisterController extends HelloUI {
    
    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private PasswordField retype;
    
    @FXML private void goLogin() {
        this.goToPopup("Login", "logowanie");
    }
    
    @FXML private void registerAction() {
        if(login.getText().equals("") || password.getText().equals("") || retype.getText().equals("")) {
            this.showError("Wszystkie pola muszą być wypełnione");
        }
        else if(!password.getText().equals(retype.getText())) {
            this.showError("Podane hasła różnią się");
        }
        else {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query query = em.createQuery("select user from HelloUser user where user.login = '" + login.getText() + "'");
            query.setMaxResults(1);
            List<HelloUser> check = query.getResultList();
            if(check.isEmpty()) {
                StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
                HelloUser newUser = new HelloUser(login.getText(), encryptor.encryptPassword(password.getText()));
                em.getTransaction().begin();
                em.persist(newUser);
                em.getTransaction().commit();
                em.close();
                this.showInfo("Konto zostało utworzone. Zaloguj się, aby móc korzystać z aplikacji");
                this.goLogin();
            }
            else {
                this.showError("Wybrany login jest już zajęty");
                em.close();
            }
        }
    }
    
}
