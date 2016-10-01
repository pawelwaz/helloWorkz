package org.pawelwaz.helloworkz.util;

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
            HelloUI.showError("Wystąpił błąd połączenia z bazą danych. Aplikacja zostanie zamknięta");
        }
    }
    
    public static EntityManagerFactory getFactory() {
        return factory;
    }
    
    public static void shutdown() {
        try {
            factory.close();
        }
        catch(Exception ex) {
            HelloUI.showError("Wystąpił błąd połączenia z bazą danych. Aplikacja zostanie zamknięta");
        }
    }
    
}