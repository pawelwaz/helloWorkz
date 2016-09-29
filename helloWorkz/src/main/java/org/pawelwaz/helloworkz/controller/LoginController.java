package org.pawelwaz.helloworkz.controller;

import org.pawelwaz.helloworkz.util.HelloUI;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author pawelwaz
 */
public class LoginController extends HelloUI {
    
    @FXML private TextField login;
    @FXML private PasswordField password;
    
    @FXML private void loginAction() {
    }
    
    @FXML private void goRegister() {
        this.goToPopup("Register");
    }
    
}
