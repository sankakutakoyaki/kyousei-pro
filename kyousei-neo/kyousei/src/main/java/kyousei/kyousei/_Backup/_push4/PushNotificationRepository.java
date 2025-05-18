package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.stereotype.Repository;

// @Repository
// public class PushNotificationRepository {
//     private List<SubscriptionRequest> list = new ArrayList<>();

//     public SubscriptionRequest findByEndpoint(String endpoint){
//         boolean result = false;
//         for (SubscriptionRequest subscription : list) {
//             if (subscription.getEndpoint() == endpoint) result = true;
//         }
//         if (result) {
//             return null;
//         } else {
//             return new SubscriptionRequest();
//         }
//     }
//     public void save(SubscriptionRequest subscription){
//         list.add(subscription);
//     }
//     public List<SubscriptionRequest> get() {
//         return list;
//     }
// }
