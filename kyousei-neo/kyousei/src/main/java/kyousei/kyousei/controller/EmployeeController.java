package kyousei.kyousei.controller;

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
import kyousei.kyousei.entity.corporation.CompanyEntity;
import kyousei.kyousei.entity.corporation.OfficeEntity;
import kyousei.kyousei.entity.data.HistoryEntity;
import kyousei.kyousei.entity.data.SimpleData;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.entity.employee.FulltimeCsv;
import kyousei.kyousei.entity.employee.FulltimeEntity;
import kyousei.kyousei.entity.employee.FulltimeListEntity;
import kyousei.kyousei.entity.employee.PartnerCsv;
import kyousei.kyousei.entity.employee.PartnerEntity;
import kyousei.kyousei.entity.employee.PartnerListEntity;
import kyousei.kyousei.entity.employee.ParttimeCsv;
import kyousei.kyousei.entity.employee.ParttimeEntity;
import kyousei.kyousei.entity.employee.ParttimeListEntity;
import kyousei.kyousei.entity.employee.StaffCsv;
import kyousei.kyousei.entity.employee.StaffEntity;
import kyousei.kyousei.entity.employee.StaffListEntity;
import kyousei.kyousei.interfaceis.IEntity;
import kyousei.kyousei.service.ComboboxService;
import kyousei.kyousei.service.CompanyService;
import kyousei.kyousei.service.DownloadService;
import kyousei.kyousei.service.EmployeeService;
import kyousei.kyousei.service.SidebarService;
import kyousei.kyousei.service.SqlService;

@Controller
// @RequiredArgsConstructor
public class EmployeeController {
    // private final SqlRepository sqlRepository;
    // private final ComboboxService comboboxService;
    // private final SidebarService sidebarService;
    // private final DownloadService downloadService;
    // private final CompanyService companyService;
    // private final EmployeeService employeeService;
    
