package kyousei.kyousei.entity.recycle;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class RecycleEntity implements IEntity {

    private int recycle_id;
    private String recycle_number;
    private LocalDate use_date;
    private int use_company_id;
    private String use_company_name;
    private int use_office_id;
    private String use_office_name;
    private int maker_code;
    private String maker_name;
    private int class_code;
    private String class_name;    
    private int price;
    private int ex_tax;
    private int version;
    private int state;
    private String user_name;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.recycle_id = rs.getInt("recycle_id");
            this.recycle_number = rs.getString("recycle_number");
            this.use_date = rs.getDate("use_date").toLocalDate();
            this.use_company_id = rs.getInt("use_company_id");
            this.use_company_name = rs.getString("use_company_name");
            this.use_office_id = rs.getInt("use_office_id");
            this.use_office_name = rs.getString("use_office_name");
            this.maker_code = rs.getInt("maker_code");
            this.maker_name = rs.getString("maker_name");
            this.class_code = rs.getInt("class_code");
            this.class_name = rs.getString("class_name");    
            this.price = rs.getInt("price");
            this.ex_tax = rs.getInt("ex_tax");
            this.version = rs.getInt("version");
            this.state = rs.getInt("state");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private String selectStrings = "SELECT *" +
                                   ", c.company_name as use_company_name, o.office_name as use_office_name, rm.recycle_maker_name as maker_name, rc.recycle_class_name as class_name" +
                                   " FROM recycle r" +
                                   " LEFT OUTER JOIN companies c ON c.company_id = r.use_company_id AND c.state = " + Enums.state.UNDECIDED.getNum() +
                                   " LEFT OUTER JOIN offices o ON o.office_id = r.use_office_id AND o.state = " + Enums.state.UNDECIDED.getNum() +
                                   " LEFT OUTER JOIN recycle_maker rm ON rm.recycle_maker_code = r.maker_code AND rm.state = " + Enums.state.UNDECIDED.getNum() +
                                   " LEFT OUTER JOIN recycle_class rc ON rc.recycle_class_code = r.class_code AND rc.state = " + Enums.state.UNDECIDED.getNum();

    public String getSelectString() {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE r.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("INSERT INTO recycle (");
        sb.append("recycle_number");
        sb.append(", use_date");
        sb.append(", use_company_id");
        sb.append(", use_office_id");
        sb.append(", maker_code");
        sb.append(", class_code");
        sb.append(", price");
        sb.append(", ex_tax");
        sb.append(") ");
        sb.append(logString("作成"));
        // sb.append(" VALUES (");
        sb.append(" SELECT ");
        sb.append("'" + this.getRecycle_number() + "'");
        sb.append(", '" + this.getUse_date() + "'");
        sb.append(", " + this.getUse_company_id());
        sb.append(", " + this.getUse_office_id());
        sb.append(", " + this.getMaker_code());
        sb.append(", " + this.getClass_code());
        sb.append(", " + this.getPrice());
        sb.append(", " + this.getEx_tax());
        sb.append(" WHERE NOT EXISTS (SELECT recycle_number FROM recycle WHERE recycle_number = '" + this.getRecycle_number() + "' AND state = " + Enums.state.UNDECIDED.getNum() + ");");
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("IF @NEW_ID > 0 BEGIN");
        sb.append(" INSERT INTO recycle_log SELECT * FROM @RecycleTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle', '作成', 'RECYCLE_ID=' + CONVERT(nvarchar,@NEW_ID));");
        sb.append("SELECT @NEW_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle', '作成失敗', 'お問合せ管理票番号=" + this.getRecycle_number() + "');");
        sb.append(" SELECT 0 as number;");
        sb.append("END;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE recycle SET");
        sb.append(" use_date = '" + this.getUse_date() + "'");
        sb.append(", recycle_number = '" + this.getRecycle_number() + "'");
        sb.append(", use_company_id = " + this.getUse_company_id());
        sb.append(", use_office_id = " + this.getUse_office_id());
        sb.append(", maker_code = " + this.getMaker_code());
        sb.append(", class_code = " + this.getClass_code());
        sb.append(", price = " + this.getPrice());
        sb.append(", ex_tax = " + this.getEx_tax());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(logString("更新"));
        sb.append(" WHERE recycle_id = " + this.getRecycle_id() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO recycle_log SELECT * FROM @RecycleTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'recycle', '更新', 'RECYCLE_ID=" + this.getRecycle_id() + "');");
        sb.append("SELECT " + this.getRecycle_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'recycle', '更新失敗', 'RECYCLE_ID=" + this.getRecycle_id() + "');");
        sb.append("SELECT 0 as number; END");
        
        return sb.toString();
    }
    public String getLogTable() {
        return logTable();
    }
    private String logTable() {
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
    public String getLogString(String process) {
        return logString(process);
    }
    private String logString(String process) {
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

