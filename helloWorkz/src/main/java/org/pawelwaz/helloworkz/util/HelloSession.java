package org.pawelwaz.helloworkz.util;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.pawelwaz.helloworkz.controller.MainViewController;
import org.pawelwaz.helloworkz.controller.MessageWindowController;
import org.pawelwaz.helloworkz.entity.Contact;
import org.pawelwaz.helloworkz.entity.HelloUser;

/**
 *
 * @author pawelwaz
 */
public class HelloSession {
    
    private static HelloUser user;
    private static URI htmlAvatar;
    private static List<MessageWindowController> mesgWindows = new ArrayList();
    private static List<Long> mesgWindowsIds = new ArrayList();
    private static MainViewController mainController = null;
    private static List<Long> userContacts;
    private static List<HelloUser> userContactPersons;
    
    public static void setUser(HelloUser user) {
        HelloSession.user = new HelloUser();
        HelloSession.user.setId(user.getId());
        HelloSession.user.setLogin(user.getLogin());
        HelloSession.user.setPassword(user.getPassword());
        HelloSession.user.setEmail(user.getEmail());
        HelloSession.user.setName(user.getName());
        HelloSession.user.setSurname(user.getSurname());
        HelloSession.user.setOrganisation(user.getOrganisation());
        HelloSession.user.setPhone(user.getPhone());
        HelloSession.user.setJob(user.getJob());
        HelloSession.user.setAvatar(user.getAvatar());
        HelloSession.user.setReadyAvatar(user.getReadyAvatar());
        File tmpAvatar = new File("tmp/" + user.getLogin() + ".png");
        htmlAvatar = tmpAvatar.toURI();
    }
    
    public static List<HelloUser> getUserContactPersons() {
        return HelloSession.userContactPersons;
    }
    
    public static List<Long> getUserContacts() {
        return HelloSession.userContacts;
    }
    
    public static void loadContacts() {
        HelloSession.userContacts = new ArrayList();
        HelloSession.userContactPersons = new ArrayList();
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        Query q = em.createQuery("select p from Contact c inner join c.contactPerson p where c.owner = :owner order by p.login");
        q.setParameter("owner", HelloSession.getUser().getId());
        List<HelloUser> contacts = q.getResultList();
        for(HelloUser contact : contacts) {
            HelloSession.userContacts.add(contact.getId());
            HelloSession.userContactPersons.add(contact);
        }
        em.close();
    }
    
    public static URI getHtmlAvatar() {
        return htmlAvatar;
    }
    
    public static HelloUser getUser() {
        return user;
    }
    
    public static List<MessageWindowController> getMsgWindows() {
        return HelloSession.mesgWindows;
    }
    
    public static List<Long> getMsgWindowsIds() {
        return HelloSession.mesgWindowsIds;
    }

    /**
     * @return the mainController
     */
    public static MainViewController getMainController() {
        return mainController;
    }

    /**
     * @param aMainController the mainController to set
     */
    public static void setMainController(MainViewController aMainController) {
        mainController = aMainController;
    }
    
}
