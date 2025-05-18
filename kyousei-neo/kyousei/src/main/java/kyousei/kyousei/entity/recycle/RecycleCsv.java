package kyousei.kyousei.entity.recycle;

import java.util.List;

import kyousei.kyousei.interfaceis.ICsv;
import kyousei.kyousei.interfaceis.IEntity;

public class RecycleCsv implements ICsv {
    public String getCsvString(List<IEntity> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,");
        sb.append("お問合せ管理票番号,");
        sb.append("使用日,");
        sb.append("引渡日,");
        sb.append("小売業者,");
        sb.append("小売業者支店,");
        sb.append("品目コード,");
        sb.append("品目,");
        sb.append("製造業者等名コード,");
        sb.append("製造業者等名,");
        sb.append("リサイクル料金(税込),");
        sb.append("リサイクル料金(税別),");
        sb.append("\n");
        for (IEntity item : items) {
            RecycleListEntity entity = (RecycleListEntity) item;
            sb.append(String.valueOf(entity.getRecycle_id()) + ",");
            sb.append(entity.getRecycle_number() + ",");
            sb.append(entity.getUse_date() + ",");
            sb.append(entity.getDelivery_date() + ",");
            sb.append(entity.getUse_company_name() + ",");
            sb.append(entity.getUse_office_name() + ",");
            sb.append(entity.getClass_code() + ",");
            sb.append(entity.getClass_name() + ",");
            sb.append(entity.getMaker_code() + ",");
            sb.append(entity.getMaker_name() + ",");
            sb.append(entity.getPrice() + ",");
            sb.append(entity.getEx_tax() + ",");
            sb.append("\n"); // 改行を追加
        }
        return sb.toString();
    }
}