package kyousei.kyousei._Backup._push4.backup;
// package kyousei.kyousei.push.backup;

// import org.bouncycastle.jce.ECNamedCurveTable;
// import org.bouncycastle.jce.provider.BouncyCastleProvider;
// import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
// import org.bouncycastle.jce.spec.ECPublicKeySpec;
// import org.bouncycastle.math.ec.ECPoint;

// import javax.crypto.*;
// import javax.crypto.spec.GCMParameterSpec;
// import javax.crypto.spec.SecretKeySpec;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.nio.ByteBuffer;
// import java.security.*;
// import java.security.interfaces.ECPrivateKey;
// import java.security.interfaces.ECPublicKey;
// import java.security.spec.ECGenParameterSpec;
// import java.time.Duration;
// import java.util.Base64;
// import java.util.ResourceBundle;

// public class WebPushSenderFull {

//     private final static String base64UrlPrivateKey = ResourceBundle.getBundle("application").getString("vapid.privateKey"); // VAPID 秘密鍵
//     private final static String base64UrlPublicKey = ResourceBundle.getBundle("application").getString("vapid.publicKey"); // VAPID 公開鍵
//     private static final String base64Urlsubject = ResourceBundle.getBundle("application").getString("vapid.subject"); //
//     private final static String audience = ResourceBundle.getBundle("application").getString("vapid.audience"); // origin

//     static {
//         Security.addProvider(new BouncyCastleProvider());
//     }

//     public static void sendWebPush(String endpoint, byte[] clientPublicKeyRaw, byte[] clientAuthSecret, String vapidPublicKeyBase64, String message, String csrftoken) throws Exception {
//         // 秘密鍵 & 公開鍵の作成
//         ECPrivateKey privateKey = (ECPrivateKey)KeyUtil.convertRawToECPrivateKey(Base64.getUrlDecoder().decode(base64UrlPrivateKey));
//         PublicKey publicKey = KeyUtil.convertRawToECPublicKey(Base64.getUrlDecoder().decode(base64UrlPublicKey));

//         // JWT 署名の作成
//         String jwt = JwtUtil.createES256JWT((ECPrivateKey) privateKey, (ECPublicKey) publicKey, audience, base64Urlsubject);

//                 // KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
//                 // ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
//                 // ECPoint point = parameterSpec.getCurve().decodePoint(clientPublicKeyRaw);
//                 // PublicKey clientPublicKey = keyFactory.generatePublic(new ECPublicKeySpec(point, parameterSpec));

//                     // サーバー側の鍵ペア生成
//                     KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
//                     generator.initialize(new ECGenParameterSpec("secp256r1"));
//                     KeyPair serverKeyPair = generator.generateKeyPair();

//                 // // ECDHによる共有鍵
//                 // KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
//                 // keyAgreement.init(serverKeyPair.getPrivate());
//                 // keyAgreement.doPhase(clientPublicKey, true);
//                 // byte[] sharedSecret = keyAgreement.generateSecret();

//                 // // salt生成
//                 // byte[] salt = new byte[16];
//                 // SecureRandom random = new SecureRandom();
//                 // random.nextBytes(salt);

//                 // // HKDF extract
//                 // byte[] prk = hkdfExtract(clientAuthSecret, sharedSecret);

//         // HKDF expand（CEK, nonce）
//         // byte[] cek = hkdfExpand(prk, "Content-Encoding: aes128gcm".getBytes(), 16);
//         // byte[] nonce = hkdfExpand(prk, "Content-Encoding: nonce".getBytes(), 12);
//         // ランダムなキー（CEK）とIVを生成
//                 // KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//                 // keyGen.init(128); // 128ビットのキーを使用
//                 // SecretKey key = keyGen.generateKey();
//                 // byte[] iv = new byte[12]; // AES-GCMは12バイトのIVを要求する
//                 // new java.security.SecureRandom().nextBytes(iv);

//                 // // ペイロードを暗号化
//                 // String encryptedPayload = encryptPayload(message, key.getEncoded(), iv);

//         // // AES-GCM暗号化
//         // Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//         // SecretKeySpec cekKey = new SecretKeySpec(cek, "AES");
//         // GCMParameterSpec gcmSpec = new GCMParameterSpec(128, nonce);
//         // cipher.init(Cipher.ENCRYPT_MODE, cekKey, gcmSpec);
//         // byte[] encryptedPayload = cipher.doFinal(payload.getBytes());

//         // // サーバー公開鍵をX.509形式に
//         // byte[] serverPublicKeyX509 = serverKeyPair.getPublic().getEncoded();
//                 // 公開鍵を取得
//                 // PublicKey serverPublicKeyX509 = serverKeyPair.getPublic();

