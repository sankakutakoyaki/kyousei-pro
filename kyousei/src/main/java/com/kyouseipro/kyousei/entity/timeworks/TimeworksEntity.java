package com.kyouseipro.kyousei.entity.timeworks;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.kyouseipro.kyousei.interfacies.IEntity;

import lombok.Data;

@Data
public class TimeworksEntity implements IEntity {

    private int timeworks_id;
    private int employee_id;
    private LocalDate work_date;
    private String start_time;
    private String end_time;
    private String comp_start_time;
    private String comp_end_time;
    private String start_latitude;
    private String end_latitude;
    private String start_longitude;
    private String end_longitude;
    private int version;
    private int state;

    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.timeworks_id = rs.getInt("timeworks_id");
            this.employee_id = rs.getInt("employee_id");
            this.work_date = rs.getDate("work_date").toLocalDate();
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
            this.start_latitude = rs.getString("start_latitude");
            this.end_latitude = rs.getString("end_latitude");
            this.start_longitude = rs.getString("start_longitude");
            this.end_longitude = rs.getString("end_longitude");
            this.version = rs.getInt("version");
            this.state = rs.getInt("state");
        } catch(Exception e) {
            System.out.println(e);
        }
    }

}

