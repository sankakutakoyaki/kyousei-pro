
/******************************************************************************************************* カテゴリー登録リスト */
/**
 * リスト作成
 * @param {アイテムのリスト} items 
 * @param {リストを挿入する場所} area 
 * @returns 
 */
function createCategoryRegistList(items, area) {
    if (area == null) return;
    if (items == null) return;

    area.classList.add('normal-list','scroll-area');
    const elmUl = document.createElement('ul');

    items.forEach(function (item) {
        if (item.state != deleteStateCode) {
            let elm = document.createElement('li');
            elm.onclick = e => changeCategoryRegistItems(e);
            elm.setAttribute('id', 'category-' + item.number)
            elm.setAttribute('data-code', item.code);
            elm.setAttribute('data-number', item.number);
            elm.setAttribute('data-state', item.state);

            elm.setAttribute('draggable', 'true');
            elm.addEventListener("dragstart", onDragStart);
            elm.addEventListener("drop", onDrop);
            elm.addEventListener("dragover", onDragover);
            elm.addEventListener("dragenter", onDragenter);
            elm.addEventListener("dragleave", onDragleave);

            elm.innerHTML = item.text;
            elmUl.insertAdjacentElement('beforeend', elm);
        }
    });
    area.insertAdjacentElement('beforeend', elmUl);
}

/**
 * ドラッグ処理
 * @param {Event} e
 */
function onDragStart(e) {
    const listNumber = getCategoryRegistListNumber(e.currentTarget);
    const item = {"listNumber":listNumber, "number":e.currentTarget.dataset.number};
    const jsonData = JSON.stringify(item);
    e.dataTransfer.setData("application/json", jsonData);
}

/**
 * ドロップ処理
 * @param {Event} e
 */
function onDrop(e) {
    e.currentTarget.classList.remove("dragging");

    const draggedElementJson = JSON.parse(e.dataTransfer.getData('application/json'));
    const draggedElementListNumber = Number(draggedElementJson.listNumber);
    const listNumber = getCategoryRegistListNumber(e.currentTarget);
    if (draggedElementListNumber != listNumber) return;

    const draggedElementNumber = draggedElementJson.number;
    const draggedElement = document.getElementById('category-' + draggedElementNumber);

    const listElements = [...e.target.parentNode.children];
    const targetIndex = listElements.indexOf(e.target);
    const draggedElementIndex = listElements.indexOf(draggedElement);

    const insertionPoint = draggedElementIndex < targetIndex ? e.target.nextSibling : e.target;
    e.target.parentNode.insertBefore(draggedElement, insertionPoint);
    const parent = e.target.closest('[name="simple-list"]');
    rewriteAllStatusCodesInList(parent);
}

/**
 * 操作が要素上に入ってきたとき
 * @param {Event} event 
 */
function onDragenter(event) {
    event.currentTarget.classList.toggle("dragging");
}

/**
 * 操作が要素上から出たとき
 * @param {Event} event 
 */
function onDragleave(event) {
    event.currentTarget.classList.toggle("dragging");
}

/**
 * 操作が要素上を通過してるとき
 * @param {Event} event 
 */
function onDragover(event) {
    event.preventDefault();
}

/**
 * リストにアイテムを追加する
 * @param {*} elm 
 * @param {*} listAreaName 
 * @param {*} parentAreaName 
 */
function addCategoryRegistItem(elm) {
    if (elm == null) return;

    const parent = elm.closest('[name="simple-list"]');
    if (parent == null) return;

    const inputElm = parent.querySelector('input');
    if (inputElm == null) return;
    if (inputElm.value == "") return;
    inputElm.value = checkDuplicatesCategoryRegistItemAddAfterNumber(parent);

    // リストにアイテムを追加する
    createCategoryRegistListElement(parent);
    // 追加したリスト以降のリストを消去する
    deleteCategoryRegistListElements(parent);
    // 選択を解除する
    clearAllSelectInList(parent);
}

/**
 * 
 * @param {*} parent 
 * @returns 
 */
