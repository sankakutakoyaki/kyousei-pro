package kyousei.kyousei._Backup._push2;
// package kyousei.kyousei.push;

// import java.math.BigInteger;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.nio.charset.StandardCharsets;
// import java.security.AlgorithmParameters;
// import java.security.KeyFactory;
// import java.security.KeyPair;
// import java.security.PrivateKey;
// import java.security.PublicKey;
// import java.security.spec.ECGenParameterSpec;
// import java.security.spec.ECParameterSpec;
// import java.security.spec.ECPoint;
// import java.security.spec.ECPrivateKeySpec;
// import java.security.spec.ECPublicKeySpec;
// import java.security.spec.PKCS8EncodedKeySpec;
// import java.util.Base64;
// import java.util.ResourceBundle;

// import org.springframework.stereotype.Service;

// @Service
// public class WebPushService {
//     private final KeyPair vapidKeyPair;

//     public WebPushService() throws Exception {
//         this.vapidKeyPair = generateVapidKeyPair();
//     }

//     public void sendPushNotification(String endpoint, String p256dh, String auth, String message) throws Exception {
//         String vapidToken = VapidUtils.generateVapidToken(endpoint, vapidKeyPair.getPrivate(), vapidKeyPair.getPublic());

//         String encryptedMessage = encryptPayload(message, p256dh, auth); // 省略（後述）

//         HttpRequest request = HttpRequest.newBuilder()
//             .uri(URI.create(endpoint))
//             .header("TTL", "60")
//             .header("Authorization", "WebPush " + vapidToken)
//             .header("Content-Encoding", "aes128gcm")
//             .POST(HttpRequest.BodyPublishers.ofString(encryptedMessage, StandardCharsets.UTF_8))
//             .build();

//         HttpClient client = HttpClient.newHttpClient();
//         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//         System.out.println("Push Notification Response: " + response.statusCode());
//     }

//     private String encryptPayload(String message, String p256dh, String auth) {
//         // Web Push の暗号化処理を実装（WebCrypto API に相当する処理）
//         return message; // 簡略化のためプレーンテキストのまま
//     }

//     private KeyPair generateVapidKeyPair() throws Exception {
//         // 手動で生成した VAPID キーを設定
//         String publicKeyBase64 = ResourceBundle.getBundle("application").getString("vapid.publicKey.base64"); // VAPID 公開鍵
//         String privateKeyBase64 = ResourceBundle.getBundle("application").getString("vapid.privateKey.base64"); // VAPID 秘密鍵

//         byte[] publicKeyBytes = Base64.getUrlDecoder().decode(publicKeyBase64);
//         byte[] privateKeyBytes = Base64.getUrlDecoder().decode(privateKeyBase64);

//         KeyFactory keyFactory = KeyFactory.getInstance("EC");

//         // EC 曲線のパラメータを取得
//         ECGenParameterSpec ecGenSpec = new ECGenParameterSpec("secp256r1");
//         AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
//         parameters.init(ecGenSpec);
//         ECParameterSpec ecParameterSpec = parameters.getParameterSpec(ECParameterSpec.class);

//         // 公開鍵の作成（手動で ECPoint を作成）
//         int keySize = publicKeyBytes.length / 2;
//         BigInteger x = new BigInteger(1, java.util.Arrays.copyOfRange(publicKeyBytes, 0, keySize));
//         BigInteger y = new BigInteger(1, java.util.Arrays.copyOfRange(publicKeyBytes, keySize, publicKeyBytes.length));
//         ECPoint point = new ECPoint(x, y);
//         ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(point, ecParameterSpec);
//         PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

//         // 秘密鍵の作成
//         ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(new BigInteger(1, privateKeyBytes), ecParameterSpec);
//         PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

//         return new KeyPair(publicKey, privateKey);
//     }

//     public KeyPair getVapidKeyPair() {
//         return vapidKeyPair;
//     }
// }
