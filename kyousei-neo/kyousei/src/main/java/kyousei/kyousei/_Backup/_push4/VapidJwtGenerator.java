package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;

// import java.math.BigInteger;
// import java.security.*;
// import java.security.interfaces.ECPrivateKey;
// import java.security.spec.*;
// import java.util.Arrays;
// import java.util.Base64;
// import java.util.ResourceBundle;

// public class VapidJwtGenerator {
//     public static ECPrivateKey createEcPrivateKey(String base64UrlPrivateKey) throws Exception {
//         // 43文字の秘密鍵 (Base64URL)
//         // String base64UrlPrivateKey = "jdlK9a3Ls1K8Qm2X3nY4F5CmU8a4XxK9Nj7aPQyZK0";
//         // String base64UrlPrivateKey = ResourceBundle.getBundle("application").getString("vapid.privateKey");

//         // 1. Base64URL をデコード
//         byte[] bytes = base64UrlDecode(base64UrlPrivateKey);



//         // 2. ECPrivateKey に変換
//         ECPrivateKey key = convertToECPrivateKey(bytes);

//         BigInteger sValue = ((ECPrivateKey) key).getS();
//         byte[] privateKeyBytes = sValue.toByteArray();

//         ECPrivateKey privateKey = convertToECPrivateKey(privateKeyBytes);


//         // 3. 結果を表示
//                 // 3. 秘密鍵の `S` 値（整数）を確認
//                 BigInteger sValue2 = ((ECPrivateKey) privateKey).getS();
//                 System.out.println("ECPrivateKey S Value: " + sValue2.toString(16)); // 16進数で表示
        
//                 // 4. `S` 値を Base64URL エンコード
//                 String base64EncodedS = Base64.getUrlEncoder().withoutPadding().encodeToString(sValue.toByteArray());
//                 System.out.println("Base64URL Encoded S Value: " + base64EncodedS + " (Length: " + base64EncodedS.length() + ")");
        
//                 // 5. DERエンコード後のバイト長を確認
//                 byte[] encodedKey = privateKey.getEncoded();
//                 System.out.println("Encoded ECPrivateKey Length: " + encodedKey.length + " bytes");
        
//                 // 6. DERエンコードをBase64で出力
//                 String base64EncodedKey = Base64.getEncoder().encodeToString(encodedKey);
//                 System.out.println("Base64 Encoded ECPrivateKey: " + base64EncodedKey);

//         return privateKey;
//     }
//     public static byte[] normalizeECPrivateKey(BigInteger s) {
//         byte[] bytes = s.toByteArray();
//         if (bytes.length == 33 && bytes[0] == 0x00) {
//             // 先頭の 0x00 を削除（符号ビット）
//             return Arrays.copyOfRange(bytes, 1, 33);
//         } else if (bytes.length < 32) {
//             // 32 バイト未満なら 0 パディング
//             byte[] padded = new byte[32];
//             System.arraycopy(bytes, 0, padded, 32 - bytes.length, bytes.length);
//             return padded;
//         }
//         return bytes;
//     }
//     // Base64URL デコード（パディングなし）
//     private static byte[] base64UrlDecode(String base64Url) {
//         String base64 = base64Url.replace("-", "+").replace("_", "/");
//         while (base64.length() % 4 != 0) {
//             base64 += "="; // パディングを追加
//         }
//         return Base64.getDecoder().decode(base64);
//     }

//     // 秘密鍵バイト配列を ECPrivateKey に変換
//     private static ECPrivateKey convertToECPrivateKey(byte[] privateKeyBytes) throws Exception {
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");

//         // P-256 (secp256r1) 曲線のパラメータを取得
//         AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
//         parameters.init(new ECGenParameterSpec("secp256r1"));
//         ECParameterSpec ecSpec = parameters.getParameterSpec(ECParameterSpec.class);

//         // ECPrivateKeySpec を作成
//         ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(new BigInteger(1, privateKeyBytes), ecSpec);

//         // ECPrivateKey に変換
//         return (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
//     }
// }
