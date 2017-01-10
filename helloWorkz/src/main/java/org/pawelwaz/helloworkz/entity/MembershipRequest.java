package org.pawelwaz.helloworkz.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "membershiprequest")
public class MembershipRequest implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long workgroup;
    private Long hellouser;

    public MembershipRequest() {
    }
    
    public MembershipRequest(Long workgroup, Long hellouser) {
        this.workgroup = workgroup;
        this.hellouser = hellouser;
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
     * @return the workgroup
     */
    public Long getWorkgroup() {
        return workgroup;
    }

    /**
     * @param workgroup the workgroup to set
     */
    public void setWorkgroup(Long workgroup) {
        this.workgroup = workgroup;
    }

    /**
     * @return the hellouser
     */
    public Long getHellouser() {
        return hellouser;
    }

    /**
     * @param hellouser the hellouser to set
     */
    public void setHellouser(Long hellouser) {
        this.hellouser = hellouser;
    }
}
