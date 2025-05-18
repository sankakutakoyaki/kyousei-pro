package kyousei.kyousei._Backup._push;
// package kyousei.kyousei._push;

// import nl.martijndwars.webpush.Notification;
// import nl.martijndwars.webpush.PushService;
// import nl.martijndwars.webpush.Utils;
// import org.apache.http.HttpResponse;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import java.nio.charset.StandardCharsets;
// import java.security.PublicKey;
// import java.util.Base64;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// @Service
// public class _PushService {
//     // private final PushService pushService;

//     // public void sendPush(
//     //     @Value("${vapid.publicKey}") String publicKey,
//     //     @Value("${vapid.privateKey}") String privateKey,
//     //     @Value("${vapid.subject}") String subject) throws Exception {
//     //     pushService = new PushService(publicKey, privateKey, subject);
//     // }

//     public void sendPush(String endpoint, String message) {
//         System.out.println("送信先: " + endpoint);
//         System.out.println("メッセージ: " + message);
//     }
// }