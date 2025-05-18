/*******************************************************************************************************  */

function getSelectedItems() {
    const parent = document.querySelector(".tablelist-container");
    if (parent == null) return;
    const ids = getAllSelectedCheckboxes(parent);
    updatelist = [];
    if (ids.length > 0) {
        ids.forEach(function (item) {
            let elm = list.find(function (value) { return value.recycle_id == item.id })
            if (elm != null) updatelist.push(elm);
        });
    }
    return updatelist;
}

function getUpdateRecycleCompanyId(updatelist) {
    const companyId = updatelist.filter((element, index, self) => self.findIndex(
        (dataElement) => dataElement.recycle_company_id == element.recycle_company_id) == index
    );
    return companyId.length == 1 ? companyId[0].recycle_company_id : -1;
}

function getUpdateRecycleOfficeId(updatelist, companyId) {
    if (companyId == -1) {
        return -1;
    } else {
        const officeId = updatelist.filter((element, index, self) => self.findIndex(
            (dataElement) => dataElement.recycle_office_id == element.recycle_office_id) == index
        );
        return officeId.length == 1 ? officeId[0].recycle_office_id : -1;
    }
}

function getUpdateRecycleUsedate(updatelist) {
    const usedate = updatelist.filter((element, index, self) => self.findIndex(
        (dataElement) => dataElement.recycle_usedate == element.recycle_usedate) == index
    );
    return usedate.length == 1 ? usedate[0].recycle_usedate : "";
}

function getUpdateRecycleClassCode(updatelist) {
    const classCode = updatelist.filter((element, index, self) => self.findIndex(
        (dataElement) => dataElement.recycle_class_code == element.recycle_class_code) == index
    );
    return classCode.length == 1 ? classCode[0].recycle_class_code : -1;    
}

function getUpdateRecycleClassName(updatelist) {
    const className = updatelist.filter((element, index, self) => self.findIndex(
        (dataElement) => dataElement.recycle_class_name == element.recycle_class_name) == index
    );
    return className.length == 1 ? className[0].recycle_class_name : "";    
}

function getUpdateRecycleMakerCode(updatelist) {
    const makerCode = updatelist.filter((element, index, self) => self.findIndex(
        (dataElement) => dataElement.recycle_maker_code == element.recycle_maker_code) == index
    );
    return makerCode.length == 1 ? makerCode[0].recycle_maker_code : -1;    
}

function getUpdateRecycleMakerName(updatelist) {
    const makerName = updatelist.filter((element, index, self) => self.findIndex(
        (dataElement) => dataElement.recycle_maker_name == element.recycle_maker_name) == index
    );
    return makerName.length == 1 ? makerName[0].recycle_maker_name : "";    
}

function getUpdateRecyclePrice(updatelist) {
    const price = updatelist.filter((element, index, self) => self.findIndex(
        (dataElement) => dataElement.recycle_price == element.recycle_price) == index
    );
    return price.length == 1 ? price[0].recycle_price : -1;    
}

function getUpdateRecycleExTax(updatelist) {
    const exTax = updatelist.filter((element, index, self) => self.findIndex(
        (dataElement) => dataElement.recycle_ex_tax == element.recycle_ex_tax) == index
    );
    return exTax.length == 1 ? exTax[0].recycle_ex_tax : -1;    
}

async function createRecycleCompanyComboBox(updateitem, shipperlist) {
    createFormDialog(updateitem, "修正", "修正");
    const area = document.getElementById('recycle-form');
    const selectArea = area.querySelector('[name="recycle-company"]');
    if (selectArea == null) return;
    selectArea.insertAdjacentHTML('beforeend', '<option value="0"></option>');
    createRecyleComboBox(selectArea, shipperlist);

    const company = area.querySelector('[name="recycle-company"]');
    if (company == null) return;
    if (updateitem.recycle_company_id > 0) company.value = updateitem.recycle_company_id;

    const office = area.querySelector('[name="recycle-office"]');
    if (office == null) return;
    await createShipperOffice(office);
    office.value = updateitem.recycle_office_id;
}

function setUpdatelistOfShipperName(updatelist, company, office) {
    updatelist.forEach(value => {
        let shipperName = "";
        if (company.selectedIndex > 0) {
            shipperName = company.options[company.selectedIndex].text;
            if (office.selectedIndex > 0) {
                shipperName += " " + office.options[office.selectedIndex].text;
            }
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_shipper_name = shipperName;
        }
    });    
}

function setUpdatelistOfCompanyId(updatelist, company_id) {
    updatelist.forEach(value => {
        if (Number(company_id) != updateitem.recycle_company_id) {
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_company_id = Number(company_id);
        }
    });    
}

