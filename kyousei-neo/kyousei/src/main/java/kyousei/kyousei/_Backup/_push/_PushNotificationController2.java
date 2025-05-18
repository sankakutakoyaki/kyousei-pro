package kyousei.kyousei._Backup._push;
// package kyousei.kyousei._push;

// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;

// import kyousei.kyousei.entity.data.SimpleData;
// import lombok.RequiredArgsConstructor;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.ResourceBundle;

// @RestController
// @RequiredArgsConstructor
// public class _PushNotificationController2 {
//     private final _PushSubscriptionRepository pushSubscriptionRepository;
//     private final _PushNotificationService pushNotificationService;
//     private static final String VAPID_PRIVATE_KEY_BASE64 = ResourceBundle.getBundle("application").getString("vapid.privateKey");
//     private static final String VAPID_PUBLIC_KEY_BASE64 = ResourceBundle.getBundle("application").getString("vapid.publicKey");

//     // @PostMapping("/api/push/send")
//     public String sendPushNotification() throws Exception {
//         List<_PushSubscription> endpoints = pushSubscriptionRepository.findAll();
//         for (_PushSubscription pushSubscription : endpoints) {
//             String endpoint = pushSubscription.getEndpoint();System.out.println(endpoint);
//             pushNotificationService.sendPushNotification("{\"title\":\"Hello!\",\"body\":\"This is a test push notification.\"}", endpoint);

//                     // プッシュ通知の受信者のエンドポイントと認証情報
//             // String endpoint = "https://fcm.googleapis.com/fcm/send"; // 例: FCMエンドポイント
//             String publicKey = VAPID_PUBLIC_KEY_BASE64;  // クライアントから取得した公開鍵
//             String privateKey = VAPID_PRIVATE_KEY_BASE64;  // サーバーで保持する秘密鍵

//             // 通知の内容をMapで作成
//             Map<String, String> payload = new HashMap<>();
//             payload.put("title", "プッシュ通知のタイトル");
//             payload.put("body", "通知の内容");

//             // 認証ヘッダーとトークン生成
//             String vapidToken = generateVapidToken(privateKey, publicKey); // VAPIDトークンを生成

//             // HTTPヘッダー
//             HttpHeaders headers = new HttpHeaders();
//             headers.set("Authorization", "Bearer " + vapidToken);
//             headers.set("Content-Type", "application/json");

//             // HTTPボディ（通知内容）
//             String jsonPayload = mapToJson(payload);

//             HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

//             // サーバーにPOSTリクエストを送信
//             RestTemplate restTemplate = new RestTemplate();
//             restTemplate.postForObject(endpoint, entity, String.class);
     
//         }


//         return "プッシュ通知を送信しました";
//     }

//     // VAPIDトークンを生成するメソッド
//     private String generateVapidToken(String privateKey, String publicKey) {
//         // JWT作成処理を実装（JWTライブラリを使ってVAPIDトークンを生成）
//         // ここでは簡略化のため、ダミーでトークンを返す
//         return "your-generated-vapid-token";
//     }

//     // Map を JSON に変換するメソッド
//     private String mapToJson(Map<String, String> map) {
//         StringBuilder jsonBuilder = new StringBuilder("{");
//         for (Map.Entry<String, String> entry : map.entrySet()) {
//             if (jsonBuilder.length() > 1) {
//                 jsonBuilder.append(",");
//             }
//             jsonBuilder.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
//         }
//         jsonBuilder.append("}");
//         return jsonBuilder.toString();
//     }
// // }






// // import org.springframework.web.bind.annotation.*;

// // import kyousei.kyousei.entity.data.PushSubscription;
// // import kyousei.kyousei.entity.data.SimpleData;
// // import kyousei.kyousei.repository.PushSubscriptionRepository;
// // import kyousei.kyousei.service.PushNotificationService;
// // import lombok.RequiredArgsConstructor;

// // import org.springframework.stereotype.Controller;
// // import org.springframework.http.ResponseEntity;

// // import java.util.List;

// // import org.json.JSONObject;

// // @Controller
// // @RequiredArgsConstructor
// // public class PushNotificationController {

