package com.kyouseipro.kyousei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyouseipro.kyousei.entity.employee.EmployeeEntity;
// import com.kyouseipro.kyousei.entity.employee._EmployeeFormEntity;
import com.kyouseipro.kyousei.entity.employee.FullTimeFormEntity;
import com.kyouseipro.kyousei.entity.employee.PartTimeFormEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.repository.EmployeeRepository;
import com.kyouseipro.kyousei.repository.ListRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PersonnelController {
    private final ListRepository listRepository;
    private final EmployeeRepository employeeRepository;
    
    @PostMapping("/personnel/fulltime/save")
    @ResponseBody
    public boolean updateEmployeeFormData(@RequestBody FullTimeFormEntity entity) {
        if (entity.getFulltime_id() > 0) {
            return listRepository.saveEntity(entity.getUpdateString());
        } else {
            return listRepository.saveEntity(entity.getInsertString());
        }
    }

    @PostMapping("/personnel/parttime/save")
    @ResponseBody
    public boolean updateParttimeFormData(@RequestBody PartTimeFormEntity entity) {
        if (entity.getParttime_id() > 0) {
            return listRepository.saveEntity(entity.getUpdateString());
        } else {
            return listRepository.saveEntity(entity.getInsertString());
        }
    }

    /**
     * コードから[employee]を取得
     * @return IEntity
     */
    @PostMapping("/personnel/get/code")
    @ResponseBody
    public IEntity getEmployeeFromCode(@RequestParam int code) {
        return (EmployeeEntity)employeeRepository.getEmployeeForCode(code);
    }

}
