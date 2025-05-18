package com.kyouseipro.kyousei.entity.corporation;

import java.sql.ResultSet;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IListEntity;

import lombok.Data;

@Data
public class CompanyListEntity implements IListEntity {

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
            this.id = rs.getInt("company_id");
            this.code = rs.getInt("code");
            this.category_id = rs.getInt("category_id");            
            this.full_name = rs.getString("company_name");
            this.full_name_kana = rs.getString("company_name_kana");
            this.category_first = rs.getString("tel_number");
            this.category_second = rs.getString("category_name");
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT c.*, co.* ,cc.company_category_name as category_name, cc.state From companies c");
        sb.append(" INNER JOIN corporations co ON co.corporation_id = c.corporation_id AND co.state = " + Enums.state.INITIAL.getNum());
        sb.append(" LEFT OUTER JOIN company_categories cc ON cc.company_category_id = c.category_id AND cc.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE c.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    @Override
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE c SET c.state = " + Enums.state.DELETE.getNum() + " FROM companies c WHERE 1 = 1");
        return sb.toString();
    }
    
}
