package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;

// import java.nio.charset.StandardCharsets;
// import java.security.*;
// import java.security.spec.*;
// import java.time.Instant;
// import java.util.Base64;

// public class JwtGenerator {

//     private static final String EC_ALGORITHM = "EC";
//     private static final String SIGN_ALGORITHM = "SHA256withECDSA";

//     /**
//      * BASE64URLエンコードされた秘密鍵から ECDSA秘密鍵を復元
//      */
//     public static PrivateKey loadPrivateKey(String base64UrlKey) throws Exception {
//         byte[] keyBytes = Base64.getUrlDecoder().decode(base64UrlKey);
//         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
//         KeyFactory keyFactory = KeyFactory.getInstance(EC_ALGORITHM);
//         return keyFactory.generatePrivate(keySpec);
//     }

//     /**
//      * JWT ヘッダー・ペイロードの BASE64URL エンコード
//      */
//     private static String base64UrlEncode(String data) {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
//     }

//     /**
//      * JWT の署名を作成 (ES256)
//      */
//     private static String sign(PrivateKey privateKey, String data) throws Exception {
//         Signature signature = Signature.getInstance(SIGN_ALGORITHM);
//         signature.initSign(privateKey);
//         signature.update(data.getBytes(StandardCharsets.UTF_8));
//         byte[] signatureBytes = signature.sign();
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
//     }

//     /**
//      * JWT の生成
//      */
//     public static String generateJwt(String base64UrlPrivateKey, String aud, String sub) throws Exception {
//         PrivateKey privateKey = loadPrivateKey(base64UrlPrivateKey);

//         // JWT ヘッダー
//         String header = base64UrlEncode("{\"typ\":\"JWT\",\"alg\":\"ES256\"}");

//         // JWT ペイロード
//         long now = Instant.now().getEpochSecond();
//         String payload = base64UrlEncode("{\"aud\":\"" + aud + "\",\"exp\":" + (now + 86400) + ",\"sub\":\"" + sub + "\"}");

//         // 署名対象のデータ
//         String message = header + "." + payload;

//         // 署名
//         String signature = sign(privateKey, message);

//         // JWT を生成
//         return message + "." + signature;
//     }
// }
