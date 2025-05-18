package kyousei.kyousei.entity.expenses;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class CashExpensesEntity implements IEntity {

    private int cash_expenses_id;
    private int bank_id; // 金庫番号
    private String bank_name;
    private LocalDate application_date; // 申請日
    private int applicant_employee_id; // 申請者
    private int applicant_employee_code;
    private String applicant_employee_name;
    private int payment; // 支払い合計
    private LocalDate payment_date; // 支払い日
    private int payment_employee_id; // 支払い者
    private String payment_employee_name;
    private int version;
    private int pay_state;
    private int state;
    private String user_name;
    // private List<CashExpensesDetailEntity> details;
    
    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.cash_expenses_id = rs.getInt("cash_expenses_id");
            this.bank_id = rs.getInt("bank_id");
            this.bank_name = rs.getString("bank_name");
            this.application_date = rs.getDate("application_date").toLocalDate();
            this.applicant_employee_id = rs.getInt("applicant_employee_id");
            this.applicant_employee_code = rs.getInt("applicant_employee_code");
            this.applicant_employee_name = rs.getString("applicant_employee_name");
            this.payment = rs.getInt("payment");
            this.payment_date = rs.getDate("payment_date").toLocalDate();
            this.payment_employee_id = rs.getInt("payment_employee_id");
            this.payment_employee_name = rs.getString("payment_employee_name");
            this.version = rs.getInt("version");
            // this.print_state = rs.getInt("print_state");
            this.pay_state = rs.getInt("pay_state");
            this.state = rs.getInt("state");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT *, app_e.code as applicant_employee_code, app_p.full_name as applicant_employee_name, pay_p.full_name as payment_employee_name, b.title as bank_name FROM cash_expenses ce");
        sb.append(" LEFT OUTER JOIN bank b ON b.bank_id = ce.bank_id AND b.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN employees app_e ON app_e.employee_id = ce.applicant_employee_id AND app_e.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN employees pay_e ON pay_e.employee_id = ce.payment_employee_id AND pay_e.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN persons app_p ON app_p.person_id = app_e.person_id");
        sb.append(" LEFT OUTER JOIN persons pay_p ON pay_p.person_id = pay_e.person_id");
        sb.append(" WHERE ce.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("INSERT INTO cash_expenses (");
        sb.append("bank_id");
        sb.append(", application_date");
        sb.append(", applicant_employee_id");
        sb.append(", payment");
        sb.append(", payment_date");
        sb.append(", payment_employee_id");
        sb.append(", pay_state");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append(this.getBank_id());
        sb.append(", '" + this.getApplication_date() + "'");
        sb.append(", " + this.getApplicant_employee_id());
        sb.append(", " + this.getPayment());
        sb.append(", '" + this.getPayment_date() + "'");
        sb.append(", " + this.getPayment_employee_id());
        sb.append(", " + this.getPay_state());
        sb.append(");");
        sb.append("DECLARE @NEW_CASH_ID int; SET @NEW_CASH_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("IF @NEW_CASH_ID > 0 BEGIN");
        sb.append(" INSERT INTO cash_expenses_log SELECT * FROM @CashExpensesTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'cash_expenses', '作成', 'CASH_EXPENSES_ID=' + CONVERT(nvarchar,@NEW_CASH_ID));");
        sb.append("SELECT @NEW_CASH_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'cash_expenses', '作成失敗', '');");
        sb.append(" SELECT 0 as number;");
        sb.append("END;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE cash_expenses SET");
        sb.append(" bank_id = " + this.getBank_id());
        sb.append(", application_date = '" + this.getApplication_date() + "'");
        sb.append(", applicant_employee_id = " + this.getApplicant_employee_id());
        sb.append(", payment = " + this.getPayment());
        sb.append(", payment_date = '" + this.getPayment_date() + "'");
        sb.append(", payment_employee_id = " + this.getPayment_employee_id());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", pay_state = " + this.getPay_state());
        sb.append(", state = " + this.getState());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(logString("更新"));
        sb.append(" WHERE cash_expenses_id = " + this.getCash_expenses_id() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        sb.append("DECLARE @NEW_CASH_ID int; SET @NEW_CASH_ID = " + this.getCash_expenses_id() + ";");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO cash_expenses_log SELECT * FROM @CashExpensesTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'cash_expenses', '更新', 'CASH_EXPENSES_ID=" + this.getCash_expenses_id() + "');");
        // sb.append("SELECT " + this.getCash_expenses_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'cash_expenses', '更新失敗', 'ID=" + this.getCash_expenses_id() + "');");
        // sb.append("SELECT 0 as number; END;");
        sb.append("END;");

        return sb.toString();
    }
    public String getLogTable() {
        return logTable();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @CashExpensesTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", cash_expenses_id INT");
        sb.append(", bank_id INT");
        sb.append(", application_date DATE");
        sb.append(", applicant_employee_id INT");
        sb.append(", payment INT");
        sb.append(", payment_date DATE");
        sb.append(", payment_employee_id INT");
        sb.append(", pay_state INT");
        sb.append(", state INT");
        sb.append(");");

        return sb.toString();
    }
    public String getLogString(String process) {
        return logString(process);
    }
    private String logString(String process) {
        StringBuilder sb = new StringBuilder();
        sb.append(" OUTPUT");
        sb.append(" '" + this.getUser_name() + "'");
        sb.append(", '" + process + "'");
        sb.append(", CURRENT_TIMESTAMP");
        sb.append(", INSERTED.cash_expenses_id");
        sb.append(", INSERTED.bank_id");
        sb.append(", INSERTED.application_date");
        sb.append(", INSERTED.applicant_employee_id");
        sb.append(", INSERTED.payment");
        sb.append(", INSERTED.payment_date");
        sb.append(", INSERTED.payment_employee_id");
        sb.append(", INSERTED.pay_state");
        sb.append(", INSERTED.state");
        sb.append(" INTO @CashExpensesTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", cash_expenses_id");
        sb.append(", bank_id");
        sb.append(", application_date");
        sb.append(", applicant_employee_id");
        sb.append(", payment");
        sb.append(", payment_date");
        sb.append(", payment_employee_id");
        sb.append(", pay_state");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}
