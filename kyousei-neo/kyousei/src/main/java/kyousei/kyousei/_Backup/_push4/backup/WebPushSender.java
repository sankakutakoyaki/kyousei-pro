package kyousei.kyousei._Backup._push4.backup;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
// import java.util.ResourceBundle;

public class WebPushSender {


    // private final static String base64UrlPrivateKey = ResourceBundle.getBundle("application").getString("vapid.privateKey"); // VAPID 秘密鍵
    // private final static String base64UrlPublicKey = ResourceBundle.getBundle("application").getString("vapid.publicKey"); // VAPID 公開鍵
    // private static final String base64Urlsubject = ResourceBundle.getBundle("application").getString("vapid.subject"); //
    // private final static String audience = ResourceBundle.getBundle("application").getString("vapid.audience"); // origin
   
    public static void sendWebPush(byte[] encryptedPayload, byte[] salt, byte[] serverPubKeyX509, String endpoint, String vapidHeader) throws Exception {
        // Base64URL encoding（ヘッダー用）
        String saltB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(salt);
        String dhB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(serverPubKeyX509);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(10))
                .header("TTL", "60")
                .header("Content-Encoding", "aes128gcm")
                .header("Content-Type", "application/octet-stream")
                .header("Content-Length", String.valueOf(encryptedPayload.length))
                .header("Encryption", "salt=" + saltB64)
                .header("Crypto-Key", "dh=" + dhB64 + "; p256ecdsa=" + vapidHeader)
                .POST(HttpRequest.BodyPublishers.ofByteArray(encryptedPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
    }
}

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.math.BigInteger;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.nio.ByteBuffer;
// import java.nio.charset.StandardCharsets;
// import java.security.KeyFactory;
// import java.security.KeyPair;
// import java.security.KeyPairGenerator;
// import java.security.PublicKey;
// import java.security.SecureRandom;
// import java.security.interfaces.ECPrivateKey;
// import java.security.interfaces.ECPublicKey;
// import java.security.spec.ECGenParameterSpec;
// import java.security.spec.ECPoint;
// import java.security.spec.ECPublicKeySpec;
// import java.util.Arrays;
// import java.util.Base64;
// import java.util.ResourceBundle;

// import javax.crypto.Cipher;
// import javax.crypto.KeyAgreement;
// import javax.crypto.spec.GCMParameterSpec;
// import javax.crypto.spec.SecretKeySpec;

// import org.bouncycastle.crypto.digests.SHA256Digest;
// import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
// import org.bouncycastle.crypto.params.HKDFParameters;
// import org.springframework.security.crypto.codec.Hex;
// import org.springframework.security.crypto.encrypt.Encryptors;
// import org.springframework.security.crypto.encrypt.TextEncryptor;

// import kyousei.kyousei.push.Encryption.DerivedKeys;
// import kyousei.kyousei.push.WebPushEncryptor.EncryptionResult;

// public class WebPushSender {

//     private final static String base64UrlPrivateKey = ResourceBundle.getBundle("application").getString("vapid.privateKey"); // VAPID 秘密鍵
//     private final static String base64UrlPublicKey = ResourceBundle.getBundle("application").getString("vapid.publicKey"); // VAPID 公開鍵
//     private static final String base64Urlsubject = ResourceBundle.getBundle("application").getString("vapid.subject"); //
//     private final static String audience = ResourceBundle.getBundle("application").getString("vapid.audience"); // origin

//     // /**
//     //  * メッセージを暗号化してクライアントへPUSH送信
//     //  * @param endpoint
//     //  * @param subscriptionRequest
//     //  * @param message
//     //  * @throws Exception
//     //  */
//     // public static void sendPushNotification(String endpoint, SubscriptionRequest subscriptionRequest, String message) throws Exception {
//     //     // 秘密鍵 & 公開鍵の作成
//     //     ECPrivateKey privateKey = (ECPrivateKey)KeyUtil.convertRawToECPrivateKey(Base64.getUrlDecoder().decode(base64UrlPrivateKey));
//     //     PublicKey publicKey = KeyUtil.convertRawToECPublicKey(Base64.getUrlDecoder().decode(base64UrlPublicKey));

//     //     // クライアントの公開鍵をデコード
//     //     ECPublicKey clientPublicKey = (ECPublicKey)KeyUtil.convertRawToECPublicKey(Base64.getUrlDecoder().decode(subscriptionRequest.getP256dh()));

//     //     // JWT 署名の作成
//     //     String jwt = JwtUtil.createES256JWT((ECPrivateKey) privateKey, (ECPublicKey) publicKey, audience, base64Urlsubject);

//     //     // JWT 署名の検証
//     //     JwtUtil.verifierJWT((ECPublicKey)publicKey, audience, jwt);

//     //     // saltは任意でランダムな16バイト
//     //     // byte[] salt = new byte[16];
//     //     // new SecureRandom().nextBytes(salt);  // ランダムなsalt

//     //     // // 共通鍵 (CEK) の生成 (ECDH 鍵交換)
//     //     // KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
//     //     // ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1"); // = P-256
//     //     // keyPairGenerator.initialize(ecSpec);
//     //     // KeyPair serverKeyPair = keyPairGenerator.generateKeyPair();
//     //     // byte[] sharedSecret = Encryption.generateSharedSecret(serverKeyPair.getPrivate(), clientPublicKey);

//     //     // String subscriptionPubKeyBase64 = subscriptionRequest.getP256dh();
//     //     // byte[] contextBuffer = Encryption.createContext(subscriptionPubKeyBase64, sharedSecret);

//     //     // // PRK を導出（salt + IKM）
//     //     // byte[] prk = Encryption.hkdfExtract(salt, sharedSecret);

//     //     // // auth info を入れて CEK/Nonce 導出
//     //     // byte[] info = Encryption.createAesInfo(contextBuffer);
//     //     // DerivedKeys keys = Encryption.deriveKeys(prk, info);

//     //     // byte[] plaintext = message.getBytes(StandardCharsets.UTF_8);
        
//     //     // ByteBuffer buffer = ByteBuffer.allocate(2 + plaintext.length);
//     //     // buffer.putShort((short) 0); // padding length = 0
//     //     // buffer.put(plaintext);
//     //     // byte[] paddedPlaintext = buffer.array();

//     //     // // すでに導出済みの CEK と Nonce を使う
//     //     // byte[] encrypted = Encryption.encrypt(paddedPlaintext, keys.cek, keys.nonce);

//     //     // // 公開鍵を非圧縮形式で取得
//     //     // byte[] uncompressedKey = KeyUtil.encodeUncompressedPoint((ECPublicKey) serverKeyPair.getPublic());
//     //     // String dhBase64Url = Base64.getUrlEncoder().withoutPadding().encodeToString(uncompressedKey);

//     //     // // 復号して確認
//     //     // byte[] decryptedData = Encryption.decrypt(encrypted, keys.cek, keys.nonce);
//     //     // Base64URL で送られてきたキー

//     //     String p256dhBase64 = subscriptionRequest.getP256dh();
//     //     String authBase64 = subscriptionRequest.getAuth();

//     //     // Javaでデコード（Base64URL）
//     //     Base64.Decoder decoder = Base64.getUrlDecoder();
//     //     byte[] userPublicKeyBytes = decoder.decode(p256dhBase64); // => 長さ65バイト（EC uncompressed point）
//     //     byte[] userAuthSecretBytes = decoder.decode(authBase64);  // => 長さ16バイト

//     //         // ✅ 0x04 を除いて 64バイトにする
//     //     if (userPublicKeyBytes.length != 65 || userPublicKeyBytes[0] != 0x04) {
//     //         throw new IllegalArgumentException("Invalid uncompressed public key");
//     //     }
//     //     byte[] rawUserPublicKey = Arrays.copyOfRange(userPublicKeyBytes, 1, 65);

//     //     WebPushEncryptor encryptor = new WebPushEncryptor();
//     //     WebPushEncryptor.EncryptionResult encrypted = encryptor.encrypt(
//     //         "こんにちは世界".getBytes(StandardCharsets.UTF_8),
//     //         rawUserPublicKey,  // クライアントの公開鍵（raw 64 bytes）
//     //         userAuthSecretBytes  // クライアントの auth secret（16 bytes）
//     //     );
        
//     //     System.out.println("Ciphertext (Base64URL): " + WebPushEncryptor.base64url(encrypted.ciphertext));
//     //     System.out.println("Salt (Base64URL): " + WebPushEncryptor.base64url(encrypted.salt));
//     //     System.out.println("ServerPublicKey (Base64URL): " + WebPushEncryptor.base64url(encrypted.serverPublicKey));

//     //     sendPushNotification(
//     //         encrypted,
//     //         encrypted.salt,
//     //         endpoint,
//     //         audience,
//     //         jwt,
//     //         encrypted.serverPublicKey,
//     //         base64UrlPublicKey
//     //     );

//     //     // System.out.println("生成された JWT: " + jwt);
//     //     // System.out.println("共通鍵: " + Base64.getUrlEncoder().encodeToString(sharedSecret));
                
//     //     // System.out.println("CEK: " + Base64.getEncoder().encodeToString(keys.cek));
//     //     // System.out.println("Nonce: " + Base64.getEncoder().encodeToString(keys.nonce));

//     //     // System.out.println("公開鍵(Base64URL): " + dhBase64Url);

//     //     // System.out.println("dh=" + dhBase64Url + ": 長さ＝" + dhBase64Url.length());
//     //     // System.out.println("p256ecdsa=" + base64UrlPublicKey);

//     //     // System.out.println("暗号化したメッセージ: " + Base64.getEncoder().encodeToString(encrypted));
//     //     // System.out.println("暗号化前のメッセージ: " + message);
//     //     // System.out.println("復号されたメッセージ: " + new String(decryptedData));
//     // }


    
//     public void sendEncryptedPushNotification(
//         // String endpoint,
//         // byte[] userPublicKey64, // 64バイト（X+Y）
//         // byte[] userAuthSecret,  // 16バイト
//         // String vapidPrivateKeyBase64Url,
//         // String vapidPublicKeyBase64Url,
//         SubscriptionRequest subscriptionRequest,
//         String payload
//     ) throws Exception {

//         // Javaでデコード（Base64URL）
//         Base64.Decoder decoder = Base64.getUrlDecoder();
//         byte[] userPublicKeyBytes = decoder.decode(subscriptionRequest.getP256dh()); // => 長さ65バイト（EC uncompressed point）
//         byte[] userAuthSecretBytes = decoder.decode(subscriptionRequest.getAuth());  // => 長さ16バイト

//             // ✅ 0x04 を除いて 64バイトにする
//         if (userPublicKeyBytes.length != 65 || userPublicKeyBytes[0] != 0x04) {
//             throw new IllegalArgumentException("Invalid uncompressed public key");
//         }
//         byte[] userPublicKey64 = Arrays.copyOfRange(userPublicKeyBytes, 1, 65);

//         // 秘密鍵 & 公開鍵の作成
//         ECPrivateKey privateKey = (ECPrivateKey)KeyUtil.convertRawToECPrivateKey(Base64.getUrlDecoder().decode(base64UrlPrivateKey));
//         PublicKey publicKey = KeyUtil.convertRawToECPublicKey(Base64.getUrlDecoder().decode(base64UrlPublicKey));

//         // JWT 署名の作成
//         String jwt = JwtUtil.createES256JWT((ECPrivateKey) privateKey, (ECPublicKey) publicKey, audience, base64Urlsubject);

//         // // JWT 署名の検証
//         // JwtUtil.verifierJWT((ECPublicKey)publicKey, audience, jwt);



//         // 1. ECDH鍵ペア（サーバー用）を生成
//         KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
//         keyGen.initialize(new ECGenParameterSpec("secp256r1"));
//         KeyPair serverKeyPair = keyGen.generateKeyPair();

//         // 2. クライアントの公開鍵を復元
//         ECPoint clientPoint = new ECPoint(
//             new BigInteger(1, Arrays.copyOfRange(userPublicKey64, 0, 32)),
//             new BigInteger(1, Arrays.copyOfRange(userPublicKey64, 32, 64))
//         );
//         KeyFactory kf = KeyFactory.getInstance("EC");
//         ECPublicKey clientPublicKey = (ECPublicKey) kf.generatePublic(new ECPublicKeySpec(
//             clientPoint, ((ECPublicKey) serverKeyPair.getPublic()).getParams()
//         ));

//         // 3. 共通鍵を生成 (ECDH)
//         KeyAgreement agreement = KeyAgreement.getInstance("ECDH");
//         agreement.init(serverKeyPair.getPrivate());
//         agreement.doPhase(clientPublicKey, true);
//         byte[] sharedSecret = agreement.generateSecret();

//         // 4. saltを生成（16バイト）
//         byte[] salt = new byte[16];
//         new SecureRandom().nextBytes(salt);

//         // 5. PRK = HKDF(auth, sharedSecret)
//         HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
//         hkdf.init(new HKDFParameters(sharedSecret, userAuthSecretBytes, null));
//         byte[] prk = new byte[32];
//         hkdf.generateBytes(prk, 0, 32);

//         // 6. コンテキスト生成 (RFC 8291準拠)
//         byte[] context = createContext(userPublicKey64, serverKeyPair.getPublic());

//         // 7. 鍵導出 (CEK / NONCE)
//         byte[] cek = hkdfExpand(prk, "Content-Encoding: aes128gcm\0", context, 16);
//         byte[] nonce = hkdfExpand(prk, "Content-Encoding: nonce\0", context, 12);

//         // 8. AES-GCM で暗号化
//         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//         GCMParameterSpec spec = new GCMParameterSpec(128, nonce);
//         SecretKeySpec keySpec = new SecretKeySpec(cek, "AES");
//         cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
//         byte[] encrypted = cipher.doFinal(payload.getBytes(StandardCharsets.UTF_8));

//         String saltBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(salt);

//         // 10. HTTPリクエストを送信
//         HttpRequest request = HttpRequest.newBuilder()
//             .uri(URI.create(subscriptionRequest.getEndpoint()))
//             .header("Content-Encoding", "aes128gcm")
//             .header("TTL", "48600") // 
//             .header("Authorization", "WebPush " + jwt) // VAPIDのJWT
//             .header("Content-Encoding", "aes128gcm")
//             // .header("Crypto-Key", "dh=" + dhBase64Url + ";p256ecdsa=" + vapidPublicKeyBase64Url)
//             .header("Crypto-Key", "p256ecdsa=" + base64UrlPublicKey)
//             .header("TTL", "43200") // オプション（例：12時間）
//             .header("Content-Type","application/octet-stream")
//             .header("Encryption", "salt=" + saltBase64)  // salt情報
//             .POST(HttpRequest.BodyPublishers.ofByteArray(encrypted))
//             .build();

//         HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.discarding());

//         // HttpClientでリクエストを送信
//         HttpClient client = HttpClient.newHttpClient();
//         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//         // 応答の確認
//         System.out.println("Response status: " + response.statusCode());
//         System.out.println("Response body: " + response.body());
//     }

//     private byte[] createContext(byte[] userPublicKey64, PublicKey serverPublicKey) throws Exception {
//         byte[] serverPubKey64 = ((ECPublicKey) serverPublicKey).getW().getAffineX().toByteArray(); // 32 or 33 bytes
//         // RFC 8291: length-prefixed keys
//         ByteArrayOutputStream out = new ByteArrayOutputStream();
//         out.write(0); // reserved
//         out.write(0x41); // label
//         out.write("P-256".getBytes(StandardCharsets.US_ASCII));
//         out.write(0); out.write(0x00); // length 0
//         out.write((byte) 0x40); // length of client pubkey
//         out.write(userPublicKey64);
//         out.write((byte) 0x40); // length of server pubkey
//         out.write(serverPubKey64);
//         return out.toByteArray();
//     }

//     public byte[] hkdfExpand(byte[] prk, String info, byte[] context, int length) {
//         HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
    
//         // info + context + 0x01
//         ByteArrayOutputStream infoBuf = new ByteArrayOutputStream();
//         try {
//             infoBuf.write(info.getBytes(StandardCharsets.UTF_8));
//             infoBuf.write(context);
//             infoBuf.write(0x01); // HKDF expansion block index
//         } catch (IOException e) {
//             throw new RuntimeException(e);
//         }
    
//         hkdf.init(new HKDFParameters(prk, null, infoBuf.toByteArray()));
//         byte[] output = new byte[length];
//         hkdf.generateBytes(output, 0, length);
//         return output;
//     }
    




//     /**
//      * Web Push 通知を送信
//      * @param encryptedPayload
//      * @param salt
//      * @param endpoint
//      * @param audience
//      * @param jwt
//      * @param localPublicKey
//      * @param vapidPublicKeyBase64Url
//      * @throws Exception
//      */
//     public static void sendPushNotification(
//             EncryptionResult encryptedPayload,
//             byte[] salt,
//             String endpoint,
//             String audience,
//             String jwt,
//             byte[] localPublicKey,
//             // String localPublicKey,
//             String vapidPublicKeyBase64Url
//     ) throws Exception {

//         String saltBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(salt);
//         String dhBase64Url = Base64.getUrlEncoder().withoutPadding().encodeToString(localPublicKey);
//         // System.out.println("salt: " + saltBase64);

//         // Web Push通知を送信するためのHttpRequestの設定
//         HttpRequest request = HttpRequest.newBuilder()
//             .uri(URI.create(endpoint))
//             .header("Authorization", "WebPush " + jwt) // VAPIDのJWT
//             .header("Content-Encoding", "aes128gcm")
//             .header("Crypto-Key", "dh=" + dhBase64Url + ";p256ecdsa=" + vapidPublicKeyBase64Url)
//             .header("TTL", "43200") // オプション（例：12時間）
//             .header("Content-Type","application/octet-stream")
//             .header("Encryption", "salt=" + saltBase64)  // salt情報
//             .POST(HttpRequest.BodyPublishers.ofByteArray(encryptedPayload.ciphertext)) // 暗号化されたメッセージ
//             .build();

//         // HttpClientでリクエストを送信
//         HttpClient client = HttpClient.newHttpClient();
//         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//         // 応答の確認
//         System.out.println("Response status: " + response.statusCode());
//         System.out.println("Response body: " + response.body());
//     }
// }






// import org.bouncycastle.jce.provider.BouncyCastleProvider;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import jakarta.annotation.PostConstruct;

// import javax.crypto.KeyAgreement;
// import javax.crypto.spec.SecretKeySpec;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.security.*;
// import java.security.interfaces.ECPrivateKey;
// import java.security.interfaces.ECPublicKey;
// import java.security.spec.*;
// import java.util.Base64;
// import java.util.HashMap;
// import java.util.Map;

// @Service
// public class WebPushService {

//     @Value("${vapid.publicKey}")
//     private String vapidPublicKeyBase64Url;

//     @Value("${vapid.privateKey}")
//     private String vapidPrivateKeyBase64Url;

//     @Value("${vapid.subject}")
//     private String vapidSubject;

//     @PostConstruct
//     public void setup() {
//         Security.addProvider(new BouncyCastleProvider());
//     }

//     public void sendPushMessage(SubscriptionRequest sub, String payload) throws Exception {
//         // --- Decode VAPID keys ---
//         byte[] privateKeyBytes = Base64.getUrlDecoder().decode(vapidPrivateKeyBase64Url);
//         byte[] publicKeyBytes = Base64.getUrlDecoder().decode(vapidPublicKeyBase64Url);

//         // --- Decode Subscription keys ---
//         byte[] userPublicKey = Base64.getUrlDecoder().decode(sub.getP256dh());
//         byte[] userAuth = Base64.getUrlDecoder().decode(sub.getAuth());

//         // --- Generate ECDH key pair ---
//         KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
//         keyGen.initialize(new ECGenParameterSpec("secp256r1"));
//         KeyPair serverKeyPair = keyGen.generateKeyPair();

//         // --- ECDH shared secret ---
//         KeyFactory kf = KeyFactory.getInstance("EC");
//         EncodedKeySpec userKeySpec = new X509EncodedKeySpec(deriveUncompressedKey(userPublicKey));
//         PublicKey userPubKey = kf.generatePublic(userKeySpec);

//         KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
//         keyAgreement.init(serverKeyPair.getPrivate());
//         keyAgreement.doPhase(userPubKey, true);
//         byte[] sharedSecret = keyAgreement.generateSecret();

//         // --- Use HKDF to derive AES-GCM key (simplified for brevity) ---
//         byte[] aesKey = hkdf(sharedSecret, userAuth);

//         // --- Encrypt payload ---
//         byte[] encrypted = aesGcmEncrypt(aesKey, payload.getBytes());

//         // --- Create JWT (VAPID) ---
//         String jwt = createVapidJwt(new URI(sub.getEndpoint()), vapidSubject, privateKeyBytes);

//         // --- Send HTTP request ---
//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(sub.getEndpoint()))
//                 .header("Authorization", "WebPush " + jwt)
//                 .header("Content-Encoding", "aes128gcm")
//                 .header("TTL", "180")
//                 .POST(HttpRequest.BodyPublishers.ofByteArray(encrypted))
//                 .build();

//         HttpClient client = HttpClient.newHttpClient();
//         HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());

//         if (response.statusCode() != 201) {
//             throw new RuntimeException("Push failed: " + response.statusCode());
//         }
//     }

//     private byte[] deriveUncompressedKey(byte[] key) {
//         byte[] uncompressed = new byte[65];
//         uncompressed[0] = 0x04;
//         System.arraycopy(key, 0, uncompressed, 1, 64);
//         return uncompressed;
//     }

//     private byte[] hkdf(byte[] ikm, byte[] salt) {
//         // 実際のHKDFロジックをここに実装 (簡略化 or ライブラリ使用)
//         byte[] dummyKey = new byte[16]; // ダミー（実際にはHKDFを正しく実装）
//         new SecureRandom().nextBytes(dummyKey);
//         return dummyKey;
//     }

//     private byte[] aesGcmEncrypt(byte[] key, byte[] plaintext) {
//         // 本来はAES/GCM暗号化を実装。ここではダミーを返す
//         return plaintext;
//     }

//     private String createVapidJwt(URI endpoint, String subject, byte[] privateKeyBytes) throws Exception {
//         long now = System.currentTimeMillis() / 1000L;
//         long exp = now + 12 * 60 * 60;

//         String header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"ES256\",\"typ\":\"JWT\"}".getBytes());
//         String payload = String.format("{\"aud\":\"%s\",\"exp\":%d,\"sub\":\"%s\"}", endpoint.getScheme() + "://" + endpoint.getHost(), exp, subject);
//         String payloadEnc = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());

//         String data = header + "." + payloadEnc;

//         Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
//         KeyFactory kf = KeyFactory.getInstance("EC");
//         ECPrivateKey privateKey = (ECPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(encodePkcs8(privateKeyBytes)));
//         ecdsaSign.initSign(privateKey);
//         ecdsaSign.update(data.getBytes());
//         byte[] signature = ecdsaSign.sign();

//         return data + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
//     }

//     private byte[] encodePkcs8(byte[] keyBytes) {
//         // PKCS#8エンコードにする必要あり。Base64URL形式の鍵ではなく本来のDER形式が必要。
//         // 適切な方法で変換してください（PEM -> DERなど）
//         throw new UnsupportedOperationException("PKCS8エンコードが必要です。PEM形式を使用するか、KeyPairを保存してください。");
//     }
// }


