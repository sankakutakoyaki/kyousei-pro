/******************************************************************************************************* 画面更新 */
/**
 * テーブルリスト画面を更新する
 */
function updateDisplay() {
    // // スピナーが表示されていなければ表示させる
    startProcessing();

    // リスト消去
    clearTableListByName('[name="data-row"]');
    // リスト作成
    createTable();
    // フッター作成
    createFooter();
    
    // // 処理が終了したらスピナーを消去する
    processingEnd();
}

/**
 * データを再取得して画面更新する
 */
async function updateTableListDisplay() {
    const search = document.getElementById('search-box');
    if (search != null) {
        search.value = "";
    }
    // リストデータを再取得する
    await updateTableList();
    // 取得日時を更新する
    acquisitionDateTime = getNow();
    // 選択を全て解除する
    deselectAllCheckBtn('all-chk-btn');
    // 画面を更新する
    updateDisplay();
}

/**
 * リストの中身を全て消去
 * @param {消去対象の名前} elmName 
 */
function clearTableListByName(elmName) {
    const parents = document.querySelectorAll(elmName);
    parents.forEach((parent) => {
        while (parent.firstChild) {
            parent.removeChild(parent.firstChild);
        }
        parent.remove();
    });
}

/**
 * 単一エンティティ取得用の、WHERE句用文字列
 * @param {取得するエンティティのID} id 
 * @returns WHERE句用文字列
 */
function addSearchCriteriaForSqlString(id) {
    return tablelistSearchString + " = " + id;
}

// 選択されたIDで検索条件を作成
/**
 * 複数エンティティ取得用の、WHERE句用文字列
 * @param {選択したチェックボックスが含まれるテーブル} self 
 * @returns WHERE句用文字列
 */
function createSqlStringFromSearchCriteriaByIds(self) {
    const ids = getAllSelectedCheckboxes(self);
    let str = tablelistSearchString + " IN (";
    return getSqlStringFromSearchCriteriaByIds(ids, str);
}

/******************************************************************************************************* フッター */
/**
 * フッターの件数項目を更新する
 */
function createFooter() {
    const parent = document.getElementById('footer-text');
    parent.firstElementChild.remove();
    parent.insertAdjacentHTML('beforeend', '<span>' + list.length + '件 : ' + acquisitionDateTime + '現在</span>');
}

/******************************************************************************************************* リスト */
/**
 * テーブルリストのヘッダー画面作成
 */
function createHeader() {
    const tbl = document.getElementById(tablelistName);
    createTableListHeader(tbl, tablelistHeaderNames);
}

/**
 * テーブルリストのデータ画面作成
 */
function createTable() {
    const tbl = document.getElementById(tablelistName);
    createTableListContent(tbl, list);
}

/**
 * テーブルリストデータを取得・表示する
 */
async function updateTableList() {
    startProcessing();

    const url = "/tablelist/update";
    const data = JSON.stringify(sqllistdata);
    const contentType = 'application/json';
    const result = await postFetch(url, data, token, contentType);
    tablelist = await result.json();
    // リストから新しいリストを抽出するための関数
    extractListFromSelectionCriteria();

    processingEnd();
}

/**
 * 記憶させたサイドバーの選択IDをもとにリストを抽出
 */
function extractListFromSelectionCriteria() {
    if (sidebarSelectId == 0) {
        list = tablelist;
    } else {
        list = tablelist.filter(value => (value.category_id == sidebarSelectId));
    }
}

/**
 * リスト選択時の処理
 * @param {リストで選択したアイテムのID} id 
 * @param {スクロールさせる対象のテーブルリストのID} tblName
 * @param {スクロールの方向} isTop 
 */
function updateWhenSelected(id, tblName, isTop) {
    sidebarSelectId = id;
    extractListFromSelectionCriteria();
    updateDisplay();
    document.getElementById(tblName).scrollIntoView(isTop);
}

/**
 * 新規作成・更新時の処理
 * @param {リストで選択したアイテムのID} id 
 * @param {スクロールさせる対象のテーブルリストのID} tblName 
 * @param {スクロールの方向} isTop 
 */
async function updateWhenCreated(id, tblName, isTop) {
    sidebarSelectId = id;
    // リストデータを再取得する
    await updateTableListDisplay();
    document.getElementById(tblName).scrollIntoView(isTop);
}

/**
 * 選択したアイテムを全て削除する
 * @param {削除用のアドレス} url 
 * @param {削除ボタン自身} self 
 * @returns 
 */
async function removeItemFromList(url, self) {
    // 削除ボタンの親要素（tablelist-container）を取得する
    const parent = self.closest('.tablelist-container');
    // 削除用のSQL文を取得する（選択されていなければ[NULL]が返る）
    const deleteStr = createSqlStringFromSearchCriteriaByIds(parent);
    if (deleteStr == null) {
        notSelectedDialog();
        return false;
    }
    const deletesqldata = { "sqlString": deleteStr, "classPath": sqllistdata.classPath }
    const data = JSON.stringify(deletesqldata);
    const contentType = 'application/json';
    const result = await postFetch(url, data, token, contentType);
    const text = await result.json();

    if (text == false) {
        // 削除失敗のダイアログを表示する
        failureDialog();
        return false;
    } else {
        // フォーム画面を閉じる
        closeDialog('form-dialog-area');
        // リストデータを再取得して画面を更新する
        await updateTableListDisplay();
        // 削除成功のダイアログを表示する
        removeSuccessDialog();
        return true;
    }
}

