package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;

// import org.springframework.stereotype.Service;

// import net.minidev.json.JSONObject;

// import java.math.BigInteger;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.nio.charset.StandardCharsets;
// import java.security.AlgorithmParameters;
// import java.security.InvalidAlgorithmParameterException;
// import java.security.InvalidKeyException;
// import java.security.KeyFactory;
// import java.security.KeyPairGenerator;
// import java.security.NoSuchAlgorithmException;
// import java.security.Signature;
// import java.security.SignatureException;
// import java.security.interfaces.ECPrivateKey;
// import java.security.interfaces.ECPublicKey;
// import java.security.spec.ECGenParameterSpec;
// import java.security.spec.ECParameterSpec;
// import java.security.spec.ECPoint;
// import java.security.spec.ECPrivateKeySpec;
// import java.security.spec.ECPublicKeySpec;
// import java.security.spec.PKCS8EncodedKeySpec;
// import java.util.Base64;
// import java.util.ResourceBundle;

// import javax.crypto.spec.SecretKeySpec;

// @Service
// public class PushNotificationService {
//     private final ECDHKeyExchange ecdh;

//     public PushNotificationService() throws Exception {
//         this.ecdh = new ECDHKeyExchange();
//     }

//     public void sendPushNotification(String endpoint, byte[] clientPublicKey, String message, String csrfToken) throws Exception {

//         // KeyFactoryを使ってEC公開鍵を作成
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");

//         // ECパラメータを使用して公開鍵を生成
//         ECParameterSpec ecSpec = getECParameterSpec();

//         // p256dh公開鍵はそのままECPointとして解釈
//         BigInteger x = new BigInteger(1, clientPublicKey, 1, 32); // X座標
//         BigInteger y = new BigInteger(1, clientPublicKey, 33, 32); // Y座標

//         ECPoint ecPoint = new ECPoint(x, y);

//         // ECPublicKeySpecを使用して公開鍵を生成
//         ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(ecPoint, ecSpec);
//         ECPublicKey publicKey = (ECPublicKey) keyFactory.generatePublic(ecPublicKeySpec);

//         // 公開鍵を出力（成功例）
//         System.out.println("ECDH クライアント公開鍵の取得成功: " + publicKey);
//         byte[] encodedKey = publicKey.getEncoded();

//         sendWebPush(endpoint, message, encodedKey, csrfToken, publicKey);
//     }

//     public void sendWebPush(String endpoint, String message, byte[] encodedKey, String csrfToken, ECPublicKey ecpublickey) throws Exception {
//         // 共通鍵を使用してメッセージをAES-GCMで暗号化
//         SecretKeySpec sharedSecret = ecdh.deriveSharedSecret(encodedKey);
//         byte[] encryptedMessageBytes = AESGCMEncryptor.encrypt(message, sharedSecret);

//         // 暗号化されたメッセージをbase64エンコード
//         String encryptedMessageBase64 = Base64.getEncoder().encodeToString(encryptedMessageBytes);
//         String vapidToken = getVapidToken("https://fcm.googleapis.com");

//         // Pushリクエストのペイロードを作成
//         JSONObject payload = new JSONObject();
//         payload.put("message", encryptedMessageBase64);

//         // ヘッダーを作成（JSONObjectでTTL設定）
//         JSONObject headers = new JSONObject();
//         headers.put("Authorization", vapidToken); // VAPID JWTをAuthorizationヘッダーとして設定
//         // headers.put("Crypto-Key", "p256ecdsa=" + ResourceBundle.getBundle("application").getString("vapid.publicKey"));
//         headers.put("TTL", 60);  // 配信時間の設定

//         // リクエストボディ
//         JSONObject body = new JSONObject();
//         body.put("to", endpoint);
//         body.put("notification", payload);
//         body.put("headers", headers);  // headersをボディに追加
// System.out.println(vapidToken);

//         // HttpClientを作成
//         HttpClient client = HttpClient.newHttpClient();

//         // HTTPリクエストを作成
//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(new URI(endpoint))
//                 .header("Content-Type", "application/json")
//                 .header("X-CSRF-TOKEN", csrfToken)
//                 .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
//                 .build();

//         // リクエストを送信
//         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//         System.out.println("Response Code: " + response.statusCode());
//         System.out.println("Response Body: " + response.body());

//         // // Web Push のペイロードをJSON形式で作成
//         // String payload = String.format("{\"message\":\"%s\"}", encryptedMessageBase64);

//         // // Web Pushのエンドポイントに通知を送信
//         // URI uri = new URI(endpoint);
//         // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//         // conn.setRequestMethod("POST");
//         // conn.setRequestProperty("Content-Type", "application/octet-stream");
//         // conn.setRequestProperty("Authorization", "vapid t=" + vapidToken + ", k=" + ResourceBundle.getBundle("application").getString("vapid.publicKey"));
//         // conn.setRequestProperty("X-CSRF-TOKEN", csrfToken);
//         // conn.setDoOutput(true);



//         // リクエストボディにペイロードを書き込む
//         // conn.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
//         // conn.getOutputStream().flush();
//         // conn.getOutputStream().close();

//         // // レスポンスを取得（確認用）
//         // int responseCode = conn.getResponseCode();
//         // System.out.println("Response Code: " + responseCode);
//     }

//     private static ECPrivateKey EC_PRIVATE_KEY;

