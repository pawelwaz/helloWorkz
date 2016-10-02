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
            HBox row = new HBox();
            if(i % 2 == 0) row.getStyleClass().add("searchResultEven");
            else row.getStyleClass().add("searchResultOdd");
            AnchorPane.setLeftAnchor(row, 0.0);
            AnchorPane.setRightAnchor(row, 0.0);
            ImageView userAvatar = new ImageView();
            user.prepareAvatar();
            userAvatar.setImage(SwingFXUtils.toFXImage(user.getReadyAvatar(), null));
            userAvatar.setFitWidth(60.0);
            userAvatar.setFitHeight(60.0);
            Label userLabel = new Label(user.getLogin());
            userLabel.getStyleClass().add("smallHeader");
            row.getChildren().addAll(userAvatar, userLabel);
            vb.getChildren().add(row);
            i++;
        }
        this.searchResults.getChildren().add(vb);
        AnchorPane.setLeftAnchor(vb, 0.0);
        AnchorPane.setRightAnchor(vb, 0.0);
        em.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //AnchorPane.setRightAnchor(this.ap, 0.0);
    }
    
}
