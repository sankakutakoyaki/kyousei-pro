package kyousei.kyousei.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.corporation.CompanyCsv;
import kyousei.kyousei.entity.corporation.CompanyEntity;
import kyousei.kyousei.entity.corporation.CompanyListEntity;
import kyousei.kyousei.entity.corporation.OfficeCsv;
import kyousei.kyousei.entity.corporation.OfficeEntity;
import kyousei.kyousei.entity.corporation.OfficeListEntity;
import kyousei.kyousei.entity.data.HistoryEntity;
import kyousei.kyousei.entity.data.SimpleData;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.interfaceis.IEntity;
import kyousei.kyousei.service.ComboboxService;
import kyousei.kyousei.service.CompanyService;
import kyousei.kyousei.service.DownloadService;
import kyousei.kyousei.service.SidebarService;
import kyousei.kyousei.service.SqlService;

@Controller
// @RequiredArgsConstructor
public class CompanyController {
    // private final SqlRepository sqlRepository;
    // private final ComboboxService comboboxService;
    // private final SidebarService sidebarService;
    // private final DownloadService downloadService;
    // private final CompanyService companyService;

    /**
     * 会社（外部）登録画面を呼び出す
     * 
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/regist/company")
    public ModelAndView showCompanyList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/regist/company");
        // SQL実行用エンティティ
        mv.addObject("simpledata", new SimpleData());
        // 削除用コード
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        // リスト初期データ取得
        CompanyListEntity listEntity = new CompanyListEntity();
        SqlData sqllistdata = DownloadService.createSqlData(listEntity, listEntity.getOutsideCompanyListString(), null);
        List<IEntity> origin = SqlService.getEntityList(sqllistdata);
        mv.addObject("origin", origin);
        // リスト再取得用エンティティ
        mv.addObject("sqllistdata", sqllistdata);
        // 新規・更新用データ
        CompanyEntity formEntity = new CompanyEntity();
        SqlData sqlformdata = DownloadService.createSqlData(formEntity, formEntity.getSelectString(), new CompanyCsv());
        mv.addObject("sqlformdata", sqlformdata);
        mv.addObject("formentity", formEntity);
        // サイドバーリストデータ取得
        List<IEntity> sidebarlist = SidebarService.getOutsideCompanyCategory();
        mv.addObject("sidebarlist", sidebarlist);
        // // CSVダウンロード用エンティティ
        // CompanyEntity company = new CompanyEntity();
        // SqlData downloaddata = downloadService.createSqlData(company, company.getSelectString(), new CompanyCsv());
        // mv.addObject("downloaddata", downloaddata);
        // コンボボックス用エンティティ
        List<IEntity> categorycombolist = ComboboxService.getOutsideCompanyCategory();
        mv.addObject("categorycombolist", categorycombolist);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("companies");
        history.setState("閲覧");
        // mv.addObject("history", history);
        history.saveHistory();

        return mv;
    }
    /**
    * 支店（外部）登録画面を呼び出す
    * 
    * @param mv
    * @param token
    * @return ModelAndView
    */
    @GetMapping("/regist/office")
    public ModelAndView showOfficeList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/tablelist/regist/office");
        // SQL実行用エンティティ
        mv.addObject("simpledata", new SimpleData());
        // 削除用コード
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        // リスト初期データ取得
        OfficeListEntity listEntity = new OfficeListEntity();
        SqlData sqllistdata = DownloadService.createSqlData(listEntity, listEntity.getOfficeListString(), null);
        List<IEntity> origin = SqlService.getEntityList(sqllistdata);
        mv.addObject("origin", origin);
        // リスト再取得用エンティティ
        mv.addObject("sqllistdata", sqllistdata);
        // 新規・更新用データ
        OfficeEntity formEntity = new OfficeEntity();
        SqlData sqlformdata = DownloadService.createSqlData(formEntity, formEntity.getSelectString(), new OfficeCsv());
        mv.addObject("sqlformdata", sqlformdata);
        mv.addObject("formentity", formEntity);
        // サイドバーリストデータ取得
        List<IEntity> sidebarparentlist = SidebarService.getOutsideGroupCategory();
        mv.addObject("sidebarparentlist", sidebarparentlist);
        List<IEntity> sidebarchildlist = SidebarService.getCompanies();
        mv.addObject("sidebarchildlist", sidebarchildlist);
        // // CSVダウンロード用エンティティ
        // OfficeEntity office = new OfficeEntity();
        // SqlData downloaddata = downloadService.createSqlData(office, office.getSelectString(), new OfficeCsv());
        // mv.addObject("downloaddata", downloaddata);
        // コンボボックス用エンティティ
        List<IEntity> companycombolist = ComboboxService.getOutsideCompanyCategory();
        mv.addObject("companycombolist", companycombolist);
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("offices");
        history.setState("閲覧");
        // mv.addObject("history", history);
        history.saveHistory();

