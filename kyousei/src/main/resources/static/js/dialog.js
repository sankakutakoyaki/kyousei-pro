
/******************************************************************************************************* メッセージダイアログ作成 */
/**
 * メッセージダイアログを表示する
 * @param {本文} msg 
 * @param {タイトル} title 
 * @param {ヘッダーの色} headerColor 
 */
function createMessageDialog(msg, title, headerColor) {
    const elmFooter = document.createElement('form');
    elmFooter.classList.add('dialog-footer', 'msg');
    elmFooter.insertAdjacentHTML('beforeend', '<button id="msg-okbtn" class="normal-btn ok" type="submit" onclick="closeDialog(\'dialog-area\', event)"><span>了解</span></button>');
    createDialogElement(msg, title, headerColor, elmFooter);
}

/******************************************************************************************************* コンフィルムダイアログ作成 */
/**
 * コンフィルムダイアログを表示する
 * @param {本文} msg 
 * @param {タイトル} title 
 * @param {ヘッダーの色} headerColor 
 * @param {ボタンのテキスト} btnText 
 * @param {実行する関数} funcName 
 */
function createConfirmDialog(msg, title, headerColor, btnText, funcName) {
    const elmFooter = document.createElement('form');
    elmFooter.classList.add('dialog-footer', 'msg');
    elmFooter.setAttribute('onsubmit', funcName);
    elmFooter.insertAdjacentHTML('beforeend', '<button id="close-btn" class="normal-btn frameless" type="button" onclick="closeDialog(\'dialog-area\', event)"><span>キャンセル</span></button>');
    elmFooter.insertAdjacentHTML('beforeend', '<button id="msg-okbtn" class="normal-btn ok" type="submit"><span>' + btnText + '</span></button>');
    createDialogElement(msg, title, headerColor, elmFooter);
}

/******************************************************************************************************* ダイアログエレメント作成 */
/**
 * メッセージ・コンフィルム両方に共通する部分の作成
 * @param {本文} msg 
 * @param {タイトル} title 
 * @param {ヘッダーの色} headerColor 
 * @param {ボタンを含んだフッター} elmFooter 
 */
function createDialogElement(msg, title, headerColor, elmFooter) {
    const element = document.querySelector('.normal-content');
    if (element != null) element.inert = true;

    const parent = document.getElementById('dialog-area');
    const elm = document.createElement('div');
    
    const check = document.querySelector('.dialog');
    if (check != null) elm.classList.add('trans');
    elm.classList.add('dialog');

    const elmHeader = document.createElement('div');
    elmHeader.classList.add('dialog-header', headerColor);
    elmHeader.insertAdjacentHTML('beforeend', '<span>' + title + '</span>');

    const elmContent = document.createElement('div');
    elmContent.className = 'msg-dialog-content';
    const arrayMsg = msg.replace(/\s+/, '<br>');
    elmContent.insertAdjacentHTML('beforeend', '<span>' + arrayMsg + '</span>');

    const elmForm = document.createElement('div');
    elmForm.classList.add('form-dialog');
    elmForm.insertAdjacentElement('beforeend', elmHeader);
    elmForm.insertAdjacentElement('beforeend', elmContent);
    elmForm.insertAdjacentElement('beforeend', elmFooter);

    parent.insertAdjacentElement('beforeend', elm);
    parent.insertAdjacentElement('beforeend', elmForm);

    document.getElementById('msg-okbtn').focus();
}

/******************************************************************************************************* 閉じる処理 */
/**
 * 指定したID以下のエレメントを全て削除する
 * @param {親要素のID} idName 
 * @param {イベント} e 
 */
function closeDialog(idName, e) {
    if (e != null) {
        e.preventDefault();
    }
    const parent = document.getElementById(idName);
    if (parent.hasChildNodes()) {
        while (parent.firstChild) {  // 全ての子要素を削除
            parent.removeChild(parent.firstChild);
        }
    }

    const elm = document.querySelector('.normal-content');
    if (elm != null) elm.inert = false;
}

/******************************************************************************************************* メッセージダイアログ */
/**
 * 保存成功のメッセージダイアログ
 */
function saveSuccessDialog() {
    const title = "成功";
    const msg = '保存しました。';
    const headerColor = 'blue';
    createMessageDialog(msg, title, headerColor);
}

/**
 * 作成成功のメッセージダイアログ
 */
function createSuccessDialog() {
    const title = "成功";
    const msg = '作成しました。';
    const headerColor = 'blue';
    createMessageDialog(msg, title, headerColor);
}

