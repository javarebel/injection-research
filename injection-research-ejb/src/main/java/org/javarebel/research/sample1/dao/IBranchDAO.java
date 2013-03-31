/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javarebel.research.sample1.dao;

import org.javarebel.research.commons.dao.AirFrameworkDAOException;
import org.javarebel.research.commons.dao.ConnectionInfo;
import org.javarebel.research.commons.dao.Query;

/**
 *
 * @author naveen
 */
@ConnectionInfo("java:/OracleDS")
public interface IBranchDAO {
    
    @Query(value="select deptname from HUMAN.afcu_department where branchno=?")
    String findBranchById(Long branchId) throws AirFrameworkDAOException;
}
