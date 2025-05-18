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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.data.HistoryEntity;
import kyousei.kyousei.entity.data.IdEntity;
import kyousei.kyousei.entity.data.SimpleData;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.entity.recycle.RecycleCsv;
import kyousei.kyousei.entity.recycle.RecycleDeliveryEntity;
import kyousei.kyousei.entity.recycle.RecycleEntity;
import kyousei.kyousei.entity.recycle.RecycleForwardEntity;
import kyousei.kyousei.entity.recycle.RecycleListEntity;
import kyousei.kyousei.entity.recycle.RecycleLossEntity;
import kyousei.kyousei.interfaceis.IEntity;
import kyousei.kyousei.service.ComboboxService;
import kyousei.kyousei.service.CompanyService;
import kyousei.kyousei.service.DownloadService;
import kyousei.kyousei.service.RecycleService;
import kyousei.kyousei.service.SqlService;

@Controller
// @RequiredArgsConstructor
public class RecycleController {

    // private final SqlRepository sqlRepository;
    // private final ComboboxService comboboxService;
    // private final DownloadService downloadService;
    // private final CompanyService companyService;
    // private final RecycleService recycleService;

    /**
     * リサイクル一覧画面を呼び出す
     * @param mv
     * @param token
     * @param principal
     * @return
     */
    @GetMapping("/recycle/list")
    public ModelAndView showRecycleList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/recycle/list");
        // コンボボックス用エンティティ
        List<IEntity> companycombolist = ComboboxService.getRetailerCompaniesWithOwn();
        mv.addObject("companycombolist", companycombolist);
        List<IEntity> officecombolist = ComboboxService.getOfficesWithCompanyId();
        mv.addObject("officecombolist", officecombolist);
        // 保存用エンティティ
        RecycleListEntity entity = new RecycleListEntity();
        // リスト初期データ取得
        String selectString = entity.getSelectString() + " AND CONVERT(DATE, r.regist_date) = '" + LocalDate.now() + "'";
        List<IEntity> origin = SqlService.getEntityList(DownloadService.createSqlData(new RecycleListEntity(), selectString, null));
        mv.addObject("origin", origin);
        // リスト再取得用・CSVダウンロード用エンティティ
        SqlData sqldata = DownloadService.createSqlData(new RecycleListEntity(), entity.getSelectString(), new RecycleCsv());
        mv.addObject("sqldata", sqldata);
        // コードリスト
        List<IEntity> makerlist = RecycleService.getAllRecycleMakerList();
        mv.addObject("makerlist", makerlist);
        List<IEntity> classlist = RecycleService.getAllRecycleClassList();
        mv.addObject("classlist", classlist);
        List<IEntity> pricelist = RecycleService.getAllRecyclePriceList();
        mv.addObject("pricelist", pricelist);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("recycle_list");
        history.setState("閲覧");
        history.saveHistory();

