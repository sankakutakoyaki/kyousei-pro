package kyousei.kyousei.entity.recycle;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class RecycleForwardEntity implements IEntity {

    private int recycle_forward_id;
    private int recycle_id;
    private String recycle_number;
    private LocalDate forward_date;
    private int forward_company_id;
    private String forward_company_name;
    private int version;
    private int state;
    private String user_name;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.recycle_forward_id = rs.getInt("recycle_forward_id");
            this.recycle_id = rs.getInt("recycle_id");
            this.recycle_number = rs.getString("recycle_number");
            this.forward_date = rs.getDate("forward_date").toLocalDate();
            this.forward_company_id = rs.getInt("forward_company_id");
            this.forward_company_name = rs.getString("forward_company_name");
            this.version = rs.getInt("version");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private String selectStrings = "SELECT *" +
                                   ", c.company_name as forward_company_name" +
                                   " FROM recycle_forward rf" +
                                   " INNER JOIN recycle r ON r.recycle_id = rf.recycle_id AND rf.state = " + Enums.state.UNDECIDED.getNum() +
                                   " INNER JOIN recycle_delivery rd ON rd.recycle_id = rf.recycle_id AND rd.state = " + Enums.state.UNDECIDED.getNum() +
                                   " LEFT OUTER JOIN companies c ON c.company_id = rf.forward_company_id AND c.state = " + Enums.state.UNDECIDED.getNum();

    public String getSelectString() {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE r.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("INSERT INTO recycle_forward (");
        sb.append("recycle_id");
        sb.append(", recycle_number");
        sb.append(", forward_date");
        sb.append(", forward_company_id");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" SELECT");
        sb.append(" recycle_id");
        sb.append(", recycle_number");
        sb.append(", '" + this.getForward_date() + "' as forward_date");
        sb.append(", '" + this.getForward_company_id() + "' as forward_company_id");
        sb.append(" FROM recycle_forward");
        sb.append(" WHERE recycle_number = '" + this.getRecycle_number() + "' AND state = " + Enums.state.UNDECIDED.getNum() + ";");
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("IF @NEW_ID > 0 BEGIN");
        sb.append(" INSERT INTO recycle_forward_log SELECT * FROM @RecycleForwardTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_forward', '作成', 'RECYCLE_FORWARD_ID=' + CONVERT(nvarchar,@NEW_ID));");
        sb.append("SELECT @NEW_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_forward', '作成失敗', 'RECYCLE_ID=" + this.getRecycle_id() + "');");
        sb.append(" SELECT 0 as number;");
        sb.append("END;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE recycle_forward SET");
        sb.append(" recycle_id = " + this.getRecycle_id());
        sb.append(", recycle_number = '" + this.getRecycle_number() + "'");
        sb.append(", forward_date = '" + this.getForward_date() + "'");
        sb.append(", forward_company_id = " + this.getForward_company_id());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(logString("更新"));
        sb.append(" WHERE recycle_forward_id = " + this.getRecycle_forward_id() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO recycle_forward_log SELECT * FROM @RecycleForwardTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'recycle_forward', '更新', 'RECYCLE_FORWARD_ID=" + this.getRecycle_forward_id() + "');");
        sb.append("SELECT " + this.getRecycle_forward_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'recycle', '更新失敗', 'RECYCLE_FORWARD_ID=" + this.getRecycle_forward_id() + "');");
        sb.append("SELECT 0 as number; END");

        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @RecycleForwardTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", recycle_forward_id INT");
        sb.append(", recycle_id INT");
        sb.append(", recycle_number NVARCHAR(15)");
        sb.append(", forward_date DATE");
        sb.append(", forward_company_id INT");
        sb.append(", state INT");
        sb.append(");");

        return sb.toString();
    }
    private String logString(String process) {
        StringBuilder sb = new StringBuilder();
        sb.append(" OUTPUT");
        sb.append(" '" + this.getUser_name() + "'");
        sb.append(", '" + process + "'");
        sb.append(", CURRENT_TIMESTAMP");
        sb.append(", INSERTED.recycle_forward_id");
        sb.append(", INSERTED.recycle_id");
        sb.append(", INSERTED.recycle_number");
        sb.append(", INSERTED.forward_date");
        sb.append(", INSERTED.forward_company_id");
        sb.append(", INSERTED.state");
        // sb.append(")");
        sb.append(" INTO @RecycleForwardTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", recycle_forward_id");
        sb.append(", recycle_id");
        sb.append(", recycle_number");
        sb.append(", forward_date");
        sb.append(", forward_company_id");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}