package kyousei.kyousei.push;

import java.util.List;

import org.springframework.stereotype.Repository;

import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.interfaceis.IEntity;
import kyousei.kyousei.service.DownloadService;
import kyousei.kyousei.service.SqlService;

@Repository
// @RequiredArgsConstructor
public class PushRepository {

    // private final DownloadService downloadService;
    // private final SqlRepository sqlRepository;

    /**
     * エンドポイントが一致するSubscriptionを取得する
     * @param endpoint
     * @return　見つからなければNullを返す
     */
    public SubscriptionRequest findByEndpoint(String endpoint){
        SubscriptionRequest subscription = new SubscriptionRequest();
        String sqlStr = subscription.getSelectString() + " WHERE endpoint = '" + endpoint + "'";
        SqlData sqldata = DownloadService.createSqlData(subscription, sqlStr, null);
        SubscriptionRequest entity = (SubscriptionRequest)SqlService.getEntity(sqldata);
        if (entity.getSubscription_id() == 0) {
            return null;
        } else {
            return entity;
        }
    }

    /**
     * 未登録のSubscription情報をDBに登録する
     * @param subscription
     * @return
     */
    public int save(SubscriptionRequest subscription){
        String sqlStr = subscription.getSelectString() + " WHERE endpoint = '" + subscription.getEndpoint() + "'";
        SqlData sqllistdata = DownloadService.createSqlData(subscription, sqlStr, null);
        SubscriptionRequest entity = (SubscriptionRequest)SqlService.getEntity(sqllistdata);
        if (entity.getSubscription_id() == 0) {
            return SqlService.excuteSqlString(subscription.getInsertString());
        } else {
            return 0;
        }
    }
    
    /**
     * 登録してあるすべてのSubscriptionを取得する
     * @return
     */
    public List<IEntity> getList() {
        SubscriptionRequest subscription = new SubscriptionRequest();
        SqlData sqllistdata = DownloadService.createSqlData(subscription, subscription.getSelectString(), null);
        return SqlService.getEntityList(sqllistdata);
    }

    /**
     * 指定したEndpointのSubscriptionをDBから削除する
     * @param endpoint
     * @return
     */
    public boolean deleteByEndpoint(String endpoint){
        String sqlStr = "DELETE FROM subscriptions WHERE endpoint = '" + endpoint + "';";
        sqlStr += "DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;SELECT @NEW_ID as number;";
        int result = SqlService.excuteSqlString(sqlStr);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }
}
