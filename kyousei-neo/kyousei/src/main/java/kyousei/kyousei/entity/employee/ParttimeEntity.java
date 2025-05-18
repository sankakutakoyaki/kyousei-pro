package kyousei.kyousei.entity.employee;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kyousei.kyousei.common.Enums;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParttimeEntity extends EmployeeEntity {

    private int parttime_id;
    private int hourly_wage;
    private int weekend_hourly_wage;
    private int trans_cost;
    private int payment_method;
    private String start_regular_hours;
    private String end_regular_hours;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.parttime_id = rs.getInt("parttime_id");
            this.hourly_wage = rs.getInt("hourly_wage");
            this.weekend_hourly_wage = rs.getInt("weekend_hourly_wage");
            this.trans_cost = rs.getInt("trans_cost");
            this.payment_method = rs.getInt("payment_method");
            if (rs.getTimestamp("start_regular_hours") != null){
                LocalDateTime startRegularHours = rs.getTimestamp("start_regular_hours").toLocalDateTime();
                this.start_regular_hours= startRegularHours.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            if (rs.getTimestamp("end_regular_hours") != null){
                LocalDateTime endRegularHours = rs.getTimestamp("end_regular_hours").toLocalDateTime();
                this.end_regular_hours= endRegularHours.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            super.setEntity(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, c.company_name, o.office_name, p.*, pt.* From employees e");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" INNER JOIN parttimes pt ON pt.employee_id = e.employee_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE e.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInsertString());
        sb.append(logTable());
        sb.append("INSERT INTO parttimes (");
        sb.append("employee_id");
        sb.append(", hourly_wage");
        sb.append(", weekend_hourly_wage");
        sb.append(", trans_cost");
        sb.append(", payment_method");
        sb.append(", start_regular_hours");
        sb.append(", end_regular_hours");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append("@NEW_EMP_ID");
        sb.append(", " + this.getHourly_wage());
        sb.append(", " + this.getWeekend_hourly_wage());
        sb.append(", " + this.getTrans_cost());
        sb.append(", " + this.getPayment_method());
        sb.append(", '" + this.getStart_regular_hours() + "'");
        sb.append(", '" + this.getEnd_regular_hours() + "'");
        sb.append(");");
        // 変更履歴
        sb.append("INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'parttimes', '作成', 'ID=' + CONVERT(nvarchar,@NEW_EMP_ID));");
        sb.append("INSERT INTO parttimes_log SELECT * FROM @ParttimeTable;");
        // 新規IDを返す
        sb.append("SELECT @NEW_EMP_ID as number;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getUpdateString());
        sb.append(logTable());
        sb.append("UPDATE parttimes SET");
        sb.append(" employee_id = " + this.getEmployee_id());
        sb.append(", hourly_wage = " + this.getHourly_wage());
        sb.append(", weekend_hourly_wage = " + this.getWeekend_hourly_wage());
        sb.append(", trans_cost = " + this.getTrans_cost());
        sb.append(", payment_method = " + this.getPayment_method());
        sb.append(", start_regular_hours = '" + this.getStart_regular_hours() + "'");
        sb.append(", end_regular_hours = '" + this.getEnd_regular_hours() + "'");
        sb.append(logString("更新"));
        sb.append(" WHERE parttime_id = " + this.getParttime_id() + ";");
        // 変更履歴
        sb.append("INSERT INTO parttimes_log SELECT * FROM @ParttimeTable;");
        sb.append("INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'parttimes', '更新', 'ID=" + this.getEmployee_id() + "');");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("DECLARE @OLD_ID int; IF @ROW_COUNT > 0 SET @OLD_ID = " + this.getEmployee_id() + " ELSE SET @OLD_ID = 0;");
        sb.append("SELECT @OLD_ID as number;");

        return sb.toString();
    }
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDeleteString());
        // 変更履歴
        sb.append("INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'parttimes', '削除', 'ID=" + (this.getIds() == null ? "0": this.getIds().substring(3)) + "');");
        // 削除が失敗していれば０を返す
        sb.append("SELECT @ROW_COUNT as number;");

        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @ParttimeTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", parttime_id INT");
        sb.append(", employee_id INT");
        sb.append(", hourly_wage INT");
        sb.append(", weekend_hourly_wage INT");
        sb.append(", trans_cost INT");
        sb.append(", payment_method INT");
        sb.append(", start_regular_hours NVARCHAR(50)");
        sb.append(", end_regular_hours NVARCHAR(50)");
        sb.append(", regular_breake_time NVARCHAR(50)");
        sb.append(");");

        return sb.toString();
    }
    private String logString(String process) {
        StringBuilder sb = new StringBuilder();
        sb.append(" OUTPUT");
        sb.append(" '" + this.getUser_name() + "'");
        sb.append(", '" + process + "'");
        sb.append(", CURRENT_TIMESTAMP");
        sb.append(", INSERTED.parttime_id");
        sb.append(", INSERTED.employee_id");
        sb.append(", INSERTED.hourly_wage");
        sb.append(", INSERTED.weekend_hourly_wage");
        sb.append(", INSERTED.trans_cost");
        sb.append(", INSERTED.payment_method");
        sb.append(", INSERTED.start_regular_hours");
        sb.append(", INSERTED.end_regular_hours");
        sb.append(", INSERTED.regular_breake_time");
        // sb.append(")");
        sb.append(" INTO @ParttimeTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", parttime_id");
        sb.append(", employee_id");
        sb.append(", hourly_wage");
        sb.append(", weekend_hourly_wage");
        sb.append(", trans_cost");
        sb.append(", payment_method");
        sb.append(", start_regular_hours");
        sb.append(", end_regular_hours");
        sb.append(", regular_breake_time");
        sb.append(")");

        return sb.toString();
    }
}
