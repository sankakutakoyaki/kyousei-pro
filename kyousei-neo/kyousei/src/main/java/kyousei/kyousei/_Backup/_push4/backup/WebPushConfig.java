package kyousei.kyousei._Backup._push4.backup;
// package kyousei.kyousei.push;

// import java.security.Security;
// import java.util.ResourceBundle;

// import org.bouncycastle.jce.provider.BouncyCastleProvider;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.context.properties.bind.ConstructorBinding;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import lombok.RequiredArgsConstructor;
// import nl.martijndwars.webpush.PushService;

// @Configuration
// @RequiredArgsConstructor
// public class WebPushConfig {
//     private final WebPushProperties webPushProperties;
//     // private final static String base64UrlPrivateKey = ResourceBundle.getBundle("application").getString("vapid.privateKey"); // VAPID 秘密鍵
//     private final static String base64UrlPublicKey = ResourceBundle.getBundle("application").getString("vapid.publicKey"); // VAPID 公開鍵
//     // private static final String base64Urlsubject = ResourceBundle.getBundle("application").getString("vapid.subject"); //
//     // private final static String audience = ResourceBundle.getBundle("application").getString("vapid.audience"); // origin
//     /**
//      * WebPush設定用プロパティ
//      * ここでは application.yml に定義したものを取得することを想定しているが、どんな方法でも問題ない
//      *
//      * @param fcmApiKey 送信用FCM APIキー
//      */
//     @ConfigurationProperties(prefix = "application")
//     // public record WebPushProperties(String fcmApiKey) {}
//     public record WebPushProperties(String vapidPublicKey) {}

//     /**
//      * PushServiceのBean化
//      * @return 設定済みPushService
//      */
//     @Bean
//     public PushService pushService() {
//         Security.addProvider(new BouncyCastleProvider());
//         return new PushService(this.webPushProperties.vapidPublicKey());
//     }
// }