package com.kyouseipro.kyousei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyouseipro.kyousei.entity.employee.StaffFormEntity;
import com.kyouseipro.kyousei.repository.ListRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SalesController {
    
    private final ListRepository listRepository;

    @PostMapping("/sales/staff/save")
    @ResponseBody
    public boolean updateStaffFormData(@RequestBody StaffFormEntity entity) {
        if (entity.getStaff_id() > 0) {
            return listRepository.saveEntity(entity.getUpdateString());
        } else {
            return listRepository.saveEntity(entity.getInsertString());
        }
    }

}