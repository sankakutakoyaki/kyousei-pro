package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;

// import java.security.interfaces.ECPrivateKey;
// import java.util.Base64;
// import java.util.Date;
// import java.util.ResourceBundle;

// import org.springframework.beans.factory.annotation.Autowired;

// import com.auth0.jwt.JWT;
// import com.auth0.jwt.algorithms.Algorithm;

// public class JWTbuild {
//     private static final Long EXPIRATION_TIME = 1000L * 60L * 60L * 1L;

//     // @Autowired
//     // LoginItem loginItem;
    
//     private Hash secret = new Hash();
    
//     //セッショントークン生成するメソッド
//     public String build(String userEmail) throws Exception{
        
//         //生成のため、日時データを取得する
//         Date issuedAt = new Date();
//         Date notBefore = new Date(issuedAt.getTime());
//         Date expiresAt = new Date(issuedAt.getTime() + EXPIRATION_TIME);
        
//         String secretKey = secret.getSecretKey();
        
//         //ヘッダー部へのアルゴリズムとハッシュ値を指定する
//         ECPrivateKey EC_PRIVATE_KEY = VapidJwtGenerator.createEcPrivateKey(ResourceBundle.getBundle("application").getString("vapid.privateKey"));
//         byte[] privateKeyBytes = VapidJwtGenerator.normalizeECPrivateKey(EC_PRIVATE_KEY.getS());
//         ECPrivateKey EC_PRIVATE_KEY2 = VapidJwtGenerator.createEcPrivateKey(Base64.getUrlEncoder().withoutPadding().encodeToString(privateKeyBytes));
//         Algorithm algorithm = Algorithm.ECDSA256(EC_PRIVATE_KEY2);
        
//         //トークンの生成
//         String token = JWT.create()
//                 .withIssuer(secret.getTokenIssuer())  //トークン発行者情報
//                 .withSubject(secret.getTokenSubject()) //トークンの主体
//                 .withAudience(userEmail)    //トークンの利用者（メールアドレスを用いてトークンを一意にする）
//                 .withIssuedAt(issuedAt)     //発行日時
//                 .withNotBefore(notBefore)   //トークンの有効期間開始時間
//                 .withExpiresAt(expiresAt)   //トークンの有効期間終了時間 今回はログアウト、セッションタイムアウトまで保持
//                 .sign(algorithm);           //アルゴリズム指定して、署名を行う
//         return token;
//     }
// }
