package com.kyouseipro.kyousei.entity.expenses;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class CashExpensesEntity implements IFormEntity {

    private int cash_expenses_id;
    private int bank_id;
    private LocalDate application_date;
    private int applicant_employee_id;
    private int payment;
    private LocalDate payment_date;
    private int payment_employee_id;
    private String sqlPayStr;
    private String sqlExpStr;
    private int version;
    private int state;
    
    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.cash_expenses_id = rs.getInt("cash_expenses_id");
            this.bank_id = rs.getInt("bank_id");
            this.application_date = rs.getDate("application_date").toLocalDate();
            this.applicant_employee_id = rs.getInt("applicant_employee_id");
            this.payment = rs.getInt("payment");
            this.payment_date = rs.getDate("payment_date").toLocalDate();
            this.payment_employee_id = rs.getInt("payment_employee_id");
            this.version = rs.getInt("version");
            this.state = rs.getInt("state");
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM cash_expenses ce");
        sb.append(" LEFT OUTER JOIN employees app_e ON e.employee_id = ce.applicant_employee_id AND e.state = " + Enums.state.INITIAL.getNum());
        sb.append(" LEFT OUTER JOIN employees pay_e ON e.employee_id = ce.payment_employee_id AND e.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE ce.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    @Override
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO cash_expenses (");
        sb.append("bank_id");
        sb.append(", application_date");
        sb.append(", applicant_employee_id");
        sb.append(", payment");
        sb.append(", payment_date");
        sb.append(", payment_employee_id");
        sb.append(") VALUES (");
        sb.append(this.getBank_id());
        sb.append(", '" + this.getApplication_date() + "'");
        sb.append(", " + this.getApplicant_employee_id());
        sb.append(", " + this.getPayment());
        sb.append(", '" + this.getPayment_date() + "'");
        sb.append(", " + this.getPayment_employee_id());
        sb.append(");");
        return sb.toString();
    }

    @Override
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE cash_expenses SET");
        sb.append(" bank_id = " + this.getBank_id());
        sb.append(", application_date = '" + this.getApplication_date() + "'");
        sb.append(", applicant_employee_id = " + this.getApplicant_employee_id());
        sb.append(", payment = " + this.getPayment());
        sb.append(", payment_date = '" + this.getPayment_date() + "'");
        sb.append(", payment_employee_id = " + this.getPayment_employee_id());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE cash_expenses_id = " + this.getCash_expenses_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }

    @Override
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ce SET ce.state = " + Enums.state.DELETE.getNum() + " FROM cash_expenses ce WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("請求金額,");
        sb.append("\n");
        for (IEntity item : items) {
            CashExpensesEntity entity = (CashExpensesEntity) item;
            sb.append(entity.getPayment() + ",");
            sb.append("\n"); // 改行を追加
        }
        return sb.toString();
    }
    
}
