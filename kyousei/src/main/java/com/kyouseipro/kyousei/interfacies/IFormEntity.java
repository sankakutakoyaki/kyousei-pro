package com.kyouseipro.kyousei.interfacies;

import java.util.List;

public interface IFormEntity extends IEntity {
    
    String getSelectString();
    String getInsertString();
    String getUpdateString();
    String getDeleteString();
    String getCsvFileString(List<IEntity> items);
    
}
