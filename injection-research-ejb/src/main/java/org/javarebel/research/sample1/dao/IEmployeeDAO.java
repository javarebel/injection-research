package org.javarebel.research.sample1.dao;

import java.util.List;
import org.javarebel.research.commons.dao.AirFrameworkDAOException;
import org.javarebel.research.commons.dao.ConnectionInfo;
import org.javarebel.research.commons.dao.Query;
import org.javarebel.research.sample1.injections.test.EmployeeVO;

@ConnectionInfo("java:/OracleDS")
public interface IEmployeeDAO {
	
	@Query(value="select empno, fname, lname from HUMAN.personnel_mstr where lname LIKE ?")
	List<EmployeeVO> findEmplyeesByLname(String lname) throws AirFrameworkDAOException;
	
	
	@Query(value="select lname from HUMAN.personnel_mstr where empno = ?")
	String findEmplyeesById(Long empno) throws AirFrameworkDAOException;
}