// //     // // サブスクリプション情報を受け取るエンドポイント
// //     // @PostMapping("/api/push/subscribe")
// //     // public ResponseEntity<String> subscribe(@RequestBody String subscriptionJson) {
// //     //     try {
// //     //         JSONObject subscription = new JSONObject(subscriptionJson);
// //     //         // サブスクリプション情報をデータベースに保存またはインメモリに保持
// //     //         // ここでは単純にコンソールに出力するだけにしています
// //     //         System.out.println("Received subscription: " + subscription);

// //     //         // 必要であればサブスクリプション情報をDBに保存
// //     //         // Push通知を送る準備ができたことを確認

// //     //         return ResponseEntity.ok("Subscription successful");
// //     //     } catch (Exception e) {
// //     //         e.printStackTrace();
// //     //         return ResponseEntity.status(500).body("Error processing subscription");
// //     //     }
// //     // }

//     // private final PushSubscriptionRepository pushSubscriptionRepository;
//     // private final PushNotificationService pushNotificationService;

//     // @PostMapping("/api/push/subscribe")
//     @ResponseBody
//     public SimpleData subscribe(@RequestBody _PushSubscription pushSubscription) {
//     // public ResponseEntity<String> subscribe(@RequestBody String subscriptionJson) {
//         try {
//             // サブスクリプション情報をJSONで解析
//             // JSONObject subscription = new JSONObject(subscriptionJson);
//             // String endpoint = subscription.getString("endpoint");
//             // String p256dh = subscription.getJSONObject("keys").getString("p256dh");
//             // String auth = subscription.getJSONObject("keys").getString("auth");

//             // // サブスクリプション情報をデータベースに保存
//             // PushSubscription pushSubscription = new PushSubscription();
//             // pushSubscription.setEndpoint(endpoint);
//             // pushSubscription.setP256dh(p256dh);
//             // pushSubscription.setAuth(auth);

//             // データベースに保存
//             SimpleData simpleData = new SimpleData();
//             int result = pushSubscriptionRepository.save(pushSubscription);
//             if (result > 0) {
//                 simpleData.setNumber(200);
//                 simpleData.setText("Push notification sent successfully");
//             } else {
//                 simpleData.setNumber(500);
//                 simpleData.setText("Error processing notification");
//             }
//             return simpleData;
//             // return ResponseEntity.ok("Subscription successful");

//         } catch (Exception e) {
//             e.printStackTrace();
//             SimpleData simpleData = new SimpleData();
//             simpleData.setNumber(500);
//             simpleData.setText("Error processing subscription");
//             return simpleData;
//             // return ResponseEntity.status(500).body("Error processing subscription");
//         }
//     }
// }
// //     @PostMapping("/api/push/send")
// //     @ResponseBody
// //     // public ResponseEntity<String> sendPushNotification(@RequestBody String message) {
// //     public void sendPushNotification(@RequestBody SimpleData message) {
// //         try {
// //             List<PushSubscription> endpoints = pushSubscriptionRepository.findAll();
// //             for (PushSubscription pushSubscription : endpoints) {
// //                 String endpoint = pushSubscription.getEndpoint();System.out.println(endpoint);
// //                 pushNotificationService.sendPushNotification("{\"title\":\"Hello!\",\"body\":\"This is a test push notification.\"}", endpoint);
// //             }
// //         } catch (Exception e) {
// //             e.printStackTrace();
// //         }
// // //         try {
// // //             // 送信したいPush通知のメッセージ
// // //             String pushMessage = "You have a new message: " + message.getText();

// // //             // すべてのサブスクリプション情報をデータベースから取得
// // //             for (PushSubscription subscription : subscriptionRepository.findAll()) {
// // //                 // サブスクリプションごとにPush通知を送信
// // //                 pushNotificationService.sendPushNotification(subscription, pushMessage);
// // //             }
// // //             SimpleData simpleData = new SimpleData();
// // //             simpleData.setNumber(200);
// // //             simpleData.setText("Push notification sent successfully");
// // //             return simpleData;
// // //             // return ResponseEntity.ok("Push notification sent successfully");
// // //         } catch (Exception e) {
// // //             e.printStackTrace();
// // //             SimpleData simpleData = new SimpleData();
// // //             simpleData.setNumber(500);
// // //             simpleData.setText("Error processing subscription");
// // //             return simpleData;
// // //             // return ResponseEntity.status(500).body("Error sending push notification");
// // //         }
// //     }
// // }
