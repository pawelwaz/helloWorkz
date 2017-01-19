package org.pawelwaz.helloworkz.util;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.pawelwaz.helloworkz.controller.DiscussionWindowController;
import org.pawelwaz.helloworkz.controller.MainViewController;
import org.pawelwaz.helloworkz.controller.MessageWindowController;
import org.pawelwaz.helloworkz.entity.HelloUser;

/**
 *
 * @author pawelwaz
 */
public class HelloSession {
    
    private static HelloUser user;
    private static URI htmlAvatar;
    private static List<MessageWindowController> mesgWindows = new ArrayList();
    private static List<DiscussionWindowController> discWindows = new ArrayList();
    private static List<Long> mesgWindowsIds = new ArrayList();
    private static List<Long> discWindowsIds = new ArrayList();
    private static MainViewController mainController = null;
    private static HelloUI subController = null;
    private static List<Long> userContacts;
    private static List<HelloUser> userContactPersons;
    private static Long groupId = null;
    private static Long groupView = null;
    private static Long membershipId = null;
    private static Long taskEdit = null;
    
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

    /**
     * @return the subController
     */
    public static HelloUI getSubController() {
        return subController;
    }

    /**
     * @param aSubController the subController to set
     */
    public static void setSubController(HelloUI aSubController) {
        subController = aSubController;
    }

    /**
     * @return the groupId
     */
    public static Long getGroupId() {
        return groupId;
    }

    /**
     * @param aGroupId the groupId to set
     */
    public static void setGroupId(Long aGroupId) {
        groupId = aGroupId;
    }

    /**
     * @return the groupView
     */
    public static Long getGroupView() {
        return groupView;
    }

    /**
     * @param aGroupView the groupView to set
     */
    public static void setGroupView(Long aGroupView) {
        groupView = aGroupView;
    }

    /**
     * @return the membershipId
     */
    public static Long getMembershipId() {
        return membershipId;
    }

    /**
     * @param aMembershipId the membershipId to set
     */
    public static void setMembershipId(Long aMembershipId) {
        membershipId = aMembershipId;
    }

    /**
     * @return the discWindows
     */
    public static List<DiscussionWindowController> getDiscWindows() {
        return discWindows;
    }

    /**
     * @param aDiscWindows the discWindows to set
     */
    public static void setDiscWindows(List<DiscussionWindowController> aDiscWindows) {
        discWindows = aDiscWindows;
    }

    /**
     * @return the discWindowsIds
     */
    public static List<Long> getDiscWindowsIds() {
        return discWindowsIds;
    }

    /**
     * @param aDiscWindowsIds the discWindowsIds to set
     */
    public static void setDiscWindowsIds(List<Long> aDiscWindowsIds) {
        discWindowsIds = aDiscWindowsIds;
    }

    /**
     * @return the taskEdit
     */
    public static Long getTaskEdit() {
        return taskEdit;
    }

    /**
     * @param aTaskEdit the taskEdit to set
     */
    public static void setTaskEdit(Long aTaskEdit) {
        taskEdit = aTaskEdit;
    }
    
}
