package com.kyouseipro.kyousei.entity.category;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ItemCategoryService {
    public String createItemCategoryInsertSqlString(List<ItemCategoryEntity> list) {
        if (list == null) return "";

        List<ItemCategoryEntity> categoryInserts = list.stream()
                .filter(item -> item.getCategory_id() == 0 && item.getSubcategory_id() == 0)
                .collect(Collectors.toList());
        List<ItemCategoryEntity> subcategoryInserts = list.stream()
                .filter(item -> item.getCategory_id() > 0 && item.getSubcategory_id() == 0)
                .collect(Collectors.toList());
        List<ItemCategoryEntity> subsubcategoryInserts = list.stream()
                .filter(item -> item.getCategory_id() > 0 && item.getSubcategory_id() > 0).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        if (categoryInserts.size() > 0) {
            categoryInserts.forEach(category -> {
                sb.append(category.getInsertString());
                sb.append("DECLARE @NEW_ID" + category.getItem_category_id() + " int; SET @NEW_ID"
                        + category.getItem_category_id() + " = @@IDENTITY;");

                if (subcategoryInserts != null) {
                    subcategoryInserts.forEach(subcategory -> {
                        sb.append("INSERT INTO item_categories (");
                        sb.append("item_class_id");
                        sb.append(", code");
                        sb.append(", category_id");
                        sb.append(", subcategory_id");
                        sb.append(", category_name");
                        sb.append(") VALUES (");
                        sb.append(subcategory.getItem_class_id());
                        sb.append(", " + subcategory.getCode());
                        if (subcategory.getCategory_id() == category.getItem_category_id()) {
                            sb.append(", @NEW_ID" + category.getItem_category_id());
                        } else {
                            sb.append(", " + subcategory.getCategory_id());
                        }
                        sb.append(", " + subcategory.getSubcategory_id());
                        sb.append(", '" + subcategory.getCategory_name() + "'");
                        sb.append(");");
                        sb.append("DECLARE @NEW_ID" + subcategory.getItem_category_id() + " int; SET @NEW_ID"
                                + subcategory.getItem_category_id() + " = @@IDENTITY;");
                        if (subsubcategoryInserts != null) {
                            subsubcategoryInserts.forEach(subsubcategory -> {
                                sb.append("INSERT INTO item_categories (");
                                sb.append("item_class_id");
                                sb.append(", code");
                                sb.append(", category_id");
                                sb.append(", subcategory_id");
                                sb.append(", category_name");
                                sb.append(") VALUES (");
                                sb.append(subsubcategory.getItem_class_id());
                                sb.append(", " + subsubcategory.getCode());
                                if (subsubcategory.getCategory_id() == category.getItem_category_id()) {
                                    sb.append(", @NEW_ID" + category.getItem_category_id());
                                } else {
                                    sb.append(", " + subsubcategory.getCategory_id());
                                }
                                if (subsubcategory.getCategory_id() == subcategory.getItem_category_id()) {
                                    sb.append(", @NEW_ID" + subcategory.getItem_category_id());
                                } else {
                                    sb.append(", " + subsubcategory.getSubcategory_id());
                                }
                                sb.append(", '" + subsubcategory.getCategory_name() + "'");
                                sb.append(");");
                            });
                        }
                    });
                }
            });
        } else if (subcategoryInserts.size() > 0) {
            subcategoryInserts.forEach(subcategory -> {
                sb.append(subcategory.getInsertString());
                sb.append("DECLARE @NEW_ID" + subcategory.getItem_category_id() + " int; SET @NEW_ID"
                        + subcategory.getItem_category_id() + " = @@IDENTITY;");
                if (subsubcategoryInserts.size() > 0) {
                    subsubcategoryInserts.forEach(subsubcategory -> {
                        sb.append("INSERT INTO item_categories (");
                        sb.append("item_class_id");
                        sb.append(", code");
                        sb.append(", category_id");
                        sb.append(", subcategory_id");
                        sb.append(", category_name");
                        sb.append(") VALUES (");
                        sb.append(subsubcategory.getItem_class_id());
                        sb.append(", " + subsubcategory.getCode());
                        sb.append(", " + subsubcategory.getCategory_id());
                        if (subsubcategory.getSubcategory_id() == subcategory.getItem_category_id()) {
                            sb.append(", @NEW_ID" + subcategory.getItem_category_id());
                        } else {
                            sb.append(", " + subsubcategory.getSubcategory_id());
                        }
                        sb.append(", '" + subsubcategory.getCategory_name() + "'");
                        sb.append(");");
                    });
                }
            });
        } else if (subcategoryInserts.size() > 0) {
            subsubcategoryInserts.forEach(subsubcategory -> {
                sb.append(subsubcategory.getInsertString());
            });
        }
        return sb.toString();
    }
}