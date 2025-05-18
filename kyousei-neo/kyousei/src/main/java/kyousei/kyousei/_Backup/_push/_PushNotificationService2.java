package kyousei.kyousei._Backup._push;
// package kyousei.kyousei._push;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.http.*;
// import org.springframework.web.client.RestTemplate;

// import java.util.HashMap;
// import java.util.Map;

// @Service
// public class _PushNotificationService2 {

//     @Value("${fcm.server.key}")
//     private String serverKey;

//     @Value("${fcm.url}")
//     private String fcmUrl;

//     public void sendPushNotification(String to, String title, String body) {
//         // 通知のペイロードを作成
//         Map<String, Object> payload = new HashMap<>();
//         Map<String, String> notification = new HashMap<>();
//         notification.put("title", title);
//         notification.put("body", body);
//         payload.put("notification", notification);
//         payload.put("to", to);

//         // HTTPヘッダーの設定
//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Authorization", "key=" + serverKey);
//         headers.setContentType(MediaType.APPLICATION_JSON);

//         // HTTPリクエストを作成
//         HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

//         // FCMにPOSTリクエストを送信
//         RestTemplate restTemplate = new RestTemplate();
//         ResponseEntity<String> response = restTemplate.exchange(fcmUrl, HttpMethod.POST, request, String.class);

//         if (response.getStatusCode() == HttpStatus.OK) {
//             System.out.println("Notification sent successfully.");
//         } else {
//             System.out.println("Failed to send notification.");
//         }
//     }
// }
