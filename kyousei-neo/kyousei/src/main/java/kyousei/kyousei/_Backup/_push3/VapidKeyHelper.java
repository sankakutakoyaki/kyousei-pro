package kyousei.kyousei._Backup._push3;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class VapidKeyHelper {
    public static KeyPair createKeyPairFromBase64(String base64PrivateKey, String base64PublicKey) throws Exception {
        byte[] decodedPrivateKey = Base64.getDecoder().decode(base64PrivateKey);
        byte[] decodedPublicKey = Base64.getDecoder().decode(base64PublicKey);

        KeyFactory keyFactory = KeyFactory.getInstance("EC");

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        return new KeyPair(publicKey, privateKey);
    }
}
