package com.kyouseipro.kyousei.entity.recycle;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class RecycleMakerEntity implements IFormEntity {

    private int recycle_maker_id;
    private int recycle_maker_code;
    private String recycle_maker_name;
    private String recycle_maker_group;
    private int version;
    private int state;

    public void setEntity(ResultSet rs) {
        try {
            this.recycle_maker_id = rs.getInt("recycle_maker_id");
            this.recycle_maker_code = rs.getInt("recycle_maker_code");
            this.recycle_maker_name = rs.getString("recycle_maker_name");
            this.recycle_maker_group = rs.getString("recycle_maker_group");
            this.version = rs.getInt("version");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT rm.* From recycle_maker rm");
        sb.append(" WHERE rm.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO recycle_maker (");
        sb.append("recycle_maker_code");
        sb.append(", recycle_maker_name");
        sb.append(", recycle_maker_group");
        sb.append(") VALUES (");
        sb.append(this.getRecycle_maker_code());
        sb.append(", '" + this.getRecycle_maker_name() + "'");
        sb.append(", '" + this.getRecycle_maker_group() + "'");
        sb.append(");");
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE recycle_maker SET");
        sb.append(" recycle_maker_code = " + this.getRecycle_maker_code());
        sb.append(", recycle_maker_name = '" + this.getRecycle_maker_name() + "'");
        sb.append(", recycle_maker_group = '" + this.getRecycle_maker_group() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE recycle_maker_id = " + this.getRecycle_maker_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }

    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE rm SET rm.state = " + Enums.state.DELETE.getNum() + " FROM recycle_maker rm WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}

