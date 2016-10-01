package org.pawelwaz.helloworkz.controller;

import java.util.List;
import org.pawelwaz.helloworkz.util.HelloUI;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.persistence.*;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.pawelwaz.helloworkz.entity.HelloUser;
import static org.pawelwaz.helloworkz.util.FieldType.*;
import org.pawelwaz.helloworkz.util.FormField;
import org.pawelwaz.helloworkz.util.HelloForm;
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
        FormField[] fields = {
            new FormField("login", LOGIN, login.getText(), true),
            new FormField("hasło", PASSWORD, password.getText(), true)
        };
        HelloForm form = new HelloForm(fields);
        
        if(form.isValid()) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("Select u from HelloUser u where u.login = '" + login.getText() + "'");
            q.setMaxResults(1);
            List<HelloUser> check = q.getResultList();
            if(!check.isEmpty()) {
                StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
                HelloUser checkPass = check.get(0);
                if(encryptor.checkPassword(password.getText(), checkPass.getPassword())) {
                    em.close();
                    checkPass.prepareAvatar();
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
