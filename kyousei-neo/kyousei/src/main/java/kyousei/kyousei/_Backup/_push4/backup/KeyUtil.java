package kyousei.kyousei._Backup._push4.backup;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;

public class KeyUtil {

    /**
     * 65バイトの公開鍵を ECPublicKey に変換
     * @param rawPublicKey
     * @return
     * @throws Exception
     */
    public static PublicKey convertRawToECPublicKey(byte[] rawPublicKey) throws Exception {
        if (rawPublicKey.length != 65 || rawPublicKey[0] != 0x04) {
            throw new IllegalArgumentException("Invalid uncompressed EC public key format");
        }
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
        parameters.init(new ECGenParameterSpec("secp256r1"));
        ECParameterSpec ecSpec = parameters.getParameterSpec(ECParameterSpec.class);

        byte[] x = new byte[32];
        byte[] y = new byte[32];
        System.arraycopy(rawPublicKey, 1, x, 0, 32);
        System.arraycopy(rawPublicKey, 33, y, 0, 32);

        ECPoint ecPoint = new ECPoint(new BigInteger(1, x), new BigInteger(1, y));
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePublic(new ECPublicKeySpec(ecPoint, ecSpec));
    }

    /**
     * 32バイトの秘密鍵を ECPrivateKey に変換
     * @param rawPrivateKey
     * @return
     * @throws Exception
     */
    public static PrivateKey convertRawToECPrivateKey(byte[] rawPrivateKey) throws Exception {
        if (rawPrivateKey.length != 32) {
            throw new IllegalArgumentException("Invalid EC private key format");
        }
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");
        parameters.init(new ECGenParameterSpec("secp256r1"));
        ECParameterSpec ecSpec = parameters.getParameterSpec(ECParameterSpec.class);

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(new ECPrivateKeySpec(new BigInteger(1, rawPrivateKey), ecSpec));
    }

    /**
     * 公開鍵かどうかをチェック
     * @param base64UrlPublicKey
     * @return
     */
    public static boolean isValidVapidPublicKey(String base64UrlPublicKey) {
        byte[] decodedKey = Base64.getUrlDecoder().decode(base64UrlPublicKey);
        return decodedKey.length == 65 && decodedKey[0] == 0x04;
    }

    /**
     * 公開鍵のバイト数と先頭バイトをチェック
     * @param base64UrlPublicKey
     */
    public static void CheckPublicKeyLength(String base64UrlPublicKey) {
        byte[] decodedKey = Base64.getUrlDecoder().decode(base64UrlPublicKey);

        System.out.println("公開鍵のバイト数: " + decodedKey.length);
        System.out.printf("先頭バイト: 0x%02X\n", decodedKey[0]); // 04 (非圧縮) or 02/03 (圧縮)
    }

    /**
     * 非圧縮形式の公開鍵（65バイト）を圧縮形式（33バイト）に変換し、Base64URL エンコード
     * @param base64UrlPublicKey
     * @return
     */
    public static String ConvertVapidKey(String base64UrlPublicKey) {
        // Base64URL デコード
        byte[] uncompressed = Base64.getUrlDecoder().decode(base64UrlPublicKey);
        if (uncompressed.length != 65 || uncompressed[0] != 0x04) {
            throw new IllegalArgumentException("公開鍵のフォーマットが正しくありません");
        }

        // X, Y 座標を取得（32バイトずつ）
        byte[] x = new byte[32];
        byte[] y = new byte[32];
        System.arraycopy(uncompressed, 1, x, 0, 32);
        System.arraycopy(uncompressed, 33, y, 0, 32);

        // 圧縮形式へ変換（0x02 または 0x03 + X座標）
        byte[] compressed = new byte[33];
        compressed[0] = (byte) ((y[y.length - 1] & 1) == 0 ? 0x02 : 0x03); // Yの偶奇で決定
        System.arraycopy(x, 0, compressed, 1, 32);

        // Base64URL エンコード
        String compressedBase64Url = Base64.getUrlEncoder().withoutPadding().encodeToString(compressed);
        System.out.println("圧縮形式の公開鍵 (Base64URL): " + compressedBase64Url);

        return compressedBase64Url;
    }

    public static void checkPrivateKeyCurve(PrivateKey privateKey) throws Exception {
        if (privateKey instanceof ECPrivateKey) {
            ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
            ECParameterSpec params = ecPrivateKey.getParams();
            ECFieldFp field = (ECFieldFp) params.getCurve().getField();
            
            // 🔹 P-256 (secp256r1) の素数 p = 2^256 - 2^224 + 2^192 + 2^96 - 1
            String expectedP256Prime = "ffffffff00000001000000000000000000000000ffffffffffffffffffffffff";
            String actualPrime = field.getP().toString(16);
    
            if (expectedP256Prime.equalsIgnoreCase(actualPrime)) {
                System.out.println("✅ PrivateKey is using P-256 (secp256r1)");
            } else {
                System.out.println("❌ PrivateKey is NOT using P-256 (secp256r1)");
            }
        } else {
            System.out.println("❌ PrivateKey is NOT an EC key");
        }
    }

    /**
     * 
     * @param publicKey
     * @return
     */
    public static String getUncompressedPublicKey(ECPublicKey publicKey) {
        // 公開鍵を非圧縮形式でエンコード
        byte[] encoded = publicKey.getEncoded();
        
        // 非圧縮形式は0x04で始まる
        if (encoded[0] != 0x04) {
            throw new IllegalArgumentException("非圧縮公開鍵形式ではありません");
        }

        // Base64URLエンコード
        return Base64.getUrlEncoder().withoutPadding().encodeToString(encoded);
    }

    public static byte[] encodeUncompressedPoint(ECPublicKey publicKey) {
        ECPoint ecPoint = publicKey.getW();
        byte[] x = toUnsignedBytes(ecPoint.getAffineX(), 32);
        byte[] y = toUnsignedBytes(ecPoint.getAffineY(), 32);
    
        ByteBuffer buffer = ByteBuffer.allocate(1 + x.length + y.length);
        buffer.put((byte) 0x04); // 非圧縮形式のマーカー
        buffer.put(x);
        buffer.put(y);
        return buffer.array();
    }

    // 補助関数: BigInteger → 32バイト配列にする
    private static byte[] toUnsignedBytes(BigInteger value, int length) {
        byte[] byteArray = value.toByteArray();
        if (byteArray.length == length) {
            return byteArray;
        }
        // BigInteger.toByteArray() は符号付きなので、先頭に0が付くことがある
        if (byteArray.length == length + 1 && byteArray[0] == 0) {
            return Arrays.copyOfRange(byteArray, 1, byteArray.length);
        }
        // 足りない分は左側に0パディング
        byte[] padded = new byte[length];
        System.arraycopy(byteArray, 0, padded, length - byteArray.length, byteArray.length);
        return padded;
    }

    public static byte[] getUncompressedPublicKeyBytes(PublicKey publicKey) throws Exception {
        ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
        ECPoint w = ecPublicKey.getW();
        byte[] x = ensureLength(w.getAffineX().toByteArray(), 32);
        byte[] y = ensureLength(w.getAffineY().toByteArray(), 32);
    
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(0x04);  // Uncompressed point indicator
        stream.write(x);
        stream.write(y);
        return stream.toByteArray();
    }
    
    private static byte[] ensureLength(byte[] src, int length) {
        if (src.length == length) return src;
        byte[] result = new byte[length];
        System.arraycopy(src, Math.max(0, src.length - length), result, length - Math.min(length, src.length), Math.min(length, src.length));
        return result;
    }
}

