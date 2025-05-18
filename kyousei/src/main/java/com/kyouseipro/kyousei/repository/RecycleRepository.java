package com.kyouseipro.kyousei.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.data.SimpleData;
import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.entity.recycle.RecycleCheckEntity;
import com.kyouseipro.kyousei.entity.recycle.RecycleClassEntity;
import com.kyouseipro.kyousei.entity.recycle.RecycleEntity;
import com.kyouseipro.kyousei.entity.recycle.RecycleMakerEntity;
import com.kyouseipro.kyousei.entity.recycle.RecyclePriceEntity;
import com.kyouseipro.kyousei.entity.recycle.RecycleProductEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.ISql;

@Repository
public class RecycleRepository implements ISql {

    private String selectStrings = "SELECT *" +
                                   ", c.company_name as recycle_company_name, o.office_name as recycle_office_name" +
                                   ", fc.company_name as recycle_shipping_company_name, fo.office_name as recycle_shipping_office_name" +
                                   ", rm.recycle_maker_name, rc.recycle_class_name" +
                                   " FROM recycle r" +
                                   " LEFT OUTER JOIN companies c ON c.company_id = r.recycle_company_id AND c.state = " + Enums.state.INITIAL.getNum() +
                                   " LEFT OUTER JOIN offices o ON o.office_id = r.recycle_office_id AND o.state = " + Enums.state.INITIAL.getNum() +
                                   " LEFT OUTER JOIN companies fc ON fc.company_id = r.recycle_shipping_company_id AND fc.state = " + Enums.state.INITIAL.getNum() +
                                   " LEFT OUTER JOIN offices fo ON fo.office_id = r.recycle_shipping_office_id AND fo.state = " + Enums.state.INITIAL.getNum() +
                                   " LEFT OUTER JOIN recycle_maker rm ON rm.recycle_maker_code = r.recycle_maker_code AND rm.state = " + Enums.state.INITIAL.getNum() +
                                   " LEFT OUTER JOIN recycle_class rc ON rc.recycle_class_code = r.recycle_class_code AND rc.state = " + Enums.state.INITIAL.getNum();