function setUpdatelistOfOfficeId(updatelist, office_id) {
    updatelist.forEach(value => {
        if (Number(company_id) == updateitem.recycle_company_id && Number(office_id) != updateitem.recycle_office_id) {
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_office_id = Number(office_id);
        } else if (Number(company_id) != updateitem.recycle_company_id) {
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_office_id = Number(office_id);
        }
    });    
}

function setUpdatelistOfMakerCode(updatelist, maker_code) {
    updatelist.forEach(value => {
        if (maker_code != "" && Number(maker_code) != updateitem.recycle_maker_code) {
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_maker_code = Number(maker_code);
        }
    });    
}

function setUpdatelistOfMakerName(updatelist, maker_name) {
    updatelist.forEach(value => {
        if (maker_name != "" && maker_name != updateitem.recycle_maker_name) {
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_maker_name = maker_name;
        }
    });    
}

function setUpdatelistOfClassCode(updatelist, class_code) {
    updatelist.forEach(value => {
        if (class_code != "" && Number(class_code) != updateitem.recycle_class_code) {
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_class_code = Number(class_code);
        }
    });
}

function setUpdatelistOfClassName(updatelist, class_name) {
    updatelist.forEach(value => {
        // console.log(class_name + " : " + updateitem.recycle_class_name)
        if (class_name != "" && class_name != updateitem.recycle_class_name) {
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_class_name = class_name;
        }
    });    
}

function setUpdatelistOfUsedate(updatelist, usedate) {
    updatelist.forEach(value => {
        if (usedate != "" && usedate != updateitem.recycle_usedate) {
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_usedate = usedate;
        }
    });    
}

function setUpdatelistOfPrice(updatelist, price) {
    updatelist.forEach(value => {
        if (price != "" && Number(price) != updateitem.recycle_price) {
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_price = Number(price);
        }
    });    
}

function setUpdatelistOfExTax(updatelist, ex_tax) {
    updatelist.forEach(value => {
        if (ex_tax != "" && Number(ex_tax) != updateitem.recycle_ex_tax) {
            let elm = list.find(item => { return value.recycle_id == item.recycle_id; });
            if (elm != null) elm.recycle_ex_tax = Number(ex_tax);
        }
    });    
}

async function saveRecycleRegist(url, item) {
    startProcessing();

    if (item != null) {
        const data = JSON.stringify(item);
        const contentType = 'application/json';
        const resultResponse = await postFetch(url, data, token, contentType);
        const result = await resultResponse.json();

        // if (result) {
        //     // execResetRecycleRegist();
        //     saveSuccessDialog();
        // } else {
        //     failureDialog();
        // }
        if (result == false) {
            failureDialog();
        }
    }

    processingEnd();
}
// async function saveRecycleRegist(url) {
//     startProcessing();

//     const check = list.find(value => { return value.state != 0 });
//     if (check != null) {
//         const data = JSON.stringify(list);
//         const contentType = 'application/json';
//         const resultResponse = await postFetch(url, data, token, contentType);
//         const result = await resultResponse.json();

//         if (result) {
//             execResetRecycleRegist();
//             saveSuccessDialog();
//         } else {
//             failureDialog();
//         }
//     }

//     processingEnd();
// }

// 保存時の処理
async function execSaveRegistListAfterLoadingList(postUrl, getUrl) {
    startProcessing();

    const check = list.find(value => { return value.state != 0 });
    if (check != null) {
        const data = JSON.stringify(list);
        const contentType = 'application/json';
        const resultResponse = await postFetch(postUrl, data, token, contentType);
        const result = await resultResponse.json();

        if (result) {
            const items = await fetch(getUrl);
            if (items != null) {
                origin = await items.json();
                list = structuredClone(origin);
                saveSuccessDialog();
            }
        } else {
            failureDialog();
        }
    }

    processingEnd();
}

async function getRecycleEntity() {
    startProcessing();
    const number = document.querySelector('[name="recycle-number"]');
    if (number == null) return;
    const url = "/recycle/get";
    const data = "number=" + encodeURIComponent(number.value);
    const contentType = 'application/x-www-form-urlencoded';
    const resultResponse = await postFetch(url, data, token, contentType);
    processingEnd();
    return await resultResponse.json();
}

async function checkRecycleNumberRegist() {
    startProcessing();
    const number = document.querySelector('[name="recycle-number"]');
    if (number == null) return;
    // const url = "/recycle/number/check";
    const url = "/recycle/get";
    const data = "number=" + encodeURIComponent(number.value);
    const contentType = 'application/x-www-form-urlencoded';
    const resultResponse = await postFetch(url, data, token, contentType);
    processingEnd();
    return await resultResponse.json();
}