function createCategoryRegistListElement(parent) {
    if (parent == null) return;


    const listElm = parent.querySelector('ul');
    if (listElm == null) return;

    const inputElm = parent.querySelector('input');
    const listItems = parent.querySelectorAll('li');
    const maxId = getCategoryRegistMaxId();

    const newElm = document.createElement('li');
    newElm.onclick = e => changeCategoryRegistItems(e);    
    newElm.setAttribute('id', 'category-' + (maxId + 1))
    newElm.setAttribute('data-code', listItems.length + 1);
    newElm.setAttribute('data-number', maxId + 1);
    newElm.setAttribute('data-state', updateStateCode);

    newElm.setAttribute('draggable', 'true');
    newElm.addEventListener("dragstart", onDragStart);
    newElm.addEventListener("drop", onDrop);
    newElm.addEventListener("dragover", onDragover);
    newElm.addEventListener("dragenter", onDragenter);
    newElm.addEventListener("dragleave", onDragleave);
    newElm.innerHTML = inputElm.value;
    listElm.insertAdjacentElement('beforeend', newElm);

    // listにアイテムを追加する
    addCategoryRegistItemToList(parent);

    inputElm.value = "";
}

/**
 * 選択したアイテムを更新する　同じ名前の場合はリネームする
 * @param {*} elm 
 * @returns 
 */
function updateCategoryRegistItem(elm) {
    if (elm == null) return;

    const parent = elm.closest('[name="simple-list"]');
    // 同じ名前の場合はリネームする
    const result = checkDuplicatesCategoryRegistItem(parent);
    if (result == false) return;

    const selectElm = parent.querySelector('li.selected');
    if (selectElm == null) return;
    selectElm.dataset.state = updateStateCode;

    const inputElm = parent.querySelector('input');
    selectElm.textContent = inputElm.value;

    const selectId = selectElm.dataset.number;
    rewriteCategoryNameFromList(selectId, inputElm.value);

    // listのアイテムを変更する
    updateCategoryRegistItemToList(selectElm);
}

/**
 * 選択したアイテムをリストから消去して、listのアイテムステータスを変更する
 * @param {*} elm 
 * @returns 
 */
function deleteCategoryRegistItem(elm) {
    if (elm == null) return;

    const parent = elm.closest('[name="simple-list"]');
    const selectElm = parent.querySelector('li.selected');
    if (selectElm == null) return;

    // 選択したアイテムを削除
    const selectId = selectElm.dataset.number;
    deleteFullElements(selectElm);

    // listのアイテムステータスコードを変更する
    deleteCategoryRegistItemFromList(selectId);
    // listのアイテムステータスコードを[新規]と[削除]以外を全て[更新]にする
    rewriteAllStatusCodesInList(parent);

    parent.querySelector('input').value = "";

    // 選択したリスト以降のリストを消去する
    deleteCategoryRegistListElements(parent);

}


/**
 * 選択された時のリスト更新処理
 * @param {*} e 
 * @param {*} listAreaName 
 * @param {*} parentAreaName 
 * @returns 
 */
function changeCategoryRegistItems(e) {
    const elm = e.currentTarget;
    if (elm == null) return;
    const area = elm.closest('[name="categorylist-area"]');

    // 全ての選択を解除する
    clearAllSelectInList(area);
    // クリックしたアイテムを選択状態にする
    elm.classList.add('selected');

    // 選択アイテムをインプットボックスにコピーする
    const selectId = elm.dataset.number;
    const item = getSelectedItemCategoryRegistFromList(selectId);
    if (item != null) {
        // リストエリア取得
        const listArea = area.closest('[name="simple-list"]');
        if (listArea == null) return;
        const inputElm = listArea.querySelector('input');
        if (inputElm != null) {
            inputElm.dataset.selectnumber = selectId;
            inputElm.value = item.category_name;
        }
    }

    // 選択したリスト以降のリストを消去する
    deleteCategoryRegistListElements(elm);
    // リストを作成する
    createCategoryRegistListElements(elm);
}

/**
 * 全てのリストを消去する
 */
function deleteAllCategoryRegistListElements() {
    const parentArea = document.getElementById('categoryregist-area1');
    // 全てのリストエリアを取得
    const parentElms = parentArea?.querySelectorAll('[name="simple-list"]');
    if (parentElms == null) return;
    parentElms.forEach(function (parentElm) {
        let childElm = parentElm.querySelector('[name="categorylist-area"]');
        let inputElm = parentElm.querySelector('input');
        if (inputElm != null) inputElm.value = "";
        deleteElements(childElm);
    });
}

/**
 * 選択したリスト以降のリストを消去する
 * @param {*} elm
 */
