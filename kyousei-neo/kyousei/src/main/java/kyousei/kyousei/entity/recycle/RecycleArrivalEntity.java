// package kyousei.kyousei.entity.recycle;

// import java.sql.ResultSet;
// import java.time.LocalDate;

// import kyousei.kyousei.common.Enums;
// import kyousei.kyousei.interfaceis.IEntity;
// import lombok.Data;

// @Data
// public class RecycleArrivalEntity implements IEntity {

//     private int recycle_arrival_id;
//     private String recycle_number;
//     private int arrival_company_id;
//     private String arrivla_company_name;
//     private int arrival_office_id;
//     private String arrival_office_name;
//     private LocalDate arrival_date;
//     private int version;
//     private int state;
//     private String user_name;

//     public void setEntity(ResultSet rs) {
//         try {
//             this.recycle_arrival_id = rs.getInt("recycle_id");
//             this.recycle_number = rs.getString("recycle_number");
//             this.arrival_company_id = rs.getInt("arrival_company_id");
//             this.arrivla_company_name = rs.getString("company_name");
//             this.arrival_office_id = rs.getInt("arrival_office_id");
//             this.arrival_office_name = rs.getString("office_name");
//             this.arrival_date = rs.getDate("arrival_date").toLocalDate();
//             this.version = rs.getInt("version");
//             this.state = rs.getInt("state");
//         } catch (Exception e) {
//             System.out.println(e);
//         }
//     }
//     public String getSelectString() {
//         StringBuilder sb = new StringBuilder();
//         sb.append("SELECT recycle_arrival_id, recycle_number, arrival_company_id, c.company_name, arrival_office_id, o.office_name, arrival_date, r.version, r.state From recycle_arrival r");
//         sb.append(" LEFT OUTER JOIN companies c ON c.company_id = r.arrival_company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
//         sb.append(" LEFT OUTER JOIN offices o ON o.office_id = r.arrival_office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
//         sb.append(" WHERE r.state = " + Enums.state.UNDECIDED.getNum());
//         return sb.toString();
//     }
//     public String getInsertString() {
//         StringBuilder sb = new StringBuilder();
//         sb.append(logTable());
//         sb.append("INSERT INTO recycle_arrival (");
//         sb.append("recycle_number");
//         sb.append(", arrival_company_id");
//         sb.append(", arrival_office_id");
//         sb.append(", arrival_date");
//         sb.append(") ");
//         sb.append(logString("作成"));
//         sb.append(" VALUES (");
//         sb.append("'" + this.recycle_number + "'");
//         sb.append(", " + this.arrival_company_id);
//         sb.append(", " + this.arrival_office_id);
//         sb.append(", '" + this.arrival_date + "'");
//         sb.append(");");
//         sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
//         // 変更履歴
//         sb.append("INSERT INTO history (user_name, table_name, state, contents)");
//         sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_arrival', '作成', 'ID=' + CONVERT(nvarchar,@NEW_ID));");
//         sb.append("INSERT INTO recycle_arrival_log SELECT * FROM @RecycleArrivalTable;");
//         // 新規IDを返す
//         sb.append("SELECT @NEW_ID as number;");

//         return sb.toString();
//     }
//     public String getUpdateString() {
//         StringBuilder sb = new StringBuilder();
//         sb.append(logTable());
//         sb.append("UPDATE recycle_arrival SET");
//         sb.append(" recycle_number = '" + this.getRecycle_number() + "'");
//         sb.append(", arrival_company_id = " + this.getArrival_company_id());
//         sb.append(", arrival_office_id = " + this.getArrival_office_id());
//         sb.append(", arrival_date = '" + this.getArrival_date() + "'");
//         sb.append(logString("更新"));
//         sb.append(" WHERE recycle_arrival_id = " + this.getRecycle_arrival_id() + ";");
//         sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
//         // 変更履歴
//         sb.append("INSERT INTO recycle_arrival_log SELECT * FROM @RecycleArrivalTable;");
//         sb.append("INSERT INTO history (user_name, table_name, state, contents) VALUES (");
//         sb.append("'" + this.getUser_name() + "', 'recycle_arrival', '更新', 'ID=" + this.getRecycle_arrival_id() + "');");
//         // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
//         sb.append("IF @NEW_ID > 0 SET @OLD_ID = " + this.getRecycle_arrival_id() + " ELSE SET @OLD_ID = 0;");
//         sb.append("SELECT @OLD_ID as number;");

