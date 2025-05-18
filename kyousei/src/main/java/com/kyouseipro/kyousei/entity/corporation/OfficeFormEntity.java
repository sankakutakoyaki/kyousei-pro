package com.kyouseipro.kyousei.entity.corporation;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.entity.abstracts.CorporationEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OfficeFormEntity extends CorporationEntity {

    private int office_id;
    private int company_id;
    private int code;

    private String full_name;
    private String full_name_kana;

    private int version;
    private int state;

    public void setEntity(ResultSet rs) {
        try {
            this.office_id = rs.getInt("office_id");
            this.company_id = rs.getInt("company_id");
            this.code = rs.getInt("code");
            this.full_name = rs.getString("office_name");
            this.full_name_kana = rs.getString("office_name_kana");
            this.version = rs.getInt("version");

            super.setEntity(rs);
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT o.*, co.*, c.category_id From offices o");
        sb.append(" INNER JOIN corporations co ON co.corporation_id = o.corporation_id AND co.state = " + Enums.state.INITIAL.getNum());
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = o.company_id AND c.state = " + Enums.state.INITIAL.getNum());        
        sb.append(" WHERE o.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder("DECLARE @MAX_CODE int = 0;");
        sb.append("SELECT @MAX_CODE = MAX(code) FROM offices WHERE state = " + Enums.state.INITIAL.getNum() + ";");
        sb.append(super.getInsertString());
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        sb.append("INSERT INTO offices (");
        sb.append("company_id");
        sb.append(", corporation_id");
        sb.append(", code");
        sb.append(", office_name");
        sb.append(", office_name_kana");
        sb.append(") VALUES (");
        sb.append(this.getCompany_id());
        sb.append(", @NEW_ID");
        sb.append(", @NEW_CODE");
        sb.append(", '" + this.getFull_name() + "'");
        sb.append(", '" + this.getFull_name_kana() + "'");
        sb.append(");");
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE offices SET");
        sb.append(" company_id = " + this.getCompany_id());
        sb.append(", corporation_id = " + this.getCorporation_id());
        sb.append(", code = " + this.getCode());
        sb.append(", office_name = '" + this.getFull_name() + "'");
        sb.append(", office_name_kana = '" + this.getFull_name_kana() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", state = " + this.getState());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE office_id = " + this.getOffice_id() + " AND version = " + this.getVersion() + ";");
        sb.append(super.getUpdateString());
        return sb.toString();
    }

    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE o SET o.state = " + Enums.state.DELETE.getNum() + " FROM offices o WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("支店番号,");
        sb.append("支店名,");
        sb.append("してんめい,");
        sb.append("電話番号,");
        sb.append("FAX番号,");
        sb.append("郵便番号,");
        sb.append("住所,");
        sb.append("メールアドレス,");
        sb.append("WEBアドレス,");
        sb.append("\n");
        for (IEntity item : items) {
            OfficeFormEntity entity = (OfficeFormEntity) item;
            sb.append(String.valueOf(entity.getCode()) + ",");
            sb.append(entity.getFull_name() + ",");
            sb.append(entity.getFull_name_kana() + ",");
            sb.append(entity.getTel_number() + ",");
            sb.append(entity.getFax_number() + ",");
            sb.append(entity.getPostal_code() + ",");
            sb.append(entity.getFull_address() + ",");
            sb.append(entity.getEmail() + ",");
            sb.append(entity.getWeb_address() + ",");
            sb.append("\n"); // 改行を追加
        }
        return sb.toString();
    }
}

