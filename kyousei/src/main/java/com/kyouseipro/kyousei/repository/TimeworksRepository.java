package com.kyouseipro.kyousei.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kyouseipro.kyousei.common.Enums;
import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.entity.employee.EmployeeEntity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksEntity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksFormEntity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksFullTimeEneity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksListEntity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksPaymentEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.interfacies.ISql;

@Repository
public class TimeworksRepository implements ISql {

    /**
     * 全従業員の今日の勤怠データリスト取得
     * @return List<IEntity>
     */
    public List<IEntity> getAttendanceDataForEveryoneOnTheDay() {
       StringBuilder sb = new StringBuilder();
        sb.append("SELECT t.*, o.office_name, p.full_name, e.category_id From timeworks t");
        sb.append(" INNER JOIN employees e ON e.employee_id = t.employee_id");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id");
        sb.append(" WHERE t.state = " + Enums.state.INITIAL.getNum());
        LocalDate date = LocalDate.now();
        sb.append(" AND t.work_date = '" + date + "'");
        sb.append(" ORDER BY o.office_name");

        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new TimeworksListEntity());
        sqlData.setSqlString(sb.toString());
        return getList(sqlData);
    }

    /**
     * 今日の個人勤怠データ取得
     * @param employee_id
     * @return IEntity
     */
    public IEntity getEmployeeAttendanceData(int employee_id) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t.*, o.office_name, p.full_name, e.category_id From timeworks t");
        sb.append(" INNER JOIN employees e ON e.employee_id = t.employee_id");
        sb.append(" INNER JOIN persons p ON p.person_id = e.person_id");
        sb.append(" LEFT OUTER JOIN offices o ON o.office_id = e.office_id");
        sb.append(" WHERE t.state = " + Enums.state.INITIAL.getNum());
        LocalDate date = LocalDate.now();
        sb.append(" AND t.work_date = '" + date + "' AND t.employee_id = " + employee_id);
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new TimeworksListEntity());
        sqlData.setSqlString(sb.toString());
        TimeworksListEntity entity = (TimeworksListEntity) getEntity(sqlData);
        if (entity.getTimeworks_id() == 0) {
            EmployeeRepository repository = new EmployeeRepository();
            EmployeeEntity emp = (EmployeeEntity)repository.getEmployeeForId(employee_id);
            entity.setEmployee_id(employee_id);
            entity.setCategory_id(emp.getCategory_id());
            entity.setFull_name(emp.getFull_name());
        }
        return entity;
    }

    /**
     * 個人勤怠データの存在確認
     * @param employee_id
     * @return データがなければ[True]を返す
     */
    public boolean checkEmployeeAttendanceData(int employee_id, String date) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * From timeworks WHERE state = " + Enums.state.INITIAL.getNum());
        sb.append(" AND work_date = '" + date + "' AND employee_id = " + employee_id);
        SqlData sqlData = new SqlData();
        sqlData.setClassPathFromEntity(new TimeworksEntity());
        sqlData.setSqlString(sb.toString());
        IEntity entity = getEntity(sqlData);
        if (entity == null || ((TimeworksEntity)entity).getTimeworks_id() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * 出勤時間打刻　打刻済みの場合は上書き
     * @param query
     * @return 成功すれば[TRUE]を返す
     */
    public boolean registTodaysEmployeeAttendanceData(TimeworksFormEntity query, boolean newState) {
        LocalDate date = LocalDate.now();
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS"));
        
        String latitude = query.getTime_category().equals("start") ? query.getStart_latitude(): query.getEnd_latitude();
        String longitude = query.getTime_category().equals("start") ? query.getStart_longitude(): query.getEnd_longitude();
        
        // IDが0の時は新規作成、それ以外は更新
        StringBuilder sb = new StringBuilder();
        if (query.getTimeworks_id() > 0) {
            sb.append("UPDATE timeworks SET");
            if (newState == true) {

                sb.append(" start_time = '" + query.getComp_start_time() + "'");
                sb.append(", comp_start_time = '" + query.getComp_start_time() + "'");
                sb.append(", start_latitude = '" + latitude + "'");
                sb.append(", start_longitude = '" + longitude + "'");
                sb.append(", end_time = '" + query.getComp_end_time() + "'");
                sb.append(", comp_end_time = '" + query.getComp_end_time() + "'");
                sb.append(", end_latitude = '" + latitude + "'");
                sb.append(", end_longitude = '" + longitude + "'");
                sb.append(" WHERE employee_id = " + query.getEmployee_id() + " AND work_date = '" + query.getWork_date() + "';"); 
            } else {
                sb.append(" " + query.getTime_category() + "_time = '" + datetime + "'");

                sb.append(", comp_" + query.getTime_category() + "_time = '" + datetime + "'");
                sb.append(", " + query.getTime_category() + "_latitude = '" + latitude + "'");
                sb.append(", " + query.getTime_category() + "_longitude = '" + longitude + "'");
                sb.append(" WHERE employee_id = " + query.getEmployee_id() + " AND work_date = '" + date + "';");                
            }

        } else {
            if (newState == true) {
                sb.append("INSERT INTO timeworks (");
                sb.append("employee_id, work_date, start_time, comp_start_time, start_latitude, start_longitude, end_time, comp_end_time, end_latitude, end_longitude");
                sb.append(") VALUES (");
                sb.append(query.getEmployee_id() + ", '" + query.getWork_date() + "', '" + query.getComp_start_time() + "', '" + query.getComp_start_time() + "', '" + latitude + "', '" + longitude + "'");
                sb.append(", '" + query.getComp_end_time() + "', '" + query.getComp_end_time() + "', '" + latitude + "', '" + longitude + "');");
            } else {
                sb.append("INSERT INTO timeworks (");
                sb.append("employee_id, work_date, " + query.getTime_category() + "_time, comp_" + query.getTime_category() + "_time, " + query.getTime_category() + "_latitude, " + query.getTime_category() + "_longitude");
                sb.append(") VALUES (");
                sb.append(query.getEmployee_id() + ", '" + date + "', '" + datetime + "', '" + datetime + "', '" + latitude + "', '" + longitude + "');");
            }

            if (query.getCategory_id() == Enums.employeeCategory.PARTTIME.getNum()) {
                sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");

                sb.append("DECLARE @HOURLY_WAGE int = 0, @WEEKEND_HOURLY_WAGE int = 0, @TRANS_COST int = 0;");
                sb.append("SELECT @HOURLY_WAGE = hourly_wage, @WEEKEND_HOURLY_WAGE = weekend_hourly_wage, @TRANS_COST = trans_cost FROM parttimes WHERE state = " + Enums.state.INITIAL.getNum());
                sb.append(" AND employee_id = " + query.getEmployee_id() + ";");

                sb.append("INSERT INTO timeworks_payment (");
                sb.append("timeworks_id");
                sb.append(", hourly_wage");
                // sb.append(", weekend_hourly_wage");
                sb.append(", trans_cost");
                // sb.append(", print_state");
                sb.append(", pay_state");
                sb.append(", state");
                sb.append(") VALUES (");
                sb.append("@NEW_ID");
                sb.append(", @HOURLY_WAGE");
                // sb.append(", @WEEKEND_HOURLY_WAGE");
                sb.append(", @TRANS_COST");
                // sb.append(", " + Enums.situation.YET.getNum());
                sb.append(", " + Enums.situation.YET.getNum());
                sb.append(", " + Enums.state.UNDECIDED.getNum());
                sb.append(");");
            } else if (query.getCategory_id() == Enums.employeeCategory.FULLTIME.getNum()) {
                sb.append("DECLARE @NEW_ID int; SET @NEW_ID = @@IDENTITY;");

                sb.append("DECLARE @TRANS_COST int = 0;");
                sb.append("SELECT @TRANS_COST = trans_cost FROM fulltimes WHERE state = " + Enums.state.INITIAL.getNum());
                sb.append(" AND employee_id = " + query.getEmployee_id() + ";");

                sb.append("INSERT INTO timeworks_fulltime (");
                sb.append("timeworks_id");
                sb.append(", trans_cost");
                sb.append(", state");
                sb.append(") VALUES (");
                sb.append("@NEW_ID");
                sb.append(", @TRANS_COST");
                sb.append(", " + Enums.state.UNDECIDED.getNum());
                sb.append(");");
            }
        }

        return saveEntity(sb.toString());
    }

    /***
     * 社員の勤怠情報確定
     * @param query
     * @return
     */
    public boolean paymentFulltime(TimeworksFullTimeEneity query) {
        StringBuilder sb = new StringBuilder();
        sb.append(query.getUpdateString());
        sb.append("UPDATE timeworks SET");
        sb.append(" comp_start_time = '" + query.getComp_start_time() + "'");
        sb.append(", comp_end_time = '" + query.getComp_end_time() + "'");
        sb.append(" WHERE timeworks_id = " + query.getTimeworks_id() + " AND state = " + Enums.state.INITIAL.getNum() + ";");

        return saveEntity(sb.toString());
    }

    /***
     * アルバイトの支払い
     * @param query
     * @return
     */
    public boolean paymentParttime(TimeworksPaymentEntity query) {
        StringBuilder sb = new StringBuilder();
        sb.append(query.getUpdateString());
        sb.append("UPDATE timeworks SET");
        sb.append(" comp_start_time = '" + query.getComp_start_time() + "'");
        sb.append(", comp_end_time = '" + query.getComp_end_time() + "'");
        sb.append(" WHERE timeworks_id = " + query.getTimeworks_id() + " AND state = " + Enums.state.INITIAL.getNum() + ";");

        return saveEntity(sb.toString());
    }

    /**
     * プリント用リスト取得
     * @param sqlData
     * @return
     */
    public List<IEntity> getSelectPrintItems(SqlData sqlData) {
        return getList(sqlData);
    }

    // /**
    //  * 支払い印刷更新
    //  * @param sqlData
    //  * @return
    //  */
    // public boolean updatePrintState(SqlData sqlData) {
    //     StringBuilder sb = new StringBuilder();
    //     sb.append("UPDATE tp SET tp.print_state = " + Enums.situation.DONE.getNum());
    //     sb.append(" FROM timeworks_payment tp WHERE " + sqlData.getSqlString());
    //     return saveEntity(sb.toString());
    // }

    /**
     * 支払い状況更新
     * @param sqlData
     * @return
     */
    public boolean updatePayState(SqlData sqlData) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE tp SET tp.pay_state = " + Enums.situation.DONE.getNum());
        sb.append(" FROM timeworks_payment tp WHERE " + sqlData.getSqlString());
        return saveEntity(sb.toString());
    }

    /**
     * 削除
     * @param sqlData
     * @return
     */
    public boolean deleteTimrworks(String str) {
        return saveEntity(str);
    }
}
