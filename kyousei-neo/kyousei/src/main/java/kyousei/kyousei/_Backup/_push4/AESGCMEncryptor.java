package kyousei.kyousei._Backup._push4;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESGCMEncryptor {
    private static final int GCM_TAG_LENGTH = 16; // 128-bit tag
    private static final int GCM_IV_LENGTH = 12; // 96-bit IV

    public static byte[] encrypt(String message, SecretKey sharedSecret) throws Exception {
        // ランダムに初期化ベクトル（IV）を生成
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        // Cipherのインスタンスを作成
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv); // タグ長はビット単位で指定

        // 鍵とIVを使って初期化
        cipher.init(Cipher.ENCRYPT_MODE, sharedSecret, spec);

        // メッセージを暗号化
        byte[] messageBytes = message.getBytes("UTF-8"); // Stringをbyte[]に変換
        byte[] encryptedMessage = cipher.doFinal(messageBytes);

        // IVと暗号化されたメッセージを結合
        byte[] encryptedMessageWithIV = new byte[GCM_IV_LENGTH + encryptedMessage.length];
        System.arraycopy(iv, 0, encryptedMessageWithIV, 0, GCM_IV_LENGTH);
        System.arraycopy(encryptedMessage, 0, encryptedMessageWithIV, GCM_IV_LENGTH, encryptedMessage.length);

        return encryptedMessageWithIV; // IVと暗号化されたメッセージを返す
    }

    public static void main(String[] args) throws Exception {
        // ダミーのメッセージと秘密鍵
        String message = "This is a secret message.";
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // 256ビットのAESキー
        SecretKey sharedSecret = keyGen.generateKey();

        // メッセージを暗号化
        byte[] encryptedMessage = encrypt(message, sharedSecret);

        // 結果をBase64で表示
        System.out.println("Encrypted Message: " + Base64.getEncoder().encodeToString(encryptedMessage));
    }
}


// import javax.crypto.Cipher;
// import javax.crypto.spec.GCMParameterSpec;
// import javax.crypto.spec.SecretKeySpec;
// import java.nio.charset.StandardCharsets;
// import java.security.SecureRandom;
// import java.util.Base64;

// public class AESGCMEncryptor {
//     private static final int IV_LENGTH = 12; // 12バイトのIV
//     private static final int GCM_TAG_LENGTH = 128;

//     public static String encrypt(String message, SecretKeySpec secretKey) throws Exception {
//         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//         byte[] iv = new byte[IV_LENGTH];
//         new SecureRandom().nextBytes(iv);

//         GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
//         cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);

//         byte[] encryptedData = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

//         byte[] combined = new byte[IV_LENGTH + encryptedData.length];
//         System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
//         System.arraycopy(encryptedData, 0, combined, IV_LENGTH, encryptedData.length);

//         return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
//     }
// }
