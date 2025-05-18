package kyousei.kyousei.entity.employee;

import java.util.List;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.ICsv;
import kyousei.kyousei.interfaceis.IEntity;

public class FulltimeCsv implements ICsv {

    public String getCsvString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,");
        sb.append("コード,");
        sb.append("姓,");
        sb.append("名,");
        sb.append("せい,");
        sb.append("めい,");
        sb.append("携帯番号,");
        sb.append("郵便番号,");
        sb.append("住所,");
        sb.append("メールアドレス,");
        sb.append("性別,");
        sb.append("血液型,");
        sb.append("生年月日,");
        sb.append("緊急連絡先,");
        sb.append("緊急連絡先番号,");
        sb.append("\n");
        for (IEntity item : items) {
            FulltimeEntity entity = (FulltimeEntity) item;
            sb.append(String.valueOf(entity.getEmployee_id()) + ",");
            sb.append(String.valueOf(entity.getCode()) + ",");
            sb.append(entity.getLast_name() + ",");
            sb.append(entity.getFirst_name() + ",");
            sb.append(entity.getLast_name_kana() + ",");
            sb.append(entity.getFirst_name_kana() + ",");
            sb.append(entity.getPhone_number() + ",");
            sb.append(entity.getPostal_code() + ",");
            sb.append(entity.getFull_address() + ",");
            sb.append(entity.getEmail() + ",");
            sb.append(Enums.gender.getStrByNum(entity.getGender()) + ",");
            sb.append(Enums.bloodType.getStrByNum(entity.getBlood_type()) + ",");
            sb.append(entity.getBirthday() + ",");
            sb.append(entity.getEmergency_contact() + ",");
            sb.append(entity.getEmergency_contact_number() + ",");
            sb.append("\n"); // 改行を追加
        }
        return sb.toString();
    }
}
