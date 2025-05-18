package kyousei.kyousei.entity.timeworks;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class TimeworksEntity implements IEntity {

    private int timeworks_id;
    private int employee_id;
    private int category_id;
    private String full_name;
    private String office_name;
    private LocalDate work_date;
    private String start_time;
    private String end_time;
    private String comp_start_time;
    private String comp_end_time;
    private String start_latitude;
    private String start_longitude;
    private String end_latitude;
    private String end_longitude;
    private int version;
    private int print_state;
    private int pay_state;
    private int state;
    // private String time_category;
    private String user_name;
    
    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.timeworks_id = rs.getInt("timeworks_id");
            this.employee_id = rs.getInt("employee_id");
            this.category_id = rs.getInt("category_id");
            this.full_name = rs.getString("full_name");
            this.office_name = rs.getString("office_name");
            this.work_date = rs.getDate("work_date").toLocalDate();
            this.start_time = rs.getTimestamp("start_time").toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            this.end_time = rs.getTimestamp("end_time").toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            this.comp_start_time = rs.getTimestamp("comp_start_time").toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            this.comp_end_time = rs.getTimestamp("comp_end_time").toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            // if (rs.getTimestamp("comp_start_time") != null){
            //     LocalDateTime startTime = rs.getTimestamp("comp_start_time").toLocalDateTime();
            //     this.comp_start_time = startTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            // }
            // if (rs.getTimestamp("comp_end_time") != null) {
            //     LocalDateTime endTime = rs.getTimestamp("comp_end_time").toLocalDateTime();
            //     this.comp_end_time = endTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            // }
            this.start_latitude = rs.getString("start_latitude");
            this.end_latitude = rs.getString("end_latitude");
            this.start_longitude = rs.getString("start_longitude");
            this.end_longitude = rs.getString("end_longitude");
            this.version = rs.getInt("version");
            this.print_state = rs.getInt("print_state");
            this.pay_state = rs.getInt("pay_state");
            this.state = rs.getInt("state");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    private String selectStrings = "SELECT t.timeworks_id, t.employee_id, t.work_date" +
                                   ", COALESCE(t.start_time, '') as start_time" +
                                   ", COALESCE(t.end_time, '') as end_time" +
                                   ", COALESCE(t.comp_start_time, '') as comp_start_time" +
                                   ", COALESCE(t.comp_end_time, '') as comp_end_time" +
                                   ", t.start_latitude, t.end_latitude, t.start_longitude, t.end_longitude, t.version, t.print_state, t.pay_state, t.state" +
                                   ", c.category_id, COALESCE(o.office_name, '登録なし') as office_name, p.full_name" +
                                   " FROM timeworks t" +
                                   " INNER JOIN employees e ON e.employee_id = t.employee_id" +
                                   " INNER JOIN persons p ON p.person_id = e.person_id" +
                                   " LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.UNDECIDED.getNum() +
                                   " LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum();

    public String getSelectString() {
        StringBuilder sb = new StringBuilder(selectStrings);
        sb.append(" WHERE t.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("INSERT INTO timeworks (");
        sb.append("employee_id");
        // sb.append(", category_id");
        sb.append(", work_date");
        if (this.getStart_time() != null ) sb.append(", start_time");
        if (this.getEnd_time() != null ) sb.append(", end_time");
        if (this.getComp_start_time() != null ) sb.append(", comp_start_time");
        if (this.getComp_end_time() != null ) sb.append(", comp_end_time");
        sb.append(", start_latitude");
        sb.append(", end_latitude");
        sb.append(", start_longitude");
        sb.append(", end_longitude");
        sb.append(", pay_state");
        sb.append(") ");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        // sb.append(" SELECT ");
        sb.append(this.getEmployee_id());
        // sb.append(", " + this.getCategory_id());
        sb.append(", '" + this.getWork_date() + "'");
        if (this.getStart_time() != null ) sb.append(", '" + this.getStart_time() + "'");
        if (this.getEnd_time() != null ) sb.append(", '" + this.getEnd_time() + "'");
        if (this.getComp_start_time() != null ) sb.append(", '" + this.getComp_start_time() + "'");
        if (this.getComp_end_time() != null ) sb.append(", '" + this.getComp_end_time() + "'");
        sb.append(", '" + this.getStart_latitude() + "'");
        sb.append(", '" + this.getEnd_latitude() + "'");
        sb.append(", '" + this.getStart_longitude() + "'");
        sb.append(", '" + this.getEnd_longitude() + "'");
        sb.append(", " + this.getPay_state());
        sb.append(");");
        sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        // 変更履歴
        sb.append("IF @NEW_ID > 0 BEGIN");
        sb.append(" INSERT INTO timeworks_log SELECT * FROM @TimeworksTable;");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'timeworks', '作成', 'TIMEWORKS_ID=' + CONVERT(nvarchar,@NEW_ID));");
        sb.append("SELECT @NEW_ID as number; END");
        sb.append(" ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents)");
        sb.append(" VALUES ('" + this.getUser_name() + "', 'timeworks', '作成失敗', '');");
        sb.append(" SELECT 0 as number;");
        sb.append("END;");

        return sb.toString();
    }
    public String getUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE timeworks SET");
        sb.append(" employee_id = " + this.getEmployee_id());
        // sb.append(", category_id = " + this.getCategory_id());
        sb.append(", work_date = '" + this.getWork_date() + "'");
        sb.append(", start_time = '" + this.getStart_time() + "'");
        sb.append(", end_time = '" + this.getEnd_time() + "'");
        sb.append(", comp_start_time = '" + this.getComp_start_time() + "'");
        sb.append(", comp_end_time = '" + this.getComp_end_time() + "'");
        sb.append(", start_latitude = '" + this.getStart_latitude() + "'");
        sb.append(", end_latitude = '" + this.getEnd_latitude() + "'");
        sb.append(", start_longitude = '" + this.getStart_longitude() + "'");
        sb.append(", end_longitude = '" + this.getEnd_longitude() + "'");
        sb.append(", update_date = '" + LocalDateTime.now() + "'");
        sb.append(", pay_state = " + this.getPay_state());
        sb.append(", print_state = " + this.getPrint_state());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(logString("更新"));
        sb.append(" WHERE timeworks_id = " + this.getTimeworks_id() + " AND version = " + this.getVersion() + ";");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO timeworks_log SELECT * FROM @TimeworksTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'timeworks', '更新', 'TIMEWORKS_ID=" + this.getTimeworks_id() + "');");
        sb.append("SELECT " + this.getTimeworks_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'timeworks', '更新失敗', 'TIMEWORKS_ID=" + this.getTimeworks_id() + "');");
        sb.append("SELECT 0 as number; END");

        return sb.toString();
    }
    public String getPrintedString() {
        StringBuilder sb = new StringBuilder();
        // sb.append(logTable());
        sb.append("UPDATE timeworks SET print_state = " + Enums.state.COMPLETE.getNum());
        sb.append(logString("印刷"));
        sb.append(" WHERE timeworks_id = " + this.getTimeworks_id() + ";");

        return sb.toString();
    }
    public String getLogTable() {
        return logTable();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @TimeworksTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", timeworks_id INT");
        sb.append(", employee_id INT");
        // sb.append(", category_id INT");
        sb.append(", work_date DATE");
        sb.append(", start_time NVARCHAR(50)");
        sb.append(", end_time NVARCHAR(50)");
        sb.append(", comp_start_time NVARCHAR(50)");
        sb.append(", comp_end_time NVARCHAR(50)");
        sb.append(", start_latitude NVARCHAR(50)");
        sb.append(", end_latitude NVARCHAR(50)");
        sb.append(", start_longitude NVARCHAR(50)");
        sb.append(", end_longitude NVARCHAR(50)");
        sb.append(", print_state INT");
        sb.append(", pay_state INT");
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
        sb.append(", INSERTED.timeworks_id");
        sb.append(", INSERTED.employee_id");
        // sb.append(", INSERTED.category_id");
        sb.append(", INSERTED.work_date");
        sb.append(", INSERTED.start_time");
        sb.append(", INSERTED.end_time");
        sb.append(", INSERTED.comp_start_time");
        sb.append(", INSERTED.comp_end_time");
        sb.append(", INSERTED.start_latitude");
        sb.append(", INSERTED.end_latitude");
        sb.append(", INSERTED.start_longitude");
        sb.append(", INSERTED.end_longitude");
        sb.append(", INSERTED.print_state");
        sb.append(", INSERTED.pay_state");
        sb.append(", INSERTED.state");
        // sb.append(")");
        sb.append(" INTO @TimeworksTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", timeworks_id");
        sb.append(", employee_id");
        // sb.append(", category_id");
        sb.append(", work_date");
        sb.append(", start_time");
        sb.append(", end_time");
        sb.append(", comp_start_time");
        sb.append(", comp_end_time");
        sb.append(", start_latitude");
        sb.append(", end_latitude");
        sb.append(", start_longitude");
        sb.append(", end_longitude");
        sb.append(", print_state");
        sb.append(", pay_state");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}