    /**
     * 社員登録画面を呼び出す
     * 
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/regist/fulltime")
    public ModelAndView showFulltimeList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/regist/fulltime");
        // SQL実行用エンティティ
        mv.addObject("simpledata", new SimpleData());
        // 削除用コード
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        // リスト初期データ取得
        FulltimeListEntity listEntity = new FulltimeListEntity();
        SqlData sqllistdata = DownloadService.createSqlData(listEntity, listEntity.getFulltimeListString(), null);
        List<IEntity> origin = SqlService.getEntityList(sqllistdata);
        mv.addObject("origin", origin);
        // リスト再取得用エンティティ
        mv.addObject("sqllistdata", sqllistdata);
        // 新規・更新用データ
        FulltimeEntity formEntity = new FulltimeEntity();
        SqlData sqlformdata = DownloadService.createSqlData(formEntity, formEntity.getSelectString(), new FulltimeCsv());
        mv.addObject("sqlformdata", sqlformdata);
        mv.addObject("formentity", formEntity);
        // サイドバーリストデータ取得
        List<IEntity> sidebarlist = SidebarService.getOwnerOffices();
        mv.addObject("sidebarlist", sidebarlist);
        // // CSVダウンロード用エンティティ
        // FulltimeEntity fulltime = new FulltimeEntity();
        // SqlData downloaddata = downloadService.createSqlData(fulltime, fulltime.getSelectString(), new FulltimeCsv());
        // mv.addObject("downloaddata", downloaddata);
        // コンボボックス用エンティティ
        List<IEntity> officecombolist = ComboboxService.getOwnerOffices();
        mv.addObject("officecombolist", officecombolist);
        List<IEntity> gendercombolist = ComboboxService.getGender();
        mv.addObject("gendercombolist", gendercombolist);
        List<IEntity> bloodtypecombolist = ComboboxService.getBloodType();
        mv.addObject("bloodtypecombolist", bloodtypecombolist);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("fulltimes");
        history.setState("閲覧");
        // mv.addObject("history", history);
        history.saveHistory();
        return mv;
    }
    /**
     * アルバイト登録画面を呼び出す
     * 
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/regist/parttime")
    public ModelAndView showParttimeList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/regist/parttime");
        // SQL実行用エンティティ
        mv.addObject("simpledata", new SimpleData());
        // 削除用コード
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        // リスト初期データ取得
        ParttimeListEntity listEntity = new ParttimeListEntity();
        SqlData sqllistdata = DownloadService.createSqlData(listEntity, listEntity.getParttimeListString(), null);
        List<IEntity> origin = SqlService.getEntityList(sqllistdata);
        mv.addObject("origin", origin);
        // リスト再取得用エンティティ
        mv.addObject("sqllistdata", sqllistdata);
        // 新規・更新用データ
        ParttimeEntity formEntity = new ParttimeEntity();
        SqlData sqlformdata = DownloadService.createSqlData(formEntity, formEntity.getSelectString(), new ParttimeCsv());
        mv.addObject("sqlformdata", sqlformdata);
        mv.addObject("formentity", formEntity);
        // サイドバーリストデータ取得
        List<IEntity> sidebarlist = SidebarService.getOwnerOffices();
        mv.addObject("sidebarlist", sidebarlist);
        // // CSVダウンロード用エンティティ
        // ParttimeEntity fulltime = new ParttimeEntity();
        // SqlData downloaddata = downloadService.createSqlData(fulltime, fulltime.getSelectString(), new ParttimeCsv());
        // mv.addObject("downloaddata", downloaddata);
        // コンボボックス用エンティティ
        List<IEntity> officecombolist = ComboboxService.getOwnerOffices();
        mv.addObject("officecombolist", officecombolist);
        List<IEntity> gendercombolist = ComboboxService.getGender();
        mv.addObject("gendercombolist", gendercombolist);
        List<IEntity> bloodtypecombolist = ComboboxService.getBloodType();
        mv.addObject("bloodtypecombolist", bloodtypecombolist);
        List<IEntity> paymentmethodcombolist = ComboboxService.getPaymentMethod();
        mv.addObject("paymentmethodcombolist", paymentmethodcombolist);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("parttimes");
        history.setState("閲覧");
        history.saveHistory();
        return mv;
    }
    /**
     * パートナー登録画面を呼び出す
     * 
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/regist/partner")
    public ModelAndView showPartnerList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/regist/partner");
        // SQL実行用エンティティ
        mv.addObject("simpledata", new SimpleData());
        // 削除用コード
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        // リスト初期データ取得
        PartnerListEntity listEntity = new PartnerListEntity();
        SqlData sqllistdata = DownloadService.createSqlData(listEntity, listEntity.getPartnerListString(), null);
        List<IEntity> origin = SqlService.getEntityList(sqllistdata);
        mv.addObject("origin", origin);
        // リスト再取得用エンティティ
        mv.addObject("sqllistdata", sqllistdata);
        // 新規・更新用データ
        PartnerEntity formEntity = new PartnerEntity();
        SqlData sqlformdata = DownloadService.createSqlData(formEntity, formEntity.getSelectString(), new PartnerCsv());
        mv.addObject("sqlformdata", sqlformdata);
        mv.addObject("formentity", formEntity);
        // サイドバーリストデータ取得
        List<IEntity> sidebarlist = SidebarService.getPartnerCompanies();
        mv.addObject("sidebarlist", sidebarlist);
        // // CSVダウンロード用エンティティ
        // PartnerEntity fulltime = new PartnerEntity();
        // SqlData downloaddata = downloadService.createSqlData(fulltime, fulltime.getSelectString(), new PartnerCsv());
        // mv.addObject("downloaddata", downloaddata);
        // コンボボックス用エンティティ
        List<IEntity> companycombolist = ComboboxService.getPartnerCompanies();
        mv.addObject("companycombolist", companycombolist);
        List<IEntity> gendercombolist = ComboboxService.getGender();
        mv.addObject("gendercombolist", gendercombolist);
        List<IEntity> bloodtypecombolist = ComboboxService.getBloodType();
        mv.addObject("bloodtypecombolist", bloodtypecombolist);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("partners");
        history.setState("閲覧");
        // mv.addObject("history", history);
        history.saveHistory();

        return mv;
    }
    /**
    * 営業担当（外部）登録画面を呼び出す
    * 
    * @param mv
    * @param token
    * @return ModelAndView
    */
    @GetMapping("/regist/staff")
    public ModelAndView showStaffList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/regist/staff");
        // SQL実行用エンティティ
        mv.addObject("simpledata", new SimpleData());
        // 削除用コード
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        // リスト初期データ取得
        StaffListEntity listEntity = new StaffListEntity();
        SqlData sqllistdata = DownloadService.createSqlData(listEntity, listEntity.getStaffListString(), null);
        List<IEntity> origin = SqlService.getEntityList(sqllistdata);
        mv.addObject("origin", origin);
        // リスト再取得用エンティティ
        mv.addObject("sqllistdata", sqllistdata);
        // 新規・更新用データ
        StaffEntity formEntity = new StaffEntity();
        SqlData sqlformdata = DownloadService.createSqlData(formEntity, formEntity.getSelectString(), new StaffCsv());
        mv.addObject("sqlformdata", sqlformdata);
        mv.addObject("formentity", formEntity);
        // サイドバーリストデータ取得
        List<IEntity> sidebarparentlist = SidebarService.getOutsideGroupCategory();
        mv.addObject("sidebarparentlist", sidebarparentlist);
        List<IEntity> sidebarchildlist = SidebarService.getCompanies();
        mv.addObject("sidebarchildlist", sidebarchildlist);
        // // CSVダウンロード用エンティティ
        // StaffEntity office = new StaffEntity();
        // SqlData downloaddata = downloadService.createSqlData(office, office.getSelectString(), new StaffCsv());
        // mv.addObject("downloaddata", downloaddata);
        // コンボボックス用エンティティ
        List<IEntity> officelist = ComboboxService.getOfficesWithCompanyId();
        mv.addObject("officelist", officelist);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("staffs");
        history.setState("閲覧");
        // mv.addObject("history", history);
        history.saveHistory();

