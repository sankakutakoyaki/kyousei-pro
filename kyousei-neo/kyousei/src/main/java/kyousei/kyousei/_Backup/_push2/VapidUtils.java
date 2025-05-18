package kyousei.kyousei._Backup._push2;
// package kyousei.kyousei.push;

// import java.nio.charset.StandardCharsets;
// import java.security.KeyFactory;
// import java.security.KeyPair;
// import java.security.KeyPairGenerator;
// import java.security.PrivateKey;
// import java.security.PublicKey;
// import java.security.interfaces.ECPrivateKey;
// import java.security.interfaces.ECPublicKey;
// import java.security.spec.ECGenParameterSpec;
// import java.security.spec.PKCS8EncodedKeySpec;
// import java.security.spec.X509EncodedKeySpec;
// import java.time.Instant;
// import java.util.Base64;
// import javax.crypto.Mac;
// import javax.crypto.spec.SecretKeySpec;

// import kyousei.kyousei.common.Enums.state;

// public class VapidUtils {
//     private static final String VAPID_SUBJECT = "mailto:your-email@example.com"; // 変更

//     public static KeyPair generateVapidKeyPair() throws Exception {
//         KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
//         keyGen.initialize(new ECGenParameterSpec("secp256r1"));
//         return keyGen.generateKeyPair();
//     }

//     public static String generateVapidToken(String endpoint, PrivateKey privateKey, PublicKey publicKey) throws Exception {
//         long expireTime = Instant.now().getEpochSecond() + 12 * 60 * 60; // 12時間有効
//         String header = "{\"alg\":\"ES256\",\"typ\":\"JWT\"}";
//         String payload = "{\"aud\":\"" + getOrigin(endpoint) + "\",\"exp\":" + expireTime + ",\"sub\":\"" + VAPID_SUBJECT + "\"}";

//         String base64Header = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));
//         String base64Payload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
//         String signingInput = base64Header + "." + base64Payload;

//         byte[] signature = sign(privateKey, signingInput.getBytes(StandardCharsets.UTF_8));
//         String base64Signature = Base64.getUrlEncoder().withoutPadding().encodeToString(signature);

//         return signingInput + "." + base64Signature;
//     }

//     private static byte[] sign(PrivateKey privateKey, byte[] data) throws Exception {
//         Mac mac = Mac.getInstance("HmacSHA256");
//         mac.init(new SecretKeySpec(privateKey.getEncoded(), "HmacSHA256"));
//         return mac.doFinal(data);
//     }

//     private static String getOrigin(String url) {
//         return url.split("/")[0] + "//" + url.split("/")[2];
//     }
// }
