package kyousei.kyousei.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.data.SimpleData;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.entity.expenses.CashExpensesDetailEntity;
import kyousei.kyousei.entity.expenses.WithholdingTaxEntity;
import kyousei.kyousei.interfaceis.IEntity;

@Service
// @RequiredArgsConstructor
public class ExpensesService {
    // private final DownloadService downloadService;
    // private final CompanyService companyService;
    // private final SqlRepository sqlRepository;

    /**
     * 源泉徴収金額リストを取得
     * @return
     */
    public static List<IEntity> getWithholdingTaxList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM withholding_tax");
        SqlData sqlData = DownloadService.createSqlData(new WithholdingTaxEntity(), sb.toString(), null);
        return  SqlService.getEntityList(sqlData);
    }
    /**
     * 金庫リストを取得
     * @param id　営業所ID
     * @return
     */
    public static List<IEntity> getBankList(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT code, bank_id as number, title as text, state FROM bank");
        int defaultId = CompanyService.getDefaultOfficeId();
        if (id != defaultId) sb.append(" WHERE office_id = " + id);
        SqlData sqlData = DownloadService.createSqlData(new SimpleData(), sb.toString(), null);
        return  SqlService.getEntityList(sqlData);
    }
    /**
     * 勘定科目リストを取得
     * @return
     */
    public static List<IEntity> getAccountList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT code, account_title_id as number, account_title_name as text, state FROM account_titles");
        sb.append(" WHERE state = " + Enums.state.UNDECIDED.getNum() + " ORDER BY code");
        SqlData sqlData = DownloadService.createSqlData(new SimpleData(), sb.toString(), null);
        return  SqlService.getEntityList(sqlData);
    }
    /**
     * CASH_EXPENSES_IDから[cash_expenses_destail]リストを取得
     * @param id
     * @return List<cash_expenses_detail>
     */
    public static List<IEntity> getCashExpensesDetailForId(int id) {
        StringBuilder sb = new StringBuilder();
        CashExpensesDetailEntity entity = new CashExpensesDetailEntity();
        sb.append(entity.getSelectString());
        sb.append(" AND ced.cash_expenses_id = " + id);
        SqlData sqlData = new SqlData();
        sqlData = DownloadService.createSqlData(new CashExpensesDetailEntity(), sb.toString(), null);

        return SqlService.getEntityList(sqlData);
    }
}
