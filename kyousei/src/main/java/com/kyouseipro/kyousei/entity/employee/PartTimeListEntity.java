package com.kyouseipro.kyousei.entity.employee;

import java.sql.ResultSet;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IListEntity;

import lombok.Data;

@Data
public class PartTimeListEntity implements IListEntity {

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
            this.id = rs.getInt("parttime_id");
            this.code = rs.getInt("code");
            this.category_id = rs.getInt("office_id");            
            this.full_name = rs.getString("full_name");
            this.full_name_kana = rs.getString("full_name_kana");
            this.category_first = rs.getString("phone_number");
            this.category_second = rs.getString("office_name");            
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, c.company_name, o.office_id, o.office_name, p.full_name, p.full_name_kana, p.phone_number, pt.* From employees e");
        sb.append(" INNER JOIN parttimes pt ON pt.employee_id = e.employee_id AND pt.state = " + Enums.state.INITIAL.getNum());
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.INITIAL.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE e.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    @Override
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE pt SET pt.state = " + Enums.state.DELETE.getNum() + " FROM parttimes pt WHERE 1 = 1");
        return sb.toString();
    }
}
