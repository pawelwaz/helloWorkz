package org.pawelwaz.helloworkz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javax.persistence.EntityManager;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.pawelwaz.helloworkz.entity.HelloUser;
import static org.pawelwaz.helloworkz.util.FieldType.PASSWORD;
import org.pawelwaz.helloworkz.util.FormField;
import org.pawelwaz.helloworkz.util.HelloForm;
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
        FormField[] fields = {
            new FormField("stare hasło", PASSWORD, old.getText(), true),
            new FormField("nowe hasło", PASSWORD, newPass.getText(), true),
            new FormField("powtórz nowe hasło", PASSWORD, retype.getText(), true)
        };
        HelloForm form = new HelloForm(fields);
        
        if(!newPass.getText().equals(retype.getText())) {
            this.showError("Nowe hasło zostało przepisane niepoprawnie");
        }
        else if(form.isValid()) {
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
