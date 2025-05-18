package kyousei.kyousei.entity.recycle;

import java.sql.ResultSet;
import java.time.LocalDateTime;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class RecyclePriceEntity  implements IEntity {

    private int recycle_price_id;
    private int recycle_price_code;
    private int recycle_maker_code;
    private int price;
    private int ex_tax;
    private int version;
    private int state;
    private String user_name;

    public void setEntity(ResultSet rs) {
        try {
            this.recycle_price_id = rs.getInt("recycle_price_id");
            this.recycle_price_code = rs.getInt("recycle_price_code");
            this.recycle_maker_code = rs.getInt("recycle_maker_code");
            this.price = rs.getInt("price");
            this.ex_tax = rs.getInt("ex_tax");
            this.version = rs.getInt("version");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT rp.* From recycle_price rp");
        sb.append(" WHERE rp.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("INSERT INTO recycle_price (");
        sb.append("recycle_price_code");
        sb.append(", recycle_maker_code");
        sb.append(", price");
        sb.append(", ex_tax");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append(this.getRecycle_price_code());
        sb.append(", " + this.getRecycle_maker_code());
        sb.append(", " + this.getPrice());
        sb.append(", " + this.getEx_tax());
        sb.append(");");
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("IF @NEW_ID > 0 BEGIN");
        sb.append(" INSERT INTO recycle_price_log SELECT * FROM @RecyclePriceTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_price', '作成', 'PRICE_ID=' + CONVERT(nvarchar,@NEW_ID));");
        sb.append("SELECT @NEW_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'recycle_price', '作成失敗', 'MAKER_CODE=" + this.getRecycle_maker_code() + "');");
        sb.append(" SELECT 0 as number;");
        sb.append("END;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE recycle_price SET");
        sb.append(" recycle_price_code = " + this.getRecycle_price_code());
        sb.append(", recycle_maker_code = " + this.getRecycle_maker_code());
        sb.append(", price = " + this.getPrice());
        sb.append(", ex_tax = " + this.getEx_tax());
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(logString("更新"));
        sb.append(" WHERE recycle_price_id = " + this.getRecycle_price_id() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO recycle_price_log SELECT * FROM @RecyclePriceTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'recycle_price', '更新', 'PRICE_ID=" + this.getRecycle_price_id() + "');");
        sb.append("SELECT " + this.getRecycle_price_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'recycle', '更新失敗', 'PRICE_ID=" + this.getRecycle_price_id() + "');");
        sb.append("SELECT 0 as number; END");

        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @RecyclePriceTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", recycle_price_id INT");
        sb.append(", recycle_price_code INT");
        sb.append(", recycle_maker_code INT");
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
        sb.append(", INSERTED.recycle_price_id");
        sb.append(", INSERTED.recycle_price_code");
        sb.append(", INSERTED.recycle_maker_code");
        sb.append(", INSERTED.price");
        sb.append(", INSERTED.ex_tax");
        sb.append(", INSERTED.state");
        // sb.append(")");
        sb.append(" INTO @RecyclePriceTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", recycle_price_id");
        sb.append(", recycle_price_code");
        sb.append(", recycle_maker_code");
        sb.append(", price");
        sb.append(", ex_tax");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}