        return mv;
    }    /**
    * リサイクル使用登録画面を呼び出す
    * @param mv
    * @param token
    * @param principal
    * @return
    */
    @GetMapping("/recycle/regist")
    public ModelAndView showRecycleRegistList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/recycle/regist");
        // SQL実行用エンティティ
        mv.addObject("simpledata", new SimpleData());
        // サイドバー初期表示用会社ID
        int defaultCompanyId = CompanyService.getDefaultCompanyId();
        mv.addObject("defaultCompanyId", defaultCompanyId);
        // コンボボックス用エンティティ
        List<IEntity> companycombolist = ComboboxService.getRetailerCompaniesWithOwn();
        mv.addObject("companycombolist", companycombolist);
        List<IEntity> officecombolist = ComboboxService.getOfficesWithCompanyId();
        mv.addObject("officecombolist", officecombolist);
        // 保存用エンティティ
        RecycleEntity entity = new RecycleEntity();
        mv.addObject("entity", entity); 
        // リスト初期データ取得
        String selectString = entity.getSelectString() + " AND r.use_date = '" + LocalDate.now() + "'";
        List<IEntity> origin = SqlService.getEntityList(DownloadService.createSqlData(new RecycleEntity(), selectString, null));
        mv.addObject("origin", origin);
        // リスト再取得用エンティティ
        SqlData sqldata = DownloadService.createSqlData(new RecycleEntity(), entity.getSelectString(), null);
        mv.addObject("sqldata", sqldata);
        // コードリスト
        List<IEntity> makerlist = RecycleService.getAllRecycleMakerList();
        mv.addObject("makerlist", makerlist);
        List<IEntity> pricelist = RecycleService.getAllRecyclePriceList();
        mv.addObject("pricelist", pricelist);
        List<IEntity> classlist = RecycleService.getAllRecycleClassList();
        mv.addObject("classlist", classlist);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("recycle_regist");
        history.setState("閲覧");
        history.saveHistory();

        return mv;
    }
    /**
     * リサイクル引渡登録画面を呼び出す
     * @param mv
     * @param token
     * @param principal
     * @return
     */
    @GetMapping("/recycle/delivery")
    public ModelAndView showRecycleDeliveryList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/recycle/delivery");
        // リスト初期データ取得
        RecycleDeliveryEntity entity = new RecycleDeliveryEntity();
        String selectString = entity.getSelectString() + " AND rd.delivery_date = '" + LocalDate.now() + "'";
        List<IEntity> origin = SqlService.getEntityList(DownloadService.createSqlData(new RecycleDeliveryEntity(), selectString, null));
        mv.addObject("origin", origin);
        // リスト再取得用エンティティ
        SqlData sqldata = DownloadService.createSqlData(new RecycleDeliveryEntity(), entity.getSelectString(), null);
        mv.addObject("sqldata", sqldata);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("recycle_delivery");
        history.setState("閲覧");
        history.saveHistory();

        return mv;
    }
    /**
     * リサイクル発送登録画面を呼び出す
     * @param mv
     * @param token
     * @param principal
     * @return
     */
    @GetMapping("/recycle/forward")
    public ModelAndView showRecycleForwardList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/recycle/forward");
        // リスト初期データ取得
        RecycleForwardEntity entity = new RecycleForwardEntity();
        String selectString = entity.getSelectString() + " AND rf.forward_date = '" + LocalDate.now() + "'";
        List<IEntity> origin = SqlService.getEntityList(DownloadService.createSqlData(new RecycleForwardEntity(), selectString, null));
        mv.addObject("origin", origin);
        // リスト再取得用エンティティ
        SqlData sqldata = DownloadService.createSqlData(new RecycleForwardEntity(), entity.getSelectString(), null);
        mv.addObject("sqldata", sqldata);
        // コンボボックス用エンティティ
        List<IEntity> companycombolist = ComboboxService.getRetailerCompanies();
        mv.addObject("companycombolist", companycombolist);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("recycle_forward");
        history.setState("閲覧");
        history.saveHistory();

        return mv;
    }
    /**
     * リサイクル発送登録画面を呼び出す
     * @param mv
     * @param token
     * @param principal
     * @return
     */
    @GetMapping("/recycle/loss")
    public ModelAndView showRecycleLossList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/recycle/loss");
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("recycle_loss");
        history.setState("閲覧");
        history.saveHistory();

        return mv;
    }
    // /**
    //  * リサイクル入荷画面を呼び出す
    //  * 
    //  * @param mv
    //  * @param token
    //  * @return ModelAndView
    //  */
    // @GetMapping("/recycle/arrival")
    // public ModelAndView showRecycleArrivalList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
    //     // ユーザー名
    //     String username = principal.getAttribute("preferred_username");
    //     mv.addObject("username", username);
    //     // MVを設定
    //     mv.setViewName("content/tablelist/recycle/arrival");
    //     // リスト初期データ取得
    //     RecycleArrivalEntity entity = new RecycleArrivalEntity();
    //     String selectString = entity.getSelectString() + " AND ra.arrival_date = '" + LocalDate.now() + "'";
    //     List<IEntity> origin = sqlRepository.getEntityList(downloadService.createSqlData(new RecycleArrivalEntity(), selectString, null));
    //     mv.addObject("origin", origin);
    //     // リスト再取得用エンティティ
    //     SqlData sqldata = downloadService.createSqlData(new RecycleArrivalEntity(), entity.getSelectString(), null);
    //     mv.addObject("sqldata", sqldata);
    //     // 履歴登録
    //     HistoryEntity history = new HistoryEntity();
    //     history.setUser_name(username);
    //     history.setTable_name("recycle_arrival");
    //     history.setState("閲覧");
    //     history.saveHistory();

    //     return mv;
    // }
    /**
     * リサイクル券使用情報を作成・更新する
     * @param recycleEntity
     * @return
     */
    @PostMapping("/create/recycle/regist")
	@ResponseBody
    public int saveRecycle(@RequestBody RecycleEntity recycleEntity) {
        return SqlService.excuteSqlString(recycleEntity.getInsertString());
    }
    /**
     * リサイクル券引渡情報を作成・更新する
     * @param recycleEntity
     * @return
     */
    @PostMapping("/create/recycle/delivery")
	@ResponseBody
    public int saveRecycleDelivery(@RequestBody RecycleDeliveryEntity recycleDeliveryEntity) {
        return SqlService.excuteSqlString(recycleDeliveryEntity.getInsertString());
    }
    /**
     * リサイクル券発送情報を作成・更新する
     * @param recycleEntity
     * @return
     */
    @PostMapping("/create/recycle/forward")
	@ResponseBody
    public int saveRecycleForward(@RequestBody RecycleForwardEntity recycleForwardEntity) {
        return SqlService.excuteSqlString(recycleForwardEntity.getInsertString());
    }
    /**
     * リサイクル券ロス処理情報を作成・更新する
     * @param recycleEntity
     * @return
     */
    @PostMapping("/create/recycle/loss")
	@ResponseBody
    public int saveRecycleLoss(@RequestBody RecycleLossEntity recycleLossEntity) {
        return SqlService.excuteSqlString(recycleLossEntity.getInsertString());
    }
    /**
     * リサイクル券情報を修正する
     * @param ids
     * @return
     */
    @PostMapping("/update/recycle")
	@ResponseBody
    public int updateRecycle(@RequestBody RecycleListEntity recycleEntity) {
        String sql = "";
        String str = "";
        if (!recycleEntity.getUse_date().toString().equals("9999-12-31")) str += "use_date = '" + recycleEntity.getUse_date() + "', ";
        // if (!recycleEntity.getDelivery_date().toString().equals("9999-12-31")) str += "delivery_date = '" + recycleEntity.getDelivery_date() + "', ";
        if (recycleEntity.getUse_company_id() > -1) str += "use_company_id = " + recycleEntity.getUse_company_id() + ", ";
        if (recycleEntity.getUse_office_id() > -1) str += "use_office_id = " + recycleEntity.getUse_office_id() + ", ";
        if (recycleEntity.getClass_code() > -1) str += "class_code = " + recycleEntity.getClass_code() + ", ";
        if (recycleEntity.getMaker_code() > -1) str += "maker_code = " + recycleEntity.getMaker_code() + ", ";
        if (recycleEntity.getPrice() > -1) str += "price = " + recycleEntity.getPrice() + ", ";
        if (recycleEntity.getEx_tax() > -1) str += "ex_tax = " + recycleEntity.getEx_tax() + ", ";

        if (!str.isEmpty()) {
            sql += recycleEntity.getLogTable();
            sql += "UPDATE recycle SET ";
            sql += str.substring(0, str.length()-2) + recycleEntity.getLogString("修正") + " WHERE recycle_id " + recycleEntity.getIds() + ";";
            // 変更履歴
            sql += "INSERT INTO recycle_log SELECT * FROM @RecycleTable;";
            sql += "INSERT INTO history (user_name, table_name, state, contents) VALUES (";
            sql += "'" + recycleEntity.getUser_name() + "', 'recycle', '修正', 'ID=" + recycleEntity.getIds().substring(0, 2) + "');";
        }

        String sql_d = "";
        if (!recycleEntity.getDelivery_date().toString().equals("9999-12-31")) {
            RecycleDeliveryEntity deliveryEntity = new RecycleDeliveryEntity();
            sql_d += deliveryEntity.getLogTable();
            sql_d += "UPDATE recycle_delivery SET delivery_date = '" + recycleEntity.getDelivery_date() + "'";
            sql_d += deliveryEntity.getLogString("修正");
            sql_d += " WHERE recycle_id " + recycleEntity.getIds() + " AND state = " + Enums.state.UNDECIDED.getNum() + ";";
            sql_d += "INSERT INTO recycle_delivery_log SELECT * FROM @RecycleDeliveryTable;";
        }
        if (sql.isEmpty() && sql_d.isEmpty()) return -1;
        sql = sql + sql_d;
        // 更新が成功していれば更新数を返す、失敗していれば０を返す
        sql += "DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;";
        sql += "DECLARE @OLD_ID int;IF @ROW_COUNT > 0 SET @OLD_ID = @ROW_COUNT ELSE SET @OLD_ID = 0;";
        sql += "SELECT @OLD_ID as number;";
        return SqlService.excuteSqlString(sql);
    }
    /**
     * リサイクル券情報を削除する
     * @param ids
     * @return
     */
    @PostMapping("/delete/recycle")
	@ResponseBody
    public int deleteRecycle(@RequestBody List<IdEntity> ids, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        RecycleEntity recycleEntity = new RecycleEntity();
        recycleEntity.setUser_name(username);
        String sql = recycleEntity.getLogTable();
        sql += "UPDATE recycle SET state = " + Enums.state.DELETE.getNum() + ", update_date = '" + LocalDateTime.now() + "'" + recycleEntity.getLogString("削除") + " WHERE recycle_id IN";
        String idStr = "(";
        for (IdEntity entity : ids) {
            idStr += entity.getId() + ", ";
        }
        idStr = idStr.substring(0, idStr.length()-2) + ")";
        sql += idStr + ";";

        // 変更履歴
        sql += "DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;";
        sql += "INSERT INTO recycle_log SELECT * FROM @RecycleTable;";
        sql += "INSERT INTO history (user_name, table_name, state, contents) VALUES (";
        sql += "'" + username + "', 'recycle', '削除', 'ID=" + idStr + "');";
        // 更新が成功していれば更新数を返す、失敗していれば０を返す
        sql += "DECLARE @OLD_ID int;IF @ROW_COUNT > 0 SET @OLD_ID = @ROW_COUNT ELSE SET @OLD_ID = 0;";
        sql += "SELECT @OLD_ID as number;";
        return SqlService.excuteSqlString(sql);
    }
    // /**
    //  * リサイクル券入荷情報を作成・更新する
    //  * @param recycleEntity
    //  * @return
    //  */
    // @PostMapping("/create/recycle/arrival")
	// @ResponseBody
    // public int saveRecycleArrival(@RequestBody RecycleArrivalEntity recycleArrivalEntity) {
    //     if (recycleArrivalEntity.getRecycle_arrival_id() == 0) {
    //         return sqlRepository.excuteSqlString(recycleArrivalEntity.getInsertString());
    //     } else {
    //         return sqlRepository.excuteSqlString(recycleArrivalEntity.getUpdateString());
    //     }
    // }
}
