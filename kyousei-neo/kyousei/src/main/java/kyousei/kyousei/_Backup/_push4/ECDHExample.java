package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;

// import java.math.BigInteger;
// import java.security.*;
// import java.security.interfaces.ECPublicKey;
// import java.security.spec.*;
// import java.util.Base64;

// public class ECDHExample {
//     public static PublicKey convertCompressedKey(byte[] compressedKey) throws Exception {
//         // if (compressedKey.length != 33 || compressedKey[0] != 0x04) {
//         //     throw new IllegalArgumentException("Invalid compressed key format");
//         // }

//         // 3. X座標とY座標を抽出
//         byte[] xBytes = new byte[32];
//         byte[] yBytes = new byte[32];
//         System.arraycopy(compressedKey, 1, xBytes, 0, 32);
//         System.arraycopy(compressedKey, 33, yBytes, 0, 32);

//         BigInteger x = new BigInteger(1, xBytes);
//         BigInteger y = new BigInteger(1, yBytes);

//         // 4. ECパラメータを取得（secp256r1 曲線）
//         AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
//         parameters.init(new ECGenParameterSpec("secp256r1"));
//         ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);

//         // 5. ECPoint を作成
//         ECPoint ecPoint = new ECPoint(x, y);

//         // 6. 公開鍵仕様を作成
//         ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(ecPoint, ecParameters);

//         // 7. 鍵ファクトリを使用して公開鍵を生成
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         ECPublicKey EC_PUBLIC_KEY = (ECPublicKey) keyFactory.generatePublic(publicKeySpec);

//         System.out.println("復元された公開鍵: " + EC_PUBLIC_KEY);

//         return EC_PUBLIC_KEY;
//     }

//     public static void main(String[] args) throws Exception {
//         // Base64URLデコード
//         byte[] compressedKey = Base64.getUrlDecoder().decode("BDlM2...jhlA");

//         // 非圧縮キーに変換
//         PublicKey publicKey = convertCompressedKey(compressedKey);

//         System.out.println("PublicKey: " + publicKey);
//     }
// }
