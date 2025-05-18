package kyousei.kyousei.service;

import org.springframework.stereotype.Service;

import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.interfaceis.ICsv;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DownloadService {

    /**
     * [SqlData]に各種情報をセットする
     * @param lisEntity
     * @param str
     * @param csvEntity
     * @return
     */
    public static SqlData createSqlData(IEntity lisEntity, String str, ICsv csvEntity) {
        SqlData sqlData = new SqlData();
        sqlData.setSqlString(str);
        sqlData.setPath(lisEntity);
        sqlData.setCsvPath(csvEntity);
        return sqlData;
    }
}
