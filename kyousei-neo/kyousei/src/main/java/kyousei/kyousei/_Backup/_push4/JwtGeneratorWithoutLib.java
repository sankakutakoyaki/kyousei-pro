package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;

// import java.math.BigInteger;
// import java.nio.charset.StandardCharsets;
// import java.security.*;
// import java.security.interfaces.ECPrivateKey;
// import java.security.spec.*;
// import java.util.Base64;
// import java.util.Date;
// import java.util.ResourceBundle;

// public class JwtGeneratorWithoutLib {
//     public static void print() throws Exception {

//         // 3️⃣ ECPrivateKey を作成
//         ECPrivateKey EC_PRIVATE_KEY = VapidJwtGenerator.createEcPrivateKey(ResourceBundle.getBundle("application").getString("vapid.privateKey"));
//         byte[] privateKeyBytes = VapidJwtGenerator.normalizeECPrivateKey(EC_PRIVATE_KEY.getS());
//         ECPrivateKey EC_PRIVATE_KEY2 = VapidJwtGenerator.createEcPrivateKey(Base64.getUrlEncoder().withoutPadding().encodeToString(privateKeyBytes));
//         // BigInteger sValue = new BigInteger(1, privateKeyBytes);
//         // System.out.println("元の S 値: " + sValue.toString(16));
//         // System.out.println("ECPrivateKey S 値: " + EC_PRIVATE_KEY2.getS().toString(16));

        
        
//         // // ✅ Base64URL エンコードされた S 値（43 文字）
//         // String base64urlS = "HLWQwpHfE5dHi1Ytv-hYLmdZfrmP1DUr71vTXKuYeuI"; // ここに秘密鍵を入力

//         // // 🔹 1. S 値をデコードして ECPrivateKey を作成
//         // byte[] sBytes = Base64.getUrlDecoder().decode(base64urlS);
//         // BigInteger sValue = new BigInteger(1, sBytes);
//         // ECPrivateKey privateKey = generateECPrivateKey(sValue);

//         // 🔹 2. JWT ヘッダーとペイロードの Base64URL エンコード
//         String header = base64UrlEncode("{\"alg\":\"ES256\",\"typ\":\"JWT\"}");
//         String payload = base64UrlEncode("{\"sub\":\"1234567890\",\"name\":\"John Doe\",\"iat\":" + (new Date().getTime() / 1000) + "}");

//         // 🔹 3. 署名データの作成
//         String message = header + "." + payload;

//         // 🔹 4. ECDSA で署名
//         byte[] derSignature = signECDSA(EC_PRIVATE_KEY2, message);

//         // 🔹 5. DER 署名を IEEE P-1363 形式に変換
//         byte[] ieeeSignature = convertDERtoIEEE(derSignature);

//         // 🔹 6. JWT を組み立て
//         String jwt = message + "." + base64UrlEncode(ieeeSignature);

//         // 🎯 JWT 出力
//         System.out.println("JWT: " + jwt);
//     }

//     // 🔹 Base64URL エンコード
//     private static String base64UrlEncode(String data) {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
//     }

//     private static String base64UrlEncode(byte[] data) {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
//     }

//     // 🔹 ECPrivateKey を作成
//     private static ECPrivateKey generateECPrivateKey(BigInteger sValue) throws Exception {
//         AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
//         parameters.init(new ECGenParameterSpec("secp256r1"));
//         ECParameterSpec ecSpec = parameters.getParameterSpec(ECParameterSpec.class);

//         ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(sValue, ecSpec);
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         return (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
//     }

//     // 🔹 SHA256withECDSA で署名
//     private static byte[] signECDSA(ECPrivateKey privateKey, String data) throws Exception {
//         Signature signature = Signature.getInstance("SHA256withECDSA");
//         signature.initSign(privateKey);
//         signature.update(data.getBytes(StandardCharsets.UTF_8));
//         return signature.sign();
//     }

//     // 🔹 DER 形式の署名を IEEE P-1363 形式に変換
//     private static byte[] convertDERtoIEEE(byte[] derSignature) throws Exception {
//         if (derSignature[0] != 0x30) {
//             throw new IllegalArgumentException("Invalid DER signature format");
//         }

//         int rLen = derSignature[3];
//         int sLen = derSignature[5 + rLen];
//         int totalLen = Math.max(rLen, sLen) * 2;

//         byte[] ieeeSignature = new byte[totalLen * 2];

//         System.arraycopy(derSignature, 4, ieeeSignature, totalLen - rLen, rLen);
//         System.arraycopy(derSignature, 6 + rLen, ieeeSignature, totalLen * 2 - sLen, sLen);

//         return ieeeSignature;
//     }
// }