//                 // 公開鍵を圧縮形式に変換
//                 // byte[] publicKeyBytes = serverPublicKeyX509.getEncoded();
//                 // byte[] compressedPublicKey = new byte[65];
//                 // compressedPublicKey[0] = 0x04; // 圧縮された形式
//                 // System.arraycopy(publicKeyBytes, 1, compressedPublicKey, 1, 64); // X座標とY座標をコピー
                
                
                
//                             // byte[] publicKeyBytes = Base64.getUrlDecoder().decode(clientPublicKeyRaw);
//                             byte[] salt = new byte[16];
//                             new SecureRandom().nextBytes(salt);
    
//                             byte[] encryptedPayload = WebPushEncryptor.encryptAES128GCM(
//                                 "Hello WebPush!".getBytes(),
//                                 clientPublicKeyRaw, // 65バイト（先頭 0x04 + x + y）
//                                 clientAuthSecret,
//                                 serverKeyPair,
//                                 salt
//                             );    

//         // Base64URL encode
//         // String saltB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(salt);
//         // String dhB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(compressedPublicKey);
//         // byte[] encryptedPayloadBytes = Base64.getDecoder().decode(encryptedPayload); // Base64からバイト配列に変換

//         ByteBuffer headerBuffer = ByteBuffer.allocate(86);
//         // 1. Salt（16バイト）
//         headerBuffer.put(salt);  // salt はランダム生成済みの 16バイト

//         // 2. Record size（4バイト）
//         headerBuffer.putInt(0x00002c00);  // 例: 11264バイト（推奨）

//         // 3. 0x41（固定）
//         headerBuffer.put((byte) 0x41);

//         // 4. サーバ公開鍵（65バイト形式）
//         byte[] serverPublicKey = KeyUtil.getUncompressedPublicKeyBytes(serverKeyPair.getPublic()); // 65バイト形式
//         headerBuffer.put(serverPublicKey);

//         // 完成したヘッダー
//         byte[] header = headerBuffer.array();

//         // // バイト配列の各バイトをバイナリ表示する
//         // for (int i = 0; i < header.length; i++) {
//         //     System.out.println("Byte " + Integer.toHexString(i) + ": " + byteToBinary(header[i]));
//         // }

//         // 最終ペイロード（header + encrypted）
//         byte[] payload = ByteBuffer.allocate(header.length + encryptedPayload.length)
//                 .put(header)
//                 .put(encryptedPayload)
//                 .array();

//         // HTTP送信
//         // HttpClient client = HttpClient.newHttpClient();
//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(endpoint))
//                 .timeout(Duration.ofSeconds(10))
//                 .header("TTL", "60")
//                 .header("X-CSRF-TOKEN", csrftoken)
//                 .header("Authorization", "vapid t=" + jwt + ", k=" + base64UrlPublicKey)
//                 // .header("Authorization", "WebPush " + jwt)
//                 .header("Content-Encoding", "aes128gcm")
//                 // .header("Content-Type", "application/octet-stream")
//                 // .header("Encryption", "salt=" + saltB64)
//                 // .header("Crypto-Key", "dh=" + dhB64 + "; p256ecdsa=" + vapidPublicKeyBase64)
//                 // .POST(HttpRequest.BodyPublishers.ofByteArray(encryptedPayload))
//                 .header("Urgency", "normal")
//                 .POST(HttpRequest.BodyPublishers.ofByteArray(payload)) // 暗号化されたペイロードをリクエストボディとして設定
//                 .build();

//         // 3. リクエストの送信
//         try {
//             // HttpClientを使ってリクエストを送信
//             HttpClient client = HttpClient.newHttpClient();
//             HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//             System.out.println("Response Code: " + response.statusCode());
//             System.out.println("Response Body: " + response.body());
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
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
//         byte[] output = new byte[length];
//         System.arraycopy(result, 0, output, 0, length);
//         return output;
//     }

//     // 公開鍵のraw取得用（65byte）
//     public static byte[] getRaw(PublicKey publicKey) {
//         byte[] encoded = publicKey.getEncoded();
//         return java.util.Arrays.copyOfRange(encoded, encoded.length - 65, encoded.length);
//     }

//         // AES-GCM でペイロードを暗号化するメソッド
//         public static String encryptPayload(String payload, byte[] key, byte[] iv) throws Exception {
//             Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//             GCMParameterSpec spec = new GCMParameterSpec(128, iv);  // 128ビットの認証タグ
//             cipher.init(Cipher.ENCRYPT_MODE, new javax.crypto.spec.SecretKeySpec(key, "AES"), spec);
//             byte[] encrypted = cipher.doFinal(payload.getBytes());
//             return Base64.getEncoder().encodeToString(encrypted);  // Base64エンコードして返す
//         }
//     // バイトをバイナリ形式に変換
//     public static String byteToBinary(byte b) {
//         return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
//     }
// } 

