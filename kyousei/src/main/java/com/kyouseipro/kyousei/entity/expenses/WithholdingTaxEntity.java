package com.kyouseipro.kyousei.entity.expenses;

import java.sql.ResultSet;

import com.kyouseipro.kyousei.interfacies.IEntity;

import lombok.Data;

@Data
public class WithholdingTaxEntity implements IEntity {
    
    private int withholding_tax_id;
    private int start_pay;
    private int end_pay;
    private int withholding_tax;

    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.withholding_tax_id = rs.getInt("withholding_tax_id");
            this.start_pay = rs.getInt("start_pay");
            this.end_pay = rs.getInt("end_pay");
            this.withholding_tax = rs.getInt("withholding_tax");
        } catch(Exception e) {
            System.out.println(e);
        }
    }   

}
