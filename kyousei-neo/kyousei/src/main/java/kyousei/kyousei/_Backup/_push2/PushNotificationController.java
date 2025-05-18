package kyousei.kyousei._Backup._push2;
// package kyousei.kyousei.push;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

// import javax.crypto.KeyGenerator;
// import javax.crypto.SecretKey;
// import java.io.IOException;
// import java.security.NoSuchAlgorithmException;
// import java.util.Base64;

// @RestController
// @RequestMapping("/push")
// public class PushNotificationController {

//     private static final String VAPID_PUBLIC_KEY = "BDtSdJ-595-GLPukMIt7t9HGcfgWQWQsMcpKTPWUGqOdfo4vGG9pIM4jAyOulTGsrioOd2lcoHAY8cK90_ZETxA"; // 生成された公開鍵
//     private static final String VAPID_PRIVATE_KEY = "dMesI91E8a1wUL7Df6uK-THeZ59jC25j8VMPhElh7z4"; // 生成された秘密鍵

//     @PostMapping("/send")
//     @ResponseBody
//     // public String sendPushNotification(@RequestParam String endpoint,
//     //                                    @RequestParam String p256dh,
//     //                                    @RequestParam String auth,
//     //                                    @RequestParam String title,
//     //                                    @RequestParam String body) {
//     public String sendPushNotification(@RequestBody SubscriptionRequest request) {
//         try {
//             // 暗号化されたPush通知を作成
//             String pushMessage = createPushMessage(request.title, request.body);

//             // Web Pushの暗号化処理（VAPID署名を含める）
//             String encryptedMessage = encryptMessage(pushMessage, request.p256dh, request.auth);

//             // Web Pushの送信
//             sendPushNotificationToClient(request.endpoint, encryptedMessage);

//             return "Push notification sent successfully!";
//         } catch (Exception e) {
//             return "Error sending push notification: " + e.getMessage();
//         }
//     }

//     private String createPushMessage(String title, String body) {
//         // 通知の内容を作成
//         return String.format("{\"title\":\"%s\", \"body\":\"%s\"}", title, body);
//     }

//     private String encryptMessage(String message, String p256dh, String auth) {
//         // Web Push APIの暗号化処理
//         // (通常、暗号化処理にはWeb Push APIのライブラリを使用しますが、ここでは擬似コードです)
//         return Base64.getEncoder().encodeToString(message.getBytes());
//     }

//     private void sendPushNotificationToClient(String endpoint, String encryptedMessage) {
//         // Web Pushエンドポイントに暗号化されたメッセージを送信
//         // (実際には、HTTPリクエストを送信してPush通知を送る処理が必要です)
//         System.out.println("Sending push notification to: " + endpoint);
//         System.out.println("Encrypted Message: " + encryptedMessage);
//     }

//     @PostMapping("/subscribe")
//     @ResponseBody
//     public ResponseEntity<String> subscribe(@RequestBody SubscriptionRequest request) {
//         // 受け取ったサブスクリプション情報を処理（保存など）
//         System.out.println("Endpoint: " + request.getEndpoint());
//         System.out.println("P256DH: " + request.getP256dh());
//         System.out.println("Auth: " + request.getAuth());

//         // ここでサブスクリプション情報をデータベースに保存するなどの処理を行う

//         return ResponseEntity.ok("Subscription successful");
//     }

//     // サブスクリプションリクエストを受け取るDTOクラス
//     public static class SubscriptionRequest {
//         private String title;
//         private String body;
//         private String endpoint;
//         private String p256dh;
//         private String auth;

//         // ゲッターとセッター
//         public String getTitle() {
//             return title;
//         }
        
//         public void setTitle(String title) {
//             this.title = title;
//         }

//         public String getBody() {
//             return body;
//         }

//         public void setBody(String body) {
//             this.body = body;
//         }

//         public String getEndpoint() {
//             return endpoint;
//         }

//         public void setEndpoint(String endpoint) {
//             this.endpoint = endpoint;
//         }

//         public String getP256dh() {
//             return p256dh;
//         }

//         public void setP256dh(String p256dh) {
//             this.p256dh = p256dh;
//         }

//         public String getAuth() {
//             return auth;
//         }

//         public void setAuth(String auth) {
//             this.auth = auth;
//         }
//     }
// }
