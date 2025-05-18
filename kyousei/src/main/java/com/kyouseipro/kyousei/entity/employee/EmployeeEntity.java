package com.kyouseipro.kyousei.entity.employee;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.entity.abstracts.PersonEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeEntity extends PersonEntity {

    private int employee_id;
    private int company_id;
    private int office_id;
    private int category_id;    
    private int code;
    private String emergency_contact;
    private String emergency_contact_number;

    private String company_name;
    private String office_name;
    
    private int version;
    private int state;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.employee_id = rs.getInt("employee_id");
            this.company_id = rs.getInt("company_id");
            this.office_id = rs.getInt("office_id");
            this.category_id = rs.getInt("category_id");
            this.code = rs.getInt("code");

            this.company_name = rs.getString("company_name");
            this.office_name = rs.getString("office_name");

            this.emergency_contact = rs.getString("emergency_contact");
            this.emergency_contact_number = rs.getString("emergency_contact_number");
            this.version = rs.getInt("version");

            super.setEntity(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT e.*, c.company_name, o.office_name, p.* From employees e");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.INITIAL.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE e.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    @Override
    public String getInsertString() {
        StringBuilder sb = new StringBuilder("DECLARE @MAX_CODE int = 0;");
        // sb.append("SELECT @MAX_CODE = MAX(code) FROM employees WHERE state = " + Enums.state.INITIAL.getNum() + " AND category_id = " + this.getCategory_id() + ";");
        sb.append("SELECT @MAX_CODE = MAX(code) FROM employees WHERE state = " + Enums.state.INITIAL.getNum() + ";");
        sb.append(super.getInsertString());
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        sb.append("INSERT INTO employees (");
        sb.append("person_id");
        sb.append(", company_id");
        sb.append(", office_id");
        sb.append(", category_id");
        sb.append(", code");
        sb.append(", emergency_contact");
        sb.append(", emergency_contact_number");
        sb.append(") VALUES (");
        sb.append("@NEW_ID");
        sb.append(", " + this.getCompany_id());
        sb.append(", " + this.getOffice_id());
        sb.append(", " + this.getCategory_id());
        sb.append(", @NEW_CODE");
        sb.append(", '" + this.getEmergency_contact() + "'");
        sb.append(", '" + this.getEmergency_contact_number() + "'");
        sb.append(");");
        return sb.toString();
    }

    @Override
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE employees SET");
        sb.append(" code = " + this.getCode());
        sb.append(", company_id = " + this.getCompany_id());
        sb.append(", office_id = " + this.getOffice_id());
        sb.append(", category_id = " + this.getCategory_id());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", emergency_contact = '" + this.getEmergency_contact() + "'");
        sb.append(", emergency_contact_number = '" + this.getEmergency_contact_number() + "'");
        sb.append(", state = " + this.getState());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE employee_id = " + this.getEmployee_id() + " AND version = " + this.getVersion() + ";");
        sb.append(super.getUpdateString());
        return sb.toString();
    }

    @Override
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE e SET e.state = " + Enums.state.DELETE.getNum() + " FROM employees e WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {        
        StringBuilder sb = new StringBuilder();
        sb.append("社員番号,");
        sb.append("姓,");
        sb.append("名,");
        sb.append("せい,");
        sb.append("めい,");
        sb.append("携帯番号,");
        sb.append("郵便番号,");
        sb.append("住所,");
        sb.append("メールアドレス,");
        sb.append("性別,");
        sb.append("血液型,");
        sb.append("生年月日,");
        sb.append("緊急連絡先,");
        sb.append("緊急連絡先番号,");
        sb.append("\n");
        for (IEntity item : items) {
            EmployeeEntity entity = (EmployeeEntity) item;
            sb.append(String.valueOf(entity.getCode()) + ",");
            sb.append(entity.getLast_name() + ",");
            sb.append(entity.getFirst_name() + ",");
            sb.append(entity.getLast_name_kana() + ",");
            sb.append(entity.getFirst_name_kana() + ",");
            sb.append(entity.getPhone_number() + ",");
            sb.append(entity.getPostal_code() + ",");
            sb.append(entity.getFull_address() + ",");
            sb.append(entity.getEmail() + ",");
            sb.append(Enums.gender.getStrByNum(entity.getGender()) + ",");
            sb.append(Enums.bloodType.getStrByNum(entity.getBlood_type()) + ",");
            sb.append(entity.getBirthday() + ",");
            sb.append(entity.getEmergency_contact() + ",");
            sb.append(entity.getEmergency_contact_number() + ",");
            sb.append("\n"); // 改行を追加
        }
        return sb.toString();
    }
}
