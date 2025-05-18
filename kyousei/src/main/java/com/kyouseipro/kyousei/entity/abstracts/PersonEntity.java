package com.kyouseipro.kyousei.entity.abstracts;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class PersonEntity implements IFormEntity {

    protected int person_id;
    protected String last_name;
    protected String first_name;
    protected String full_name;
    protected String last_name_kana;
    protected String first_name_kana;
    protected String full_name_kana;
    protected String phone_number;
    protected String postal_code;
    protected String full_address;
    protected String email;
    protected int gender;
    protected int blood_type;
    protected LocalDate birthday = LocalDate.of(9999, 12, 31);

    public void setEntity(ResultSet rs) {
        try {
            this.person_id = rs.getInt("person_id");
            this.last_name = rs.getString("last_name");
            this.first_name = rs.getString("first_name");
            this.full_name = rs.getString("full_name");
            this.last_name_kana = rs.getString("last_name_kana");
            this.first_name_kana = rs.getString("first_name_kana");
            this.full_name_kana = rs.getString("full_name_kana");
            this.phone_number = rs.getString("phone_number");
            this.postal_code = rs.getString("postal_code");
            this.full_address = rs.getString("full_address");
            this.email = rs.getString("email");
            this.gender = rs.getInt("gender");
            this.blood_type = rs.getInt("blood_type");
            this.birthday = rs.getDate("birthday").toLocalDate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT * From persons WHERE state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @NEW_CODE int; SET @NEW_CODE = ISNULL(@MAX_CODE, 0) + 1;");
        sb.append("INSERT INTO persons (");
        sb.append("last_name");
        sb.append(", first_name");
        sb.append(", last_name_kana");
        sb.append(", first_name_kana");
        sb.append(", phone_number");
        sb.append(", postal_code");
        sb.append(", full_address");
        sb.append(", email");
        sb.append(", gender");
        sb.append(", blood_type");
        sb.append(", birthday");
        sb.append(") VALUES (");
        sb.append("'" + this.getLast_name() + "'");
        sb.append(", '" + this.getFirst_name() + "'");
        sb.append(", '" + this.getLast_name_kana() + "'");
        sb.append(", '" + this.getFirst_name_kana() + "'");
        sb.append(", '" + this.getPhone_number() + "'");
        sb.append(", '" + this.getPostal_code() + "'");
        sb.append(", '" + this.getFull_address() + "'");
        sb.append(", '" + this.getEmail() + "'");
        sb.append(", " + this.getGender());
        sb.append(", " + this.getBlood_type());
        if (this.getBirthday() == null) {
            this.setBirthday(LocalDate.of(9999, 12, 31));
        }
        sb.append(", '" + this.getBirthday() + "'");
        sb.append(");");
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE persons SET");
        sb.append(" last_name = '" + this.getLast_name() + "'");
        sb.append(", first_name = '" + this.getFirst_name() + "'");
        sb.append(", last_name_kana = '" + this.getLast_name_kana() + "'");
        sb.append(", first_name_kana = '" + this.getFirst_name_kana() + "'");
        sb.append(", phone_number = '" + this.getPhone_number() + "'");
        sb.append(", postal_code = '" + this.getPostal_code() + "'");
        sb.append(", full_address = '" + this.getFull_address() + "'");
        sb.append(", email = '" + this.getEmail() + "'");
        sb.append(", gender = " + this.getGender());
        sb.append(", blood_type = " + this.getBlood_type());
        if (this.getBirthday() == null) {
            this.setBirthday(LocalDate.of(9999, 12, 31));
        }
        sb.append(", birthday = '" + this.getBirthday() + "'");
        sb.append(" WHERE person_id = " + this.getPerson_id() + ";");
        return sb.toString();
    }

    public String getDeleteString() {
        return "";
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        return "";
    }
    
}
