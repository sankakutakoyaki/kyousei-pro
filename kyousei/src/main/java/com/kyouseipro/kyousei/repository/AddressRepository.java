package com.kyouseipro.kyousei.repository;

import org.springframework.stereotype.Repository;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.common.Utilities;
import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.entity.info.AddressEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.ISql;

@Repository
public class AddressRepository implements ISql {
    
    /**
     * 郵便番号から住所データを取得する
     * @param code
     * @return
     */
    public IEntity getAddressFromPostalCode(String code) {
        String postalCode = Utilities.checkPostalCode(code);        
        if (postalCode == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM address WHERE postal_code = '" + postalCode + "' AND state = " + Enums.state.INITIAL.getNum());
        SqlData data = new SqlData();
        data.setClassPathFromEntity(new AddressEntity());
        data.setSqlString(sb.toString());
        return getEntity(data);
    }

}
