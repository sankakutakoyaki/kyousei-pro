package kyousei.kyousei._Backup._push;
// package kyousei.kyousei._push;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.concurrent.CopyOnWriteArrayList;

// import org.springframework.stereotype.Repository;

// import kyousei.kyousei.entity.data.SimpleData;
// import lombok.RequiredArgsConstructor;

// @Repository
// public class _PushSubscriptionRepository {

//     private final List<_PushSubscription> endpoints = new CopyOnWriteArrayList<>();

//     // public PushSubscriptionRepository findByEndpoint(String endpoint){
//     //     return new PushSubscriptionRepository();
//     // }

//     public int save(_PushSubscription pushSubscription) {
//         System.out.println(pushSubscription);
//         endpoints.add(pushSubscription);
//         System.out.println("test-save" + endpoints);
//         return 1;
//     }

//     public List<_PushSubscription> findAll() {
//         // List<PushSubscription> list = new ArrayList<>();
//         // return list;
//         System.out.println("test-find" + endpoints);
//         return endpoints;
//     }
// }
