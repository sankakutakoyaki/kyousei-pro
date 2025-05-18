package com.kyouseipro.kyousei.entity.expenses;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class CashExpensesDetailEntity implements IFormEntity {
    
    private int cash_expenses_detail_id;
    private int cash_expenses_id;
    private LocalDate usedate;
    private int amount;
    private int version;
    private int state;
    
    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.cash_expenses_detail_id = rs.getInt("cash_expenses_detail_id");
            this.cash_expenses_id = rs.getInt("cash_expenses_id");
            this.usedate = rs.getDate("usedate").toLocalDate();
            this.amount = rs.getInt("amout");
            this.version = rs.getInt("version");
            this.state = rs.getInt("state");
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM cash_expenses_detail ced");
        sb.append(" LEFT OUTER JOIN cash_expense ce ON ce.cash_expenses_id = ced.cash_expenses_id AND ce.state = " + Enums.state.INITIAL.getNum());
        sb.append(" LEFT OUTER JOIN item_category ic ON ic.item_category_id = ced.item_category_id AND ic.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE ced.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    @Override
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO cash_expenses_detail (");
        sb.append("cash_expenses_id");
        sb.append(", usedate");
        sb.append(", amount");
        sb.append(") VALUES (");
        sb.append(this.getCash_expenses_id());
        sb.append(", '" + this.getUsedate() + "'");
        sb.append(", " + this.getAmount());
        sb.append(");");
        return sb.toString();
    }

    @Override
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE cash_expenses_detail SET");
        sb.append(" cash_expenses_id = " + this.getCash_expenses_id());
        sb.append(", usedate = '" + this.getUsedate() + "'");
        sb.append(", amount = " + this.getAmount());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE cash_expenses_detail_id = " + this.getCash_expenses_detail_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }

    @Override
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ced SET ced.state = " + Enums.state.DELETE.getNum() + " FROM cash_expenses_detail ced WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("支払い金額,");
        sb.append("\n");
        for (IEntity item : items) {
            CashExpensesDetailEntity entity = (CashExpensesDetailEntity) item;
            sb.append(entity.getAmount() + ",");
            sb.append("\n"); // 改行を追加
        }
        return sb.toString();
    }
    
}