    //     /**
    //  * JWT署名を作成
    //  * @param message
    //  * @param csrftoken
    //  * @return
    //  * @throws Exception
    //  */
    // @PostMapping("/send")
    // public void sendPush(@RequestParam String message, @RequestParam String csrftoken) throws Exception {

    //     List<IEntity> list = pushRepository.getList();
    //     for (IEntity entity : list) {
    //         // // 秘密鍵 & 公開鍵の作成
    //         // PrivateKey privateKey = KeyUtil.convertRawToECPrivateKey(Base64.getUrlDecoder().decode(base64UrlPrivateKey));
    //         // PublicKey publicKey = KeyUtil.convertRawToECPublicKey(Base64.getUrlDecoder().decode(base64UrlPublicKey));

    //         // // JWT 署名の作成
    //         // String jwt = JwtUtil.createES256JWT((ECPrivateKey) privateKey, (ECPublicKey) publicKey, audience, base64Urlsubject);
    //         // System.out.println("生成された JWT: " + jwt);

    //         // // JWT 署名の検証
    //         // JWTVerifier verifier = JWT.require(Algorithm.ECDSA256((ECPublicKey) publicKey, null))
    //         //         .withAudience(audience)
    //         //         .build();
    //         // DecodedJWT decodedJWT = verifier.verify(jwt);
    //         // System.out.println("JWT 署名検証 成功！ペイロード: " + decodedJWT.getPayload());

