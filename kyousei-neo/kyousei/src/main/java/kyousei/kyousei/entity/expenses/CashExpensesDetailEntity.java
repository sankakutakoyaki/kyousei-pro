package kyousei.kyousei.entity.expenses;

import java.sql.ResultSet;
import java.time.LocalDate;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class CashExpensesDetailEntity implements IEntity {
    
    private int cash_expenses_detail_id;
    private int cash_expenses_id;
    private LocalDate usedate;
    private int supplier_id;
    private String supplier_name;
    private int account_title_id;
    private String account_title_name;
    private String account_title_contents;
    private int amount;
    private int tax_rate;
    private int version;
    private int state;
    private String user_name;
    private int code;
    private LocalDate application_date; // 申請日
    private int applicant_employee_id; // 申請者
    private int applicant_employee_code;
    private String applicant_employee_name;
    private LocalDate payment_date; // 支払い日
    private int payment_state; // 支払い状況
    private int expenses_version;
    private int bank_id;
    private int payment_employee_id;
    
    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.cash_expenses_detail_id = rs.getInt("cash_expenses_detail_id");
            this.cash_expenses_id = rs.getInt("cash_expenses_id");
            this.usedate = rs.getDate("usedate").toLocalDate();
            this.supplier_id = rs.getInt("supplier_id");
            this.supplier_name = rs.getString("supplier_name");
            this.account_title_id = rs.getInt("account_title_id");
            this.account_title_name = rs.getString("account_title_name");
            this.account_title_contents = rs.getString("account_title_contents");
            this.amount = rs.getInt("amount");
            this.tax_rate = rs.getInt("tax_rate");
            this.version = rs.getInt("version");
            this.state = rs.getInt("state");
            this.application_date = rs.getDate("application_date").toLocalDate();
            this.applicant_employee_id = rs.getInt("applicant_employee_id");
            this.applicant_employee_code = rs.getInt("applicant_employee_code");
            this.applicant_employee_name = rs.getString("applicant_employee_name");
            this.expenses_version = rs.getInt("expenses_version");
            this.payment_state = rs.getInt("payment_state");
            this.bank_id = rs.getInt("bank_id");
            this.payment_employee_id = rs.getInt("payment_employee_id");
            this.payment_date = rs.getDate("payment_date").toLocalDate();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ced.*, at.account_title_name, ce.application_date, ce.applicant_employee_id, e.code as applicant_employee_code, p.full_name as applicant_employee_name, c.company_name as supplier_name");
        sb.append(", ce.version as expenses_version, ce.pay_state as payment_state, ce.payment_date, ce.payment_employee_id, ce.bank_id FROM cash_expenses_detail ced");
        sb.append(" LEFT OUTER JOIN cash_expenses ce ON ce.cash_expenses_id = ced.cash_expenses_id AND ce.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN account_titles at ON at.account_title_id = ced.account_title_id AND at.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = ced.supplier_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN employees e ON e.employee_id = ce.applicant_employee_id AND e.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" WHERE ced.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        // sb.append(logTable());
        sb.append("INSERT INTO cash_expenses_detail (");
        sb.append("cash_expenses_id");
        sb.append(", usedate");
        sb.append(", supplier_id");
        sb.append(", account_title_id");
        sb.append(", account_title_contents");
        sb.append(", amount");
        sb.append(", tax_rate");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append("@NEW_CASH_ID");
        sb.append(", '" + this.getUsedate() + "'");
        sb.append(", " + this.getSupplier_id());
        sb.append(", " + this.getAccount_title_id());
        sb.append(", '" + this.getAccount_title_contents() + "'");
        sb.append(", " + this.getAmount());
        sb.append(", " + this.getTax_rate());
        sb.append(");");
        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        // sb.append(logTable());
        sb.append("UPDATE cash_expenses_detail SET");
        // sb.append(" cash_expenses_detail_id = " + this.getCash_expenses_detail_id());
        sb.append(" cash_expenses_id = " + this.getCash_expenses_id());
        sb.append(", account_title_id = " + this.getAccount_title_id());
        sb.append(", account_title_contents = '" + this.getAccount_title_contents() + "'");
        sb.append(", usedate = '" + this.getUsedate() + "'");
        sb.append(", supplier_id = " + this.getSupplier_id());
        sb.append(", amount = " + this.getAmount());
        sb.append(", tax_rate = " + this.getTax_rate());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(", state = " + this.getState());
        sb.append(logString("更新"));
        sb.append(" WHERE cash_expenses_detail_id = " + this.getCash_expenses_detail_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }
    public String getLogTable() {
        return logTable();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @CashExpensesDetailTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", cash_expenses_detail_id INT");
        sb.append(", cash_expenses_id INT");
        sb.append(", usedate DATE");
        sb.append(", supplier_id INT");
        sb.append(", account_title_id INT");
        sb.append(", account_title_contents NVARCHAR(255)");
        sb.append(", amount INT");
        sb.append(", tax_rate INT");
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
        sb.append(", INSERTED.cash_expenses_detail_id");
        sb.append(", INSERTED.cash_expenses_id");
        sb.append(", INSERTED.usedate");
        sb.append(", INSERTED.supplier_id");
        sb.append(", INSERTED.account_title_id");
        sb.append(", INSERTED.account_title_contents");
        sb.append(", INSERTED.amount");
        sb.append(", INSERTED.tax_rate");
        sb.append(", INSERTED.state");
        sb.append(" INTO @CashExpensesDetailTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", cash_expenses_detail_id");
        sb.append(", cash_expenses_id");
        sb.append(", usedate");
        sb.append(", supplier_id");
        sb.append(", account_title_id");
        sb.append(", account_title_contents");
        sb.append(", amount");
        sb.append(", tax_rate");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}
