package kyousei.kyousei.entity.timeworks;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.interfaceis.IEntity;
import lombok.Data;

@Data
public class TimeworksPaymentEntity implements IEntity {

    private int timeworks_payment_id;
    private int timeworks_id;
    private int company_id;
    private String company_name;
    private int office_id;
    private String office_name;
    private int employee_id;    
    private int code;
    private String full_name;
    private String full_name_kana;
    private String full_address;
    private int payment_employee_id;
    private int bank_id;

    private LocalDate work_date;
    private String day_of_week;
    private String start_time;
    private String end_time;
    private String comp_start_time;
    private String comp_end_time;
    private String start_regular_hours;
    private String end_regular_hours;
    private int hourly_wage;
    private int weekend_hourly_wage;
    private int trans_cost;
    private int regular_trans_cost;
    private String regular_breake_time;
    private String breake_time;
    private int breake_time_num;
    private String work_time;
    private int work_time_num;
    private String overtime;
    private String nighttime;
    private int work_time_unit;
    private int overtime_unit;
    private int nighttime_unit;
    private int real_work_time_unit;
    private int overtime_pay;
    private int nighttime_pay;
    private String real_work_time;    
    private int realtime_pay;
    private int total_amount;
    private int withholding_tax;
    private int payment;
    private LocalDate payment_date;
    private int version;
    private int print_state;
    private int pay_state;
    private int state;
    private String user_name;
    

    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.timeworks_payment_id = rs.getInt("timeworks_payment_id");
            this.timeworks_id = rs.getInt("timeworks_id");
            this.company_id = rs.getInt("company_id");
            this.company_name = rs.getString("company_name");
            this.office_id = rs.getInt("office_id");
            this.office_name = rs.getString("office_name");
            this.employee_id = rs.getInt("employee_id");
            this.code = rs.getInt("code");
            this.full_name = rs.getString("full_name");
            this.full_name_kana = rs.getString("full_name_kana");
            this.full_address = rs.getString("full_address");
            this.work_date = rs.getDate("work_date").toLocalDate();
            this.day_of_week = rs.getString("day_of_week");
            if (rs.getTimestamp("start_time") != null){
                LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                this.start_time = startTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            if (rs.getTimestamp("end_time") != null) {
                LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();
                this.end_time = endTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            if (rs.getTimestamp("comp_start_time") != null){
                LocalDateTime compStartTime = rs.getTimestamp("comp_start_time").toLocalDateTime();
                this.comp_start_time = compStartTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            if (rs.getTimestamp("comp_end_time") != null) {
                LocalDateTime compEndTime = rs.getTimestamp("comp_end_time").toLocalDateTime();
                this.comp_end_time = compEndTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            if (rs.getTimestamp("start_regular_hours") != null) {
                LocalDateTime startRegularHours = rs.getTimestamp("start_regular_hours").toLocalDateTime();
                this.start_regular_hours = startRegularHours.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            if (rs.getTimestamp("end_regular_hours") != null) {
                LocalDateTime endRegularHours = rs.getTimestamp("end_regular_hours").toLocalDateTime();
                this.end_regular_hours = endRegularHours.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            this.hourly_wage = rs.getInt("hourly_wage");
            this.weekend_hourly_wage = rs.getInt("weekend_hourly_wage");
            this.trans_cost = rs.getInt("trans_cost");
            this.regular_trans_cost = rs.getInt("regular_trans_cost");
            if (rs.getTimestamp("regular_breake_time") != null){
                LocalDateTime regularBreakeTime = rs.getTimestamp("regular_breake_time").toLocalDateTime();
                this.regular_breake_time = regularBreakeTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            if (rs.getTimestamp("breake_time") != null){
                LocalDateTime breakeTime = rs.getTimestamp("breake_time").toLocalDateTime();
                this.breake_time = breakeTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            this.breake_time_num = rs.getInt("breake_time_num");
            if (rs.getTimestamp("work_time") != null){
                LocalDateTime workTime = rs.getTimestamp("work_time").toLocalDateTime();
                this.work_time = workTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            this.work_time_num = rs.getInt("work_time_num");
            if (rs.getTimestamp("overtime") != null){
                LocalDateTime overTime = rs.getTimestamp("overtime").toLocalDateTime();
                this.overtime = overTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            if (rs.getTimestamp("nighttime") != null){
                LocalDateTime nightTime = rs.getTimestamp("nighttime").toLocalDateTime();
                this.nighttime = nightTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            this.work_time_unit = rs.getInt("work_time_unit");
            this.overtime_unit = rs.getInt("overtime_unit");
            this.nighttime_unit = rs.getInt("nighttime_unit");
            this.real_work_time_unit = rs.getInt("real_work_time_unit");
            this.overtime_pay = rs.getInt("overtime_pay");
            this.nighttime_pay = rs.getInt("nighttime_pay");
            if (rs.getTimestamp("real_work_time") != null){
                LocalDateTime realWorkTime = rs.getTimestamp("real_work_time").toLocalDateTime();
                this.real_work_time = realWorkTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            this.realtime_pay = rs.getInt("realtime_pay");
            this.total_amount = rs.getInt("total_amount");
            this.withholding_tax = rs.getInt("withholding_tax");
            this.payment = rs.getInt("payment");
            this.payment_date = rs.getDate("payment_date").toLocalDate();
            this.version = rs.getInt("version");
            this.print_state = rs.getInt("print_state");
            this.state = rs.getInt("state");
            // this.state_str = Enums.state.getStrByNum(this.state);
            this.pay_state = rs.getInt("pay_state");
            // this.pay_state_str = Enums.state.getStrByNum(this.pay_state);
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t.timeworks_id, t.employee_id, t.work_date, t.start_time, t.end_time, t.comp_start_time, t.comp_end_time, t.print_state, t.pay_state");
        sb.append(", tp.timeworks_payment_id, tp.day_of_week, tp.trans_cost, tp.breake_time, tp.breake_time_num, tp.work_time, tp.work_time_num");
        sb.append(", tp.overtime, tp.nighttime, tp.real_work_time, tp.work_time_unit, tp.overtime_unit, tp.nighttime_unit, tp.real_work_time_unit, tp.version, tp.state");
        sb.append(", tp.hourly_wage, tp.overtime_pay, tp.nighttime_pay, tp.realtime_pay, tp.total_amount, tp.withholding_tax, tp.payment, tp.payment_date");
        sb.append(", p.full_name, p.full_name_kana, p.full_address, c.company_id, c.company_name, o.office_id, o.office_name, e.code");
        sb.append(", pt.hourly_wage, pt.weekend_hourly_wage, pt.trans_cost as regular_trans_cost, pt.start_regular_hours, pt.end_regular_hours, pt.regular_breake_time");
        sb.append(" From timeworks t");
        sb.append(" INNER JOIN timeworks_payment tp ON tp.timeworks_id = t.timeworks_id AND NOT(tp.state = " + Enums.state.DELETE.getNum() + ")");
        sb.append(" INNER JOIN parttimes pt ON pt.employee_id = t.employee_id");
        sb.append(" LEFT OUTER JOIN employees e ON e.employee_id = t.employee_id AND e.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" WHERE t.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
    public String getInsertString() {
        StringBuilder sb = new StringBuilder();
        // sb.append("INSERT INTO timeworks (");
        // sb.append("employee_id, work_date, comp_start_time, comp_end_time");
        // sb.append(") VALUES (");
        // sb.append(this.getEmployee_id() + ", '" + this.getWork_date() + "', '" + this.getComp_start_time() + "', '" + this.getComp_end_time() + "');");
        // sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        // sb.append(logTable());
        sb.append("INSERT INTO timeworks_payment (");
        sb.append("timeworks_id");
        sb.append(", full_name");
        sb.append(", full_address");
        sb.append(", work_date");
        sb.append(", day_of_week");
        sb.append(", hourly_wage");
        sb.append(", trans_cost");
        sb.append(", breake_time");
        sb.append(", breake_time_num");
        sb.append(", work_time");
        sb.append(", work_time_num");
        sb.append(", overtime");
        sb.append(", nighttime");
        sb.append(", work_time_unit");
        sb.append(", overtime_unit");
        sb.append(", nighttime_unit");
        sb.append(", real_work_time_unit");
        sb.append(", overtime_pay");
        sb.append(", nighttime_pay");
        sb.append(", real_work_time");
        sb.append(", realtime_pay");
        sb.append(", total_amount");
        sb.append(", withholding_tax");
        sb.append(", payment");
        // sb.append(", pay_state");
        // sb.append(", print_state");
        sb.append(", state");
        sb.append(")");
        sb.append(logString("作成"));
        sb.append(" VALUES (");
        sb.append(this.getTimeworks_id());
        sb.append(", '" + this.getFull_name() + "'");
        sb.append(", '" + this.getFull_address() + "'");
        sb.append(", '" + this.getWork_date() + "'");
        sb.append(", '" + this.getDay_of_week() + "'");
        sb.append(", " + this.getHourly_wage());
        sb.append(", " + this.getTrans_cost());
        sb.append(", '" + this.getBreake_time() + "'");
        sb.append(", " + this.getBreake_time_num());
        sb.append(", '" + this.getWork_time() + "'");
        sb.append(", " + this.getWork_time_num());
        sb.append(", '" + this.getOvertime() + "'");
        sb.append(", '" + this.getNighttime() + "'");
        sb.append(", " + this.getWork_time_unit());
        sb.append(", " + this.getOvertime_unit());
        sb.append(", " + this.getNighttime_unit());
        sb.append(", " + this.getReal_work_time_unit());
        sb.append(", " + this.getOvertime_pay());
        sb.append(", " + this.getNighttime_pay());
        sb.append(", '" + this.getReal_work_time() + "'");
        sb.append(", " + this.getRealtime_pay());
        sb.append(", " + this.getTotal_amount());
        sb.append(", " + this.getWithholding_tax());
        sb.append(", " + this.getPayment());
        // sb.append(", " + this.getPay_state());
        // sb.append(", " + this.getPrint_state());
        sb.append(", " + this.getState());
        sb.append(");");
        // sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");
        // // 変更履歴
        // sb.append("INSERT INTO history (user_name, table_name, state, contents)");
        // sb.append(" VALUES ('" + this.getUser_name() + "', 'timeworks_payment', '作成', 'ID=' + CONVERT(nvarchar,@NEW_ID));");
        // sb.append("INSERT INTO timeworks_payment_log SELECT * FROM @TimeworksPaymentTable;");
        // // 新規IDを返す
        // sb.append("SELECT @NEW_ID as number;");

        return sb.toString();
    }
    public String getUpdateString() {
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS"));
        StringBuilder sb = new StringBuilder();
        sb.append(logTable());
        sb.append("UPDATE timeworks_payment SET");
        sb.append(" full_name = '" + this.getFull_name() + "'");
        sb.append(", full_address = '" + this.getFull_address() + "'");
        sb.append(", work_date = '" + this.getWork_date() + "'");
        sb.append(", day_of_week = '" + this.getDay_of_week() + "'");
        sb.append(", hourly_wage = " + this.getHourly_wage());
        sb.append(", trans_cost = " + this.getTrans_cost());
        sb.append(", breake_time = '" + this.getBreake_time() + "'");
        sb.append(", breake_time_num = " + this.getBreake_time_num());
        sb.append(", work_time = '" + this.getWork_time() + "'");
        sb.append(", work_time_num = " + this.getWork_time_num());
        sb.append(", overtime = '" + this.getOvertime() + "'");
        sb.append(", nighttime = '" + this.getNighttime() + "'");
        sb.append(", work_time_unit = " + this.getWork_time_unit());
        sb.append(", overtime_unit = " + this.getOvertime_unit());
        sb.append(", nighttime_unit = " + this.getNighttime_unit());
        sb.append(", real_work_time_unit = " + this.getReal_work_time_unit());
        sb.append(", overtime_pay = " + this.getOvertime_pay());
        sb.append(", nighttime_pay = " + this.getNighttime_pay());
        sb.append(", real_work_time = '" + this.getReal_work_time() + "'");
        sb.append(", realtime_pay = " + this.getRealtime_pay());
        sb.append(", total_amount = " + this.getTotal_amount());
        sb.append(", withholding_tax = " + this.getWithholding_tax());
        sb.append(", payment = " + this.getPayment());
        sb.append(", payment_date = '" + datetime + "'");
        // sb.append(", pay_state = " + this.getPay_state());
        // sb.append(", print_state = " + this.getPrint_state());
        int ver = this.getVersion() + 1;
        sb.append(", version = " + ver);
        sb.append(", state = " + this.getState());
        sb.append(logString("更新"));
        sb.append(" WHERE timeworks_payment_id = " + this.getTimeworks_payment_id() + " AND version = " + this.getVersion());
        sb.append(" AND (state = " + Enums.state.UNDECIDED.getNum() + " OR state = " + Enums.state.COMFILM.getNum() + ");");
        sb.append("DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;");
        // 更新が成功していれば対象エンティティのIDを返す、失敗していれば０を返す
        sb.append("IF @ROW_COUNT > 0 BEGIN");
        sb.append(" INSERT INTO timeworks_payment_log SELECT * FROM @TimeworksPaymentTable;");        
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'timeworks_payment', '更新', 'TIMEWORKS_PAYMENT_ID=" + this.getTimeworks_payment_id() + "');");
        sb.append("SELECT " + this.getTimeworks_id() + " as number;");
        sb.append(" END ELSE BEGIN");
        sb.append(" INSERT INTO history (user_name, table_name, state, contents) VALUES (");
        sb.append("'" + this.getUser_name() + "', 'timeworks_payment', '更新失敗', 'TIMEWORKS_PAYMENT_ID=" + this.getTimeworks_payment_id() + "');");
        sb.append("SELECT 0 as number; END");

        return sb.toString();
    }
    public String getLogTable() {
        return logTable();
    }
    private String logTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("DECLARE @TimeworksPaymentTable TABLE (");
        sb.append("editor NVARCHAR(255)");
        sb.append(", process NVARCHAR(50)");
        sb.append(", regist_date DATETIME2(7)");
        sb.append(", timeworks_payment_id INT");
        sb.append(", timeworks_id INT");
        sb.append(", full_name NVARCHAR(255)");
        sb.append(", full_address NVARCHAR(255)");
        sb.append(", work_date DATE");
        sb.append(", day_of_week NVARCHAR(5)");
        sb.append(", hourly_wage INT");
        sb.append(", trans_cost INT");
        sb.append(", breake_time DATETIME2(7)");
        sb.append(", breake_time_num INT");
        sb.append(", work_time DATETIME2(7)");
        sb.append(", work_time_num INT");
        sb.append(", overtime DATETIME2(7)");
        sb.append(", nighttime DATETIME2(7)");
        sb.append(", work_time_unit INT");
        sb.append(", overtime_unit INT");
        sb.append(", nighttime_unit INT");
        sb.append(", real_work_time_unit INT");
        sb.append(", overtime_pay INT");
        sb.append(", nighttime_pay INT");
        sb.append(", real_work_time DATETIME2(7)");
        sb.append(", realtime_pay INT");
        sb.append(", total_amount INT");
        sb.append(", withholding_tax INT");
        sb.append(", payment INT");
        sb.append(", payment_date DATETIME2(7)");
        // sb.append(", pay_state INT");
        // sb.append(", print_state INT");
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
        sb.append(", INSERTED.timeworks_payment_id");
        sb.append(", INSERTED.timeworks_id");
        sb.append(", INSERTED.full_name");
        sb.append(", INSERTED.full_address");
        sb.append(", INSERTED.work_date");
        sb.append(", INSERTED.day_of_week");
        sb.append(", INSERTED.hourly_wage");
        sb.append(", INSERTED.trans_cost");
        sb.append(", INSERTED.breake_time");
        sb.append(", INSERTED.breake_time_num ");
        sb.append(", INSERTED.work_time");
        sb.append(", INSERTED.work_time_num");
        sb.append(", INSERTED.overtime");
        sb.append(", INSERTED.nighttime");
        sb.append(", INSERTED.work_time_unit");
        sb.append(", INSERTED.overtime_unit");
        sb.append(", INSERTED.nighttime_unit");
        sb.append(", INSERTED.real_work_time_unit");
        sb.append(", INSERTED.overtime_pay");
        sb.append(", INSERTED.nighttime_pay");
        sb.append(", INSERTED.real_work_time");
        sb.append(", INSERTED.realtime_pay");
        sb.append(", INSERTED.total_amount");
        sb.append(", INSERTED.withholding_tax");
        sb.append(", INSERTED.payment");
        sb.append(", INSERTED.payment_date");
        // sb.append(", INSERTED.pay_state");
        // sb.append(", INSERTED.print_state");
        sb.append(", INSERTED.state");
        sb.append(" INTO @TimeworksPaymentTable (");
        sb.append("editor");
        sb.append(", process");
        sb.append(", regist_date");
        sb.append(", timeworks_payment_id");
        sb.append(", timeworks_id");
        sb.append(", full_name");
        sb.append(", full_address");
        sb.append(", work_date");
        sb.append(", day_of_week");
        sb.append(", hourly_wage");
        sb.append(", trans_cost");
        sb.append(", breake_time");
        sb.append(", breake_time_num ");
        sb.append(", work_time");
        sb.append(", work_time_num");
        sb.append(", overtime");
        sb.append(", nighttime");
        sb.append(", work_time_unit");
        sb.append(", overtime_unit");
        sb.append(", nighttime_unit");
        sb.append(", real_work_time_unit");
        sb.append(", overtime_pay");
        sb.append(", nighttime_pay");
        sb.append(", real_work_time");
        sb.append(", realtime_pay");
        sb.append(", total_amount");
        sb.append(", withholding_tax");
        sb.append(", payment");
        sb.append(", payment_date");
        // sb.append(", pay_state");
        // sb.append(", print_state");
        sb.append(", state");
        sb.append(")");

        return sb.toString();
    }
}

