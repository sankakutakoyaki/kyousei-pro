"use strict"

//#region /* WEB */
    /**
     * Post送信する
     * @param {アドレス} url 
     * @param {送信するデータ} data 
     * @param {トークン} token 
     * @param {コンテントタイプ} contentType 
     * @returns Promiseデータ
     */
    async function postFetch(url, data, token, contentType) {
        const spinner = document.getElementById('loading');
        if (spinner != null) {
            spinner.classList.remove('loaded');
        }
        try {
            return await fetch(url, {
                method: "POST",   // HTTP-Methodを指定する！
                headers: {  // リクエストヘッダを追加
                    'X-CSRF-TOKEN': token,
                    'Content-Type': contentType,
                },
                body: data,
            });
        } catch (e) {
            if (e = "TypeError: Failed to fetch") {
                alert("ネットワーク接続が異常です。");
                reloadPage();
            } else {
                alert(e);  // 例外（エラー）が発生した場合に実行
            }
        } finally {
            // 処理結果の成否に関わらず実行
            if (spinner != null) {
                spinner.classList.add('loaded');
            }
        }
    }
    // 公開鍵（VAPIDの公開鍵）をURLセーフなBase64エンコードに変換
    function urlBase64ToUint8Array(base64String) {
        // Base64文字列の末尾に必要な「=」を付加して、URLセーフに変換
        const padding = '='.repeat((4 - base64String.length % 4) % 4);
        const base64 = (base64String + padding)
            .replace(/\-/g, '+')
            .replace(/_/g, '/');

        // Base64をデコードしてUint8Arrayに変換
        const rawData = window.atob(base64);
        const outputArray = new Uint8Array(rawData.length);
        for (let i = 0; i < rawData.length; ++i) {
            outputArray[i] = rawData.charCodeAt(i);
        }
        return outputArray;
    }
    // async function subscribe() {
    //     const registration = await navigator.serviceWorker.register("/sw.js");
        
    //     const subscription = await registration.pushManager.subscribe({
    //         userVisibleOnly: true,
    //         applicationServerKey: "BDtSdJ-595-GLPukMIt7t9HGcfgWQWQsMcpKTPWUGqOdfo4vGG9pIM4jAyOulTGsrioOd2lcoHAY8cK90_ZETxA"
    //     });
    
    //     await fetch("/push/subscribe", {
    //         method: "POST",
    //         body: JSON.stringify(subscription),
    //         headers: { "Content-Type": "application/json" }
    //     });
    // }
    
    // async function sendNotification() {
    //     const subscription = await fetch("/push/subscribe").then(res => res.json());
        
    //     await fetch("/push/notify", {
    //         method: "POST",
    //         body: JSON.stringify(subscription),
    //         headers: { "Content-Type": "application/json" }
    //     });
    // }
    //#endregion
//#region /* ページ操作 */
    /**
     * リンク先へジャンプさせる
     * @param {リンク先} link 
     */
    function moveLinkPage(link) {
        location.href = link;
    };
    /**
    * リロードする
    */
    function reloadPage() {
        location.reload();
    }
    //#endregion
//#region /* エレメント操作 */
    /**
     * 指定したエレメント以下の要素を削除
     * @param {削除対象のエレメントもしくはエレメントのID} areaId
     */
    function deleteElements(areaId) {
        let item;
        if(areaId instanceof HTMLElement) {
            item = areaId;
        } else {
            item = document.getElementById(areaId);
        }
        // const item = document.getElementById(areaId);
        if (item == null) return;
        while (item.firstChild) {
            item.removeChild(item.firstChild);
        };
    }
    /**
    * 指定したエレメントを全て削除
    * @param {削除対象のエレメントもしくはエレメントのID} areaId
    */
    function deleteElementsAll(areaId) {
        let item;
        if(areaId instanceof HTMLElement) {
            item = areaId;
        } else {
            item = document.getElementById(areaId);
        }
        // const item = document.getElementById(areaId);
        if (item == null) return;
        while (item.firstChild) {
            item.removeChild(item.firstChild);
        };
        item.remove();
    }
    /**
     * 選択された要素(elm)に、状態(state)に応じてセレクトクラスを付与もしくは剥奪する
     * @param {選択された要素} elm 
     * @param {状態} state 
     */
    function addSelectClassToRow(elm, state) {
        if (state == true) {
            if (!elm.classList.contains('selected')) {
                elm.classList.add('selected');
            }
        } else {
            if (elm.classList.contains('selected')) {
                elm.classList.remove('selected');
            }
        }
    }
    //#endregion
//#region /* エレメント作成 */
    /**
     * コンボボックスを作成する
     * @param {*} selectArea 
     * @param {*} items SimpleData
     * @returns 
     */
    function createComboBox(selectArea, items) {
        if (selectArea == null) return;
        items.forEach(function (item) {
            if (item.number > -1) {
                selectArea.insertAdjacentHTML('beforeend', '<option value="' + item.number + '">' + item.text + '</option>');
            }
        })
    }
    function createComboBoxWithTop(selectArea, items) {
        if (selectArea == null) return;
        selectArea.insertAdjacentHTML('beforeend', '<option value="0"></option>');
        createComboBox(selectArea, items);
    }
    function setComboboxSelected(selectArea, id) {
        if (selectArea == null) return;
        const select = selectArea.querySelectorAll('option');
        if (select != null) {
            select.forEach(function (opt) {
                if (opt.value == id) opt.selected = true;
            })
        }
    }
    //#endregion
//#region /* 画面操作 */
    /**
     * 処理開始時の処理　スピナー表示
     */
    function startProcessing() {
        const body = document.querySelector('.normal-body');
        if (body == null) return;
        body.inert = true;

        const elm = document.querySelector('#scroll-area');
        if (elm == null) return;

        const spinner = document.querySelector('#loading');
        if (spinner == null) {
            elm.insertAdjacentHTML('beforeend', '<div id="loading"><div class="spinner"></div></div>');
        } else {
            spinner.classList.remove('loaded');
        }
    }
    /**
    * 処理終了時の処理 スピナーを消す
    */
    function processingEnd() {
        const elm = document.querySelector('#scroll-area');
        if (elm == null) return;

        const spinner = document.querySelector('#loading');
        if (spinner == null) return;
        deleteElementsAll("loading");
        // spinner.classList.add('loaded');
        // if (elm != null) elm.inert = false;

        const body = document.querySelector('.normal-body');
        if (body != null) body.inert = false;
    }
    //#endregion
