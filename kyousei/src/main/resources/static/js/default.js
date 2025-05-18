
/******************************************************************************************************* ページトップ */
/**
 * ページトップボタン処理をHTML読み込み時に登録する
 */
document.addEventListener('DOMContentLoaded', function () {
    const elm = document.getElementById('scroll-area');
    if (elm != null) {
        document.querySelectorAll('.page-top').forEach(function (btn) {
            btn.addEventListener('click', function () {
                if (elm.scrollTop > elm.clientHeight) {
                    const scroll = document.getElementById('scroll-element');
                    scroll.scrollIntoView({
                        block: "start",
                        behavior: 'smooth'
                    });
                }
            });
        });
        elm.onscroll = function () {
            if (elm.scrollTop > elm.clientHeight) {
                elm.querySelectorAll('.page-top').forEach(function (element) {
                    element.style.opacity = "0.7";
                });
            } else if (elm.scrollTop < elm.clientHeight) {
                elm.querySelectorAll('.page-top').forEach(function (element) {
                    element.style.opacity = "0";
                });
            }
        };
    }
});

// /******************************************************************************************************* 無限スクロール */
// /**
//  * 無限スクロール 
//  */
// document.addEventListener('DOMContentLoaded', function () {
//     const elm = document.getElementById('scroll-element');
//     elm.onscroll = function () {
//         if (this.scrollTop + this.clientHeight + .5 >= this.scrollHeight) {
//             // スクロール時の処理
//             scrollFunc();
//         }
//     };
// });

/******************************************************************************************************* リンク操作 */
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

/******************************************************************************************************* 画面操作 */
/**
 * 処理開始時の処理　スピナー表示
 */
function startProcessing() {
    const elm = document.querySelector('.normal-content');
    if (elm != null) elm.inert = true;
    const spinner = document.getElementById('loading');
    if (spinner != null) {
        spinner.classList.remove('loaded');
    }
}

/**
 * 処理終了時の処理 スピナーを消す
 */
function processingEnd() {
    const spinner = document.querySelector('#loading');
    if (spinner != null) {
        spinner.classList.add('loaded')
    }
    const elm = document.querySelector('.normal-content');
    if (elm != null) elm.inert = false;
}

/**
 * 画面操作をできなくする
 */
function displayLockon() {        
    const element = document.querySelector('.normal-content');
    if (element != null) element.inert = true; 
}

/**
 * 画面ロックの解除
 */
function displayLockoff() {        
    const element = document.querySelector('.normal-content');
    if (element != null) element.inert = false; 
}

/******************************************************************************************************* エレメント操作 */
/**
 * 指定したエレメント以下の要素を削除
 * @param {削除対象のエレメント} item
 */
function deleteElements(item) {
    if (item == null) return;
    while (item.firstChild) {
        item.removeChild(item.firstChild);
    };
}

/**
 * 指定したエレメントを削除
 * @param {削除対象のエレメント} item
 */
function deleteFullElements(item) {
    if (item == null) return;
    while (item.firstChild) {
        item.removeChild(item.firstChild);
    };
    item.remove();
}

/**
 * 指定したID以下のエレメントを削除
 * @param {削除対象の親エレメントID} id 
 */
function deleteElementsWithSpecifiedId(id) {
    const item = document.getElementById(id);
    if (item == null) return;
    while (item.firstChild) {
        item.removeChild(item.firstChild);
    };
}

/**
 * 指定したNAME以下のエレメントを削除
 * @param {削除対象の親エレメントNAME} name 
 */
function deleteElementsWithSpecifiedName(parent, name) {
    const item = parent?.querySelector('[name="' + name + '"]');
    if (item == null) return;
    while (item.firstChild) {
        item.removeChild(item.firstChild);
    };
}

/**
 * 指定した全てのNAMEのエレメントを削除
 * @param {削除対象の親エレメントNAME} name 
 */
function deleteAllElementsByName(parent, name) {
    const names = parent?.querySelectorAll('[name="' + name + '"]');    
    names.forEach((item) => {
        if (item == null) return;
        while (item.firstChild) {
            item.removeChild(item.firstChild);
        };
        item.remove();
    });
}

/**
 * [name]で指定した全てのエレメントにクラスを付与する
 * @param {対象のエレメントを内包する親要素のエレメント} parentElm
 * @param {取得するエレメントの名前} name 
 * @param {付与するクラス名} className 
 */
function addClassToAllElementsOfSameName(parentElm, name, className) {
    changeClassToAllElementsOfSameName(parentElm, name, className, 'add')
}

/**
 * [name]で指定した全てのエレメントにクラスを剥奪する
 * @param {対象のエレメントを内包する親要素のエレメント} parentElm
 * @param {取得するエレメントの名前} name 
 * @param {剥奪するクラス名} className 
 */
function removeClassToAllElementsOfSameName(parentElm, name, className) {
    changeClassToAllElementsOfSameName(parentElm, name, className, 'remove')
}

/**
 * [name]で指定した全てのエレメントにクラスを付与もしくは剥奪する
 * @param {対象のエレメントを内包する親要素のエレメント} parentElm
 * @param {取得するエレメントの名前} name 
 * @param {付与・剥奪するクラス名} className 
 * @param {addで付与・removeで剥奪} reverse 
 */
function changeClassToAllElementsOfSameName(parentElm, name, className, reverse) {
    const selects = parentElm.querySelectorAll('[name="' + name + '"]');
    if (selects != null) {
        // 全てのセルに適用
        selects.forEach((select) => {
            if (reverse == 'add') {
                if (!select.classList.contains(className)) {
                    select.classList.add(className);
                }
            } else if (reverse == 'remove') {
                if (select.classList.contains(className)) {
                    select.classList.remove(className);
                }
            }
        });
    }
}

/**
 * エレメントのDisabledを反転させる
 * @param {*} elm 
 */
function reverseDisabled(elm) {
    elm.disabled = !elm.disabled;
}

/******************************************************************************************************* Post送信 */
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