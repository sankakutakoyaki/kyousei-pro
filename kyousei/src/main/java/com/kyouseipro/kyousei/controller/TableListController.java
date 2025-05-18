package com.kyouseipro.kyousei.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;
import com.kyouseipro.kyousei.repository.ListRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TableListController {

    private final ListRepository listRepository;

    /**
     * リスト画面更新処理
     * @param sqlData
     * @return
     */
    @PostMapping("/tablelist/update")
    @ResponseBody
    public List<IEntity> updateTableList(@RequestBody SqlData sqldata) {
        return listRepository.getAllEntitiesThatMatchSearchCiteria(sqldata);
    }

    /**
     * IDからエンティティを取得する
     * @param sqlData
     * @return
     */
    @PostMapping("/tablelist/getform/id")
    @ResponseBody
    public IEntity getEntityById(@RequestBody SqlData sqldata) {
        return listRepository.getEntitiyThatMatchSearchCiteria(sqldata);
    }
    
    /**
     * 選択したIDのエンティティを削除する
     * @param sqlData
     * @return
     */
    @PostMapping("/tablelist/remove/multiple")
    @ResponseBody
    public boolean removeEntityByIds(@RequestBody SqlData sqldata) {
        return listRepository.removeEntitiyThatMatchSelectedIds(sqldata);
    }

    /**
     * 選択したエンティティのCSVファイルを作成してダウンロードする
     * @param sqldata
     * @return
     */
    @SuppressWarnings("deprecation")
    @PostMapping("/tablelist/download/csv")
    @ResponseBody
    public String downloadTableListCsvFiles(@RequestBody SqlData sqldata) {
        List<IEntity> list = listRepository.getAllEntitiesThatMatchSearchCiteria(sqldata);
        try {
            Class<?> c = Class.forName(sqldata.getClassPath());
            IFormEntity ent = (IFormEntity) c.newInstance();
            return ent.getCsvFileString(list);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
