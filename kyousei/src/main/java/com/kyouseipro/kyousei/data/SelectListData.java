package com.kyouseipro.kyousei.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.ISql;

import lombok.Data;

@Data
public class SelectListData implements IEntity, ISql {
    
    private int code;
    private int number;
    private int subnumber;
    private String text;
    private int state;

    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.code = rs.getInt("code");
            this.number = rs.getInt("number");
            this.subnumber = rs.getInt("subnumber");
            this.text = rs.getString("text");
            this.state = rs.getInt("state");
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void setData(String str) {
        execSql(s -> {
            try {
                ResultSet rs = s.executeQuery(str);
                if (rs.next()){
                    this.setEntity(rs);                    
                }
                return true;
            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        });
    }

}