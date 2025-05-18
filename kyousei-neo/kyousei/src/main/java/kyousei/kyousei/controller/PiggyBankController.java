package kyousei.kyousei.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.data.HistoryEntity;
import kyousei.kyousei.entity.data.IdEntity;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.entity.employee.EmployeeEntity;
import kyousei.kyousei.entity.expenses.CashExpensesDetailEntity;
import kyousei.kyousei.entity.expenses.CashExpensesEntity;
import kyousei.kyousei.interfaceis.IEntity;
import kyousei.kyousei.service.DownloadService;
import kyousei.kyousei.service.EmployeeService;
import kyousei.kyousei.service.ExpensesService;
import kyousei.kyousei.service.SqlService;

@Controller
// @RequiredArgsConstructor
public class PiggyBankController {
    // private final DownloadService downloadService;
    // private final EmployeeService employeeService;
    // private final ExpensesService expensesService;
    // private final SqlRepository sqlRepository;
    /**
     * 現金経費画面を呼び出す
     * 
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/cash_expenses")
    public ModelAndView showCashExpenses(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/piggy_bank/cash_expenses");
        // 各種設定コード
        mv.addObject("unpaidCode", Enums.cashState.UNPAID.getNum()); // 未払いコード
        mv.addObject("paidCode", Enums.cashState.PAID.getNum()); // 支払い済コード
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum()); // 削除コード
        mv.addObject("undecidedStateCode", Enums.state.UNDECIDED.getNum()); // 未定コード
        // リスト初期データ取得
        CashExpensesEntity listEntity = new CashExpensesEntity();
        SqlData sqldata = DownloadService.createSqlData(listEntity, listEntity.getSelectString(), null);
        // リスト再取得用エンティティ
        mv.addObject("sqldata", sqldata);
        // 新規・更新用データ
        CashExpensesDetailEntity formEntity = new CashExpensesDetailEntity();
        formEntity.setPayment_state(Enums.cashState.PAID.getNum());
        mv.addObject("formentity", formEntity);
        // 金庫リスト
        EmployeeEntity employee = (EmployeeEntity)EmployeeService.getEmployeeForAccount(username);
        List<IEntity> banklist = ExpensesService.getBankList(employee.getOffice_id());
        mv.addObject("banklist", banklist);
        // 勘定科目リスト
        mv.addObject("accountlist", ExpensesService.getAccountList());
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("cash_expenses");
        history.setState("閲覧");
        history.saveHistory();
        return mv;
    }
    /**
     * CASH_EXPENSES_IDから現金経費詳細データを取得
     * @return IEntity
     */
    @PostMapping("/cash_expenses_detail/get")
    @ResponseBody
    public List<IEntity> getCashExpensesDetailFromId(@RequestParam int id) {
        return ExpensesService.getCashExpensesDetailForId(id);
    }
    /**
     * 日払い情報を作成する
     * @param timeworksEntity
     * @return
     */
    @PostMapping("/cash_expenses_detail/save")
	@ResponseBody
    public int saveTimeworksPayment(@RequestBody List<CashExpensesDetailEntity> details, @AuthenticationPrincipal OidcUser principal) {
        if (details.size() == 0) return 0;
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        int totalPayment = 0;
        // 詳細
        CashExpensesDetailEntity ent = new CashExpensesDetailEntity();
        String detailStr = ent.getLogTable();
        for (CashExpensesDetailEntity detailEntity : details) {
            detailEntity.setUser_name(username);
            if (detailEntity.getState() != Enums.state.DELETE.getNum()) {
                totalPayment += detailEntity.getAmount();
            }
            if (detailEntity.getCash_expenses_detail_id() > 0) {
                detailStr += detailEntity.getUpdateString();
            } else {
                detailStr += detailEntity.getInsertString();
            }
        }
        
        CashExpensesEntity expensesEntity = new CashExpensesEntity();
        String expensesStr = "";
        LocalDate applicationDate = details.get(0).getApplication_date();
        expensesEntity.setApplication_date(applicationDate);
        int applicantEmployeeId = details.get(0).getApplicant_employee_id();
        expensesEntity.setApplicant_employee_id(applicantEmployeeId);
        expensesEntity.setPayment(totalPayment);
        LocalDate paymentDate = details.get(0).getPayment_date();
        expensesEntity.setPayment_date(paymentDate);
        int bank = details.get(0).getBank_id();
        expensesEntity.setBank_id(bank);
        int paymentEmployeeId = details.get(0).getPayment_employee_id();
        expensesEntity.setPayment_employee_id(paymentEmployeeId);
        int paymentState = details.get(0).getPayment_state();
        expensesEntity.setPay_state(paymentState);
        int expensesVersion = details.get(0).getExpenses_version();
        expensesEntity.setVersion(expensesVersion);
        expensesEntity.setUser_name(username);
        if (details.get(0).getCash_expenses_id() > 0) {
            expensesEntity.setCash_expenses_id(details.get(0).getCash_expenses_id());
            expensesStr = expensesEntity.getUpdateString();
        } else {
            expensesStr = expensesEntity.getInsertString();
            expensesStr += "DECLARE @ROW_COUNT int;";
        }
        // 変更履歴
        detailStr += "SET @ROW_COUNT = @@ROWCOUNT;";
        detailStr += "IF @ROW_COUNT > 0 BEGIN";
        detailStr += " INSERT INTO cash_expenses_detail_log SELECT * FROM @CashExpensesTable;";
        detailStr += " INSERT INTO history (user_name, table_name, state, contents)";
        detailStr += " VALUES ('" + username + "', 'cash_expenses_details', '作成・更新', '更新数=" + details.size() + "');";
        // detailStr += "SELECT @ROW_COUNT as number; END";
        detailStr += "END";
        detailStr += " ELSE BEGIN";
        detailStr += " INSERT INTO history (user_name, table_name, state, contents)";
        detailStr += " VALUES ('" + username + "', 'cash_expenses_details', '作成失敗', '');";
        // detailStr += " SELECT 0 as number;";
        detailStr += "END;";
        String sqlStr = expensesStr + detailStr + "SELECT @NEW_CASH_ID as number;";
        return SqlService.excuteSqlString(sqlStr);
    }
    /**
     * 現金経費情報を作成する
     * @param timeworksEntity
     * @return
     */
    @PostMapping("/cash_expenses/payment")
	@ResponseBody
    public int saveTimeworksPayment(@RequestBody CashExpensesEntity cashExpensesEntity, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        // String paymentStr = cashExpensesEntity.getLogTable();
        String paymentStr = "";
        cashExpensesEntity.setUser_name(username);
        if (cashExpensesEntity.getCash_expenses_id() > 0) {
            paymentStr += cashExpensesEntity.getUpdateString();
        } else {
            paymentStr += cashExpensesEntity.getInsertString();
        }
        // 変更履歴
        paymentStr += "INSERT INTO history (user_name, table_name, state, contents)";
        paymentStr += "VALUES ('" + principal.getAttribute("preferred_username") + "', 'cash_expenses', '作成・更新', ";
        if (cashExpensesEntity.getCash_expenses_id() > 0) {
            paymentStr += "'対象ID=" + cashExpensesEntity.getCash_expenses_id() + "');";
        } else {
            paymentStr += "'対象ID=@NEW_CASH_ID');";
        }
        paymentStr += "INSERT INTO cash_expenses_log SELECT * FROM @CashExpensesTable;";
        paymentStr += "SELECT @NEW_CASH_ID as number;";
        return SqlService.excuteSqlString(paymentStr);
    }
    /**
     * 現金経費情報を削除する
     * @param ids
     * @return
     */
    @PostMapping("/cash_expenses/delete")
	@ResponseBody
    public int deleteRecycle(@RequestBody List<IdEntity> ids, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        CashExpensesEntity cashExpensesEntity = new CashExpensesEntity();
        cashExpensesEntity.setUser_name(username);
        String sql = cashExpensesEntity.getLogTable();
        sql += "UPDATE cash_expenses SET state = " + Enums.state.DELETE.getNum() + ", update_date = '" + LocalDateTime.now() + "'" + cashExpensesEntity.getLogString("削除") + " WHERE cash_expenses_id IN";
        CashExpensesDetailEntity detailEntity = new CashExpensesDetailEntity();
        detailEntity.setUser_name(username);
        String detailStr = detailEntity.getLogTable();
        detailStr += "UPDATE cash_expenses_detail SET state = " + Enums.state.DELETE.getNum() + ", update_date = '" + LocalDateTime.now() + "'" + detailEntity.getLogString("削除") + " WHERE cash_expenses_id IN";
        String idStr = "(";
        
        for (IdEntity entity : ids) {
            idStr += entity.getId() + ", ";

        }
        idStr = idStr.substring(0, idStr.length()-2) + ")";
        sql += idStr + ";" + detailStr + idStr + ";";

        // 変更履歴
        sql += "DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;";
        sql += "INSERT INTO cash_expenses_log SELECT * FROM @CashExpensesTable;";
        sql += "INSERT INTO cash_expenses_detail_log SELECT * FROM @CashExpensesDetailTable;";
        sql += "INSERT INTO history (user_name, table_name, state, contents) VALUES (";
        sql += "'" + username + "', 'cash_expenses', '削除', 'ID=" + idStr + "');";
        // 更新が成功していれば更新数を返す、失敗していれば０を返す
        sql += "DECLARE @OLD_ID int;IF @ROW_COUNT > 0 SET @OLD_ID = @ROW_COUNT ELSE SET @OLD_ID = 0;";
        sql += "SELECT @OLD_ID as number;";
        return SqlService.excuteSqlString(sql);
    }
}
