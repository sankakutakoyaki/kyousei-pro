package com.kyouseipro.kyousei.entity.recycle;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class RecycleClassEntity  implements IFormEntity {

    private int recycle_class_id;
    private int recycle_class_code;
    private String recycle_class_name;
    private int version;
    private int state;

    public void setEntity(ResultSet rs) {
        try {
            this.recycle_class_id = rs.getInt("recycle_class_id");
            this.recycle_class_code = rs.getInt("recycle_class_code");
            this.recycle_class_name = rs.getString("recycle_class_name");
            this.version = rs.getInt("version");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT rc.* From recycle_class rc");
        sb.append(" WHERE rc.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO recycle_class (");
        sb.append("recycle_class_code");
        sb.append(", recycle_class_name");
        sb.append(") VALUES (");
        sb.append(this.getRecycle_class_code());
        sb.append(", '" + this.getRecycle_class_name() + "'");
        sb.append(");");
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE recycle_class SET");
        sb.append(" recycle_class_code = " + this.getRecycle_class_code());
        sb.append(", recycle_class_name = '" + this.getRecycle_class_name() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE recycle_class_id = " + this.getRecycle_class_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }

    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE rc SET rc.state = " + Enums.state.DELETE.getNum() + " FROM recycle_class rc WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}


