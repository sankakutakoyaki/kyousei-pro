package kyousei.kyousei.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import kyousei.kyousei.entity.data.SimpleData;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.interfaceis.IEntity;
import kyousei.kyousei.service.SqlService;

@Controller
public class SqlController {

    /**
     * SQLを実行
     * @param simpleData
     * @return
     */
	@PostMapping("/sql/excute")
	@ResponseBody
    public int excuteSql(@RequestBody SimpleData simpleData) {
        return SqlService.excuteSqlString(simpleData.getText());
    }
    /**
     * エンティティリストを取得
     * @param sqlData
     * @return
     */
    @PostMapping("/sql/get/list")
	@ResponseBody
    public List<IEntity> getList(@RequestBody SqlData sqlData) {
        return SqlService.getEntityList(sqlData);
    }
    /**
     * エンティティを取得
     * @param sqlData
     * @return
     */
    @PostMapping("/sql/get/single")
	@ResponseBody
    public IEntity getSingle(@RequestBody SqlData sqlData) {
        return SqlService.getEntity(sqlData);
    }
}
