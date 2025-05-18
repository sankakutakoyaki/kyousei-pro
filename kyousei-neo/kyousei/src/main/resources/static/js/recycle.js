"use strict"

// function execRecycleNumberInputChanged(e) {
//     if (e.target.value == "") return;

//     if(e.key === 'Enter'){
//         e.preventDefault();
//         afterRecyleNumberInputChanged();
//     }
// }
// function execRecycleNumberInputBlur(e) {
//     if (e.target.value == "") return;
//     checkRecycleNumberInput(e);
// }
// function afterRecyleNumberInputChanged() {
//     const nextBlock = document.querySelector('[name="regist-btn"]');
//     const number = document.querySelector("[name='recycle-number']");
//     if (nextBlock != null && number != null) {
//         checkRecycleNumber(number);
//         if (number.value != "") {
//             nextBlock.click();
//         } else {
//             number.focus();
//         }
//     }
// }
// function checkRecycleNumberInput(e) {
//     checkRecycleNumber(e.target);
// }
// function checkRecycleNumber(num) {
//     const number = num.value.replaceAll("-", "").replaceAll("a", "").replaceAll("A", "");
//     if (number.length != 13 || isNaN(number) == true) {
//         num.value = "";
//         return false;
//     }
//     num.value = number.slice(0, 4) + "-" + number.slice(4, 12) + "-" + number.slice(12, number.length);
//     return true;
// }

// リサイクル券番号が書式通りかチェックする
function checkRecycleNumber(num) {
    const number = num.value.replaceAll("-", "").replaceAll("a", "").replaceAll("A", "");
    if (number.length != 13 || isNaN(number) == true) {
        num.value = "";
        return false;
    }
    num.value = number.slice(0, 4) + "-" + number.slice(4, 12) + "-" + number.slice(12, number.length);
    return true;
}