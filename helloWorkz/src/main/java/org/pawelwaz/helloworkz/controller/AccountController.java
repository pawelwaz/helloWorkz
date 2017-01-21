package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javax.persistence.EntityManager;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.FormField;
import static org.pawelwaz.helloworkz.util.FieldType.*;
import org.pawelwaz.helloworkz.util.HelloForm;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class AccountController extends HelloUI {
    
    @FXML private TextField name;
    @FXML private TextField surname;
    @FXML private TextField phone;
    @FXML private TextField email;
    @FXML private TextField organisation;
    @FXML private TextField job;
    
    @FXML private void accountAction() {
        FormField[] fields = {
            new FormField("imię", SHORTFIELD, name.getText(), false),
            new FormField("nazwisko", SHORTFIELD, surname.getText(), false),
            new FormField("nr telefonu", MEDIUMFIELD, phone.getText(), false),
            new FormField("adres e-mail", MEDIUMFIELD, email.getText(), false),
            new FormField("organizacja", LONGFIELD, organisation.getText(), false),
            new FormField("stanowisko", LONGFIELD, job.getText(), false)
        };
        HelloForm form = new HelloForm(fields);
        if(form.isValid()) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            HelloUser user = em.find(HelloUser.class, HelloSession.getUser().getId());
            em.getTransaction().begin();
            if(name.getText().length() == 0) user.setName("");
            else user.setName(name.getText());
            if(surname.getText().length() == 0) user.setSurname("");
            else user.setSurname(surname.getText());
            if(phone.getText().length() == 0) user.setPhone("");
            else user.setPhone(phone.getText());
            if(email.getText().length() == 0) user.setEmail("");
            else user.setEmail(email.getText());
            if(organisation.getText().length() == 0) user.setOrganisation("");
            else user.setOrganisation(organisation.getText());
            if(job.getText().length() == 0) user.setJob("");
            else user.setJob(job.getText());
            em.getTransaction().commit();
            em.close();
            HelloSession.setUser(user);
            this.closeWindow();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(HelloSession.getUser().getName() != null) name.setText(HelloSession.getUser().getName());
        if(HelloSession.getUser().getSurname() != null) surname.setText(HelloSession.getUser().getSurname());
        if(HelloSession.getUser().getPhone() != null) phone.setText(HelloSession.getUser().getPhone());
        if(HelloSession.getUser().getEmail() != null) email.setText(HelloSession.getUser().getEmail());
        if(HelloSession.getUser().getOrganisation() != null) organisation.setText(HelloSession.getUser().getOrganisation());
        if(HelloSession.getUser().getJob() != null) job.setText(HelloSession.getUser().getJob());
    }
    
    @FXML private void cancel() {
        this.closeWindow();
    }
    
}
