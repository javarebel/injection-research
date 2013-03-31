package org.javarebel.research.sample1.injections.test;

import java.io.Serializable;

public class EmployeeVO implements Serializable {

    private static final long serialVersionUID = -3733239772579212390L;
    private Long empno;
    private String fname;
    private String lname;

    public Long getEmpno() {
        return empno;
    }

    public void setEmpno(Long empno) {
        this.empno = empno;
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

    @Override
    public String toString() {
        return new StringBuilder("Empno : ")
                .append(this.empno).append(" First Name : ")
                .append(this.fname).append(" Last Name : ")
                .append(this.lname).toString();
    }
}
