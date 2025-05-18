package com.kyouseipro.kyousei.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.entity.category.ClassCategoryEntity;
import com.kyouseipro.kyousei.entity.category.ItemCategoryEntity;
import com.kyouseipro.kyousei.entity.category.ItemCategoryService;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.repository.CategoryRepository;
import com.kyouseipro.kyousei.repository.ListRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final ItemCategoryService itemCategoryService;
    private final ListRepository listRepository;

    @GetMapping("/category/item/get")
    @ResponseBody
    public List<IEntity> getAllItemCategoryRegistList() {
        return categoryRepository.getAllItemCategoryList();
    }

    @GetMapping("/category/class/get")
    @ResponseBody
    public List<IEntity> getAllClassCategoryRegistList() {
        return categoryRepository.getAllClassCategoryList();
    }

    @PostMapping("/category/item/save")
    @ResponseBody
    public boolean saveItemCategories(@RequestBody List<ItemCategoryEntity> list) {
        StringBuilder sb = new StringBuilder();
        // 新規作成SQL文作成
        List<ItemCategoryEntity> inserts = list.stream().filter(item -> item.getState() == Enums.state.CREATE.getNum()).collect(Collectors.toList());
        if (inserts.size() > 0) sb.append(itemCategoryService.createItemCategoryInsertSqlString(inserts));
        // 更新SQL文作成
        List<ItemCategoryEntity> updates = list.stream().filter(item -> item.getState() == Enums.state.UPDATE.getNum()).collect(Collectors.toList());
        if (updates.size() > 0) updates.forEach(item -> sb.append(item.getUpdateString()));        
        // 削除用SQL文作成
        List<ItemCategoryEntity> deletes = list.stream().filter(item -> item.getState() == Enums.state.DELETE.getNum()).collect(Collectors.toList());
        if (deletes.size() > 0) sb.append(categoryRepository.getSqlStringForDeleteItemCategories(deletes));
        
        if (sb.toString() == "") return false;
        return listRepository.saveEntity(sb.toString());
    }

    @PostMapping("/category/class/save")
    @ResponseBody
    public boolean saveClassCategories(@RequestBody List<ClassCategoryEntity> list) {
        StringBuilder sb = new StringBuilder();
        List<ClassCategoryEntity> inserts = list.stream().filter(item -> item.getState() == Enums.state.CREATE.getNum()).collect(Collectors.toList());
        if (inserts.size() > 0) inserts.forEach(item -> sb.append(item.getInsertString()));
        List<ClassCategoryEntity> updates = list.stream().filter(item -> item.getState() == Enums.state.UPDATE.getNum()).collect(Collectors.toList());
        if (updates.size() > 0) updates.forEach(item -> sb.append(item.getUpdateString()));
        List<ClassCategoryEntity> deletes = list.stream().filter(item -> item.getState() == Enums.state.DELETE.getNum()).collect(Collectors.toList());
        if (deletes.size() > 0) sb.append(categoryRepository.getSqlStringForDeleteClassCategories(deletes));
        if (sb.toString() == "") return false;
        return listRepository.saveEntity(sb.toString());
    }
}
