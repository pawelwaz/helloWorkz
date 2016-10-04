package org.pawelwaz.helloworkz.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;
import org.pawelwaz.helloworkz.util.MessageButton;

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
    @FXML private VBox mainBox;
    private WritableImage messageButton;
    private WritableImage addToContactsButton;
    private boolean done = false;
    
    @FXML private void searchContactsAction() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = this.prepareQuery(em);
        List<HelloUser> users = q.getResultList();
        this.showHeader(users.size());
        this.searchResults.getChildren().clear();
        GridPane grid = new GridPane();
        int i = 0;
        for( HelloUser user : users ) {
            this.prepareRow(user, grid, i);
            i++;
        }
        this.searchResults.getChildren().add(grid);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        cc.setFillWidth(true);
        grid.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(), cc);
        AnchorPane.setLeftAnchor(grid, 0.0);
        AnchorPane.setRightAnchor(grid, 0.0);
        em.close();
    }
    
    private AnchorPane insertMessageButton(String evenOdd, Long userId) {
        MessageWindowController controller = null;
        Parent messageRoot = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MessageWindow.fxml"));
            messageRoot = loader.load();
            controller = (MessageWindowController) loader.getController();
            controller.setUserId(userId);
        }
        catch(Exception ex) {
            this.showError("Wystąpił błąd podczas działania aplikacji i zostanie ona zamknięta");
            System.exit(1);
        }
        Window window = this.ap.getScene().getWindow();
        MessageButton img = new MessageButton(this.messageButton, messageRoot, window);
        img.setCursor(Cursor.HAND);
        Tooltip.install(img, new Tooltip("wyślij wiadomość"));
        img.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((MessageButton)(event.getSource())).openMessageWindow();
            }
        });
        AnchorPane result = this.wrapNode(img, evenOdd, 15.0);
        AnchorPane.setLeftAnchor(img, 0.0);
        return result;
    }
    
    private AnchorPane insertEmpty(String evenOdd) {
        AnchorPane result = new AnchorPane();
        HelloUI.setAnchors(result, 0.0);
        result.getStyleClass().add(evenOdd);
        return result;
    }
    
    private void showHeader(int results) {
        if(this.done) mainBox.getChildren().remove(2);
        else this.done = true;
        Label header = new Label();
        header.getStyleClass().add("blueLabelSm");
        if(results == 0) header.setText("Nie znaleziono użytkowników spełniających zadane kryteria");
        else header.setText("Ilość znalezionych użytkowników: " + results);
        this.mainBox.getChildren().add(2, header);
    }
    
    private void prepareRow(HelloUser user, GridPane grid, int i) {
        String evenOdd = "stripeOdd";
        if(i % 2 == 0) evenOdd = "stripeEven";
        grid.add(this.prepareAvatar(user, evenOdd), 0, i);
        grid.add(this.prepareDescription(user, evenOdd), 1, i);
        grid.add(this.insertEmpty(evenOdd), 2, i);
        grid.add(this.insertMessageButton(evenOdd, user.getId()), 3, i);
    }
    
    private AnchorPane prepareDescription(HelloUser user, String evenOdd) {
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
        return this.wrapNode(desc, evenOdd, 5.0);
    }
    
    private AnchorPane prepareAvatar(HelloUser user, String evenOdd) {
        ImageView userAvatar = new ImageView();
        user.prepareAvatar();
        userAvatar.setImage(SwingFXUtils.toFXImage(user.getReadyAvatar(), null));
        userAvatar.setFitWidth(70.0);
        userAvatar.setFitHeight(70.0);
        return this.wrapNode(userAvatar, evenOdd, 5.0);
    }
    
    private AnchorPane wrapNode(Node node, String evenOdd, double anchors) {
        AnchorPane container = new AnchorPane();
        container.getStyleClass().add(evenOdd);
        container.getChildren().add(node);
        HelloUI.setAnchors(node, anchors);
        HelloUI.setAnchors(container, 0.0);
        return container;
    }
    
    private CriteriaQuery prepareOrder(CriteriaQuery q, CriteriaBuilder builder, Root<HelloUser> user) {
        String order = (String) this.sortBy.getValue();
        switch(order) {
            case "login":
                q.orderBy(builder.asc(user.get("login")));
                break;
            case "imię, nazwisko":
                q.orderBy(builder.asc(user.get("name")), builder.asc(user.get("surname")));
                break;
            case "nazwisko, imię":
                q.orderBy(builder.asc(user.get("surname")), builder.asc(user.get("name")));
                break;
            default:
                q.orderBy(builder.asc(user.get("organisation")));
        }
        return q;
    }
    
    private Query prepareQuery(EntityManager em) {
        boolean criteriaUsed = false;
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery q = builder.createQuery();
        Root<HelloUser> user = q.from(HelloUser.class);
        q.select(user);
        List<Predicate> preds = new ArrayList();
        this.addPredicate(login, builder, preds, user, "login");
        this.addPredicate(name, builder, preds, user, "name");
        this.addPredicate(surname, builder, preds, user, "surname");
        this.addPredicate(phone, builder, preds, user, "phone");
        this.addPredicate(organisation, builder, preds, user, "organisation");
        this.addPredicate(job, builder, preds, user, "job");
        if(!preds.isEmpty()) {
            q.where(builder.and(preds.toArray(new Predicate[preds.size()])));
        }
        q = this.prepareOrder(q, builder, user);
        return em.createQuery(q);
    }
    
    private void addPredicate(TextField field, CriteriaBuilder builder, List<Predicate> preds, Root<HelloUser> user, String name) {
        if(field.getText().length() > 0) {
            preds.add(builder.like(
                    builder.lower(user.<String>get(name)), 
                    "%" + field.getText().toLowerCase() + "%")
            );
        }
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
    
    private void populateSortList() {
        this.sortBy.setItems(FXCollections.observableArrayList("login", "imię, nazwisko", "nazwisko, imię", "organizacja"));
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.prepareButtons();
        this.populateSortList();
    }
    
}