/**
 * テーブルリストのヘッダー部分作成
 * @param {テーブル自身} tbl 
 * @param {ヘッダーのタイトルリスト} names 
 */
function createTableListHeader(tbl, names) {
    if (tbl != null) {
        const newRow = tbl.insertRow();
        newRow.setAttribute('id', 'row0');
        newRow.setAttribute('name', 'tablelist-header');
        // 選択用チェックボックス
        const chkCell = document.createElement('th');
        chkCell.setAttribute('name', 'chk-cell');
        chkCell.innerHTML = '<input id="all-chk-btn" class="normal-chk" type="checkbox" onclick="clickAllCheckBtn(this)">';
        newRow.insertAdjacentElement('beforeend', chkCell);
        names.forEach(function (name) {
            let cell = document.createElement('th');
            cell.textContent = name;
            newRow.insertAdjacentElement('beforeend', cell);
        });
    }
}

/**
 * テーブルリストのコンテント部分作成
 * @param {テーブル自身} tbl 
 * @param {テーブルの内容} list 
 */
function createTableListContent(tbl, list) {
    list.forEach(function (item) {
        // 隠しID（Post送信用）
        let newRow = tbl.insertRow();
        newRow.setAttribute('id', 'row' + item.id);
        newRow.setAttribute('name', 'data-row');
        // 選択用チェックボックス
        let chk = newRow.insertCell();
        chk.setAttribute('name', 'chk-cell');
        chk.onclick = function () { clickCheckBtn(item.id, this); }
        chk.innerHTML = '<input id="chk' + item.id + '" class="normal-chk" name="chk-box" type="checkbox" value="' + item.id + '">';
        // コード
        let idCell = newRow.insertCell();
        idCell.setAttribute('name', 'link-cell');
        idCell.innerHTML += '<span onclick="createFormDisplay(' + item.id + ')">' + String(item.code).padStart(4, '0'); + '</span>';
        // 名前
        let nameCell = newRow.insertCell();
        let nameElm = document.createElement('div');
        nameElm.insertAdjacentHTML('beforeend', '<span class="kana6">' + item.full_name_kana + '</span>');
        nameElm.insertAdjacentHTML('beforeend', '<br>');
        nameElm.insertAdjacentHTML('beforeend', '<span>' + item.full_name + '</span>');
        nameCell.insertAdjacentElement('afterbegin', nameElm);
        // ファーストカテゴリー
        let categoryFirstCell = newRow.insertCell();
        if (item.category_first == null || item.category_first == '') {
            categoryFirstCell.innerHTML += '<span>登録なし</span>';
        } else {
            categoryFirstCell.innerHTML += '<span>' + item.category_first + '</span>';
        }
        // セカンドカテゴリー
        let categorySecondCell = newRow.insertCell();
        if (item.category_second == null || item.category_second == '') {
            categorySecondCell.innerHTML += '<span>登録なし</span>';
        } else {
            categorySecondCell.innerHTML += '<span>' + item.category_second + '</span>';
        }
    });
}

/**
 * CSVファイルをダウンロードする
 */
