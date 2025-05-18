package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import lombok.RequiredArgsConstructor;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.ResourceBundle;
// import java.util.Base64;

// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/api/push")
// public class PushNotificationController {

//     private final PushNotificationService pushNotificationService;
//     private final PushNotificationRepository pushNotificationRepository;

//     // private static final String VAPID_PUBLIC_KEY = ResourceBundle.getBundle("application").getString("vapid.publicKey"); // VAPID 公開鍵
//     // private static final String VAPID_PRIVATE_KEY = ResourceBundle.getBundle("application").getString("vapid.privateKey"); // VAPID 秘密鍵
//     // private static final String VAPID_SUBJECT = ResourceBundle.getBundle("application").getString("vapid.subject"); //

//     // public PushNotificationController(PushNotificationService pushNotificationService) {
//     //     this.pushNotificationService = pushNotificationService;
//     // }

//     private final List<SubscriptionRequest> subscriptions = new ArrayList<>();

//     // @PostMapping("/subscribe")
//     // public void subscribe(@RequestBody SubscriptionRequest subscription) {
//     //     subscriptions.add(subscription);
//     // }

//     @PostMapping("/send")
//     public void sendNotification(@RequestParam String message, @RequestParam String csrftoken) throws Exception {
//         for (SubscriptionRequest subscription : subscriptions) {
//             sendPushNotification(subscription, message, csrftoken);
//         }
//     }

//     private void sendPushNotification(SubscriptionRequest subscription, String message, String csrfToken) {
//         try {
//             byte[] p256dhKey = Base64.getUrlDecoder().decode(subscription.getP256dh());

//             pushNotificationService.sendPushNotification(subscription.getEndpoint(), p256dhKey, message, csrfToken);
//         } catch (Exception e) {
//             e.printStackTrace();  // 適切なエラーハンドリングを行うべき
//         }
//     }

//     // クライアントから Pushサブスクリプション情報を受け取る
//     @PostMapping("/subscribe")
//     public ResponseEntity<String> subscribe(@RequestBody SubscriptionRequest subscriptionRequest) {
//         // PushNotificationRepository pushNotificationRepository = new PushNotificationRepository();
//         SubscriptionRequest result = pushNotificationRepository.findByEndpoint(subscriptionRequest.getEndpoint());
//         if (result.getEndpoint() == null) {
//             pushNotificationRepository.save(subscriptionRequest);
//             subscriptions.add(subscriptionRequest);
//         }
//         return ResponseEntity.ok("Push notification sent!");
//     }

//     // VAPID公開鍵を取得
//     @GetMapping("/vapid-public-key")
//     public String getVapidPublicKey() {
//         try {
//             return ResourceBundle.getBundle("application").getString("vapid.publicKey"); // VAPID 秘密鍵
//         } catch (Exception e) {
//             return "Error retrieving VAPID public key: ";
//         }
//     }




//     private static ECPublicKey EC_PUBLIC_KEY;
//     private static ECPrivateKey EC_PRIVATE_KEY;

//     private String generateVapidAuthHeader(String audience) throws Exception {
//         // ① URL セーフ Base64 をデコード
//         byte[] publicKeyBytes = Base64.getUrlDecoder().decode(VAPID_PUBLIC_KEY);

//         // 2. 鍵の形式を確認
//         if (publicKeyBytes.length == 65 && publicKeyBytes[0] == 0x04) {

//             // 3. X座標とY座標を抽出
//             byte[] xBytes = new byte[32];
//             byte[] yBytes = new byte[32];
//             System.arraycopy(publicKeyBytes, 1, xBytes, 0, 32);
//             System.arraycopy(publicKeyBytes, 33, yBytes, 0, 32);

//             BigInteger x = new BigInteger(1, xBytes);
//             BigInteger y = new BigInteger(1, yBytes);

//             // 4. ECパラメータを取得（secp256r1 曲線）
//             AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
//             parameters.init(new ECGenParameterSpec("secp256r1"));
//             ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);

//             // 5. ECPoint を作成
//             ECPoint ecPoint = new ECPoint(x, y);

//             // 6. 公開鍵仕様を作成
//             ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(ecPoint, ecParameters);

