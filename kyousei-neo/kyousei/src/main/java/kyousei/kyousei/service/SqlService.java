package kyousei.kyousei.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import kyousei.kyousei.entity.data.SimpleData;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.interfaceis.IEntity;

@Service
public class SqlService {

    static String driverName = ResourceBundle.getBundle("application").getString("spring.datasource.driver-class-name");
    static String url = ResourceBundle.getBundle("application").getString("spring.datasource.url");
    static String userName = ResourceBundle.getBundle("application").getString("spring.datasource.username");
    static String password = ResourceBundle.getBundle("application").getString("spring.datasource.password");

    public static boolean execSql(Predicate<Statement> execQuery) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, userName, password);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            boolean result = execQuery.test(stmt);
            conn.commit();
            return result;
        } catch (Exception e) {
            System.out.println(e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignore) {
                }
            }
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }
    /**
     * 単独のエンティティを取得する
     * @param data
     * @return
     */
    // public static IEntity getEntity(SqlData data)  {
    //     try {
    //         Class<?> c = Class.forName(data.getClassPath());
    //         IEntity entity = (IEntity) c.newInstance();
    //         execSql(s -> {
    //             try {
    //                 ResultSet rs = s.executeQuery(data.getSqlString());
    //                 if (rs.next()){
    //                     entity.setEntity(rs);
    //                 }
    //                 return true;
    //             } catch (SQLException ex) {
    //                 ex.printStackTrace();
    //                 return false;
    //             } 
    //         });
    //         return entity;
    //     } catch (ClassNotFoundException ex) {
    //         ex.printStackTrace();
    //     } catch (IllegalAccessException ex) {
    //         ex.printStackTrace();
    //     } catch (InstantiationException ex) {
    //         ex.printStackTrace();
    //     }
    //     return null;        
    // }
    public static IEntity getEntity(SqlData data) {
        try {
            Class<?> c = Class.forName(data.getClassPath());

            // 引数なしのコンストラクタを取得
            Constructor<?> constructor = c.getDeclaredConstructor();

            // アクセス許可（protected/private の場合）
            constructor.setAccessible(true);

            // インスタンス生成
            IEntity entity = (IEntity) constructor.newInstance();

            execSql(s -> {
                try {
                    ResultSet rs = s.executeQuery(data.getSqlString());
                    if (rs.next()) {
                        entity.setEntity(rs);
                    }
                    return true;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return false;
                }
            });

            return entity;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        return null;
    }
    /**
     * エンティティを保存・更新する
     * @param str
     * @return
     */
    public static int excuteSqlString(String str) {
        SimpleData simpleData = new SimpleData();
        execSql(s -> {
            try {
                ResultSet rs = s.executeQuery(str);
                if (rs.next()){
                    simpleData.setNumber(rs.getInt("number"));
                }
                return true;
            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        });
        return simpleData.getNumber();    
    }
    /**
     * エンティティリストを取得する
     * @param data
     * @return
     */
    // public static List<IEntity> getEntityList(SqlData data) {
    //     List<IEntity> list = new ArrayList<>();
    //     execSql(s -> {
    //         try {
    //             ResultSet rs = s.executeQuery(data.getSqlString());
    //             while(rs.next()){
    //                 Class<?> c = Class.forName(data.getClassPath());
    //                 IEntity ent = (IEntity) c.newInstance();
    //                 ent.setEntity(rs);
    //                 list.add(ent);
    //             }
    //             return true;
    //         } catch (SQLException ex) {
    //             ex.printStackTrace();
    //         } catch (ClassNotFoundException ex) {
    //             ex.printStackTrace();
    //         } catch (IllegalAccessException ex) {
    //             ex.printStackTrace();
    //         } catch (InstantiationException ex) {
    //             ex.printStackTrace();
    //         }
    //         return false;
    //     });
    //     return list;
    // }
    public static List<IEntity> getEntityList(SqlData data) {
        List<IEntity> list = new ArrayList<>();
        execSql(s -> {
            try {
                ResultSet rs = s.executeQuery(data.getSqlString());
    
                // 毎ループでクラスをロードするのは非効率なので、事前に取得
                Class<?> c = Class.forName(data.getClassPath());
                Constructor<?> constructor = c.getDeclaredConstructor();
                constructor.setAccessible(true);
    
                while (rs.next()) {
                    IEntity ent = (IEntity) constructor.newInstance();
                    ent.setEntity(rs);
                    list.add(ent);
                }
    
                return true;
    
            } catch (SQLException | ClassNotFoundException | IllegalAccessException |
                     InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();
                return false;
            }
        });
        return list;
    }
    /**
     * SQL文から数値を取得する
     * @param str
     * @return
     */
    public static int getId(String str) {
        SimpleData simpleData = new SimpleData();
        execSql(s -> {
            try {
                ResultSet rs = s.executeQuery(str);
                if (rs.next()){
                    simpleData.setNumber(rs.getInt("number"));
                }
                return true;
            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        });
        return simpleData.getNumber();    
    }
}