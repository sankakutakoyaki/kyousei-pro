package com.kyouseipro.kyousei.entity.recycle;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class RecyclePriceEntity implements IFormEntity {

    private int recycle_price_id;
    private int recycle_price_code;
    private int recycle_maker_code;
    private int price;
    private int ex_tax;
    private int version;
    private int state;

    public void setEntity(ResultSet rs) {
        try {
            this.recycle_price_id = rs.getInt("recycle_price_id");
            this.recycle_price_code = rs.getInt("recycle_price_code");
            this.recycle_maker_code = rs.getInt("recycle_maker_code");
            this.price = rs.getInt("price");
            this.ex_tax = rs.getInt("ex_tax");
            this.version = rs.getInt("version");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT rp.* From recycle_price rp");
        sb.append(" WHERE rp.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO recycle_price (");
        sb.append("recycle_price_code");
        sb.append(", recycle_maker_code");
        sb.append(", price");
        sb.append(", ex_tax");
        sb.append(") VALUES (");
        sb.append(this.getRecycle_price_code());
        sb.append(", " + this.getRecycle_maker_code());
        sb.append(", " + this.getPrice());
        sb.append(", " + this.getEx_tax());
        sb.append(");");
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE recycle_price SET");
        sb.append(" recycle_price_code = " + this.getRecycle_price_code());
        sb.append(", recycle_maker_code = " + this.getRecycle_maker_code());
        sb.append(", price = " + this.getPrice());
        sb.append(", ex_tax = " + this.getEx_tax());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE recycle_price_id = " + this.getRecycle_price_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }

    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE rp SET rp.state = " + Enums.state.DELETE.getNum() + " FROM recycle_price rp WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}
