package kyousei.kyousei._Backup._push;
// package kyousei.kyousei._push;

// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.security.KeyFactory;
// import java.security.PrivateKey;
// import java.security.Signature;
// import java.security.spec.PKCS8EncodedKeySpec;
// import java.time.Instant;
// import java.util.Base64;
// import java.util.ResourceBundle;

// import org.springframework.stereotype.Service;

// @Service
// public class _PushNotificationService {

//     private static final String VAPID_PRIVATE_KEY_BASE64 = ResourceBundle.getBundle("application").getString("vapid.privateKey");
//     private static final String VAPID_PUBLIC_KEY_BASE64 = ResourceBundle.getBundle("application").getString("vapid.publicKey");
//     private static final String VAPID_SUBJECT = "mailto:your@email.com";
//     // private static final String ENDPOINT = "【購読者のエンドポイントURL】";
//     private static final String P256_ALGORITHM = "EC";
//     private static final String SIGN_ALGORITHM = "SHA256withECDSA";

//     public void sendPushNotification(String payload, String ENDPOINT) throws Exception {
//         // JWTの生成
//         String jwt = generateVapidToken();

//         // Push通知を送信
//         HttpClient client = HttpClient.newHttpClient();
//         HttpRequest request = HttpRequest.newBuilder()
//             .uri(URI.create(ENDPOINT))
//             .header("Authorization", "Bearer " + jwt)
//             .header("TTL", "60")
//             .POST(HttpRequest.BodyPublishers.ofString(payload))
//             .build();

//         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//         System.out.println("Response: " + response.statusCode() + " - " + response.body());
//     }

//     private String generateVapidToken() throws Exception {
//         long exp = Instant.now().getEpochSecond() + 3600;
//         String header = "{\"alg\":\"ES256\",\"typ\":\"JWT\"}";
//         String payload = String.format("{\"sub\":\"%s\",\"exp\":%d}", VAPID_SUBJECT, exp);

//         String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes());
//         String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());

//         String unsignedToken = encodedHeader + "." + encodedPayload;

//         String signature = signWithPrivateKey(unsignedToken, VAPID_PRIVATE_KEY_BASE64);

//         return unsignedToken + "." + signature;
//     }

//     private String signWithPrivateKey(String data, String privateKeyBase64) throws Exception {
//         byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
//         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
//         KeyFactory keyFactory = KeyFactory.getInstance(P256_ALGORITHM);
//         PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

//         Signature signature = Signature.getInstance(SIGN_ALGORITHM);
//         signature.initSign(privateKey);
//         signature.update(data.getBytes());

//         byte[] signedData = signature.sign();
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(signedData);
//     }
// }


// 秘密鍵 Base64
// LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JR0hBZ0VBTUJNR0J5cUdTTTQ5QWdFR0NDcUdTTTQ5QXdFSEJHMHdhd0lCQVFRZ2RFV2ZjMUc3NXdXQWQ4MkEKOWJZVVFTVkVxVzVRSVZ5WURycVB5MWplU1NhaFJBTkNBQVFxenJtN25YWFp1Z0tHVXBYNTlORVJGLzA1NXpTegpEUk5qeFRScU5VZHM4dk9mek82b2h5aVRLMDgxZU5BNVVqNWlzaEgyZVF6a2JkckpURHJmK21FcQotLS0tLUVORCBQUklWQVRFIEtFWS0tLS0tCg==%
// 公開鍵 Base64
// LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUZrd0V3WUhLb1pJemowQ0FRWUlLb1pJemowREFRY0RRZ0FFS3M2NXU1MTEyYm9DaGxLVitmVFJFUmY5T2VjMApzdzBUWThVMGFqVkhiUEx6bjh6dXFJY29reXRQTlhqUU9WSStZcklSOW5rTTVHM2F5VXc2My9waEtnPT0KLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0tCg==%




// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import kyousei.kyousei.entity.data.PushSubscription;

// import org.springframework.http.HttpHeaders;

// import java.util.ResourceBundle;

// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;

// @Service
// public class PushNotificationService {

//     // @Value("${vapid.publicKey}")
//     // private static String VAPID_PUBLIC_KEY;
//     // @Value("${vapid.privateKey}")
//     // private static String VAPID_PRIVATE_KEY;

//     // public void sendPushNotification(String endpoint, String vapidToken, String message) {
//     //     // Web Pushのリクエストヘッダー
//     //     HttpHeaders headers = new HttpHeaders();
//     //     headers.set("Authorization", "WebPush " + vapidToken);
//     //     headers.set("Content-Type", "application/json");

//     //     // Push通知の内容
//     //     String body = "{ \"notification\": { \"title\": \"New Message\", \"body\": \"" + message + "\" } }";

//     //     // HTTPリクエストを作成して送信
//     //     HttpEntity<String> entity = new HttpEntity<>(body, headers);
//     //     RestTemplate restTemplate = new RestTemplate();
//     //     ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);

