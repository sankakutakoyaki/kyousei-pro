package com.kyouseipro.kyousei.entity.timeworks;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.kyouseipro.kyousei.interfacies.IEntity;

import lombok.Data;

@Data
public class TimeworksFormEntity implements IEntity {

    private int timeworks_id;
    private int employee_id;
    private int category_id;
    private String full_name;
    private String office_name;
    private LocalDate work_date;
    private String comp_start_time;
    private String comp_end_time;
    private String start_latitude;
    private String start_longitude;
    private String end_latitude;
    private String end_longitude;
    private int version;
    private int state;
    private String time_category;
    
    @Override
    public void setEntity(ResultSet rs) {
        try{
            this.timeworks_id = rs.getInt("timeworks_id");
            this.employee_id = rs.getInt("employee_id");
            this.category_id = rs.getInt("category_id");
            this.full_name = rs.getString("full_name");
            this.office_name = rs.getString("office_name");
            if (rs.getTimestamp("comp_start_time") != null){
                LocalDateTime startTime = rs.getTimestamp("comp_start_time").toLocalDateTime();
                this.comp_start_time = startTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            }
            if (rs.getTimestamp("comp_end_time") != null) {
                LocalDateTime endTime = rs.getTimestamp("comp_end_time").toLocalDateTime();
                this.comp_end_time = endTime.format(DateTimeFormatter.ofPattern("HH:mm")).toString();
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
