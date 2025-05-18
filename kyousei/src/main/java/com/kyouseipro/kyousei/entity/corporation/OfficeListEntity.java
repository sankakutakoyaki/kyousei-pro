package com.kyouseipro.kyousei.entity.corporation;

import java.sql.ResultSet;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IListEntity;

import lombok.Data;

@Data
public class OfficeListEntity implements IListEntity {

    private int id;
    private int code;
    private int category_id;
    private String full_name;
    private String full_name_kana;
    private String category_first;
    private String category_second;    
    
    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.id = rs.getInt("office_id");
            this.code = rs.getInt("code");
            this.category_id = rs.getInt("company_id");
            this.full_name = rs.getString("office_name");
            this.full_name_kana = rs.getString("office_name_kana");
            this.category_first = rs.getString("tel_number");
            this.category_second = rs.getString("company_name");
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT o.*, co.*, c.company_name From offices o");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = o.company_id AND c.state = " + Enums.state.INITIAL.getNum());
        sb.append(" INNER JOIN corporations co ON co.corporation_id = o.corporation_id AND co.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE o.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    @Override
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE o SET o.state = " + Enums.state.DELETE.getNum() + " FROM offices o WHERE 1 = 1");
        return sb.toString();
    }
    
}

