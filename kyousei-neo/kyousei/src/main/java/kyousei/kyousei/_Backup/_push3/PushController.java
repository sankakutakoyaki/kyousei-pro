package kyousei.kyousei._Backup._push3;
// package kyousei.kyousei._push3;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import java.security.KeyPair;
// import java.util.Base64;
// import java.util.Map;
// import java.util.ResourceBundle;
// import java.util.concurrent.ConcurrentHashMap;

// @RestController
// @RequestMapping("/push")
// public class PushController {

//     private final KeyPair serverKeyPair;
//     // private final String VAPID_PUBLIC_KEY; // VAPID 公開鍵
//     // private final String VAPID_PRIVATE_KEY; // VAPID 秘密鍵

//     public PushController() throws Exception {
//         // String VAPID_PUBLIC_KEY = ResourceBundle.getBundle("application").getString("vapid.publicKey"); // VAPID 公開鍵
//         // String VAPID_PRIVATE_KEY = ResourceBundle.getBundle("application").getString("vapid.privateKey"); // VAPID 秘密鍵

//         this.serverKeyPair = VapidKeyGenerator.generateVapidKeyPair();
//         // PushService pushService = new PushService();
//         // this.serverKeyPair = pushService.getVapidKeyPair();
//     }

//     private final Map<String, SubscriptionRequest> subscriptions = new ConcurrentHashMap<>();

//     @PostMapping("/subscribe")
//     public String subscribe(@RequestBody SubscriptionRequest request) {
//         if (request.getEndpoint() == null || request.getP256dh() == null || request.getAuth() == null) {
//             return "Invalid subscription data";
//         }
//         subscriptions.put(request.getEndpoint(), request);
//         return "Subscription saved!";
//     }
//     @GetMapping("/vapidPublicKey")
//     public String getVapidPublicKey() throws Exception {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(serverKeyPair.getPublic().getEncoded());
//     }
//     @PostMapping("/send")
//     public String sendNotification(@RequestBody SubscriptionRequest request) {
//         try {
//             // pushService.sendPushNotification(request.getEndpoint(), request.getP256dh(), request.getAuth(), "Hello, this is a test push!");
//             return "Notification sent!";
//         } catch (Exception e) {
//             return "Error sending notification: " + e.getMessage();
//         }
//     }
// }