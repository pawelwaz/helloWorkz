package org.pawelwaz.helloworkz.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
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
            FileReader fileReader = new FileReader(new File("host.txt"));
            BufferedReader br = new BufferedReader(fileReader);
            String host = br.readLine();
            Map<String, String> persistenceMap = new HashMap<String, String>();
            persistenceMap.put("javax.persistence.jdbc.url", "jdbc:mysql://" + host + ":3306/hello?useUnicode=yes&amp;characterEncoding=UTF-8");
            factory = Persistence.createEntityManagerFactory("hello", persistenceMap);
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