    //         // WebPushSender.sendPushNotification(((SubscriptionRequest)entity).getEndpoint(), ((SubscriptionRequest)entity), message);
    //         // WebPushSend.send((SubscriptionRequest)entity, message);
    //         // WebPushEncryptor webPushEncryptor = new WebPushEncryptor();
    //         // WebPushEncryptor.encrypt((SubscriptionRequest)entity, message);
    //         // System.out.println("暗号化ペイロード: " + WebPushEncryptor.text64String);
    //         // System.out.println("Encryptionヘッダー用salt: " + WebPushEncryptor.salt64String);
    //         // System.out.println("Crypto-Keyヘッダー用dh: " + WebPushEncryptor.serverPublicKey64String);

    //         // WebPushSender.sendWebPush(WebPushEncryptor.text64String, WebPushEncryptor.salt64String, WebPushEncryptor.serverPublicKey64String, message, csrftoken);
    //         // // encryptedPayload: AES-GCM 暗号化済みのペイロード
    //         // // salt: 16バイトのランダムな salt
    //         // // serverPubKeyX509: サーバー公開鍵（X.509形式）
    //         // // endpoint: サブスクライバーの endpoint URL
    //         // // vapidHeader: VAPID 署名付きの p256ecdsa 公開鍵（Base64URL）

