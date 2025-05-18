
/******************************************************************************************************* サイドバーリスト */

/**
 * サイドバー用リスト作成
 * @param {アイテムのリスト} items 
 * @param {リストエリア} area
 * @param {タイトル} title 
 * @param {クリック時の処理} funcName 
 * @param {テーブルID} tableId 
 */
function createSidebarList(items, area, title, funcName, tblName) {
    if (area == null) return;

    area.classList.add('normal-list', 'sidebar');
    const elmUl = document.createElement('ul');

    if (title != "") {
        const titleElm = document.createElement('p');
        const titleText = document.createTextNode(title);
        titleElm.appendChild(titleText);
        elmUl.insertAdjacentElement('beforeend', titleElm);
    }

    items.forEach(function (item) {
        let elm = document.createElement('li');
        elm.setAttribute('onclick', 'changeListWhenSelected(' + item.number + ', this, ' + funcName + ', "' + tblName + '", true)');
        elm.setAttribute('id', 'sidebar-item' + item.number);
        elm.insertAdjacentHTML('beforeend', '<input type="hidden" value="' + item.number + '">');
        if (item.number == sidebarSelectId) {
            elm.classList.add('selected');
        }
        elm.insertAdjacentHTML('beforeend', '<span>' + item.text + '</span>');
        elmUl.insertAdjacentElement('beforeend', elm);
    });
    area.insertAdjacentElement('beforeend', elmUl);
}

/**
 * 選択された時アイテムに基づいてテーブルリストを更新する
 * @param {テーブルを更新するID} id 
 * @param {クリックしたアイテム} obj 
 * @param {クリックした時の処理} funcName 
 * @param {更新対象のテーブルID} tableId 
 */
function changeListWhenSelected(id, obj, funcName, tbl, isTop) {
    // 検索ボックスがあれば初期化する
    const searchBox = document.getElementById('search-box');
    if (searchBox != null) {
        searchBox.value = "";
    }
    // 選択状態を解除する
    const elm = obj.closest('.normal-list');
    clearAllSelectInList(elm)
    hamburgerClose();
    
    // 自身を選択状態にする
    obj.classList.add('selected');
    // 選択内容に基づいて更新する
    funcName(id, tbl, isTop);
}

/******************************************************************************************************* ハンバーガーメニュー作成 */
/**
 * ハンバーガーメニュー作成
 */
const menuOpen = document.querySelector('#menu-open');
const menuClose = document.querySelector('#menu-close');
const menuPanel = document.querySelector('.normal-content');
const mainPanel = document.querySelector('#hamburger-area');

document.addEventListener('DOMContentLoaded', function () {
  if (menuOpen != null) {
    menuOpen.addEventListener('click', () => {
      menuPanel.classList.add('hamburger-open');
      menuPanel.classList.remove('hamburger-close');
      mainPanel.classList.add('dialog');
    });
  };
  if (menuClose != null) {
    menuClose.addEventListener('click', () => {
      menuPanel.classList.add('hamburger-close');
      menuPanel.classList.remove('hamburger-open');
      mainPanel.classList.remove('dialog');
    });
    hamburgerClose();
  }
  if (mainPanel != null) {
    mainPanel.addEventListener('click', () => {
      hamburgerClose();
    });
  };
});

/******************************************************************************************************* ハンバーガーメニューを閉じる（手動） */
/**
 * ハンバーガーメニューを手動で閉じる
 */
function hamburgerClose() {
  const menuClose = document.querySelector('#menu-close');
  menuClose.click();
}