package org.pawelwaz.helloworkz.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import org.pawelwaz.helloworkz.util.HelloUI;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Discussion;
import org.pawelwaz.helloworkz.entity.Group;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Invitation;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.entity.MembershipRequest;
import org.pawelwaz.helloworkz.entity.Notification;
import org.pawelwaz.helloworkz.entity.Task;
import org.pawelwaz.helloworkz.entity.TaskUser;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.JpaUtil;
import org.pawelwaz.helloworkz.util.TaskDescriptor;

/**
 *
 * @author pawelwaz
 */
public class GroupViewController extends HelloUI {
    
    @FXML private Label header;
    @FXML private Label groupDesc;
    @FXML private Label memberTitle;
    @FXML private Label memberDesc;
    @FXML private VBox vb;
    @FXML private AnchorPane membersAP;
    @FXML private AnchorPane requestsAP;
    @FXML private AnchorPane discussionsAP;
    @FXML private Label editHeader;
    @FXML private GridPane membersGrid;
    @FXML private GridPane requestGrid;
    @FXML private Tab requestTab;
    @FXML private Label discussionAdd;
    @FXML private VBox discussionsVB;
    @FXML private TableView<TaskDescriptor> taskTable;
    @FXML private Button addTaskButton;
    @FXML private Button taskDetailsButton;
    @FXML private Button taskRemoveButton;
    @FXML private Button taskEditButton;
    @FXML private Button taskCompleteButton;
    private WritableImage viewIcon;
    private Group group;
    private Membership membership;
    
    @FXML private void completeTask() {
        HelloSession.setTaskEdit(this.taskTable.getSelectionModel().getSelectedItem().getId());
        HelloSession.getMainController().openPopup("TaskComplete", "Zamykanie zadania");
        HelloSession.setTaskEdit(null);
        this.getTasks();
        this.disableButtons();
    }
    
    @FXML private void editTask() {
        TaskDescriptor td = this.taskTable.getSelectionModel().getSelectedItem();
        HelloSession.setTaskEdit(td.getId());
        HelloSession.getMainController().openPopup("Task", "Edycja zadania");
        this.getTasks();
        HelloSession.setTaskEdit(null);
        this.disableButtons();
    }
    
    @FXML private void removeTask() {
        if(HelloUI.showConfirmation("Czy na pewno usunąć to zadanie?")) {
            TaskDescriptor td = this.taskTable.getSelectionModel().getSelectedItem();
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select tu from TaskUser tu where tu.task = " + td.getId());
            List<TaskUser> tuResult = q.getResultList();
            em.getTransaction().begin();
            for(TaskUser tu : tuResult) {
                em.remove(tu);
            }
            em.getTransaction().commit();
            q = em.createQuery("select t from Task t where t.id = " + td.getId());
            List<Task> result = q.getResultList();
            Task t = result.get(0);
            q = em.createQuery("select g from Group g where g.id = " + HelloSession.getGroupView());
            List<Group> gResult = q.getResultList();
            Group g = gResult.get(0);
            String nContent = "Użytkownik " + HelloSession.getUser().getLogin() + " usunął w grupie " + g.getGroup_name() + " zadanie nr " + t.getNumber();
            q = em.createQuery("select m from Membership m where m.active = 1 and m.workgroup = " + g.getId());
            List<Membership> memberships = q.getResultList();
            em.getTransaction().begin();
            for(Membership m : memberships) {
                if(!m.getHellouser().equals(HelloSession.getUser().getId())) {
                    Notification n = new Notification(m.getHellouser(), nContent);
                    em.persist(n);
                }
            }
            em.remove(t);
            em.getTransaction().commit();
            em.close();
            this.getTasks();
            this.disableButtons();
        }
    }
    
    @FXML private void addTask() {
        HelloSession.getMainController().openPopup("Task", "Dodawanie nowego zadania");
        this.getTasks();
        this.disableButtons();
    }
    
