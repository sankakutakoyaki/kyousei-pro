package kyousei.kyousei._Backup._push3;
// package kyousei.kyousei._push3;

// import javax.crypto.Mac;
// import javax.crypto.spec.SecretKeySpec;

// public class HKDF {
//     public static byte[] extractAndExpand(byte[] salt, byte[] ikm, byte[] info, int length) throws Exception {
//         Mac mac = Mac.getInstance("HmacSHA256");

//         if (salt == null || salt.length == 0) {
//             salt = new byte[32]; // 全てゼロのデフォルト値
//         }
//         mac.init(new SecretKeySpec(salt, "HmacSHA256"));
//         byte[] prk = mac.doFinal(ikm);

//         byte[] t = new byte[0];
//         byte[] result = new byte[length];
//         int bytesCopied = 0;

//         for (byte i = 1; bytesCopied < length; i++) {
//             mac.init(new SecretKeySpec(prk, "HmacSHA256"));
//             mac.update(t);
//             mac.update(info);
//             mac.update(i);
//             t = mac.doFinal();

//             int bytesToCopy = Math.min(t.length, length - bytesCopied);
//             System.arraycopy(t, 0, result, bytesCopied, bytesToCopy);
//             bytesCopied += bytesToCopy;
//         }

//         return result;
//     }
// }