//#region /* サイドバー */
    /**
     * 
     * @returns 
     */
    function getHomeParentlist() {
        const parents = [
            { "id": -1, "text": "ホーム", "img": "/icons/home.png", "func": "onclick=\"moveLinkPage('/')\"" },
            { "id": 1, "text": "営業", "img": "/icons/sales.png", "func": "onclick=\"moveLinkPage('/sales')\"" },
            { "id": 2, "text": "人事・労務", "img": "/icons/person.png", "func": "onclick=\"moveLinkPage('/personnel')\"" },
            { "id": 3, "text": "経理", "img": "/icons/piggy_bank.png", "func": "onclick=\"moveLinkPage('/piggy_bank')\"" },
            { "id": 4, "text": "管理", "img": "/icons/pc_desk.png", "func": "onclick=\"moveLinkPage('/management')\"" },
            { "id": 5, "text": "登録", "img": "/icons/regist.png", "func": "onclick=\"moveLinkPage('/regist')\"" },
            { "id": 6, "text": "リサイクル", "img": "/icons/recycle.png", "func": "onclick=\"moveLinkPage('/recycle')\"" },
            { "id": 7, "text": "お知らせ", "img": "/icons/info.png", "func": "onclick=\"moveLinkPage('/info')\"" },
            { "id": 8, "text": "設定", "img": "/icons/setting.png", "func": "onclick=\"moveLinkPage('/')\"" },
        ]
        return parents;
    }
    /**
     * 
     * @returns 
     */
    function getParentlist() {
        const parents = [
            { "id": -1, "text": "ホーム", "img": "/icons/home.png", "func": "onclick=\"moveLinkPage('/')\"" },
            { "id": 1, "text": "営業", "img": "/icons/sales.png" },
            { "id": 2, "text": "人事・労務", "img": "/icons/person.png" },
            { "id": 3, "text": "経理", "img": "/icons/piggy_bank.png" },
            { "id": 4, "text": "管理", "img": "/icons/pc_desk.png" },
            { "id": 5, "text": "登録", "img": "/icons/regist.png" },
            { "id": 6, "text": "リサイクル", "img": "/icons/recycle.png" },
            { "id": -1, "text": "お知らせ", "img": "/icons/info.png", "func": "onclick=\"moveLinkPage('/info')\"" },
            { "id": -1, "text": "設定", "img": "/icons/setting.png", "func": "onclick=\"moveLinkPage('/')\"" },
        ]
        return parents;
    }
    /**
     * 
     * @returns 
     */
    function getChildlist() {
        const childlist = [
            // 営業
            { "id": 0, "parent_id": 1, "text": "製作中", "func": "onclick=\"moveLinkPage('/')\"", "img": "/icons/noimage.png" },
            // 人事・労務
            { "id": 0, "parent_id": 2, "text": "打刻一覧", "func": "onclick=\"moveLinkPage('/timeworks')\"", "img": "/icons/clock2.png" },
            { "id": 0, "parent_id": 2, "text": "日払い", "func": "onclick=\"moveLinkPage('/timeworks/dailypay')\"", "img": "/icons/payment.png" },
            // 経理
            { "id": 0, "parent_id": 3, "text": "現金経費", "func": "onclick=\"moveLinkPage('/cash_expenses')\"", "img": "/icons/payment.png" },
            // 管理
            { "id": 0, "parent_id": 4, "text": "制作中", "func": "onclick=\"moveLinkPage('/')\"", "img": "/icons/noimage.png" },
            // 登録
            { "id": 0, "parent_id": 5, "text": "社員", "func": "onclick=\"moveLinkPage('/regist/fulltime')\"", "img": "/icons/human.png" },
            { "id": 0, "parent_id": 5, "text": "アルバイト", "func": "onclick=\"moveLinkPage('/regist/parttime')\"", "img": "/icons/parttime.png" },
            { "id": 0, "parent_id": 5, "text": "会社(外部)", "func": "onclick=\"moveLinkPage('/regist/company')\"", "img": "/icons/buillding.png" },
            { "id": 0, "parent_id": 5, "text": "支店(外部)", "func": "onclick=\"moveLinkPage('/regist/office')\"", "img": "/icons/office.png" },
            { "id": 0, "parent_id": 5, "text": "営業担当(外部)", "func": "onclick=\"moveLinkPage('/regist/staff')\"", "img": "/icons/staff.png" },
            { "id": 0, "parent_id": 5, "text": "施工担当(外部)", "func": "onclick=\"moveLinkPage('/regist/partner')\"", "img": "/icons/contractor.png" },
            // リサイクル   
            // { "id": 0, "parent_id": 6, "text": "入荷", "func": "onclick=\"moveLinkPage('/recycle/arrival')\"", "img": "/icons/arrival.png" },
            { "id": 0, "parent_id": 6, "text": "一覧", "func": "onclick=\"moveLinkPage('/recycle/list')\"", "img": "/icons/booklist.png" },
            { "id": 0, "parent_id": 6, "text": "使用", "func": "onclick=\"moveLinkPage('/recycle/regist')\"", "img": "/icons/recycle_dust.png" },
            { "id": 0, "parent_id": 6, "text": "引渡", "func": "onclick=\"moveLinkPage('/recycle/delivery')\"", "img": "/icons/good.png" },
            { "id": 0, "parent_id": 6, "text": "発送", "func": "onclick=\"moveLinkPage('/recycle/forward')\"", "img": "/icons/recycle_truck.png" },
            { "id": 0, "parent_id": 6, "text": "ロス", "func": "onclick=\"moveLinkPage('/recycle/loss')\"", "img": "/icons/ban.png" },
        ];
        return childlist;
    }
    /**
     * 標準サイドバー作成
     * @param {アイテムのリスト} items id= -1 でスマホのみ表示
     * @param {タイトル} title
     */
    function createSidebar(items, title) {
        const area = document.getElementById('sidebar-area');
        if (area == null) return;
        area.classList.add('normal-list', 'sidebar');

        if (title != null) {
            area.insertAdjacentHTML('beforeend', "<p class='pc-style'>" + title + "</p>");
        }
        const elmUl = document.createElement('ul');
        items.forEach(function (item) {
            let img = "";
            if (item.img != null) {
                img = "<img src=\"" + item.img + "\"></img>";
            }
            let func = "";
            if (item.func != null) {
                func = " " + item.func;
            }
            let className = "";
            if (item.id == -1) {
                className = " class=\" sp-style\"";
            }
            elmUl.insertAdjacentHTML('beforeend', '<li data-id="' + item.id + '"' + func + className + '>' + img + item.text + '</li>');
        });
        area.insertAdjacentElement('beforeend', elmUl);
    }
    /**
     * リストの先頭に全て表示を追加する
     * @param {先頭に追加させるアイテムリスト} items 
     * @returns アイテムリスト
     */
    function addAllSelectionsToTopOfList(items) {
        const item = { "id": 0, "text": "全て表示", "func": "onclick=\"clickSidebarItem(this)\"" };
        items.unshift(item);
        return items;
    }
    /**
     * サイドバーの先頭アイテムを選択する
     */
    function selectSidebarToTopOfList() {
        const sidebarIrem = document.querySelector('#sidebar-area>ul>li:first-child');
        if (sidebarIrem != null) sidebarIrem.classList.add('selected');
    }
    /**
     * 選択されたアイテムにセレクトクラスを付与する
     * @param {クリックしたアイテム} self 
     */
    function selectSidebarItem(self) {
        // 選択状態を解除する
        const elm = document.querySelector('.normal-list.sidebar');
        const list = elm?.querySelectorAll('li.selected');
        if (list == null) return;
        list.forEach(function (item) {
            item.classList.remove('selected');
        })
        // 自身を選択状態にする
        self.classList.add('selected');
    }
    /**
     * 選択されたアイテムにセレクトクラスを付与する
     * @param {クリックしたアイテム} self 
     */
    function selectAccordionItem(self) {
        // 選択状態を解除する
        const elm = document.querySelector('.normal-list.sidebar');
        const list = elm?.querySelectorAll('p.selected');
        if (list == null) return;
        list.forEach(function (item) {
            item.classList.remove('selected');
        })
        // 自身を選択状態にする
        self.classList.add('selected');
    }
    /**
     * アコーディオンサイドバー作成
     * @param {親要素リスト} parents id= -1 でスマホのみ表示　id=0 でシングル表示
     * @param {小要素リスト} items 
     * @param {タイトル} title 
     * @returns 
     */
    function createAccordionSidebar(parents, items, title, isMark) {
        const area = document.getElementById('sidebar-area');
        if (area == null) return;
        area.classList.add('normal-list', 'sidebar');

        if (title != null) {
            area.insertAdjacentHTML('beforeend', "<p>" + title + "</p>");
        }

        parents.forEach(function (item) {
            let img = "";
            let func = "";
            let single = "";
            if (item.img != null) {
                img = "<img src=\"" + item.img + "\"></img>";
            }
            let elmDetails = document.createElement('details');
            elmDetails.classList.add('accordion', 'sidebar');
            // 小要素がない場合は(item.id=0)single・sp-styleクラスを付与する
            if (item.id == 0) {
                single = " class=\"single\"";
                if (item.func != null) {
                    func = " " + item.func;
                }
            } else if (item.id == -1) {
                single = " class=\"sp-style\"";
                if (item.func != null) {
                    func = " " + item.func;
                }
            }            
            let summary = "<summary data-id='" + item.parent_id + "'" + single + func + ">" + img + item.text + "</summary>";
            elmDetails.insertAdjacentHTML('beforeend', summary);
            if (isMark) {
                let mark = elmDetails.querySelector('summary');
                if (mark != null) mark.classList.add('mark');
            }            
            let childlist = items.filter(value => (value.parent_id == item.id));
            childlist.forEach(function (child) {
                let img = "";
                let func = "";
                if (child.img != null) {
                    img = "<img src=\"" + child.img + "\"></img>";
                }
                if (child.func != null) {
                    func = " " + child.func;
                }
                elmDetails.insertAdjacentHTML('beforeend', '<p data-id="' + child.id + '"' + func + '>' + img + child.text + '</p>');
            });
            area.insertAdjacentElement('beforeend', elmDetails);
        });
    }
    //#endregion
