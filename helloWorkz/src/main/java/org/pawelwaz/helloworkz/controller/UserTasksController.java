/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.Task;
import org.pawelwaz.helloworkz.entity.TaskUser;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;
import org.pawelwaz.helloworkz.util.TaskDescriptor;

/**
 *
 * @author A20111
 */
public class UserTasksController extends HelloUI {
    
    @FXML private TableView<TaskDescriptor> taskTable;
    @FXML private Button detailsButton;
    @FXML private Button completeButton;
    
    @FXML private void showDetails() {
        TaskDescriptor td = this.taskTable.getSelectionModel().getSelectedItem();
        String info = "Zadanie nr " + td.getNumber() + "(Grupa " + td.getWorkgroupName() + ")\n\n";
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
    
    @FXML private void completeTask() {
        HelloSession.setTaskEdit(this.taskTable.getSelectionModel().getSelectedItem().getId());
        HelloSession.setGroupView(this.taskTable.getSelectionModel().getSelectedItem().getWorkgroup().getId());
        HelloSession.getMainController().openPopup("TaskComplete", "Zamykanie zadania");
        HelloSession.setTaskEdit(null);
        HelloSession.setGroupView(null);
        this.getTasks();
        this.disableButtons();
    }
    
    private void getTasks() {
        try {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select tu from TaskUser tu join tu.taskJoin t where tu.hellouser = " + HelloSession.getUser().getId());
        List<TaskUser> results = q.getResultList();
        ObservableList<TaskDescriptor> tableData = FXCollections.observableArrayList();
        for(TaskUser tu : results) {
            tableData.add(new TaskDescriptor(tu.getTaskJoin()));
        }
        this.taskTable.setItems(tableData);
        em.close();
        } catch(Exception ex) {
            HelloUI.showError(ex.getMessage());
        }
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
        TableColumn groupCol = new TableColumn("Grupa");
        groupCol.setCellValueFactory(new PropertyValueFactory<TaskDescriptor, String>("workgroupName"));
        this.taskTable.getColumns().clear();
        this.taskTable.getColumns().addAll(numberCol, groupCol, contentCol, deadlineCol, workersCol, creatorCol, statusCol, createdCol, closedCol);
        this.taskTable.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                detailsButton.setDisable(false);
                completeButton.setDisable(false);
            }
        });
    }
    
    public void disableButtons() {
        this.completeButton.setDisable(true);
        this.detailsButton.setDisable(true);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.disableButtons();
        this.prepareTable();
        this.getTasks();
    }
    
}