//     //     // レスポンス確認
//     //     if (response.getStatusCode().is2xxSuccessful()) {
//     //         System.out.println("Push notification sent successfully!");
//     //     } else {
//     //         System.err.println("Failed to send push notification: " + response.getBody());
//     //     }
//     // }
//     // public static void main(String[] args) throws Exception {
//     //     String endpoint = "<SUBSCRIBER_ENDPOINT>"; // 送信先のエンドポイント
//     //     String vapidToken = VapidTokenGenerator.generateVapidToken("https://fcm.googleapis.com", "mailto:example@example.com");
//     //     new PushNotificationService().sendPushNotification(endpoint, vapidToken, "Hello, Web Push!");
//     // }



//     // サーバーからPush通知を送信
//     public void sendPushNotification(PushSubscription subscription, String message) throws Exception {
//         // Push通知を送信するエンドポイント（Subscriptionから取得）
//         String endpoint = subscription.getEndpoint();

//         // Web Push通知のリクエストを送るためのヘッダー
//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Authorization", "WebPush " + getVapidAuthorization());
//         headers.set("Content-Type", "application/json");

//         // Push通知の内容
//         String body = createPushNotificationBody(message);

//         // リクエストを作成して送信
//         HttpEntity<String> entity = new HttpEntity<>(body, headers);
//         RestTemplate restTemplate = new RestTemplate();
//         ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);

//         // レスポンスを確認
//         if (response.getStatusCode().is2xxSuccessful()) {
//             System.out.println("Push notification sent successfully!");
//         } else {
//             System.err.println("Failed to send push notification: " + response.getBody());
//         }
//     }

//     // VAPID署名を作成するメソッド（省略）
//     private String getVapidAuthorization() throws Exception {
//         return VapidTokenGenerator.getJwt();
//         // // VAPID署名を生成する処理（VAPID署名用ライブラリを利用）
//         // String VAPID_SUBJECT = ResourceBundle.getBundle("application").getString("vapid.subject");
//         // return VapidTokenGenerator.generateVapidToken("https://fcm.googleapis.com", VAPID_SUBJECT);
//         // return "vapid-signed-token";
//     }

//     // 通知メッセージをWeb Push用のJSONフォーマットで作成
//     private String createPushNotificationBody(String message) {
//         // Web Push通知の本体（通常の通知内容）
//         return "{ \"notification\": { \"title\": \"New Message\", \"body\": \"" + message + "\" } }";
//     }
// }





// import java.io.OutputStream;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.security.KeyFactory;
// import java.security.PrivateKey;
// import java.security.Signature;
// import java.security.interfaces.ECPrivateKey;
// import java.security.spec.PKCS8EncodedKeySpec;
// import java.util.Base64;
// import javax.crypto.Cipher;
// import org.json.JSONObject;

// public class PushNotificationSender {

//     private static final String VAPID_PRIVATE_KEY = "dMesI91E8a1wUL7Df6uK-THeZ59jC25j8VMPhElh7z4";  // VAPID秘密鍵
//     private static final String VAPID_PUBLIC_KEY = "BDtSdJ-595-GLPukMIt7t9HGcfgWQWQsMcpKTPWUGqOdfo4vGG9pIM4jAyOulTGsrioOd2lcoHAY8cK90_ZETxA";    // VAPID公開鍵

//     // 通知を送信するメソッド
//     public void sendPushNotification(String subscriptionJson, String payload) throws Exception {
//         JSONObject subscription = new JSONObject(subscriptionJson);
//         String endpoint = subscription.getString("endpoint");
//         String auth = subscription.getJSONObject("keys").getString("auth");
//         String p256dh = subscription.getJSONObject("keys").getString("p256dh");

//         // VAPIDヘッダーを生成
//         String vapidHeader = generateVapidHeader(endpoint);

//         // 通知のペイロード
//         JSONObject notificationData = new JSONObject();
//         notificationData.put("title", "New Notification");
//         notificationData.put("body", payload);

//         // HTTPリクエストを送信
//         sendPushRequest(endpoint, notificationData.toString(), vapidHeader);
//     }

//     // VAPIDヘッダーの生成
//     private String generateVapidHeader(String endpoint) throws Exception {
//         String vapidClaim = "{\"aud\": \"" + endpoint + "\", \"sub\": \"mailto:your-email@example.com\"}";
//         String vapidSignature = generateVapidSignature(vapidClaim);

//         return "vapid t=" + Base64.getUrlEncoder().encodeToString(vapidClaim.getBytes()) +
//                ", k=" + VAPID_PUBLIC_KEY + ", x5=" + vapidSignature;
//     }

//     // VAPID署名の生成
//     private String generateVapidSignature(String claim) throws Exception {
//         PrivateKey privateKey = getPrivateKey(VAPID_PRIVATE_KEY);
//         Signature signature = Signature.getInstance("SHA256withECDSA");
//         signature.initSign(privateKey);
//         signature.update(claim.getBytes());
//         byte[] signedData = signature.sign();
//         return Base64.getUrlEncoder().encodeToString(signedData);
//     }

