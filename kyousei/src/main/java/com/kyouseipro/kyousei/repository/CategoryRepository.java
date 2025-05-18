package com.kyouseipro.kyousei.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.data.SimpleData;
import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.entity.category.ClassCategoryEntity;
import com.kyouseipro.kyousei.entity.category.ItemCategoryEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.ISql;

@Repository
public class CategoryRepository implements ISql {

    /**
     * アイテムカテゴリーリストのタイトルを取得
     * @return
     */
    public List<IEntity> getItemClassListWithSimpleDataFromClassCategories() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT code, class_category_id as number, category_name as text, state FROM class_categories");
        sb.append(" WHERE state = " + Enums.state.INITIAL.getNum() + " ORDER BY code;");
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new SimpleData());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * アイテムカテゴリーリストを取得
     * @return
     */
    public List<IEntity> getAllItemCategoryList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM item_categories WHERE state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new ItemCategoryEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * クラスカテゴリーリストを取得
     * @return
     */
    public List<IEntity> getAllClassCategoryList() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM class_categories WHERE state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new ClassCategoryEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 指定したアイテムカテゴリーを削除するためのSQL文を作成する（state変更）
     * @param list
     * @return SQL文
     */
    public String getSqlStringForDeleteItemCategories(List<ItemCategoryEntity> list) {
        StringBuilder sb = new StringBuilder("UPDATE ca SET ca.state = " + Enums.state.DELETE.getNum() + " FROM item_categories ca WHERE ca.item_category_id IN ");
        String str = "(";
        for (ItemCategoryEntity entity : list) {
            str += entity.getItem_category_id() + " ,";
        }
        str = str.substring(0, str.length() - 2) + ");";
        return sb.toString() + str ;
    }

    /**
     * 指定したクラスカテゴリーを削除するためのSQL文を作成する（state変更）
     * @param list
     * @return SQL文
     */
    public String getSqlStringForDeleteClassCategories(List<ClassCategoryEntity> list) {
        StringBuilder sb = new StringBuilder("UPDATE cl SET cl.state = " + Enums.state.DELETE.getNum() + " FROM class_categories cl WHERE cl.class_category_id IN ");
        String str = "(";
        for (ClassCategoryEntity entity : list) {
            str += entity.getClass_category_id() + " ,";
        }
        str = str.substring(0, str.length() - 2) + ");";
        return sb.toString() + str ;
    }

    /**
     * 会社カテゴリーリストを取得
     * @return
     */
    public List<IEntity> getCompanyClassListWithSimpleData() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT code, company_category_id as number, company_category_name as text, state FROM company_categories");
        sb.append(" WHERE state = " + Enums.state.INITIAL.getNum());
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new SimpleData());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

}