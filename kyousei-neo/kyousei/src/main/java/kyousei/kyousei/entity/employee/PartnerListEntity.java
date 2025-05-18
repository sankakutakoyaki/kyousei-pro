package kyousei.kyousei.entity.employee;

import java.sql.ResultSet;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class PartnerListEntity implements IEntity {
    private int employee_id;
    private int code;
    private int category_id; // サイドバー選択用に必要
    private String full_name;
    private String full_name_kana;
    private String phone_number;
    private String company_name; 
    
    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.employee_id = rs.getInt("employee_id");
            this.code = rs.getInt("code");
            this.category_id = rs.getInt("company_id");
            this.full_name = rs.getString("full_name");
            this.full_name_kana = rs.getString("full_name_kana");
            this.phone_number = rs.getString("phone_number");
            this.company_name = rs.getString("company_name");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public String getPartnerListString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, c.company_name, o.office_name, p.full_name, p.full_name_kana, p.phone_number From employees e");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" INNER JOIN partners pa ON pa.employee_id = e.employee_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE e.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
}
