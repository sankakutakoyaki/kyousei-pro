"use strict"

/******************************************************************************************************* リスト */
/**
 * テーブルリストのヘッダー部分作成
 * @param {テーブル自身} tbl 
 */
function createListHeader(tbl, names, isCheckBox) {
    if (tbl != null) {
        const panel = tbl.closest('.tab-panel-box');
        let tab = "00";
        if (panel != null) {
            tab = panel.dataset.panel;
        }
        const newRow = tbl.insertRow();
        newRow.setAttribute('id', 'row' + tab + '-0');
        newRow.setAttribute('name', 'tablelist-header');
        newRow.classList.add('pc-style');
        if (isCheckBox == true) {
            // 選択用チェックボックス
            newRow.insertAdjacentHTML('beforeend', '<th name="chk-cell"><input id="chk' + tab + '-0" class="normal-chk" type="checkbox" onclick="clickAllCheckBtn(this)"></th>');
        }
        names.forEach(function (name) {
            newRow.insertAdjacentHTML('beforeend', '<th><span>' + name + '<span class="sort-arrow"></span></span></th>');
        });
    }
}
/**
 * フッターの件数項目を更新する
 * @param {テーブル自身} tbl 
 */
function createListFooter(footerId, list) {
    // deleteElements('footer-text');
    // const parent = document.getElementById('footer-text');
    // parent.insertAdjacentHTML('beforeend', '<span>' + list.length + '件 : ' + getNow() + ' 現在</span>');
    deleteElements(footerId);
    const footer = document.getElementById(footerId);
    footer.insertAdjacentHTML('beforeend', '<span>' + list.length + '件 : ' + getNow() + ' 現在</span>');
}
/**
 * 新規作成画面を開く
 * @param {*} self 
 */
function execCreateForm(self, formentity) {
    // ケバブメニューを閉じる
    closeKebabDialog(self);
    // 入力フォームダイアログを開く
    formDialog(formentity, '新規', '作成');
}
/**
 * 編集画面を開く
 * @param {*} self 
 */
async function execEditForm(selectsqldata) {
    // スピナー表示
    startProcessing();
    // 選択されたIDのエンティティを取得
    const data = JSON.stringify(selectsqldata);
    const resultResponse = await postFetch("/sql/get/single", data, token, 'application/json');
    const result = await resultResponse.json();
    // 入力フォームダイアログを開く
    formDialog(result, '編集', '更新');
    // スピナー消去
    processingEnd();
}
/**
 * 選択された要素をDBから削除する
 * @param {*} parent 親要素
 */
// async function execRemove(parent, simpledata) {
async function exexRemove(formData, listData, url, tableId) {
    // スピナー表示
    startProcessing();
    // 選択された要素を全て取得
    const parent = document.getElementById(tableId);
    const ids = getAllSelectedInCheckbox(parent);
    if (ids != null) {
        // 選択された要素をSQL文(IN句)に変換
        const str = getSearchStringWithSelectedId(ids);
        if (str != null) {
            formData.ids = str;
            formData.user_name = username;
            const data = JSON.stringify(formData);
            const resultResponse = await postFetch(url, data, token, 'application/json');
            const result = await resultResponse.json();

            // const history = structuredClone(originHistory);
            // history.state_str = "削除";
            // history.contents = "ID=" + str.slice(3);
            // const historyStr = "INSERT INTO history (user_name, table_name, state_str, contents) VALUES (" +
            //                     "'" + history.user_name + "' , '" + history.table_name + "', '" + history.state_str + "', '" + history.contents + "');";
            // simpledata.text = simpledata.text + str + "DECLARE @ROW_COUNT int; SET @ROW_COUNT = @@ROWCOUNT;" + historyStr + "SELECT @ROW_COUNT as number;"
            // };
            // // SQL実行
            // const data = JSON.stringify(simpledata);
            // const resultResponse = await postFetch("/sql/excute", data, token, 'application/json');
            // const result = await resultResponse.json();
            if (result == 0) {
                messageDialog("削除できませんでした", "失敗", "red");
            } else {
                messageDialog("削除しました", "成功", "blue");
                // updateFunc();
                // 画面更新
                await updateList(listData);
                // // 追加・変更行に移動
                // scrollIntoTableList(tableId, result);
            }
        }
    } else {
        messageDialog("選択されていません", "警告", "red");
    }
    // simpledata.text = "";
    // スピナー消去
    processingEnd();
}
/**
 * 選択された要素のCSVファイルを作成してダウンロードする
 * @param {*} parent 
 * @param {*} downloaddata 
 */
async function execDownloadCsv(parent, downloaddata) {
    // スピナー表示
    startProcessing();
    // 選択された要素を全て取得
    const ids = getAllSelectedInCheckbox(parent);
    if (ids != null) {
        // 選択された要素をSQL文(IN句)に変換
        const str = getSearchStringWithSelectedId(ids);
        if (str != null) {
            // ダウンロード用SQL文に選択された要素を追加
            downloaddata.sqlString += str;
            // CSVファイルを作成
            const data = JSON.stringify(downloaddata);
            const result = await postFetch("/download/csv", data, token, "application/json");
            const text = await result.text();
            // 文字列データが返却されなければ、エラーメッセージを表示
            if (text == null || text == "") {
                messageDialog("作成できませんでした", "失敗", "red");
            } else {
                // CSVファイルをダウンロード
                const bom = new Uint8Array([0xef, 0xbb, 0xbf]);
                const blob = new Blob([bom, text], { type: "text/csv" });
                const objectUrl = URL.createObjectURL(blob);
                const downloadLink = document.createElement("a");
                downloadLink.download = getNowNoBreak() + ".csv";
                downloadLink.href = objectUrl;
                downloadLink.click();
                downloadLink.remove();
                // スピナー消去
                processingEnd();
                return true;
            }
        }
    } else {
        messageDialog("選択されていません", "警告", "red");
        // スピナー消去
        processingEnd();
        return null;
    }
    // スピナー消去
    processingEnd();
    return false;
}
/**
 * リストデータを更新して画面を再描画する
 */
