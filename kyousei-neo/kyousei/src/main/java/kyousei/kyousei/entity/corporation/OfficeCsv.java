package kyousei.kyousei.entity.corporation;

import java.util.List;

import kyousei.kyousei.interfaceis.ICsv;
import kyousei.kyousei.interfaceis.IEntity;

public class OfficeCsv implements ICsv {
    public String getCsvString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,");
        sb.append("コード,");
        sb.append("会社名,");
        sb.append("支店名,");
        sb.append("してんめい,");
        sb.append("電話番号,");
        sb.append("FAX番号,");
        sb.append("郵便番号,");
        sb.append("住所,");
        sb.append("メールアドレス,");
        sb.append("WEBアドレス,");
        sb.append("\n");
        for (IEntity item : items) {
            OfficeEntity entity = (OfficeEntity) item;
            sb.append(String.valueOf(entity.getOffice_id()) + ",");
            sb.append(String.valueOf(entity.getCode()) + ",");
            sb.append(entity.getCompany_name() + ",");
            sb.append(entity.getOffice_name() + ",");
            sb.append(entity.getOffice_name_kana() + ",");
            sb.append(entity.getTel_number() + ",");
            sb.append(entity.getFax_number() + ",");
            sb.append(entity.getPostal_code() + ",");
            sb.append(entity.getFull_address() + ",");
            sb.append(entity.getEmail() + ",");
            sb.append(entity.getWeb_address() + ",");
            sb.append("\n"); // 改行を追加
        }
        return sb.toString();
    }
}
