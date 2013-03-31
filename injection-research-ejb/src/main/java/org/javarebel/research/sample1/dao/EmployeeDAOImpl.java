/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javarebel.research.sample1.dao;

import java.util.Arrays;
import java.util.List;
import org.javarebel.research.commons.dao.AirFrameworkDAOException;
import org.javarebel.research.sample1.injections.test.EmployeeVO;

/**
 *
 * @author naveen
 */
public class EmployeeDAOImpl implements IEmployeeDAO {

    @Override
    public List<EmployeeVO> findEmplyeesByLname(String lname) throws AirFrameworkDAOException {
        System.out.println("findEmplyeesByLname with last name " + lname);
        EmployeeVO emp = new EmployeeVO();
        emp.setEmpno(1L);
        emp.setFname("Naveen");
        emp.setLname("Sisupalan");
        return Arrays.asList(new EmployeeVO[] {emp});
    }

    @Override
    public String findEmplyeesById(Long empno) throws AirFrameworkDAOException {
        System.out.println("findEmplyeesById with empno " + empno);
        return "Naveen Sisupalan";
    }
    
}
