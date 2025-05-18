package com.kyouseipro.kyousei.entity.employee;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StaffFormEntity extends EmployeeEntity {

    private int staff_id;

    private int version;
    private int state;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.staff_id = rs.getInt("staff_id");
            this.version = rs.getInt("version");
            super.setEntity(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, c.company_name, o.office_name, p.*, st.* From employees e");
        sb.append(" INNER JOIN staffs st ON st.employee_id = e.employee_id AND st.state = " + Enums.state.INITIAL.getNum());
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.INITIAL.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE e.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    @Override
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInsertString());
        sb.append("DECLARE @NEW_EMP_ID int; SET @NEW_EMP_ID = @@IDENTITY;");

        sb.append("INSERT INTO staffs (");
        sb.append("employee_id");
        sb.append(") VALUES (");
        sb.append("@NEW_EMP_ID");
        sb.append(");");
        return sb.toString();
    }

    @Override
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE staffs SET");
        sb.append(" employee_id = " + this.getEmployee_id());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", state = " + this.getState());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE staff_id = " + this.getStaff_id() + " AND version = " + this.getVersion() + ";");
        sb.append(super.getUpdateString());
        return sb.toString();
    }

    @Override
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE st SET st.state = " + Enums.state.DELETE.getNum() + " FROM staffs st WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("担当者番号,");
        sb.append("会社,");
        sb.append("支店,");
        sb.append("姓,");
        sb.append("名,");
        sb.append("せい,");
        sb.append("めい,");
        sb.append("携帯番号,");
        sb.append("メールアドレス,");
        sb.append("\n");
        for (IEntity item : items) {
            StaffFormEntity entity = (StaffFormEntity) item;
            sb.append(String.valueOf(entity.getCode()) + ",");
            sb.append(entity.getCompany_name() + ",");
            sb.append(entity.getOffice_name() + ",");
            sb.append(entity.getLast_name() + ",");
            sb.append(entity.getFirst_name() + ",");
            sb.append(entity.getLast_name_kana() + ",");
            sb.append(entity.getFirst_name_kana() + ",");
            sb.append(entity.getPhone_number() + ",");
            sb.append(entity.getEmail() + ",");
            sb.append("\n"); // 改行を追加
        }
        return sb.toString();
    }

}

