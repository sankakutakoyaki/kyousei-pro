
/******************************************************************************************************* 現在地取得 */
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

/******************************************************************************************************* 郵便番号から住所を取得 */
/**
 * 郵便番号から住所を取得
 * @param {社員コード} codeId 
 * @param {郵便番号ボックスのID} addressId 
 * @param {イベント} e 
 */
async function getAddressFromPostalCode(codeId, addressId, e) {
  if (e != null) {
    e.preventDefault();
  }

  const postalCode = document.getElementById(codeId);
  if (postalCode != null) {
    const data = "postal_code=" + encodeURIComponent(postalCode.value);  // URIエンコード
    const url = '/postalcode/getaddress';
    const contentType = 'application/x-www-form-urlencoded';
    const result = await postFetch(url, data, token, contentType);
    const address = await result.json();

    const fullAddress = document.getElementById(addressId);
    postalCode.value = address.postal_code;
    if (address.address_id > 0) {
      fullAddress.value = address.prefecture + address.city + address.town;
    } else {
      fullAddress.value = "";
    }
    fullAddress.focus();
  }
}