//#region /* ページトップ */
    // /**
    //  * ページトップボタン処理をHTML読み込み時に登録する
    //  */
    // document.addEventListener('load', function () {
    // // document.addEventListener('DOMContentLoaded', function () {
    //     // const elm = document.getElementById('scroll-area');console.log(elm)
    //     // const elm = document.getElementById('tablelist');console.log(elm)
    //     const elm = document.getElementById('scroll-element');console.log(elm)
    //     if (elm != null) {
    //         document.querySelectorAll('.page-top').forEach(function (btn) {
    //             btn.addEventListener('click', function () {
    //                 if (elm.scrollTop > elm.clientHeight) {
    //                     const scroll = document.querySelector('#scroll-area [name="scroll-element"]');
    //                     scroll.scrollIntoView({
    //                         block: "start",
    //                         behavior: 'smooth'
    //                     });
    //                 }
    //             });
    //         });
    //         elm.onscroll = function () {console.log(elm.scrollTop);console.log(elm.scrollHeight)
    //         // elm.addEventListener("scroll", () => {console.log(elm.scrollTop);console.log(elm.scrollHeight)
    //             if (elm.scrollTop > elm.clientHeight) {
    //                 elm.querySelectorAll('.page-top').forEach(function (element) {
    //                     element.style.opacity = "0.7";
    //                 });
    //             } else if (elm.scrollTop < elm.clientHeight) {
    //                 elm.querySelectorAll('.page-top').forEach(function (element) {
    //                     element.style.opacity = "0";
    //                 });
    //             }
    //         };
    //         // elm.onscroll = function () {
    //         //     if (elm.scrollTop > elm.clientHeight) {
    //         //         elm.querySelectorAll('.page-top').forEach(function (element) {
    //         //             element.style.opacity = "0.7";
    //         //         });
    //         //     } else if (elm.scrollTop < elm.clientHeight) {
    //         //         elm.querySelectorAll('.page-top').forEach(function (element) {
    //         //             element.style.opacity = "0";
    //         //         });
    //         //     }
    //         // };
    //     }
    // });
    /**
     * ページトップボタン処理を登録する
     */
    function setPageTopButton(tableId) {
        const tbl = document.getElementById(tableId);
        // const elm = document.getElementById('scroll-element');
        const elm = tbl.querySelector('[name="scroll-element"]');
        if (elm != null) {
            document.querySelectorAll('.page-top').forEach(function (btn) {
                btn.addEventListener('click', function () {
                    if (elm.scrollTop > elm.clientHeight) {
                        const scroll = tbl.querySelector('[name="scroll-element"] tr');
                        scroll.scrollIntoView({
                            block: "start",
                            behavior: 'smooth'
                        });
                    }
                });
                btn.style.opacity = "0";
            });
            elm.onscroll = function () {
                const area = tbl.closest('#scroll-area');
                if (elm.scrollTop > elm.clientHeight) {
                    area.querySelectorAll('.page-top').forEach(function (element) {
                        element.style.opacity = "0.7";
                    });
                } else if (elm.scrollTop < elm.clientHeight) {
                    area.querySelectorAll('.page-top').forEach(function (element) {
                        element.style.opacity = "0";
                    });
                }
            };
        }
    }
    //#endregion
