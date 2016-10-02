package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.persistence.*;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author pawelwaz
 */
public class SearchContactsController extends HelloUI {
    
    @FXML private TextField login;
    @FXML private TextField name;
    @FXML private TextField surname;
    @FXML private TextField organisation;
    @FXML private TextField job;
    @FXML private AnchorPane searchResults;
    
    @FXML private void searchContactsAction() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select u from HelloUser u");
        List<HelloUser> users = q.getResultList();
        this.searchResults.getChildren().clear();
        VBox vb = new VBox();
        int i = 0;
        for( HelloUser user : users ) {
            AnchorPane rowPane = new AnchorPane();
            AnchorPane.setLeftAnchor(rowPane, 0.0);
            AnchorPane.setRightAnchor(rowPane, 0.0);
            rowPane.getStyleClass().add("rowPane");
            if(i % 2 == 0) rowPane.getStyleClass().add("searchResultEven");
            else rowPane.getStyleClass().add("searchResultOdd");
            HBox row = this.prepareRow(user);
            rowPane.getChildren().add(row);
            vb.getChildren().add(rowPane);
            i++;
        }
        this.searchResults.getChildren().add(vb);
        AnchorPane.setLeftAnchor(vb, 0.0);
        AnchorPane.setRightAnchor(vb, 0.0);
        em.close();
    }
    
    private HBox prepareRow(HelloUser user) {
        HBox row = new HBox();
        row.getChildren().add(this.prepareAvatar(user));
        row.getChildren().add(this.prepareDescription(user));
        return row;
    }
    
    private VBox prepareDescription(HelloUser user) {
        VBox desc = new VBox();
        Label userLabel = new Label(user.getLogin());
        userLabel.getStyleClass().add("smallHeader");
        desc.getChildren().add(userLabel);
        if(user.getName() != null || user.getSurname() != null) {
            Label nameSurname = new Label("");
            if(user.getName() != null) nameSurname.setText(user.getName() + " ");
            if(user.getSurname() != null) nameSurname.setText(nameSurname.getText() + user.getSurname());
            nameSurname.getStyleClass().add("description");
            desc.getChildren().add(nameSurname);
        }
        if(user.getPhone() != null || user.getEmail() != null) {
            Label phoneEmail = new Label("");
            if(user.getPhone() != null) phoneEmail.setText("tel. " + user.getPhone() + " ");
            if(user.getEmail() != null) phoneEmail.setText(phoneEmail.getText() + "e-mail: " + user.getEmail());
            phoneEmail.getStyleClass().add("description");
            desc.getChildren().add(phoneEmail);
        }
        if(user.getOrganisation() != null || user.getJob() != null) {
            Label orgJob = new Label("");
            if(user.getOrganisation() != null) orgJob.setText("organizacja: " + user.getOrganisation() + " ");
            if(user.getJob() != null) orgJob.setText(orgJob.getText() + "stanowisko: " + user.getJob());
            orgJob.getStyleClass().add("description");
            desc.getChildren().add(orgJob);
        }
        return desc;
    }
    
    private ImageView prepareAvatar(HelloUser user) {
        ImageView userAvatar = new ImageView();
        user.prepareAvatar();
        userAvatar.setImage(SwingFXUtils.toFXImage(user.getReadyAvatar(), null));
        userAvatar.setFitWidth(70.0);
        userAvatar.setFitHeight(70.0);
        return userAvatar;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //AnchorPane.setRightAnchor(this.ap, 0.0);
    }
    
}
