package org.pawelwaz.helloworkz.entity;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.pawelwaz.helloworkz.util.HelloUI;

@Entity
@Table(name = "hellouser")
public class HelloUser {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;
    private String name = null;
    private String surname = null;
    private String email = null;
    private String organisation = null;
    private String phone = null;
    private String job = null;
    private byte[] avatar = null;
    @Transient
    private BufferedImage readyAvatar = null;
    
    public HelloUser() {
        super();
    }
    
    public HelloUser(Long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }
    
    public HelloUser(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    public void prepareAvatar() {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(this.avatar);
            this.readyAvatar = ImageIO.read(stream);
        }
        catch(Exception ex) {
            HelloUI.showError("Problem z działaniem aplikacji, zostanie ona zamknięta");
            System.exit(1);
        }
    }
    
    public void setDefaultAvatar() {
        try {
            File file = new File("classes/img/avatar.png");
            BufferedImage bufferedImage = ImageIO.read(file);
            ByteArrayOutputStream stream = new ByteArrayOutputStream(262144);
            ImageIO.write(bufferedImage, "png", stream);
            stream.flush();
            this.avatar = stream.toByteArray();
        }
        catch(Exception ex) {
            HelloUI.showError("Brak części plików aplikacji. Zostanie ona zamknięta");
            System.exit(1);
        }
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the organisation
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * @param organisation the organisation to set
     */
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the job
     */
    public String getJob() {
        return job;
    }

    /**
     * @param job the job to set
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * @return the avatar
     */
    public byte[] getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
        this.prepareAvatar();
    }

    /**
     * @return the readyAvatar
     */
    public BufferedImage getReadyAvatar() {
        return readyAvatar;
    }

    /**
     * @param readyAvatar the readyAvatar to set
     */
    public void setReadyAvatar(BufferedImage readyAvatar) {
        this.readyAvatar = readyAvatar;
        this.prepareAvatar();
    }
    
}
