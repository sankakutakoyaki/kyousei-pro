package kyousei.kyousei._Backup._push4.backup;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.nio.ByteBuffer;
import java.security.*;
import java.security.interfaces.*;
// import java.util.Base64;

public class WebPushEncryptor {

    public static byte[] encryptAES128GCM(
            byte[] plaintext,
            byte[] clientPublicKey,  // クライアントの65バイト公開鍵
            byte[] authSecret,       // 16バイトのauth secret
            KeyPair serverKeyPair,   // サーバ鍵ペア
            byte[] salt              // 16バイトランダムなsalt
    ) throws Exception {

        // 1. 共有鍵 = ECDH(client_pub, server_priv)
        PublicKey clientPub = loadUncompressedPublicKey(clientPublicKey);
        byte[] sharedSecret = deriveECDHSecret(serverKeyPair.getPrivate(), clientPub);

        // 2. PRK_key = HMAC(auth_secret, sharedSecret)
        byte[] prkKey = hmacSha256(authSecret, sharedSecret);

        // 3. key_info = "WebPush: info" || 0x00 || client_pub || server_pub
        byte[] serverPubBytes = getUncompressedPublicKey(serverKeyPair.getPublic());
        byte[] keyInfo = ByteBuffer.allocate("WebPush: info".length() + 1 + clientPublicKey.length + serverPubBytes.length)
                .put("WebPush: info".getBytes())
                .put((byte) 0x00)
                .put(clientPublicKey)
                .put(serverPubBytes)
                .array();

        // 4. IKM = HMAC(prkKey, key_info || 0x01)
        byte[] ikm = hmacSha256(prkKey, concat(keyInfo, (byte) 0x01));

        // 5. PRK = HMAC(salt, ikm)
        byte[] prk = hmacSha256(salt, ikm);

        // 6. CEK = HMAC(PRK, "Content-Encoding: aes128gcm" || 0x00 || 0x01).take(16)
        byte[] cek = hmacSha256(prk, concat("Content-Encoding: aes128gcm".getBytes(), (byte) 0x00, (byte) 0x01));
        cek = copyOf(cek, 16);

        // 7. NONCE = HMAC(PRK, "Content-Encoding: nonce" || 0x00 || 0x01).take(12)
        byte[] nonce = hmacSha256(prk, concat("Content-Encoding: nonce".getBytes(), (byte) 0x00, (byte) 0x01));
        nonce = copyOf(nonce, 12);

        // 8. AES-GCM 暗号化
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec cekKey = new SecretKeySpec(cek, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, cekKey, gcmSpec);
        return cipher.doFinal(plaintext);
    }

    private static byte[] hmacSha256(byte[] key, byte[] message) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(message);
    }

    private static byte[] concat(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] a : arrays) totalLength += a.length;
        byte[] result = new byte[totalLength];
        int pos = 0;
        for (byte[] a : arrays) {
            System.arraycopy(a, 0, result, pos, a.length);
            pos += a.length;
        }
        return result;
    }

    private static byte[] concat(byte[] array, byte b1, byte b2) {
        return concat(array, new byte[]{b1, b2});
    }

    private static byte[] concat(byte[] array, byte b) {
        return concat(array, new byte[]{b});
    }

    private static byte[] copyOf(byte[] src, int len) {
        byte[] result = new byte[len];
        System.arraycopy(src, 0, result, 0, len);
        return result;
    }

    private static byte[] deriveECDHSecret(PrivateKey priv, PublicKey pub) throws Exception {
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(priv);
        ka.doPhase(pub, true);
        return ka.generateSecret();
    }

    private static PublicKey loadUncompressedPublicKey(byte[] uncompressed) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("EC");
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("prime256v1");
        ECCurve curve = ecSpec.getCurve();
        ECPoint point = curve.decodePoint(uncompressed);
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);
        return kf.generatePublic(pubSpec);
    }

    public static byte[] getUncompressedPublicKey(PublicKey publicKey) {
        ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
        java.security.spec.ECPoint w = ecPublicKey.getW();
        byte[] x = ensureLength(w.getAffineX().toByteArray(), 32);
        byte[] y = ensureLength(w.getAffineY().toByteArray(), 32);
        return ByteBuffer.allocate(65).put((byte) 0x04).put(x).put(y).array();
    }

    private static byte[] ensureLength(byte[] src, int len) {
        if (src.length == len) return src;
        byte[] dst = new byte[len];
        System.arraycopy(src, Math.max(0, src.length - len), dst, len - Math.min(len, src.length), Math.min(len, src.length));
        return dst;
    }
}







// import javax.crypto.Cipher;
// import javax.crypto.KeyAgreement;
// import javax.crypto.Mac;
// import javax.crypto.SecretKey;
// import javax.crypto.spec.GCMParameterSpec;
// import javax.crypto.spec.SecretKeySpec;

// import org.bouncycastle.jce.provider.BouncyCastleProvider;
// import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

// import java.security.*;
// import java.security.interfaces.ECPublicKey;
// import java.security.spec.ECGenParameterSpec;
// import java.security.spec.X509EncodedKeySpec;
// import java.util.Base64;

// import org.bouncycastle.jce.ECNamedCurveTable;
// import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
// import org.bouncycastle.math.ec.ECPoint;
// import org.bouncycastle.jce.spec.ECPublicKeySpec;
// import org.bouncycastle.jce.provider.BouncyCastleProvider;
// import java.security.Security;

// public class WebPushEncryptor {
    
