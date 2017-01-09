package org.pawelwaz.helloworkz.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javax.imageio.ImageIO;
import org.pawelwaz.helloworkz.entity.HelloUser;

/**
 *
 * @author pawelwaz
 */
public class UserGroupsController extends HelloUI {
    
    @FXML private VBox vb;
    private WritableImage viewIcon;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            File file = new File("classes/img/search.png");
            BufferedImage bufferedImage = ImageIO.read(file);
            this.viewIcon = SwingFXUtils.toFXImage(bufferedImage, null);
        }
        catch(Exception e) {
            HelloUI.showError("Wystąpił błąd podczas ładowania plików programu");
        }
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query query = em.createQuery("select m from Membership m join m.memberGroup g where m.hellouser = " + HelloSession.getUser().getId());
        List<Membership> userMemberships = query.getResultList();
        if(userMemberships.isEmpty()) {
            Label noResults = new Label("Obecnie nie posiadasz członkostwa w żadnej grupie");
            noResults.getStyleClass().add("blueLabelSm");
            vb.getChildren().add(noResults);
        }
        else {
            GridPane gp = new GridPane();
            int n = 0;
            for(Membership mbm : userMemberships) {
                this.prepareRow(mbm, gp, n);
                n++;
            }
            vb.getChildren().add(gp);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            gp.getColumnConstraints().addAll(new ColumnConstraints(), cc);
            AnchorPane.setLeftAnchor(gp, 0.0);
            AnchorPane.setRightAnchor(gp, 0.0);
        }
    }
    
    private void prepareRow(Membership mbm, GridPane grid, int i) {
        String styleClass = "stripeOdd";
        if(i % 2 == 0) styleClass = "stripeEven";
        grid.add(HelloUI.wrapNode(this.prepareGroupDescription(mbm), styleClass, 0.0), 0, i);
        grid.add(HelloUI.insertEmptyCell(styleClass), 1, i);
        grid.add(this.insertViewButton(styleClass), 2, i);
    }
    
    private AnchorPane insertViewButton(String styleClass) {
        ImageView result = new ImageView();
        result.setImage(viewIcon);
        return this.wrapNode(result, styleClass, 15.0, 15.0, 0.0, 5.0);
    }
    
    private VBox prepareGroupDescription(Membership mbm) {
        VBox result = new VBox();
        Label groupName = new Label(mbm.getMemberGroup().getGroup_name());
        groupName.getStyleClass().add("smallHeader");
        result.getChildren().add(groupName);
        Label memberType = new Label("Stanowisko: " + mbm.getTitle());
        memberType.getStyleClass().add("description");
        result.getChildren().add(memberType);
        Label memberDesc = new Label("Opis stanowiska: " + mbm.getDescription());
        memberDesc.getStyleClass().add("description");
        if(mbm.getDescription().length() == 0) memberDesc.setText(memberDesc.getText() + "brak");
        result.getChildren().add(memberDesc);
        Label groupDesc = new Label("Opis grupy: " + mbm.getMemberGroup().getDescription());
        groupDesc.getStyleClass().add("description");
        if(mbm.getMemberGroup().getDescription().length() == 0) groupDesc.setText(groupDesc.getText() + "brak");
        result.getChildren().add(groupDesc);
        return result;
    }
    
}
