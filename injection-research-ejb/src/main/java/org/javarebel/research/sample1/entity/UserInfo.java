/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javarebel.research.sample1.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author naveen
 */
@Entity
@Table(name = "user_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserInfo.findAll", query = "SELECT u FROM UserInfo u"),
    @NamedQuery(name = "UserInfo.findByUserid", query = "SELECT u FROM UserInfo u WHERE u.userid = :userid"),
    @NamedQuery(name = "UserInfo.findByFname", query = "SELECT u FROM UserInfo u WHERE u.fname = :fname"),
    @NamedQuery(name = "UserInfo.findByLname", query = "SELECT u FROM UserInfo u WHERE u.lname = :lname"),
    @NamedQuery(name = "UserInfo.findByAddress", query = "SELECT u FROM UserInfo u WHERE u.address = :address"),
    @NamedQuery(name = "UserInfo.findByState", query = "SELECT u FROM UserInfo u WHERE u.state = :state"),
    @NamedQuery(name = "UserInfo.findByZip", query = "SELECT u FROM UserInfo u WHERE u.zip = :zip"),
    @NamedQuery(name = "UserInfo.findByPhone", query = "SELECT u FROM UserInfo u WHERE u.phone = :phone")})
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "userid")
    private Integer userid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "fname")
    private String fname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "lname")
    private String lname;
    @Size(max = 2147483647)
    @Column(name = "address")
    private String address;
    @Size(max = 2)
    @Column(name = "state")
    private String state;
    @Column(name = "zip")
    private Integer zip;
    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 12)
    @Column(name = "phone")
    private String phone;
    @JoinColumn(name = "userid", referencedColumnName = "loginid", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private LoginCredentials loginCredentials;

    public UserInfo() {
    }

    public UserInfo(Integer userid) {
        this.userid = userid;
    }

    public UserInfo(Integer userid, String fname, String lname) {
        this.userid = userid;
        this.fname = fname;
        this.lname = lname;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LoginCredentials getLoginCredentials() {
        return loginCredentials;
    }

    public void setLoginCredentials(LoginCredentials loginCredentials) {
        this.loginCredentials = loginCredentials;
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
        if (!(object instanceof UserInfo)) {
            return false;
        }
        UserInfo other = (UserInfo) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.javarebel.research.sample1.entity.UserInfo[ userid=" + userid + " ]";
    }
    
}
