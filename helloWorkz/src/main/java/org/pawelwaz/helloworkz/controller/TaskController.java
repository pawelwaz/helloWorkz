/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Membership;
import org.pawelwaz.helloworkz.entity.Task;
import org.pawelwaz.helloworkz.entity.TaskUser;
import org.pawelwaz.helloworkz.util.HelloSession;
import org.pawelwaz.helloworkz.util.HelloUI;
import org.pawelwaz.helloworkz.util.JpaUtil;

/**
 *
 * @author A20111
 */
public class TaskController extends HelloUI {
    
    @FXML private TextArea content;
    @FXML private DatePicker picker;
    @FXML private TextField hour;
    @FXML private ListView workers1;
    @FXML private ListView workers2;
    private Task editedTask = null;
    
    @FXML private void removeWorker() {
        HelloUser worker = (HelloUser) this.workers2.getSelectionModel().getSelectedItem();
        if(worker != null) {
            this.workers2.getItems().remove(worker);
            this.workers1.getItems().add(worker);
        }
    }
    
    @FXML private void addWorker() {
        HelloUser worker = (HelloUser) this.workers1.getSelectionModel().getSelectedItem();
        if(worker != null) {
            this.workers1.getItems().remove(worker);
            this.workers2.getItems().add(worker);
        }
    }
    
    private void removeTaskUsers(Long id) {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select tu from TaskUser tu where tu.task = " + id);
        List<TaskUser> results = q.getResultList();
        em.getTransaction().begin();
        for(TaskUser tu : results) {
            em.remove(tu);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    private void addTaskUsers(Long id, List<HelloUser> users) {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        em.getTransaction().begin();
        for(HelloUser u : users) {
            TaskUser tu = new TaskUser(id, u.getId());
            em.persist(tu);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    @FXML private void taskAction() {
        if(this.content.getText().length() == 0) HelloUI.showError("opis zadania nie może być pusty");
        else if(this.hour.getText().length() == 0) HelloUI.showError("godzina realizacji nie może być pusta");
        else if(this.workers2.getItems().isEmpty()) HelloUI.showError("musisz przydzielić pracowników do tego zadania");
        else if(this.picker.getValue() == null) HelloUI.showError("musisz wybrać termin realizacji");
        else if(HelloSession.getTaskEdit() == null) {
            Task t = new Task(HelloSession.getUser().getId(), HelloSession.getGroupView(), this.picker.getValue().toString(), this.hour.getText(), this.content.getText(), null, 0);
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
            em.close();
            this.addTaskUsers(t.getId(), this.workers2.getItems());
            this.closeWindow();
        }
        else if(HelloSession.getTaskEdit() != null) {
            EntityManager em = JpaUtil.getFactory().createEntityManager();
            Query q = em.createQuery("select t from Task t where t.id = " + HelloSession.getTaskEdit());
            List<Task> result = q.getResultList();
            Task t = result.get(0);
            em.getTransaction().begin();
            t.setContent(this.content.getText());
            t.setCreator(HelloSession.getUser().getId());
            t.setDeadline(this.picker.getValue().toString());
            t.setDeadlinehour(this.hour.getText());
            em.persist(t);
            em.getTransaction().commit();
            this.removeTaskUsers(t.getId());
            this.addTaskUsers(t.getId(), this.workers2.getItems());
            em.close();
            this.closeWindow();
        }
    }
    
    @FXML private void cancel() {
        this.closeWindow();
    }
    
    private boolean compareUsers(List<HelloUser> source, HelloUser u) {
        for(HelloUser user : source) {
            if(user.getId().equals(u.getId())) return true;
        }
        return false;
    }
    
    private void populateList() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select u from Membership m join m.memberUser u where m.workgroup = " + HelloSession.getGroupView());
        List<HelloUser> results = q.getResultList();
        for(HelloUser user : results) {
            if(!this.compareUsers(this.workers2.getItems(), user)) this.workers1.getItems().add(user);
        }
        em.close();
    }
    
    public void prepareData() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select t from Task t where t.id = " + HelloSession.getTaskEdit());
        List<Task> result = q.getResultList();
        editedTask = result.get(0);
        content.setText(editedTask.getContent());
        hour.setText(editedTask.getDeadlinehour());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(editedTask.getDeadline(), formatter);
        picker.setValue(localDate);
        for(TaskUser tu : this.editedTask.getTaskusers()) {
            this.workers1.getItems().remove(tu.getWorker());
            this.workers2.getItems().add(tu.getWorker());
        }
        em.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = 
                DateTimeFormatter.ofPattern("yyyy-MM-dd");
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };             
        picker.setConverter(converter);
        if(HelloSession.getTaskEdit() != null) this.prepareData();
        this.populateList();
    }
    
}