//     private String getVapidToken(String audience)  throws Exception {
// //         String header = Base64.getUrlEncoder().withoutPadding().encodeToString(
// //             "{\"alg\":\"ES256\",\"typ\":\"JWT\"}".getBytes()
// //         );

// //         long expiry = System.currentTimeMillis() / 1000 + 12 * 60 * 60;
// //         String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(
// //             String.format("{\"aud\":\"%s\",\"exp\":%d,\"sub\":\"%s\"}", audience, expiry, ResourceBundle.getBundle("application").getString("vapid.subject")).getBytes()
// //         );

//         ECPrivateKey EC_PRIVATE_KEY = VapidJwtGenerator.createEcPrivateKey(ResourceBundle.getBundle("application").getString("vapid.privateKey"));
//         // ECPrivateKey EC_PRIVATE_KEY = createEcPrivateKey();        // JWT署名部分の生成

// // BigInteger sValue = EC_PRIVATE_KEY.getS();  // S 値を取得
// // byte[] privateKeyBytes = sValue.toByteArray();
// byte[] privateKeyBytes = VapidJwtGenerator.normalizeECPrivateKey(EC_PRIVATE_KEY.getS());
// // // 長さを確認
// // System.out.println("S Value Length: " + privateKeyBytes.length);
// // System.out.println("S Value : " + Base64.getUrlEncoder().withoutPadding().encodeToString(privateKeyBytes));
// // System.out.println("S Value (Hex): " + sValue.toString(16));
// ECPrivateKey EC_PRIVATE_KEY2 = VapidJwtGenerator.createEcPrivateKey(Base64.getUrlEncoder().withoutPadding().encodeToString(privateKeyBytes));
// JwtGeneratorWithoutLib.print();
// JWTbuild jwTbuild = new JWTbuild();
// String jwtStr = jwTbuild.build("hashimoto@kyouseibin.com");
// return "WebPush " + jwtStr;



// //         String toSign = header + "." + payload;

// //         // 署名の生成（ES256）
// //         Signature signature = Signature.getInstance("SHA256withECDSA");
// //         signature.initSign(EC_PRIVATE_KEY2);
// //         signature.update(toSign.getBytes());

// //         byte[] signedData = signature.sign();


// //         String encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signedData);
// // System.out.println("encordedSignature: " + encodedSignature);
// //         // JWTを組み立て
// //         return "WebPush " + toSign + "." + encodedSignature;


//         // // 3. 署名（R,S）を取得
//         // // ECDSA署名はR,Sの2つの整数で構成される
//         // BigInteger r = new BigInteger(1, signedData, 0, 32); // R部分
//         // BigInteger s = new BigInteger(1, signedData, 32, 32); // S部分

//         // // 4. RとSを連結し、Base64Urlエンコード
//         // byte[] rBytes = r.toByteArray();
//         // byte[] sBytes = s.toByteArray();

//         // // 署名のフォーマットに合わせてBase64Urlエンコード
//         // String signatureBase64Url = base64UrlEncode(concatenate(rBytes, sBytes));

//         // return "WebPush " + header + "." + payload + "." + signatureBase64Url;
//     }

//     // ECパラメータの取得
//     private static ECParameterSpec getECParameterSpec() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
//         // secp256r1のパラメータを取得
//         ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256r1");
//         KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
//         keyPairGen.initialize(ecGenParameterSpec);  // ECGenParameterSpecを使用
//         ECParameterSpec ecSpec = ((ECPublicKey) keyPairGen.generateKeyPair().getPublic()).getParams();
//         return ecSpec;
//     }
    
//     // Base64Urlエンコード
//     private static String base64UrlEncode(byte[] data) {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
//     }

//     // 2つのバイト配列を連結
//     private static byte[] concatenate(byte[] rBytes, byte[] sBytes) {
//         byte[] result = new byte[rBytes.length + sBytes.length];
//         System.arraycopy(rBytes, 0, result, 0, rBytes.length);
//         System.arraycopy(sBytes, 0, result, rBytes.length, sBytes.length);
//         return result;
//     }

//     // // 保存したURLセーフなBase64の秘密鍵をEcPrivateKeyを作成する
//     // public static ECPrivateKey createEcPrivateKey() throws Exception {
//     //     try {
//     //         // Base64デコードを行う
//     //         byte[] decodedPrivateKey = Base64.getUrlDecoder().decode(ResourceBundle.getBundle("application").getString("vapid.privateKey"));
//     //         // デコードされた秘密鍵が空でないかを確認
//     //         if (decodedPrivateKey == null || decodedPrivateKey.length == 0) {
//     //             throw new IllegalArgumentException("Decoded private key is null or empty");
//     //         }
//     //         // 4. ECパラメータを取得（secp256r1 曲線）
//     //         AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
//     //         parameters.init(new ECGenParameterSpec("secp256r1"));
//     //         ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);

//     //         // KeyFactoryを使って秘密鍵の生成に必要なパラメータを作成
//     //         KeyFactory keyFactory = KeyFactory.getInstance("EC");

//     //         // ECPrivateKeySpecを使って秘密鍵を作成
//     //         ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(new BigInteger(1, decodedPrivateKey), ecParameters);

//     //         // EC秘密鍵を生成
//     //         EC_PRIVATE_KEY = (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
            
//     //         return EC_PRIVATE_KEY;

//     //     } catch (Exception e) {
//     //         e.printStackTrace();
//     //         return null;
//     //     }
//     // }
// }