//#region /* ハンバーガーメニュー */
    /**
     * ハンバーガーメニュー作成
     */
    const menuOpen = document.querySelector('#menu-open');
    const menuClose = document.querySelector('#menu-close');
    const menuPanel = document.querySelector('.normal-content');
    let mainPanel = document.querySelector('#panel-area');
    if (mainPanel == null) {
        mainPanel = document.createElement('div');
        mainPanel.setAttribute('id', 'panel-area');
        menuPanel.insertAdjacentElement('beforeend', mainPanel);
    }
    document.addEventListener('DOMContentLoaded', function () {
        if (menuOpen != null) {
            menuOpen.addEventListener('click', () => {
                menuPanel.classList.add('hamburger-open');
                menuPanel.classList.remove('hamburger-close');
                mainPanel.classList.add('dialog');
                const body = document.querySelector('.normal-body');
                if (body != null) body.inert = true;
            });
        };
        if (menuClose != null) {
            menuClose.addEventListener('click', () => {
                menuPanel.classList.add('hamburger-close');
                menuPanel.classList.remove('hamburger-open');
                mainPanel.classList.remove('dialog');
                const body = document.querySelector('.normal-body');
                if (body != null) body.inert = false;
            });
            hamburgerClose();
        }
        if (mainPanel != null) {
            mainPanel.addEventListener('click', () => {
                hamburgerClose();
            });
        };
    });
    /**
     * ハンバーガーメニューを手動で閉じる
     */
    function hamburgerClose() {
        const menuClose = document.querySelector('#menu-close');
        if (menuClose != null) menuClose.click();
    }
    //#endregion
