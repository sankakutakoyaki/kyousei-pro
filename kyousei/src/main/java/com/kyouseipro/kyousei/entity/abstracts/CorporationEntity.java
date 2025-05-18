package com.kyouseipro.kyousei.entity.abstracts;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class CorporationEntity implements IFormEntity {

    private int corporation_id;
    private int category_id;

    private String tel_number;
    private String fax_number;
    private String postal_code;
    private String full_address;
    private String email;
    private String web_address;

    private int version;
    private int state;

    public void setEntity(ResultSet rs) {
        try {
            this.corporation_id = rs.getInt("corporation_id");
            this.category_id = rs.getInt("category_id");
            this.tel_number = rs.getString("tel_number");
            this.fax_number = rs.getString("fax_number");
            this.postal_code = rs.getString("postal_code");
            this.full_address = rs.getString("full_address");
            this.email = rs.getString("email");
            this.web_address = rs.getString("web_address");
            this.version = rs.getInt("version");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT c.* From corporations co");
        sb.append(" WHERE co.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @NEW_CODE int; SET @NEW_CODE = ISNULL(@MAX_CODE, 0) + 1;");
        sb.append("INSERT INTO corporations (");
        sb.append("tel_number");
        sb.append(", fax_number");
        sb.append(", postal_code");
        sb.append(", full_address");
        sb.append(", email");
        sb.append(", web_address");
        sb.append(") VALUES (");
        sb.append("'" + this.getTel_number() + "'");
        sb.append(", '" + this.getFax_number() + "'");
        sb.append(", '" + this.getPostal_code() + "'");
        sb.append(", '" + this.getFull_address() + "'");
        sb.append(", '" + this.getEmail() + "'");
        sb.append(", '" + this.getWeb_address() + "'");
        sb.append(");");
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE corporations SET");
        sb.append(" tel_number = '" + this.getTel_number() + "'");
        sb.append(", fax_number = '" + this.getFax_number() + "'");
        sb.append(", postal_code = '" + this.getPostal_code() + "'");
        sb.append(", full_address = '" + this.getFull_address() + "'");
        sb.append(", email = '" + this.getEmail() + "'");
        sb.append(", web_address = '" + this.getWeb_address() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", state = " + this.getState());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE corporation_id = " + this.getCorporation_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }

    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE co SET co.state = " + Enums.state.DELETE.getNum() + " FROM corporations co WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        return "";
    }
}
