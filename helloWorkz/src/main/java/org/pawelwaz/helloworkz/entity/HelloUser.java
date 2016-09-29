package org.pawelwaz.helloworkz.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hellouser")
public class HelloUser {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    private String login;
    @Basic
    private String password;
    
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
    
}
