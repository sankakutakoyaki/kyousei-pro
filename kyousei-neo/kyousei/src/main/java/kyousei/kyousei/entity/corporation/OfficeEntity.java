package kyousei.kyousei.entity.corporation;

import java.sql.ResultSet;
import java.time.LocalDateTime;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.abstracts.CorporationEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OfficeEntity extends CorporationEntity {

    private int office_id;
    private int company_id;
    private int corporation_id;
    private String company_name;
    private int code;
    private String office_name;
    private String office_name_kana;
    private int version;
    private int state;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.office_id = rs.getInt("office_id");
            this.company_id = rs.getInt("company_id");
            this.corporation_id = rs.getInt("corporation_id");
            this.company_name = rs.getString("company_name");
            this.code = rs.getInt("code");
            this.office_name = rs.getString("office_name");
            this.office_name_kana = rs.getString("office_name_kana");
            this.version = rs.getInt("version");
            super.setEntity(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT o.*, co.*, c.company_name From offices o");
        sb.append(" INNER JOIN corporations co ON co.corporation_id = o.corporation_id");
        sb.append(" INNER JOIN companies c ON c.company_id = o.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE o.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInsertString());
        sb.append(logTable());
        sb.append("DECLARE @MAX_CODE int = 0;SELECT @MAX_CODE = MAX(code)+1 FROM offices WHERE state = " + Enums.state.UNDECIDED.getNum() + ";");
        sb.append("INSERT INTO offices (");
        sb.append("company_id");
        sb.append(", corporation_id");
        sb.append(", code");
        sb.append(", office_name");
        sb.append(", office_name_kana");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append(this.getCompany_id());
        sb.append(", @NEW_ID");
        sb.append(", @MAX_CODE");
        sb.append(", '" + this.getOffice_name() + "'");
        sb.append(", '" + this.getOffice_name_kana() + "'");
        sb.append(");");
        // 変更履歴
        sb.append("IF @NEW_ID > 0 BEGIN");
        sb.append(" INSERT INTO offices_log SELECT * FROM @OfficeTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'offices', '作成', 'CORPORATION_ID=' + CONVERT(nvarchar,@NEW_ID));");
        sb.append("SELECT @NEW_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'offices', '作成失敗', '');");
        sb.append(" SELECT 0 as number;");
        sb.append("END;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getUpdateString());
        sb.append(logTable());
        sb.append("UPDATE offices SET");
        sb.append(" company_id = " + this.getCompany_id());
        sb.append(", corporation_id = " + this.getCorporation_id());
        sb.append(", code = " + this.getCode());
        sb.append(", office_name = '" + this.getOffice_name() + "'");
        sb.append(", office_name_kana = '" + this.getOffice_name_kana() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", state = " + this.getState());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(logString("更新"));
        sb.append(" WHERE office_id = " + this.getOffice_id() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO offices_log SELECT * FROM @OfficeTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'offices', '更新', 'OFFICE_ID=" + this.getOffice_id() + "');");
        sb.append("SELECT " + this.getOffice_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'offices', '更新失敗', 'OFFICE_ID=" + this.getOffice_id() + "');");
        sb.append("SELECT 0 as number; END");

        return sb.toString();
    }
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE offices SET state = " + Enums.state.DELETE.getNum());
        sb.append(logString("削除"));
        sb.append(" WHERE office_id " + this.getIds() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO offices_log SELECT * FROM @OfficeTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'offices', '更新', 'OFFICE_ID=" + this.getOffice_id() + "');");
        sb.append("SELECT " + this.getOffice_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'offices', '更新失敗', 'OFFICE_ID=" + this.getOffice_id() + "');");
        sb.append("SELECT 0 as number; END");
        
        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @OfficeTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", office_id INT");
        sb.append(", corporation_id INT");
        sb.append(", company_id INT");
        sb.append(", code INT");
        sb.append(", office_name NVARCHAR(255)");
        sb.append(", office_name_kana NVARCHAR(255)");
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
        sb.append(", INSERTED.office_id");
        sb.append(", INSERTED.corporation_id");
        sb.append(", INSERTED.company_id");
        sb.append(", INSERTED.code");
        sb.append(", INSERTED.office_name");
        sb.append(", INSERTED.office_name_kana");
        sb.append(", INSERTED.state");
        // sb.append(")");
        sb.append(" INTO @FulltimeTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", office_id");
        sb.append(", corporation_id");
        sb.append(", company_id");
        sb.append(", code");
        sb.append(", office_name");
        sb.append(", office_name_kana");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}
