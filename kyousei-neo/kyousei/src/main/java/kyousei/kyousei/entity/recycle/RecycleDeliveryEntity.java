package kyousei.kyousei.entity.recycle;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class RecycleDeliveryEntity implements IEntity {

    private int recycle_delivery_id;
    private int recycle_id;
    private String recycle_number;
    private LocalDate delivery_date;
    private LocalDate use_date;
    private String maker_name;
    private String class_name;
    private int version;
    private int state;
    private String user_name;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.recycle_delivery_id = rs.getInt("recycle_delivery_id");
            this.recycle_id = rs.getInt("recycle_id");
            this.recycle_number = rs.getString("recycle_number");
            this.use_date = rs.getDate("use_date").toLocalDate();
            this.delivery_date = rs.getDate("delivery_date").toLocalDate();
            this.maker_name = rs.getString("maker_name");
            this.class_name = rs.getString("class_name");   
            this.version = rs.getInt("version");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private String selectStrings = "SELECT *" +
                                   ", r.use_date , rm.recycle_maker_name as maker_name, rc.recycle_class_name as class_name" +
                                   " FROM recycle_delivery rd" +
                                   " INNER JOIN recycle r ON r.recycle_id = rd.recycle_id AND r.state = " + Enums.state.UNDECIDED.getNum() +
                                   " LEFT OUTER JOIN recycle_maker rm ON rm.recycle_maker_code = r.maker_code AND rm.state = " + Enums.state.UNDECIDED.getNum() +
                                   " LEFT OUTER JOIN recycle_class rc ON rc.recycle_class_code = r.class_code AND rc.state = " + Enums.state.UNDECIDED.getNum();

    public String getSelectString() {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE rd.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        // sb.append("DECLARE @RECYCLE_ID int;SET @RECYCLE_ID = (SELECT COUNT(recycle_delivery_id) FROM recycle WHERE recycle_number = '" + this.getRecycle_number() + "' AND state = " + Enums.state.UNDECIDED.getNum() + ");");
        // sb.append("IF @RECYCLE_ID = 0 BEGIN ");
        sb.append(logTable());
        sb.append("INSERT INTO recycle_delivery (");
        sb.append("recycle_id");
        sb.append(", recycle_number");
        sb.append(", delivery_date");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" SELECT");
        sb.append(" recycle_id");
        sb.append(", recycle_number");
        sb.append(", '" + this.getDelivery_date() + "' as delivery_date");
        sb.append(" FROM recycle");
        sb.append(" WHERE recycle_number = '" + this.getRecycle_number() + "' AND state = " + Enums.state.UNDECIDED.getNum() + ";");
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("IF @NEW_ID > 0 BEGIN ");
        sb.append("INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_delivery', '作成', 'ID=' + CONVERT(nvarchar,@NEW_ID));");
        sb.append("INSERT INTO recycle_delivery_log SELECT * FROM @RecycleDeliveryTable;");
        sb.append("SELECT @NEW_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_delivery', '作成失敗', 'RECYCLE_ID=" + this.getRecycle_id() + "');");
        sb.append(" SELECT 0 as number; END;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE recycle_delivery SET");
        sb.append(" recycle_id = " + this.getRecycle_id());
        sb.append(", recycle_number = '" + this.getRecycle_number() + "'");
        sb.append(", delivery_date = '" + this.getDelivery_date() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(logString("更新"));
        sb.append(" WHERE recycle_delivery_id = " + this.getRecycle_delivery_id() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN ");
        sb.append("INSERT INTO recycle_delivery_log SELECT * FROM @RecycleDeliveryTable;");
        sb.append("INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'recycle_delivery', '更新', 'ID=" + this.getRecycle_delivery_id() + "');");
        sb.append("SELECT @ROW_COUNT as number; END");
        sb.append(" ELSE BEGIN");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'recycle_delivery', '更新失敗', 'ID=" + this.getRecycle_delivery_id() + "');");
        sb.append("SELECT 0 as number; END");

        return sb.toString();
    }
    public String getLogTable() {
        return logTable();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @RecycleDeliveryTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", recycle_delivery_id INT");
        sb.append(", recycle_id INT");
        sb.append(", recycle_number NVARCHAR(15)");
        sb.append(", delivery_date DATE");
        sb.append(", state INT");
        sb.append(");");

        return sb.toString();
    }
    public String getLogString(String process) {
        return logString(process);
    }
    private String logString(String process) {
        StringBuilder sb = new StringBuilder();
        sb.append(" OUTPUT");
        sb.append(" '" + this.getUser_name() + "'");
        sb.append(", '" + process + "'");
        sb.append(", CURRENT_TIMESTAMP");
        sb.append(", INSERTED.recycle_delivery_id");
        sb.append(", INSERTED.recycle_id");
        sb.append(", INSERTED.recycle_number");
        sb.append(", INSERTED.delivery_date");
        sb.append(", INSERTED.state");
        // sb.append(")");
        sb.append(" INTO @RecycleDeliveryTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", recycle_delivery_id");
        sb.append(", recycle_id");
        sb.append(", recycle_number");
        sb.append(", delivery_date");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}

