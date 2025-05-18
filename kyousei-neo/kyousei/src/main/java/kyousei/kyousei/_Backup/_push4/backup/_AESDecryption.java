package kyousei.kyousei._Backup._push4.backup;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class _AESDecryption {

    // AES-GCM 復号
    public static byte[] decrypt(byte[] encryptedData, byte[] aesKey, byte[] nonce) throws Exception {
        final int GCM_TAG_LENGTH = 128; // GCM のタグ長（ビット）

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, nonce);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

        return cipher.doFinal(encryptedData);
    }

    // 使用例
    public static void main(String[] args) throws Exception {
        // Base64で受け取ったと仮定（実際はバイナリで受け取る）
        String base64EncryptedData = "暗号化されたデータをBase64で";
        String base64Key = "AES鍵（Base64）";
        String base64Nonce = "Nonce（Base64）";

        byte[] encryptedData = Base64.getDecoder().decode(base64EncryptedData);
        byte[] aesKey = Base64.getDecoder().decode(base64Key);
        byte[] nonce = Base64.getDecoder().decode(base64Nonce);

        byte[] decryptedData = decrypt(encryptedData, aesKey, nonce);

        System.out.println("復号されたメッセージ: " + new String(decryptedData));
    }
}