//#region /* ダイアログ */
    /**
     * メッセージダイアログ表示
     * @param {本文} msg 
     * @param {タイトル} title 
     * @param {ヘッダーの色} headerColor 
     * @param {ボタンを含んだフッター} elmFooter 
     */
    function messageDialog(msg, title, headerColor) {
        // ボタンを使用不可にする
        const element = document.querySelector('.normal-content');
        if (element != null) element.inert = true;
        const check = element.closest('.normal-main')?.querySelector('.dialog');
        // ダイアログエリアの取得
        const parent = document.getElementById('dialog-area');
        let elm = parent.querySelector('.dialog');  
        if (elm == null) {
            elm = document.createElement('div');
            elm.classList.add('dialog');
            parent.insertAdjacentElement('beforeend', elm);
        }
        if (check != null) {
            // ダイアログが重なる場合は背景を透明にする
            elm.classList.add('trans');
        }
        // メッセージダイアログ作成
        const elmForm = document.createElement('div');
        elmForm.classList.add('msg-dialog', 'msg');
        // ヘッダー
        elmForm.insertAdjacentHTML('beforeend', '<div class="dialog-header ' + headerColor + '"><span>' + title + '</span>' + 
            '<div class="img-btn" onclick="closeDialog(\'dialog-area\', event)"><img title="閉じる" src="/icons/close-s.png"></div>');
        const arrayMsg = msg.replace(/\s+/, '<br>');
        elmForm.insertAdjacentHTML('beforeend', '<span class="msg-dialog-content">' + arrayMsg + '</span>');
        elmForm.insertAdjacentHTML('beforeend', '<form class="dialog-footer"><button id="msg-okbtn" class="normal-btn ok" type="submit" onclick="closeDialog(\'dialog-area\', event)"><span>了解</span></button></form>');
        parent.insertAdjacentElement('beforeend', elmForm);
        // OKボタンにフォーカスを合わせる
        document.getElementById('msg-okbtn').focus();
    }
    /**
     * ダイアログを閉じる
     * @param {ダイアログのID名} dialogId 
     * @param {イベント} e 
     */
    function closeDialog(dialogId, e) {
        if (e != null) {
            e.preventDefault();
        }
        // 全ての子要素を削除
        const parent = document.getElementById(dialogId);
        if (parent.hasChildNodes()) {
            while (parent.firstChild) {
                parent.removeChild(parent.firstChild);
            }
        }
        // 要素がクリック禁止になっている場合は解除する
        const elm = document.querySelector('.normal-content');
        if (elm != null) elm.inert = false;
        // autofocus属性が付いているアイテムをフォーカスする
        const focusBox = document.querySelector('input[autofocus]');
        if (focusBox != null) focusBox.focus();
    }

    /**
     * ケバブメニューを開く
     * @param {ケバブメニューボタン} self 
     * @returns 
     */
    function openKebabDialog(self) {
        const area = self.closest('.kebab-parts')?.querySelector('.img-btns.kebab');
        if (area == null) return;

        const menuPanel = document.querySelector('.normal-content');
        if (menuPanel == null) return;
        let mainPanel = menuPanel.querySelector('#panel-area');
        if (mainPanel == null) {
            mainPanel = document.createElement('div');
            mainPanel.setAttribute('id', 'panel-area');
            menuPanel.insertAdjacentElement('beforeend', mainPanel);
        }
        mainPanel.classList.add('dialog');
        mainPanel.addEventListener('click', () => {
            closeKebabDialog();
        });
        // [img-btns]に[open]クラスを付与する
        area.classList.add('open');
    }
    // /**
    //  * リサイクルケバブメニューを開く
    //  */
    // function createRecycleKebabDialog() {
    //     const tbl = document.getElementById('tablelist');
    //     const isChecked = isAnyOneChecked(tbl);
    //     const parent = document.getElementById('kebab-menu-dialog-area');
    //     const elm = document.createElement('div');
    //     elm.classList.add('dialog');
    //     elm.onclick = function (event) { closeDialog('kebab-menu-dialog-area', event); }

    //     const elmContent = document.createElement('ul');
    //     // 削除ボタン
    //     if (isChecked == true) {
    //         elmContent.insertAdjacentHTML('beforeend', '<li id="kebab-delete" onclick="execDelete"><img title="削除" src="/icons/dust.png">削除</li>');
    //     }
    //     // CSVダウンロードボタン
    //     if (isChecked == true) {
    //         elmContent.insertAdjacentHTML('beforeend', '<li id="kebab-download" onclick="execDownload"><img title="ダウンロード" src="/icons/download.png">ダウンロード(CSV)</li>');
    //     }
    //     // 編集ボタン
    //     if (isChecked == true) {
    //         elmContent.insertAdjacentHTML('beforeend', '<li id="kebab-edit" onclick="execEdit()"><img title="編集" src="/icons/edit.png">編集</li>');
    //     }
    //     // 閉じるボタン
    //     elmContent.insertAdjacentHTML('beforeend', '<li id="kebab-close" onclick="closeDialog(\'kebab-menu-dialog-area\')"><img title="閉じる" src="/icons/close.png">閉じる</li>');

    //     const elmForm = document.createElement('div');
    //     elmForm.classList.add('menu-dialog', 'context');
    //     elmForm.insertAdjacentElement('beforeend', elmContent);

    //     parent.insertAdjacentElement('beforeend', elm);
    //     parent.insertAdjacentElement('beforeend', elmForm);
    // }
    /**
     * ケバブメニューを閉じる
     * @param {ケバブメニューボタン} self 
     * @returns 
     */
    function closeKebabDialog() {
        // [img-btns]から[open]クラスを剥奪する
        const menuPanel = document.querySelector('.normal-content');
        const mainPanel = menuPanel?.querySelector('#panel-area');
        if (mainPanel != null) {
            const areas = document.querySelectorAll('.img-btns.kebab');
            areas.forEach(function(area) {
                if (area?.classList.contains('open')) {
                    area.classList.remove('open');
                    mainPanel.classList.remove('dialog');
                    deleteElements('panel-area');
                }
            });
        }
    }
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
    /**
     * フォーム画面のヘッダー作成
     * @param {*} title 
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
        const closeImg = document.createElement('img');
        closeImg.setAttribute('title', '閉じる');
        closeImg.setAttribute('src', '/icons/close-s.png');
        closeElm.insertAdjacentElement('beforeend', closeImg);
        elmHeader.insertAdjacentElement('beforeend', closeElm);
        return elmHeader;
    }
    //#endregion
//#region /* 情報取得・操作 */
    /**
     * 年月日等の各要素を取得
     * @returns yyyyMMdd h:m
     */
    function getNow() {
        const date = new Date();
        const y = date.getFullYear();
        const m = date.getMonth() + 1;
        const d = date.getDate();
        const h = date.getHours();
        const min = date.getMinutes();
        return y + "/" + ('0' + m).slice(-2) + "/" + ('0' + d).slice(-2) + ' ' + ('0' + h).slice(-2) + ":" + ('0' + min).slice(-2);
    }
    /**
    * 年月日等の各要素を取得
    * @returns yyyyMMddhhmm000000
    */
    function getNowNoBreak() {
        const date = new Date();
        const y = date.getFullYear();
        const m = date.getMonth() + 1;
        const d = date.getDate();
        const h = date.getHours();
        const min = date.getMinutes();
        const milli = date.getMilliseconds();
        return y + ('0' + m).slice(-2) + ('0' + d).slice(-2) + ('0' + h).slice(-2) + ('0' + min).slice(-2) + ('00' + milli).slice(-3);
    }
    /**
     * 年月日等の各要素を取得
     * @returns yyyy-MM-dd hh:mm:ss.SSS:
     */
    function getNowHyphen() {
        const date = new Date();
        const y = date.getFullYear();
        const m = date.getMonth() + 1;
        const d = date.getDate();
        const h = date.getHours();
        const min = date.getMinutes();
        const sec = date.getSeconds()
        const milli = date.getMilliseconds();
        return y + "-" + ('0' + m).slice(-2) + "-" + ('0' + d).slice(-2) + ' ' + ('0' + h).slice(-2) + ":" + ('0' + min).slice(-2) + ":" + ('0' + sec).slice(-2) + "." + ('00' + milli).slice(-3);
    }
    /**
     * 期間指定ボックスを変更する
     * @param {*} str 分岐用文字列
     * @returns 開始日と終了日を変更する
     */
    function execSpecifyPeriod(str) {
        const date = new Date();

        const startdate = document.querySelector('input[name="start-date"]');
        if (startdate == null) return;
        const start = new Date(startdate.value);

        const enddate = document.querySelector('input[name="end-date"]');
        if (enddate == null) return;
        const end = new Date(enddate.value);

        switch (str) {
            case "last-month":
                startdate.value = new Date(date.getFullYear(), date.getMonth() - 1, 1).toLocaleDateString('sv-SE');
                enddate.value = new Date(date.getFullYear(), date.getMonth(), 0).toLocaleDateString('sv-SE');
                break;
            case "this-month":
                startdate.value = new Date(date.getFullYear(), date.getMonth(), 1).toLocaleDateString('sv-SE');
                enddate.value = new Date(date.getFullYear(), date.getMonth() + 1, 0).toLocaleDateString('sv-SE');
                break;
            case "prev-day":
                startdate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() - 1).toLocaleDateString('sv-SE');
                enddate.value = new Date(end.getFullYear(), end.getMonth(), end.getDate() - 1).toLocaleDateString('sv-SE');
                break;
            case "next-day":
                startdate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() + 1).toLocaleDateString('sv-SE');
                enddate.value = new Date(end.getFullYear(), end.getMonth(), end.getDate() + 1).toLocaleDateString('sv-SE');
                break;
            case "yesterday":
                startdate.value = new Date(date.getFullYear(), date.getMonth(), date.getDate() - 1).toLocaleDateString('sv-SE');
                enddate.value = new Date(date.getFullYear(), date.getMonth(), date.getDate() - 1).toLocaleDateString('sv-SE');
                break;
            case "prev-week":
                startdate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() - 7).toLocaleDateString('sv-SE');
                enddate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() - 1).toLocaleDateString('sv-SE');
                break;
            case "next-week":
                startdate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() + 7).toLocaleDateString('sv-SE');
                enddate.value = new Date(start.getFullYear(), start.getMonth(), start.getDate() + 13).toLocaleDateString('sv-SE');
                break;
            case "this-week":
                startdate.value = new Date(date.getFullYear(), date.getMonth(), date.getDate()).toLocaleDateString('sv-SE');
                enddate.value = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 6).toLocaleDateString('sv-SE');
                break;
            case "today":
                startdate.value = date.toLocaleDateString('sv-SE');
                enddate.value = date.toLocaleDateString('sv-SE');
                break;
            case "prev-month":
                startdate.value = new Date(start.getFullYear(), start.getMonth() - 1, 1).toLocaleDateString('sv-SE');
                enddate.value = new Date(start.getFullYear(), start.getMonth(), 0).toLocaleDateString('sv-SE');
                break;
            case "next-month":
                startdate.value = new Date(end.getFullYear(), end.getMonth() + 1, 1).toLocaleDateString('sv-SE');
                enddate.value = new Date(end.getFullYear(), end.getMonth() + 2, 0).toLocaleDateString('sv-SE');
                break;
            default:
                break;
        }
    }
    /**
     * 郵便番号から住所を取得
     * @param {*} e 
     */
    async function getAddress(e) {
        if(e == null || e.key === 'Enter'){
            if (e != null) e.preventDefault();

            const sourceElm = document.querySelector('input[name="postal_code"]');
            if (sourceElm != null) {
                // 文字列から[-]を除去
                const modified = sourceElm.value.replace('-', '');
                // 修正した文字列が７桁の数値か確認
                if (modified.length != 7 || isNaN(modified) == true) return;
                // 郵便番号の定型に変換[000-0000]
                const code = modified.substr(0, 3) + "-" + modified.substr(3, 4);
                // 郵便番号で住所を検索
                const data = "postal_code=" + encodeURIComponent(code);
                const resultResponse = await postFetch('/getaddress/postalcode', data, token, 'application/x-www-form-urlencoded');
                const result = await resultResponse.json();
                // 定型に修正した郵便番号を郵便番号入力ボックスに代入
                const targetElm = document.querySelector('input[name="full_address"]');
                sourceElm.value = result.postal_code;
                targetElm.focus();
                // 検索結果によって処理を分岐
                if (result.address_id > 0) {
                    targetElm.value = result.prefecture + result.city + result.town;
                    targetElm.setSelectionRange(end, targetElm.value.length);
                } else {
                    targetElm.value = "";
                }
                
            }
        }
    }
    /**
     * ターゲット要素が郵便番号か確認
     * @param {*} sourceElm 
     * @returns 
     */
    function checkPostalCode(sourceElm) {
        if (sourceElm.value == "") return true;
        // 文字列から[-]を除去
        const modified = sourceElm.value.replaceAll('-', '');
        // 修正した文字列が７桁の数値か確認
        if (modified.length != 7 || isNaN(modified) == true) return false;
        return true;
    }
    /**
     * ターゲット要素が電話番号か確認
     * @param {*} sourceElm 
     * @returns 
     */
    function checkPhoneNumber(sourceElm) {
        if (sourceElm.value == "") return true;
        // 文字列から[-]を除去
        const modified = sourceElm.value.replaceAll('-', '');
        // 修正した文字列が数値か確認
        if (isNaN(modified) == true) return false;
        // 修正した文字列が11桁か10桁か確認
        if (modified.length != 11 && modified.length != 10) return false;
        return true;
    }
    /**
     * ターゲット要素がインボイス登録番号か確認
     * @param {*} sourceElm 
     * @returns 
     */
    function checkRegistrationNumber(sourceElm) {
        if (sourceElm.value == "") return true;
        // 文字列が数値か確認
        if (isNaN(sourceElm.value) == true) return false;
        // 文字列が13桁か確認
        if (sourceElm.value.length != 13) return false;
        return true;
    }
    /**
     * ターゲット要素がメールアドレスか確認
     * @param {*} sourceElm 
     * @returns 
     */
    function checkMailAddress(sourceElm) {
        if (sourceElm.value == "") return true;
        // 文字列がメールアドレスの形式か確認
        if (!sourceElm.value.match(/.+@.+\..+/)) return false;
        return true;
    }
    /**
     * ターゲット要素がWEBアドレスか確認
     * @param {*} sourceElm 
     * @returns 
     */
    function checkWebAddress(sourceElm) {
        if (sourceElm.value == "") return true;
        // 文字列がWEBアドレスの形式か確認
        if (!URL.canParse(sourceElm.value)) return false;
        return true;
    }
    /**
     * 開始時刻と終了時刻の差を求める
     * @param {*} bt 開始時刻(00:00)
     * @param {*} at 終了時刻(00:00)
     * @returns 差(00:00)
     */
    function resultSubtractingTime(bt, at) {
        let dt = [bt, at].map(v => {
            v = v.split(":");
            return v[0] * 60 + +v[1];
        }).reduce((p, c) => c - p);
        return [dt / 60 | 0, dt % 60].map(v => {
            return (v + "").padStart(2, "0");
        }).join(":");
    }
    /**
     * 時刻を数値に直す
     * @param {*} timeString 時刻文字列(00:00)
     * @returns 1/60の数値
     */
    function parseTimeToNum(timeString) {
        if (timeString == null || timeString == "") return 0;
        const timeParts = timeString.split(":");
        const hours = parseInt(timeParts[0]);
        const minutes = parseInt(timeParts[1]);
        return (hours * 60 + minutes) / 60;
    }
    /**
     * 数値を時刻に直す
     * @param {*} timeNum 時刻数値
     * @returns (00:00))
     */
    function parseTimeToStr(timeNum) {
        if (timeNum == null || timeNum == "") return "0:00";
        const timeString = timeNum.toFixed(2);
        const timeParts = timeString.split(".");
        const hours = parseInt(timeParts[0]).toString();
        const minutes = (parseInt(timeParts[1]) * 60 / 100).toFixed(0).toString();
        return hours + ":" + minutes.substring(0, 2).padStart(2, "0");
    }
    /**
     * 現在地を取得する
     * @returns latitude:緯度　longitude:経度
     */
    async function getLoacation() {
        // Geolocationのサポートを確認
        if ("geolocation" in navigator) {
        return new Promise((resolve, reject) => {
            navigator.geolocation.getCurrentPosition(resolve, reject)
        })
        } else {
        console.error("このブラウザはGeolocationをサポートしていません。");
        }
    }
    //#endregion
