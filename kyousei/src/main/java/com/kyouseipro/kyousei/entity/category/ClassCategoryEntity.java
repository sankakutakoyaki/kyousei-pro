package com.kyouseipro.kyousei.entity.category;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class ClassCategoryEntity implements IFormEntity {

    private int class_category_id;
    private int code;
    private String category_name;
    private int version;
    private int state;

    public void setEntity(ResultSet rs) {
        try {
            this.class_category_id = rs.getInt("class_category_id");
            this.code = rs.getInt("code");
            this.category_name = rs.getString("category_name");
            this.state = rs.getInt("state");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT cl.* From class_categories cl");
        sb.append(" WHERE cl.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO class_categories (");
        sb.append("code");
        sb.append(", category_name");
        sb.append(") VALUES (");
        sb.append(this.getCode());
        sb.append(", '" + this.getCategory_name() + "'");
        sb.append(");");
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE class_categories SET");
        sb.append(" code = " + this.getCode());
        sb.append(", category_name = '" + this.getCategory_name() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE class_category_id = " + this.getClass_category_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }

    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE cl SET cl.state = " + Enums.state.DELETE.getNum() + " FROM class_categories cl WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }
}
