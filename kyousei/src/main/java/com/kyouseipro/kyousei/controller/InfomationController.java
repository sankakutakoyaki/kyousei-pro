package com.kyouseipro.kyousei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.repository.AddressRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InfomationController {
    
    private final AddressRepository addressRepository;

    /**
     * 郵便番号から住所を取得
     * @param postal_code
     * @return
     */
    @PostMapping("/postalcode/getaddress")
    @ResponseBody
    public IEntity postalCodeToAddress(@RequestParam String postal_code){    
        return addressRepository.getAddressFromPostalCode(postal_code);
    }

}
