package com.kyouseipro.kyousei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyouseipro.kyousei.entity.corporation.CompanyFormEntity;
import com.kyouseipro.kyousei.entity.corporation.OfficeFormEntity;
import com.kyouseipro.kyousei.entity.employee.PartnerFormEntity;
import com.kyouseipro.kyousei.repository.ListRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CompanyController {
        
    private final ListRepository listRepository;

    @PostMapping("/company/save")
    @ResponseBody
    public boolean updateCompanyFormData(@RequestBody CompanyFormEntity entity) {
        if (entity.getCompany_id() > 0) {
            return listRepository.saveEntity(entity.getUpdateString());
        } else {
            return listRepository.saveEntity(entity.getInsertString());
        }
    }

    @PostMapping("/company/office/save")
    @ResponseBody
    public boolean updateOfficeFormData(@RequestBody OfficeFormEntity entity) {
        if (entity.getOffice_id() > 0) {
            return listRepository.saveEntity(entity.getUpdateString());
        } else {
            return listRepository.saveEntity(entity.getInsertString());
        }
    }
    
    @PostMapping("/company/partner/save")
    @ResponseBody
    public boolean updatePartnerFormData(@RequestBody PartnerFormEntity entity) {
        if (entity.getPartner_id() > 0) {
            return listRepository.saveEntity(entity.getUpdateString());
        } else {
            return listRepository.saveEntity(entity.getInsertString());
        }
    }

}
