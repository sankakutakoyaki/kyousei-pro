package kyousei.kyousei.controller;

import java.time.LocalDate;
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
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.entity.employee.EmployeeEntity;
import kyousei.kyousei.entity.expenses.CashExpensesDetailEntity;
import kyousei.kyousei.entity.expenses.CashExpensesEntity;
import kyousei.kyousei.entity.timeworks.TimeworksEntity;
import kyousei.kyousei.entity.timeworks.TimeworksListEntity;
import kyousei.kyousei.entity.timeworks.TimeworksPaymentEntity;
import kyousei.kyousei.entity.timeworks.TimeworksPaymentListEntity;
import kyousei.kyousei.interfaceis.IEntity;
import kyousei.kyousei.service.DownloadService;
import kyousei.kyousei.service.EmployeeService;
import kyousei.kyousei.service.ExpensesService;
import kyousei.kyousei.service.SqlService;
import kyousei.kyousei.service.TimeworksService;

@Controller
// @RequiredArgsConstructor
public class TimeworksController {
    // private final TimeworksService timeworksService;
    // private final EmployeeService employeeService;
    // private final DownloadService downloadService;
    // private final ExpensesService expensesService;
    // private final SqlRepository sqlRepository;

    /**
     * 打刻一覧画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
	@GetMapping("/timeworks")
	public ModelAndView showList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
		mv.setViewName("content/attendance/timeworks");
        // 勤怠データ新規・更新用
        TimeworksListEntity entity = new TimeworksListEntity();
        mv.addObject("entity", entity);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("timeworks");
        history.setState("閲覧");
        history.saveHistory();
	    return mv;
	}
    /**
     * 日払い画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/timeworks/dailypay")
    public ModelAndView showDailypayList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/attendance/dailypay");
        // リスト検索用エンティティ
        TimeworksPaymentListEntity entity = new TimeworksPaymentListEntity();
        SqlData sqlData = DownloadService.createSqlData(entity, entity.getSelectString(), null);
        mv.addObject("sqldata", sqlData);
        // 金庫リスト
        String account = principal.getAttribute("preferred_username");
        EmployeeEntity employee = (EmployeeEntity)EmployeeService.getEmployeeForAccount(account);
        List<IEntity> banklist = ExpensesService.getBankList(employee.getOffice_id());
        mv.addObject("banklist", banklist);

        mv.addObject("notStateCode", Enums.state.UNDECIDED.getNum()); // 未定
        mv.addObject("completeStateCode", Enums.state.COMPLETE.getNum()); // 完了
        mv.addObject("unpaidCode", Enums.cashState.UNPAID.getNum()); // 未払い
        mv.addObject("paidCode", Enums.cashState.PAID.getNum()); // 支払い済
        // 源泉リスト
        List<IEntity> taxlist = ExpensesService.getWithholdingTaxList();
        mv.addObject("taxlist", taxlist);

        return mv;
    }
    /**
     * 今日の全勤怠データリストを取得
     * @return
     */
    @GetMapping("/timeworks/get/today")
    @ResponseBody
    public List<IEntity> getTodaysAttendanceDataForAllEmployees() {
        return TimeworksService.getAttendanceDataForEveryoneOnTheDay();
    }
    /**
     * コードから今日の個人勤怠データを取得
     * @return IEntity
     */
    @PostMapping("/timeworks/get/today/id")
    @ResponseBody
    public IEntity getTodaysEmployeeAttendanceDataFromId(@RequestParam int id) {
        EmployeeEntity emp = (EmployeeEntity) EmployeeService.getEmployeeForId(id);
        return TimeworksService.getEmployeeAttendanceData(emp);
    }
    /**
     * コードから今日の個人勤怠データを取得
     * @return IEntity
     */
    @PostMapping("/timeworks/get/today/code")
    @ResponseBody
    public IEntity getTodaysEmployeeAttendanceDataFromCode(@RequestParam int code) {
        EmployeeEntity emp = (EmployeeEntity) EmployeeService.getEmployeeForCode(code);
        return TimeworksService.getEmployeeAttendanceData(emp);
    }
    /**
     * 勤怠情報を作成・更新する
     * @param timeworksEntity
     * @return
     */
    @PostMapping("/timeworks/regist/today")
	@ResponseBody
    public int saveTimeworksToday(@RequestBody TimeworksEntity timeworksEntity) {
        // LocalDate date = LocalDate.now();
        // String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS"));
        if (timeworksEntity.getTimeworks_id() == 0) {
            timeworksEntity.setPay_state(Enums.cashState.UNPAID.getNum());
            return SqlService.excuteSqlString(timeworksEntity.getInsertString());
        } else {
            return SqlService.excuteSqlString(timeworksEntity.getUpdateString());
        }
    }
    /**
     * 印刷情報を更新する
     * @param timeworksEntity
     * @return
     */
    @PostMapping("/timeworks/print")
	@ResponseBody
    public int saveTimeworksPrint(@RequestBody List<TimeworksPaymentEntity> timeworksPaymentEntity, @AuthenticationPrincipal OidcUser principal) {
        TimeworksEntity entity = new TimeworksEntity();
        String sqlStr = entity.getLogTable();
        for (TimeworksPaymentEntity payment : timeworksPaymentEntity) {
            entity.setTimeworks_id(payment.getTimeworks_id());
            entity.setUser_name(principal.getAttribute("preferred_username"));
            sqlStr += entity.getPrintedString();
        }
        sqlStr += "DECLARE @NEW_ID int; SET @NEW_ID = @@ROWCOUNT;";
        // 変更履歴
        sqlStr += "INSERT INTO history (user_name, table_name, state, contents)";
        sqlStr += "VALUES ('" + principal.getAttribute("preferred_username") + "', 'timeworks', '印刷', '更新数=" + timeworksPaymentEntity.size() + "');";
        sqlStr += "INSERT INTO timeworks_log SELECT * FROM @TimeworksTable;";
        // 新規IDを返す
        sqlStr += "SELECT @NEW_ID as number;";
        return SqlService.excuteSqlString(sqlStr);
    }
    /**
     * 日払い情報を作成する
     * @param timeworksEntity
     * @return
     */
    @PostMapping("/timeworks/payment/save")
	@ResponseBody
    public int saveTimeworksPayment(@RequestBody List<TimeworksPaymentEntity> paymentList, @AuthenticationPrincipal OidcUser principal) {
        TimeworksPaymentEntity paymentEntity = new TimeworksPaymentEntity();
        String idStr = "SELECT account_title_id as number FROM account_titles WHERE account_title_name = '雑給'";
        int accountTitleId = SqlService.getId(idStr);
        String paymentStr = paymentEntity.getLogTable();
        CashExpensesDetailEntity detailEntity = new CashExpensesDetailEntity();
        String detailStr = detailEntity.getLogTable();
        int total = 0;
        String idsStr = "";
        for (TimeworksPaymentEntity payment : paymentList) {
            paymentStr += payment.getInsertString();
            total += payment.getPayment();
            detailEntity = new CashExpensesDetailEntity();
            detailEntity.setUsedate(payment.getWork_date());
            detailEntity.setAccount_title_id(accountTitleId);
            detailEntity.setAccount_title_contents("日払い賃金");
            detailEntity.setAmount(payment.getPayment());
            detailEntity.setTax_rate(0);
            detailStr += detailEntity.getInsertString();
            idsStr += payment.getTimeworks_id() + ", ";
        }
        if (idsStr != "") {
            String ids = idsStr.substring(0, idsStr.length()-2);
            TimeworksEntity timeworksEntity = new TimeworksEntity();
            timeworksEntity.setUser_name(principal.getAttribute("preferred_username"));
            String payString = timeworksEntity.getLogTable();
            payString += "UPDATE timeworks SET pay_state = " + Enums.cashState.PAID.getNum() + timeworksEntity.getLogString("支払い");
            payString += " WHERE timeworks_id IN(" + ids + ");";
            paymentStr += payString;
        }
        
        paymentStr += "DECLARE @NEW_ID int; SET @NEW_ID = @@ROWCOUNT;";
        // 変更履歴
        paymentStr += "INSERT INTO history (user_name, table_name, state, contents)";
        paymentStr += "VALUES ('" + principal.getAttribute("preferred_username") + "', 'timeworks_payment', '作成', '更新数=" + paymentList.size() + "');";
        paymentStr += "INSERT INTO timeworks_payment_log SELECT * FROM @TimeworksPaymentTable;";
        // 変更履歴
        detailStr += "INSERT INTO history (user_name, table_name, state, contents)";
        detailStr += "VALUES ('" + principal.getAttribute("preferred_username") + "', 'cash_expenses_detail', '作成', '更新数=" + paymentList.size() + "');";
        detailStr += "INSERT INTO cash_expenses_detail_log SELECT * FROM @CashExpensesDetailTable;";

        CashExpensesEntity expensesEntity = new CashExpensesEntity();
        expensesEntity.setBank_id(paymentList.get(0).getBank_id());
        expensesEntity.setApplicant_employee_id(paymentList.get(0).getEmployee_id());
        expensesEntity.setApplication_date(LocalDate.now());
        expensesEntity.setPayment(total);
        expensesEntity.setPayment_date(LocalDate.now());
        expensesEntity.setPayment_employee_id(paymentList.get(0).getPayment_employee_id());
        expensesEntity.setPay_state(Enums.cashState.PAID.getNum());
        String expensesStr = expensesEntity.getInsertString();

        String sqlStr = paymentStr + expensesStr + detailStr + "SELECT @NEW_ID as number;";
        return SqlService.excuteSqlString(sqlStr);
    }
}
