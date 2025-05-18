package kyousei.kyousei.entity.recycle;

import java.sql.ResultSet;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class RecycleLossEntity implements IEntity {

    private int recycle_loss_id;
    private int recycle_id;
    private String recycle_number;
    private String reason;
    private String comment;
    private int version;
    private int state;
    private String user_name;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.recycle_loss_id = rs.getInt("recycle_loss_id");
            this.recycle_id = rs.getInt("recycle_id");
            this.recycle_number = rs.getString("recycle_number");
            this.reason = rs.getString("reason");
            this.comment = rs.getString("comment");
            this.version = rs.getInt("version");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private String selectStrings = "SELECT * FROM recycle_loss rl";

    public String getSelectString() {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE rl.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        // リサイクル使用登録削除
        sb.append("DECLARE @NEW_ID int, @RECYCLE_ID int;");
        sb.append("SET @NEW_ID = (SELECT recycle_loss_id FROM recycle_loss WHERE recycle_number = '" + this.getRecycle_number() + "' AND state = " + Enums.state.UNDECIDED.getNum() + ");");
        sb.append("IF @NEW_ID > 0 BEGIN SELECT 0 as number RETURN END;");
        sb.append("SET @RECYCLE_ID = (SELECT recycle_id FROM recycle WHERE recycle_number = '" + this.getRecycle_number() + "' AND state = " + Enums.state.UNDECIDED.getNum() + ");");
        sb.append("IF @RECYCLE_ID > 0 BEGIN ");
        sb.append(logTable2());
        sb.append(" UPDATE recycle SET state = " + Enums.state.DELETE.getNum());
        sb.append(logString2("ロス処理"));
        sb.append(" WHERE recycle_number = '" + this.getRecycle_number() + "';");
        sb.append(" INSERT INTO recycle_log SELECT * FROM @RecyclelTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle', 'ロス処理', 'RECYCLE_ID=" + this.getRecycle_id() + "'); END");
        sb.append(" ELSE BEGIN ");
        sb.append(logTable2());
        sb.append(" INSERT INTO recycle (recycle_number, state) " + logString2("ロス処理") + " VALUES ('" + this.getRecycle_number() + "', " + Enums.state.DELETE.getNum() + ")");
        sb.append(" SET @RECYCLE_ID = @@IDENTITY;");
        sb.append(" INSERT INTO recycle_log SELECT * FROM @RecyclelTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle', 'ロス処理', 'RECYCLE_ID=" + this.getRecycle_id() + "'); END");
        // ロス処理登録
        sb.append("INSERT INTO recycle_loss (");
        sb.append("recycle_id");
        sb.append(", recycle_number");
        sb.append(", reason");
        sb.append(", comment");
        sb.append(") ");
        sb.append(logString("ロス処理"));
        sb.append(" VALUES (");
        sb.append("@RECYCLE_ID");
        sb.append(", '" + this.getRecycle_number() + "'");
        sb.append(", '" + this.getReason() + "'");
        sb.append(", '" + this.getComment() + "'");
        sb.append(");");
        sb.append("SET @NEW_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("IF @NEW_ID > 0 BEGIN");
        sb.append(" INSERT INTO recycle_loss_log SELECT * FROM @RecycleLossTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_loss', 'ロス処理', 'RECYCLE_LOSS_ID=' + CONVERT(nvarchar,@NEW_ID));");
        sb.append("SELECT @NEW_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_delivery', 'ロス処理失敗', 'RECYCLE_ID=" + this.getRecycle_id() + "');");
        sb.append(" SELECT 0 as number;");
        sb.append("END;");

        return sb.toString();
    }
    // public String getUpdateString() {
    //     StringBuilder sb = new StringBuilder();
    //     sb.append(logTable());
    //     sb.append("UPDATE recycle_loss SET");
    //     sb.append(" recycle_id = " + this.getRecycle_id());
    //     sb.append(", recycle_number = '" + this.getRecycle_number() + "'");
    //     sb.append(", reason = '" + this.getReason() + "'");
    //     sb.append(", comment = '" + this.getComment() + "'");
    //     sb.append(", update_date = '" + LocalDateTime.now() + "'");
    //     int ver = this.getVersion() + 1;
    //     sb.append(", version = " + ver);
    //     sb.append(logString("更新"));
    //     sb.append(" WHERE recycle_loss_id = " + this.getRecycle_loss_id() + " AND version = " + this.getVersion() + ";");
    //     sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
    //     // 変更履歴
    //     sb.append("INSERT INTO recycle_loss_log SELECT * FROM @RecycleLossTable;");
    //     sb.append("INSERT INTO history (user_name, table_name, state, contents) VALUES (");
    //     sb.append("'" + this.getUser_name() + "', 'recycle_loss', '更新', 'ID=" + this.getRecycle_loss_id() + "');");
    //     // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
    //     sb.append("DECLARE @OLD_ID int;IF @NEW_ID > 0 SET @OLD_ID = " + this.getRecycle_loss_id() + " ELSE SET @OLD_ID = 0;");
    //     sb.append("SELECT @OLD_ID as number;");

    //     return sb.toString();
    // }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @RecycleLossTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", recycle_loss_id INT");
        sb.append(", recycle_id INT");
        sb.append(", recycle_number NVARCHAR(15)");
        sb.append(", reason NVARCHAR(50)");
        sb.append(", comment NVARCHAR(50)");
        sb.append(", state INT");
        sb.append(");");
        sb.append("DECLARE @RecycleTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", recycle_id INT");
        sb.append(", recycle_number NVARCHAR(15)");
        sb.append(", use_date DATE");
        sb.append(", use_company_id INT");
        sb.append(", use_office_id INT");
        sb.append(", maker_code INT");
        sb.append(", class_code INT");
        sb.append(", price INT");
        sb.append(", ex_tax INT");
        sb.append(", state INT");
        sb.append(");");

        return sb.toString();
    }
    private String logTable2() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @RecycleTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", recycle_id INT");
        sb.append(", recycle_number NVARCHAR(15)");
        sb.append(", use_date DATE");
        sb.append(", use_company_id INT");
        sb.append(", use_office_id INT");
        sb.append(", maker_code INT");
        sb.append(", class_code INT");
        sb.append(", price INT");
        sb.append(", ex_tax INT");
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
        sb.append(", INSERTED.recycle_loss_id");
        sb.append(", INSERTED.recycle_id");
        sb.append(", INSERTED.recycle_number");
        sb.append(", INSERTED.reason");
        sb.append(", INSERTED.comment");
        sb.append(", INSERTED.state");
        // sb.append(")");
        sb.append(" INTO @RecycleLossTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", recycle_loss_id");
        sb.append(", recycle_id");
        sb.append(", recycle_number");
        sb.append(", reason");
        sb.append(", comment");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
    private String logString2(String process) {
        StringBuilder sb = new StringBuilder();
        sb.append(" OUTPUT");
        sb.append(" '" + this.getUser_name() + "'");
        sb.append(", '" + process + "'");
        sb.append(", CURRENT_TIMESTAMP");
        sb.append(", INSERTED.recycle_id");
        sb.append(", INSERTED.recycle_number");
        sb.append(", INSERTED.use_date");
        sb.append(", INSERTED.use_company_id");
        sb.append(", INSERTED.use_office_id");
        sb.append(", INSERTED.maker_code");
        sb.append(", INSERTED.class_code");
        sb.append(", INSERTED.price");
        sb.append(", INSERTED.ex_tax");
        sb.append(", INSERTED.state");
        // sb.append(")");
        sb.append(" INTO @RecycleTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", recycle_id");
        sb.append(", recycle_number");
        sb.append(", use_date");
        sb.append(", use_company_id");
        sb.append(", use_office_id");
        sb.append(", maker_code");
        sb.append(", class_code");
        sb.append(", price");
        sb.append(", ex_tax");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}

