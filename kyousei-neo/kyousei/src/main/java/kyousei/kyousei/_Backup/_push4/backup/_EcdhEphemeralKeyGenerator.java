package kyousei.kyousei._Backup._push4.backup;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

public class _EcdhEphemeralKeyGenerator {

    public static KeyPair generateEphemeralKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(new ECGenParameterSpec("secp256r1")); // P-256
        return keyGen.generateKeyPair();
    }

    public static void main(String[] args) throws Exception {
        KeyPair ephKeyPair = generateEphemeralKeyPair();

        String publicKey = Base64.getUrlEncoder().withoutPadding().encodeToString(
                ephKeyPair.getPublic().getEncoded());

        System.out.println("ECDH 公開鍵 (Base64URL): " + publicKey);
    }
}

// // 公開鍵の復元
// KeyFactory kf = KeyFactory.getInstance("EC");
// byte[] decodedPublicKey = Base64.getUrlDecoder().decode(base64PublicKey);
// PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(decodedPublicKey));

// // 秘密鍵の復元
// byte[] decodedPrivateKey = Base64.getUrlDecoder().decode(base64PrivateKey);
// PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(decodedPrivateKey));
