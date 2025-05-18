// package kyousei.kyousei.interfaceis;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.ResourceBundle;
// import java.util.function.Predicate;

// public interface ISqlBasic {

//     static String driverName = ResourceBundle.getBundle("application").getString("spring.datasource.driver-class-name");
//     static String url = ResourceBundle.getBundle("application").getString("spring.datasource.url");
//     static String userName = ResourceBundle.getBundle("application").getString("spring.datasource.username");
//     static String password = ResourceBundle.getBundle("application").getString("spring.datasource.password");

//     default boolean execSql(Predicate<Statement> execQuery) {
//         Connection conn = null;
//         Statement stmt = null;

//         try {
//             Class.forName(driverName);
//             conn = DriverManager.getConnection(url, userName, password);
//             conn.setAutoCommit(false);
//             stmt = conn.createStatement();
//             boolean result = execQuery.test(stmt);
//             conn.commit();
//             return result;
//         } catch (Exception e) {
//             System.out.println(e);
//             if (conn != null) {
//                 try {
//                     conn.rollback();
//                 } catch (SQLException ignore) {
//                 }
//             }
//             return false;
//         } finally {
//             try {
//                 if (stmt != null) {
//                     stmt.close();
//                 }
//                 if (conn != null) {
//                     conn.close();
//                 }
//             } catch (SQLException e) {
//                 System.out.println(e);
//             }
//         }
//     }
    
// }
