package kyousei.kyousei.controller;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyousei.kyousei.entity.data.HistoryEntity;
import kyousei.kyousei.entity.employee.EmployeeEntity;
import kyousei.kyousei.interfaceis.IEntity;
import kyousei.kyousei.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexViewController {

	/**
	 * スタートページ
	 * @param mv
	 * @return
	 */
	@GetMapping("/")
	@ResponseBody
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff', 'APPROLE_user', 'APPROLE_office')")
	public ModelAndView getIndex(ModelAndView mv, @AuthenticationPrincipal OidcUser principal) {
		// mv.addObject("sidebarName", "fragment/main::sidebar-fragment");
		mv.setViewName("content/index/home");
		// ユーザー名
		String username = principal.getAttribute("preferred_username");
		mv.addObject("username", username);
		IEntity entity = EmployeeService.getEmployeeForAccount(username);
		mv.addObject("employeeId", ((EmployeeEntity)entity).getEmployee_id());
		
        return mv;
    }
	/**
	 * ログアウト
	 * @param httpRequest
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/logout")
	@ResponseBody
    public void logOut(HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        httpRequest.getSession().invalidate();
        String endSessionEndpoint = "https://login.microsoftonline.com/common/oauth2/v2.0/logout";
        // String redirectUrl = "http://localhost:8080/";
		String redirectUrl = "https://www.kyouseipro.com/";
        response.sendRedirect(endSessionEndpoint + "?post_logout_redirect_uri=" + URLEncoder.encode(redirectUrl, "UTF-8"));
    }
	/**
	 * 営業インデックス
	 * @param mv
	 * @return
	 */
	@GetMapping("/sales")
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff', 'APPROLE_user', 'APPROLE_office')")
	public ModelAndView getSalesIndex(ModelAndView mv) {
		mv.setViewName("content/index/sales");
		return mv;
	}
	/**
	 * 人事インデックス
	 * @param mv
	 * @return
	 */
	@GetMapping("/personnel")
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff', 'APPROLE_user', 'APPROLE_office')")
	public ModelAndView getPersonnelIndex(ModelAndView mv) {
		mv.setViewName("content/index/personnel");
		return mv;
	}
	/**
	 * 経理インデックス
	 * @param mv
	 * @return
	 */
	@GetMapping("/piggy_bank")
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff', 'APPROLE_user', 'APPROLE_office')")
	public ModelAndView getPiggyBankIndex(ModelAndView mv) {
		mv.setViewName("content/index/piggy_bank");
		return mv;
	}
	/**
	 * 管理インデックス
	 * @param mv
	 * @return
	 */
	@GetMapping("/management")
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff', 'APPROLE_user', 'APPROLE_office')")
	public ModelAndView getManagementIndex(ModelAndView mv) {
		mv.setViewName("content/index/management");
		return mv;
	}
	/**
	 * 登録インデックス
	 * @param mv
	 * @return
	 */
	@GetMapping("/regist")
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff')")
	public ModelAndView getRegistIndex(ModelAndView mv) {
		mv.setViewName("content/index/regist");
		return mv;
	}
	/**
	 * リサイクルインデックス
	 * @param mv
	 * @return
	 */
	@GetMapping("/recycle")
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff', 'APPROLE_user', 'APPROLE_office')")
	public ModelAndView getRecycleIndex(ModelAndView mv) {
		mv.setViewName("content/index/recycle");
		return mv;
	}
	/**
     * お知らせの作成・一覧
     * @param history
     * @return
     */
    @GetMapping("/info")
	@ResponseBody
    public ModelAndView pushSend(ModelAndView mv, OAuth2AuthenticationToken token, @AuthenticationPrincipal OidcUser principal) {
        // ユーザー名
        String username = principal.getAttribute("preferred_username");
        mv.addObject("username", username);
        // MVを設定
        mv.setViewName("content/index/info");
        // 履歴登録
        HistoryEntity history = new HistoryEntity();
        history.setUser_name(username);
        history.setTable_name("subscriptions");
        history.setState("閲覧");
        history.saveHistory();

        return mv;
    }
}
   