        return mv;
    }
    /**
     * 登録番号から[company]を取得する
     * @param registcode
     * @return
     */
    @PostMapping("/company/registcode")
	@ResponseBody
    public IEntity getCompanyForRegistCode(@RequestParam String code) {
        return CompanyService.getCompanyForRegistCode(code);
    }

//#region backup
    // /**
    //  * 会社詳細画面を呼び出す
    //  * 
    //  * @param mv
    //  * @param token
    //  * @return ModelAndView
    //  */
    // @GetMapping("/company-detail")
    // public ModelAndView showOfficeList(ModelAndView mv, OAuth2AuthenticationToken token) {
    //     // MVを設定
    //     mv.setViewName("content/tablelist/company-detail");
    //     // SQL実行用エンティティ
    //     mv.addObject("simpledata", new SimpleData());
    //     // 削除用コード
    //     mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());

    //     // 支店リスト初期データ取得
    //     OfficeListEntity officeListEntity = new OfficeListEntity();
    //     SqlData sqlofficelistdata = downloadService.createSqlData(officeListEntity, officeListEntity.getOfficeListString(), null);
    //     List<IEntity> origin01 = sqlRepository.getEntityList(sqlofficelistdata);
    //     mv.addObject("origin01", origin01);
    //     // 支店リスト再取得用エンティティ
    //     mv.addObject("sqlofficelistdata", sqlofficelistdata);
    //     // 営業担当者リスト初期データ取得
    //     StaffListEntity staffListEntity = new StaffListEntity();
    //     SqlData sqlstafflistdata = downloadService.createSqlData(staffListEntity, staffListEntity.getStaffListString(), null);
    //     List<IEntity> origin02 = sqlRepository.getEntityList(sqlstafflistdata);
    //     mv.addObject("origin02", origin02);
    //     // 営業担当者リスト再取得用エンティティ
    //     mv.addObject("sqlstafflistdata", sqlstafflistdata);
    //     // 支店新規・更新用データ
    //     OfficeEntity officeFormEntity = new OfficeEntity();
    //     SqlData sqlofficeformdata = downloadService.createSqlData(officeFormEntity, officeFormEntity.getSelectString(), null);
    //     mv.addObject("sqlofficeformdata", sqlofficeformdata);
    //     mv.addObject("formentity", officeFormEntity);
    //     // 営業担当者新規・更新用データ
    //     StaffEntity staffFormEntity = new StaffEntity();
    //     SqlData sqlstaffformdata = downloadService.createSqlData(staffFormEntity, staffFormEntity.getSelectString(), null);
    //     mv.addObject("sqlstaffformdata", sqlstaffformdata);
    //     mv.addObject("staffFormEntity", staffFormEntity);
    //     // 支店CSVダウンロード用エンティティ
    //     OfficeEntity office = new OfficeEntity();
    //     SqlData downloaddata = downloadService.createSqlData(office, office.getSelectString(), new OfficeCsv());
    //     mv.addObject("downloaddata", downloaddata);

    //     // サイドバーリストデータ取得
    //     List<IEntity> sidebarparentlist = sidebarService.getOutsideGroupCategory();
    //     mv.addObject("sidebarparentlist", sidebarparentlist);
    //     List<IEntity> sidebarchildlist = sidebarService.getCompanies();
    //     mv.addObject("sidebarchildlist", sidebarchildlist);
    //     // // コンボリスト用データ
    //     // List<IEntity> officecombolist = comboboxService.getOffices();
    //     // mv.addObject("officecombolist", officecombolist);
    //     return mv;
    // }
//#endregion
}
