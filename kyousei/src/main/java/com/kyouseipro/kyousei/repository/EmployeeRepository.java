package com.kyouseipro.kyousei.repository;

import org.springframework.stereotype.Repository;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.entity.employee.EmployeeEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.ISql;

@Repository
public class EmployeeRepository implements ISql {

    /**
     * アカウントから[employee]を取得
     * @param account
     * @return employee
     */
    public IEntity getEmployeeForAccount(String account) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, p.*, a.account, c.company_name, o.office_name FROM employees e");
        sb.append(" INNER JOIN accounts a ON a.person_id = e.person_id");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.INITIAL.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE a.account = '" + account + "' AND e.state = " + Enums.state.INITIAL.getNum());
        
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new EmployeeEntity());
        sqlData.setSqlString(sb.toString());

        return getEntity(sqlData);
    }

    /**
     * コードから[employee]を取得
     * @param code
     * @return employee
     */
    public IEntity getEmployeeForCode(int code) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, p.*, c.company_name, o.office_name FROM employees e");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.INITIAL.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE e.code = " + code + " AND e.state = " + Enums.state.INITIAL.getNum());
        
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new EmployeeEntity());
        sqlData.setSqlString(sb.toString());

        return getEntity(sqlData);
    }

    /**
     * IDから[employee]を取得
     * @param code
     * @return employee
     */
    public IEntity getEmployeeForId(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, p.*, c.company_name, o.office_name FROM employees e");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.INITIAL.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE e.employee_id = " + id + " AND e.state = " + Enums.state.INITIAL.getNum());
        
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new EmployeeEntity());
        sqlData.setSqlString(sb.toString());

        return getEntity(sqlData);
    }

}
