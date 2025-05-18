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
public class CompanyFormEntity extends CorporationEntity {

    private int company_id;
    private int category_id;
    private int code;

    private String full_name;
    private String full_name_kana;

    private int version;
    private int state;

    public void setEntity(ResultSet rs) {
        try {
            this.company_id = rs.getInt("company_id");
            this.category_id = rs.getInt("category_id");
            this.code = rs.getInt("code");
            this.full_name = rs.getString("company_name");
            this.full_name_kana = rs.getString("company_name_kana");
            this.version = rs.getInt("version");

            super.setEntity(rs);
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT c.*, co.* From companies c");
        sb.append(" INNER JOIN corporations co ON co.corporation_id = c.corporation_id AND co.state = " + Enums.state.INITIAL.getNum());
        sb.append(" WHERE c.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder("DECLARE @MAX_CODE int = 0;");
        sb.append("SELECT @MAX_CODE = MAX(code) FROM companies WHERE state = " + Enums.state.INITIAL.getNum() + " AND category_id = " + this.getCategory_id() + ";");
        sb.append(super.getInsertString());
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        sb.append("INSERT INTO companies (");
        sb.append("corporation_id");
        sb.append(", category_id");
        sb.append(", code");
        sb.append(", company_name");
        sb.append(", company_name_kana");
        sb.append(") VALUES (");
        sb.append("@NEW_ID");
        sb.append(", " + this.getCategory_id());
        sb.append(", @NEW_CODE");
        sb.append(", '" + this.getFull_name() + "'");
        sb.append(", '" + this.getFull_name_kana() + "'");
        sb.append(");");
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE companies SET");
        sb.append(" category_id = " + this.getCategory_id());
        sb.append(", code = " + this.getCode());
        sb.append(", company_name = '" + this.getFull_name() + "'");
        sb.append(", company_name_kana = '" + this.getFull_name_kana() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", state = " + this.getState());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE company_id = " + this.getCompany_id() + " AND version = " + this.getVersion() + ";");
        sb.append(super.getUpdateString());
        return sb.toString();
    }

    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE c SET c.state = " + Enums.state.DELETE.getNum() + " FROM companies c WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("会社番号,");
        sb.append("会社名,");
        sb.append("かいしゃめい,");
        sb.append("電話番号,");
        sb.append("FAX番号,");
        sb.append("郵便番号,");
        sb.append("住所,");
        sb.append("メールアドレス,");
        sb.append("WEBアドレス,");
        sb.append("\n");
        for (IEntity item : items) {
            CompanyFormEntity entity = (CompanyFormEntity) item;
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
