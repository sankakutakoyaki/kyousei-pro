async function encryptData(aesKey, plaintext) {
    const iv = crypto.getRandomValues(new Uint8Array(12)); // 12バイトのIV（推奨）
    const encoder = new TextEncoder();
    const data = encoder.encode(plaintext);

    const key = await crypto.subtle.importKey("raw", aesKey, { name: "AES-GCM" }, false, ["encrypt"]);
    const ciphertext = await crypto.subtle.encrypt({ name: "AES-GCM", iv }, key, data);

    return {
        iv: arrayBufferToBase64(iv),
        ciphertext: arrayBufferToBase64(ciphertext),
    };
}

async function decryptData(aesKey, ivBase64, ciphertextBase64) {
    const iv = base64ToArrayBuffer(ivBase64);
    const ciphertext = base64ToArrayBuffer(ciphertextBase64);

    const key = await crypto.subtle.importKey("raw", aesKey, { name: "AES-GCM" }, false, ["decrypt"]);
    const decryptedData = await crypto.subtle.decrypt({ name: "AES-GCM", iv }, key, ciphertext);

    return new TextDecoder().decode(decryptedData);
}
