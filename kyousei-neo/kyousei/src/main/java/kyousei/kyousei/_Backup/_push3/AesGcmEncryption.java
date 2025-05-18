package kyousei.kyousei._Backup._push3;
// package kyousei.kyousei._push3;

// import javax.crypto.Cipher;
// import javax.crypto.SecretKey;
// import javax.crypto.spec.GCMParameterSpec;
// import javax.crypto.spec.SecretKeySpec;

// import java.security.SecureRandom;
// import java.util.Base64;

// public class AesGcmEncryption {
//     public static String encrypt(String plainText, byte[] secretKey) throws Exception {
//         SecureRandom secureRandom = new SecureRandom();
//         byte[] iv = new byte[12]; // 12バイトのIV
//         secureRandom.nextBytes(iv);

//         SecretKey key = new SecretKeySpec(secretKey, "AES");

//         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//         cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));

//         byte[] encryptedData = cipher.doFinal(plainText.getBytes());

//         byte[] encryptedMessage = new byte[iv.length + encryptedData.length];
//         System.arraycopy(iv, 0, encryptedMessage, 0, iv.length);
//         System.arraycopy(encryptedData, 0, encryptedMessage, iv.length, encryptedData.length);

//         return Base64.getEncoder().encodeToString(encryptedMessage);
//     }
// }
