package kyousei.kyousei.entity.employee;

import java.sql.ResultSet;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class FulltimeListEntity implements IEntity {
    private int employee_id;
    private int code;
    private int category_id; // サイドバー選択用に必要
    private String full_name;
    private String full_name_kana;
    private String phone_number;
    private String office_name; 
    
    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.employee_id = rs.getInt("employee_id");
            this.code = rs.getInt("code");
            this.category_id = rs.getInt("office_id");            
            this.full_name = rs.getString("full_name");
            this.full_name_kana = rs.getString("full_name_kana");
            this.phone_number = rs.getString("phone_number");
            this.office_name = rs.getString("office_name");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public String getFulltimeListString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT f.fulltime_id, e.employee_id, e.code, e.office_id, o.office_name, p.full_name, p.full_name_kana, NULLIF(p.phone_number, '') as phone_number From employees e");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" INNER JOIN fulltimes f ON f.employee_id = e.employee_id");
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE e.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
}