async function checkRecycleNumberLossRegist() {
    startProcessing();
    const number = document.querySelector('[name="recycle-number"]');
    if (number == null) return;
    const url = "/recycle/number/check/loss";
    const data = "number=" + encodeURIComponent(number.value);
    const contentType = 'application/x-www-form-urlencoded';
    const resultResponse = await postFetch(url, data, token, contentType);
    processingEnd();
    return await resultResponse.json();
}

// function checkRecycleNumberInput(e) {
//     if (e.target.value.length != 13 || isNaN(e.target.value) == true) {
//         e.target.value = "";
//         return false;
//     }
//     e.target.value = e.target.value.slice(0, 4) + "-" + e.target.value.slice(4, 12) + "-" + e.target.value.slice(12, e.target.value.length);
//     return true;
// }

async function createShipperOffice(elm) {
    const area = elm.closest('[name="recycle-input-area"]');
    if (area == null) return;
    const company = area.querySelector('[name="recycle-company"]');
    if (company == null) return;
    const office = area.querySelector('[name="recycle-office"]');
    if (office == null) return;

    const url = "/recycle/office/get";
    const data = "id=" + encodeURIComponent(company.value);
    const contentType = 'application/x-www-form-urlencoded';
    const resultResponse = await postFetch(url, data, token, contentType);
    const result = await resultResponse.json();

    if (result != null) {
        deleteElements(office);
        office.insertAdjacentHTML('beforeend', '<option value="0"></option>');
        createRecyleComboBox(office, result);
    }
    return result;
}


function createRecyleComboBox(selectArea, items) {
    if (selectArea == null) return;
    items.forEach(function (item) {
        if (item.number > 0) {
            selectArea.insertAdjacentHTML('beforeend', '<option value="' + item.number + '">' + item.text + '</option>');
        }
    })
}

function deleteRecycleRegist() {
    const parent = document.querySelector(".tablelist-container");
    if (parent == null) return;
    const ids = getAllSelectedCheckboxes(parent);
    if (ids.length > 0) {
        ids.forEach(function (item) {
            let elm = list.find(function(value) { return value.recycle_id == item.id})
            if (elm != null) elm.state = deleteStateCode;
        });
        deleteElementsWithSpecifiedId("tablelist");
        createRecyleTableListHeader();
        resetInputBox();
        createRecycleRegistList();
        const number = document.querySelector('[name="recycle-number"]');
        number.focus();
    }
}

function createRecycleRegistList() {
    list.forEach(item => {
        if (item.state != deleteStateCode) {
            createRecyleTableListContent(item);
        }                        
    });
}

function execResetRecycleRegist() {
    // list = [];
    const parent = document.getElementById('tablelist');
    deleteAllElementsByName(parent, 'data-row');
    updateRecycleDisplay();
    // deleteElementsWithSpecifiedId("tablelist");
    // createRecyleTableListHeader();
    resetInputBox();
    const number = document.querySelector('[name="recycle-number"]');
    number.focus();
}

function renewRecycleRegistDisplay() {
    deleteElementsWithSpecifiedId("tablelist");
    createRecyleTableListHeader();
    resetInputBox();
    createRecycleRegistList();
    const number = document.querySelector('[name="recycle-number"]');
    number.focus();
}

function execRecycleNumberInputBlur(e) {
    if (e.target.value == "") return;
    checkRecycleNumberInput(e);
}

function checkRecycleNumberInput(e) {
    checkRecycleNumber(e.target);
    // const number = e.target.value.replaceAll("-", "").replaceAll("a", "").replaceAll("A", "");

    // if (number.length != 13 || isNaN(number) == true) {
    //     e.target.value = "";
    //     return false;
    // }
    // e.target.value = number.slice(0, 4) + "-" + number.slice(4, 12) + "-" + number.slice(12, number.length);
    // return true;
}

function checkRecycleNumber(num) {
    const number = num.value.replaceAll("-", "").replaceAll("a", "").replaceAll("A", "");
    if (number.length != 13 || isNaN(number) == true) {
        num.value = "";
        return false;
    }
    num.value = number.slice(0, 4) + "-" + number.slice(4, 12) + "-" + number.slice(12, number.length);
    return true;
}

/******************************************************************************************************* 一覧画面 */

