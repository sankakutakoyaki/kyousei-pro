package kyousei.kyousei.entity.timeworks;

import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kyousei.kyousei.common.Enums;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimeworksPaymentListEntity extends TimeworksPaymentEntity {
    
    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.setTimeworks_id(rs.getInt("timeworks_id"));
            this.setCompany_id(rs.getInt("company_id"));
            this.setCompany_name(rs.getString("company_name"));
            this.setOffice_id(rs.getInt("office_id"));
            this.setOffice_name(rs.getString("office_name"));
            this.setEmployee_id(rs.getInt("employee_id"));
            this.setCode(rs.getInt("code"));
            this.setFull_name(rs.getString("full_name"));
            this.setFull_name_kana(rs.getString("full_name_kana"));
            this.setFull_address(rs.getString("full_address"));
            LocalDate date = rs.getDate("work_date").toLocalDate();
            this.setWork_date(date);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            this.setDay_of_week(Enums.dayOfWeekToStr.getStrByNum(dayOfWeek.getValue()));
            if (rs.getTimestamp("start_time") != null){
                LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                this.setStart_time(startTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString());
            }
            if (rs.getTimestamp("end_time") != null) {
                LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();
                this.setEnd_time(endTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString());
            }
            if (rs.getTimestamp("comp_start_time") != null){
                LocalDateTime startTime = rs.getTimestamp("comp_start_time").toLocalDateTime();
                this.setComp_start_time(startTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString());
            }
            if (rs.getTimestamp("comp_end_time") != null) {
                LocalDateTime endTime = rs.getTimestamp("comp_end_time").toLocalDateTime();
                this.setComp_end_time(endTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString());
            }
            if (rs.getTimestamp("start_regular_hours") != null) {
                LocalDateTime startRegularHours = rs.getTimestamp("start_regular_hours").toLocalDateTime();
                this.setStart_regular_hours(startRegularHours.format(DateTimeFormatter.ofPattern("HH:mm")).toString());
            }
            if (rs.getTimestamp("end_regular_hours") != null) {
                LocalDateTime endRegularHours = rs.getTimestamp("end_regular_hours").toLocalDateTime();
                this.setEnd_regular_hours(endRegularHours.format(DateTimeFormatter.ofPattern("HH:mm")).toString());
            }
            this.setHourly_wage(rs.getInt("hourly_wage"));
            this.setWeekend_hourly_wage(rs.getInt("weekend_hourly_wage"));
            this.setTrans_cost(rs.getInt("trans_cost"));
            if (rs.getTimestamp("regular_breake_time") != null){
                LocalDateTime regularBreakeTime = rs.getTimestamp("regular_breake_time").toLocalDateTime();
                this.setBreake_time(regularBreakeTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString());
            }
            this.setState(rs.getInt("state"));
            this.setPay_state(rs.getInt("pay_state"));
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    @Override
    public String getSelectString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t.timeworks_id, t.employee_id, t.work_date, t.state, t.start_time, t.end_time, t.comp_start_time, t.comp_end_time, t.pay_state");
        sb.append(", p.full_name, p.full_name_kana, p.full_address, c.company_id, c.company_name, o.office_id, o.office_name, e.code");
        sb.append(", pt.hourly_wage, pt.weekend_hourly_wage, pt.trans_cost, pt.start_regular_hours, pt.end_regular_hours, pt.regular_breake_time");
        sb.append(" From timeworks t");
        sb.append(" INNER JOIN parttimes pt ON pt.employee_id = t.employee_id");
        sb.append(" LEFT OUTER JOIN employees e ON e.employee_id = t.employee_id AND e.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN companies c ON c.company_id = e.company_id AND c.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id AND o.state = " + Enums.state.UNDECIDED.getNum());
        sb.append(" LEFT OUTER JOIN timeworks_payment tp ON tp.timeworks_id = t.timeworks_id AND NOT(tp.state = " + Enums.state.DELETE.getNum() + ")");
        sb.append(" WHERE t.state = " + Enums.state.UNDECIDED.getNum());
        return sb.toString();
    }
}