async function downloadCsvFile(url, self) {
    // ダウンロードボタンの親要素（tablelist-container）を取得する
    const parent = self.closest('.tablelist-container');
    // ダウンロード用のSQL文を取得する（選択されていなければ[NULL]が返る）
    const downloadStr = createSqlStringFromSearchCriteriaByIds(parent);
    if (downloadStr == null) {
        notSelectedDialog();
        return false;
    }
    const downloadsqldata = { "sqlString": sqlformdata.sqlString + downloadStr, "classPath": sqlformdata.classPath };
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

/**
 * 検索ワード（空白区切り）をもとにリストを抽出して画面更新する
 * @param {検索ワード} words 
 * @returns 
 */
function createTablelistFromSearchCriteria(words) {
    if (list.length == 0) return;
    extractListFromSelectionCriteria();
    let list_word = words.split(/\s+/);
    list_word.forEach(word => {
        const result = list.filter(val => {
            return extractListFromSearchString(val, word);
        });
        list = result;
    })
    updateDisplay();
}

/**
 * 検索ワードに該当するエンティティーを返す
 * @param {検索対象データ} val 
 * @param {検索ワード} word 
 * @returns 
 */
function extractListFromSearchString(val, word) {
    return val.full_name?.includes(word) ||
        val.full_name_kana?.includes(word) ||
        val.category_first?.includes(word) ||
        val.category_second?.includes(word);
}

/******************************************************************************************************* ケバブメニュー作成 */
/**
 * 登録リストヘッダー
 */
function createKebabRegistHeaderMenuDialog(tableId) {
    const tbl = document.getElementById(tableId);
    const isChecked = isAnyOneChecked(tbl);
    const parent = document.getElementById('kebab-menu-dialog-area');
    const elm = document.createElement('div');
    // elm.classList.add('dialog', 'clear-back');
    elm.classList.add('dialog');
    elm.onclick = function (event) { closeDialog('kebab-menu-dialog-area', event); }

    const elmContent = document.createElement('ul');
    // 新規作成ボタン
    const newElm = createKebabMenuItemByNew();
    elmContent.insertAdjacentElement('beforeend', newElm);
    // 選択解除ボタン
    if (isChecked == true) {
        const selectElm = createKebabMenuItemByDeselect();
        elmContent.insertAdjacentElement('beforeend', selectElm);
    }
    // 削除ボタン
    if (isChecked == true) {
        const deleteElm = createKebabMenuItemByDelete('/tablelist/remove/multiple');
        elmContent.insertAdjacentElement('beforeend', deleteElm);
    }
    // CSVダウンロードボタン
    if (isChecked == true) {
        const downElm = createKebabMenuItemByDownload('/tablelist/download/csv');
        elmContent.insertAdjacentElement('beforeend', downElm);
    }
    // 画面更新ボタン
    const updateElm = createKebabMenuItemByUpdate();
    elmContent.insertAdjacentElement('beforeend', updateElm);
    // 閉じるボタン
    const closeElm = createKebabMenuItemByClose();
    elmContent.insertAdjacentElement('beforeend', closeElm);

    const elmForm = document.createElement('div');
    elmForm.classList.add('menu-dialog', 'context');
    elmForm.insertAdjacentElement('beforeend', elmContent);

    parent.insertAdjacentElement('beforeend', elm);
    parent.insertAdjacentElement('beforeend', elmForm);
}

/******************************************************************************************************* メニュー部品 */
/**
 * 新規作成ボタン
 * @returns 
 */
function createKebabMenuItemByNew() {
    const newElm = document.createElement('li');
    // newElm.classList.add('sp-style');
    newElm.insertAdjacentHTML('beforeend', '<img title="新規作成" src="/icons/add.png">');
    const newText = document.createTextNode('作成');
    newElm.appendChild(newText);
    newElm.onclick = function (event) {        
        closeDialog('kebab-menu-dialog-area', event);
        createFormDisplay(0);
    }
    return newElm;
}
/**
 * 選択解除ボタン
 * @returns 
 */
function createKebabMenuItemByDeselect() {
    const selectElm = document.createElement('li');
    selectElm.insertAdjacentHTML('beforeend', '<img title="選択解除" src="/icons/check.png">');
    const selectText = document.createTextNode('選択を解除');
    selectElm.appendChild(selectText);
    selectElm.onclick = async function () {        
        const allchkbtn = document.getElementById('all-chk-btn');
        allchkbtn.checked = false;
        clickAllCheckBtn(allchkbtn);
        closeDialog('kebab-menu-dialog-area');
    }
    return selectElm;
}
/**
 * 削除ボタン
 */
function createKebabMenuItemByDelete(url) {
    const deleteElm = document.createElement('li');
    deleteElm.insertAdjacentHTML('beforeend', '<img title="削除" src="/icons/dust.png">');
    const deleteText = document.createTextNode('削除');
    deleteElm.appendChild(deleteText);
    deleteElm.onclick = function () {
        removeItemFromList(url, this);
        closeDialog('kebab-menu-dialog-area');
    }
    return deleteElm;
}
/**
 * CSVダウンロードボタン
 * @returns 
 */
function createKebabMenuItemByDownload(url) {
    const downElm = document.createElement('li');
    downElm.insertAdjacentHTML('beforeend', '<img title="ダウンロード" src="/icons/download.png">');
    const downText = document.createTextNode('ダウンロード(CSV)');
    downElm.appendChild(downText);
    downElm.onclick = function () {
        downloadCsvFile(url, this);
        closeDialog('kebab-menu-dialog-area');
    }
    return downElm;
}
/**
 * 再読み込みボタン
 * @returns 
 */
function createKebabMenuItemByUpdate() {
    const updateElm = document.createElement('li');
    updateElm.insertAdjacentHTML('beforeend', '<img title="再読み込み" src="/icons/update.png">');
    const updateText = document.createTextNode('再読み込み');
    updateElm.appendChild(updateText);
    updateElm.onclick = async function () {
        closeDialog('kebab-menu-dialog-area');
        await updateTableListDisplay();
        
    }
    return updateElm;
}
/**
 * 閉じるボタン
 * @returns 
 */
function createKebabMenuItemByClose() {
    const closeElm = document.createElement('li');
    closeElm.insertAdjacentHTML('beforeend', '<img title="閉じる" src="/icons/close.png">');
    const closeText = document.createTextNode('閉じる');
    closeElm.appendChild(closeText);
    closeElm.onclick = function () {
        closeDialog('kebab-menu-dialog-area');
    }
    return closeElm;
}