    /**
     * 指定したお問合せ番号の使用済みリサイクル券を取得する
     * @return
     */
    public IEntity getRecycle(String number) {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE r.state = " + Enums.state.INITIAL.getNum() + " AND recycle_number = '" + number + "'");
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleEntity());
        sqlData.setSqlString(sb.toString());
        return getEntity(sqlData);
    }

    /**
     * 使用済みリサイクル券を全て取得する
     * @return
     */
    public List<IEntity> getAllRecycleList() {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE r.state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }


    /**
     * 指定した日付の使用済みリサイクル券を全て取得する
     * @return
     */
    public List<IEntity> getRecycleList(int dateclass, LocalDate date) {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE r.state = " + Enums.state.INITIAL.getNum());
        switch (dateclass) {
            case 1:
                sb.append(" AND r.recycle_usedate = '" + date + "' AND r.recycle_deliverydate = '9999-12-31' AND r.recycle_shippingdate = '9999-12-31'");
                break;
            case 2:
                sb.append(" AND r.recycle_deliverydate = '" + date + "' AND r.recycle_shippingdate = '9999-12-31'");
                break;
            case 3:
                sb.append(" AND r.recycle_shippingdate = '" + date + "'");
                break;
            case 4:
                sb.append(" AND r.recycle_lossdate = '" + date + "'");
                break;
            default:
                break;
        }
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 指定した日付の使用済みリサイクル券を全て取得する
     * @return
     */
    public List<IEntity> getRecycleListInputToday(int dateclass) {
        String date = LocalDate.now().toString();
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE r.state = " + Enums.state.INITIAL.getNum());
        sb.append(" AND CAST(r.update_date AS DATE) = '" + date + "'");
        switch (dateclass) {
            case 1:
                sb.append(" AND NOT (r.recycle_usedate = '9999-12-31')");
                break;
            case 2:
                sb.append(" AND NOT (r.recycle_deliverydate = '9999-12-31')");
                break;
            case 3:
                sb.append(" AND NOT (r.recycle_shippingdate = '9999-12-31')");
                break;
            case 4:
                sb.append(" AND NOT (r.recycle_lossdate = '9999-12-31')");
                break;
            default:
                break;
        }
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 指定した期間の使用済みリサイクル券を全て取得する
     * @return
     */
    public List<IEntity> getBetweenRecycleList(int dateclass, LocalDate start, LocalDate end) {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE r.state = " + Enums.state.INITIAL.getNum());
        switch (dateclass) {
            case 1:
                sb.append(" AND r.recycle_usedate BETWEEN '" + start + "' AND '" + end + "'");
                break;
            case 2:
                sb.append(" AND r.recycle_deliverydate BETWEEN '" + start + "' AND '" + end + "'");
                break;
            case 3:
                sb.append(" AND r.recycle_lossdate BETWEEN '" + start + "' AND '" + end + "'");
                break;
            case 9:
                sb.append(" AND CAST(r.update_date AS DATE) BETWEEN '" + start + "' AND '" + end + "'");
                break;
            default:
                break;
        }
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 検索条件に一致した使用済みリサイクル券を全て取得する
     * @return
     */
    public List<IEntity> getSearchRecycleList(String searchText) {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE r.state = " + Enums.state.INITIAL.getNum());
        sb.append(searchText);
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 指定した使用済みリサイクル券を削除するためのSQL文を作成する（state変更）
     * @param list
     * @return
     */
    public String getSqlStringForDeleteRecycle(List<RecycleEntity> list) {
        StringBuilder sb = new StringBuilder("UPDATE r SET r.state = " + Enums.state.DELETE.getNum() + " FROM recycle r WHERE r.recycle_id IN ");
        String str = "(";
        for (RecycleEntity entity : list) {
            str += entity.getRecycle_id() + " ,";
        }
        str = str.substring(0, str.length() - 2) + ");";
        return sb.toString() + str ;
    }

    /**
     * リサイクルメーカーを全て取得する
     * @return
     */
    public List<IEntity> getAllRecycleMakerList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM recycle_maker WHERE state = " + Enums.state.INITIAL.getNum() + " ORDER BY recycle_maker_code");
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleMakerEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 指定したリサイクルメーカーを削除するためのSQL文を作成する（state変更）
     * @param list
     * @return
     */
    public String getSqlStringForDeleteRecycleMaker(List<RecycleMakerEntity> list) {
        StringBuilder sb = new StringBuilder("UPDATE rm SET rm.state = " + Enums.state.DELETE.getNum() + " FROM recycle_maker rm WHERE rm.recycle_maker_id IN ");
        String str = "(";
        for (RecycleMakerEntity entity : list) {
            str += entity.getRecycle_maker_id() + " ,";
        }
        str = str.substring(0, str.length() - 2) + ");";
        return sb.toString() + str ;
    }

    /**
     * 製造業者を全て取得する
     * @return
     */
    public List<IEntity> getAllRecycleProductList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM recycle_product WHERE state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleProductEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 指定した製造業者を削除するためのSQL文を作成する（state変更）
     * @param list
     * @return
     */
    public String getSqlStringForDeleteRecycleProduct(List<RecycleProductEntity> list) {
        StringBuilder sb = new StringBuilder("UPDATE rp SET rp.state = " + Enums.state.DELETE.getNum() + " FROM recycle_product rp WHERE rp.recycle_product_id IN ");
        String str = "(";
        for (RecycleProductEntity entity : list) {
            str += entity.getRecycle_product_id() + " ,";
        }
        str = str.substring(0, str.length() - 2) + ");";
        return sb.toString() + str ;
    }

    /**
     * リサイクルクラスを全て取得する
     * @return
     */
    public List<IEntity> getAllRecycleClassList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM recycle_class WHERE state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleClassEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * リサイクル料金を全て取得する
     * @return
     */
    public List<IEntity> getAllRecyclePriceList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM recycle_price WHERE state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecyclePriceEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 指定したリサイクル料金を削除するためのSQL文を作成する（state変更）
     * @param list
     * @return
     */
    public String getSqlStringForDeleteRecyclePrice(List<RecyclePriceEntity> list) {
        StringBuilder sb = new StringBuilder("UPDATE rp SET rp.state = " + Enums.state.DELETE.getNum() + " FROM recycle_price rp WHERE rp recycle_price_id IN ");
        String str = "(";
        for (RecyclePriceEntity entity : list) {
            str += entity.getRecycle_price_id() + " ,";
        }
        str = str.substring(0, str.length() - 2) + ");";
        return sb.toString() + str ;
    }

    /**
     * 指定したコードの製造業者を取得
     * @param code
     * @return
     */
    public IEntity getRecycleMakerByCode(int code) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM recycle_maker WHERE recycle_maker_code = " + code + " AND state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleMakerEntity());
        sqlData.setSqlString(sb.toString());
        return getEntity(sqlData);
    }

    /**
     * 会社リストを取得
     * @param id
     * @return
     */
    public List<IEntity> getShipperListWithSimpleData() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT code, company_id as number, company_name as text, state FROM companies");
        sb.append(" WHERE category_id IN (" + Enums.companyCategory.OWN.getNum() + ", " + Enums.companyCategory.SHIPPER.getNum() + ") AND state = " + Enums.state.INITIAL.getNum());
        sb.append(" ORDER BY company_id, code;");
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new SimpleData());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 支店リストを取得
     * @param id
     * @return
     */
    public List<IEntity> getOfficeListWithSimpleData(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT code, office_id as number, office_name as text, state FROM offices");
        sb.append(" WHERE company_id = " + id + " AND state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new SimpleData());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * お問合せ管理票番号が重複してるか調べる
     * @param number
     * @return
     */
    public boolean checkDuplicateRecycleNumbers(String number) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT recycle_id, recycle_number FROM recycle");
        sb.append(" WHERE recycle_number = '" + number + "' AND state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleCheckEntity());
        sqlData.setSqlString(sb.toString());
        IEntity entity = getEntity(sqlData);
        if (entity != null && ((RecycleCheckEntity)entity).getRecycle_id() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * お問合せ管理票番号が引渡し処理されているか調べる
     * @param number
     * @return
     */
    public boolean checkDuplicateRecycleDeliveryNumbers(String number) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT recycle_id, recycle_number FROM recycle");
        sb.append(" WHERE recycle_number = '" + number + "' AND NOT(recycle_deliverydate = '9999-12-31')");
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleCheckEntity());
        sqlData.setSqlString(sb.toString());
        IEntity entity = getEntity(sqlData);
        if (entity != null && ((RecycleCheckEntity)entity).getRecycle_id() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * お問合せ管理票番号が引渡し処理されているか調べる
     * @param number
     * @return
     */
    public boolean checkDuplicateRecycleForwardNumbers(String number) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT recycle_id, recycle_number FROM recycle");
        sb.append(" WHERE recycle_number = '" + number + "' AND NOT(recycle_shippingdate = '9999-12-31')");
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleCheckEntity());
        sqlData.setSqlString(sb.toString());
        IEntity entity = getEntity(sqlData);
        if (entity != null && ((RecycleCheckEntity)entity).getRecycle_id() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * お問合せ管理票番号がロス処理されているか調べる
     * @param number
     * @return
     */
    public boolean checkDuplicateRecycleLossNumbers(String number) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT recycle_id, recycle_number FROM recycle_loss");
        sb.append(" WHERE recycle_number = '" + number + "'");
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new RecycleCheckEntity());
        sqlData.setSqlString(sb.toString());
        IEntity entity = getEntity(sqlData);
        if (entity != null && ((RecycleCheckEntity)entity).getRecycle_id() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 使用日を登録するためのSQL文を作成する
     * @param list
     * @return
     */
    public String getSqlStringForRegistRecycleUseDate(RecycleEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE recycle SET");
        sb.append(" recycle_company_id = " + entity.getRecycle_company_id());
        sb.append(", recycle_office_id = " + entity.getRecycle_office_id());
        sb.append(", recycle_usedate = '" + entity.getRecycle_usedate() + "'");
        sb.append(", recycle_class_code = " + entity.getRecycle_class_code());
        sb.append(", recycle_maker_code = " + entity.getRecycle_maker_code());
        sb.append(", recycle_price = " + entity.getRecycle_price());
        sb.append(", recycle_ex_tax = " + entity.getRecycle_ex_tax());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(" WHERE recycle_id = " + entity.getRecycle_id() + " AND state = " + Enums.state.INITIAL.getNum()); 
        return sb.toString();
    }

    /**
     * 発送日を登録するためのSQL文を作成する
     * @param list
     * @return
     */
    // public String getSqlStringForRegistRecycleShippingDate(List<RecycleEntity> list) {
    //     StringBuilder sb = new StringBuilder();
    //     for (RecycleEntity entity : list) {
    //         sb.append("UPDATE recycle SET");
    //         sb.append(" recycle_shipping_company_id = " + entity.getRecycle_shipping_company_id());
    //         sb.append(", recycle_shipping_office_id = " + entity.getRecycle_shipping_office_id());
    //         sb.append(", recycle_shippingdate = '" + entity.getRecycle_shippingdate() + "'");
    //         sb.append(", update_date = '" + LocalDateTime.now() + "'");
    //         sb.append(" WHERE recycle_number = '" + entity.getRecycle_number() + "' AND state = " + Enums.state.INITIAL.getNum());            
    //     }
    //     return sb.toString();
    public String getSqlStringForRegistRecycleShippingDate(RecycleEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE recycle SET");
        sb.append(" recycle_shipping_company_id = " + entity.getRecycle_shipping_company_id());
        sb.append(", recycle_shipping_office_id = " + entity.getRecycle_shipping_office_id());
        sb.append(", recycle_shippingdate = '" + entity.getRecycle_shippingdate() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(" WHERE recycle_id = " + entity.getRecycle_id() + " AND state = " + Enums.state.INITIAL.getNum()); 
        return sb.toString();
    }

    /**
     * 引渡日を登録するためのSQL文を作成する
     * @param list
     * @return
     */
    // public String getSqlStringForRegistRecycleDeliveryDate(List<RecycleEntity> list) {
    //     StringBuilder sb = new StringBuilder();
    //     for (RecycleEntity entity : list) {
    //         sb.append("UPDATE recycle SET");
    //         sb.append(" recycle_deliverydate = '" + entity.getRecycle_deliverydate() + "'");
    //         sb.append(", update_date = '" + LocalDateTime.now() + "'");
    //         sb.append(" WHERE recycle_number = '" + entity.getRecycle_number() + "' AND state = " + Enums.state.INITIAL.getNum());            
    //     }
    //     return sb.toString();
    // }
    public String getSqlStringForRegistRecycleDeliveryDate(RecycleEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE recycle SET");
        sb.append(" recycle_deliverydate = '" + entity.getRecycle_deliverydate() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(" WHERE recycle_id = " + entity.getRecycle_id() + " AND state = " + Enums.state.INITIAL.getNum());    
        return sb.toString();
    }

    /**
     * ロス処理日を登録するためのSQL文を作成する
     * @param list
     * @return
     */
    public String getSqlStringForRegistRecycleLossDate(List<RecycleEntity> list) {
        StringBuilder sb = new StringBuilder();
        for (RecycleEntity entity : list) {
            sb.append("UPDATE recycle SET");
            sb.append(" recycle_lossdate = '" + entity.getRecycle_lossdate() + "'");
            sb.append(", state = " + Enums.state.DELETE.getNum());
            sb.append(" WHERE recycle_id = " + entity.getRecycle_id() + ";");
            sb.append("INSERT INTO recycle_loss (");
            sb.append("recycle_id");
            sb.append(", recycle_number");
            sb.append(") VALUES (");
            sb.append(entity.getRecycle_id());
            sb.append(", '" + entity.getRecycle_number() + "'");
            sb.append(");");
        }
        return sb.toString();
    }
}
