package org.pawelwaz.helloworkz.controller;

import org.pawelwaz.helloworkz.util.HelloUI;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author pawelwaz
 */
public class RegisterController extends HelloUI {
    
    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private PasswordField retype;
    
    @FXML private void goLogin() {
        this.goToPopup("Login");
    }
    
    @FXML private void registerAction() {
        if(login.getText().equals("") || password.getText().equals("") || retype.getText().equals("")) {
            this.showError("Wszystkie pola muszą być wypełnione");
        }
        else if(!password.getText().equals(retype.getText())) {
            this.showError("Podane hasła różnią się");
        }
        else {
            
        }
    }
    
}
