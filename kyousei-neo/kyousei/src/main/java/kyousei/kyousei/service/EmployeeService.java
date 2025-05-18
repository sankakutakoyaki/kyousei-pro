package kyousei.kyousei.service;

import org.springframework.stereotype.Service;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.entity.employee.EmployeeEntity;
import kyousei.kyousei.interfaceis.IEntity;

@Service
// @RequiredArgsConstructor
public class EmployeeService {
    // private final DownloadService downloadService;
    // private final SqlRepository sqlRepository;
    /**
     * アカウントから[employee]を取得
     * @param account
     * @return employee
     */
    public static IEntity getEmployeeForAccount(String account) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, p.*, a.account, c.company_name, o.office_name FROM employees e");
        sb.append(" INNER JOIN accounts a ON a.person_id = e.person_id");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE a.account = '" + account + "' AND e.state = " + Enums.state.UNDECIDED.getNum());
        
        SqlData sqlData = new SqlData();
        sqlData = DownloadService.createSqlData(new EmployeeEntity(), sb.toString(), null);
        return SqlService.getEntity(sqlData);
    }
    /**
     * IDから[employee]を取得
     * @param code
     * @return employee
     */
    public static IEntity getEmployeeForId(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, p.*, c.company_name, o.office_name FROM employees e");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE e.employee_id = " + id + " AND e.state = " + Enums.state.UNDECIDED.getNum());
        
        SqlData sqlData = new SqlData();
        sqlData = DownloadService.createSqlData(new EmployeeEntity(), sb.toString(), null);

        return SqlService.getEntity(sqlData);
    }
    /**
     * コードから[employee]を取得
     * @param code
     * @return employee
     */
    public static IEntity getEmployeeForCode(int code) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, p.*, c.company_name, o.office_name FROM employees e");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE e.code = " + code + " AND e.state = " + Enums.state.UNDECIDED.getNum());
        
        SqlData sqlData = new SqlData();
        sqlData = DownloadService.createSqlData(new EmployeeEntity(), sb.toString(), null);

        return SqlService.getEntity(sqlData);
    }
}
