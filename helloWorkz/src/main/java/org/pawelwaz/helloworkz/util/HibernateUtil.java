package org.pawelwaz.helloworkz.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author pawelwaz
 */
public class HibernateUtil {
    
    private static final SessionFactory factory = buildSessionFactory();
    
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure("/sql/hibernate.cfg.xml").buildSessionFactory();
        }
        catch(Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return factory;
    }
    
    public static void shutdown() {
        getSessionFactory().close();
    }
}
