package com.kyouseipro.kyousei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyouseipro.kyousei.entity.expenses.CashExpensesEntity;
import com.kyouseipro.kyousei.repository.ExpensesRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ExpensesController {

    private final ExpensesRepository expensesRepository;

    @PostMapping("/expenses/cash/daily/save")
    @ResponseBody
    public boolean insertDailyCashExpenses(@RequestBody CashExpensesEntity entity) {
        return expensesRepository.saveDailyCashExpenses(entity);
    }

}
