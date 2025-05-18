package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;
// package kyousei.kyousei.push;

// import org.springframework.stereotype.Service;

// import java.security.*;
// import java.security.interfaces.ECPublicKey;
// import java.security.spec.ECGenParameterSpec;
// import java.security.spec.InvalidKeySpecException;
// import java.util.Base64;

// @Service
// public class VapidKeyService {
//     public static void main(String[] args) throws Exception {
//         KeyPair keyPair = generateVapidKeyPair();
//         String publicKey = encodePublicKeyToUrlSafeBase64(keyPair.getPublic());
//         String privateKey = encodePrivateKeyToUrlSafeBase64(keyPair.getPrivate());

//         System.out.println("Public Key (87 characters): " + publicKey);
//         System.out.println("Private Key: " + privateKey);
//     }

//     // VAPID用の公開鍵と秘密鍵を生成する
//     private static KeyPair generateVapidKeyPair() throws Exception {
//         KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
//         keyPairGenerator.initialize(256); // secp256r1の鍵長を指定
//         return keyPairGenerator.generateKeyPair();
//     }

//     // 公開鍵をURLセーフなBase64形式でエンコードする
//     private static String encodePublicKeyToUrlSafeBase64(PublicKey publicKey) {
//         byte[] encodedPublicKey = publicKey.getEncoded();
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(encodedPublicKey);
//     }

//     // 秘密鍵をURLセーフなBase64形式でエンコードする
//     private static String encodePrivateKeyToUrlSafeBase64(PrivateKey privateKey) {
//         byte[] encodedPrivateKey = privateKey.getEncoded();
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(encodedPrivateKey);
//     }
// }
