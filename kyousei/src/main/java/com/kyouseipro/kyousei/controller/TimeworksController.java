package com.kyouseipro.kyousei.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyouseipro.kyousei.data.SimpleData;
import com.kyouseipro.kyousei.data.SqlData;
import com.kyouseipro.kyousei.entity.employee.EmployeeEntity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksFormEntity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksFullTimeEneity;
import com.kyouseipro.kyousei.entity.timeworks.TimeworksPaymentEntity;
import com.kyouseipro.kyousei.interfacies.IEntity;
import com.kyouseipro.kyousei.repository.EmployeeRepository;
import com.kyouseipro.kyousei.repository.TimeworksRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TimeworksController {
    
    private final TimeworksRepository timeworksRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * 今日の全勤怠データリストを取得
     * @return
     */
    @GetMapping("/timeworks/get/today")
    @ResponseBody
    public List<IEntity> getTodaysAttendanceDataForAllEmployees() {
        return timeworksRepository.getAttendanceDataForEveryoneOnTheDay();
    }

    /**
     * IDから今日の個人勤怠データを取得
     * @return IEntity
     */
    @PostMapping("/timeworks/get/today/id")
    @ResponseBody
    public IEntity getTodaysEmployeeAttendanceDataFromId(@RequestParam int id) {
        return timeworksRepository.getEmployeeAttendanceData(id);
    }

    /**
     * IDと日付から個人勤怠データを取得
     * @return IEntity
     */
    @PostMapping("/timeworks/check/employee")
    @ResponseBody
    public boolean getEmployeeAttendanceDataFromId(@RequestBody SimpleData data) {
        return timeworksRepository.checkEmployeeAttendanceData(data.getNumber(), data.getText());
    }

    /**
     * コードから今日の個人勤怠データを取得
     * @return IEntity
     */
    @PostMapping("/timeworks/get/today/code")
    @ResponseBody
    public IEntity getTodaysEmployeeAttendanceDataFromCode(@RequestParam int code) {
        // SimpleData data = employeeRepository.getEmployeeIdAndNameWithSimpleDataForCode(code);
        EmployeeEntity data = (EmployeeEntity)employeeRepository.getEmployeeForCode(code);
        return timeworksRepository.getEmployeeAttendanceData(data.getEmployee_id());
    }

    /**
     * 今日の個人勤怠データを登録
     * @param query
     * @return 時間を登録できれば[True]を、できなければ[False]を返す
     */
    @PostMapping("/timeworks/regist/today")
    @ResponseBody
    public boolean setTimeWorks(@RequestBody TimeworksFormEntity query) {
        return timeworksRepository.registTodaysEmployeeAttendanceData(query, false);
    }

    /**
     * 個人勤怠データを作成
     * @param query
     * @return 時間を登録できれば[True]を、できなければ[False]を返す
     */
    @PostMapping("/timeworks/create/today")
    @ResponseBody
    public boolean setNewTimeWorks(@RequestBody TimeworksFormEntity query) {
        return timeworksRepository.registTodaysEmployeeAttendanceData(query, true);
    }

    /**
     * 社員の勤怠情報確定
     * @param query
     * @return
     */
    @PostMapping("/timeworks/payment/fulltime")
    @ResponseBody
    public boolean dailyPayment(@RequestBody TimeworksFullTimeEneity query) {
        return timeworksRepository.paymentFulltime(query);
    }

    /**
     * 日雇いの支払い情報確定
     * @param query
     * @return
     */
    @PostMapping("/timeworks/payment/parttime")
    @ResponseBody
    public boolean dailyPayment(@RequestBody TimeworksPaymentEntity query) {
        return timeworksRepository.paymentParttime(query);
    }

    /**
     * 選択した印刷用のアイテムを取得
     * @param query
     * @return
     */
    @PostMapping("/timeworks/payment/list")
    @ResponseBody
    public List<IEntity> getDailyPrintItems(@RequestBody SqlData query) {
        return timeworksRepository.getSelectPrintItems(query);
    }

    // /**
    //  * 日雇いの支払い印刷確定
    //  * @param query
    //  * @return
    //  */
    // @PostMapping("/timeworks/payment/print")
    // @ResponseBody
    // public boolean updateDayilyPrintState(@RequestBody SqlData query) {
    //     return timeworksRepository.updatePrintState(query);
    // }

    /**
     * 日雇いの支払い清算確定
     * @param query
     * @return
     */
    @PostMapping("/timeworks/payment/update")
    @ResponseBody
    public boolean updateDayilyPayState(@RequestBody SqlData query) {
        return timeworksRepository.updatePayState(query);
    }

    /**
     * 勤怠情報の削除
     * @param query
     * @return
     */
    @PostMapping("/timeworks/delete")
    @ResponseBody
    public boolean deleteTimeworks(@RequestBody SqlData query) {
        return timeworksRepository.deleteTimrworks(query.getSqlString());
    }
}