        return mv;
    }
    /**
     * 社員情報を作成・更新する
     * @param fulltimeEntity
     * @return
     */
    @PostMapping("/update/fulltime")
	@ResponseBody
    public int saveFulltime(@RequestBody FulltimeEntity fulltimeEntity) {
        if (fulltimeEntity.getState() == Enums.state.DELETE.getNum()) {
            return SqlService.excuteSqlString(fulltimeEntity.getDeleteString());
        }
        fulltimeEntity.setCompany_id(CompanyService.getDefaultCompanyId());
        fulltimeEntity.setCategory_id(Enums.companyCategory.OWN.getNum());
        if (fulltimeEntity.getEmployee_id() == 0) {
            return SqlService.excuteSqlString(fulltimeEntity.getInsertString());
        } else {
            return SqlService.excuteSqlString(fulltimeEntity.getUpdateString());
        }
    }
    /**
     * アルバイト情報を作成・更新する
     * @param parttimeEntity
     * @return
     */
    @PostMapping("/update/parttime")
	@ResponseBody
    public int saveParttime(@RequestBody ParttimeEntity parttimeEntity) {
        if (parttimeEntity.getState() == Enums.state.DELETE.getNum()) {
            return SqlService.excuteSqlString(parttimeEntity.getDeleteString());
        }
        parttimeEntity.setCompany_id(CompanyService.getDefaultCompanyId());
        parttimeEntity.setCategory_id(Enums.companyCategory.OWN.getNum());
        if (parttimeEntity.getEmployee_id() == 0) {
            return SqlService.excuteSqlString(parttimeEntity.getInsertString());
        } else {
            return SqlService.excuteSqlString(parttimeEntity.getUpdateString());
        }
    }
    /**
     * パートナー情報を作成・更新する
     * @param partnerEntity
     * @return
     */
    @PostMapping("/update/partner")
	@ResponseBody
    public int savePartner(@RequestBody PartnerEntity partnerEntity) {
        if (partnerEntity.getState() == Enums.state.DELETE.getNum()) {
            return SqlService.excuteSqlString(partnerEntity.getDeleteString());
        }
        partnerEntity.setCategory_id(Enums.companyCategory.PARTNER.getNum());
        if (partnerEntity.getEmployee_id() == 0) {
            return SqlService.excuteSqlString(partnerEntity.getInsertString());
        } else {
            return SqlService.excuteSqlString(partnerEntity.getUpdateString());
        }
    }
    /**
     * 営業担当者（外部）情報を作成・更新する
     * @param partnerEntity
     * @return
     */
    @PostMapping("/update/staff")
	@ResponseBody
    public int saveStaff(@RequestBody StaffEntity staffEntity) {
        if (staffEntity.getState() == Enums.state.DELETE.getNum()) {
            return SqlService.excuteSqlString(staffEntity.getDeleteString());
        }
        staffEntity.setCategory_id(Enums.companyCategory.PARTNER.getNum());
        if (staffEntity.getEmployee_id() == 0) {
            return SqlService.excuteSqlString(staffEntity.getInsertString());
        } else {
            return SqlService.excuteSqlString(staffEntity.getUpdateString());
        }
    }
    /**
     * 会社情報を作成・更新する
     * @param companyEntity
     * @return
     */
    @PostMapping("/update/company")
	@ResponseBody
    public int saveCompany(@RequestBody CompanyEntity companyEntity) {
        if (companyEntity.getState() == Enums.state.DELETE.getNum()) {
            return SqlService.excuteSqlString(companyEntity.getDeleteString());
        }
        if (companyEntity.getCompany_id() == 0) {
            return SqlService.excuteSqlString(companyEntity.getInsertString());
        } else {
            return SqlService.excuteSqlString(companyEntity.getUpdateString());
        }
    }
    /**
     * 支店情報を作成・更新する
     * @param officeEntity
     * @return
     */
    @PostMapping("/update/office")
	@ResponseBody
    public int saveOffice(@RequestBody OfficeEntity officeEntity) {
        if (officeEntity.getState() == Enums.state.DELETE.getNum()) {
            return SqlService.excuteSqlString(officeEntity.getDeleteString());
        }
        if (officeEntity.getOffice_id() == 0) {
            return SqlService.excuteSqlString(officeEntity.getInsertString());
        } else {
            return SqlService.excuteSqlString(officeEntity.getUpdateString());
        }
    }
    /**
     * コードから[Employee]を取得する
     * @param officeEntity
     * @return
     */
    @PostMapping("/employee/code")
	@ResponseBody
    public IEntity getEmployeeForCode(@RequestParam int code) {
        return EmployeeService.getEmployeeForCode(code);
    }
}
