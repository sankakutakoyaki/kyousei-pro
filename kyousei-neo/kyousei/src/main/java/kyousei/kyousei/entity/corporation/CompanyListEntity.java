package kyousei.kyousei.entity.corporation;

import java.sql.ResultSet;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class CompanyListEntity implements IEntity {

    private int company_id;
    private int code;
    private int category_id; // サイドバー選択用に必要
    private String company_name;
    private String company_name_kana;
    private String tel_nuber;
    private String category_name;    
    
    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.company_id = rs.getInt("company_id");
            this.code = rs.getInt("code");
            this.category_id = rs.getInt("category_id");
            this.company_name = rs.getString("company_name");
            this.company_name_kana = rs.getString("company_name_kana");
            this.tel_nuber = rs.getString("tel_number");
            if (this.category_id > 0) {
                this.category_name = Enums.companyCategory.getStrByNum(this.category_id);
            } else {
                this.category_name = "登録なし";
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public String getCompanyListString() {
        StringBuilder sb = new StringBuilder("SELECT c.*, co.tel_number From companies c");
        sb.append(" INNER JOIN corporations co ON co.corporation_id = c.corporation_id");
        sb.append(" WHERE c.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getOutsideCompanyListString() {
        StringBuilder sb = new StringBuilder("SELECT c.*, co.tel_number From companies c");
        sb.append(" INNER JOIN corporations co ON co.corporation_id = c.corporation_id");
        sb.append(" WHERE c.state = " + Enums.state.UNDECIDED.getNum() + " AND NOT(c.category_id = " + Enums.companyCategory.OWN.getNum() + ")");
        return sb.toString();
    }
}
