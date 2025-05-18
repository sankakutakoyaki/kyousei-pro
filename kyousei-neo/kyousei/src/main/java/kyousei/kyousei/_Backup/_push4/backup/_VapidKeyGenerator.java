package kyousei.kyousei._Backup._push4.backup;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

public class _VapidKeyGenerator {

    public static KeyPair generateVapidKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(new ECGenParameterSpec("secp256r1")); // P-256
        return keyGen.generateKeyPair();
    }

    public static void main(String[] args) throws Exception {
        KeyPair vapidKeyPair = generateVapidKeyPair();

        String publicKey = Base64.getUrlEncoder().withoutPadding().encodeToString(
                vapidKeyPair.getPublic().getEncoded());

        String privateKey = Base64.getUrlEncoder().withoutPadding().encodeToString(
                vapidKeyPair.getPrivate().getEncoded());

        System.out.println("VAPID 公開鍵 (Base64URL): " + publicKey);
        System.out.println("VAPID 秘密鍵 (Base64URL): " + privateKey);
    }
}

