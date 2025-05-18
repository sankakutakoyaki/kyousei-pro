package com.kyouseipro.kyousei.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.data.SimpleData;
import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.entity.expenses.CashExpensesEntity;
import com.kyouseipro.kyousei.entity.expenses.WithholdingTaxEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.ISql;

@Repository
public class ExpensesRepository implements ISql {

    /***
     * 清算状況を済みにし、現金経費データを新規作成する
     * 
     * @param query
     * @return 成功すれば[TRUE]を返す
     */
    public boolean saveDailyCashExpenses(CashExpensesEntity query) {
        LocalDate date = LocalDate.now();
        StringBuilder sb = new StringBuilder();

        // 清算状況を[済]にアップデート
        sb.append(query.getSqlPayStr());

        // cash_expenses 新規作成
        sb.append("INSERT INTO cash_expenses (");
        sb.append("application_date");
        sb.append(", applicant_employee_id");
        sb.append(", payment");
        sb.append(", payment_date");
        sb.append(", payment_employee_id");
        sb.append(", state");
        sb.append(") VALUES (");
        sb.append(" '" + date + "'");
        sb.append(", " + query.getApplicant_employee_id());
        sb.append(", " + query.getPayment());
        sb.append(", '" + date + "'");
        sb.append(", " + query.getPayment_employee_id());
        sb.append(", " + Enums.situation.DONE.getNum());
        sb.append(");");
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");

        // cash_expenses_detail 新規作成
        sb.append(query.getSqlExpStr());

        return saveEntity(sb.toString());
    }

    /**
     * 源泉徴収金額リストを取得
     * @return
     */
    public List<IEntity> getWithholdingTaxList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM withholding_tax");
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new WithholdingTaxEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 金庫リストを取得
     * @param id　営業所ID
     * @return
     */
    public List<IEntity> getBankList(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT code, bank_id as number, title as text, state FROM bank WHERE office_id = " + id);
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new SimpleData());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }
}
