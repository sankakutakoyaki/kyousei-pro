package kyousei.kyousei.entity.corporation;

import java.sql.ResultSet;
import java.time.LocalDateTime;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.abstracts.CorporationEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyEntity extends CorporationEntity {

    private int company_id;
    private int corporation_id;
    private int category_id;
    private int code;
    private String registration_number;

    private String company_name;
    private String company_name_kana;

    private int version;
    private int state;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.company_id = rs.getInt("company_id");
            this.corporation_id = rs.getInt("corporation_id");
            this.category_id = rs.getInt("category_id");
            this.code = rs.getInt("code");
            this.registration_number = rs.getString("registration_number");
            this.company_name = rs.getString("company_name");
            this.company_name_kana = rs.getString("company_name_kana");
            this.version = rs.getInt("version");
            super.setEntity(rs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder("SELECT c.*, co.* From companies c");
        sb.append(" INNER JOIN corporations co ON co.corporation_id = c.corporation_id");
        sb.append(" WHERE c.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getInsertString());
        sb.append(logTable());
        sb.append("DECLARE @MAX_CODE int = 0;SELECT @MAX_CODE = MAX(code)+1 FROM companies WHERE state = " + Enums.state.UNDECIDED.getNum() + ";");
        sb.append("INSERT INTO companies (");
        sb.append("corporation_id");
        sb.append(", category_id");
        sb.append(", code");
        sb.append(", registration_number");
        sb.append(", company_name");
        sb.append(", company_name_kana");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append("@NEW_ID");
        sb.append(", " + this.getCategory_id());
        sb.append(", @MAX_CODE");
        sb.append(", '" + this.getRegistration_number() + "'");
        sb.append(", '" + this.getCompany_name() + "'");
        sb.append(", '" + this.getCompany_name_kana() + "'");
        sb.append(");");
        // 変更履歴
        sb.append("IF @NEW_ID > 0 BEGIN");
        sb.append(" INSERT INTO companies_log SELECT * FROM @CompanyTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'companies', '作成', 'CORPORATION_ID=' + CONVERT(nvarchar,@NEW_ID));");
        sb.append("SELECT @NEW_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'companies', '作成失敗', '');");
        sb.append(" SELECT 0 as number;");
        sb.append("END;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getUpdateString());
        sb.append(logTable());
        sb.append("UPDATE companies SET");
        sb.append(" corporation_id = " + this.getCorporation_id());
        sb.append(", category_id = " + this.getCategory_id());
        sb.append(", code = " + this.getCode());
        sb.append(", registration_number = '" + this.getRegistration_number() + "'");
        sb.append(", company_name = '" + this.getCompany_name() + "'");
        sb.append(", company_name_kana = '" + this.getCompany_name_kana() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", state = " + this.getState());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(logString("更新"));
        sb.append(" WHERE company_id = " + this.getCompany_id() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO companies_log SELECT * FROM @CompanyTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'companies', '更新', 'COMPANY_ID=" + this.getCompany_id() + "');");
        sb.append("SELECT " + this.getCompany_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'companies', '更新失敗', 'COMPANY_ID=" + this.getCompany_id() + "');");
        sb.append("SELECT 0 as number; END");

        return sb.toString();
    }
    public String getDeleteString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE companies SET state = " + Enums.state.DELETE.getNum());
        sb.append(logString("削除"));
        sb.append(" WHERE company_id " + this.getIds() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO companies_log SELECT * FROM @CompanyTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'companies', '削除', 'COMPANY_ID=" + this.getCompany_id() + "');");
        sb.append("SELECT " + this.getCompany_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'companies', '削除失敗', 'COMPANY_ID=" + this.getCompany_id() + "');");
        sb.append("SELECT 0 as number; END");

        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @CompanyTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", company_id INT");
        sb.append(", corporation_id INT");
        sb.append(", category_id INT");
        sb.append(", code INT");
        sb.append(", registration_number NVARCHAR(20)");
        sb.append(", company_name NVARCHAR(255)");
        sb.append(", company_name_kana NVARCHAR(255)");
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
        sb.append(", INSERTED.company_id");
        sb.append(", INSERTED.corporation_id");
        sb.append(", INSERTED.category_id");
        sb.append(", INSERTED.code");
        sb.append(", INSERTED.registration_number");
        sb.append(", INSERTED.company_name");
        sb.append(", INSERTED.company_name_kana");
        sb.append(", INSERTED.state");
        // sb.append(")");
        sb.append(" INTO @FulltimeTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", company_id");
        sb.append(", corporation_id");
        sb.append(", category_id");
        sb.append(", code");
        sb.append(", registration_number");
        sb.append(", company_name");
        sb.append(", company_name_kana");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}
