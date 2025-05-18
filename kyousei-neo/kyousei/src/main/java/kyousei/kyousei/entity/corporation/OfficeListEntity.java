package kyousei.kyousei.entity.corporation;

import java.sql.ResultSet;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class OfficeListEntity implements IEntity {
    private int office_id;
    private int code;
    private int category_id; // サイドバー選択用に必要
    private String office_name;
    private String office_name_kana;
    private String company_name;
    
    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.office_id = rs.getInt("office_id");
            this.code = rs.getInt("code");
            this.category_id = rs.getInt("company_id");            
            this.office_name = rs.getString("office_name");
            this.office_name_kana = rs.getString("office_name_kana");
            this.company_name = rs.getString("company_name");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public String getOfficeListString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT o.*, c.company_name From offices o");
        sb.append(" INNER JOIN companies c ON c.company_id = o.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE o.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
}