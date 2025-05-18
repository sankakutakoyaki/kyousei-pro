// package kyousei.kyousei.repository;

// import java.util.List;

// import org.springframework.stereotype.Repository;

// import kyousei.kyousei.entity.data.SqlData;
// import kyousei.kyousei.interfaceis.IEntity;
// import kyousei.kyousei.interfaceis.ISql;

// // @Repository
// public class SqlRepository implements ISql {
//     /**
//      * エンティティを取得する
//      * @param sqlData
//      * @return
//      */
//     public static IEntity getEntity(SqlData sqlData) {
//         return getSingle(sqlData);
//     }
//     /**
//      * 全てのエンティティを取得する
//      * @param sqlData
//      * @return
//      */
//     public List<IEntity> getEntityList(SqlData sqlData) {
//         return getList(sqlData);
//     }
//     /**
//      * SQLを実行する
//      * @param SQL文
//      * @return 成功した件数を返す
//      */
//     public int excuteSqlString(String str) {
//         return saveEntity(str);
//     }
//     // /**
//     //  * SQLを実行して[id]を返す
//     //  * @param SQL文
//     //  * @return int
//     //  */
//     // public int getEntityId(String str) {
//     //     return getId(str);
//     // }
// }
