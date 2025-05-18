package com.kyouseipro.kyousei.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IListEntity;
import com.kyouseipro.kyousei.interfacies.ISql;

@Repository
public class ListRepository implements ISql {

    /**
     * 検索条件に一致した全てのエンティティを取得する
     * @param sqlData
     * @return
     */
    public List<IEntity> getAllEntitiesThatMatchSearchCiteria(SqlData sqlData) {
        return getList(sqlData);
    }

    /**
     * 検索条件に一致したエンティティを取得する
     * @param sqlData
     * @return
     */
    public IEntity getEntitiyThatMatchSearchCiteria(SqlData sqlData) {
        return getEntity(sqlData);
    }

    /**
     * 選択したIDの検索条件に一致したエンティティを削除する
     * @param sqldata
     * @return
     */
    @SuppressWarnings("deprecation")
    public boolean removeEntitiyThatMatchSelectedIds(SqlData sqldata) {
        try {
            Class<?> c = Class.forName(sqldata.getClassPath());
            IListEntity ent = (IListEntity) c.newInstance();
            String str = ent.getDeleteString() + sqldata.getSqlString();
            return saveEntity(str);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
