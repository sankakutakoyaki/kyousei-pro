package kyousei.kyousei.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kyousei.kyousei.entity.data.AddressEntity;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.interfaceis.ICsv;
import kyousei.kyousei.interfaceis.IEntity;
import kyousei.kyousei.service.DownloadService;
import kyousei.kyousei.service.SqlService;

@Controller
// @RequiredArgsConstructor
public class DownloadController {
    // private final SqlRepository sqlRepository;

    // private final DownloadService downloadService;

    /**
     * 選択したエンティティのCSVファイルを作成してダウンロードする
     * @param sqldata
     * @return
     */
    // @SuppressWarnings("deprecation")
    @PostMapping("/download/csv")
    @ResponseBody
    // public String downloadCsvFiles(@RequestBody SqlData sqlData) {
    //     List<IEntity> list = SqlService.getEntityList(sqlData);
    //     try {
    //         Class<?> c = Class.forName(sqlData.getCsvClassPath());
    //         ICsv ent = (ICsv) c.newInstance();
    //         return ent.getCsvString(list);
    //     } catch (ClassNotFoundException ex) {
    //         ex.printStackTrace();
    //     } catch (InstantiationException e) {
    //         e.printStackTrace();
    //     } catch (IllegalAccessException e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }
    public String downloadCsvFiles(@RequestBody SqlData sqlData) {
    List<IEntity> list = SqlService.getEntityList(sqlData);
    try {
        Class<?> c = Class.forName(sqlData.getCsvClassPath());

        // 引数なしのコンストラクタを取得
        Constructor<?> constructor = c.getDeclaredConstructor();

        // 必要ならアクセス可能に（protected や private の場合）
        constructor.setAccessible(true);

        // インスタンス生成
        ICsv ent = (ICsv) constructor.newInstance();

        return ent.getCsvString(list);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
             InvocationTargetException | NoSuchMethodException e) {
        e.printStackTrace();
    }
    return null;
}
    /**
     * 郵便番号から住所を取得する
     * @param postal_code
     * @return AddressEntity
     */
    @PostMapping("/getaddress/postalcode")
	@ResponseBody
    public IEntity getAddressFromPostalCode(@RequestParam String postal_code) {
        StringBuilder sb = new StringBuilder("SELECT * From address WHERE postal_code = '" + postal_code + "';");
        SqlData sqlData = DownloadService.createSqlData(new AddressEntity(), sb.toString(), null);
        return SqlService.getEntity(sqlData);
    }
}
