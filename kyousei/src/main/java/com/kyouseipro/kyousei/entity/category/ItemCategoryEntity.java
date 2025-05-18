package com.kyouseipro.kyousei.entity.category;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class ItemCategoryEntity implements IFormEntity {

    private int item_category_id;
    private int item_class_id;
    private int code;
    private int category_id;
    private int subcategory_id;
    private String category_name;
    private int version;
    private int state;

    public void setEntity(ResultSet rs) {
        try {
            this.item_category_id = rs.getInt("item_category_id");
            this.item_class_id = rs.getInt("item_class_id");
            this.code = rs.getInt("code");
            this.category_id = rs.getInt("category_id");
            this.subcategory_id = rs.getInt("subcategory_id");            
            this.category_name = rs.getString("category_name");
            this.version = rs.getInt("version");
            this.state = rs.getInt("state");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT ca.* From item_categories ca");
        sb.append(" WHERE ca.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO item_categories (");
        sb.append("item_class_id");
        sb.append(", code");
        sb.append(", category_id");
        sb.append(", subcategory_id");
        sb.append(", category_name");
        sb.append(") VALUES (");
        sb.append(this.getItem_class_id());
        sb.append(", " + this.getCode());
        sb.append(", " + this.getCategory_id());
        sb.append(", " + this.getSubcategory_id());
        sb.append(", '" + this.getCategory_name() + "'");
        sb.append(");");
        
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE item_categories SET");
        sb.append(" item_class_id = " + this.getItem_class_id());
        sb.append(", code = " + this.getCode());
        sb.append(", category_id = " + this.getCategory_id());
        sb.append(", subcategory_id = " + this.getSubcategory_id());
        sb.append(", category_name = '" + this.getCategory_name() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE item_category_id = " + this.getItem_category_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }

    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ca SET ca.state = " + Enums.state.DELETE.getNum() + " FROM item_categories ca WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        return "";
    }
}