function deleteCategoryRegistListElements(elm) {
    if (elm == null) return;

    const parentArea = elm.closest('[name="categoryregist-area"]');
    // 全てのリストエリアを取得
    const parentElms = parentArea?.querySelectorAll('[name="simple-list"] ~ [name="simple-list"]');
    if (parentElms == null) return;
    const listNumber = getCategoryRegistListNumber(elm);
    parentElms.forEach(function (parentElm) {
        if (parentElm.dataset.listnumber > listNumber) {
            let childElm = parentElm.querySelector('[name="categorylist-area"]');
            let inputElm = parentElm.querySelector('input');
            if (inputElm != null) inputElm.value = "";
            deleteElements(childElm);
        }
    });
}

/**
 * リストを作成する
 * @param {*} parent 
 */
function createCategoryRegistListElements(elm) {
    if (elm == null) return;

    // リストデータ取得
    const categorylist = getCategoryRegistItems(elm);
    const listNumber = getCategoryRegistListNumber(elm);
    // 全てのリストエリアを取得
    const parentArea = elm.closest('[name="categoryregist-area"]');
    const parentElms = parentArea?.querySelectorAll('[name="simple-list"] ~ [name="simple-list"]');
    if (parentElms == null) return;
    parentElms.forEach(function (parentElm) {
        if (parentElm.dataset.listnumber == listNumber + 1) {
            let area = parentElm.querySelector('[name="categorylist-area"]');
            createCategoryRegistList(categorylist, area);
        }
    });
}

/**
 * 
 * @returns 
 */
function getCategoryClassId() {
    const header = document.getElementById('categoryregist-header');
    const select = header.querySelector('select');
    return select == null ? 0 : select.value;
}

/**
 * 
 * @param {*} number 
 * @returns 
 */
function createCategoryRegistContent(number) {
    const elm = document.createElement('div');
    elm.setAttribute('name', 'simple-list');
    elm.setAttribute('data-listnumber', number);
    elm.classList.add('categoryregist-list');

    const inputElm = document.createElement('div');
    inputElm.classList.add('categoryregist-input');

    const inputItem = document.createElement('input');
    inputItem.setAttribute('name', 'category-text');
    inputItem.setAttribute('data-selectnumber', '0')
    inputItem.classList.add('normal-input');

    const buttonAddElm = document.createElement('div');
    buttonAddElm.classList.add('img-btn', 'pc-style');
    buttonAddElm.onclick = function () { addCategoryRegistItem(this); }
    const imgAddElm = document.createElement('img');
    imgAddElm.setAttribute('title', '追加');
    imgAddElm.setAttribute('src', '/icons/add.png');
    buttonAddElm.insertAdjacentElement('beforeend', imgAddElm);

    const buttonUpElm = document.createElement('div');
    buttonUpElm.classList.add('img-btn', 'pc-style');
    buttonUpElm.onclick = function () { updateCategoryRegistItem(this); }
    const imgUpElm = document.createElement('img');
    imgUpElm.setAttribute('title', '更新');
    imgUpElm.setAttribute('src', '/icons/update.png');
    buttonUpElm.insertAdjacentElement('beforeend', imgUpElm);

    const buttonCopyElm = document.createElement('div');
    buttonCopyElm.classList.add('img-btn', 'pc-style');
    buttonCopyElm.onclick = function () { copyCategoryRegistItem(this) };
    const imgCopyElm = document.createElement('img');
    imgCopyElm.setAttribute('title', 'コピー');
    imgCopyElm.setAttribute('src', '/icons/copy.png');
    buttonCopyElm.insertAdjacentElement('beforeend', imgCopyElm);

    const buttonDelElm = document.createElement('div');
    buttonDelElm.classList.add('img-btn', 'pc-style');
    buttonDelElm.onclick = function () { deleteCategoryRegistItem(this) };
    const imgDelElm = document.createElement('img');
    imgDelElm.setAttribute('title', '削除');
    imgDelElm.setAttribute('src', '/icons/dust.png');
    buttonDelElm.insertAdjacentElement('beforeend', imgDelElm);

    const buttonElms = document.createElement('div');
    buttonElms.classList.add('img-btns', 'pc-style');
    buttonElms.insertAdjacentElement('beforeend', buttonAddElm);
    buttonElms.insertAdjacentElement('beforeend', buttonUpElm);
    buttonElms.insertAdjacentElement('beforeend', buttonCopyElm);
    buttonElms.insertAdjacentElement('beforeend', buttonDelElm);

    const areaElm = document.createElement('div');
    areaElm.setAttribute('name', 'categorylist-area');

    elm.insertAdjacentElement('beforeend', buttonElms);
    elm.insertAdjacentElement('beforeend', inputItem);    
    elm.insertAdjacentElement('beforeend', areaElm);

    return elm;
}