    //         // WebPushSender.sendWebPush(encryptedPayload, salt, serverPublicKeyX509, endpoint, vapidPublicKeyBase64);
            
    //         // Web Push サブスクリプション情報（クライアントから受け取る値）
    //         String endpoint = ((SubscriptionRequest)entity).getEndpoint();  // 実際の endpoint に差し替えてください

    //         // クライアント公開鍵（raw 65バイト）
    //         String clientPublicKeyBase64 = ((SubscriptionRequest)entity).getP256dh();  // 差し替え
    //         byte[] clientPublicKeyRaw = Base64.getUrlDecoder().decode(clientPublicKeyBase64);

    //         // クライアント auth secret（16バイト）
    //         String authSecretBase64 = ((SubscriptionRequest)entity).getAuth();  // 差し替え
    //         byte[] clientAuthSecret = Base64.getUrlDecoder().decode(authSecretBase64);

    //         // VAPID 公開鍵（Base64URLエンコード済みのX.509鍵）
    //         String vapidPublicKeyBase64 = base64UrlPublicKey;  // 差し替え

    //         // 送信するペイロード
    //         // String payload = "Hello from Java WebPush!";
    //         String payload = "{  \"title\": \"New notification\", \"body\": \"You have a new message!\" }";

    //         // 送信実行
    //         WebPushSenderFull.sendWebPush(endpoint, clientPublicKeyRaw, clientAuthSecret, vapidPublicKeyBase64, payload, csrftoken);

