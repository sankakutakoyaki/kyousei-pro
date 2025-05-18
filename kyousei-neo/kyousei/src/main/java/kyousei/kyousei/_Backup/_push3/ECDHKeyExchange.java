package kyousei.kyousei._Backup._push3;
// package kyousei.kyousei._push3;

// import java.security.*;
// import java.security.spec.X509EncodedKeySpec;
// import javax.crypto.KeyAgreement;

// public class ECDHKeyExchange {
//     public static byte[] generateSharedSecret(PrivateKey privateKey, byte[] clientPublicKeyBytes) throws Exception {
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         PublicKey clientPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(clientPublicKeyBytes));

//         KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
//         keyAgreement.init(privateKey);
//         keyAgreement.doPhase(clientPublicKey, true);

//         return keyAgreement.generateSecret();
//     }
// }

