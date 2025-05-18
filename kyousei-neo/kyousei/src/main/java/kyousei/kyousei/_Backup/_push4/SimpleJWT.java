package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;
// package kyousei.kyousei.push;

// import java.nio.charset.StandardCharsets;
// import java.util.Base64;
// import javax.crypto.Mac;
// import javax.crypto.spec.SecretKeySpec;

// public class SimpleJWT {

//     // Base64エンコード（標準Base64）
//     private static String base64UrlEncode(byte[] input) {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
//     }

//     // HMACSHA256による署名
//     private static String hmacSHA256(String data, String key) throws Exception {
//         Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//         SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//         sha256_HMAC.init(secretKey);
//         byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
//         return base64UrlEncode(hash);
//     }

//     // JWT生成
//     public static String generateJWT(String payload, String secretKey) throws Exception {
//         // ヘッダーの作成
//         String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
//         String encodedHeader = base64UrlEncode(header.getBytes(StandardCharsets.UTF_8));

//         // ペイロードのエンコード
//         String encodedPayload = base64UrlEncode(payload.getBytes(StandardCharsets.UTF_8));

//         // 署名の生成
//         String signatureBase = encodedHeader + "." + encodedPayload;
//         String signature = hmacSHA256(signatureBase, secretKey);

//         // JWT作成（ヘッダー.ペイロード.署名）
//         return encodedHeader + "." + encodedPayload + "." + signature;
//     }

//     // public static void main(String[] args) {
//     //     try {
//     //         // ペイロードの例（通常、ここにはユーザー情報などのデータが入る）
//     //         String payload = "{\"sub\":\"1234567890\",\"name\":\"John Doe\",\"iat\":1516239022}";
//     //         // シークレットキー
//     //         String secretKey = "your-256-bit-secret";

//     //         // JWTの生成
//     //         String jwt = generateJWT(payload, secretKey);
//     //         System.out.println("Generated JWT: " + jwt);

//     //     } catch (Exception e) {
//     //         e.printStackTrace();
//     //     }
//     // }
// }
