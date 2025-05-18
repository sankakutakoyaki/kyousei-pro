// package kyousei.kyousei.interfaceis;

// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.List;

// import kyousei.kyousei.entity.data.SimpleData;
// import kyousei.kyousei.entity.data.SqlData;
// import kyousei.kyousei.service.SqlService;

// public interface ISql {

//     /**
//      * 単独のエンティティを取得する
//      * @param data
//      * @return
//      */
//     // @SuppressWarnings("deprecation")
//     static IEntity getSingle(SqlData data)  {
//         try {
//             Class<?> c = Class.forName(data.getClassPath());
//             IEntity entity = (IEntity) c.newInstance();
//             SqlService.execSql(s -> {
//                 try {
//                     ResultSet rs = s.executeQuery(data.getSqlString());
//                     if (rs.next()){
//                         entity.setEntity(rs);
//                     }
//                     return true;
//                 } catch (SQLException ex) {
//                     ex.printStackTrace();
//                     return false;
//                 } 
//             });
//             return entity;
//         } catch (ClassNotFoundException ex) {
//             ex.printStackTrace();
//         } catch (IllegalAccessException ex) {
//             ex.printStackTrace();
//         } catch (InstantiationException ex) {
//             ex.printStackTrace();
//         }
//         return null;        
//     }
//     /**
//      * エンティティを保存・更新する
//      * @param str
//      * @return
//      */
//     static public int saveEntity(String str) {
//         SimpleData simpleData = new SimpleData();
//         SqlService.execSql(s -> {
//             try {
//                 ResultSet rs = s.executeQuery(str);
//                 if (rs.next()){
//                     simpleData.setNumber(rs.getInt("number"));
//                 }
//                 return true;
//             } catch (SQLException e) {
//                 System.out.println(e);
//                 return false;
//             }
//         });
//         return simpleData.getNumber();    
//         // boolean result = true;
//         // result = execSql(s -> {
//         //     try {
//         //         int rowcount = s.executeUpdate(str);
//         //         if (rowcount == 0) {
//         //             return false;
//         //         } else {
//         //             return true;
//         //         }
//         //     } catch (SQLException e) {
//         //         System.out.println(e);
//         //         return false;
//         //     }
//         // });
//         // return result;    
//     }
//     /**
//      * エンティティリストを取得する
//      * @param data
//      * @return
//      */
//     @SuppressWarnings("deprecation")
//     static public List<IEntity> getList(SqlData data) {
//         List<IEntity> list = new ArrayList<>();
//         SqlService.execSql(s -> {
//             try {
//                 ResultSet rs = s.executeQuery(data.getSqlString());
//                 while(rs.next()){
//                     Class<?> c = Class.forName(data.getClassPath());
//                     IEntity ent = (IEntity) c.newInstance();
//                     ent.setEntity(rs);
//                     list.add(ent);
//                 }
//                 return true;
//             } catch (SQLException ex) {
//                 ex.printStackTrace();
//             } catch (ClassNotFoundException ex) {
//                 ex.printStackTrace();
//             } catch (IllegalAccessException ex) {
//                 ex.printStackTrace();
//             } catch (InstantiationException ex) {
//                 ex.printStackTrace();
//             }
//             return false;
//         });
//         return list;
//     }
//     /**
//      * SQL文から数値を取得する
//      * @param str
//      * @return
//      */
//     static public int getId(String str) {
//         SimpleData simpleData = new SimpleData();
//         SqlService.execSql(s -> {
//             try {
//                 ResultSet rs = s.executeQuery(str);
//                 if (rs.next()){
//                     simpleData.setNumber(rs.getInt("number"));
//                 }
//                 return true;
//             } catch (SQLException e) {
//                 System.out.println(e);
//                 return false;
//             }
//         });
//         return simpleData.getNumber();    
//     }
// } 
