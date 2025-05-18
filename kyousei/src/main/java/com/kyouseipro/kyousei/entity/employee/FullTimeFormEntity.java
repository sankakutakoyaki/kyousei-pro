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
public class FullTimeFormEntity extends EmployeeEntity {

    private int fulltime_id;
    // private String start_regular_hours;
    // private String end_regular_hours;
    private int trans_cost;
    private int version;
    private int state;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.fulltime_id = rs.getInt("fulltime_id");
            // if (rs.getTimestamp("start_regular_hours") != null){
            //     LocalDateTime startRegularHours = rs.getTimestamp("start_regular_hours").toLocalDateTime();
            //     this.start_regular_hours= startRegularHours.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            // }
            // if (rs.getTimestamp("end_regular_hours") != null){
            //     LocalDateTime endRegularHours = rs.getTimestamp("end_regular_hours").toLocalDateTime();
            //     this.end_regular_hours= endRegularHours.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            // }
            this.trans_cost = rs.getInt("trans_cost");
            this.version = rs.getInt("version");
            super.setEntity(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT e.*, c.company_name, o.office_name, p.*, f.* From employees e");
        sb.append(" INNER JOIN fulltimes f ON f.employee_id = e.employee_id AND e.state = " + Enums.state.INITIAL.getNum());
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

        sb.append("INSERT INTO fulltimes (");
        sb.append("employee_id");
        sb.append(", trans_cost");
        sb.append(") VALUES (");
        sb.append("@NEW_EMP_ID");
        sb.append(", " + this.getTrans_cost());
        sb.append(");");
        return sb.toString();
    }

    @Override
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE fulltimes SET");
        sb.append(" employee_id = " + this.getEmployee_id());
        sb.append(", trans_cost = " + this.getTrans_cost());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", state = " + this.getState());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE fulltime_id = " + this.getFulltime_id() + " AND version = " + this.getVersion() + ";");
        sb.append(super.getUpdateString());
        return sb.toString();
    }

    @Override
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE f SET f.state = " + Enums.state.DELETE.getNum() + " FROM fulltimes f WHERE 1 = 1");
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
            FullTimeFormEntity entity = (FullTimeFormEntity) item;
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
