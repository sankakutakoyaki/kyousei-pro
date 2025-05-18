package kyousei.kyousei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import kyousei.kyousei.entity.data.HistoryEntity;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LogController {

    /**
     * 履歴を作成する
     * @param history
     * @return
     */
    @PostMapping("/history/save")
	@ResponseBody
    public int saveHistory(@RequestBody HistoryEntity historyEntity) {
        return historyEntity.saveHistory();
    }
}
