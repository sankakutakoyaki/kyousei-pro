package kyousei.kyousei._Backup._push2;
// package kyousei.kyousei.push;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// @Service
// public class PushSubscriptionService {

//     @Autowired
//     private PushSubscriptionRepository pushSubscriptionRepository;

//     // サブスクリプション情報をデータベースに保存するメソッド
//     public void saveSubscription(String endpoint, String p256dh, String auth) {
//         // すでに同じエンドポイントが存在する場合は更新、存在しない場合は新規作成
//         PushSubscription subscription = pushSubscriptionRepository.findByEndpoint(endpoint);
//         if (subscription == null) {
//             subscription = new PushSubscription(endpoint, p256dh, auth);
//         } else {
//             subscription.setP256dhKey(p256dh);
//             subscription.setAuthKey(auth);
//         }

//         // サブスクリプションを保存
//         pushSubscriptionRepository.save(subscription);
//     }
// }
