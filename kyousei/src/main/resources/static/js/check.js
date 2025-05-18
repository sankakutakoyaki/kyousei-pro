
/*******************************************************************************************************  */
/**
 * 全てチェックボックス押下時
 * @param {全て選択ボタン自身} allchk 
 */
function clickAllCheckBtn(allchk) {
    const parentId = allchk.closest('table');
    const row = document.getElementById('row0');
    if (allchk.checked == true) {
        if (!row.classList.contains('selected')) {
            row.classList.add('selected');
        }
        addClassToAllElementsOfSameName(parentId, 'data-row', 'selected')
    } else {
        if (row.classList.contains('selected')) {
            row.classList.remove('selected');
        }
        removeClassToAllElementsOfSameName(parentId, 'data-row', 'selected')
    }
    const allchks = parentId.querySelectorAll('input[name="chk-box"]');
    if (allchks != null) {
        allchks.forEach((chk) => {
            chk.checked = allchk.checked;
        });
    }
}

/**
 * チェックボックスの選択を全て解除する
 * @param {全て選択ボタンのID} allchkId 
 */
function deselectAllCheckBtn(allchkId) {
    const allchk = document.getElementById(allchkId);
    allchk.checked = false;
    clickAllCheckBtn(allchk);
}

/**
 * チェックボックス押下時 
 * @param {選択アイテムID} id 
 * @param {チェックボックス自身} self 
 */
function clickCheckBtn(id, self) {
    const parentId = self.closest('table');
    /* セレクトクラスを付与もしくは剥奪する */
    const row = document.getElementById('row' + id);
    const chk = document.getElementById('chk' + id);
    addSelectClassToRow(row, chk.checked)
    /* 全て選択されていれば[all-chk-btn]をチェック状態にする、全て選択されていなければ[all-chk-btn]を非選択状態にする */
    const result = isEverythingChecked(parentId);
    const allchkbtn = document.getElementById('all-chk-btn');
    allchkbtn.checked = result;    
    /* セレクトクラスを付与もしくは剥奪する */
    const header = document.getElementById('row0');
    addSelectClassToRow(header, result)
}

/**
 * 選択された行(row)に、状態(state)に応じてセレクトクラスを付与もしくは剥奪する
 * @param {選択された行} row 
 * @param {状態} state 
 */
function addSelectClassToRow(row, state) {
    if (state == true) {
        if (!row.classList.contains('selected')) {
            row.classList.add('selected');
        }
    } else {
        if (row.classList.contains('selected')) {
            row.classList.remove('selected');
        }
    }
}

/**
 * 全てチェックされているか確認する
 * @param {確認対象の親要素ID} parentId
 * @returns 全て選択されていれば[True]、一つでも選択されていなければ[False]を返す
 */
function isEverythingChecked(parentId) {
    const allchks = parentId.querySelectorAll('input[name="chk-box"]');
    let result = true;
    if (allchks != null) {
        for (let i = 0; i < allchks.length; i++) {
            if (allchks[i].checked == false) {
                result = false;
                break;
            }
        }
    }
    return result;
}

/**
 * 一つ以上チェックされているか確認する
 * @param {確認対象の親要素ID} parentId
 * @returns 一つ以上選択されていれば[True]、一つも選択されていなければ[False]を返す
 */
function isAnyOneChecked(parentId) {
    const allchks = parentId.querySelectorAll('input[name="chk-box"]');
    let result = false;
    if (allchks != null) {
        for (let i = 0; i < allchks.length; i++) {
            if (allchks[i].checked == true) {
                result = true;
                break;
            }
        }
    }
    return result;
}

/**
 * 選択されているチェックボックスを全て取得する
 * @param {取得対象の親要素} parent 
 * @returns チェックされているIDのリスト
 */
function getAllSelectedCheckboxes(parent) {
    const ids = parent.querySelectorAll('input[name="chk-box"]:checked');
    const checked_data = [];
    if (0 < ids.length) {
        for (let data of ids) {
            let num = parseInt(data.value);
            checked_data.push({ 'id': num });
        }
    }
    return checked_data;
}

/**
 * 選択されたIDで検索条件を作成
 * @param {*} self 
 * @returns 
 */
function getSqlStringFromSearchCriteriaByIds(ids, str) {
    if (ids.length > 0) {
        ids.forEach(function (item) {
            str += item.id + ", ";
        });
        str = str.slice(0, str.length - 2);
        str += ")";
        return str;
    } else {
        return null;
    }
}