async function updateList(sqldata) {
    // スピナー表示
    startProcessing();
    // リストデート取得
    const data = JSON.stringify(sqldata);
    const resultResponse = await postFetch("/sql/get/list", data, token, 'application/json');
    origin = await resultResponse.json();
    // // 編集用リストを初期化用リストからコピー
    // list = structuredClone(origin);
    // 画面更新
    filterDisplay();
    // スピナー消去
    processingEnd();
}
/**
 * テーブルリスト画面を更新する
 */
function updateListDisplay(tableId, headerNames, list, footerId, isCheckBox) {
    // リスト作成
    const tbl = document.getElementById(tableId);
    if (tbl != null) {
        // リスト画面を初期化
        deleteElements(tableId);
        // ヘッダー作成
        const header = tbl.createTHead();
        createListHeader(header, headerNames, isCheckBox);
        // リスト作成
        const body = tbl.createTBody();
        body.setAttribute('name', 'scroll-element');
        createListContent(body, list);
        // フッター作成
        createListFooter(footerId, list);
        // テーブルをソート可能にする
        makeSortable(tbl);
        // スクロール時のページトップボタン処理を登録する
        setPageTopButton(tableId);
    }
}
/**
 * フォームデータをDBに登録する
 * @param {*} formData 
 * @param {*} url 
 */
async function updateForm(formData, listData, url, tableId) {
    // スピナー表示
    startProcessing();
    // 保存処理
    formData.user_name = username;
    const data = JSON.stringify(formData);
    const resultResponse = await postFetch(url, data, token, 'application/json');
    const result = await resultResponse.json();
    if (result == 0) {
        messageDialog("登録できませんでした", 'エラー', 'red');
    } else {
        messageDialog("登録しました", '成功', 'blue');
        // 画面更新
        await updateList(listData);
        // 追加・変更行に移動
        scrollIntoTableList(tableId, result);
    }
    // ダイアログを閉じる
    closeDialog('form-dialog-area');

    // スピナー消去
    processingEnd();
}
/**
 * SQL文を実行してDBに登録する
 * @param {*} sqlData 
 * @param {*} url 
 */
async function updateData(listData, simpledata, tableId) {
    // スピナー表示
    startProcessing();
    // 保存処理
    simpledata.text = "DECLARE @@ROW_COUNT int;" + simpledata.text + ";SELECT @@ROW_COUNT";
    // SQL実行
    const data = JSON.stringify(simpledata);
    const resultResponse = await postFetch("/sql/excute", data, token, 'application/json');
    const result = await resultResponse.json();
    if (result == false) {
        messageDialog("失敗しました", 'エラー', 'red');
    } else {
        messageDialog("保存しました", '成功', 'blue');
        // 画面更新
        await updateList(listData);
        // 追加・変更行に移動
        scrollIntoTableList(tableId, result);
    }
    simpledata.text = "";
    // スピナー消去
    processingEnd();
}
/**
 * データ入力用フォームダイアログを開く
 * @param {*} item 
 * @param {*} title 
 * @param {*} btnText 
 */
function formDialog(item, title, btnText) {
    const elmForm = document.createElement('div');
    elmForm.classList.add('form-dialog');
    // ヘッダー
    elmForm.insertAdjacentHTML('beforeend', '<div class="dialog-header"><span>' + title + '</span>' + 
        '<div class="img-btn" onclick="closeDialog(\'form-dialog-area\', event)"><img title="閉じる" src="/icons/close-s.png"></div>');
    // コンテント
    const elmContent = getFormContent(item);
    elmForm.insertAdjacentElement('beforeend', elmContent);
    // フッター
    elmForm.insertAdjacentHTML('beforeend', '<div class="dialog-footer">' + 
        '<button class="normal-btn frameless" type="button" onclick="closeDialog(\'form-dialog-area\', event)"><span>キャンセル</span></button>' + 
        '<button class="normal-btn ok" type="button" onclick="execUpdateFormData()"><span>' + btnText + '</span></button>');
    // ダイアログ作成
    const parent = document.getElementById('form-dialog-area');
    parent.insertAdjacentHTML('beforeend', '<div class="dialog"></div>');
    parent.insertAdjacentElement('beforeend', elmForm);
}
// 指定した要素へスクロールさせる
function scrollIntoTableList(tableId, id) {
    const parent = document.getElementById(tableId);
    const panel = parent.closest('.tab-panel-box');
    let tab = "00";
    if (panel != null) {
        tab = panel.dataset.panel;
    }
    const row = document.getElementById('row' + tab + "-" + id);
    if (row != null) {
        row.scrollIntoView({
            block: "center"
        });
    }
}
// バーコード読み取り
function execCamera(self) {
    const number = document.querySelector("[name='recycle-number']");
    if (number != null) number.value = "";
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
            messageDialog("カメラがありません", "エラー", "red");
            return
        }
        console.log("Initialization finished. Ready to start");
        Quagga.start();
    })
    Quagga.onDetected(success => {
        const code = success.codeResult.code;
        if(code) {
            // Quagga.stop();
            closeCamera();

            const number = document.querySelector("[name='recycle-number']");
            if (number != null) {
                number.value = code;
                afterRecyleNumberInputChanged();
            }
        }
    })
}
function closeCamera() {
    Quagga.stop();
    const parent = document.getElementById('dialog-area');
    if (parent != null) deleteElements(parent);
}