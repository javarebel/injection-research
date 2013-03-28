/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javarebel.research.sample1.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author naveen
 */
@Entity
@Table(name = "login_credentials", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LoginCredentials.findAll", query = "SELECT l FROM LoginCredentials l"),
    @NamedQuery(name = "LoginCredentials.findByUsername", query = "SELECT l FROM LoginCredentials l WHERE l.username = :username"),
    @NamedQuery(name = "LoginCredentials.findByPassword", query = "SELECT l FROM LoginCredentials l WHERE l.password = :password"),
    @NamedQuery(name = "LoginCredentials.findByUserid", query = "SELECT l FROM LoginCredentials l WHERE l.userid = :userid")})
public class LoginCredentials implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "username")
    private String username;
    @Size(max = 2147483647)
    @Column(name = "password")
    private String password;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "userid")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "login_userid_seq")
    @SequenceGenerator(name="login_userid_seq",sequenceName = "login_userid_seq", allocationSize = 1)
    private Integer userid;

    public LoginCredentials() {
    }

    public LoginCredentials(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoginCredentials)) {
            return false;
        }
        LoginCredentials other = (LoginCredentials) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.javarebel.research.sample1.entity.LoginCredentials[ userid=" + userid + " ]";
    }
    
}