//     // VAPID秘密鍵からPrivateKeyを取得するメソッド
//     private PrivateKey getPrivateKey(String privateKeyBase64) throws Exception {
//         byte[] decodedKey = Base64.getUrlDecoder().decode(privateKeyBase64);
//         // PrivateKeyの生成（ECアルゴリズム）
//         ECPrivateKey privateKey = (ECPrivateKey) KeyFactory.getInstance("EC")
//             .generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
//         return privateKey;
//     }

//     // Push通知リクエストを送信
//     @SuppressWarnings("deprecation")
//     private void sendPushRequest(String endpoint, String payload, String vapidHeader) throws Exception {
//         URL url = new URL(endpoint);
//         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//         connection.setRequestMethod("POST");
//         connection.setRequestProperty("Authorization", "Bearer " + vapidHeader);
//         connection.setRequestProperty("Content-Type", "application/json");
//         connection.setDoOutput(true);

//         // 通知データを送信
//         try (OutputStream os = connection.getOutputStream()) {
//             byte[] input = payload.getBytes("utf-8");
//             os.write(input, 0, input.length);
//         }

//         // レスポンスの取得
//         int responseCode = connection.getResponseCode();
//         System.out.println("Push Notification Response Code: " + responseCode);
//     }
// }





// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.io.OutputStream;
// import java.security.PrivateKey;
// import java.security.PublicKey;
// import java.security.Signature;
// import java.util.Base64;
// import javax.crypto.Mac;
// import javax.crypto.spec.SecretKeySpec;
// import org.json.JSONObject;

// public class PushNotificationService {

//     private static final String PRIVATE_KEY = "YOUR_PRIVATE_KEY"; // VAPID private key
//     private static final String PUBLIC_KEY = "YOUR_PUBLIC_KEY"; // VAPID public key

//     public void sendPushNotification(String endpoint, String payload, String subscriptionJson) throws Exception {
//         // VAPIDの署名を作成
//         String vapidHeader = generateVapidHeader(subscriptionJson);

//         // HTTPリクエストの送信
//         URL url = new URL(endpoint);
//         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//         connection.setRequestMethod("POST");
//         connection.setRequestProperty("Content-Type", "application/json");
//         connection.setRequestProperty("Authorization", vapidHeader);
//         connection.setDoOutput(true);

//         // リクエストのボディに通知データをセット
//         JSONObject jsonPayload = new JSONObject();
//         jsonPayload.put("title", "New Notification");
//         jsonPayload.put("body", payload);

//         // リクエスト送信
//         try (OutputStream os = connection.getOutputStream()) {
//             byte[] input = jsonPayload.toString().getBytes("utf-8");
//             os.write(input, 0, input.length);
//         }

//         // サーバーからのレスポンスを確認
//         int responseCode = connection.getResponseCode();
//         System.out.println("Response Code: " + responseCode);
//     }

//     private String generateVapidHeader(String subscriptionJson) {
//         // VAPID署名の生成
//         String subscriptionData = subscriptionJson;  // サブスクリプションデータを元に
//         // 署名作成ロジック（JWTなど）
//         // 省略するが、適切にVAPIDヘッダーを生成
//         return "vapid-key";
//     }
// }




// import nl.martijndwars.webpush.Notification;
// import nl.martijndwars.webpush.PushService;
// import org.apache.http.HttpResponse;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import java.nio.charset.StandardCharsets;
// import java.security.KeyFactory;
// import java.security.PublicKey;
// import java.security.spec.X509EncodedKeySpec;
// import java.util.Base64;
// @Service
// public class PushNotificationService {
//     private final PushService pushService;

//     public PushNotificationService(
//             @Value("${vapid.publicKey}") String publicKey,
//             @Value("${vapid.privateKey}") String privateKey,
//             @Value("${vapid.subject}") String subject) throws Exception {
//         pushService = new PushService(publicKey, privateKey, subject);
//     }

//     public void sendNotification(String endpoint, String p256dh, String auth, String message) throws Exception {
//         // p256dh を Base64 デコードし、PublicKey に変換
//         byte[] publicKeyBytes = Base64.getUrlDecoder().decode(p256dh);
//         X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         PublicKey userPublicKey = keyFactory.generatePublic(keySpec);

//         // auth を Base64 デコード
//         byte[] userAuth = Base64.getUrlDecoder().decode(auth);

//         // メッセージを UTF-8 でエンコード
//         byte[] payload = message.getBytes(StandardCharsets.UTF_8);

//         // Notification オブジェクトを作成
//         Notification notification = new Notification(endpoint, userPublicKey, userAuth, payload);

//         // Web Push を送信
//         HttpResponse response = pushService.send(notification);
//         System.out.println("Push Notification Response: " + response.getStatusLine());
//     }
// }