//     // private final static String base64UrlPrivateKey = ResourceBundle.getBundle("application").getString("vapid.privateKey"); // VAPID 秘密鍵
//     // private final static String base64UrlPublicKey = ResourceBundle.getBundle("application").getString("vapid.publicKey"); // VAPID 公開鍵
//     // private static final String base64Urlsubject = ResourceBundle.getBundle("application").getString("vapid.subject"); //
//     // private final static String audience = ResourceBundle.getBundle("application").getString("vapid.audience"); // origin

//     public static String text64String;
//     public static String salt64String;
//     public static String serverPublicKey64String;

//     static {
//         Security.addProvider(new BouncyCastleProvider());
//     }

//     public static void encrypt(SubscriptionRequest subscriptionRequest, String payload) throws Exception {
//         // クライアントから提供される値（例: Base64URLで受信）
//         String clientPublicKeyBase64 = subscriptionRequest.getP256dh(); // <Base64URL client public key (65 bytes with 0x04)>
//         String authSecretBase64 = subscriptionRequest.getAuth(); // <Base64URL 16 bytes auth secret>

//         byte[] clientPublicKeyBytes = Base64.getUrlDecoder().decode(clientPublicKeyBase64);
//         byte[] authSecret = Base64.getUrlDecoder().decode(authSecretBase64);

//         // 1. ECDH鍵ペア生成
//         KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
//         keyPairGenerator.initialize(new ECGenParameterSpec("secp256r1"));
//         KeyPair serverKeyPair = keyPairGenerator.generateKeyPair();
//         PublicKey serverPublicKey = serverKeyPair.getPublic();
//         serverPublicKey64String = Base64.getUrlEncoder().withoutPadding().encodeToString(getRaw(serverPublicKey));

//         // クライアントの公開鍵からPublicKey生成
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         X509EncodedKeySpec x509 = new X509EncodedKeySpec(convertRawToX509(clientPublicKeyBytes));
//         ECPublicKey clientPublicKey = (ECPublicKey) keyFactory.generatePublic(x509);

//         // 2. ECDH共有鍵生成
//         KeyAgreement agreement = KeyAgreement.getInstance("ECDH");
//         agreement.init(serverKeyPair.getPrivate());
//         agreement.doPhase(clientPublicKey, true);
//         byte[] sharedSecret = agreement.generateSecret();

//         // 3. salt生成（16バイト）
//         byte[] salt = new byte[16];
//         new SecureRandom().nextBytes(salt);
//         salt64String = Base64.getUrlEncoder().withoutPadding().encodeToString(salt);

//         // 4. HKDF Extract
//         byte[] prk = hkdfExtract(authSecret, sharedSecret);

//         // 5. HKDF Expand（CEK: 16バイト, nonce: 12バイト）
//         byte[] cek = hkdfExpand(prk, info("aes128gcm", clientPublicKeyBytes, serverPublicKey.getEncoded()), 16);
//         byte[] nonce = hkdfExpand(prk, info("nonce", clientPublicKeyBytes, serverPublicKey.getEncoded()), 12);

//         // 6. AES-GCM 暗号化
//         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//         GCMParameterSpec spec = new GCMParameterSpec(128, nonce);
//         SecretKey key = new SecretKeySpec(cek, "AES");
//         cipher.init(Cipher.ENCRYPT_MODE, key, spec);
//         byte[] ciphertext = cipher.doFinal(payload.getBytes());
//         text64String = Base64.getEncoder().encodeToString(ciphertext);

//         // 出力
//         System.out.println("暗号化ペイロード: " + Base64.getEncoder().encodeToString(ciphertext));
//         System.out.println("Encryptionヘッダー用salt: " + Base64.getUrlEncoder().withoutPadding().encodeToString(salt));
//         System.out.println("Crypto-Keyヘッダー用dh: " + Base64.getUrlEncoder().withoutPadding().encodeToString(getRaw(serverPublicKey)));
//     }

//     private static byte[] hkdfExtract(byte[] salt, byte[] ikm) throws Exception {
//         Mac mac = Mac.getInstance("HmacSHA256");
//         SecretKeySpec keySpec = new SecretKeySpec(salt, "HmacSHA256");
//         mac.init(keySpec);
//         return mac.doFinal(ikm);
//     }

//     private static byte[] hkdfExpand(byte[] prk, byte[] info, int length) throws Exception {
//         Mac mac = Mac.getInstance("HmacSHA256");
//         SecretKeySpec keySpec = new SecretKeySpec(prk, "HmacSHA256");
//         mac.init(keySpec);
//         mac.update(info);
//         mac.update((byte) 1);
//         byte[] result = mac.doFinal();
//         byte[] out = new byte[length];
//         System.arraycopy(result, 0, out, 0, length);
//         return out;
//     }

//     private static byte[] info(String type, byte[] clientPub, byte[] serverPub) {
//         return ("Content-Encoding: " + type + "\0").getBytes(); // 簡易版。実際にはclientPub/serverPubも使う
//     }

//     private static byte[] convertRawToX509(byte[] rawPublicKeyBytes) throws Exception {
//         if (rawPublicKeyBytes.length != 65 || rawPublicKeyBytes[0] != 0x04) {
//             throw new IllegalArgumentException("Invalid uncompressed public key format");
//         }

//         ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
//         ECPoint point = parameterSpec.getCurve().decodePoint(rawPublicKeyBytes);

//         KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
//         ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, parameterSpec);
//         PublicKey pubKey = keyFactory.generatePublic(pubSpec);
//         return pubKey.getEncoded(); // X.509 DER形式
//     }

//     private static byte[] getRaw(PublicKey pubKey) {
//         // 公開鍵をアンコンプレスト形式（0x04付き）で取得（省略可）
//         return pubKey.getEncoded(); // ただしこれはSubjectPublicKeyInfo形式。要変換
//     }
// }
