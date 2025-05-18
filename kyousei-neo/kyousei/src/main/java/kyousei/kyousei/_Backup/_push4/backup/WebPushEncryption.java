package kyousei.kyousei._Backup._push4.backup;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WebPushEncryption {

    public static byte[] encryptPayload(String payload, SecretKey aesKey, byte[] nonce) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, nonce);  // ✅ 128ビット認証タグ, 12バイト nonce
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmSpec);
        byte[] aad = "Content-Encoding: aes128gcm".getBytes(StandardCharsets.UTF_8);
        cipher.updateAAD(aad);
        return cipher.doFinal(payload.getBytes(StandardCharsets.UTF_8));
    }

    public static SecretKey deriveAesKey(byte[] sharedSecret, byte[] salt) throws Exception {
        byte[] prk = hkdfExtract(salt, sharedSecret);
        byte[] aesKey = hkdfExpand(prk, "Content-Encoding: aes128gcm", 16);
        return new SecretKeySpec(aesKey, "AES");
    }

    public static byte[] deriveNonce(byte[] prk) throws Exception {
        return hkdfExpand(prk, "Content-Encoding: nonce", 12);
    }

    public static byte[] hkdfExtract(byte[] salt, byte[] ikm) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(salt, "HmacSHA256"));
        return mac.doFinal(ikm);
    }

    public static byte[] hkdfExpand(byte[] prk, String info, int length) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(prk, "HmacSHA256"));
        mac.update(info.getBytes(StandardCharsets.UTF_8));
        return Arrays.copyOf(mac.doFinal(), length);
    }
}
