/******************************************************************************************************* フォーム作成 */
/**
 * 検索条件をもとに、選択したIDのエンティティーを取得してフォーム画面を表示する
 * @param {取得する要素の検索用ID} id 
 */
async function createFormDisplay(id) {
    // 検索条件を[SQLSELECT文]に追加
    const selectStr = addSearchCriteriaForSqlString(id);
    const selectsqldata = { "sqlString": sqlformdata.sqlString + selectStr, "classPath": sqlformdata.classPath };
    // 追加・更新処理
    const title = id == 0 ? '新規作成' : '編集';
    const btnText = id == 0 ? '作成' : '更新';
    const data = JSON.stringify(selectsqldata);
    const contentType = 'application/json';
    const result = await postFetch('/tablelist/getform/id', data, token, contentType);
    const item = await result.json();
    createFormDialog(item, title, btnText);
}

/**
 * フォーム画面を作成する
 * @param {取得したエンティティー} item 
 * @param {タイトル} title 
 * @param {ボタンテキスト} btnText 
 */
function createFormDialog(item, title, btnText) {
    // ヘッダー
    const elmHeader = getFormHeader(title);
    // コンテント
    const elmContent = getFormContent(item);
    // フッター
    const elmFooter = getFormFooterByComfirm(btnText)
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

/******************************************************************************************************* 保存 */
/**
 * フォームデータで入力した内容をもとにエンティティーを追加・更新する
 * @param {追加・更新用のアドレス} url 
 * @param {追加・更新するデータ} formdata 
 * @param {フォームデータのID} id 
 * @param {カテゴリーID} selectId 
 * @param {テーブル名} tblName 
 * @param {選択するサイドバーアイテム} obj 
 * @returns 
 */
async function saveFormData(url, formdata, id, selectId, tblName, obj) {
    const data = JSON.stringify(formdata);
    const contentType = 'application/json';
    const resultResponse = await postFetch(url, data, token, contentType);
    const result = await resultResponse.json();
    if (result == false) {
        // 失敗のダイアログを表示する
        failureDialog();
    } else {
        // フォーム画面を閉じる
        closeDialog('form-dialog-area');
        // IDが0ならば追加の処理、0以上なら更新の処理を実行する
        if (id == 0) {
            // 追加成功のダイアログを表示する
            createSuccessDialog();
            if (selectId != sidebarSelectId) {
                // サイドバーアイテムを選択して画面更新する
                changeListWhenSelected(selectId, obj, updateWhenCreated, tblName, false);
                return;
            } else {
                await updateWhenCreated(selectId, tblName, false)
            }
        } else {
            // 更新成功のダイアログを表示する
            updateSuccessDialog();
        }
        // 画面更新
        await updateTableListDisplay();
    }
}

/******************************************************************************************************* ヘッダー */
/**
 * フォーム画面のヘッダーを作成する
 * @param {タイトル} title 
 * @returns 
 */
function getFormHeader(title) {
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

/******************************************************************************************************* フッター */
/**
 * フォーム画面のフッターを作成する（コンフィルムボタン）
 * @param {ボタンテキスト} btnText 
 * @returns 
 */
function getFormFooterByComfirm(btnText) {
    const elmFooter = document.createElement('div');
    elmFooter.classList.add('dialog-footer');
    elmFooter.insertAdjacentHTML('beforeend', '<button class="normal-btn frameless ct5 cp4" type="button" onclick="closeDialog(\'form-dialog-area\', event)"><span>キャンセル</span></button>');
    elmFooter.insertAdjacentHTML('beforeend', '<button class="normal-btn ok ct9 cp4" type="button" onclick="execUpdateFormData()"><span>' + btnText + '</span></button>');
    return elmFooter;
}

/******************************************************************************************************* 部品 */
/**
 * フォーム画面の性別選択ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getGenderSelectBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>性別</label>')
    const select = document.createElement('select');
    select.classList.add('normal-select');
    select.setAttribute('name', 'gender');
    select.insertAdjacentHTML('beforeend', '<option value=0 ' + (item.gender == 0 ? ' selected' : "") + '>選択してください</option>');
    select.insertAdjacentHTML('beforeend', '<option value=1 ' + (item.gender == 1 ? ' selected' : "") + '>男</option>');
    select.insertAdjacentHTML('beforeend', '<option value=2 ' + (item.gender == 2 ? ' selected' : "") + '>女</option>');
    select.insertAdjacentHTML('beforeend', '<option value=9 ' + (item.gender == 9 ? ' selected' : "") + '>無回答</option>');
    elm.insertAdjacentElement('beforeend', select);
    return elm;
}

/**
 * フォーム画面の支払い方法選択ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getPaymentMethodSelectBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>支払い方法</label>')
    const select = document.createElement('select');
    select.classList.add('normal-select');
    select.setAttribute('name', 'payment_method');
    // select.insertAdjacentHTML('beforeend', '<option value=0 ' + (item.payment_method == 0 ? ' selected' : "") + '>選択してください</option>');
    select.insertAdjacentHTML('beforeend', '<option value=1 ' + (item.payment_method == 1 ? ' selected' : "") + '>振り込み</option>');
    select.insertAdjacentHTML('beforeend', '<option value=2 ' + (item.payment_method == 2 ? ' selected' : "") + '>日払い</option>');
    elm.insertAdjacentElement('beforeend', select);
    return elm;
}

/**
 * フォーム画面の血液型選択ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getBloodTypeSelectBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>血液型</label>')
    const select = document.createElement('select');
    select.classList.add('normal-select');
    select.setAttribute('name', 'blood_type');
    select.insertAdjacentHTML('beforeend', '<option value=0 ' + (item.blood_type == 0 ? ' selected' : "") + '>選択してください</option>');
    select.insertAdjacentHTML('beforeend', '<option value=1 ' + (item.blood_type == 1 ? ' selected' : "") + '>A型</option>');
    select.insertAdjacentHTML('beforeend', '<option value=2 ' + (item.blood_type == 2 ? ' selected' : "") + '>B型</option>');
    select.insertAdjacentHTML('beforeend', '<option value=3 ' + (item.blood_type == 3 ? ' selected' : "") + '>O型</option>');
    select.insertAdjacentHTML('beforeend', '<option value=4 ' + (item.blood_type == 4 ? ' selected' : "") + '>AB型</option>');
    select.insertAdjacentHTML('beforeend', '<option value=9 ' + (item.blood_type == 9 ? ' selected' : "") + '>無回答</option>');
    elm.insertAdjacentElement('beforeend', select);
    return elm;
}

/**
 * フォーム画面の住所入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getAddressInputBox(item) {
    const elm = document.createElement('div');
    // elm.classList.add('search-box', 'postal');
    elm.classList.add('form-dialog-parts', 'separate');
    elm.insertAdjacentHTML('beforeend', '<label class="cp3">郵便番号</label>');
    elm.insertAdjacentHTML('beforeend', '<label class="ct4 cp9 pc-style">住所</label>');
    const postal = document.createElement('form');
    postal.setAttribute('onsubmit', 'getAddressFromPostalCode("postalcode", "fulladdress", event)');
    // postal.classList.add('search-box', 'postal', 'cp2');
    postal.classList.add('cp3');
    postal.insertAdjacentHTML('beforeend', '<input id="postalcode" name="postal_code" class="normal-input" type="text" inputmode="numeric" pattern="\\d{3}-?\\d{4}" value="' + (item.postal_code ?? "") + '" placeholder="012-3456" onfocus="this.select();">');
    elm.insertAdjacentElement('beforeend', postal);
    elm.insertAdjacentHTML('beforeend', '<label class="sp-style">住所</label>');
    elm.insertAdjacentHTML('beforeend', '<input id="fulladdress" name="full_address" class="normal-input ct4 cp9" type="text" value="' + (item.full_address ?? "") + '" placeholder="例）大阪府高槻市柱本5丁目7番10号" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の生年月日入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getBirthdaySelectBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>生年月日</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="birthday" class="normal-input" type="date" value="' + (item.birthday == "9999-12-31" ? "" : item.birthday) + '" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の氏名入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getSeparateNameInputBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts', 'separate');
    elm.insertAdjacentHTML('beforeend', '<label class="cp12">氏名</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="last_name" class="normal-input cp6" type="text" value="' + (item.last_name ?? "") + '" placeholder="姓" onfocus="this.select();">');
    elm.insertAdjacentHTML('beforeend', '<input name="first_name" class="normal-input cp6 ct7" type="text" value="' + (item.first_name ?? "") + '" placeholder="名" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の氏名入力ボックス（かな入力ボックス付き）を作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getSeparateNameInputBoxWithKana(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts', 'separate');
    elm.insertAdjacentHTML('beforeend', '<label class="cp12">氏名</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="last_name" class="normal-input cp6" type="text" value="' + (item.last_name ?? "") + '" placeholder="姓" onfocus="this.select();">');
    elm.insertAdjacentHTML('beforeend', '<input name="first_name" class="normal-input cp6 ct7" type="text" value="' + (item.first_name ?? "") + '" placeholder="名" onfocus="this.select();">');
    elm.insertAdjacentHTML('beforeend', '<label class="sp-style">ふりがな</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="last_name_kana" class="normal-input cp6" type="text" value="' + (item.last_name_kana ?? "") + '" placeholder="せい" onfocus="this.select();">');
    elm.insertAdjacentHTML('beforeend', '<input name="first_name_kana" class="normal-input cp6 ct7" type="text" value="' + (item.first_name_kana ?? "") + '" placeholder="めい" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の名前入力ボックス（かな入力ボックス付き）を作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getNameInputBoxWithKana(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>名前</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="full_name" class="normal-input" type="text" value="' + (item.full_name ?? "") + '" placeholder="名前" onfocus="this.select();">');
    elm.insertAdjacentHTML('beforeend', '<label class="sp-style">ふりがな</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="full_name_kana" class="normal-input" type="text" value="' + (item.full_name_kana ?? "") + '" placeholder="なまえ" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の携帯番号入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getPhoneNumberInputBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>携帯番号</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="phone_number" class="normal-input" type="text" inputmode="numeric" pattern="\\d{2,4}-\\d{2,4}-\\d{3,4}" value="' + (item.phone_number ?? "") + '" placeholder="012-3456-7890" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の電話番号入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getTelNumberInputBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>電話番号</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="tel_number" class="normal-input" type="text" inputmode="numeric" pattern="\\d{2,4}-\\d{2,4}-\\d{3,4}" value="' + (item.tel_number ?? "") + '" placeholder="012-3456-7890" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面のFAX番号入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getFaxNumberInputBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>FAX番号</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="fax_number" class="normal-input" type="text" inputmode="numeric" pattern="\\d{2,4}-\\d{2,4}-\\d{3,4}" value="' + (item.fax_number ?? "") + '" placeholder="012-3456-7890" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面のメールアドレス入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getMailAddressInputBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>メールアドレス</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="email" class="normal-input" type="email" value="' + (item.email ?? "") + '" placeholder="例）example@kyouseibin.com" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の緊急連絡先入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getEmergencyInputBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts', 'separate');
    elm.insertAdjacentHTML('beforeend', '<label class="cp12">緊急連絡先</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="emergency_contact_number" class="normal-input cp4" type="text" inputmode="numeric" pattern="^[0-9]+$" value=\'' + (item.emergency_contact_number ?? "") + '\' placeholder="012-3456-7890" onfocus="this.select();">');
    elm.insertAdjacentHTML('beforeend', '<input name="emergency_contact" class="normal-input ct5 cp8" type="text" value="' + (item.emergency_contact ?? "") + '" placeholder="例）実家・妻" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の手数料入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getCommissionInputBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>手数料(%)</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="commission" class="normal-input text-right" type="text" inputmode="numeric" pattern="\d*" value="' + item.commission + '" placeholder="%" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の時給入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getHourlyWageInputBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>時給(平日)</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="hourly_wage" class="normal-input text-right" type="text" inputmode="numeric" pattern="\d*" value="' + item.hourly_wage + '" placeholder="円" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の時給(週末)入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getWeekendHourlyWageInputBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>時給(土日)</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="weekend_hourly_wage" class="normal-input text-right" type="text" inputmode="numeric" pattern="\d*" value="' + item.weekend_hourly_wage + '" placeholder="円" onfocus="this.select();">');
    return elm;
}

/**
 * フォーム画面の交通費入力ボックスを作成する
 * @param {表示用のエンティティー} item 
 * @returns 
 */
function getTransCostInputBox(item) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>交通費(円)</label>');
    elm.insertAdjacentHTML('beforeend', '<input name="trans_cost" class="normal-input text-right" type="text" inputmode="numeric" pattern="\d*" value="' + item.trans_cost + '" placeholder="円" onfocus="this.select();">');
    return elm;
}

/*
 * フォーム画面のコンボボックスを作成する
 * @param {*} title 
 * @param {*} selectId 
 * @returns 
 */
function getComboBox(title, select) {
    const elm = document.createElement('div');
    elm.classList.add('form-dialog-parts');
    elm.insertAdjacentHTML('beforeend', '<label>' + title + '</label>');
    elm.insertAdjacentElement('beforeend', select);
    return elm;
}