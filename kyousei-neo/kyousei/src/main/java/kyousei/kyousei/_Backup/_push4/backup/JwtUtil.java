package kyousei.kyousei._Backup._push4.backup;
// package kyousei.kyousei.push.backup;

// import java.security.interfaces.ECPrivateKey;
// import java.security.interfaces.ECPublicKey;
// import java.util.Base64;
// import java.util.Date;

// import com.auth0.jwt.JWT;
// import com.auth0.jwt.JWTVerifier;
// import com.auth0.jwt.algorithms.Algorithm;
// import com.auth0.jwt.interfaces.DecodedJWT;

// public class JwtUtil {

//     /**
//      * JWT (ES256) を生成する（DER 署名を (r, s) 形式へ変換）
//      * @param privateKey
//      * @param publicKey
//      * @param audience
//      * @param subject
//      * @return
//      * @throws Exception
//      */
//     public static String createES256JWT(ECPrivateKey privateKey, ECPublicKey publicKey, String audience, String subject) throws Exception {
//         Algorithm algorithm = Algorithm.ECDSA256(publicKey, privateKey);
//         // System.out.println("Algorithm: " + algorithm.getName());

//         String jwt = JWT.create()
//                 .withAudience(audience)
//                 .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))  // 1時間後に失効
//                 .withSubject(subject)
//                 .sign(algorithm);

//         // JWTがDER形式かチェック
//         int result = checkSignatureFormat(jwt);

//         switch (result) {
//             case 1:
//                 // (r, s) 形式なのでそのまま返信
//                 return jwt;
//             case 2:
//                 // DER 形式の署名を (r, s) 形式に変換
//                 return convertJWTSignature(jwt);        
//             default:
//                 return "";
//         }
//     }

//     /**
//      * DER 形式の署名を (r, s) 形式に変換
//      * @param derSignature
//      * @return
//      */
//     public static byte[] convertDERToRS(byte[] derSignature) {
//         if (derSignature[0] != 0x30) {
//             throw new IllegalArgumentException("Invalid DER signature format");
//         }

//         int rLength = derSignature[3];
//         int sIndex = 4 + rLength + 1;
//         int sLength = derSignature[sIndex - 1];

//         byte[] r = new byte[32];
//         byte[] s = new byte[32];

//         System.arraycopy(derSignature, 4, r, 32 - rLength, rLength);
//         System.arraycopy(derSignature, sIndex, s, 32 - sLength, sLength);

//         byte[] rsSignature = new byte[64];
//         System.arraycopy(r, 0, rsSignature, 0, 32);
//         System.arraycopy(s, 0, rsSignature, 32, 32);

//         return rsSignature;
//     }

//     /**
//      * JWT署名を変換する
//      * @param jwt
//      * @return
//      */
//     public static String convertJWTSignature(String jwt) {
//         String[] parts = jwt.split("\\.");
//         byte[] derSignature = Base64.getUrlDecoder().decode(parts[2]);
//         byte[] rsSignature = convertDERToRS(derSignature);
//         String encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(rsSignature);
//         return parts[0] + "." + parts[1] + "." + encodedSignature;
//     }

//     /**
//      * JWTがDER形式かチェック
//      * @param jwt
//      * @return
//      */
//     public static int checkSignatureFormat(String jwt) {
//         String[] parts = jwt.split("\\.");
//         if (parts.length != 3) {
//             throw new IllegalArgumentException("Invalid JWT format");
//         }

//         byte[] signature = Base64.getUrlDecoder().decode(parts[2]);

//         // System.out.println("🔍 署名のバイト数: " + signature.length);

//         if (signature.length == 64) {
//             // System.out.println("✅ JWT の署名は正しい (r, s) 形式です。変換不要！");
//             return 1;
//         } else if (signature.length > 64 && signature[0] == 0x30) {
//             // System.out.println("⚠️ JWT の署名は DER 形式です。変換が必要！");
//             return 2;
//         } else {
//             // System.out.println("❌ 署名が不正な形式です！");
//             return 3;
//         }
//     }

//     public static void verifierJWT(ECPublicKey publicKey, String audience, String jwt) {
//         JWTVerifier verifier = JWT.require(Algorithm.ECDSA256((ECPublicKey) publicKey, null))
//                 .withAudience(audience)
//                 .build();
//         DecodedJWT decodedJWT = verifier.verify(jwt);
//         System.out.println("JWT 署名検証 成功！ペイロード: " + decodedJWT.getPayload());
//     }
// }
