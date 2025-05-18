package kyousei.kyousei._Backup._push4.backup;
// package kyousei.kyousei.push.backup;

// import org.bouncycastle.jce.ECNamedCurveTable;
// import org.bouncycastle.jce.provider.BouncyCastleProvider;
// import org.bouncycastle.jce.spec.ECParameterSpec;
// import org.bouncycastle.jce.spec.ECPublicKeySpec;
// import org.bouncycastle.math.ec.ECPoint;

// import kyousei.kyousei.push.SubscriptionRequest;

// import javax.crypto.Cipher;
// import javax.crypto.KeyAgreement;
// import javax.crypto.Mac;
// import javax.crypto.SecretKey;
// import javax.crypto.spec.GCMParameterSpec;
// import javax.crypto.spec.SecretKeySpec;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.nio.ByteBuffer;
// import java.nio.charset.StandardCharsets;
// import java.security.*;
// import java.security.interfaces.ECPrivateKey;
// import java.security.interfaces.ECPublicKey;
// import java.security.spec.ECGenParameterSpec;
// import java.util.Base64;
// import java.util.ResourceBundle;

// public class WebPushSend {

//     private final static String base64UrlPrivateKey = ResourceBundle.getBundle("application").getString("vapid.privateKey"); // VAPID 秘密鍵
//     private final static String base64UrlPublicKey = ResourceBundle.getBundle("application").getString("vapid.publicKey"); // VAPID 公開鍵
//     private static final String base64Urlsubject = ResourceBundle.getBundle("application").getString("vapid.subject"); //
//     private final static String audience = ResourceBundle.getBundle("application").getString("vapid.audience"); // origin

//     static {
//         Security.addProvider(new BouncyCastleProvider());
//     }

//     public static void send(SubscriptionRequest subscriptionRequest, String message) throws Exception {

//         // 秘密鍵 & 公開鍵の作成
//         ECPrivateKey privateKey = (ECPrivateKey)KeyUtil.convertRawToECPrivateKey(Base64.getUrlDecoder().decode(base64UrlPrivateKey));
//         PublicKey publicKey = KeyUtil.convertRawToECPublicKey(Base64.getUrlDecoder().decode(base64UrlPublicKey));

//         // JWT 署名の作成
//         String jwt = JwtUtil.createES256JWT((ECPrivateKey) privateKey, (ECPublicKey) publicKey, audience, base64Urlsubject);
        
//         String endpoint = subscriptionRequest.getEndpoint();
//         String clientPublicKeyBase64 = subscriptionRequest.getP256dh();
//         String clientAuthBase64 = subscriptionRequest.getAuth();

//         byte[] clientPubKeyBytes = Base64.getUrlDecoder().decode(clientPublicKeyBase64);
//         byte[] clientAuthSecret = Base64.getUrlDecoder().decode(clientAuthBase64);

//         // 公開鍵に必要に応じて0x04を付加
//         byte[] fullClientPubKey = (clientPubKeyBytes.length == 64)
//                 ? addUncompressedPrefix(clientPubKeyBytes)
//                 : clientPubKeyBytes;

//         // クライアント公開鍵をBouncy Castleで読み込む
//         ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
//         ECPoint point = ecSpec.getCurve().decodePoint(fullClientPubKey);
//         ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, ecSpec);
//         KeyFactory keyFactory = KeyFactory.getInstance("ECDH", "BC");
//         PublicKey clientPublicKey = keyFactory.generatePublic(pubKeySpec);

//         // サーバー側でECDH鍵生成
//         KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
//         kpg.initialize(new ECGenParameterSpec("secp256r1"));
//         KeyPair serverKeyPair = kpg.generateKeyPair();

//         // ECDH共有鍵生成
//         KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
//         keyAgreement.init(serverKeyPair.getPrivate());
//         keyAgreement.doPhase(clientPublicKey, true);
//         byte[] sharedSecret = keyAgreement.generateSecret();

//         // HKDF extract (auth secret)
//         byte[] prk = hkdfExtract(clientAuthSecret, sharedSecret);

//         // salt生成
//         byte[] salt = SecureRandom.getInstanceStrong().generateSeed(16);

//         // CEK, nonce導出
//         byte[] cek = hkdfExpand(prk, salt, "Content-Encoding: aes128gcm\0", 16);
//         byte[] nonce = hkdfExpand(prk, salt, "Content-Encoding: nonce\0", 12);

//         // ペイロード末尾に0x02を追加
//         byte[] plaintext = concat(message.getBytes(StandardCharsets.UTF_8), new byte[]{0x02});
//         byte[] ciphertext = aesGcmEncrypt(plaintext, cek, nonce);

