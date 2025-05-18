async function generateAesGcmKey() {
    // (1) EC 鍵ペアを生成（P-256）
    const keyPair = await crypto.subtle.generateKey(
        {
            name: "ECDH",
            namedCurve: "P-256",
        },
        true,
        ["deriveBits"]
    );

    // (2) サーバーの公開鍵を取得
    const serverPublicKeyBase64 = await fetch("/ecdh/public-key")
        .then(response => response.text());

    // Base64 → ArrayBuffer にデコード
    const serverPublicKeyArrayBuffer = base64ToArrayBuffer(serverPublicKeyBase64);

    // (3) サーバーの公開鍵をインポート
    const serverPublicKey = await crypto.subtle.importKey(
        "spki",
        serverPublicKeyArrayBuffer,
        { name: "ECDH", namedCurve: "P-256" },
        false,
        []
    );

    // (4) ECDH で共有鍵（IKM）を生成
    const sharedSecret = await crypto.subtle.deriveBits(
        { name: "ECDH", public: serverPublicKey },
        keyPair.privateKey,
        256 // 256-bit の共有鍵
    );

    // (5) HKDF を適用し、AES-GCM の鍵を導出（128-bit）
    const aesKey = await hkdf(sharedSecret, "AES-GCM", 16);

    console.log("🔑 AES-GCM Key:", arrayBufferToBase64(aesKey));

    return aesKey;
}

// HKDF（HMAC-SHA256）による鍵導出
async function hkdf(ikm, info, length) {
    const salt = new TextEncoder().encode("WebPush"); // 任意の Salt
    const prk = await hmacSha256(salt, ikm); // PRK（Pseudo-Random Key）
    const okm = await hmacSha256(prk, new TextEncoder().encode(info)); // OKM（Output Key Material）
    return okm.slice(0, length);
}

// HMAC-SHA256 の計算
async function hmacSha256(key, data) {
    const cryptoKey = await crypto.subtle.importKey("raw", key, { name: "HMAC", hash: "SHA-256" }, false, ["sign"]);
    return await crypto.subtle.sign("HMAC", cryptoKey, data);
}

// Base64 ⇄ ArrayBuffer の変換
function base64ToArrayBuffer(base64) {
    const binaryString = atob(base64);
    const bytes = new Uint8Array(binaryString.length);
    for (let i = 0; i < binaryString.length; i++) bytes[i] = binaryString.charCodeAt(i);
    return bytes.buffer;
}

function arrayBufferToBase64(buffer) {
    const bytes = new Uint8Array(buffer);
    let binary = "";
    for (let i = 0; i < bytes.byteLength; i++) binary += String.fromCharCode(bytes[i]);
    return btoa(binary);
}
