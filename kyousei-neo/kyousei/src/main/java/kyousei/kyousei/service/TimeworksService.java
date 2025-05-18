package kyousei.kyousei.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import kyousei.kyousei.common.Enums;
import kyousei.kyousei.entity.data.SqlData;
import kyousei.kyousei.entity.employee.EmployeeEntity;
import kyousei.kyousei.entity.timeworks.TimeworksEntity;
import kyousei.kyousei.entity.timeworks.TimeworksListEntity;
import kyousei.kyousei.interfaceis.IEntity;

@Service
// @RequiredArgsConstructor
public class TimeworksService {
    // private final DownloadService downloadService;
    // private final SqlRepository sqlRepository;

    /**
     * 全従業員の今日の勤怠データリスト取得
     * @return List<IEntity>
     */
    public static List<IEntity> getAttendanceDataForEveryoneOnTheDay() {
       StringBuilder sb = new StringBuilder();
        TimeworksListEntity entity = new TimeworksListEntity();
        sb.append(entity.getSelectString());
        LocalDate date = LocalDate.now();
        sb.append(" AND t.work_date = '" + date + "' AND c.category_id = " + Enums.companyCategory.OWN.getNum());
        sb.append(" ORDER BY o.office_id");

        SqlData sqlData = DownloadService.createSqlData(entity, sb.toString(), null);
        return SqlService.getEntityList(sqlData);
    }
    /**
     * 今日の個人勤怠データ取得
     * @param employee_id
     * @return IEntity
     */
    public static IEntity getEmployeeAttendanceData(EmployeeEntity emp) {
        StringBuilder sb = new StringBuilder();
        TimeworksEntity entity = new TimeworksEntity();
        LocalDate date = LocalDate.now();
        sb.append(entity.getSelectString());
        sb.append(" AND t.work_date = '" + date + "' AND t.employee_id = " + emp.getEmployee_id());
        SqlData sqlData = new SqlData();
        sqlData = DownloadService.createSqlData(new TimeworksEntity(), sb.toString(), null);
        entity = (TimeworksEntity)SqlService.getEntity(sqlData);
        if (entity.getTimeworks_id() == 0) {
            entity.setWork_date(date);
            entity.setEmployee_id(emp.getEmployee_id());
            entity.setCategory_id(emp.getCategory_id());
            entity.setFull_name(emp.getFull_name());
        }
        return entity;
    }
}
