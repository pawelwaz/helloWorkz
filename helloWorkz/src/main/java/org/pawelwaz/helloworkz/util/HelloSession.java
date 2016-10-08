package org.pawelwaz.helloworkz.util;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
    
    public static URI getHtmlAvatar() {
        return htmlAvatar;
    }
    
    public static HelloUser getUser() {
        return user;
    }
    
    public static List<MessageWindowController> getMsgWindows() {
        return HelloSession.mesgWindows;
    }
    
}
