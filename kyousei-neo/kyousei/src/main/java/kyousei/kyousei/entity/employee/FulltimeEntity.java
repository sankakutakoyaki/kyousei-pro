package kyousei.kyousei.entity.employee;

import java.sql.ResultSet;

import kyousei.kyousei.common.Enums;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FulltimeEntity extends EmployeeEntity {
    private int fulltime_id;
    private int trans_cost;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.fulltime_id = rs.getInt("fulltime_id");
            this.trans_cost = rs.getInt("trans_cost");
            super.setEntity(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, c.company_name, o.office_name, p.*, f.* From employees e");
        sb.append(" INNER JOIN fulltimes f ON f.employee_id = e.employee_id");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE e.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInsertString());
        sb.append(logTable());
        sb.append("INSERT INTO fulltimes (");
        sb.append("employee_id");
        sb.append(", trans_cost");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append("@NEW_EMP_ID");
        sb.append(", " + this.getTrans_cost());
        sb.append(");");
        // 変更履歴
        sb.append("INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'fulltimes', '作成', 'ID=' + CONVERT(nvarchar,@NEW_EMP_ID));");
        sb.append("INSERT INTO fulltimes_log SELECT * FROM @FulltimeTable;");
        // 新規IDを返す
        sb.append("SELECT @NEW_EMP_ID as number;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getUpdateString());
        sb.append(logTable());
        sb.append("UPDATE fulltimes SET");
        sb.append(" employee_id = " + this.getEmployee_id());
        sb.append(", trans_cost = " + this.getTrans_cost());
        sb.append(logString("更新"));
        sb.append(" WHERE fulltime_id = " + this.getFulltime_id() + ";");
        // 変更履歴
        sb.append("INSERT INTO fulltimes_log SELECT * FROM @FulltimeTable;");
        sb.append("INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'fulltimes', '更新', 'ID=" + this.getEmployee_id() + "');");
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
        sb.append("'" + this.getUser_name() + "', 'fulltimes', '削除', 'ID=" + (this.getIds() == null ? "0": this.getIds().substring(3)) + "');");
        // 削除が成功していれば1を返す、失敗していれば０を返す
        sb.append("SELECT @ROW_COUNT as number;");

        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @FulltimeTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", fulltime_id INT");
        sb.append(", employee_id INT");
        sb.append(", trans_cost INT");
        sb.append(");");

        return sb.toString();
    }
    private String logString(String process) {
        StringBuilder sb = new StringBuilder();
        sb.append(" OUTPUT");
        sb.append(" '" + this.getUser_name() + "'");
        sb.append(", '" + process + "'");
        sb.append(", CURRENT_TIMESTAMP");
        sb.append(", INSERTED.fulltime_id");
        sb.append(", INSERTED.employee_id");
        sb.append(", INSERTED.trans_cost");
        // sb.append(")");
        sb.append(" INTO @FulltimeTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", fulltime_id");
        sb.append(", employee_id");
        sb.append(", trans_cost");
        sb.append(")");

        return sb.toString();
    }
}
