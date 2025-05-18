package com.kyouseipro.kyousei.data;

import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.IFormEntity;
import com.kyouseipro.kyousei.interfacies.IListEntity;

import lombok.Data;

@Data
public class SqlData {

    private String sqlString = "";
    private String classPath = "";

    public void setClassPathFromEntity(IEntity entity) {
        this.classPath = entity.getClass().getName();
    }

    public void createListEntityData(IListEntity entity) {
        this.sqlString = entity.getSelectString();
        this.classPath = entity.getClass().getName();
    }

    public void createFormEntityData(IFormEntity entity) {
        this.sqlString = entity.getSelectString();
        this.classPath = entity.getClass().getName();
    }

    // public void createEmployeeFormEntityData(EmployeeEntity entity) {
    //     this.sqlString = entity.getSelectString();
    //     this.classPath = entity.getClass().getName();
    // }

}