//#region /* チェックボックス */
    /**
     * 手動でチェックボタンを押す
     * @param {対象要素ID} id
     * @param {要素自身} self 
     */
    function clickCheckBtnManual(id, self) {
        const panel = self.closest('.tab-panel-box');
        let tab = "00";
        if (panel != null) {
            tab = panel.dataset.panel;
        }
        const chk = document.getElementById('chk' + tab + '-' + id);
        chk.checked = !chk.checked;
        clickCheckBtn(id, self)
    }
    /**
     * 手動でチェックボタンをオンまたはオフにする
     * @param {対象要素ID} id
     * @param {要素自身} self 
     * @param {結果} result
     */
    function changeCheckBtnManual(id, self, result) {
        const panel = self.closest('.tab-panel-box');
        let tab = "00";
        if (panel != null) {
            tab = panel.dataset.panel;
        }
        const chk = document.getElementById('chk' + tab + '-' + id);
        chk.checked = result;
        clickCheckBtn(id, self)
    }
    /**
     * チェックボックス押下時 
     * @param {選択アイテムのタブID} tab タブパネルがない場合は0
     * @param {選択アイテムID} id 
     * @param {チェックボックス自身} self 
     */
    function clickCheckBtn(id, self) {
        const panel = self.closest('.tab-panel-box');
        let tab = "00";
        if (panel != null) {
            tab = panel.dataset.panel;
        }
        const parent = self.closest('table');
        /* セレクトクラスを付与もしくは剥奪する */
        const row = document.getElementById('row' + tab + "-" + id);
        const chk = document.getElementById('chk' + tab + "-" + id);
        addSelectClassToRow(row, chk.checked)
        /* 全て選択されていれば[chk0]をチェック状態にする、全て選択されていなければ[chk0]を非選択状態にする */
        const result = isEverythingChecked(parent);
        const allchkbtn = document.getElementById('chk' + tab + '-0');
        allchkbtn.checked = result;
        /* セレクトクラスを付与もしくは剥奪する */
        const header = document.getElementById('row' + tab + '-0');
        addSelectClassToRow(header, result)
    }
    /**
     * 全てチェックされているか確認する
     * @param {確認対象の親要素} parent
     * @returns 全て選択されていれば[True]、一つでも選択されていなければ[False]を返す
     */
    function isEverythingChecked(parent) {
        const selects = parent.querySelectorAll('input[name="chk-box"]');
        let result = true;
        if (selects != null) {
            for (let i = 0; i < selects.length; i++) {
                if (selects[i].checked == false) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    /**
     * 全てチェックボックス押下時
     * @param {全て選択ボタン自身} self
     */
    function clickAllCheckBtn(self) {
        const panel = self.closest('.tab-panel-box');
        let tab = "00";
        if (panel != null) {
            tab = panel.dataset.panel;
        }
        const parent = self.closest('table');
        let row = document.getElementById('row' + tab + '-0');
        const selects = parent.querySelectorAll('input[name="chk-box"]');
        addSelectClassToRow(row, self.checked);
        if (selects != null) {
            selects.forEach((chk) => {
                chk.checked = self.checked;
                row = chk.closest('[name="data-row"]');
                addSelectClassToRow(row, self.checked);
            });
        }
    }
    /**
     * 選択されているチェックボックスを全て取得する
     * @param {取得対象の親要素} parent 
     * @returns チェックされている要素のIDリストをJSON形式で返す { id:0 }
     */
    function getAllSelectedInCheckbox(parent) {
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
    * 選択されたIDでIN句用文字列を作成
    * @param {変換用の選択された要素群} ids
    * @returns IN句用の形式で返す　IN(1,2,3,,,,)
    */
    function getSearchStringWithSelectedId(ids) {
        let str = "IN ("
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
    //#endregion
//#region /* 絞り込み */
    /**
     * 検索ワード（空白区切り）をもとにリストを抽出して画面更新する
     * @param {検索ワード} words 
     */
    function extractListFromSearchBox(list) {
        const box = document.getElementById('search-box');
        if (box == null) return list;
        // 検索ワードを取得
        const words = box.value;
        if (words.length == 0) return list;
        // // 検索ワードが空白の場合はリストを初期化する
        // if (words.length == 0) {
        //     list = structuredClone(origin);
        //     return;
        // }
        // 空白で区切りで配列を作成
        let list_word = words.split(/\s+/);
        list_word.forEach(word => {
            const result = list.filter(val => {
                let res = false;
                Object.values(val).forEach(item => {
                    // 要素が文字列かどうかを判定
                    if (typeof (item) == "string" || item instanceof String) {
                        // 要素にワードが含まれていれば、変数[res]に[true]を代入
                        if (item.includes(word)) res = true;
                    }
                });
                // 要素の中に[true]が一つでもあれば要素を返す
                if (res) return val;
            });
            list = result;
        });
        return list;
    }
    /**
     * サイドバーの選択要素で絞り込み
     */
    function extractListFromSidebarItem(origin) {
        let list = [];
        const area = document.getElementById('sidebar-area');
        if (area != null) {
            // サイドバーで選択されている要素を取得
            const selectItem = area.querySelector('li.selected');
            if (selectItem != null) {
                if (Number(selectItem.dataset.id) > 0) {
                    // サイドバーで選択したIDをもとにリストを抽出する
                    list = origin.filter(value => (value.category_id == Number(selectItem.dataset.id)));
                } else {
                    list = structuredClone(origin);
                }
            }
        }
        return list;
    }
    /**
     * サイドバーの選択要素で絞り込み
     */
    function extractListFromAccordionItem(origin) {
        let list = [];
        const area = document.getElementById('sidebar-area');
        if (area != null) {
            // サイドバーで選択されている要素を取得
            const selectItem = area.querySelector('p.selected');
            if (selectItem != null) {
                if (Number(selectItem.dataset.id) > 0) {
                    // サイドバーで選択したIDをもとにリストを抽出する
                    list = origin.filter(value => (value.category_id == Number(selectItem.dataset.id)));
                } else {
                    list = structuredClone(origin);
                }
            }
        }
        return list;
    }
    //#endregion
//#region /* タブ */
    // イベントの処理
    function tabSwitch(e) {
        // クリックされた要素のデータ属性を取得
        const tabTargetData = e.currentTarget.dataset.tab;
        // クリックされた要素の親要素と、その子要素を取得
        const tabList = e.currentTarget.closest('.tab-menu');
        const tabItems = tabList.querySelectorAll('.tab-menu-item');
        // クリックされた要素の親要素の兄弟要素の子要素を取得
        const tabPanelItems = tabList.
        nextElementSibling.querySelectorAll('.tab-panel-box');
        // クリックされたtabの同階層のmenuとpanelのクラスを削除
        tabItems.forEach((tabItem) => {
            tabItem.classList.remove('is-active');
        })
        tabPanelItems.forEach((tabPanelItem) => {
            tabPanelItem.classList.remove('is-show');
        })
        // クリックされたmenu要素にis-activeクラスを付加
        e.currentTarget.classList.add('is-active');
        // クリックしたmenuのデータ属性と等しい値を持つパネルにis-showクラスを付加
        tabPanelItems.forEach((tabPanelItem) => {
            if (tabPanelItem.dataset.panel ===  tabTargetData) {
                tabPanelItem.classList.add('is-show');
            }
        })
    }
//#endregion
//#region /* テーブルソート */
    // テーブルをソートする関数
    function sortTable(table, col, reverse) {
        // テーブルのtbodyを取得
        // tbody内のすべての行を配列に変換
        const tbody = table.tBodies[0];
        const tr = Array.prototype.slice.call(tbody.rows);
        
        // 行をソート
        tr.sort(function(a, b) {
            // // 各行の指定列の値を取得し、数値として比較
            // const aValue = parseFloat(a.cells[col].textContent.trim());
            // const bValue = parseFloat(b.cells[col].textContent.trim());
            // // 昇順または降順でソート
            // return reverse ? bValue - aValue : aValue - bValue;

            const aValue = a.cells[col].textContent.trim().toLowerCase();
            const bValue = b.cells[col].textContent.trim().toLowerCase();
            if (reverse) {
                if     (aValue < bValue){ return -1; }
                else if(aValue > bValue){ return  1; }
                return 0;
            } else {
                if     (bValue < aValue){ return -1; }
                else if(bValue > aValue){ return  1; }
                return 0;
            }
        });
        // ソートされた行をテーブルに再配置
        tr.forEach(function(row) {
            tbody.appendChild(row);
        });
    }
    // テーブルのヘッダーをクリック可能にする関数
    function makeSortable(table) {
        // テーブル内のすべてのヘッダーセルを取得
        const headers = table.querySelectorAll('th:not(first-child)');
        
        // 各ヘッダーにクリックイベントを追加
        headers.forEach(function(header, index) {
            header.addEventListener('click', function() {
                // ヘッダーが既に昇順ソートされているか確認
                const isAsc = header.classList.contains('sorted-asc');
                // const isDesc = header.classList.contains('sorted-desc');
                // 他のヘッダーのソート状態をリセット
                resetSortIndicators(headers);
                // 昇順の場合は降順にソート、それ以外は昇順にソート
                if (isAsc) {
                    sortTable(table, index, true); // 降順
                    header.classList.add('sorted-desc'); // 降順のクラスを追加
                // } else if (isDesc) {
                //     filterDisplay();
                //     header.classList.remove('sorted-desc'); // 降順のクラスを削除
                } else {
                sortTable(table, index, false); // 昇順
                header.classList.add('sorted-asc'); // 昇順のクラスを追加
            }
            });
        });
    }
    // すべてのヘッダーのソート状態をリセットする関数
    function resetSortIndicators(headers) {
        // 各ヘッダーからソートクラスを削除
        headers.forEach(function(header) {
            header.classList.remove('sorted-asc', 'sorted-desc');
        });
    }
    // // ページが読み込まれたら、テーブルをソート可能にする
    // document.addEventListener('DOMContentLoaded', function() {
    //     makeSortable(document.getElementById('sortable_table'));
    // });
//#endregion