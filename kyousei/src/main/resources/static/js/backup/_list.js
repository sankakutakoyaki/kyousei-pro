
/*******************************************************************************************************  */


// /******************************************************************************************************* データ操作 */
// /**
//  * 指定したIDでSimpleDataリストを取得
//  * @param {*} id 
//  * @param {*} url 
//  * @returns 
//  */
// async function getSimpleDatalistById(id, url) {
//     const data = "id=" + encodeURIComponent(id);
//     const contentType = 'application/x-www-form-urlencoded';
//     const result = await postFetch(url, data, token, contentType);
//     return await result.json();
// }

/******************************************************************************************************* アコーディオンメニュー作成 */

// async function setSidebarAccordionList(parents, childs) {
//     if (parents == null) return;
//     const parent = document.getElementById('sidebar-area');

//     parent.classList.add('sidebar-list');
//     const elmUl = document.createElement('ul');

//     parents.forEach(elm => {
//         let accordionElm = document.createElement('details');
//         accordionElm.setAttribute('name', 'parentAccordion')
//         accordionElm.insertAdjacentHTML('beforeend', '<input type="hidden" value="' + elm.id + '">');
//         let summaryElm = document.createElement('summary');
//         let summaryText = document.createTextNode(elm.name);
//         summaryElm.appendChild(summaryText);
//         accordionElm.insertAdjacentElement('beforeend', summaryElm);

//         let items = childs.filter(item => {
//             if (item.parent_name == elm.name) {
//                 return item;
//             }
//         })

//         let topElm = document.createElement('li');
//         let topSpan = document.createElement('span');
//         topSpan.setAttribute('onclick', 'changeAccordionEntityList(0, this)');
//         let topText = document.createTextNode('全て表示');
//         topSpan.appendChild(topText);
//         topElm.insertAdjacentHTML('beforeend', '<input type="hidden" value="0">');
//         topElm.insertAdjacentElement('beforeend', topSpan);
//         accordionElm.insertAdjacentElement('beforeend', topElm);

//         items.forEach(item => {
//             let accordionContent = document.createElement('li');
//             let accordionSpan = document.createElement('span');
//             accordionSpan.setAttribute('onclick', 'changeAccordionEntityList(' + item.child_id + ', this)');
//             let accordionText = document.createTextNode(item.child_name);
//             accordionSpan.appendChild(accordionText);
//             accordionContent.insertAdjacentHTML('beforeend', '<input type="hidden" value="' + item.child_id + '">');
//             accordionContent.insertAdjacentElement('beforeend', accordionSpan);
//             accordionElm.insertAdjacentElement('beforeend', accordionContent);
//         });
//         elmUl.insertAdjacentElement('beforeend', accordionElm);
//     });
//     parent.insertAdjacentElement('beforeend', elmUl);
// }

// // 選択された時のリスト更新処理
// async function changeEntityList(id, obj) {
//     const searchText = document.getElementById('search-text');
//     if (searchText != null) {
//         searchText.value = "";
//     }

//     const data = "id=" + encodeURIComponent(id);
//     const contentType = 'application/x-www-form-urlencoded';
//     await postFetch(selectUrl, data, token, contentType);

//     await sidebarItemUnSelect();
//     obj.classList.add('selected');
//     await updateDisplay();
// }

// // 選択された時のリスト更新処理
// async function pushAccordionParent(id) {
//     const data = "id=" + encodeURIComponent(id);
//     const contentType = 'application/x-www-form-urlencoded';
//     await postFetch(selectParentUrl, data, token, contentType);
// }

// // 選択された時のリスト更新処理
// async function changeAccordionEntityList(id, obj) {
//     const searchText = document.getElementById('search-text');
//     if (searchText != null) {
//         searchText.value = "";
//     }

//     const selectInput = document.querySelector('details[open] > input');
//     await pushAccordionParent(selectInput.value);

//     const data = "id=" + encodeURIComponent(id);
//     const contentType = 'application/x-www-form-urlencoded';
//     await postFetch(selectUrl, data, token, contentType);

//     await sidebarAccordionItemUnSelect();
//     obj.classList.add('selected');
//     await updateDisplay();
// }

// // アコーディオンパネルのSELECT状態を全て解除する
// async function sidebarAccordionItemUnSelect() {
//     let sidemenuItems = document.querySelectorAll('details > li > span');
//     sidemenuItems.forEach(function (item) {
//         if (item.classList.contains('selected')) {
//             item.classList.remove('selected');
//         }
//     })
//     hamburgerClose();
// }

// // 引数によって処理分岐　accordionは選択されていないと不可　selectは全て表示以外を選択しないと不可　listは選択してなくても可
// async function execCreateItemConditionsSidebarSelected(type) {
//     if (type == 'accordion') {
//         const accordionElm = document.querySelector('details[open]');
//         const selectSpan = document.querySelector('details > li > span.selected');
//         if (accordionElm == null || selectSpan == null) {
//             const msg = '選択されていません';
//             const btnText = '了解です';
//             const closeId = 'dialog-area';
//             const funcName = 'afterAddSidebarItemFunc(event)';
//             const isConfirm = false;
//             const headerColor = 'red';
//             createMessageDialog(msg, btnText, closeId, funcName, isConfirm, headerColor);
//         } else {
//             await formDisplay(0, true);
//         }
//     } else if (type == 'list') {
//         await formDisplay(0, true);
//     } else {
//         throw new Exception("sidebar.jsでエラーです");
//     }
// }

// // ダイアログを画面更新なしで閉じる
// async function afterAddSidebarItemFunc(e) {
//     e.preventDefault();
//     closeDialog('dialog-area');
// }

// // 選択した項目を記憶させる
// async function setSidebarItem(id) {
//     const data = "id=" + encodeURIComponent(id);
//     const contentType = 'application/x-www-form-urlencoded';
//     const result = await postFetch(selectUrl, data, token, contentType);
//     return await result.json();
// }

// // IDを指定してリストを取得
// async function getSidebarItemListById(url) {
//     const contentType = 'application/json';
//     const result = await fetch(url);
//     return await result.json();
// }