function execResetRecycleList() {
    const dateClass = document.querySelector('[name="recycle-date-class"]');
    if (dateClass != null) dateClass.selectedIndex = 0;
    const date = new Date().toLocaleDateString('sv-SE');
    const startdate = document.querySelector('[name="recycle-startdate"]');
    if (startdate != null) startdate.value = date;
    const enddate = document.querySelector('[name="recycle-enddate"]');
    if (enddate != null) enddate.value = date;
    const number = document.querySelector('[name="recycle-number"]');
    if (number != null) number.value = "";
    const company = document.querySelector('[name="recycle-company"]');
    if (company != null) company.selectedIndex = -1;
    const office = document.querySelector('[name="recycle-office"]');
    if (office != null) office.selectedIndex = -1;

    execSearchRecycleList();
}

async function execSearchRecycleNumber() {
    const number = document.querySelector('[name="recycle-number"]');
    if (number.value == "") {
        number.focus();
        return;
    }
    const txt = " AND r.recycle_number = '" + number.value + "'";

    startProcessing();

    const data = "str=" + encodeURIComponent(txt);  // URIエンコード
    const url = '/recycle/list/search';
    const contentType = 'application/x-www-form-urlencoded';
    const resultResponse = await postFetch(url, data, token, contentType);
    list = await resultResponse.json();

    const parent = document.getElementById('tablelist');
    deleteAllElementsByName(parent, 'data-row');
    updateRecycleDisplay();

    processingEnd();
}

async function execSearchRecycleList() {
    displayLockon();

    startProcessing();

    let txt = "";
    const dateclass = document.querySelector('[name="recycle-date-class"]');
    if (dateclass == null) return;
    const startdate = document.querySelector('[name="recycle-startdate"]');
    if (startdate == null) return;
    const enddate = document.querySelector('[name="recycle-enddate"]');
    if (enddate == null) return;
    const company = document.querySelector('[name="recycle-company"]');
    if (company == null) return;
    const office = document.querySelector('[name="recycle-office"]');
    if (office == null) return;

    if (dateclass.value > 0) {
        switch (Number(dateclass.value)) {
            case inputStateCode:
                txt += " AND CAST(r.update_date AS DATE)";
                break;
            case useStateCode:
                txt += " AND r.recycle_usedate";
                break;
            case deliveryStateCode:
                txt += " AND r.recycle_deliverydate";
                break;
            case forwardStateCode:
                txt += " AND r.recycle_shippingdate";
                break;
            default:
                break;
        }

        if (startdate.value != "" && enddate.value != "") {
            txt += " BETWEEN '" + startdate.value + "' AND '" + enddate.value + "'";
        } else if (startdate.value != "" && enddate.value == "") {
            txt += " >= '" + startdate.value + "'";
        } else if (startdate.value == "" && enddate.value != "") {
            txt += " <= '" + enddate.value + "'";
        }
    // } else {
    //     if (startdate.value != "" && enddate.value != "") {
    //         txt += " AND (r.recycle_usedate BETWEEN '" + startdate.value + "' AND '" + enddate.value + "'";
    //         txt += " OR r.recycle_deliverydate BETWEEN '" + startdate.value + "' AND '" + enddate.value + "'";
    //         txt += " OR r.recycle_shippingdate BETWEEN '" + startdate.value + "' AND '" + enddate.value + "')";
    //     } else if (startdate.value != "" && enddate.value == "") {
    //         txt += " AND ((r.recycle_usedate >= '" + startdate.value + "' AND r.recycle_usedate < '9999-12-31')";
    //         txt += " OR (r.recycle_deliverydate >= '" + startdate.value + "' AND r.recycle_deliverydate < '9999-12-31')";
    //         txt += " OR (r.recycle_shippingdate >= '" + startdate.value + "' AND r.recycle_shippingdate < '9999-12-31'))";
    //     } else if (startdate.value == "" && enddate.value != "") {
    //         txt += " AND (r.recycle_usedate <= '" + enddate.value + "'";
    //         txt += " OR r.recycle_deliverydate <= '" + enddate.value + "'";
    //         txt += " OR (r.recycle_shippingdate <= '" + enddate.value + "')";
    //     }
    }

    if (company.value > 0) txt += " AND r.recycle_company_id = " + company.value;
    if (office.value > 0) txt += " AND r.recycle_office_id = " + office.value;

    const data = "str=" + encodeURIComponent(txt);  // URIエンコード
    const url = '/recycle/list/search';
    const contentType = 'application/x-www-form-urlencoded';
    const resultResponse = await postFetch(url, data, token, contentType);
    list = await resultResponse.json();

    const parent = document.getElementById('tablelist');
    deleteAllElementsByName(parent, 'data-row');
    updateRecycleDisplay();

    hamburgerClose();
    processingEnd();

    displayLockoff();
}

