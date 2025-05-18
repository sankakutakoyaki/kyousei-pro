
/******************************************************************************************************* ケバブメニュー作成 */
/**
 * 登録リストヘッダー
 */
async function createKebabRegistHeaderMenuDialog(tableId) {
    const tbl = document.getElementById(tableId);
    const isChecked = await isAnyOneChecked(tbl);
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
    updateElm.onclick = function () {
        closeDialog('kebab-menu-dialog-area');
        updateTableListDisplay();
        
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