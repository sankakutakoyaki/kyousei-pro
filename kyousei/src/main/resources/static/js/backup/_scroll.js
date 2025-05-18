
/******************************************************************************************************* 無限スクロール */
/**
 * 無限スクロール 
 */
document.addEventListener('DOMContentLoaded', function () {
    const elm = document.getElementById('scroll-element');
    elm.onscroll = function () {
        if (this.scrollTop + this.clientHeight + .5 >= this.scrollHeight) {
            // スクロール時の処理
            scrollFunc();
        }
    };
});
