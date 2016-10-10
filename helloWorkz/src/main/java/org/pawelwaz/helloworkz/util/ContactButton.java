package org.pawelwaz.helloworkz.util;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.pawelwaz.helloworkz.entity.Contact;
import org.pawelwaz.helloworkz.entity.HelloUser;
import org.pawelwaz.helloworkz.entity.Message;

/**
 *
 * @author pawelwaz
 */
public class ContactButton extends ImageView {
    
    private HelloUser user;
    private boolean swap;
    private boolean add;
    
    public ContactButton(HelloUser user, boolean add, boolean swap) {
        this.user = user;
        this.add = add;
        this.swap = swap;
        if(add) this.setImage(HelloUI.addContactButton);
        else this.setImage(HelloUI.removeContactButton);
        this.setCursor(Cursor.HAND);
    }
    
    public void handleContact() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        if(this.add) {
            if(HelloUI.showConfirmation("Czy dodać użytkownika " + this.user.getLogin() + " do osobistej listy kontaktów?")) {
                Contact contact = new Contact();
                contact.setOwner(HelloSession.getUser().getId());
                contact.setPerson(this.user.getId());
                em.getTransaction().begin();
                em.persist(contact);
                em.getTransaction().commit();
                HelloSession.getUserContacts().add(this.user.getId());
                if(this.swap) {
                    this.add = false;
                    this.setImage(HelloUI.removeContactButton);
                    Tooltip.install(this, new Tooltip("usuń z kontaktów"));
                }
            }
        }
        else {
            if(HelloUI.showConfirmation("Czy usunąć użytkownika " + this.user.getLogin() + " z osobistej listy kontaktów?")) {
                    HelloSession.getUserContacts().remove(this.user.getId());
                    CriteriaBuilder builder = em.getCriteriaBuilder();
                    CriteriaQuery q = builder.createQuery();
                    Root<Contact> root = q.from(Contact.class);
                    q.select(root);
                    List<Predicate> preds = new ArrayList();
                    preds.add(builder.equal(root.get("owner"), HelloSession.getUser().getId()));
                    preds.add(builder.equal(root.get("person"), this.user.getId()));
                    q.where(preds.toArray(new Predicate[preds.size()]));
                    List<Contact> result = em.createQuery(q).getResultList();
                    Contact contact = result.get(0);
                    em.getTransaction().begin();
                    em.remove(contact);
                    em.getTransaction().commit();
                    if(this.swap) {
                    this.add = true;
                    this.setImage(HelloUI.addContactButton);
                    Tooltip.install(this, new Tooltip("dodaj do kontaktów"));
                }
            }
        }
        em.close();
    }
    
}
