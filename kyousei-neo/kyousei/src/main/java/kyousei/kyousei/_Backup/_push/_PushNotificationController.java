package kyousei.kyousei._Backup._push;
// package kyousei.kyousei._push;

// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.client.RestTemplate;

// import java.util.HashMap;
// import java.util.Map;

// @RestController
// // @RequestMapping("/push-notification")
// public class _PushNotificationController {
//     // プッシュ通知の送信エンドポイント
//     private static final String PUSH_SERVER_URL = "https://fcm.googleapis.com/fcm/send"; // FCMなどのURL

//     @PostMapping("/send")
//     @ResponseBody
//     public String sendPushNotification(@RequestBody _PushNotificationRequest request) {
//         // サーバーに送信する通知の内容
//         Map<String, String> payload = new HashMap<>();
//         payload.put("title", request.getTitle());
//         payload.put("body", request.getBody());

//         // JSON形式に変換
//         String jsonPayload = mapToJson(payload);

//         // HTTPヘッダーの設定
//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Authorization", "Bearer " + request.getVapidToken());  // VAPIDトークンを使用
//         headers.set("Content-Type", "application/json");

//         HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

//         // HTTPリクエストを送信
//         RestTemplate restTemplate = new RestTemplate();
//         restTemplate.postForObject(PUSH_SERVER_URL, entity, String.class);

//         return "プッシュ通知を送信しました";
//     }

//     // MapをJSONに変換
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
// }
