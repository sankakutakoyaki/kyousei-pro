package kyousei.kyousei.controller;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ManagementController {

    // /**
    //  * プッシュ通知を送信する
    //  * @param history
    //  * @return
    //  */
    // @GetMapping("/info")
	// @ResponseBody
    // public ModelAndView pushSend(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
    //     // ユーザー名
    //     String username = principal.getAttribute("preferred_username");
    //     mv.addObject("username", username);
    //     // MVを設定
    //     mv.setViewName("content/management/info");
    //     // 履歴登録
    //     HistoryEntity history = new HistoryEntity();
    //     history.setUser_name(username);
    //     history.setTable_name("subscriptions");
    //     history.setState("閲覧");
    //     history.saveHistory();

    //     return mv;
    // }
}
