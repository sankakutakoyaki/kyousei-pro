package kyousei.kyousei._Backup._push4.backup;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    /** AES */
	public static final String AES = "AES";
	/** AES/GCM/NoPadding */
	public static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
	/** GCMのIVの長さ */
	public static final int GCM_IV_LENGTH = 12;
	/** AESのキーの長さ ( 128 or 192 or 256 ) */
	public static final int AES_KEY_SIZE = 256;
	// /** GCMのタグの長さ ( 96 or 104 or 112 or 120 or 128 ) */
	// private static final int GCM_TAG_LENGTH = 128;

    /**
     * 共通鍵 (CEK) を生成
     * @param serverPrivateKey
     * @param clientPublicKey
     * @return
     * @throws Exception
     */    
    public static byte[] generateSharedSecret(PrivateKey serverPrivateKey, PublicKey clientPublicKey) throws Exception {
        KeyAgreement keyAgree = KeyAgreement.getInstance("ECDH");
        keyAgree.init(serverPrivateKey);
        keyAgree.doPhase(clientPublicKey, true);
        return keyAgree.generateSecret();
    }

    /**
     * Simplified HKDF: HMAC-SHA256 based key derivation
     * @param salt the salt (byte[])
     * @param ikm input keying material (byte[])
     * @param info optional context and application specific info (byte[])
     * @param length desired length in bytes (up to 32)
     * @return derived key (byte[])
     * @throws Exception if HMAC fails
     */
    public static byte[] hkdf(byte[] salt, byte[] ikm, byte[] info, int length) throws Exception {
        if (length > 32) {
            throw new IllegalArgumentException("Length must be 32 bytes or less");
        }

        // Extract phase
        byte[] prk = hmacSha256(salt, ikm);

        // Expand phase
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec prkKey = new SecretKeySpec(prk, "HmacSHA256");
        hmac.init(prkKey);

        hmac.update(info);
        hmac.update(new byte[]{0x01});  // ONE_BUFFER equivalent

        byte[] okm = hmac.doFinal();
        return Arrays.copyOfRange(okm, 0, length);
    }

    private static byte[] hmacSha256(byte[] key, byte[] data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
        mac.init(keySpec);
        return mac.doFinal(data);
    }


    /**
     * 共通鍵生成に使う「Context」のバッファを生成
     * @param subscriptionPubKeyBase64
     * @param localPublicKey
     * @return
     */
    public static byte[] createContext(String subscriptionPubKeyBase64, byte[] localPublicKey) {
        // keyLabel: "P-256\0" in UTF-8
        byte[] keyLabel = "P-256\u0000".getBytes(StandardCharsets.UTF_8);

        // Decode subscription public key from Base64 (URL-safe if needed)
        byte[] subscriptionPubKey = Base64.getUrlDecoder().decode(subscriptionPubKeyBase64);

        // Lengths as 2-byte unsigned integers (big-endian)
        byte[] subscriptionPubKeyLength = ByteBuffer.allocate(2).putShort((short) subscriptionPubKey.length).array();
        byte[] localPublicKeyLength = ByteBuffer.allocate(2).putShort((short) localPublicKey.length).array();

        // Combine all parts into one buffer
        ByteBuffer contextBuffer = ByteBuffer.allocate(
                keyLabel.length +
                2 + subscriptionPubKey.length +
                2 + localPublicKey.length
        );

        contextBuffer.put(keyLabel);
        contextBuffer.put(subscriptionPubKeyLength);
        contextBuffer.put(subscriptionPubKey);
        contextBuffer.put(localPublicKeyLength);
        contextBuffer.put(localPublicKey);

        return contextBuffer.array();
    }

    /**
     * HKDF を実行し、32バイトの PRK（派生鍵）を取得
     * @param authBase64
     * @param sharedSecret
     * @return
     * @throws Exception
     */
    public static byte[] derivePRK(String authBase64, byte[] sharedSecret) throws Exception {
        // ① auth (Base64) をデコード
        byte[] authSecret = Base64.getDecoder().decode(authBase64);

        // ② info バッファ: "Content-Encoding: auth\0"
        byte[] authInfo = "Content-Encoding: auth\u0000".getBytes(StandardCharsets.UTF_8);

        // ③ PRK を導出
        return hkdf(authSecret, sharedSecret, authInfo, 32);
    }

    /**
     * 
     * @param contextBuffer
     * @return
     */
    public static byte[] createAesInfo(byte[] contextBuffer) {
        // "Content-Encoding: aes128gcm\0"
        byte[] nonceEncBuffer = "Content-Encoding: aes128gcm\u0000".getBytes(StandardCharsets.UTF_8);
        return concat(nonceEncBuffer, contextBuffer);
    }

    /**
     * 
     * @param contextBuffer
     * @return
     */
    public static byte[] createNonceInfo(byte[] contextBuffer) {
        // "Content-Encoding: nonce\0"
        byte[] nonceEncBuffer = "Content-Encoding: nonce\u0000".getBytes(StandardCharsets.UTF_8);
        return concat(nonceEncBuffer, contextBuffer);
    }

    /**
     * 
     * @param contextBuffer
     * @return
     */
    public static byte[] createCEKInfo(byte[] contextBuffer) {
        // "Content-Encoding: aesgcm\0"
        byte[] cekEncBuffer = "Content-Encoding: aesgcm\u0000".getBytes(StandardCharsets.UTF_8);
        return concat(cekEncBuffer, contextBuffer);
    }

    /**
     * 
     * @param a
     * @param b
     * @return
     */
    private static byte[] concat(byte[] a, byte[] b) {
        ByteBuffer buffer = ByteBuffer.allocate(a.length + b.length);
        buffer.put(a);
        buffer.put(b);
        return buffer.array();
    }

    /**
     * Derives the CEK (Content Encryption Key) and Nonce using HKDF.
     *
     * @param prk            The pseudo-random key (from previous HKDF)
     * @param contextBuffer  The context buffer
     * @return A result object containing cek and nonce
     * @throws Exception if HKDF fails
     */
    public static DerivedKeys deriveKeys(byte[] prk, byte[] contextBuffer) throws Exception {
        // Info strings
        byte[] cekInfo = createCEKInfo(contextBuffer);       // "Content-Encoding: aesgcm\0" + context
        byte[] nonceInfo = createNonceInfo(contextBuffer);   // "Content-Encoding: nonce\0" + context

        // Use PRK as the salt in HKDF for simplicity (as per RFC8291)
        byte[] cek = hkdf(prk, new byte[0], cekInfo, 16);            // CEK: 128-bit (16 bytes)
        byte[] nonce = hkdf(prk, new byte[0], nonceInfo, 12);        // Nonce: 96-bit (12 bytes)

        return new DerivedKeys(cek, nonce);
    }

    // POJO class to hold derived keys
    public static class DerivedKeys {
        public final byte[] cek;
        public final byte[] nonce;

        public DerivedKeys(byte[] cek, byte[] nonce) {
            this.cek = cek;
            this.nonce = nonce;
        }
    }

    /**
     * AES-GCMで暗号化
     *
     * @param plaintext 平文バイト列
     * @param cek       コンテンツ暗号鍵 (16バイト)
     * @param nonce     ノンス (12バイト)
     * @return 暗号化されたバイト列（暗号文 + 認証タグ）
     * @throws Exception エラー時
     */
    public static byte[] encrypt(byte[] plaintext, byte[] cek, byte[] nonce) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        SecretKeySpec keySpec = new SecretKeySpec(cek, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, nonce); // 認証タグ 128ビット

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
        return cipher.doFinal(plaintext);
    }

    /**
     * AES-GCM 復号
     * @param encryptedData
     * @param aesKey
     * @param nonce
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] aesKey, byte[] nonce) throws Exception {
        final int GCM_TAG_LENGTH = 128; // GCM のタグ長（ビット）

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, nonce);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

        return cipher.doFinal(encryptedData);
    }

    public static byte[] encodeUncompressedPoint(ECPublicKey publicKey) {
        ECPoint point = publicKey.getW();
        BigInteger x = point.getAffineX();
        BigInteger y = point.getAffineY();

        // X座標とY座標を32バイトに調整
        byte[] xBytes = toFixedByteArray(x, 32);
        byte[] yBytes = toFixedByteArray(y, 32);

        // 65バイトの非圧縮形式に変換
        ByteBuffer buffer = ByteBuffer.allocate(65);
        buffer.put((byte) 0x04);  // 非圧縮形式の識別バイト
        buffer.put(xBytes);
        buffer.put(yBytes);

        return buffer.array();
    }

    // BigIntegerを指定バイト長に調整
    private static byte[] toFixedByteArray(BigInteger bigInteger, int byteLength) {
        byte[] byteArray = bigInteger.toByteArray();

        // 最上位の符号付きバイトを削除する処理
        if (byteArray[0] == 0) {
            byte[] temp = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, temp, 0, byteArray.length - 1);
            byteArray = temp;
        }

        // 32バイトに調整
        if (byteArray.length > byteLength) {
            // 余分なバイトを削除
            byte[] adjustedArray = new byte[byteLength];
            System.arraycopy(byteArray, byteArray.length - byteLength, adjustedArray, 0, byteLength);
            return adjustedArray;
        } else if (byteArray.length < byteLength) {
            // 不足している場合、ゼロパディング
            byte[] adjustedArray = new byte[byteLength];
            System.arraycopy(byteArray, 0, adjustedArray, byteLength - byteArray.length, byteArray.length);
            return adjustedArray;
        } else {
            return byteArray;
        }
    }

    // EC公開鍵を65バイトの非圧縮形式にエンコード
    public static byte[] getEncodedPublicKey(KeyPair keyPair) {
        // ECPublicKeyを取得
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        return encodeUncompressedPoint(publicKey);
    }

    // HKDF-Extract: PRK を生成 (HMAC を salt で初期化)
    public static byte[] hkdfExtract(byte[] salt, byte[] ikm) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(salt, "HmacSHA256"));
        return mac.doFinal(ikm);
    }
}