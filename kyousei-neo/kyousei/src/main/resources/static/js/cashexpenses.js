"use strict"

// -------------------------------------------------------------------------------------------------------------------------------------- 支払い画面
    // 現金経費支払いフォーム画面作成
    function createCashExpensesFormDialog() {
        // ヘッダー
        const elmHeader = getFormHeader("現金経費支払い");
        // コンテント
        const cash = getCashEntity();
        const elmContent = getCashExpensesFormContent(cash);
        // フッター
        const elmFooter = getCashExpensesFormFooter("支払い")
        // ダイアログ作成
        const elmForm = document.createElement('div');
        elmForm.classList.add('form-dialog');
        elmForm.insertAdjacentElement('beforeend', elmHeader);
        elmForm.insertAdjacentElement('beforeend', elmContent);
        elmForm.insertAdjacentElement('beforeend', elmFooter);
        elmForm.querySelector('input[name="payment-employee-code"]').addEventListener('blur', function(e) {
            if (e.target.value == "") {
                const name = document.querySelector('input[name="payment-employee-name"]');
                name.value = "";
                return;
            }
            searchForNameByCode(e);
        });
        elmForm.querySelector('input[name="payment-employee-code"]').addEventListener('keydown', function (e) {
            if (e.target.value == "") {
                const name = document.querySelector('input[name="payment-employee-name"]');
                name.value = "";
                return;
            }
            if(e.key === 'Enter'){
                e.preventDefault();
                searchForNameByCode(e);
            }
        });
        const parent = document.getElementById('form-dialog-area');
        const elm = document.createElement('div');
        elm.classList.add('dialog');
        parent.insertAdjacentElement('beforeend', elm);
        parent.insertAdjacentElement('beforeend', elmForm);
        const code = parent.querySelector('input[name="payment-employee-code"]');
        if (code != null) code.focus();
    }
    // 現金経費支払いフォーム画面のコンテント部分作成
    function getCashExpensesFormContent(item) {
        const elmContent = document.createElement('form');
        elmContent.classList.add('dialog-content', 'cash-expenses');
        elmContent.setAttribute('id', 'cash-expenses-form');
        // DB登録用隠しアイテム
        elmContent.insertAdjacentHTML('beforeend', '<input name="payment-cash-expenses-id" type="hidden" value="0">');
        elmContent.insertAdjacentHTML('beforeend', '<input name="payment-employee-id" type="hidden" value="0">');
        elmContent.insertAdjacentHTML('beforeend', '<input name="payment-pay-state" type="hidden" value="0">');
        elmContent.insertAdjacentHTML('beforeend', '<input name="payment-version" type="hidden" value="0">');
        // 金庫選択ボックス
        elmContent.insertAdjacentHTML('beforeend', '<div class="form-parts w60"><label>金庫</label><select name="bank" class="normal-select"></select></div>');
        const bankArea = elmContent.querySelector('select[name="bank"]');
        createComboBox(bankArea, banklist);
        // 支払い担当者
        elmContent.insertAdjacentHTML('beforeend', '<div class="input-area"><div class="form-parts w40"><label>コード</label><input name="payment-employee-code" class="normal-input" type="text" inputmode="numeric" pattern="\d*" onfocus="this.select();"></div>' +
                                                    '<div class="form-parts"><label>支払い者</label><input name="payment-employee-name" class="normal-input" type="text" disabled></div></div>');
        // 出金金額
        elmContent.insertAdjacentHTML('beforeend', '<div class="form-parts w30"><label>出金額</label><input name="total-payment" class="normal-input text-right" type="text" value="' + item.payment + '" disabled"></div>');

        return elmContent;
    }
    // 現金経費支払いフォーム画面のフッター部分作成
    function getCashExpensesFormFooter(btnText) {
        const elmFooter = document.createElement('div');
        elmFooter.classList.add('dialog-footer');
        elmFooter.insertAdjacentHTML('beforeend', '<button class="normal-btn frameless" type="button" onclick="closeDialog(\'form-dialog-area\', event)"><span>キャンセル</span></button>');
        elmFooter.insertAdjacentHTML('beforeend', '<button id="payment_btn" class="normal-btn ok" type="button" onclick="saveCashExpensesFormData()"><span>' + btnText + '</span></button>');
        return elmFooter;
    }
    // コードから[employee]を取得して、名前を表示
    async function searchForNameByCode(e) {
        e.preventDefault();
        const id = document.querySelector('input[name="payment-employee-id"]');
        const code = document.querySelector('input[name="payment-employee-code"]');
        const name = document.querySelector('input[name="payment-employee-name"]');
        const btn = document.getElementById('payment_btn');
        if (id == null || code == null || name == null || btn == null) return;
        if (code.value == "" || isNaN(code.value)) {
            id.value = "";
            name.value = "";
            return;
        }
        // [id=code]に入力されたコードから[employee]を取得して[payment_employee_name]に入力する
        const data = "code=" + encodeURIComponent(parseInt(code.value));
        const result = await postFetch('/employee/code', data, token, 'application/x-www-form-urlencoded');
        const entity = await result.json();
        if (entity != null && entity.employee_id > 0) {
            id.value = entity.employee_id;
            name.value = entity.full_name;
            btn.focus();
            return;
        } else {
            id.value = "";
            code.value = ""
            name.value = "";
            messageDialog("コードが登録されていません", 'エラー', 'red');
            return;
        }
    }
    // 入力チェック
    function execCashExpensesFormDataCheck() {
        let msg = "";
        const code = document.querySelector('input[name="payment-employee-code"]');
        if (code == null || code.value == "" || code.value == "0") {
            msg += '\nコードが入力されていません';
        }
        const pay = document.querySelector('input[name="total-payment"]');
        if (pay == null || pay.value == "" || pay.value == "0") {
            msg += '\n出金額が入力されていません';
        }
        // エラーが一つ以上あればエラーメッセージダイアログを表示する
        if (msg != "") {
            messageDialog(msg, 'エラー', 'red');
            return false;
        }
        return true;
    }
