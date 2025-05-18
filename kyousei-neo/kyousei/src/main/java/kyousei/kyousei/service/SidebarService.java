package kyousei.kyousei.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.data.SidebarData;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.interfaceis.IEntity;

@Service
// @RequiredArgsConstructor
public class SidebarService {
    // private final SqlRepository sqlRepository;

    /**
     * SidebarDataのリストを作成
     * @param entity
     * @param str
     * @return
     */
    private static List<IEntity> setSidebarData(IEntity entity, String str) {
        SqlData sqlData = new SqlData();
        sqlData.setSqlString(str);
        sqlData.setPath(entity);
        return SqlService.getEntityList(sqlData);
   }
    /**
     * 自社営業所のリスト
     * @return
     */
    public static List<IEntity> getOwnerOffices() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT o.office_id as id, o.company_id as parent_id, o.office_name as text, NULLIF('','') as img From offices o");
        sb.append(" INNER JOIN companies c ON c.company_id = o.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE o.state = " + Enums.state.UNDECIDED.getNum() + " AND c.category_id = " + Enums.companyCategory.OWN.getNum());
        return setSidebarData(new SidebarData(), sb.toString());
    }
    /**
     * パートナーのリスト
     * @return
     */
    public static List<IEntity> getPartnerCompanies() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.company_id as id, c.category_id as parent_id, c.company_name as text, NULLIF('','') as img From companies c");
        sb.append(" WHERE c.state = " + Enums.state.UNDECIDED.getNum() + " AND c.category_id = " + Enums.companyCategory.PARTNER.getNum());
        return setSidebarData(new SidebarData(), sb.toString());
    }
    /**
     * 自社以外の会社カテゴリー
     * @return
     */
    public static List<IEntity> getOutsideCompanyCategory () {
        List<IEntity> list = new ArrayList<>();
        for(Enums.companyCategory ent: Enums.companyCategory.values()) {
            if (ent.getNum() != Enums.companyCategory.OWN.getNum()) {
                SidebarData sidebarData = new SidebarData();
                sidebarData.setId(ent.getNum());
                sidebarData.setParent_id(ent.getNum());
                sidebarData.setText(ent.getStr());
                list.add(sidebarData); 
            }
        }
        return list;
    }
    /**
     * グループ以外の会社カテゴリー
     * @return
     */
    public static List<IEntity> getOutsideGroupCategory () {
        List<IEntity> list = new ArrayList<>();
        for(Enums.companyCategory ent: Enums.companyCategory.values()) {
            if (ent.getNum() != Enums.companyCategory.OWN.getNum() && ent.getNum() != Enums.companyCategory.PARTNER.getNum()) {
                SidebarData sidebarData = new SidebarData();
                sidebarData.setId(ent.getNum());
                sidebarData.setParent_id(ent.getNum());
                sidebarData.setText(ent.getStr());
                list.add(sidebarData); 
            }
        }
        return list;
    }
    /**
     * 会社のリスト
     * @return
     */
    public static List<IEntity> getCompanies() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.company_id as id, c.category_id as parent_id, c.company_name as text, NULLIF('','') as img From companies c");
        sb.append(" WHERE c.state = " + Enums.state.UNDECIDED.getNum());
        return setSidebarData(new SidebarData(), sb.toString());
    }
    /**
     * 支店のリスト
     * @return
     */
    public static List<IEntity> getOffices() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT o.office_id as id, o.company_id as parent_id, o.office_name as text, NULLIF('','') as img From offices o");
        sb.append(" INNER JOIN companies c ON c.company_id = o.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE o.state = " + Enums.state.UNDECIDED.getNum());
        return setSidebarData(new SidebarData(), sb.toString());
    }
}
