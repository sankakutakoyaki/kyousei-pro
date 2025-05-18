package com.kyouseipro.kyousei.entity.recycle;

import java.sql.ResultSet;

import com.kyouseipro.kyousei.interfacies.IEntity;

import lombok.Data;

@Data
public class RecycleCheckEntity implements IEntity {

    private int recycle_id;
    private String recycle_number;

    public void setEntity(ResultSet rs) {
        try {
            this.recycle_id = rs.getInt("recycle_id");
            this.recycle_number = rs.getString("recycle_number");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
