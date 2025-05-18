package kyousei.kyousei.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.entity.recycle.RecycleClassEntity;
import kyousei.kyousei.entity.recycle.RecycleMakerEntity;
import kyousei.kyousei.entity.recycle.RecyclePriceEntity;
import kyousei.kyousei.interfaceis.IEntity;

@Service
// @RequiredArgsConstructor
public class RecycleService {

    // private final SqlRepository sqlRepository;
    // private final DownloadService downloadService;

    /**
     * リサイクルメーカーを全て取得する
     * @return
     */
    public static List<IEntity> getAllRecycleMakerList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM recycle_maker WHERE state = " + Enums.state.UNDECIDED.getNum() + " ORDER BY recycle_maker_code");
        RecycleMakerEntity entity = new RecycleMakerEntity();
        SqlData sqldata = DownloadService.createSqlData(entity, entity.getSelectString(), null);
        return SqlService.getEntityList(sqldata);
    }
    /**
     * リサイクルクラスを全て取得する
     * @return
     */
    public static List<IEntity> getAllRecycleClassList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM recycle_class WHERE state = " + Enums.state.UNDECIDED.getNum());
        RecycleClassEntity entity = new RecycleClassEntity();
        SqlData sqldata = DownloadService.createSqlData(entity, entity.getSelectString(), null);
        return SqlService.getEntityList(sqldata);
    }
    /**
     * リサイクル料金を全て取得する
     * @return
     */
    public static List<IEntity> getAllRecyclePriceList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM recycle_price WHERE state = " + Enums.state.UNDECIDED.getNum());
        RecyclePriceEntity entity = new RecyclePriceEntity();
        SqlData sqldata = DownloadService.createSqlData(entity, entity.getSelectString(), null);
        return SqlService.getEntityList(sqldata);
    }
}
