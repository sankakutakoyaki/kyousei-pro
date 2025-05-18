package kyousei.kyousei.entity.timeworks;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class TimeworksListEntity implements IEntity {

    private int timeworks_id;
    private int employee_id;
    private int category_id;
    private String full_name;
    private String office_name;
    private LocalDate work_date;
    private String comp_start_time;
    private String comp_end_time;

    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.timeworks_id = rs.getInt("timeworks_id");
            this.employee_id = rs.getInt("employee_id");
            this.category_id = rs.getInt("category_id");
            this.full_name = rs.getString("full_name");
            this.office_name = rs.getString("office_name");
            if (rs.getTimestamp("comp_start_time") != null){
                LocalDateTime startTime = rs.getTimestamp("comp_start_time").toLocalDateTime();
                this.comp_start_time = startTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            if (rs.getTimestamp("comp_end_time") != null) {
                LocalDateTime endTime = rs.getTimestamp("comp_end_time").toLocalDateTime();
                this.comp_end_time = endTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t.*, e.employee_id, c.category_id, COALESCE(o.office_name, '登録なし') as office_name, p.full_name From timeworks t");
        sb.append(" INNER JOIN employees e ON e.employee_id = t.employee_id");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id");
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id");
        sb.append(" WHERE t.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
}
