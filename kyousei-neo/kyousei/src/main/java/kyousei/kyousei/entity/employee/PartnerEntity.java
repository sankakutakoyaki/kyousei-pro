package kyousei.kyousei.entity.employee;

import java.sql.ResultSet;

import kyousei.kyousei.common.Enums;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PartnerEntity extends EmployeeEntity {

    private int partner_id;
    private int commission;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.partner_id = rs.getInt("partner_id");
            this.commission = rs.getInt("commission");
            super.setEntity(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, c.company_name, o.office_name, p.*, pa.* From employees e");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" INNER JOIN partners pa ON pa.employee_id = e.employee_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE e.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInsertString());
        sb.append(logTable());
        sb.append("INSERT INTO partners (");
        sb.append("employee_id");
        sb.append(", commission");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append("@NEW_EMP_ID");
        sb.append(", " + this.getCommission());
        sb.append(");");
        // 変更履歴
        sb.append("INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'partners', '作成', 'ID=' + CONVERT(nvarchar,@NEW_EMP_ID));");
        sb.append("INSERT INTO partners_log SELECT * FROM @PartnerTable;");
        // 新規IDを返す
        sb.append("SELECT @NEW_EMP_ID as number;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getUpdateString());
        sb.append(logTable());
        sb.append("UPDATE partners SET");
        sb.append(" employee_id = " + this.getEmployee_id());
        sb.append(", commission = " + this.getCommission());
        sb.append(logString("更新"));
        sb.append(" WHERE partner_id = " + this.getPartner_id() + ";");
        // 変更履歴
        sb.append("INSERT INTO partners_log SELECT * FROM @PartnerTable;");
        sb.append("INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'partners', '更新', 'ID=" + this.getEmployee_id() + "');");
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
        sb.append("'" + this.getUser_name() + "', 'partners', '削除', 'ID=" + (this.getIds() == null ? "0": this.getIds().substring(3)) + "');");
        // 削除が失敗していれば０を返す
        sb.append("SELECT @ROW_COUNT as number;");

        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @PartnerTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", partner_id INT");
        sb.append(", employee_id INT");
        sb.append(", commission INT");
        sb.append(");");

        return sb.toString();
    }
    private String logString(String process) {
        StringBuilder sb = new StringBuilder();
        sb.append(" OUTPUT");
        sb.append(" '" + this.getUser_name() + "'");
        sb.append(", '" + process + "'");
        sb.append(", CURRENT_TIMESTAMP");
        sb.append(", INSERTED.partner_id");
        sb.append(", INSERTED.employee_id");
        sb.append(", INSERTED.commission");
        // sb.append(")");
        sb.append(" INTO @PartnerTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", partner_id");
        sb.append(", employee_id");
        sb.append(", commission");
        sb.append(")");

        return sb.toString();
    }
}
