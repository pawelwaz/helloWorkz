package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;

/**
 *
 * @author pawelwaz
 */
public class ContactsController extends HelloUI {
    
    @FXML private VBox content;
    
    @FXML private void goSearchContacts() {
        HelloSession.getMainController().goSearchContacts();
    }
    
    private void showContacts() {
        this.addHeader("Prywatne kontakty");
        if(HelloSession.getUserContacts().isEmpty()) {
            Label noResults = new Label("(brak kontaktów do wyświetlenia)");
            noResults.setPadding(new Insets(0, 0, 0, 5));
            noResults.getStyleClass().add("blueLabelSm");
            this.content.getChildren().add(noResults);
        }
        else {
            GridPane grid = new GridPane();
            int i = 0;
            for(HelloUser contact : HelloSession.getUserContactPersons()) {
                this.prepareRow(contact, grid, i);
                i++;
            }
            this.content.getChildren().add(grid);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            grid.getColumnConstraints().addAll(new ColumnConstraints(), cc);
            AnchorPane.setLeftAnchor(grid, 0.0);
            AnchorPane.setRightAnchor(grid, 0.0);
        }
    }
    
    private void addHeader(String text) {
        Label header = new Label(text);
        header.setPadding(new Insets(5, 5, 5, 5));
        header.getStyleClass().add("blueLabel");
        this.content.getChildren().add(header);
    }
    
    private void prepareRow(HelloUser user, GridPane grid, int i) {
        String styleClass = "stripeOdd";
        if(i % 2 == 0) styleClass = "stripeEven";
        grid.add(HelloUI.wrapNode(HelloUI.prepareUserDescription(user, null), styleClass, 0.0), 0, i);
        grid.add(HelloUI.insertEmptyCell(styleClass), 1, i);
        grid.add(this.insertContactButton(styleClass, user, false, false), 2, i);
        grid.add(this.insertMessageButton(styleClass, user), 3, i);
        grid.add(HelloUI.insertInvitationButton(styleClass, user.getId()), 4, i);
    }
    
    @Override
    public void refresh() {
        this.content.getChildren().clear();
        this.showContacts();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.showContacts();
    }
    
}
