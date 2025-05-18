package com.kyouseipro.kyousei.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.common.Utilities;
import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.entity.corporation.CompanyFormEntity;
import com.kyouseipro.kyousei.entity.corporation.CompanyListEntity;
import com.kyouseipro.kyousei.entity.corporation.OfficeFormEntity;
import com.kyouseipro.kyousei.entity.corporation.OfficeListEntity;
import com.kyouseipro.kyousei.entity.employee.EmployeeEntity;
import com.kyouseipro.kyousei.entity.employee.FullTimeFormEntity;
import com.kyouseipro.kyousei.entity.employee.FullTimeListEntity;
import com.kyouseipro.kyousei.entity.employee.PartTimeFormEntity;
import com.kyouseipro.kyousei.entity.employee.PartTimeListEntity;
import com.kyouseipro.kyousei.entity.employee.PartnerFormEntity;
import com.kyouseipro.kyousei.entity.employee.PartnerListEntity;
import com.kyouseipro.kyousei.entity.employee.StaffFormEntity;
import com.kyouseipro.kyousei.entity.employee.StaffListEntity;
import com.kyouseipro.kyousei.entity.recycle.RecycleEntity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksFullTimeEneity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksListEntity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksPaymentEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.repository.CategoryRepository;
import com.kyouseipro.kyousei.repository.CorporationRepository;
import com.kyouseipro.kyousei.repository.EmployeeRepository;
import com.kyouseipro.kyousei.repository.ExpensesRepository;
import com.kyouseipro.kyousei.repository.ListRepository;
import com.kyouseipro.kyousei.repository.RecycleRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ModelAndViewController {

    private final CorporationRepository corporationRepository;
    private final EmployeeRepository employeeRepository;
    private final ListRepository listRepository;
    private final CategoryRepository categoryRepository;
    private final RecycleRepository recycleRepository;
    private final ExpensesRepository expensesRepository;

    /**
     * 従業員一覧画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/list/fulltime")
    public ModelAndView showFulltimeList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/tablelist/employee/fulltimeList");
        mv.addObject("sidebarName", "fragment/sidebarFragment::sidebar-fragment");
        
        // 自社IDを設定
        int companyId = corporationRepository.getDefaultCompanyId();
        mv.addObject("companyid", companyId);

        // 社員カテゴリーIDを設定
        mv.addObject("categoryid", Enums.employeeCategory.FULLTIME.getNum());

        // サイドバー用エンティティリスト
        List<IEntity> categorylist = corporationRepository.getOfficeListWithSimpleDataByCompanyId(companyId);
        mv.addObject("categorylist", categorylist);

        // フォーム検索用エンティティ
        SqlData sqlformData = new SqlData();
        sqlformData.createFormEntityData(new FullTimeFormEntity());
        mv.addObject("sqlformdata", sqlformData);

        // リスト検索用エンティティ
        SqlData sqllistData = new SqlData();
        sqllistData.createListEntityData(new FullTimeListEntity());
        mv.addObject("sqllistdata", sqllistData);

        // リスト初期データ
        List<IEntity> tablelist = listRepository.getAllEntitiesThatMatchSearchCiteria(sqllistData);
        mv.addObject("tablelist", tablelist);

        return mv;
    }

    /**
     * アルバイト一覧画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/list/parttime")
    public ModelAndView showParttimeList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/tablelist/employee/parttimeList");
        mv.addObject("sidebarName", "fragment/sidebarFragment::sidebar-fragment");

        // 自社IDを設定
        int companyId = corporationRepository.getDefaultCompanyId();
        mv.addObject("companyid", companyId);

        // 社員カテゴリーIDを設定
        mv.addObject("categoryid", Enums.employeeCategory.PARTTIME.getNum());
        // mv.addObject("categoryid", 2);

        // サイドバー用エンティティリスト
        List<IEntity> categorylist = corporationRepository.getOfficeListWithSimpleDataByCompanyId(companyId);
        mv.addObject("categorylist", categorylist);

        // フォーム検索用エンティティ
        SqlData sqlformData = new SqlData();
        sqlformData.createFormEntityData(new PartTimeFormEntity());
        mv.addObject("sqlformdata", sqlformData);

        // リスト検索用エンティティ
        SqlData sqllistData = new SqlData();
        sqllistData.createListEntityData(new PartTimeListEntity());
        mv.addObject("sqllistdata", sqllistData);

        // リスト初期データ
        List<IEntity> tablelist = listRepository.getAllEntitiesThatMatchSearchCiteria(sqllistData);
        mv.addObject("tablelist", tablelist);

        return mv;
    }

    /**
     * 会社一覧画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/list/company")
    public ModelAndView showCompanyList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/tablelist/company/companyList");
        mv.addObject("sidebarName", "fragment/sidebarFragment::sidebar-fragment");

        // サイドバー用エンティティリスト
        List<IEntity> categorylist = categoryRepository.getCompanyClassListWithSimpleData();
        mv.addObject("categorylist", categorylist);

        // フォーム検索用エンティティ
        SqlData sqlformData = new SqlData();
        sqlformData.createFormEntityData(new CompanyFormEntity());
        mv.addObject("sqlformdata", sqlformData);

        // リスト検索用エンティティ
        SqlData sqllistData = new SqlData();
        sqllistData.createListEntityData(new CompanyListEntity());
        mv.addObject("sqllistdata", sqllistData);

        // リスト初期データ
        List<IEntity> tablelist = listRepository.getAllEntitiesThatMatchSearchCiteria(sqllistData);
        mv.addObject("tablelist", tablelist);

        return mv;
    }

    /**
     * 支店一覧画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/list/office")
    public ModelAndView showOfficeList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/tablelist/office/officeList");
        mv.addObject("sidebarName", "fragment/sidebarFragment::sidebar-fragment");

        // サイドバー用エンティティリスト
        List<IEntity> categorylist = corporationRepository.getAllCompanyListWithSimpleData();
        mv.addObject("categorylist", categorylist);

        // フォーム検索用エンティティ
        SqlData sqlformData = new SqlData();
        sqlformData.createFormEntityData(new OfficeFormEntity());
        mv.addObject("sqlformdata", sqlformData);

        // リスト検索用エンティティ
        SqlData sqllistData = new SqlData();
        sqllistData.createListEntityData(new OfficeListEntity());
        mv.addObject("sqllistdata", sqllistData);

        // リスト初期データ
        List<IEntity> tablelist = listRepository.getAllEntitiesThatMatchSearchCiteria(sqllistData);
        mv.addObject("tablelist", tablelist);

        return mv;
    }

    /**
     * 施工担当一覧画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/list/partner")
    public ModelAndView showPartnerList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/tablelist/partner/partnerList");
        mv.addObject("sidebarName", "fragment/sidebarFragment::sidebar-fragment");

        // サイドバー用エンティティリスト
        List<IEntity> categorylist = corporationRepository.getCompanyListWithSimpleDataByCategoryId(Enums.companyCategory.PARTNER.getNum());
        mv.addObject("categorylist", categorylist);

        // サイドバー用エンティティリスト
        List<IEntity> subcategorylist = corporationRepository.getOfficeListWithSelectListDataByCategoryId(Enums.companyCategory.PARTNER.getNum());
        mv.addObject("subcategorylist", subcategorylist);

        // フォーム検索用エンティティ
        SqlData sqlformData = new SqlData();
        sqlformData.createFormEntityData(new PartnerFormEntity());
        mv.addObject("sqlformdata", sqlformData);

        // リスト検索用エンティティ
        SqlData sqllistData = new SqlData();
        sqllistData.createListEntityData(new PartnerListEntity());
        mv.addObject("sqllistdata", sqllistData);

        // リスト初期データ
        List<IEntity> tablelist = listRepository.getAllEntitiesThatMatchSearchCiteria(sqllistData);
        mv.addObject("tablelist", tablelist);

        return mv;
    }

    /**
     * 営業担当一覧画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/list/staff")
    public ModelAndView showStaffList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/tablelist/staff/staffList");
        mv.addObject("sidebarName", "fragment/sidebarFragment::sidebar-fragment");

        // サイドバー用エンティティリスト
        List<IEntity> categorylist = corporationRepository.getCompanyListWithSimpleDataByCategoryId(Enums.companyCategory.SHIPPER.getNum());
        mv.addObject("categorylist", categorylist);

        // サイドバー用エンティティリスト
        List<IEntity> subcategorylist = corporationRepository.getOfficeListWithSelectListDataByCategoryId(Enums.companyCategory.SHIPPER.getNum());
        mv.addObject("subcategorylist", subcategorylist);

        // フォーム検索用エンティティ
        SqlData sqlformData = new SqlData();
        sqlformData.createFormEntityData(new StaffFormEntity());
        mv.addObject("sqlformdata", sqlformData);

        // リスト検索用エンティティ
        SqlData sqllistData = new SqlData();
        sqllistData.createListEntityData(new StaffListEntity());
        mv.addObject("sqllistdata", sqllistData);

        // リスト初期データ
        List<IEntity> tablelist = listRepository.getAllEntitiesThatMatchSearchCiteria(sqllistData);
        mv.addObject("tablelist", tablelist);

        return mv;
    }

    /**
     * 選択項目登録画面を呼び出す
     * @param mv
     * @param token
     * @return
     */
    @GetMapping("/category/regist/item")
    public ModelAndView showCategoryRegistItem(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/category/regist/itemCategoryRegist");
        mv.addObject("updateStateCode", Enums.state.UPDATE.getNum());
        mv.addObject("createStateCode", Enums.state.CREATE.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        List<IEntity> categoryRegistClassList = categoryRepository.getItemClassListWithSimpleDataFromClassCategories();
        mv.addObject("categoryRegistClassList", categoryRegistClassList);
        List<IEntity> list = categoryRepository.getAllItemCategoryList();
        mv.addObject("list", list);

        return mv;
    }

    // /**
    //  * 選択項目カテゴリー登録画面を呼び出す
    //  * @param mv
    //  * @param token
    //  * @return
    //  */
    // @GetMapping("/category/regist/class")
    // public ModelAndView showCategoryRegistClass(ModelAndView mv, OAuth2AuthenticationToken token) {
    //     mv.setViewName("content/category/regist/classCategoryRegist");
    //     mv.addObject("updateStateCode", Enums.state.UPDATE.getNum());
    //     mv.addObject("createStateCode", Enums.state.CREATE.getNum());
    //     mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
    //     List<IEntity> list = categoryRepository.getAllClassCategoryList();
    //     mv.addObject("list", list);

    //     return mv;
    // }

    /**
     * 使用済みリサイクル券の一覧画面を呼び出す
     * @param mv
     * @param token
     * @return
     */
    @GetMapping("/recycle/list")
    public ModelAndView showRecycleList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/recycle/list/recycleList");
        mv.addObject("sidebarName", "fragment/sidebarFragment::sidebar-fragment");

        mv.addObject("updateStateCode", Enums.state.UPDATE.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        List<IEntity> shipperlist = recycleRepository.getShipperListWithSimpleData();
        mv.addObject("shipperlist", shipperlist);
        List<IEntity> makerlist = recycleRepository.getAllRecycleMakerList();
        mv.addObject("makerlist", makerlist);
        List<IEntity> pricelist = recycleRepository.getAllRecyclePriceList();
        mv.addObject("pricelist", pricelist);
        List<IEntity> classlist = recycleRepository.getAllRecycleClassList();
        mv.addObject("classlist", classlist);
        SqlData sqlformData = new SqlData();
        sqlformData.createFormEntityData(new RecycleEntity());
        mv.addObject("sqlformdata", sqlformData);
        LocalDate date = LocalDate.now();
        List<IEntity> list = recycleRepository.getBetweenRecycleList(Enums.recycleDateCategory.INPUT.getNum(), date, date);
        mv.addObject("list", list);
        mv.addObject("inputStateCode", Enums.recycleDateCategory.INPUT.getNum());
        mv.addObject("useStateCode", Enums.recycleDateCategory.USE.getNum());
        mv.addObject("deliveryStateCode", Enums.recycleDateCategory.DELIVERY.getNum());
        mv.addObject("forwardStateCode", Enums.recycleDateCategory.FORWARD.getNum());

        return mv;
    }

    /**
     * 使用済みリサイクル券登録画面を呼び出す
     * @param mv
     * @param token
     * @return
     */
    @GetMapping("/recycle/regist")
    public ModelAndView showRecycleRegistList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/recycle/regist/recycleRegist");
        mv.addObject("updateStateCode", Enums.state.UPDATE.getNum());
        mv.addObject("createStateCode", Enums.state.CREATE.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        List<IEntity> shipperlist = recycleRepository.getShipperListWithSimpleData();
        mv.addObject("shipperlist", shipperlist);
        List<IEntity> makerlist = recycleRepository.getAllRecycleMakerList();
        mv.addObject("makerlist", makerlist);
        List<IEntity> pricelist = recycleRepository.getAllRecyclePriceList();
        mv.addObject("pricelist", pricelist);
        List<IEntity> classlist = recycleRepository.getAllRecycleClassList();
        mv.addObject("classlist", classlist);
        // LocalDate date = LocalDate.now();
        // List<IEntity> list = recycleRepository.getRecycleList(Enums.recycleDateCategory.USE.getNum(),date);
        List<IEntity> list = recycleRepository.getRecycleListInputToday(Enums.recycleDateCategory.USE.getNum());
        mv.addObject("list", list);
        mv.addObject("categoryid", Enums.recycleDateCategory.USE.getNum());
        return mv;
    }

    /**
     * 引渡リサイクル券登録画面を呼び出す
     * @param mv
     * @param token
     * @return
     */
    @GetMapping("/recycle/regist/delivery")
    public ModelAndView showRecycleDeliveryRegistList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/recycle/delivery/recycleDeliveryRegist");
        mv.addObject("updateStateCode", Enums.state.UPDATE.getNum());
        mv.addObject("createStateCode", Enums.state.CREATE.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        // LocalDate date = LocalDate.now();
        // List<IEntity> list = recycleRepository.getRecycleList(Enums.recycleDateCategory.DELIVERY.getNum(),date);
        List<IEntity> list = recycleRepository.getRecycleListInputToday(Enums.recycleDateCategory.DELIVERY.getNum());
        mv.addObject("list", list);
        mv.addObject("categoryid", Enums.recycleDateCategory.DELIVERY.getNum());
        return mv;
    }

    /**
     * 発送リサイクル券登録画面を呼び出す
     * @param mv
     * @param token
     * @return
     */
    @GetMapping("/recycle/regist/forward")
    public ModelAndView showRecycleForwardRegistList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/recycle/forward/recycleForwardRegist");
        mv.addObject("updateStateCode", Enums.state.UPDATE.getNum());
        mv.addObject("createStateCode", Enums.state.CREATE.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        List<IEntity> shipperlist = recycleRepository.getShipperListWithSimpleData();
        mv.addObject("shipperlist", shipperlist);
        List<IEntity> officelist = recycleRepository.getOfficeListWithSimpleData(corporationRepository.getDefaultCompanyId());
        mv.addObject("officelist", officelist);
        // LocalDate date = LocalDate.now();
        // List<IEntity> list = recycleRepository.getRecycleList(Enums.recycleDateCategory.FORWARD.getNum(),date);
        List<IEntity> list = recycleRepository.getRecycleListInputToday(Enums.recycleDateCategory.FORWARD.getNum());
        mv.addObject("list", list);
        mv.addObject("categoryid", Enums.recycleDateCategory.FORWARD.getNum());
        return mv;
    }

    /**
     * リサイクルメーカー登録画面を呼び出す
     * @param mv
     * @param token
     * @return
     */
    @GetMapping("/recycle/regist/maker")
    public ModelAndView showRecycleMakerList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/recycle/maker/recycleMakerRegist");
        mv.addObject("updateStateCode", Enums.state.UPDATE.getNum());
        mv.addObject("createStateCode", Enums.state.CREATE.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        List<IEntity> list = recycleRepository.getAllRecycleMakerList();
        mv.addObject("list", list);

        return mv;
    }

    /**
     * 製造業者登録画面を呼び出す
     * @param mv
     * @param token
     * @return
     */
    @GetMapping("/recycle/regist/product")
    public ModelAndView showRecycleProductList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/recycle/product/recycleProductRegist");
        mv.addObject("updateStateCode", Enums.state.UPDATE.getNum());
        mv.addObject("createStateCode", Enums.state.CREATE.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        List<IEntity> list = recycleRepository.getAllRecycleProductList();
        mv.addObject("list", list);
        List<IEntity> makerlist = recycleRepository.getAllRecycleMakerList();
        mv.addObject("makerlist", makerlist);

        return mv;
    }

    /**
     * リサイクル法定料金登録画面を呼び出す
     * @param mv
     * @param token
     * @return
     */
    @GetMapping("/recycle/regist/price")
    public ModelAndView showRecyclePriceList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/recycle/price/recyclePriceRegist");
        mv.addObject("updateStateCode", Enums.state.UPDATE.getNum());
        mv.addObject("createStateCode", Enums.state.CREATE.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());
        List<IEntity> list = recycleRepository.getAllRecyclePriceList();
        mv.addObject("list", list);

        return mv;
    }

    /**
     * ロス処理登録画面を呼び出す
     * @param mv
     * @param token
     * @return
     */
    @GetMapping("/recycle/regist/loss")
    public ModelAndView showRecycleLossRegistList(ModelAndView mv, OAuth2AuthenticationToken token) {
        mv.setViewName("content/recycle/loss/recycleLossRegist");
        mv.addObject("updateStateCode", Enums.state.UPDATE.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());

        return mv;
    }

    /**
     * タイムレコーダー画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
	@GetMapping("/timeworks/recorder")
	public ModelAndView showList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
		mv.setViewName("content/timeworks/timeworks");
        TimeworksListEntity entity = new TimeworksListEntity();
		if (token != null) {
            // ログイン名から[employee_id]を取得
            String username = principal.getAttribute("preferred_username");
            EmployeeEntity emp = (EmployeeEntity)employeeRepository.getEmployeeForAccount(username);
            mv.addObject("employee_id", emp.getEmployee_id());
            mv.addObject("category_id", emp.getCategory_id());
        }
        mv.addObject("entity", entity);
	    return mv;
	}

    /**
     * 勤怠一覧画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/timeworks/list")
    public ModelAndView showTimewoeksList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        mv.setViewName("content/timeworks/list/timeworksList");

        // リスト検索用エンティティ
        SqlData sqlData = new SqlData();
        sqlData.createFormEntityData(new TimeworksFullTimeEneity());
        mv.addObject("sqldata", sqlData);

        // リスト初期データ
        String firstDate = Utilities.getFirstDate();
        String lastDate = Utilities.getLastDate();
        SqlData sqldata = new SqlData();
        TimeworksFullTimeEneity entity = new TimeworksFullTimeEneity();
        sqldata.setSqlString(entity.getSelectString() + " AND tw.work_date BETWEEN '" + firstDate + "' AND '" + lastDate + "' ORDER BY tw.work_date");
        sqldata.setClassPath(entity.getClass().getName());
        List<IEntity> tablelist = listRepository.getAllEntitiesThatMatchSearchCiteria(sqldata);
        mv.addObject("tablelist", tablelist);
        mv.addObject("selectSql", entity.getSelectString());
        // // 源泉リスト
        // List<IEntity> taxlist = expensesRepository.getWithholdingTaxList();
        // mv.addObject("taxlist", taxlist);
        // // 金庫リスト
        // String account = principal.getAttribute("preferred_username");
        // EmployeeEntity employee = (EmployeeEntity)employeeRepository.getEmployeeForAccount(account);
        // List<IEntity> banklist = expensesRepository.getBankList(employee.getOffice_id());
        // mv.addObject("banklist", banklist);
        // 自社IDを設定
        int companyId = corporationRepository.getDefaultCompanyId();
        mv.addObject("companyid", companyId);
        mv.addObject("categoryid", Enums.employeeCategory.FULLTIME.getNum());

        // mv.addObject("completePayStateCode", Enums.situation.DONE.getNum());
        // mv.addObject("notPayStateCode", Enums.situation.YET.getNum());
        mv.addObject("completeStateCode", Enums.state.COMFILM.getNum());
        mv.addObject("notStateCode", Enums.state.UNDECIDED.getNum());
        // mv.addObject("completePrintStateCode", Enums.situation.DONE.getNum());
        // mv.addObject("notPrintStateCode", Enums.situation.YET.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());

        return mv;
    }

    /**
     * 勤怠確定画面を呼び出す
     * @param mv
     * @param token
     * @return ModelAndView
     */
    @GetMapping("/timeworks/confilm")
    public ModelAndView showTimeworksConfilmList(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        mv.setViewName("content/timeworks/confilm/timeworksConfilm");

        // リスト検索用エンティティ
        SqlData sqlData = new SqlData();
        sqlData.createFormEntityData(new TimeworksPaymentEntity());
        mv.addObject("sqldata", sqlData);

        // リスト初期データ
        String firstDate = Utilities.getFirstDate();
        String lastDate = Utilities.getLastDate();
        SqlData sqldata = new SqlData();
        TimeworksPaymentEntity entity = new TimeworksPaymentEntity();
        sqldata.setSqlString(entity.getSelectString() + " AND tw.work_date BETWEEN '" + firstDate + "' AND '" + lastDate + "' ORDER BY tw.work_date");
        sqldata.setClassPath(entity.getClass().getName());
        List<IEntity> tablelist = listRepository.getAllEntitiesThatMatchSearchCiteria(sqldata);
        mv.addObject("tablelist", tablelist);
        mv.addObject("selectSql", entity.getSelectString());
        // 源泉リスト
        List<IEntity> taxlist = expensesRepository.getWithholdingTaxList();
        mv.addObject("taxlist", taxlist);
        // 自社IDを設定
        int companyId = corporationRepository.getDefaultCompanyId();
        mv.addObject("companyid", companyId);
        mv.addObject("categoryid", Enums.employeeCategory.PARTTIME.getNum());

        mv.addObject("completePayStateCode", Enums.situation.DONE.getNum());
        mv.addObject("notPayStateCode", Enums.situation.YET.getNum());
        mv.addObject("completeStateCode", Enums.state.COMFILM.getNum());
        mv.addObject("notStateCode", Enums.state.UNDECIDED.getNum());
        // mv.addObject("completePrintStateCode", Enums.situation.DONE.getNum());
        // mv.addObject("notPrintStateCode", Enums.situation.YET.getNum());
        mv.addObject("deleteStateCode", Enums.state.DELETE.getNum());

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
        mv.setViewName("content/timeworks/dailypay/dailypayList");

        // リスト検索用エンティティ
        SqlData sqlData = new SqlData();
        sqlData.createFormEntityData(new TimeworksPaymentEntity());
        mv.addObject("sqldata", sqlData);

        // リスト初期データ
        String firstDate = Utilities.getFirstDate();
        String lastDate = Utilities.getLastDate();
        SqlData sqldata = new SqlData();
        TimeworksPaymentEntity entity = new TimeworksPaymentEntity();
        sqldata.setSqlString(entity.getSelectString() + " AND tw.work_date BETWEEN '" + firstDate + "' AND '" + lastDate + "' ORDER BY tw.work_date");
        sqldata.setClassPath(entity.getClass().getName());
        List<IEntity> tablelist = listRepository.getAllEntitiesThatMatchSearchCiteria(sqldata);
        mv.addObject("tablelist", tablelist);
        mv.addObject("selectSql", entity.getSelectString());
        // 源泉リスト
        List<IEntity> taxlist = expensesRepository.getWithholdingTaxList();
        mv.addObject("taxlist", taxlist);
        // 金庫リスト
        String account = principal.getAttribute("preferred_username");
        EmployeeEntity employee = (EmployeeEntity)employeeRepository.getEmployeeForAccount(account);
        List<IEntity> banklist = expensesRepository.getBankList(employee.getOffice_id());
        mv.addObject("banklist", banklist);
        // 自社IDを設定
        int companyId = corporationRepository.getDefaultCompanyId();
        mv.addObject("companyid", companyId);

        mv.addObject("completePayStateCode", Enums.situation.DONE.getNum());
        mv.addObject("notPayStateCode", Enums.situation.YET.getNum());
        mv.addObject("completeStateCode", Enums.state.COMFILM.getNum());
        mv.addObject("notStateCode", Enums.state.UNDECIDED.getNum());
        mv.addObject("completePrintStateCode", Enums.situation.DONE.getNum());
        mv.addObject("notPrintStateCode", Enums.situation.YET.getNum());

        return mv;
    }
}