//             // 7. 鍵ファクトリを使用して公開鍵を生成
//             KeyFactory keyFactory = KeyFactory.getInstance("EC");
//             EC_PUBLIC_KEY = (ECPublicKey) keyFactory.generatePublic(publicKeySpec);

//             System.out.println("復元された公開鍵: " + EC_PUBLIC_KEY);
//         } else {
//             System.err.println("サポートされていない鍵形式または無効な鍵長です。");
//         }

//         try {
//             // Base64エンコードされたEC秘密鍵
//             // String base64PrivateKey = "HLWQwpHfE5dHi1Ytv-hYLmdZfrmP1DUr71vTXKuYeuI";  // ここにBase64形式の秘密鍵を挿入
            
//             // Base64デコードを行う
//             byte[] decodedPrivateKey = Base64.getUrlDecoder().decode(VAPID_PRIVATE_KEY);
//             // デコードされた秘密鍵が空でないかを確認
//             if (decodedPrivateKey == null || decodedPrivateKey.length == 0) {
//                 throw new IllegalArgumentException("Decoded private key is null or empty");
//             }
//             // 4. ECパラメータを取得（secp256r1 曲線）
//             AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
//             parameters.init(new ECGenParameterSpec("secp256r1"));
//             ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);

//             // KeyFactoryを使って秘密鍵の生成に必要なパラメータを作成
//             KeyFactory keyFactory = KeyFactory.getInstance("EC");

//             // ECPrivateKeySpecを使って秘密鍵を作成
//             ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(new BigInteger(1, decodedPrivateKey), ecParameters);

//             // EC秘密鍵を生成
//             EC_PRIVATE_KEY = (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);

//             // 結果を出力
//             System.out.println("EC Private Key: " + EC_PRIVATE_KEY.getS());

//         } catch (Exception e) {
//             e.printStackTrace();
//         }

//         String header = Base64.getUrlEncoder().withoutPadding().encodeToString(
//                 "{\"alg\":\"ES256\",\"typ\":\"JWT\"}".getBytes()
//         );

//         long expiry = System.currentTimeMillis() / 1000 + 12 * 60 * 60;
//         String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(
//                 String.format("{\"aud\":\"%s\",\"exp\":%d,\"sub\":\"%s\"}", audience, expiry, VAPID_SUBJECT).getBytes()
//         );

//         // final Signature signature = Signature.getInstance("SHA256withECDSAinP1363Format");
//         final Signature signature = Signature.getInstance("SHA256withECDSA");
//         signature.initSign(EC_PRIVATE_KEY);
//         signature.update((header + "." + payload).getBytes());




//         byte[] signedData = signature.sign();
        
//         // 3. 署名（R,S）を取得
//         // ECDSA署名はR,Sの2つの整数で構成される
//         BigInteger r = new BigInteger(1, signedData, 0, 32); // R部分
//         BigInteger s = new BigInteger(1, signedData, 32, 32); // S部分

//         // 4. RとSを連結し、Base64Urlエンコード
//         byte[] rBytes = r.toByteArray();
//         byte[] sBytes = s.toByteArray();

//         // 署名のフォーマットに合わせてBase64Urlエンコード
//         String signatureBase64Url = base64UrlEncode(concatenate(rBytes, sBytes));

//         // System.out.println("private: " + VAPID_PRIVATE_KEY);
//         // System.out.println("JWT Signature (Base64Url): " + signatureBase64Url);

//         return "vapid t=" + header + "." + payload + "." + signatureBase64Url + ", k=" + Base64.getUrlEncoder().withoutPadding().encodeToString(publicKeyBytes);


//         // return "vapid t=" + header + "." + payload + "." + signature + ", k=" + Base64.getUrlEncoder().withoutPadding().encodeToString(publicKeyBytes);
//     }

//     // Base64Urlエンコード
//     private static String base64UrlEncode(byte[] data) {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
//     }

//     // 2つのバイト配列を連結
//     private static byte[] concatenate(byte[] rBytes, byte[] sBytes) {
//         byte[] result = new byte[rBytes.length + sBytes.length];
//         System.arraycopy(rBytes, 0, result, 0, rBytes.length);
//         System.arraycopy(sBytes, 0, result, rBytes.length, sBytes.length);
//         return result;
//     }
// }
