package com.kyouseipro.kyousei.entity.recycle;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;

import lombok.Data;

@Data
public class RecycleEntity implements IFormEntity {

    private int recycle_id;
    private String recycle_number;
    private int recycle_company_id;
    private String recycle_company_name;
    private int recycle_office_id;
    private String recycle_office_name;
    private String recycle_shipper_name;
    private LocalDate recycle_usedate;
    private LocalDate recycle_deliverydate;
    private LocalDate recycle_shippingdate;
    private LocalDate recycle_lossdate;
    private int recycle_maker_code;
    private String recycle_maker_name;
    private int recycle_class_code;
    private String recycle_class_name;    
    private int recycle_shipping_company_id;
    private String recycle_shipping_company_name;
    private int recycle_shipping_office_id;
    private String recycle_shipping_office_name;
    private String recycle_shipping_shipper_name;
    private String recycle_state_str;
    private int recycle_price;
    private int recycle_ex_tax;
    private int version;
    private int state;

    public void setEntity(ResultSet rs) {
        try {
            this.recycle_id = rs.getInt("recycle_id");
            this.recycle_number = rs.getString("recycle_number");
            this.recycle_company_id = rs.getInt("recycle_company_id");
            this.recycle_company_name = rs.getString("recycle_company_name");
            this.recycle_office_id = rs.getInt("recycle_office_id");
            this.recycle_office_name = rs.getString("recycle_office_name");
            // this.recycle_shipper_name = this.getRecycle_company_name() + this.getRecycle_office_name() == "" ? "": " " + this.getRecycle_office_name();
            this.recycle_shipper_name = this.getRecycle_office_name() == null ? this.getRecycle_company_name(): this.getRecycle_company_name() + " " + this.getRecycle_office_name();
            this.recycle_usedate = rs.getDate("recycle_usedate").toLocalDate();
            this.recycle_deliverydate = rs.getDate("recycle_deliverydate").toLocalDate();
            this.recycle_shippingdate = rs.getDate("recycle_shippingdate").toLocalDate();
            this.recycle_lossdate = rs.getDate("recycle_lossdate").toLocalDate();
            this.recycle_maker_code = rs.getInt("recycle_maker_code");
            this.recycle_maker_name = rs.getString("recycle_maker_name");
            this.recycle_class_code = rs.getInt("recycle_class_code");
            this.recycle_class_name = rs.getString("recycle_class_name");            
            this.recycle_shipping_company_id = rs.getInt("recycle_shipping_company_id");
            this.recycle_shipping_company_name = rs.getString("recycle_shipping_company_name");
            this.recycle_shipping_office_id = rs.getInt("recycle_shipping_office_id");
            this.recycle_shipping_office_name = rs.getString("recycle_shipping_office_name");
            this.recycle_shipping_shipper_name = this.getRecycle_shipping_office_name() == null ? this.getRecycle_shipping_company_name(): this.getRecycle_shipping_company_name() + " " + this.getRecycle_shipping_office_name();
            if (!this.recycle_shippingdate.toString().equals("9999-12-31")) {
                this.recycle_state_str = "発送済";
            } else if (!this.recycle_deliverydate.toString().equals("9999-12-31")) {
                this.recycle_state_str = "引渡済";
            } else if (!this.recycle_usedate.toString().equals("9999-12-31")) {
                this.recycle_state_str = "使用済";
            } else {
                this.recycle_state_str = "未使用";
            }
            this.recycle_price = rs.getInt("recycle_price");
            this.recycle_ex_tax = rs.getInt("recycle_ex_tax");
            this.version = rs.getInt("version");
            this.state = rs.getInt("state");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private String selectStrings = "SELECT *" +
                                   ", c.company_name as recycle_company_name, o.office_name as recycle_office_name" +
                                   ", fc.company_name as recycle_shipping_company_name, fo.office_name as recycle_shipping_office_name" +
                                   ", rm.recycle_maker_name, rc.recycle_class_name" +
                                   " FROM recycle r" +
                                   " LEFT OUTER JOIN companies c ON c.company_id = r.recycle_company_id AND c.state = " + Enums.state.INITIAL.getNum() +
                                   " LEFT OUTER JOIN offices o ON o.office_id = r.recycle_office_id AND o.state = " + Enums.state.INITIAL.getNum() +
                                   " LEFT OUTER JOIN companies fc ON fc.company_id = r.recycle_shipping_company_id AND fc.state = " + Enums.state.INITIAL.getNum() +
                                   " LEFT OUTER JOIN offices fo ON fo.office_id = r.recycle_shipping_office_id AND fo.state = " + Enums.state.INITIAL.getNum() +
                                   " LEFT OUTER JOIN recycle_maker rm ON rm.recycle_maker_code = r.recycle_maker_code AND rm.state = " + Enums.state.INITIAL.getNum() +
                                   " LEFT OUTER JOIN recycle_class rc ON rc.recycle_class_code = r.recycle_class_code AND rc.state = " + Enums.state.INITIAL.getNum();

    public String getSelectString() {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE r.state = " + Enums.state.INITIAL.getNum());
        return sb.toString();
    }

    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO recycle (");
        sb.append("recycle_number");
        sb.append(", recycle_company_id");
        sb.append(", recycle_office_id");
        sb.append(", recycle_usedate");
        sb.append(", recycle_deliverydate");
        sb.append(", recycle_shippingdate");
        sb.append(", recycle_lossdate");
        sb.append(", recycle_maker_code");
        sb.append(", recycle_class_code");
        sb.append(", recycle_shipping_company_id");
        sb.append(", recycle_shipping_office_id");
        sb.append(", recycle_price");
        sb.append(", recycle_ex_tax");
        sb.append(") VALUES (");
        sb.append("'" + this.getRecycle_number() + "'");
        sb.append(", " + this.getRecycle_company_id());
        sb.append(", " + this.getRecycle_office_id());
        sb.append(", '" + this.getRecycle_usedate() + "'");
        sb.append(", '" + this.getRecycle_deliverydate() + "'");
        sb.append(", '" + this.getRecycle_shippingdate() + "'");
        sb.append(", '" + this.getRecycle_lossdate() + "'");
        sb.append(", " + this.getRecycle_maker_code());
        sb.append(", " + this.getRecycle_class_code());
        sb.append(", " + this.getRecycle_shipping_company_id());
        sb.append(", " + this.getRecycle_shipping_office_id());
        sb.append(", " + this.getRecycle_price());
        sb.append(", " + this.getRecycle_ex_tax());
        sb.append(");");
        return sb.toString();
    }

    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE recycle SET");
        sb.append(" recycle_number = '" + this.getRecycle_number() + "'");
        sb.append(", recycle_company_id = " + this.getRecycle_company_id());
        sb.append(", recycle_office_id = " + this.getRecycle_office_id());
        sb.append(", recycle_usedate = '" + this.getRecycle_usedate() + "'");
        sb.append(", recycle_deliverydate = '" + this.getRecycle_deliverydate() + "'");
        sb.append(", recycle_shippingdate = '" + this.getRecycle_shippingdate() + "'");
        sb.append(", recycle_lossdate = '" + this.getRecycle_lossdate() + "'");
        sb.append(", recycle_maker_code = " + this.getRecycle_maker_code());
        sb.append(", recycle_class_code = " + this.getRecycle_class_code());
        sb.append(", recycle_shipping_company_id = " + this.getRecycle_shipping_company_id());
        sb.append(", recycle_shipping_office_id = " + this.getRecycle_shipping_office_id());
        sb.append(", recycle_price = " + this.getRecycle_price());
        sb.append(", recycle_ex_tax = " + this.getRecycle_ex_tax());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(" WHERE recycle_id = " + this.getRecycle_id() + " AND version = " + this.getVersion() + ";");
        return sb.toString();
    }

    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE r SET r.state = " + Enums.state.DELETE.getNum() + " FROM recycle r WHERE 1 = 1");
        return sb.toString();
    }

