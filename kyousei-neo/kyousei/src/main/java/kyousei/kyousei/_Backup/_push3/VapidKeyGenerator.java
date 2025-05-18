package kyousei.kyousei._Backup._push3;
// package kyousei.kyousei._push3;

// import java.security.*;
// import java.security.spec.ECGenParameterSpec;
// import java.util.Base64;

// public class VapidKeyGenerator {
//     public static void main(String[] args) {
//         try {
//             KeyPair keyPair = generateVapidKeyPair();

//             String publicKeyBase64 = encodeBase64(keyPair.getPublic().getEncoded());
//             String privateKeyBase64 = encodeBase64(keyPair.getPrivate().getEncoded());

//             System.out.println("Public Key (Base64): " + publicKeyBase64);
//             System.out.println("Private Key (Base64): " + privateKeyBase64);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     public static KeyPair generateVapidKeyPair() throws Exception {
//         KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
//         keyPairGenerator.initialize(new ECGenParameterSpec("secp256r1"));
//         return keyPairGenerator.generateKeyPair();
//     }

//     public static String encodeBase64(byte[] key) {
//         return Base64.getEncoder().encodeToString(key);
//     }
// }
