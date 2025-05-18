
/******************************************************************************************************* コンボボックス */

/**
 * シンプルなコンボボックスを作成する
 * @param {*} selectArea 
 * @param {*} items SimpleData
 * @returns 
 */
function createSimpleComboBox(selectArea, items) {
    if (selectArea == null) return;
    items.forEach(function (item) {
        if (item.number > 0) {
            selectArea.insertAdjacentHTML('beforeend', '<option value="' + item.number + '">' + item.text + '</option>');
        }
    })
}

/**
 * シンプルなコンボボックスを作成する（先頭空白行）
 * @param {*} selectArea 
 * @param {*} items SimpleData
 * @returns 
 */
function createSimpleComboBoxWithTop(selectArea, items) {
    if (selectArea == null) return;
    selectArea.insertAdjacentHTML('beforeend', '<option value="0"></option>');
    items.forEach(function (item) {
        if (item.number > 0) {
            selectArea.insertAdjacentHTML('beforeend', '<option value="' + item.number + '">' + item.text + '</option>');
        }
    })
}

/**
 * コンボボックスを作成する
 * @param {List<SimpleData>} items 
 * @param {コンボボックスの名前} name 
 * @param {選択させるID} selectId 
 * @returns 
 */
function createComboBox(items, name, selectId, func) {
    const selectContent = document.createElement('select');
    selectContent.setAttribute('name', name);
    selectContent.classList.add('normal-select');
    if (func != null) {
        selectContent.onchange = function () { func(); }
    }
    return createComboBoxOptions(selectContent, items, selectId);
}

/**
 * 親コンボボックスの選択アイテムによって子コンボボックスの内容を変更する
 * @param {親コンボボックスの先祖要素ID} id 
 * @param {変更対象のコンボボックスのName} name 
 * @param {変更対象の親コンボボックス} parentName 
 * @param {データ取得用URL} url 
 */
function createComboBoxItems(id, name, parentName) {
    const parentItem = document.getElementById(id);
    if (parentItem == null) return;
    const selectItem = parentItem.querySelector('[name = "' + parentName + '"]');
    if (selectItem == null) return;
    const parentId = selectItem.value;
    deleteElementsWithSpecifiedName(parentItem, name);
    const selectContent = document.querySelector('[name = "' + name + '"]');
    // list = await getSimpleDatalistById(parentId, url);
    items = subcategorylist.filter(val => { return val.subnumber == parentId });
    createComboBoxOptions(selectContent, items, 0);
}

/**
 * コンボボックスのオプションを登録する
 * @param {リスト本体} selectContent 
 * @param {アイテムリスト} items 
 * @param {選択するアイテムのID} selectId 
 * @returns 
 */
function createComboBoxOptions(selectContent, items, selectId) {
    selectContent.insertAdjacentHTML('beforeend', '<option value="0"></option>');
    items.forEach(function (item) {
        if (item.number > 0) {
            selectContent.insertAdjacentHTML('beforeend', '<option value=' + item.number + ' ' + (item.number == selectId ? ' selected' : "") + '>' + item.text + '</option>');
        }
    })
    return selectContent;
}