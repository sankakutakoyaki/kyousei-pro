package kyousei.kyousei.entity.recycle;

import java.sql.ResultSet;
import java.time.LocalDateTime;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class RecycleClassEntity  implements IEntity {

    private int recycle_class_id;
    private int recycle_class_code;
    private String recycle_class_name;
    private int version;
    private int state;
    private String user_name;

    public void setEntity(ResultSet rs) {
        try {
            this.recycle_class_id = rs.getInt("recycle_class_id");
            this.recycle_class_code = rs.getInt("recycle_class_code");
            this.recycle_class_name = rs.getString("recycle_class_name");
            this.version = rs.getInt("version");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT rc.* From recycle_class rc");
        sb.append(" WHERE rc.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("INSERT INTO recycle_class (");
        sb.append("recycle_class_code");
        sb.append(", recycle_class_name");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append(this.getRecycle_class_code());
        sb.append(", '" + this.getRecycle_class_name() + "'");
        sb.append(");");
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("IF @NEW_ID > 0 BEGIN");
        sb.append(" INSERT INTO recycle_class_log SELECT * FROM @RecycleClassTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_class', '作成', 'CLASS_ID=' + CONVERT(nvarchar,@NEW_ID));");
        sb.append("SELECT @NEW_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_class', '作成失敗', 'CLASS_ID=" + this.getRecycle_class_id() + "');");
        sb.append(" SELECT 0 as number;");
        sb.append("END;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE recycle_class SET");
        sb.append(" recycle_class_code = " + this.getRecycle_class_code());
        sb.append(", recycle_class_name = '" + this.getRecycle_class_name() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(logString("更新"));
        sb.append(" WHERE recycle_class_id = " + this.getRecycle_class_id() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO recycle_class_log SELECT * FROM @RecycleClassTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'recycle_class', '更新', 'CLASS_ID=" + this.getRecycle_class_id() + "');");
        sb.append("SELECT " + this.getRecycle_class_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'recycle_class', '更新失敗', 'CLASS_ID=" + this.getRecycle_class_id() + "');");
        sb.append("SELECT 0 as number; END");

        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @RecycleClassTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", recycle_class_id INT");
        sb.append(", recycle_class_code INT");
        sb.append(", recycle_class_name NVARCHAR(255)");
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
        sb.append(", INSERTED.recycle_class_id");
        sb.append(", INSERTED.recycle_class_code");
        sb.append(", INSERTED.recycle_class_name");
        sb.append(", INSERTED.state");
        // sb.append(")");
        sb.append(" INTO @RecycleClassTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", recycle_class_id");
        sb.append(", recycle_class_code");
        sb.append(", recycle_class_name");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}
