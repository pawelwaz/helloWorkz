package org.pawelwaz.helloworkz.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author pawelwaz
 */
public class JpaUtil {
    private static EntityManagerFactory factory = null;
    
    public static void buildFactory() {
        try {
            factory = Persistence.createEntityManagerFactory("hello", System.getProperties());
        }
        catch(Exception ex) {
            showError();
        }
    }
    
    private static void showError() {
        Alert a = new Alert(AlertType.ERROR, "Wystąpił błąd połączenia z bazą danych. Aplikacja zostanie zamknięta");
        a.setTitle("");
        a.setHeaderText("Błąd");
        a.showAndWait();
        System.exit(1);
    }
    
    public static EntityManagerFactory getFactory() {
        return factory;
    }
    
    public static void shutdown() {
        try {
            factory.close();
        }
        catch(Exception ex) {
            showError();
        }
    }
    
}