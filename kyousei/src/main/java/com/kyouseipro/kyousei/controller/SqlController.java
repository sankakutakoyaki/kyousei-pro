package com.kyouseipro.kyousei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyouseipro.kyousei.repository.ListRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SqlController {
    private final ListRepository listRepository;

    /**
     * SQL実行
     * @param str
     * @return
     */
    @PostMapping("/exec/sqlstr")
    @ResponseBody
    public boolean updateTableList(@RequestParam String str) {
        return listRepository.saveEntity(str);
    }
}
