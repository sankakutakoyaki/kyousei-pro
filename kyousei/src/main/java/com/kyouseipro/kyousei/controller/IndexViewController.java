package com.kyouseipro.kyousei.controller;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	public ModelAndView getIndex(ModelAndView mv) {
		mv.addObject("sidebarName", "fragment/sidebarFragment::sidebar-fragment");
		mv.setViewName("content/index/homeIndex");
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
		mv.setViewName("content/index/salesIndex");
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
		mv.setViewName("content/index/personnelIndex");
		return mv;
	}

	/**
	 * 経理インデックス
	 * @param mv
	 * @return
	 */
	@GetMapping("/accounting")
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff', 'APPROLE_user', 'APPROLE_office')")
	public ModelAndView getAccountingIndex(ModelAndView mv) {
		mv.setViewName("content/index/accountingIndex");
		return mv;
	}

	/**
	 * 物流インデックス
	 * @param mv
	 * @return
	 */
	@GetMapping("/logistics")
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff', 'APPROLE_user', 'APPROLE_office')")
	public ModelAndView getLogisticsIndex(ModelAndView mv) {
		mv.setViewName("content/index/logisticsIndex");
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
		mv.setViewName("content/index/recycleIndex");
		return mv;
	}

	/**
	 * 登録インデックス
	 * @param mv
	 * @return
	 */
	@GetMapping("/regist")
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff', 'APPROLE_user', 'APPROLE_office')")
	public ModelAndView getRegistIndex(ModelAndView mv) {
		mv.setViewName("content/index/registIndex");
		return mv;
	}

	/**
	 * 勤怠インデックス
	 * @param mv
	 * @return
	 */
	@GetMapping("/timeworks")
	@PreAuthorize("hasAnyAuthority('APPROLE_admin', 'APPROLE_master', 'APPROLE_leader', 'APPROLE_staff', 'APPROLE_user', 'APPROLE_office')")
	public ModelAndView getTimeworksIndex(ModelAndView mv) {
		mv.setViewName("content/index/timeworksIndex");
		return mv;
	}
}
   