// CSVファイルをダウンロードする
async function execDownloadRecycleList() {
    // ダウンロードボタンの親要素（tablelist-container）を取得する
    const parent = document.querySelector('.tablelist-container');
    // ダウンロード用のSQL文を取得する（選択されていなければ[NULL]が返る）
    const downloadStr = createSqlStringFromSearchCriteriaByIds(parent);
    if (downloadStr == null) {
        notSelectedDialog();
        return false;
    }
    const downloadsqldata = { "sqlString": sqlformdata.sqlString + downloadStr, "classPath": sqlformdata.classPath };

    const url = '/tablelist/download/csv';
    const data = JSON.stringify(downloadsqldata);
    const contentType = 'application/json';
    const result = await postFetch(url, data, token, contentType);
    const text = await result.text();

    const bom = new Uint8Array([0xef, 0xbb, 0xbf]);
    const blob = new Blob([bom, text], { type: "text/csv" });
    const objectUrl = URL.createObjectURL(blob);
    const downloadLink = document.createElement("a");
    downloadLink.download = getNowNoBreak() + ".csv";
    downloadLink.href = objectUrl;
    downloadLink.click();
    downloadLink.remove();

    // 選択を全て解除する
    deselectAllCheckBtn('all-chk-btn');
}

// 選択されたIDで検索条件を作成
function createSqlStringFromSearchCriteriaByIds() {
    const selectedlist = getSelectedItems();
    let str = " AND r.recycle_id IN (";
    if (selectedlist.length > 0) {
        selectedlist.forEach(function (item) {
            str += item.recycle_id + ", ";
        });
        str = str.slice(0, str.length - 2);
        str += ")";
        return str;
    } else {
        return null;
    }
}

// バーコード読み取り
function execCamera(self) {
    const number = document.querySelector("[name='recycle-number']");
    if (number != null) number.value = "";
    // const close = document.querySelector('.camera-area');
    // if (close != null) close.classList.add('open');
    createCameraDialog();
    Quagga.init({
        inputStream: {
            name: 'Live',
            type: 'LiveStream',
            target: document.querySelector('#interactive'),//埋め込んだdivのID
        },
        decoder: {
            // readers: ['codabar_reader', 'ean_reader', 'code_128_reader']//decoderの種類
            // readers: ['code_128_reader']
            // readers: ['ean_reader']
            // readers: ['ean_8_reader']
            // readers: ['code_39_reader']
            // readers: ['code_39_vin_reader']
            readers: ['codabar_reader']
            // readers: ['upc_reader']
            // readers: ['upc_e_reader']
            // readers: ['i2of5_reader']
            // readers: ['2of5_reader']
            // readers: ['code_93_reader']
        }
    }, (err) => {
        if(err) {
            console.log(err);
            createMessageDialog("カメラがありません", "エラー", "red");
            return
        }
        console.log("Initialization finished. Ready to start");
        Quagga.start();
    })

    // Quagga.onProcessed(result => {
    //     const drawingCtx = Quagga.canvas.ctx.overlay;
    //     const drawingCanvas = Quagga.canvas.dom.overlay;

    //     if (result) {
    //     // 検出中の緑の線の枠
    //         if (result.boxes) {
    //             drawingCtx.clearRect(0, 0, parseInt(drawingCanvas.getAttribute("width")), parseInt(drawingCanvas.getAttribute("height")));
    //             result.boxes.filter(function (box) {
    //                 return box !== result.box;
    //             }).forEach(function (box) {
    //                 Quagga.ImageDebug.drawPath(box, {x: 0, y: 1}, drawingCtx, {color: "green", lineWidth: 2});
    //             });
    //         }
    //         // 読込中の青枠
    //         if (result.box) {
    //             Quagga.ImageDebug.drawPath(result.box, {x: 0, y: 1}, drawingCtx, {color: "#00F", lineWidth: 2});
    //         }
    //         // 検出完了時の赤線
    //         if (result.codeResult && result.codeResult.code) {
    //             Quagga.ImageDebug.drawPath(result.line, {x: 'x', y: 'y'}, drawingCtx, {color: 'red', lineWidth: 3});
    //         }
    //     }
    // })

    Quagga.onDetected(success => {
        const code = success.codeResult.code;
        if(code) {
            // Quagga.stop();
            closeCamera();

            const number = document.querySelector("[name='recycle-number']");
            if (number != null) {
                // number.focus();
                number.value = code;
                afterRecyleNumberInputChanged();
            }
        }
    })
}

function closeCamera() {
    Quagga.stop();

    // const close = document.querySelector('.camera-area');
    // if (close != null) close.classList.remove('open');
    
    const parent = document.getElementById('dialog-area');
    if (parent != null) deleteElements(parent);
}