    //     }
    // }



    //     /**
    //  * JWT署名を作成
    //  * @param message
    //  * @param csrftoken
    //  * @return
    //  * @throws Exception
    //  */
    // @PostMapping("/send")
    // public void sendPush(@RequestParam String message, @RequestParam String csrftoken) throws Exception {

    //     Security.addProvider(new BouncyCastleProvider());
    //     PushService pushService = new PushService(base64UrlPublicKey);
    //     List<IEntity> list = pushRepository.getList();
    //     for (IEntity entity : list) {
    //         SubscriptionRequest subscriptionRequest = (SubscriptionRequest)entity;

    //         // public void sendPush(String endpoint, String publicKey, String authenticationSecret, byte[] payload) {
    //         try {
    //             Notification notification = new Notification(subscriptionRequest.getEndpoint(), subscriptionRequest.getP256dh(), subscriptionRequest.getAuth(), message);

    //             HttpResponse response = pushService.send(notification);
    //         } catch (Exception exception) {
    //             // log.error(exception.getMessage());
    //         }
    //         // }
    //     }
    // }

    // @RestController
    // @RequestMapping("/api/push")
    // public class PushController {

    //     @Autowired
    //     private WebPushService pushService;

    //     @PostMapping("/send")
    //     public String sendPush(@RequestBody SubscriptionRequest sub) {
    //         try {
    //             pushService.sendPushMessage(sub, "こんにちは、Push通知です！");
    //             return "OK";
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //             return "NG: " + e.getMessage();
    //         }
    //     }
    // }