/**
 * 更新成功のメッセージダイアログ
 */
function updateSuccessDialog() {
    const title = "成功";
    const msg = '更新しました。';
    const headerColor = 'blue';
    createMessageDialog(msg, title, headerColor);
}

/**
 * 削除成功のメッセージダイアログ
 */
function removeSuccessDialog() {
    const title = "成功";
    const msg = '削除しました。';
    const headerColor = 'blue';
    createMessageDialog(msg, title, headerColor);
}

/**
 * 失敗のメッセージダイアログ
 */
function failureDialog() {
    const msg = '失敗しました。';
    const title = '失敗'
    const headerColor = 'red';
    createMessageDialog(msg, title, headerColor);
}

/**
 * 一つも選択されていない時のメッセージダイアログ
 */
function notSelectedDialog() {
    const msg = '選択されていません。';
    const title = '未選択'
    const headerColor = 'yellow';
    createMessageDialog(msg, title, headerColor);
}

/**
 * 
 * @param {*} item 
 * @param {*} title 
 * @param {*} btnText 
 */
function createFormDialog(item, title, btnText) {
    // ヘッダー
    const elmHeader = getFormDialogHeader(title);
    // コンテント
    const elmContent = getFormDialogContent(item);
    // フッター
    const elmFooter = getFormDialogFooterConfirmBtn(btnText)
    // ダイアログ作成
    const elmForm = document.createElement('div');
    elmForm.classList.add('form-dialog');
    elmForm.insertAdjacentElement('beforeend', elmHeader);
    elmForm.insertAdjacentElement('beforeend', elmContent);
    elmForm.insertAdjacentElement('beforeend', elmFooter);
    const parent = document.getElementById('form-dialog-area');
    const elm = document.createElement('div');
    elm.classList.add('dialog');
    parent.insertAdjacentElement('beforeend', elm);
    parent.insertAdjacentElement('beforeend', elmForm);
}

/**
 * 
 * @param {*} title 
 * @returns 
 */
function getFormDialogHeader(title) {
    const elmHeader = document.createElement('div');
    elmHeader.classList.add('dialog-header');
    elmHeader.insertAdjacentHTML('beforeend', '<span>' + title + '</span>');
    // 閉じるボタン
    const closeElm = document.createElement('div');
    closeElm.classList.add('img-btn');
    closeElm.onclick = function (event) { closeDialog('form-dialog-area', event); }
    closeImg = document.createElement('img');
    closeImg.setAttribute('title', '閉じる');
    closeImg.setAttribute('src', '/icons/close-s.png');
    closeElm.insertAdjacentElement('beforeend', closeImg);
    elmHeader.insertAdjacentElement('beforeend', closeElm);
    return elmHeader;
}

/**
 * 
 * @param {*} btnText 
 * @returns 
 */
function getFormDialogFooterConfirmBtn(btnText) {
    const elmFooter = document.createElement('div');
    elmFooter.classList.add('dialog-footer');
    elmFooter.insertAdjacentHTML('beforeend', '<button class="normal-btn frameless ct5 cp4" type="button" onclick="closeDialog(\'form-dialog-area\', event)"><span>キャンセル</span></button>');
    elmFooter.insertAdjacentHTML('beforeend', '<button class="normal-btn ok ct9 cp4" type="button" onclick="execUpdateFormData()"><span>' + btnText + '</span></button>');
    return elmFooter;
}


/******************************************************************************************************* カメラダイアログ作成 */
/**
 * カメラダイアログを表示する
 * @param {本文} msg 
 * @param {タイトル} title 
 * @param {ヘッダーの色} headerColor 
 */
function createCameraDialog() {
    const element = document.querySelector('.normal-content');
    if (element != null) element.inert = true;

    const parent = document.getElementById('dialog-area');
    const elm = document.createElement('div');
    elm.classList.add('dialog');

    const formElm = document.createElement('div');
    formElm.classList.add('form-dialog');

    const areaElm = document.createElement('div');
    areaElm.classList.add('camera-area');

    areaElm.insertAdjacentHTML('beforeend', '<div id="interactive" class="viewport"></div>');
    areaElm.insertAdjacentHTML('beforeend', '<button class="img-btn close" onclick="closeDialog(\'dialog-area\');"><img src="/icons/close.png"></button>');
    formElm.insertAdjacentElement('beforeend', areaElm);

    parent.insertAdjacentElement('beforeend', elm);
    parent.insertAdjacentElement('beforeend', formElm);
}
