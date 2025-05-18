package kyousei.kyousei._Backup._push2;
// packakyousei.kyousei._push2ush;

// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/push")
// public class PushController {
//     private final WebPushService pushService;

//     public PushController() throws Exception {
//         this.pushService = new WebPushService();
//     }

//     @PostMapping("/send")
//     public String sendNotification(@RequestBody SubscriptionRequest request) {
//         try {
//             pushService.sendPushNotification(request.getEndpoint(), request.getP256dh(), request.getAuth(), "Hello, this is a test push!");
//             return "Notification sent!";
//         } catch (Exception e) {
//             return "Error sending notification: " + e.getMessage();
//         }
//     }
// }
