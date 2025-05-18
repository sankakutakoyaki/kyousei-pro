package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;

// import java.security.PrivateKey;
// import java.security.Signature;
// import java.util.Base64;

// public class VapidJwtSigner {

//     private static final String ALGORITHM = "ES256"; // ES256 (ECDSA)

//     // JWTの署名を生成するメソッド
//     public static String generateSignature(String header, String payload, PrivateKey privateKey) throws Exception {
//         // ヘッダーとペイロードを結合
//         String dataToSign = header + "." + payload;

//         // ECDSA署名を生成
//         Signature ecdsaSign = Signature.getInstance(ALGORITHM);
//         ecdsaSign.initSign(privateKey);
//         ecdsaSign.update(dataToSign.getBytes());

//         // 署名を生成
//         byte[] signature = ecdsaSign.sign();

//         // 署名をBase64URLエンコードして返す
//         return base64UrlEncode(signature);
//     }

//     // Base64URLエンコードのメソッド
//     private static String base64UrlEncode(byte[] data) {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
//     }
// }