    @Override
    public String getCsvFileString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("お問合せ管理番号,");
        sb.append("小売業者,");
        sb.append("支店,");
        sb.append("使用日,");
        sb.append("引渡日,");
        sb.append("発送日,");
        sb.append("品目コード,");
        sb.append("品目,");
        sb.append("略称コード,");
        sb.append("略称,");        
        sb.append("\n");
        for (IEntity item : items) {
            RecycleEntity entity = (RecycleEntity) item;
            sb.append(String.valueOf(entity.getRecycle_number()) + ",");
            sb.append(entity.getRecycle_company_name() + ",");
            sb.append(entity.getRecycle_office_name() + ",");
            LocalDate localDate = LocalDate.of(9999, 12, 31);
            String usedate = entity.getRecycle_usedate().isEqual(localDate) ? "": entity.getRecycle_usedate().toString();
            sb.append(usedate + ",");
            String deliverydate = entity.getRecycle_deliverydate().isEqual(localDate) ? "": entity.getRecycle_deliverydate().toString();
            sb.append(deliverydate + ",");
            String shippingdate = entity.getRecycle_shippingdate().isEqual(localDate) ? "": entity.getRecycle_shippingdate().toString();
            sb.append(shippingdate + ",");
            sb.append(entity.getRecycle_class_code() + ",");
            sb.append(entity.getRecycle_class_name() + ",");
            sb.append(entity.getRecycle_maker_code() + ",");
            sb.append(entity.getRecycle_maker_name() + ",");
            sb.append("\n"); // 改行を追加
        }
        return sb.toString();
    }
}

