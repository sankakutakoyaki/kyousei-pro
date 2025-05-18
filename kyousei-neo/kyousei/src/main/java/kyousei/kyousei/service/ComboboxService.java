package kyousei.kyousei.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.corporation.OfficeEntity;
import kyousei.kyousei.entity.data.SimpleData;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.interfaceis.IEntity;

@Service
// @RequiredArgsConstructor
public class ComboboxService {
    // private final SqlRepository sqlRepository;

    /**
     * [SimpleData]のリストを作成
     * @param entity
     * @param str
     * @return
     */
    private static List<IEntity> setSqlDataList(IEntity entity, String str) {
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
        sb.append("SELECT o.office_id as number, o.office_name as text From offices o");
        sb.append(" INNER JOIN companies c ON c.company_id = o.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE o.state = " + Enums.state.UNDECIDED.getNum() + " AND c.category_id = " + Enums.companyCategory.OWN.getNum());
        return setSqlDataList(new SimpleData(), sb.toString());
    }
    /**
     * パートナー会社のリスト
     * @return
     */
    public static List<IEntity> getPartnerCompanies() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.company_id as number, c.company_name as text From companies c");
        sb.append(" WHERE c.state = " + Enums.state.UNDECIDED.getNum() + " AND c.category_id = " + Enums.companyCategory.PARTNER.getNum());
        return setSqlDataList(new SimpleData(), sb.toString());
    }
    /**
     * 小売業者のリスト(自社含む)
     * @return
     */
    public static List<IEntity> getRetailerCompaniesWithOwn() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.company_id as number, c.company_name as text From companies c");
        sb.append(" WHERE c.state = " + Enums.state.UNDECIDED.getNum() + " AND (c.category_id = " + Enums.companyCategory.OWN.getNum() + " OR " + " c.category_id = " + Enums.companyCategory.CLIENT.getNum() + ")");
        return setSqlDataList(new SimpleData(), sb.toString());
    }
    /**
     * 小売業者のリスト(自社含まず)
     * @return
     */
    public static List<IEntity> getRetailerCompanies() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.company_id as number, c.company_name as text From companies c");
        sb.append(" WHERE c.state = " + Enums.state.UNDECIDED.getNum() + " AND (c.category_id = " + Enums.companyCategory.CLIENT.getNum() + ")");
        return setSqlDataList(new SimpleData(), sb.toString());
    }
    /**
     * 会社ID付きの支店リスト（主にコンボボックスで使用）
     * @return
     */
    public static List<IEntity> getOfficesWithCompanyId() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT o.*, co.*, c.company_name From offices o");
        sb.append(" INNER JOIN corporations co ON co.corporation_id = o.corporation_id");
        sb.append(" INNER JOIN companies c ON c.company_id = o.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" ORDER BY o.office_name_kana");
        return setSqlDataList(new OfficeEntity(), sb.toString());
    }
    /**
     * 性別のリスト
     * @return
     */
    public static List<IEntity> getGender() {
        List<IEntity> list = new ArrayList<>();
        for(Enums.gender ent: Enums.gender.values()) {
            SimpleData simpleData = new SimpleData();
            simpleData.setNumber(ent.getNum());
            simpleData.setText(ent.getStr());
            list.add(simpleData);
        }
        return list;
    }
    /**
     * 血液型のリスト
     * @return
     */
    public static List<IEntity> getBloodType() {
        List<IEntity> list = new ArrayList<>();
        for(Enums.bloodType ent: Enums.bloodType.values()) {
            SimpleData simpleData = new SimpleData();
            simpleData.setNumber(ent.getNum());
            simpleData.setText(ent.getStr());
            list.add(simpleData);
        }
        return list;
    }
    /**
     * 支払い方法のリスト
     * @return
     */
    public static List<IEntity> getPaymentMethod() {
        List<IEntity> list = new ArrayList<>();
        for(Enums.paymentMethod ent: Enums.paymentMethod.values()) {
            SimpleData simpleData = new SimpleData();
            simpleData.setNumber(ent.getNum());
            simpleData.setText(ent.getStr());
            list.add(simpleData);
        }
        return list;
    }
    /**
     * 支払い方法のリスト
     * @return
     */
    public static List<IEntity> getOutsideCompanyCategory() {
        List<IEntity> list = new ArrayList<>();
        for(Enums.companyCategory ent: Enums.companyCategory.values()) {
            if (ent.getNum() != Enums.companyCategory.OWN.getNum()) {
                SimpleData simpleData = new SimpleData();
                simpleData.setNumber(ent.getNum());
                simpleData.setText(ent.getStr());
                list.add(simpleData);
            }
        }
        return list;
    }
}
