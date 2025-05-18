package kyousei.kyousei.entity.data;

import kyousei.kyousei.interfaceis.ICsv;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class SqlData {

    private String sqlString = "";
    private String classPath = "";
    private String csvClassPath = "";

    /**
     * 主エンティティのパスを取得してセットする
     * @param entity
     */
    public void setPath(IEntity entity) {
        if (entity == null) return;
        this.classPath = entity.getClass().getName();
    }
    /**
     * CSVファイル用のエンティティのパスを取得してセットする
     * @param entity
     */
    public void setCsvPath(ICsv entity) {
        if (entity == null) return;
        this.csvClassPath = entity.getClass().getName();
    }
}