//         return sb.toString();
//     }
//     private String logTable() {
//         StringBuilder sb = new StringBuilder();
//         sb.append("DECLARE @RecycleArrivalTable TABLE (");
//         sb.append("editor NVARCHAR(255)");
//         sb.append(", process NVARCHAR(50)");
//         sb.append(", regist_date DATETIME2(7)");
//         sb.append(", recycle_arrival_id INT");
//         sb.append(", recycle_number NVARCHAR(15)");
//         sb.append(", arrivale_date DATE");
//         sb.append(", arrival_company_id INT");
//         sb.append(", arrival_office_id INT");
//         sb.append(", state INT");
//         sb.append(");");
//         sb.append("DECLARE @RecycleTable TABLE (");
//         sb.append("editor NVARCHAR(255)");
//         sb.append(", process NVARCHAR(50)");
//         sb.append(", regist_date DATETIME2(7)");
//         sb.append(", recycle_id INT");
//         sb.append(", recycle_number NVARCHAR(15)");
//         sb.append(", use_date DATE");
//         sb.append(", use_company_id INT");
//         sb.append(", use_office_id INT");
//         sb.append(", maker_code INT");
//         sb.append(", class_code INT");
//         sb.append(", price INT");
//         sb.append(", ex_tax INT");
//         sb.append(", state INT");
//         sb.append(");");

//         return sb.toString();
//     }
//     private String logString(String process) {
//         StringBuilder sb = new StringBuilder();
//         sb.append(" OUTPUT");
//         sb.append(" '" + this.getUser_name() + "'");
//         sb.append(", '" + process + "'");
//         sb.append(", CURRENT_TIMESTAMP");
//         sb.append(", INSERTED.recycle_arrival_id");
//         sb.append(", INSERTED.recycle_number");
//         sb.append(", INSERTED.arrival_date");
//         sb.append(", INSERTED.arrival_company_id");
//         sb.append(", INSERTED.arrival_office_id");
//         sb.append(", INSERTED.state");
//         // sb.append(")");
//         sb.append(" INTO @RecycleArrivalTable (");
//         sb.append("editor");
//         sb.append(", process");
//         sb.append(", regist_date");
//         sb.append(", recycle_arrival_id");
//         sb.append(", recycle_number");
//         sb.append(", arrival_date");
//         sb.append(", arrival_company_id");
//         sb.append(", arrival_office_id");
//         sb.append(", state");
//         sb.append(")");

//         return sb.toString();
//     }
//     private String logString2(String process) {
//         StringBuilder sb = new StringBuilder();
//         sb.append(" OUTPUT");
//         sb.append(" '" + this.getUser_name() + "'");
//         sb.append(", '" + process + "'");
//         sb.append(", CURRENT_TIMESTAMP");
//         sb.append(", INSERTED.recycle_id");
//         sb.append(", INSERTED.recycle_number");
//         sb.append(", INSERTED.use_date");
//         sb.append(", INSERTED.use_company_id");
//         sb.append(", INSERTED.use_office_id");
//         sb.append(", INSERTED.maker_code");
//         sb.append(", INSERTED.class_code");
//         sb.append(", INSERTED.price");
//         sb.append(", INSERTED.ex_tax");
//         sb.append(", INSERTED.state");
//         // sb.append(")");
//         sb.append(" INTO @RecycleTable (");
//         sb.append("editor");
//         sb.append(", process");
//         sb.append(", regist_date");
//         sb.append(", recycle_id");
//         sb.append(", recycle_number");
//         sb.append(", use_date");
//         sb.append(", use_company_id");
//         sb.append(", use_office_id");
//         sb.append(", maker_code");
//         sb.append(", class_code");
//         sb.append(", price");
//         sb.append(", ex_tax");
//         sb.append(", state");
//         sb.append(")");

//         return sb.toString();
//     }
// }
