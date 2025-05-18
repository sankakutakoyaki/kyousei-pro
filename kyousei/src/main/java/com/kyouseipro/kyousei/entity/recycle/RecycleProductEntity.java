package com.kyouseipro.kyousei.entity.recycle;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class RecycleProductEntity implements IFormEntity {

    private int recycle_product_id;
    private int code;
    private int recycle_maker_code;
    private String recycle_product_name;
    private int version;
    private int state;

    public void setEntity(ResultSet rs) {
        try {
            this.recycle_product_id = rs.getInt("recycle_product_id");
            this.code = rs.getInt("code");
            this.recycle_maker_code = rs.getInt("recycle_maker_code");
            this.recycle_product_name = rs.getString("recycle_product_name");
            this.version = rs.getInt("version");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT rp.* From recycle_product rp");
        sb.append(" WHERE rp.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO recycle_product (");
        sb.append("code");
        sb.append(", recycle_maker_code");
        sb.append(", recycle_product_name");
        sb.append(") VALUES (");
        sb.append(this.getCode());
        sb.append(", " + this.getRecycle_maker_code());
        sb.append(", '" + this.getRecycle_product_name() + "'");
        sb.append(");");
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE recycle_product SET");
        sb.append(" code = " + this.getCode());
        sb.append(", recycle_maker_code = " + this.getRecycle_maker_code());
        sb.append(", recycle_product_name = '" + this.getRecycle_product_name() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE recycle_product_id = " + this.getRecycle_product_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }

    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE rp SET rp.state = " + Enums.state.DELETE.getNum() + " FROM recycle_product rp WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}


