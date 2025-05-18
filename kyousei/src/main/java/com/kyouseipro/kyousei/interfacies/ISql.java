package com.kyouseipro.kyousei.interfacies;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kyouseipro.kyousei.data.SqlData;

public interface ISql extends ISqlBasic {

    @SuppressWarnings("deprecation")
    default public IEntity getEntity(SqlData data)  {
        try {
            Class<?> c = Class.forName(data.getClassPath());
            IEntity entity = (IEntity) c.newInstance();
            execSql(s -> {
                try {
                    ResultSet rs = s.executeQuery(data.getSqlString());
                    if (rs.next()){
                        entity.setEntity(rs);
                    }
                    return true;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return false;
                } 
            });
            return entity;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }
        return null;        
    }

    default public boolean saveEntity(String str) {
        boolean result = true;
        execSql(s -> {
            try {
                s.executeUpdate(str);
                return true;
            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        });
        return result;    
    }

    @SuppressWarnings("deprecation")
    default public List<IEntity> getList(SqlData data) {
        List<IEntity> list = new ArrayList<>();
        execSql(s -> {
            try {
                ResultSet rs = s.executeQuery(data.getSqlString());
                while(rs.next()){
                    Class<?> c = Class.forName(data.getClassPath());
                    IEntity ent = (IEntity) c.newInstance();
                    ent.setEntity(rs);
                    list.add(ent);
                }
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            }
            return false;
        });
        return list;
    }
} 
