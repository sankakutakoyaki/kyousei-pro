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

// /******************************************************************************************************* CSVファイルダウンロード */
// /**
//  * CSVファイルをダウンロードする
//  */
// async function downloadCsvFile(url, self) {
//   // ダウンロードボタンの親要素（tablelist-container）を取得する
//   const parent = self.closest('.tablelist-container');
//   // ダウンロード用のSQL文を取得する（選択されていなければ[NULL]が返る）
//   const downloadStr = await createSqlStringFromSearchCriteriaByIds(parent);
//   if (downloadStr == null) {
//       notSelectedDialog();
//       return false;
//   }
//   const downloadsqldata = { "sqlString": downloadStr, "classPath": sqlformdata.classPath }
//   const data = JSON.stringify(downloadsqldata);
//   const contentType = 'application/json';
//   const result = await postFetch(url, data, token, contentType);
//   const text = await result.json();
  
//   // const ids = await getAllSelectedCheckboxes(self);
//   // // const result = isExistCheckCellCheckedByName(parentId, 'chk-box');
//   // if (ids.length > 0) {
//   //   const data = JSON.stringify(ids);
//   //   const contentType = 'application/json';
//   //   const result = await postFetch(url, data, token, contentType);
//   //   const text = await result.text();
  
//   //   const bom = new Uint8Array([0xef, 0xbb, 0xbf]);
//   //   const blob = new Blob([bom, text], { type: "text/csv" });
//   //   const objectUrl = URL.createObjectURL(blob);
//   //   const downloadLink = document.createElement("a");
//   //   downloadLink.download = getNowNoBreak() + ".csv";
//   //   downloadLink.href = objectUrl;
//   //   downloadLink.click();
//   //   downloadLink.remove();
//   // } else {
//   //   notSelectedDialog();
//   // }
// }