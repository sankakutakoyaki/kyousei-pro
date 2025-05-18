package kyousei.kyousei.entity.employee;

import java.util.List;

import kyousei.kyousei.interfaceis.ICsv;
import kyousei.kyousei.interfaceis.IEntity;

public class StaffCsv implements ICsv {
    public String getCsvString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("担当者番号,");
        sb.append("会社,");
        sb.append("支店,");
        sb.append("姓,");
        sb.append("名,");
        sb.append("せい,");
        sb.append("めい,");
        sb.append("携帯番号,");
        sb.append("メールアドレス,");
        sb.append("\n");
        for (IEntity item : items) {
            StaffEntity entity = (StaffEntity) item;
            sb.append(String.valueOf(entity.getCode()) + ",");
            sb.append(entity.getCompany_name() + ",");
            sb.append(entity.getOffice_name() + ",");
            sb.append(entity.getLast_name() + ",");
            sb.append(entity.getFirst_name() + ",");
            sb.append(entity.getLast_name_kana() + ",");
            sb.append(entity.getFirst_name_kana() + ",");
            sb.append(entity.getPhone_number() + ",");
            sb.append(entity.getEmail() + ",");
            sb.append("\n"); // 改行を追加
        }
        return sb.toString();
    }
}
