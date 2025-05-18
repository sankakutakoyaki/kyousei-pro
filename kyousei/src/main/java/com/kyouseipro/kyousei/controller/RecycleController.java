package com.kyouseipro.kyousei.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.entity.recycle.RecycleEntity;
import com.kyouseipro.kyousei.entity.recycle.RecycleMakerEntity;
import com.kyouseipro.kyousei.entity.recycle.RecyclePriceEntity;
import com.kyouseipro.kyousei.entity.recycle.RecycleProductEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.repository.ListRepository;
import com.kyouseipro.kyousei.repository.RecycleRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RecycleController {

    private final RecycleRepository recycleRepository;
    private final ListRepository listRepository;

    /**
     * お問合せ番号で指定したリサイクル券を取得する
     * @param number
     * @return
     */
    @PostMapping("/recycle/get")
    @ResponseBody
    public IEntity getRecycleByNumber(@RequestParam String number) {
        return recycleRepository.getRecycle(number);
    }

    /**
     * 検索条件で指定したリサイクル券を取得する
     * @param str
     * @return
     */
    @PostMapping("/recycle/list/search")
    @ResponseBody
    public List<IEntity> getRecycleFromSearchString(@RequestParam String str) {
        return recycleRepository.getSearchRecycleList(str);
    }

    /**
     * 小売業者（会社）を全て取得する
     * @param code
     * @return
     */
    @PostMapping("/recycle/maker/get")
    @ResponseBody
    public IEntity getRecycleMakerFromCode(@RequestParam int code) {
        return recycleRepository.getRecycleMakerByCode(code);
    }

    /**
     * IDで指定した小売業者の支店を全て取得する
     * @param id
     * @return
     */
    @PostMapping("/recycle/office/get")
    @ResponseBody
    public List<IEntity> getRecycleOfficeFromCompanyId(@RequestParam int id) {
        return recycleRepository.getOfficeListWithSimpleData(id);
    }

    /**
     * お問合せ管理票番号が重複していないか調べる(重複してれば[true]を返す)
     * @param number
     * @return
     */
    @PostMapping("/recycle/number/check")
    @ResponseBody
    public boolean checkDuplicateRecycleNumbers(@RequestParam String number) {
        return recycleRepository.checkDuplicateRecycleNumbers(number);
    }

    /**
     * お問合せ管理票番号がロス処理されていないか調べる(処理されてれば[true]を返す)
     * @param number
     * @return
     */
    @PostMapping("/recycle/number/check/loss")
    @ResponseBody
    public boolean checkDuplicateRecycleLossNumbers(@RequestParam String number) {
        return recycleRepository.checkDuplicateRecycleLossNumbers(number);
    }

    /**
     * リサイクルメーカーを全て取得する
     * @return
     */
    @GetMapping("/list/recycle/maker")
    @ResponseBody
    public List<IEntity> getAllRecycleMakerList() {
        return recycleRepository.getAllRecycleMakerList();
    }

    /**
     * リサイクルメーカーのプロダクトを全て取得する
     * @return
     */
    @GetMapping("/list/recycle/product")
    @ResponseBody
    public List<IEntity> getAllRecycleProductList() {
        return recycleRepository.getAllRecycleProductList();
    }

    /**
     * リサイクル料金を全て取得する
     * @return
     */
    @GetMapping("/list/recycle/price")
    @ResponseBody
    public List<IEntity> getAllRecyclePriceList() {
        return recycleRepository.getAllRecyclePriceList();
    }

    /**
     * 
     * @param list
     * @return
     */
    @PostMapping("/recycle/get/list/use")
    @ResponseBody
    public List<IEntity> getUseRecycleRegistList(@RequestParam LocalDate date) {
        return recycleRepository.getRecycleList(Enums.recycleDateCategory.USE.getNum(), date);
    }
    @PostMapping("/recycle/get/list/delivery")
    @ResponseBody
    public List<IEntity> getDeliveryRecycleRegistList(@RequestParam LocalDate date) {
        return recycleRepository.getRecycleList(Enums.recycleDateCategory.DELIVERY.getNum(), date);
    }
    @PostMapping("/recycle/get/list/forward")
    @ResponseBody
    public List<IEntity> getForwardRecycleRegistList(@RequestParam LocalDate date) {
        return recycleRepository.getRecycleList(Enums.recycleDateCategory.FORWARD.getNum(), date);
    }
    @PostMapping("/recycle/get/list/loss")
    @ResponseBody
    public List<IEntity> getLossRecycleRegistList(@RequestParam LocalDate date) {
        return recycleRepository.getRecycleList(Enums.recycleDateCategory.LOSS.getNum(), date);
    }
    @PostMapping("/recycle/get/list/input/today")
    @ResponseBody
    public List<IEntity> getRecycleRegistListInputToday(@RequestParam int categoryId) {
        return recycleRepository.getRecycleListInputToday(categoryId);
    }

    /**
     * リサイクル券を登録する(単独)
     * @param
     * @return
     */
    @PostMapping("/recycle/save")
    @ResponseBody
    public boolean saveRecycleRegist(@RequestBody RecycleEntity entity) {
        return listRepository.saveEntity(entity.getInsertString());
    }

    /**
     * リサイクル券を更新する(単独)
     * @param
     * @return
     */
    @PostMapping("/recycle/update")
    @ResponseBody
    public boolean updateRecycleRegist(@RequestBody RecycleEntity entity) {
        return listRepository.saveEntity(entity.getUpdateString());
    }

    /**
     * リサイクル券を削除する
     * @param
     * @return
     */
    @PostMapping("/recycle/delete")
    @ResponseBody
    public boolean deleteRecycleRegist(@RequestBody List<RecycleEntity> list) {
        StringBuilder sb = new StringBuilder();
        List<RecycleEntity> deletes = list.stream().filter(item -> item.getState() == Enums.state.DELETE.getNum()).collect(Collectors.toList());
        if (deletes.size() > 0) sb.append(recycleRepository.getSqlStringForDeleteRecycle(deletes));
        if (sb.toString() == "") return false;
        return listRepository.saveEntity(sb.toString());
    }

    /**
     * 使用済みリサイクル券を修正する
     * @param list
     * @return
     */
    @PostMapping("/recycle/list/update")
    @ResponseBody
    public boolean updateRecycleRegist(@RequestBody List<RecycleEntity> list) {
        StringBuilder sb = new StringBuilder();
        List<RecycleEntity> updates = list.stream().filter(item -> item.getState() == Enums.state.UPDATE.getNum()).collect(Collectors.toList());
        if (updates.size() > 0) updates.forEach(item -> sb.append(item.getUpdateString()));
        if (sb.toString() == "") return false;
        return listRepository.saveEntity(sb.toString());
    }
    // /**
    //  * 使用済みリサイクル券を登録する
    //  * @param list
    //  * @return
    //  */
    // @PostMapping("/recycle/list/save")
    // @ResponseBody
    // public boolean saveRecycleRegist(@RequestBody List<RecycleEntity> list) {
    //     StringBuilder sb = new StringBuilder();
    //     List<RecycleEntity> inserts = list.stream().filter(item -> item.getState() == Enums.state.CREATE.getNum()).collect(Collectors.toList());
    //     if (inserts.size() > 0) inserts.forEach(item -> sb.append(item.getInsertString()));
    //     List<RecycleEntity> updates = list.stream().filter(item -> item.getState() == Enums.state.UPDATE.getNum()).collect(Collectors.toList());
    //     if (updates.size() > 0) updates.forEach(item -> sb.append(item.getUpdateString()));
    //     List<RecycleEntity> deletes = list.stream().filter(item -> item.getState() == Enums.state.DELETE.getNum()).collect(Collectors.toList());
    //     if (deletes.size() > 0) sb.append(recycleRepository.getSqlStringForDeleteRecycle(deletes));
    //     if (sb.toString() == "") return false;
    //     return listRepository.saveEntity(sb.toString());
    // }

    /**
     * リサイクルメーカーを登録する
     * @param list
     * @return
     */
    @PostMapping("/recycle/maker/save")
    @ResponseBody
    public boolean saveRecycleMakerRegist(@RequestBody List<RecycleMakerEntity> list) {
        StringBuilder sb = new StringBuilder();
        List<RecycleMakerEntity> inserts = list.stream().filter(item -> item.getState() == Enums.state.CREATE.getNum()).collect(Collectors.toList());
        if (inserts.size() > 0) inserts.forEach(item -> sb.append(item.getInsertString()));
        List<RecycleMakerEntity> updates = list.stream().filter(item -> item.getState() == Enums.state.UPDATE.getNum()).collect(Collectors.toList());
        if (updates.size() > 0) updates.forEach(item -> sb.append(item.getUpdateString()));
        List<RecycleMakerEntity> deletes = list.stream().filter(item -> item.getState() == Enums.state.DELETE.getNum()).collect(Collectors.toList());
        if (deletes.size() > 0) sb.append(recycleRepository.getSqlStringForDeleteRecycleMaker(deletes));
        if (sb.toString() == "") return false;
        return listRepository.saveEntity(sb.toString());
    }

    /**
     * 製造業者を登録する
     * @param list
     * @return
     */
    @PostMapping("/recycle/product/save")
    @ResponseBody
    public boolean saveRecycleProduct(@RequestBody List<RecycleProductEntity> list) {
        StringBuilder sb = new StringBuilder();
        List<RecycleProductEntity> inserts = list.stream().filter(item -> item.getState() == Enums.state.CREATE.getNum()).collect(Collectors.toList());
        if (inserts.size() > 0) inserts.forEach(item -> sb.append(item.getInsertString()));
        List<RecycleProductEntity> updates = list.stream().filter(item -> item.getState() == Enums.state.UPDATE.getNum()).collect(Collectors.toList());
        if (updates.size() > 0) updates.forEach(item -> sb.append(item.getUpdateString()));
        List<RecycleProductEntity> deletes = list.stream().filter(item -> item.getState() == Enums.state.DELETE.getNum()).collect(Collectors.toList());
        if (deletes.size() > 0) sb.append(recycleRepository.getSqlStringForDeleteRecycleProduct(deletes));
        if (sb.toString() == "") return false;
        return listRepository.saveEntity(sb.toString());
    }

    /**
     * リサイクル料金を登録する
     * @param list
     * @return
     */
    @PostMapping("/recycle/price/save")
    @ResponseBody
    public boolean saveRecyclePrice(@RequestBody List<RecyclePriceEntity> list) {
        StringBuilder sb = new StringBuilder();
        List<RecyclePriceEntity> inserts = list.stream().filter(item -> item.getState() == Enums.state.CREATE.getNum()).collect(Collectors.toList());
        if (inserts.size() > 0) inserts.forEach(item -> sb.append(item.getInsertString()));
        List<RecyclePriceEntity> updates = list.stream().filter(item -> item.getState() == Enums.state.UPDATE.getNum()).collect(Collectors.toList());
        if (updates.size() > 0) updates.forEach(item -> sb.append(item.getUpdateString()));
        List<RecyclePriceEntity> deletes = list.stream().filter(item -> item.getState() == Enums.state.DELETE.getNum()).collect(Collectors.toList());
        if (deletes.size() > 0) sb.append(recycleRepository.getSqlStringForDeleteRecyclePrice(deletes));
        if (sb.toString() == "") return false;
        return listRepository.saveEntity(sb.toString());
    }

    /**
     * 使用日を登録する
     * @param list
     * @return
     */
    @PostMapping("/recycle/use/save")
    @ResponseBody
    public boolean saveRecycleUseDate(@RequestBody RecycleEntity entity) {
        String str = recycleRepository.getSqlStringForRegistRecycleUseDate(entity);
        if (str == "") return false;
        return listRepository.saveEntity(str);
    }

    /**
     * 発送先・発送日を登録する
     * @param list
     * @return
     */
    @PostMapping("/recycle/forward/save")
    @ResponseBody
    // public boolean saveRecycleForwardDate(@RequestBody List<RecycleEntity> list) {
    //     List<RecycleEntity> updates = list.stream().filter(item -> item.getState() == Enums.state.UPDATE.getNum()).collect(Collectors.toList());
    //     if (updates.size() > 0) {
    //         String str = recycleRepository.getSqlStringForRegistRecycleShippingDate(list);
    //         if (str == "") return false;
    //         return listRepository.saveEntity(str);
    //     } else {
    //         return false;
    //     }
    // }
    public boolean saveRecycleForwardDate(@RequestBody RecycleEntity entity) {
        String str = recycleRepository.getSqlStringForRegistRecycleShippingDate(entity);
        if (str == "") return false;
        return listRepository.saveEntity(str);
    }

    /**
     * 引渡日を登録する
     * @param list
     * @return
     */
    @PostMapping("/recycle/delivery/save")
    @ResponseBody
    // public boolean saveRecycleDeliveryDate(@RequestBody List<RecycleEntity> list) {
    //     List<RecycleEntity> updates = list.stream().filter(item -> item.getState() == Enums.state.UPDATE.getNum()).collect(Collectors.toList());
    //     if (updates.size() > 0) {
    //         String str = recycleRepository.getSqlStringForRegistRecycleDeliveryDate(updates);
    //         if (str == "") return false;
    //         return listRepository.saveEntity(str);
    //     } else {
    //         return false;
    //     }
    public boolean saveRecycleDeliveryDate(@RequestBody RecycleEntity entity) {
        String str = recycleRepository.getSqlStringForRegistRecycleDeliveryDate(entity);
        if (str == "") return false;
        return listRepository.saveEntity(str);
    }

    /**
     * ロス処理日を登録する
     * @param list
     * @return
     */
    @PostMapping("/recycle/loss/save")
    @ResponseBody
    public boolean saveRecycleLossDate(@RequestBody List<RecycleEntity> list) {
        List<RecycleEntity> deletes = list.stream().filter(item -> item.getState() == Enums.state.DELETE.getNum()).collect(Collectors.toList());
        if (deletes.size() > 0) {
            String str = recycleRepository.getSqlStringForRegistRecycleLossDate(deletes);
            if (str == "") return false;
            return listRepository.saveEntity(str);
        } else {
            return false;
        }
    }
}
