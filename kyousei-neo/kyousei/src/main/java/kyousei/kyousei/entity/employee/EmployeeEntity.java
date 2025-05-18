package kyousei.kyousei.entity.employee;

import java.sql.ResultSet;
import java.time.LocalDateTime;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.abstracts.PersonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeEntity extends PersonEntity {
    private int employee_id;
    private int person_id;
    private int company_id;
    private int office_id;
    private int category_id;    
    private int code;
    private String emergency_contact;
    private String emergency_contact_number;
    private int version;
    private int state;

    private String company_name;
    private String office_name;
    private String ids;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.employee_id = rs.getInt("employee_id");
            this.person_id = rs.getInt("person_id");
            this.company_id = rs.getInt("company_id");
            this.office_id = rs.getInt("office_id");
            this.category_id = rs.getInt("category_id");
            this.code = rs.getInt("code");
            this.emergency_contact = rs.getString("emergency_contact");
            this.emergency_contact_number = rs.getString("emergency_contact_number");
            this.version = rs.getInt("version");

            this.company_name = rs.getString("company_name");
            this.office_name = rs.getString("office_name");

            super.setEntity(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @MAX_CODE int = 0;SELECT @MAX_CODE = MAX(code) FROM employees WHERE state = " + Enums.state.UNDECIDED.getNum() + ";");
        sb.append(super.getInsertString());
        sb.append(logTable());
        // sb.append("DECLARE @EmployeeTable TABLE (");
        // sb.append("editor NVARCHAR(255)");
        // sb.append(", state NVARCHAR(50)");
        // sb.append(", regist_date DATETIME2(7)");
        // sb.append(", employee_id INT");
        // sb.append(", person_id INT");
        // sb.append(", company_id INT");
        // sb.append(", office_id INT");
        // sb.append(", category_id INT");
        // sb.append(", code INT");
        // sb.append(", emergency_contact NVARCHAR(255)");
        // sb.append(", emergency_contact_number NVARCHAR(255)");
        // sb.append(", state INT");
        // sb.append(");");
        // sb.append("DECLARE @MAX_CODE int = 0;SELECT @MAX_CODE = MAX(code) FROM employees WHERE state = " + Enums.state.UNDECIDED.getNum() + ";");
        sb.append("INSERT INTO employees (");
        sb.append("person_id");
        sb.append(", company_id");
        sb.append(", office_id");
        sb.append(", category_id");
        sb.append(", code");
        sb.append(", emergency_contact");
        sb.append(", emergency_contact_number");
        sb.append(")");
        sb.append(logString("作成"));
        // sb.append(" OUTPUT '" + this.getUser_name() + "', '作成', CURRENT_TIMESTAMP, INSERTED.employee_id, INSERTED.person_id, INSERTED.company_id, INSERTED.office_id, INSERTED.category_id, INSERTED.code, INSERTED.emergency_contact, INSERTED.emergency_contact_number, INSERTED.state");
        // sb.append(" INTO @EmployeeTable (editor, state, regist_date, employee_id, person_id, company_id, office_id, category_id, code, emergency_contact, emergency_contact_number, state)");
        sb.append(" VALUES (");
        sb.append("@NEW_PERSON_ID");
        sb.append(", " + this.getCompany_id());
        sb.append(", " + this.getOffice_id());
        sb.append(", " + this.getCategory_id());
        sb.append(", @NEW_CODE");
        sb.append(", '" + this.getEmergency_contact() + "'");
        sb.append(", '" + this.getEmergency_contact_number() + "'");
        sb.append(");");
        sb.append("DECLARE @NEW_EMP_ID int; SET @NEW_EMP_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("IF @NEW_EMP_ID > 0 BEGIN");
        sb.append(" INSERT INTO employees_log SELECT * FROM @EmployeeTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'employee', '作成', 'EMPLOYEE_ID=' + CONVERT(nvarchar,@NEW_EMP_ID));");
        sb.append("SELECT @NEW_EMP_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'employee', '作成失敗', '');");
        sb.append(" SELECT 0 as number;");
        sb.append("END;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getUpdateString());
        sb.append(logTable());
        sb.append("UPDATE employees SET");
        sb.append(" code = " + this.getCode());
        sb.append(", company_id = " + this.getCompany_id());
        sb.append(", office_id = " + this.getOffice_id());
        sb.append(", category_id = " + this.getCategory_id());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", emergency_contact = '" + this.getEmergency_contact() + "'");
        sb.append(", emergency_contact_number = '" + this.getEmergency_contact_number() + "'");
        sb.append(", state = " + this.getState());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(logString("更新"));
        // sb.append(" OUTPUT '" + this.getUser_name() + "', '更新', CURRENT_TIMESTAMP, INSERTED.employee_id, INSERTED.person_id, INSERTED.company_id, INSERTED.office_id, INSERTED.category_id, INSERTED.code, INSERTED.emergency_contact, INSERTED.emergency_contact_number, INSERTED.state");
        // sb.append(" INTO employees_log (editor, state, regist_date, employee_id, person_id, company_id, office_id, category_id, code, emergency_contact, emergency_contact_number)");
        sb.append(" WHERE employee_id = " + this.getEmployee_id() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO employees_log SELECT * FROM @EmployeeTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'employee', '更新', 'EMPLOYEE_ID=" + this.getEmployee_id() + "');");
        sb.append("SELECT " + this.getEmployee_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'employee', '更新失敗', 'EMPLOYEE_ID=" + this.getEmployee_id() + "');");
        sb.append("SELECT 0 as number; END;");

        return sb.toString();
    }
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE employees SET state = " + Enums.state.DELETE.getNum());
        sb.append(logString("削除"));
        // sb.append(" OUTPUT '" + this.getUser_name() + "', '削除', CURRENT_TIMESTAMP, INSERTED.employee_id, INSERTED.person_id, INSERTED.company_id, INSERTED.office_id, INSERTED.category_id, INSERTED.code, INSERTED.emergency_contact, INSERTED.emergency_contact_number, INSERTED.state");
        // sb.append(" INTO employees_log (editor, state, regist_date, employee_id, person_id, company_id, office_id, category_id, code, emergency_contact, emergency_contact_number, state)");
        sb.append(" WHERE employee_id " + this.getIds() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO employees_log SELECT * FROM @EmployeeTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'employee', '削除', 'EMPLOYEE_ID=" + this.getEmployee_id() + "');");
        sb.append("SELECT @ROW_COUNT as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'employee', '削除失敗', 'EMPLOYEE_ID=" + this.getEmployee_id() + "');");
        sb.append("SELECT 0 as number; END;");

        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @EmployeeTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", employee_id INT");
        sb.append(", person_id INT");
        sb.append(", company_id INT");
        sb.append(", office_id INT");
        sb.append(", category_id INT");
        sb.append(", code INT");
        sb.append(", emergency_contact NVARCHAR(255)");
        sb.append(", emergency_contact_number NVARCHAR(255)");
        sb.append(", state INT");
        sb.append(");");
        return sb.toString();
    }
    private String logString(String process) {
        StringBuilder sb = new StringBuilder();
        sb.append(" OUTPUT");
        sb.append(" '" + this.getUser_name() + "'");
        sb.append(", '" + process + "'");
        sb.append(", CURRENT_TIMESTAMP");
        sb.append(", INSERTED.employee_id");
        sb.append(", INSERTED.person_id");
        sb.append(", INSERTED.company_id");
        sb.append(", INSERTED.office_id");
        sb.append(", INSERTED.category_id");
        sb.append(", INSERTED.code");
        sb.append(", INSERTED.emergency_contact");
        sb.append(", INSERTED.emergency_contact_number");
        sb.append(", INSERTED.state");
        sb.append(" INTO @EmployeeTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", employee_id");
        sb.append(", person_id");
        sb.append(", company_id");
        sb.append(", office_id");
        sb.append(", category_id");
        sb.append(", code");
        sb.append(", emergency_contact");
        sb.append(", emergency_contact_number");
        sb.append(", state");
        sb.append(")");
        return sb.toString();
    }
}
