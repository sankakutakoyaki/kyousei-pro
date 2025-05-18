package kyousei.kyousei.entity.data;

import java.sql.ResultSet;

import kyousei.kyousei.interfaceis.IEntity;
import kyousei.kyousei.service.SqlService;
// import kyousei.kyousei.repository.SqlRepository;
import lombok.Data;

@Data
public class HistoryEntity implements IEntity {
    
    private int history_id;
    private String user_name;
    private String table_name;
    private String state;
    private String contents;

    @Override
    public void setEntity(ResultSet rs) {
        try {
            this.history_id= rs.getInt("history_id");
            this.user_name = rs.getString("user_name");
            this.table_name = rs.getString("table_name");
            this.state = rs.getString("state");
            this.contents = rs.getString("contents");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public int saveHistory() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', '" + this.getTable_name() + "', '" + this.getState() + "', '" + this.getContents() + "');");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;SELECT @ROW_COUNT as number;");
        return SqlService.excuteSqlString(sb.toString());
    }
}
