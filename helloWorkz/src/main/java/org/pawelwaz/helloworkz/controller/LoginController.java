package org.pawelwaz.helloworkz.controller;

import java.util.List;
import javafx.application.Platform;
import javafx.event.EventHandler;
import org.pawelwaz.helloworkz.util.HelloUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
            try {
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
                        HelloUI.saveTmpImage(HelloSession.getUser().getReadyAvatar(), HelloSession.getUser().getLogin());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
                        Parent root = loader.load();
                        HelloSession.setMainController((MainViewController) loader.getController());
                        HelloSession.loadContacts();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setTitle("helloWorkz");
                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                Platform.exit();
                            }
                        });
                        stage.show(); 
                        Stage thisWindow = (Stage) ap.getScene().getWindow();
                        thisWindow.close();
                        return;
                    }
                }
                this.showError("Błędne dane logowania");
                em.close();
            }
            catch(Exception e) {
                this.showError("Błąd połączenia z bazą danych" + e.getMessage());
            }
        }
    }
    
    @FXML private void goRegister() {
        this.goToPopup("Register", "rejestracja");
    }
    
}
