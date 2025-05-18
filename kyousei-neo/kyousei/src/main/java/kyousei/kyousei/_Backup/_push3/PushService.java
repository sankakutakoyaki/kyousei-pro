package kyousei.kyousei._Backup._push3;
// package kyousei.kyousei._push3;

// import java.security.KeyPair;
// import java.util.ResourceBundle;

// public class PushService {

//     private static final String VAPID_PUBLIC_KEY = ResourceBundle.getBundle("application").getString("vapid.publicKey.base64"); // VAPID 公開鍵
//     private static final String VAPID_PRIVATE_KEY = ResourceBundle.getBundle("application").getString("vapid.privateKey.base64"); // VAPID 秘密鍵

//     private final KeyPair vapidKeyPair;

//     public PushService() throws Exception {
//         this.vapidKeyPair = VapidKeyHelper.createKeyPairFromBase64(VAPID_PUBLIC_KEY, VAPID_PRIVATE_KEY);
//     }
//     public KeyPair getVapidKeyPair() {
//         return vapidKeyPair;
//     }
//     // // サブスクリプション情報をデータベースに保存するメソッド
//     // public void saveSubscription(String endpoint, String p256dh, String auth) {
//     //     // すでに同じエンドポイントが存在する場合は更新、存在しない場合は新規作成
//     //     SubscriptionRequest subscription = pushRepository.findByEndpoint(endpoint);
//     //     if (subscription == null) {
//     //         subscription = new SubscriptionRequest();
//     //         subscription.setEndpoint(endpoint);
//     //         subscription.setP256dh(p256dh);
//     //         subscription.setAuth(auth);
//     //         // サブスクリプションを保存
//     //         pushRepository.save(subscription);
//     //     }
//     // }
// }
