package kyousei.kyousei._Backup._push;
// package kyousei.kyousei._push;
// package kyousei.kyousei.controller;

// import org.springframework.web.bind.annotation.*;

// import kyousei.kyousei.service.PushNotificationSender;

// import java.util.ArrayList;
// import java.util.List;

// @RestController
// public class PushController {
//     private final List<String> subscriptions = new ArrayList<>();
//     private final PushNotificationSender pushNotificationSender;

//     @PostMapping("/subscribe")
//     public String subscribe(@RequestBody String subscription) {
//         subscriptions.add(subscription);
//         System.out.println("登録されたPushサブスクリプション: " + subscription);
//         return "Subscribed";
//     }

//     @PostMapping("/send")
//     public String sendNotification(@RequestBody String message) {
//         for (String sub : subscriptions) {
//             System.out.println("通知を送信: " + message);
//             PushNotificationSender push = new PushController(); sendPushNotification("", "");
//         }
//         return "通知送信完了";
//     }
// }

// package kyousei.kyousei.controller;

// // import java.util.HashMap;
// // import java.util.Map;

// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/push")
// public class PushController {

//     @PostMapping("/send")
//     public String sendPush(@RequestBody String subscription) {
//         // ここでVAPID署名と通知内容を使ってPush通知を送信します
//         String vapidPrivateKey = "dMesI91E8a1wUL7Df6uK-THeZ59jC25j8VMPhElh7z4";
//         String vapidPublicKey = "BDtSdJ-595-GLPukMIt7t9HGcfgWQWQsMcpKTPWUGqOdfo4vGG9pIM4jAyOulTGsrioOd2lcoHAY8cK90_ZETxA";
        
//         // エンドユーザーに送信するペイロードの作成
//         String payload = "Push Notification message";
        
//         // VAPID署名を作成し、Web Pushサーバーに通知を送信
//         // 署名処理など詳細な実装は、Web Pushライブラリの仕様に従って行います

//         return "Push notification sent successfully";
//     }

//     // @PostMapping("/send")
//     // public Map<String, String> sendPush(@RequestBody Map<String, String> payload) {
//     //     System.out.println("Push Notification: " + payload.get("message"));
//     //     Map<String, String> response = new HashMap<>();
//     //     response.put("status", "sent");
//     //     return response;
//     // }
// }

// package kyousei.kyousei.controller;

// import nl.martijndwars.webpush.Notification;
// import nl.martijndwars.webpush.PushService;
// import nl.martijndwars.webpush.Subscription;
// import org.jose4j.lang.JoseException;
// import org.springframework.web.bind.annotation.*;

// import java.io.IOException;
// import java.security.GeneralSecurityException;
// import java.util.concurrent.ExecutionException;

// @RestController
// @RequestMapping("/push")
// public class PushController {

//     private final PushService pushService;

//     public PushController() throws GeneralSecurityException {
//         this.pushService = new PushService();
//         // VAPID キーを設定 (事前に生成)
//         String publicKey = "BDtSdJ-595-GLPukMIt7t9HGcfgWQWQsMcpKTPWUGqOdfo4vGG9pIM4jAyOulTGsrioOd2lcoHAY8cK90_ZETxA";
//         String privateKey = "dMesI91E8a1wUL7Df6uK-THeZ59jC25j8VMPhElh7z4";
//         pushService.setPublicKey(publicKey);
//         pushService.setPrivateKey(privateKey);
//     }

//     // クライアントから購読情報を受け取るAPI
//     @PostMapping("/subscribe")
//     public void subscribe(@RequestBody Subscription subscription) {
//         // 本来はDBに保存する (ここでは省略)
//         System.out.println("Subscribed: " + subscription.endpoint);
//     }

//     // Push通知を送信するAPI
//     @PostMapping("/notify")
//     public void sendNotification(@RequestBody Subscription subscription)
//             throws GeneralSecurityException, IOException, JoseException, ExecutionException, InterruptedException {
//         String payload = "{\"title\":\"プッシュ通知\",\"body\":\"これはSpring Bootから送信された通知です。\"}";
//         Notification notification = new Notification(subscription, payload);
//         pushService.send(notification);
//     }
// }

