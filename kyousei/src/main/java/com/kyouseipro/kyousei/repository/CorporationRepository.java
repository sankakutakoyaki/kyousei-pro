package com.kyouseipro.kyousei.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.data.SelectListData;
import com.kyouseipro.kyousei.data.SimpleData;
import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.ISql;

@Repository
public class CorporationRepository implements ISql {
    
    /**
     * 協成産業株式会社のIDを取得する
     * @return
     */
    public int getDefaultCompanyId() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ROW_NUMBER() OVER(ORDER BY company_id ASC) code, company_id as number, company_name as text, state FROM companies");
        sb.append(" WHERE company_name = '協成産業株式会社' AND state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new SimpleData());
        sqlData.setSqlString(sb.toString());
        SimpleData entity = (SimpleData) getEntity(sqlData);
        return entity.getNumber();
    }

    /**
     * 全ての会社リストを取得する
     * @return
     */
    public List<IEntity> getAllCompanyListWithSimpleData() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT code, company_id as number, company_name as text, state FROM companies");
        sb.append(" WHERE state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new SimpleData());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * IDで指定したカテゴリーの会社リストを取得する
     * @return
     */
    public List<IEntity> getCompanyListWithSimpleDataByCategoryId(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT code, company_id as number, company_name as text, state FROM companies");
        sb.append(" WHERE state = " + Enums.state.INITIAL.getNum() + " AND category_id = " + id);
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new SimpleData());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * IDで指定したカテゴリーの会社と支店のリストを取得する
     * @param id
     * @return
     */
    public List<IEntity> getOfficeListWithSelectListDataByCategoryId(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT o.code, o.office_id as number, o.office_name as text, c.company_id as subnumber, c.state FROM companies c");
        sb.append(" INNER JOIN offices o ON o.company_id = c.company_id AND o.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE c.state = " + Enums.state.INITIAL.getNum() + " AND c.category_id = " + id);
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new SelectListData());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * IDで指定した会社の支店リストを取得
     * @param id
     * @return
     */
    public List<IEntity> getOfficeListWithSimpleDataByCompanyId(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT code, office_id as number, office_name as text, state FROM offices");
        sb.append(" WHERE state = " + Enums.state.INITIAL.getNum() + " AND company_id = " + id);
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new SimpleData());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }
}
