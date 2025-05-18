package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;
// package kyousei.kyousei.push;

// import com.auth0.jwt.JWT;
// import com.auth0.jwt.algorithms.Algorithm;
// import com.auth0.jwt.interfaces.DecodedJWT;

// import org.apache.http.client.methods.CloseableHttpResponse;
// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.entity.StringEntity;
// import org.apache.http.impl.client.HttpClients;
// import org.apache.http.impl.client.CloseableHttpClient;

// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.security.KeyFactory;
// import java.security.PrivateKey;
// import java.security.PublicKey;
// import java.security.interfaces.RSAPrivateKey;
// import java.security.interfaces.RSAPublicKey;
// import java.security.spec.PKCS8EncodedKeySpec;
// import java.security.spec.X509EncodedKeySpec;
// import java.util.Base64;
// import java.util.Date;
// import java.util.ResourceBundle;

// public class PushNotificationSender {

//     // サーバーに送信するVAPIDトークンを生成
//     public static String createVapidJWT(String audience) throws Exception {
//         long now = System.currentTimeMillis();
//         Date expiresAt = new Date(now + 3600 * 1000);  // 1時間後に期限切れ

//         // 秘密鍵と公開鍵を生成する
//         String pub = ResourceBundle.getBundle("application").getString("vapid.publicKey"); // VAPID 公開鍵
//         String pri = ResourceBundle.getBundle("application").getString("vapid.privateKey"); // VAPID 秘密鍵
//         // String pri = new String(Files.readAllBytes(Paths.get("vapid_private.pem")));
//         // String pub = new String(Files.readAllBytes(Paths.get("vapid_public.pem")));
//         PrivateKey privateKey = getPrivateKeyFromPem(pri);
//         PublicKey publicKey = getPublicKeyFromPem(pub);

//         // RSA256を使用して署名
//         Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey);

//         return JWT.create()
//                 .withIssuer(pub)  // 発行者は公開鍵
//                 .withAudience(audience)  // 受信サーバーのエンドポイント
//                 .withIssuedAt(new Date(now))  // 発行時刻
//                 .withExpiresAt(expiresAt)  // 1時間後に期限切れ
//                 .sign(algorithm);
//     }

//     // PEM形式からPrivateKeyを生成
//     private static PrivateKey getPrivateKeyFromPem(String pem) throws Exception {
//         String privateKeyPEM = pem.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");
//         byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//         return keyFactory.generatePrivate(keySpec);
//     }

//     // PEM形式からPublicKeyを生成
//     private static PublicKey getPublicKeyFromPem(String pem) throws Exception {
//         String publicKeyPEM = pem.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s+", "");
//         byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
//         return keyFactory.generatePublic(keySpec);
//     }

//     // プッシュ通知を送信するメソッド
//     public static void sendPushNotification(String pushEndpoint, SubscriptionRequest subscription, byte[] payload) throws Exception {
//         // VAPIDトークンを生成
//         String vapidJWT = createVapidJWT(pushEndpoint);

//         // プッシュ通知のデータをJSON形式で構築
//         String pushNotificationData = "{"
//                 + "\"subscription\": \"" + subscription + "\","
//                 + "\"payload\": \"" + payload + "\","
//                 + "\"vapid\": \"" + vapidJWT + "\""
//                 + "}";

//         // HTTPリクエストを作成
//         try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//             HttpPost postRequest = new HttpPost(pushEndpoint);
//             postRequest.setHeader("Content-Type", "application/json");
//             postRequest.setHeader("Authorization", "vapid " + vapidJWT);

//             // リクエストボディにJSONデータを設定
//             StringEntity entity = new StringEntity(pushNotificationData);
//             postRequest.setEntity(entity);

//             // リクエストを実行して、レスポンスを取得
//             try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
//                 System.out.println("Push notification sent, response: " + response.getStatusLine());
//             }
//         }
//     }

//     // public static void main(String[] args) throws Exception {
//     //     // クライアントから送られてきたサブスクリプション情報
//     //     String pushEndpoint = "<push-endpoint>";
//     //     String subscription = "<subscription-information>";
//     //     String payload = "Hello, this is a push notification!";

//     //     // プッシュ通知を送信
//     //     sendPushNotification(pushEndpoint, subscription, payload);
//     // }
// }
