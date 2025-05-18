
/******************************************************************************************************* リスト操作 */
/**
 * リストの先頭に全て表示を追加する
 * @param {先頭に追加させるアイテムリスト} items 
 * @returns アイテムリスト
 */
function addAllSelectionsToTopOfList(items) {
    const item = { number: 0, text: "全て表示" };
    items.unshift(item);
    return items;
}

/**
 * アイテムをクリックする
 * @param {クリックするアイテム} obj 
 */
function clickListItem(obj) {
    obj.click();
}

/**
 * SELECT状態を全て解除する
 * @param {*} elm 
 * @returns 
 */
function clearAllSelectInList(elm) {
    if (elm == null) return;
    const sidemenuItems = elm.querySelectorAll('li');
    if (sidemenuItems == null) return;
    sidemenuItems.forEach(function (item) {
        if (item.classList.contains('selected')) {
            item.classList.remove('selected');
        }
    })
}

/**
 * テキストが重複してるかチェックする
 * @param {*} text 
 * @returns 重複していれば、数字の添字をつける
 */
function checkDuplicatesTextAddAfterNumber(parent) {
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
function checkDuplicatesText(parent) {
    const inputElm = parent.querySelector('input');
    const listItems = parent.querySelectorAll('li');
    let result = true;
    listItems.forEach(function (item) {
        if (item.textContent == inputElm.textContent) result = false;
    });
    return result;
}

/******************************************************************************************************* 登録リスト */
/**
 * リスト作成
 * @param {アイテムのリスト} items 
 * @param {リストを挿入する場所} area 
 * @returns 
 */
function createRegistList(items, area) {
    if (area == null) return;
    if (items == null) return;

    area.classList.add('normal-list','scroll-area');
    const elmUl = document.createElement('ul');

    items.forEach(function (item) {
        if (item.state != deleteStateCode) {
            let elm = document.createElement('li');
            elm.onclick = e => changeRegistListItem(e);
            elm.setAttribute('id', 'registlist-' + item.number)
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
    const item = {"number":e.currentTarget.dataset.number};
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

    const draggedElementNumber = draggedElementJson.number;
    const draggedElement = document.getElementById('registlist-' + draggedElementNumber);

    const listElements = [...e.target.parentNode.children];
    const targetIndex = listElements.indexOf(e.target);
    const draggedElementIndex = listElements.indexOf(draggedElement);

    const insertionPoint = draggedElementIndex < targetIndex ? e.target.nextSibling : e.target;
    e.target.parentNode.insertBefore(draggedElement, insertionPoint);
    const parent = e.target.closest('[name="registlist-area"]');
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

        let elm = getSelectedItemRegistList(id)
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