package com.kyouseipro.kyousei.entity.timeworks;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.kyouseipro.kyousei.interfacies.IEntity;

import lombok.Data;

@Data
public class TimeworksListEntity implements IEntity {

    private int timeworks_id;
    private int employee_id;
    private int category_id;
    private String full_name;
    private String office_name;
    private LocalDate work_date;
    private String comp_start_time;
    private String comp_end_time;

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
        } catch(Exception e) {
            System.out.println(e);
        }
    }

}