//         String payload = "{\"title\":\"New Message\", \"body\":\"You have a new message from John.\", \"icon\":\"/images/icon.png\"}";
//         // AES-GCMで暗号化
//         byte[] encryptedPayload = encryptPayload(payload, cek); // ペイロードをCEKで暗号化

//         // ヘッダー作成
//         // String cryptoKey = "p256ecdsa=" + base64UrlEncode(((ECPublicKey) serverKeyPair.getPublic()).getEncoded());
//         String cryptoKey = "p256ecdsa=" + base64UrlPublicKey;
//         String encryption = "salt=" + base64UrlEncode(salt);

//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(endpoint))
//                 .header("TTL", "48600")
//                 // .header("Content-Type", "application/octet-stream")
//                 .header("Authorization", "WebPush " + jwt)
//                 // .header("Authorization", "vapid t=" + jwt + ", k=" + base64UrlPublicKey)
//                 .header("Content-Encoding", "aes128gcm")
//                 .header("Encryption", encryption)
//                 .header("Crypto-Key", cryptoKey)
//                 .POST(HttpRequest.BodyPublishers.ofByteArray(encryptedPayload))
//                 .build();

//         HttpClient client = HttpClient.newHttpClient();
//         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//         System.out.println("Response: " + response.statusCode());
//         System.out.println(response.body());
//     }

//     static byte[] hkdfExtract(byte[] salt, byte[] ikm) throws Exception {
//         Mac mac = Mac.getInstance("HmacSHA256");
//         mac.init(new SecretKeySpec(salt, "HmacSHA256"));
//         return mac.doFinal(ikm);
//     }

//     static byte[] hkdfExpand(byte[] prk, byte[] salt, String info, int length) throws Exception {
//         Mac mac = Mac.getInstance("HmacSHA256");
//         mac.init(new SecretKeySpec(prk, "HmacSHA256"));
//         byte[] infoBytes = info.getBytes(StandardCharsets.UTF_8);
//         byte[] input = ByteBuffer.allocate(infoBytes.length + 1).put(infoBytes).put((byte) 1).array();
//         byte[] result = mac.doFinal(input);
//         byte[] truncated = new byte[length];
//         System.arraycopy(result, 0, truncated, 0, length);
//         return truncated;
//     }

//     static byte[] aesGcmEncrypt(byte[] plaintext, byte[] key, byte[] nonce) throws Exception {
//         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//         SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
//         GCMParameterSpec gcmSpec = new GCMParameterSpec(128, nonce);
//         cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
//         return cipher.doFinal(plaintext);
//     }

//     static byte[] addUncompressedPrefix(byte[] pubKey64) {
//         if (pubKey64.length != 64) {
//             throw new IllegalArgumentException("公開鍵は64バイト（XとYの各32バイト）である必要があります。現在の長さ: " + pubKey64.length);
//         }
//         byte[] result = new byte[65];
//         result[0] = 0x04;
//         System.arraycopy(pubKey64, 0, result, 1, 64);
//         return result;
//     }

//     static byte[] concat(byte[] a, byte[] b) {
//         byte[] result = new byte[a.length + b.length];
//         System.arraycopy(a, 0, result, 0, a.length);
//         System.arraycopy(b, 0, result, a.length, b.length);
//         return result;
//     }

//     static String base64UrlEncode(byte[] data) {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
//     }

//         // AES-GCM暗号化の実装
//     public static byte[] encryptPayload(String payload, byte[] cek) throws Exception {
//         // 暗号化のためのIV（初期化ベクター）を生成
//         byte[] iv = new byte[12];  // AES-GCMのIVは12バイト
//         new java.security.SecureRandom().nextBytes(iv);

//         // AES-GCMモードで暗号化を行う
//         SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(cek, "AES");
//         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//         GCMParameterSpec spec = new GCMParameterSpec(128, iv); // 128-bitの認証タグ
//         cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

//         byte[] encrypted = cipher.doFinal(payload.getBytes("UTF-8"));

//         // IVと暗号化されたデータを一緒に送る（ヘッダーとしてIVを使用する）
//         ByteBuffer byteBuffer = ByteBuffer.allocate(12 + encrypted.length);
//         byteBuffer.put(iv);  // IVを先頭に追加
//         byteBuffer.put(encrypted);  // 暗号化されたペイロード

//         return byteBuffer.array(); // IVと暗号化データを合わせたバイト配列を返す
//     }
// }

