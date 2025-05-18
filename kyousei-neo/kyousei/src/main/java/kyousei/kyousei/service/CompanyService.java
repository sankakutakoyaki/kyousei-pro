package kyousei.kyousei.service;

import org.springframework.stereotype.Service;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.corporation.CompanyEntity;
import kyousei.kyousei.entity.data.SimpleData;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.interfaceis.IEntity;

@Service
// @RequiredArgsConstructor
public class CompanyService {
    // private final SqlRepository sqlRepository;
    // private final DownloadService downloadService;

    /**
     * 自社IDを取得
     * @return
     */
    public static int getDefaultCompanyId() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT company_id as number, company_name as text FROM companies");
        sb.append(" WHERE company_name = '協成産業株式会社' AND state = " + Enums.state.UNDECIDED.getNum());
        SqlData sqlData = DownloadService.createSqlData(new SimpleData(), sb.toString(), null);
        SimpleData simpleData = (SimpleData)SqlService.getEntity(sqlData);
        return simpleData.getNumber();
    }
    /**
     * 本社IDを取得
     * @return
     */
    public static int getDefaultOfficeId() {
        int companyId = getDefaultCompanyId();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT office_id as number, office_name as text FROM offices");
        sb.append(" WHERE office_name = '本社' AND company_id = " + companyId + " AND state = " + Enums.state.UNDECIDED.getNum());
        SqlData sqlData = DownloadService.createSqlData(new SimpleData(), sb.toString(), null);
        SimpleData simpleData = (SimpleData)SqlService.getEntity(sqlData);
        return simpleData.getNumber();
    }
    /**
     * 登録番号から[company]を取得
     * @param code
     * @return company
     */
    public static IEntity getCompanyForRegistCode(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.*, co.* FROM companies c");
        sb.append(" INNER JOIN corporations co ON co.corporation_id = c.corporation_id");
        sb.append(" WHERE c.registration_number = '" + code + "' AND c.state = " + Enums.state.UNDECIDED.getNum());
        
        SqlData sqlData = new SqlData();
        sqlData = DownloadService.createSqlData(new CompanyEntity(), sb.toString(), null);

        return SqlService.getEntity(sqlData);
    }
}