    @FXML private void showTaskDetails() {
        TaskDescriptor td = this.taskTable.getSelectionModel().getSelectedItem();
        String info = "Zadanie nr " + td.getNumber() + "\n\n";
        info += "Opis zadania:\n" + td.getContent() + "\n\n";
        info += "Termin realizacji: " + td.getDeadline() + "\n\n";
        info += "Przydzielone osoby: " + td.getWorkers() + "\n\n";
        info += "Przydzielone przez: " + td.getCreator() + " w dniu " + td.getCreated() + "\n\n";
        info += "Status: " + td.getStatus();
        if(td.getStatusCode() == 1) info += " (zamknięto " + td.getClosed() + ")";
        info += "\n\n";
        info += "Komentarz: ";
        if(td.getAnnotation() == null) info += "brak";
        else info += td.getAnnotation();
        HelloUI.showInfo(info);
    }
    
    private void getTasks() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select t from Task t where t.workgroup = " + this.group.getId() + " order by t.status, t.deadline, t.deadlinehour");
        List<Task> results = q.getResultList();
        ObservableList<TaskDescriptor> tableData = FXCollections.observableArrayList();
        for(Task t : results) {
            tableData.add(new TaskDescriptor(t));
        }
        this.taskTable.setItems(tableData);
        em.close();
    }
    
    private void disableButtons() {
        this.taskDetailsButton.setDisable(true);
        this.taskRemoveButton.setDisable(true);
        this.taskCompleteButton.setDisable(true);
        this.taskEditButton.setDisable(true);
    }
    
    private void prepareTable() {
        TableColumn contentCol = new TableColumn("Opis zadania");
        contentCol.setCellValueFactory(new PropertyValueFactory<TaskDescriptor, String>("content"));
        TableColumn deadlineCol = new TableColumn("Termin");
        deadlineCol.setCellValueFactory(new PropertyValueFactory<TaskDescriptor, String>("deadline"));
        TableColumn workersCol = new TableColumn("Przydzielone osoby");
        workersCol.setCellValueFactory(new PropertyValueFactory<TaskDescriptor, String>("workers"));
        TableColumn creatorCol = new TableColumn("Przydzielający");
        creatorCol.setCellValueFactory(new PropertyValueFactory<TaskDescriptor, String>("creator"));
        TableColumn statusCol = new TableColumn("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<TaskDescriptor, String>("status"));
        TableColumn numberCol = new TableColumn("Nr");
        numberCol.setCellValueFactory(new PropertyValueFactory<TaskDescriptor, String>("number"));
        TableColumn createdCol = new TableColumn("Dodano");
        createdCol.setCellValueFactory(new PropertyValueFactory<TaskDescriptor, String>("created"));
        TableColumn closedCol = new TableColumn("Zamknięto");
        closedCol.setCellValueFactory(new PropertyValueFactory<TaskDescriptor, String>("closed"));
        this.taskTable.getColumns().clear();
        this.taskTable.getColumns().addAll(numberCol, contentCol, deadlineCol, workersCol, creatorCol, statusCol, createdCol, closedCol);
        this.taskTable.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                taskDetailsButton.setDisable(false);
                if(membership.getTasks() == 1) {
                    taskRemoveButton.setDisable(false);
                }
                if(membership.getTasks() == 1 && taskTable.getSelectionModel().getSelectedItem().getStatusCode() == 0) taskEditButton.setDisable(false);
                else taskEditButton.setDisable(true);
                if(taskTable.getSelectionModel().getSelectedItem().getStatusCode() == 0) {
                    if(membership.getTasks() == 1 || taskTable.getSelectionModel().getSelectedItem().isEditable()) taskCompleteButton.setDisable(false);
                    else taskCompleteButton.setDisable(true);
                }
                else taskCompleteButton.setDisable(true);
            }
        });
    }
    
    private void getDiscussions() {
        this.discussionsVB.getChildren().clear();
        this.discussionAdd = new Label();
        this.discussionAdd.getStyleClass().add("blueLabelSm");
        this.discussionAdd.setUnderline(true);
        if(this.membership.getDiscussions() == 1) {
            this.discussionAdd.setCursor(Cursor.HAND);
            this.discussionAdd.setText("Utwórz nową dyskusję w grupie");
            this.discussionAdd.setOnMouseClicked(new EventHandler<MouseEvent> () {
                @Override
                public void handle(MouseEvent event) {
                    HelloSession.getMainController().openPopup("DiscussionAdd", "Tworzenie dyskusji");
                    getDiscussions();
                }
            });
        }
        else {
            this.discussionAdd.setVisible(false);
        }
        this.discussionsVB.getChildren().add(this.discussionAdd);
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select d from Discussion d where d.workgroup = " + this.group.getId() + " order by d.lastPost desc");
        List<Discussion> results = q.getResultList();
        if(results.isEmpty()) {
            Label noResults = new Label("brak dyskusji do wyświetlenia");
            noResults.getStyleClass().add("blueLabelSm");
            this.discussionsVB.getChildren().add(noResults);
        }
        else {
            Label headerTitle = new Label("tytuł");
            headerTitle.getStyleClass().add("smallHeader");
            Label headerPosts = new Label("ilość postów");
            headerPosts.getStyleClass().add("smallHeader");
            Label headerLastPost = new Label("ostatnia aktywność");
            headerLastPost.getStyleClass().add("smallHeader");
            String styleClass = "stripeEven";
            int i = 0;
            GridPane discussionsGrid = new GridPane();
            discussionsGrid.add(HelloUI.wrapNode(headerTitle, styleClass, 5.0), 0, i);
            discussionsGrid.add(HelloUI.wrapNode(headerPosts, styleClass, 5.0), 1, i);
            discussionsGrid.add(HelloUI.wrapNode(headerLastPost, styleClass, 5.0), 2, i);
            discussionsGrid.add(HelloUI.insertEmptyCell(styleClass), 3, i);
            discussionsGrid.add(HelloUI.insertEmptyCell(styleClass), 4, i);
            for(Discussion dis : results) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                i++;
                styleClass = "stripeOdd";
                if(i % 2 == 0) styleClass = "stripeEven";
                discussionsGrid.add(HelloUI.wrapNode(new Label(dis.getTitle()), styleClass, 10.0), 0, i);
                discussionsGrid.add(HelloUI.wrapNode(new Label(dis.getPosts().toString()), styleClass, 10.0), 1, i);
                discussionsGrid.add(HelloUI.wrapNode(new Label(df.format(dis.getLastPost())), styleClass, 10.0), 2, i);
                discussionsGrid.add(HelloUI.insertEmptyCell(styleClass), 3, i);
                discussionsGrid.add(this.insertDiscussionButton(styleClass, dis), 4, i);
            }
            this.discussionsVB.getChildren().add(discussionsGrid);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            discussionsGrid.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints(), cc);
            AnchorPane.setLeftAnchor(discussionsGrid, 0.0);
            AnchorPane.setRightAnchor(discussionsGrid, 0.0);
        }
        em.close();
    }
    
    @FXML private void editGroup() {
        HelloSession.setGroupId(this.group.getId());
        HelloSession.getMainController().goGroupEdit();
        HelloSession.setGroupId(null);
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select g from Group g where g.id = " + this.group.getId());
        List<Group> result = q.getResultList();
        this.group = result.get(0);
        this.groupDesc.setText(this.group.getDescription());
        this.header.setText("Grupa " + this.group.getGroup_name());
        em.close();
    }
    
    public void getMembers() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query query = em.createQuery("select m from Membership m join m.memberUser u where m.active = 1 and m.workgroup = " + this.group.getId());
        List<Membership> memberships = query.getResultList();
        this.membersGrid = new GridPane();
        int i = 0;
        for(Membership mem : memberships) {
            String styleClass = "stripeOdd";
            if(i % 2 == 0) styleClass = "stripeEven";
            HBox userDesc = new HBox();
            userDesc.getChildren().add(HelloUI.prepareUserAvatar(mem.getMemberUser()));
            VBox desc = new VBox();
            Label userName = new Label(mem.getMemberUser().getLogin());
            if(mem.getMemberUser().getName().length() > 0 && mem.getMemberUser().getSurname().length() > 0) {
                userName.setText(userName.getText() + " (" + mem.getMemberUser().getName() + " " + mem.getMemberUser().getSurname() + ")");
            }
            userName.getStyleClass().add("smallHeader");
            desc.getChildren().add(userName);
            Label memberTitle = new Label("Stanowisko: " + mem.getTitle());
            memberTitle.getStyleClass().add("description");
            desc.getChildren().add(memberTitle);
            Label memberDesc = new Label("Opis stanowiska: ");
            if(mem.getDescription().length() > 0) memberDesc.setText(memberDesc.getText() + mem.getDescription());
            else memberDesc.setText(memberDesc.getText() + "brak");
            memberDesc.getStyleClass().add("description");
            desc.getChildren().add(memberDesc);
            userDesc.getChildren().add(desc);
            this.membersGrid.add(HelloUI.wrapNode(userDesc, styleClass, 0.0), 0, i);
            this.membersGrid.add(HelloUI.insertEmptyCell(styleClass), 1, i);
            if(!mem.getHellouser().equals(HelloSession.getUser().getId())) {
                if(this.membership.getUsers() == 1) this.membersGrid.add(this.insertSettingsButton(styleClass, mem.getId()), 2, i);
                else this.membersGrid.add(HelloUI.insertEmptyCell(styleClass), 2, i);
                this.membersGrid.add(this.insertMessageButton(styleClass, mem.getMemberUser()), 3, i);
            }
            else {
                this.membersGrid.add(HelloUI.insertEmptyCell(styleClass), 2, i);
                this.membersGrid.add(HelloUI.insertEmptyCell(styleClass), 3, i);
            }
            i++;
        }
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        cc.setFillWidth(true);
        this.membersGrid.getColumnConstraints().addAll(new ColumnConstraints(), cc);
        AnchorPane.setLeftAnchor(this.membersGrid, 0.0);
        AnchorPane.setRightAnchor(this.membersGrid, 0.0);
        this.membersAP.getChildren().clear();
        this.membersAP.getChildren().add(this.membersGrid);
    }
    
    public void getRequests() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select r from MembershipRequest r join r.requestUser u where r.workgroup = " + this.group.getId());
        List<MembershipRequest> requests = q.getResultList();
        this.requestTab.setText("Prośby o dołączenie (" + requests.size() + ")");
        if(requests.isEmpty()) {
            Label noResults = new Label("brak zgłoszeń o dołączenie do grupy");
            noResults.getStyleClass().add("blueLabel");
            this.requestsAP.getChildren().clear();
            this.requestsAP.getChildren().add(noResults);
        }
        else {
            this.requestGrid = new GridPane();
            int i = 0;
            for(MembershipRequest r : requests) {
                String styleClass = "stripeOdd";
                if(i % 2 == 0) styleClass = "stripeEven";
                this.requestGrid.add(HelloUI.wrapNode(HelloUI.prepareUserDescription(r.getRequestUser(), styleClass), styleClass, 0.0), 0, i);
                this.requestGrid.add(HelloUI.insertEmptyCell(styleClass), 1, i);
                if(this.membership.getUsers() == 1) {
                    this.requestGrid.add(this.insertAcceptButton(styleClass, r.getId()), 2, i);
                    this.requestGrid.add(this.insertDeclineButton(styleClass, r.getId()), 3, i);
                }
                else {
                    this.requestGrid.add(HelloUI.insertEmptyCell(styleClass), 2, i);
                    this.requestGrid.add(HelloUI.insertEmptyCell(styleClass), 3, i);
                }
                this.requestGrid.add(this.insertMessageButton(styleClass, r.getRequestUser()), 4, i);
                i++;
            }
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            this.requestGrid.getColumnConstraints().addAll(new ColumnConstraints(), cc);
            AnchorPane.setLeftAnchor(this.requestGrid, 0.0);
            AnchorPane.setRightAnchor(this.requestGrid, 0.0);
            this.requestsAP.getChildren().clear();
            this.requestsAP.getChildren().add(this.requestGrid);
        }
        em.close();
    }
    
    public AnchorPane insertDeclineButton(String styleClass, final Long id) {
        ImageView btn = new ImageView();
        btn.setImage(HelloUI.declineButton);
        btn.setCursor(Cursor.HAND);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                declineRequest(id);
            }
        });
        Tooltip.install(btn, new Tooltip("odrzuć prośbę"));
        return HelloUI.wrapNode(btn, styleClass,  15.0, 15.0, 0.0, 5.0);
    }
    
    public void declineRequest(Long id) {
        if(HelloUI.showConfirmation("Czy na pewno odrzucić tę prośbę?")) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select r from MembershipRequest r where r.id = " + id);
            List<MembershipRequest> result = q.getResultList();
            MembershipRequest r = result.get(0);
            q = em.createQuery("select g from Group g where g.id = " + r.getWorkgroup());
            List<Group> resultGroup = q.getResultList();
            Group g = resultGroup.get(0);
            Notification n = new Notification(r.getHellouser(), "Twoja prośba o dołączenie do grupy " + g.getGroup_name() + " została odrzucona przez użytkownika " + HelloSession.getUser().getLogin());
            em.getTransaction().begin();
            em.persist(n);
            em.remove(r);
            em.getTransaction().commit();
            em.close();
            this.getRequests();
        }
    }
    
    public void acceptRequest(Long id) {
        if(HelloUI.showConfirmation("Czy na pewno zaakceptować tę prośbę?")) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select r from MembershipRequest r where r.id = " + id);
            List<MembershipRequest> result = q.getResultList();
            MembershipRequest r = result.get(0);
            Membership mem = new Membership(r.getWorkgroup(), r.getHellouser(), "Pracownik", "", 0, 0, 0, 0);
            q = em.createQuery("select g from Group g where g.id = " + r.getWorkgroup());
            List<Group> resultGroup = q.getResultList();
            Group g = resultGroup.get(0);
            q = em.createQuery("select i from Invitation i where i.hellouser = " + r.getHellouser() + " and i.workgroup = " + g.getId());
            List<Invitation> invitationResult = q.getResultList();
            Notification n = new Notification(r.getHellouser(), "Twoja prośba o dołączenie do grupy " + g.getGroup_name() + " została zaakceptowana przez użytkownika " + HelloSession.getUser().getLogin());
            em.getTransaction().begin();
            em.persist(n);
            em.persist(mem);
            em.remove(r);
            if(!invitationResult.isEmpty()) {
                Invitation in = invitationResult.get(0);
                em.remove(in);
            }
            em.getTransaction().commit();
            em.close();
            this.getRequests();
            this.getMembers();
        }
    }
    
    public AnchorPane insertSettingsButton(String styleClass, final Long id) {
        ImageView btn = new ImageView();
        btn.setImage(HelloUI.settingsButton);
        btn.setCursor(Cursor.HAND);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                HelloSession.setMembershipId(id);
                HelloSession.getMainController().openPopup("Membership", "Edycja członkostwa w grupie");
                HelloSession.setMembershipId(id);
                getMembers();
                getTasks();
            }
        });
        Tooltip.install(btn, new Tooltip("edytuj członkostwo"));
        return HelloUI.wrapNode(btn, styleClass,  15.0, 15.0, 0.0, 5.0);
    }
    
    public AnchorPane insertAcceptButton(String styleClass, final Long id) {
        ImageView btn = new ImageView();
        btn.setImage(HelloUI.acceptButton);
        btn.setCursor(Cursor.HAND);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                acceptRequest(id);
            }
        });
        Tooltip.install(btn, new Tooltip("akceptuj prośbę"));
        return HelloUI.wrapNode(btn, styleClass,  15.0, 15.0, 0.0, 5.0);
    }
    
    private void prepareUserHeaders() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query query = em.createQuery("select g from Group g where g.id = " + HelloSession.getGroupView());
        List<Group> groupResult = query.setMaxResults(1).getResultList();
        this.group = groupResult.get(0);
        query = em.createQuery("select m from Membership m where m.hellouser = " + HelloSession.getUser().getId() + " and m.workgroup = " + HelloSession.getGroupView());
        List<Membership> memberResult = query.setMaxResults(1).getResultList();
        this.membership = memberResult.get(0);
        this.header.setText(this.header.getText() + group.getGroup_name());
        this.groupDesc.setText(this.groupDesc.getText() + group.getDescription());
        if(group.getDescription().length() == 0) this.groupDesc.setText(this.groupDesc.getText() + "brak");
        this.memberTitle.setText(this.memberTitle.getText() + this.membership.getTitle());
        this.memberDesc.setText(this.memberDesc.getText() + this.membership.getDescription());
        if(this.membership.getDescription().length() == 0) this.memberDesc.setText(this.memberDesc.getText() + "brak");
    }
    
    private void prepareButtons() {
        if(this.membership.getTasks() == 0) this.addTaskButton.setDisable(true);
    }
    
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
        this.prepareUserHeaders();
        this.getMembers();
        this.getRequests();
        this.getDiscussions();
        this.prepareTable();
        this.getTasks();
        this.prepareButtons();
    }
    
}