/**
 * テキストが重複してるかチェックする
 * @param {*} text 
 * @returns 重複していれば、数字の添字をつける
 */
function checkDuplicatesCategoryRegistItemAddAfterNumber(parent) {
    const inputElm = parent.querySelector('input');
    const listItems = parent.querySelectorAll('li');
    let returnText = inputElm.value;
    let i = 1;
    listItems.forEach(function (item) {
        if (item.textContent == returnText) {
            i++;
            returnText = inputElm.value + '-' + i;
        }
    });
    return returnText;
}


/**
 * テキストが重複してるかチェックする
 * @param {*} text 
 * @returns 重複していれば、Falseを返す
 */
function checkDuplicatesCategoryRegistItem(parent) {
    const inputElm = parent.querySelector('input');
    const listItems = parent.querySelectorAll('li');
    let result = true;
    listItems.forEach(function (item) {
        if (item.textContent == inputElm.textContent) result = false;
    });
    return result;
}

/**
 * リスト番号取得
 * @param {*} elm 
 * @returns 
 */
function getCategoryRegistListNumber(elm) {
    // リストエリア取得        
    const listArea = elm.closest('[name="simple-list"]');
    if (listArea == null) return;
    // リスト番号取得
    return Number(listArea.dataset.listnumber);
}

/**
 * 保存時の処理
 * @param {*} url 
 */
async function saveCategoryRegist(postUrl, getUrl) {
    const spinner = document.getElementById('loading');
    if (spinner != null) {
        spinner.classList.remove('loaded');
    }

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

    if (spinner != null) {
        spinner.classList.add('loaded');
    }
}

/**
 * リセット時の処理
 */
function execResetCategoryRegist() {
    const spinner = document.getElementById('loading');
    if (spinner != null) {
        spinner.classList.remove('loaded');
    }

    list = structuredClone(origin);
    deleteAllCategoryRegistListElements();

    const header = document.getElementById('categoryregist-header');
    const select = header.querySelector('select');
    if (select != null) {
        select.selectedIndex = -1;
    }

    if (spinner != null) {
        spinner.classList.add('loaded');
    }
}

/**
 * カテゴリーコンボボックス選択時の処理
 * @returns 
 */
function changeItemCategoryClassSelected() {
    deleteAllCategoryRegistListElements();

    const classId = getCategoryClassId();
    if (classId == 0) return;
    const categorylist = getCategorylist(classId);console.log(categorylist)
    categorylist.sort((a, b) => a.code > b.code ? 1 : -1);

    const parentArea = document.getElementById('categoryregist-area1');
    const area = parentArea.querySelector('[name="categorylist-area"]');
    createCategoryRegistList(categorylist, area);
}

/**
 * listから選択したアイテムのテキストを書き換える
 * @param {*} selectId 
 * @param {*} text 
 */
function rewriteCategoryNameFromList(selectId, text) {
    const selectItem = getSelectedItemCategoryRegistFromList(selectId);
    if (selectItem != null) {
        selectItem.category_name = text;
    }
}

/**
 * listから選択したアイテムのステータスコードをdeleteCodeに変更する
 * @param {*} selectId 
 */
function deleteCategoryRegistItemFromList(selectId) {
    const items = getSelectedAllItemCategoryRegistFromList(selectId);
    items.forEach(function (item) {
        item.state = deleteStateCode;
    });    
}

/**
 * 
 * @param {*} parent 
 */
function rewriteAllStatusCodesInList(parent){
    const newListElements = parent.querySelectorAll('li');
    let i = 0;
    newListElements.forEach(function (listElm) {
        i++;
        listElm.dataset.code = i;
        if (listElm.dataset.state != createStateCode) {
            listElm.dataset.state = updateStateCode;
        }                    
        let id = listElm.dataset.number;

        let elm = getSelectedItemCategoryRegistFromList(id)
        if (elm.state != deleteStateCode) {
            elm.code = i;
            if (elm.state == createStateCode) {
                elm.state = createStateCode;
            } else {
                elm.state = updateStateCode;
            }
        } else {
            elm.code = 0;
        }
    });
}