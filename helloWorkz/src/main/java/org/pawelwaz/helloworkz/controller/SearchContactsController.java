package org.pawelwaz.helloworkz.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    @FXML private TextField phone;
    @FXML private ChoiceBox sortBy;
    @FXML private AnchorPane searchResults;
    private WritableImage messageButton;
    private WritableImage addToContactsButton;
    
    @FXML private void searchContactsAction() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = this.prepareQuery(em);
        List<HelloUser> users = q.getResultList();
        this.searchResults.getChildren().clear();
        VBox vb = new VBox();
        int i = 0;
        for( HelloUser user : users ) {
            AnchorPane rowPane = new AnchorPane();
            AnchorPane.setLeftAnchor(rowPane, 0.0);
            AnchorPane.setRightAnchor(rowPane, 0.0);
            if(i % 2 == 0) rowPane.getStyleClass().add("searchResultEven");
            else rowPane.getStyleClass().add("searchResultOdd");
            HBox row = this.prepareRow(user);
            rowPane.getChildren().add(row);
            ImageView sendMsgBtn = new ImageView();
            sendMsgBtn.setImage(this.messageButton);
            AnchorPane.setRightAnchor(sendMsgBtn, 15.0);
            AnchorPane.setTopAnchor(sendMsgBtn, 15.0);
            rowPane.getChildren().add(sendMsgBtn);
            
            ImageView addBtn = new ImageView();
            addBtn.setImage(this.addToContactsButton);
            AnchorPane.setRightAnchor(addBtn, 80.0);
            AnchorPane.setTopAnchor(addBtn, 15.0);
            rowPane.getChildren().add(addBtn);
            
            vb.getChildren().add(rowPane);
            i++;
        }
        this.searchResults.getChildren().add(vb);
        AnchorPane.setLeftAnchor(vb, 0.0);
        AnchorPane.setRightAnchor(vb, 0.0);
        em.close();
    }
    
    private Query prepareQuery(EntityManager em) {
        boolean criteriaUsed = false;
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery q = builder.createQuery();
        Root<HelloUser> user = q.from(HelloUser.class);
        q.select(user);
        if(this.login.getText().length() > 0) {
            Predicate pred = builder.like(user.<String>get("login"), this.login.getText());
            q.where(pred);
        }
        return em.createQuery(q);
    }
    
    private HBox prepareRow(HelloUser user) {
        HBox row = new HBox();
        row.getChildren().add(this.prepareAvatar(user));
        row.getChildren().add(this.prepareDescription(user));
        AnchorPane.setLeftAnchor(row, 5.0);
        AnchorPane.setTopAnchor(row, 5.0);
        AnchorPane.setBottomAnchor(row, 5.0);
        return row;
    }
    
    private void prepareButtons() {
        try {
            File file = new File("classes/img/messageButton.png");
            BufferedImage bufferedImage = ImageIO.read(file);
            this.messageButton = SwingFXUtils.toFXImage(bufferedImage, null);
            file = new File("classes/img/add.png");
            bufferedImage = ImageIO.read(file);
            this.addToContactsButton = SwingFXUtils.toFXImage(bufferedImage, null);
        }
        catch(Exception ex) {
            this.showError("Brak części plików aplikacji. Zostanie ona zamknięta");
            System.exit(1);
        }
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
        this.prepareButtons();
    }
    
}
