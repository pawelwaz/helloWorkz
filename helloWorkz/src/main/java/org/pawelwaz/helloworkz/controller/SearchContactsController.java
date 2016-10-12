package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.HelloSession;
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
    @FXML private VBox mainBox;
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
        grid.getColumnConstraints().addAll(new ColumnConstraints(), cc);
        AnchorPane.setLeftAnchor(grid, 0.0);
        AnchorPane.setRightAnchor(grid, 0.0);
        em.close();
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
        String styleClass = "stripeOdd";
        if(i % 2 == 0) styleClass = "stripeEven";
        grid.add(HelloUI.wrapNode(HelloUI.prepareUserDescription(user, null), styleClass, 0.0), 0, i);
        grid.add(HelloUI.insertEmptyCell(styleClass), 1, i);
        if(user.getId().equals(HelloSession.getUser().getId())) {
            grid.add(HelloUI.insertEmptyCell(styleClass), 2, i);
            grid.add(HelloUI.insertEmptyCell(styleClass), 3, i);
        }
        else {
            if(HelloSession.getUserContacts().contains(user.getId())) grid.add(this.insertContactButton(styleClass, user, false, true), 2, i);
            else grid.add(this.insertContactButton(styleClass, user, true, true), 2, i);
            grid.add(this.insertMessageButton(styleClass, user), 3, i);
        }
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
    
    private void populateSortList() {
        this.sortBy.setItems(FXCollections.observableArrayList("login", "imię, nazwisko", "nazwisko, imię", "organizacja"));
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.populateSortList();
    }
    
}
