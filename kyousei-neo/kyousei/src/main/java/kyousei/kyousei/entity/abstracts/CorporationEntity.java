package kyousei.kyousei.entity.abstracts;

import java.sql.ResultSet;

import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class CorporationEntity implements IEntity {

    protected int corporation_id;
    protected String tel_number;
    protected String fax_number;
    protected String postal_code;
    protected String full_address;
    protected String email;
    protected String web_address;
    protected String user_name;
    protected String ids;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.corporation_id = rs.getInt("corporation_id");
            this.tel_number = rs.getString("tel_number");
            this.fax_number = rs.getString("fax_number");
            this.postal_code = rs.getString("postal_code");
            this.full_address = rs.getString("full_address");
            this.email = rs.getString("email");
            this.web_address = rs.getString("web_address");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("INSERT INTO corporations (");
        sb.append("tel_number");
        sb.append(", fax_number");
        sb.append(", postal_code");
        sb.append(", full_address");
        sb.append(", email");
        sb.append(", web_address");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append("'" + this.getTel_number() + "'");
        sb.append(", '" + this.getFax_number() + "'");
        sb.append(", '" + this.getPostal_code() + "'");
        sb.append(", '" + this.getFull_address() + "'");
        sb.append(", '" + this.getEmail() + "'");
        sb.append(", '" + this.getWeb_address() + "'");
        sb.append(");");
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("INSERT INTO corporations_log SELECT * FROM @CorporationTable;");
        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE corporations SET");
        sb.append(" tel_number = '" + this.getTel_number() + "'");
        sb.append(", fax_number = '" + this.getFax_number() + "'");
        sb.append(", postal_code = '" + this.getPostal_code() + "'");
        sb.append(", full_address = '" + this.getFull_address() + "'");
        sb.append(", email = '" + this.getEmail() + "'");
        sb.append(", web_address = '" + this.getWeb_address() + "'");
        sb.append(logString("作成"));
        sb.append(" WHERE corporation_id = " + this.getCorporation_id() + ";");
        // 変更履歴
        sb.append("INSERT INTO corporations_log SELECT * FROM @CorporationTable;");
        sb.append("INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'corporations', '更新', 'ID=" + this.getCorporation_id() + "');");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 SET @OLD_ID = " + this.getCorporation_id() + " ELSE SET @OLD_ID = 0;");
        sb.append("SELECT @OLD_ID as number;");

        return sb.toString();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @OfficeTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", state NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        // sb.append(", fulltime_id INT");
        // sb.append(", employee_id INT");
        // sb.append(", trans_cost INT");
        sb.append(");");

        return sb.toString();
    }
    private String logString(String process) {
        StringBuilder sb = new StringBuilder();
        sb.append(" OUTPUT");
        sb.append(" '" + this.getUser_name() + "'");
        sb.append(", '" + process + "'");
        sb.append(", CURRENT_TIMESTAMP");
        // sb.append(", INSERTED.fulltime_id");
        // sb.append(", INSERTED.employee_id");
        // sb.append(", INSERTED.trans_cost");
        sb.append(")");
        sb.append(" INTO @FulltimeTable (");
        sb.append("editor");
        sb.append(", state");
        sb.append(", regist_date");
        // sb.append(", fulltime_id");
        // sb.append(", employee_id");
        // sb.append(", trans_cost");
        sb.append(")");

        return sb.toString();
    }
}