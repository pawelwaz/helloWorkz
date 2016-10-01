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
        FormField[] fields = {
            new FormField("login", LOGIN, login.getText(), true),
            new FormField("hasło", PASSWORD, password.getText(), true),
            new FormField("powtórz hasło", PASSWORD, retype.getText(), true)
        };
        HelloForm form = new HelloForm(fields);
        
        if(!password.getText().equals(retype.getText())) {
            this.showError("Podane hasła różnią się");
        }
        else if(form.isValid() && this.verifyLogin(login.getText())) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query query = em.createQuery("select user from HelloUser user where user.login = '" + login.getText() + "'");
            query.setMaxResults(1);
            List<HelloUser> check = query.getResultList();
            if(check.isEmpty()) {
                StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
                HelloUser newUser = new HelloUser(login.getText(), encryptor.encryptPassword(password.getText()));
                newUser.setDefaultAvatar();
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
    
    private boolean verifyLogin(String input) {
        for(int i = 0; i < input.length(); i++) {
            char check = input.charAt(i);
            if(check == 32) {}
            else if(check >= 48 && check <= 57) {}
            else if(check >= 65 && check <= 90) {}
            else if(check >= 97 && check <= 122) {}
            else {
                this.showError("Pole 'login' zawiera niedozwolone znaki. Login może składać się z liter (bez polskich znaków) i cyfr. Może również zawierać spacje.");
                return false;
            }
        }
        return true;
    }
    
}
