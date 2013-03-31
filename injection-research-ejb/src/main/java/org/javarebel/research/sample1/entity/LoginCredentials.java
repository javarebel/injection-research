/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javarebel.research.sample1.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
    @NamedQuery(name = "LoginCredentials.findByPassword", query = "SELECT l FROM LoginCredentials l WHERE l.password = :password")})
public class LoginCredentials implements Serializable {
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "loginCredentials")
    private UserInfo userInfo;
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
    @Column(name = "loginid")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "login_userid_seq")
    @SequenceGenerator(name="login_userid_seq",sequenceName = "login_userid_seq", allocationSize = 1)
    private Integer loginid;

    public LoginCredentials() {
    }

    public LoginCredentials(Integer loginid) {
        this.loginid = loginid;
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

    public Integer getLoginid() {
        return loginid;
    }

    public void setLoginid(Integer loginid) {
        this.loginid = loginid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loginid != null ? loginid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoginCredentials)) {
            return false;
        }
        LoginCredentials other = (LoginCredentials) object;
        if ((this.loginid == null && other.loginid != null) || (this.loginid != null && !this.loginid.equals(other.loginid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.javarebel.research.sample1.entity.LoginCredentials[ userid=" + loginid + " ]";
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    
}
