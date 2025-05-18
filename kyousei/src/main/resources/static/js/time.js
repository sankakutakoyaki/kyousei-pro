
/******************************************************************************************************* 日時取得 */
/**
 * 年月日等の各要素を取得
 * @returns yyyyMMdd h:m
 */
function getNow() {
    var date = new Date();
    const y = date.getFullYear();
    const m = date.getMonth() + 1;
    const d = date.getDate();
    const h = date.getHours();
    const min = date.getMinutes();
    return y + '/' + m + '/' + d + ' ' + h + ':' + min;
}

/**
 * 年月日等の各要素を取得
 * @returns yyyyMMddhhmm000000
 */
function getNowNoBreak() {
    var date = new Date();
    const y = date.getFullYear();
    const m = date.getMonth() + 1;
    const d = date.getDate();
    const h = date.getHours();
    const min = date.getMinutes();
    const milli = date.getMilliseconds();
    return y + ('0' + m).slice(-2) + ('0' + d).slice(-2) + ('0' + h).slice(-2) + ('0' + min).slice(-2) + ('00' + milli).slice(-3);
}

/**
 * 曜日を取得
 * @param {*} day 取得する日付
 */
function getDayOfWeek(day) {
    const days = [
        "日曜日",
        "月曜日",
        "火曜日",
        "水曜日",
        "木曜日",
        "金曜日",
        "土曜日",
    ];
    return days[day.getDay()];
}

/**
 * 曜日を一文字で取得
 * @param {*} day 取得する日付
 */
function getDayOfWeekAsSingle(d) {
    const day = new Date(d);
    const days = [
        "日",
        "月",
        "火",
        "水",
        "木",
        "金",
        "土",
    ];
    return days[day.getDay()];
}

/**
 * 期間指定ボックスの開始時間チェック
 * @param {*} e 
 * @param {*} name 比較対象のボックスの[name]
 * @returns 
 */
function checkStartDateChanged(e, name) {
    if (e.target.value == "") return;

    const enddate = document.querySelector('[name="' + name + '"]');

    const start = new Date(e.target.value);
    const end = new Date(enddate.value);
    // 開始が終了よりも大きければ終了を開始に合わせる
    if (start > end) enddate.value = e.target.value;
}

/**
 * 期間指定ボックスの終了時間チェック
 * @param {*} e 
 * @param {*} name 比較対象のボックスの[name]
 * @returns 
 */
function checkEndDateChanged(e, name) {
    if (e.target.value == "") return;

    const startdate = document.querySelector('[name="' + name + '"]');

    const start = new Date(startdate.value);
    const end = new Date(e.target.value);
    // 開始が終了よりも大きければ開始を終了に合わせる
    if (start > end) startdate.value = e.target.value;
}

/**
 * 期間指定ボックスを変更する
 * @param {*} str 分岐用文字列
 * @returns 開始日と終了日を変更する
 */
function execSpecifyPeriod(str) {
    const date = new Date();

    const startdate = document.querySelector('[name="startdate"]');
    if (startdate == null) return;
    const start = new Date(startdate.value);

    const enddate = document.querySelector('[name="enddate"]');
    if (enddate == null) return;
    const end = new Date(enddate.value);

    switch (str) {
        case "last-month":
            startdate.value = new Date(date.getFullYear(), date.getMonth() - 1, 1).toLocaleDateString('sv-SE');
            enddate.value = new Date(date.getFullYear(), date.getMonth(), 0).toLocaleDateString('sv-SE');
            break;
        case "this-month":
            startdate.value = new Date(date.getFullYear(), date.getMonth(), 1).toLocaleDateString('sv-SE');
            enddate.value = new Date(date.getFullYear(), date.getMonth() + 1, 0).toLocaleDateString('sv-SE');
            break;
        case "prev-day":
            startdate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() - 1).toLocaleDateString('sv-SE');
            enddate.value = new Date(end.getFullYear(), end.getMonth(), end.getDate() - 1).toLocaleDateString('sv-SE');
            break;
        case "next-day":
            startdate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() + 1).toLocaleDateString('sv-SE');
            enddate.value = new Date(end.getFullYear(), end.getMonth(), end.getDate() + 1).toLocaleDateString('sv-SE');
            break;
        case "yesterday":
            startdate.value = new Date(date.getFullYear(), date.getMonth(), date.getDate() - 1).toLocaleDateString('sv-SE');
            enddate.value = new Date(date.getFullYear(), date.getMonth(), date.getDate() - 1).toLocaleDateString('sv-SE');
            break;
        case "prev-week":
            startdate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() - 7).toLocaleDateString('sv-SE');
            enddate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() - 1).toLocaleDateString('sv-SE');
            break;
        case "next-week":
            startdate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() + 7).toLocaleDateString('sv-SE');
            enddate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() + 13).toLocaleDateString('sv-SE');
            break;
        case "this-week":
            startdate.value = new Date(date.getFullYear(), date.getMonth(), date.getDate()).toLocaleDateString('sv-SE');
            enddate.value = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 6).toLocaleDateString('sv-SE');
            break;
        case "today":
            startdate.value = date.toLocaleDateString('sv-SE');
            enddate.value = date.toLocaleDateString('sv-SE');
            break;
        case "prev-month":
            startdate.value = new Date(start.getFullYear(), start.getMonth() - 1, 1).toLocaleDateString('sv-SE');
            enddate.value = new Date(start.getFullYear(), start.getMonth(), 0).toLocaleDateString('sv-SE');
            break;
        case "next-month":
            startdate.value = new Date(end.getFullYear(), end.getMonth() + 1, 1).toLocaleDateString('sv-SE');
            enddate.value = new Date(end.getFullYear(), end.getMonth() + 2, 0).toLocaleDateString('sv-SE');
            break;
        default:
            break;
    }
}


/**
 * 開始時刻と終了時刻の差を求める
 * @param {*} bt 開始時刻(00:00)
 * @param {*} at 終了時刻(00:00)
 * @returns 差(00:00)
 */
function resultSubtractingTime(bt, at) {
    let dt = [bt, at].map(v => {
        v = v.split(":");
        return v[0] * 60 + +v[1];
    }).reduce((p, c) => c - p);
    return [dt / 60 | 0, dt % 60].map(v => {
        return (v + "").padStart(2, "0");
    }).join(":");
}

/**
 * 時刻を数値に直す
 * @param {*} timeString 時刻文字列(00:00)
 * @returns 1/60の数値
 */
function parseTimeToNum(timeString) {
    if (timeString == null || timeString == "") return 0;
    const timeParts = timeString.split(":");
    const hours = parseInt(timeParts[0]);
    const minutes = parseInt(timeParts[1]);
    return (hours * 60 + minutes) / 60;
}

/**
 * 数値を時刻に直す
 * @param {*} timeNum 時刻数値
 * @returns (00:00))
 */
function parseTimeToStr(timeNum) {
    if (timeNum == null || timeNum == "") return "0:00";
    const timeString = timeNum.toFixed(2);
    const timeParts = timeString.split(".");
    const hours = parseInt(timeParts[0]).toString();
    const minutes = (parseInt(timeParts[1]) * 60 / 100).toFixed(0).toString();
    return hours + ":" + minutes.substring(0, 2).padStart(2, "0");
}