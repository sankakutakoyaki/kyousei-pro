package kyousei.kyousei._Backup._push4.backup;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WebPushHKDFUtil {

    /**
     * Step 1: PRK = HKDF-Extract(auth, sharedSecret)
     */
    public static byte[] hkdfExtract(byte[] authSecret, byte[] sharedSecret) {
        HMac hmac = new HMac(new SHA256Digest());
        hmac.init(new KeyParameter(authSecret));
        hmac.update(sharedSecret, 0, sharedSecret.length);
        byte[] prk = new byte[hmac.getMacSize()];
        hmac.doFinal(prk, 0);
        return prk;
    }

    /**
     * Step 2: HKDF-Expand(PRK, info, context, length)
     * Used to derive CEK (16 bytes) and Nonce (12 bytes)
     */
    public static byte[] hkdfExpand(byte[] prk, String info, byte[] context, int length) {
        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        byte[] infoBytes = concat(info.getBytes(StandardCharsets.UTF_8), context, new byte[]{0x01});
        hkdf.init(new HKDFParameters(prk, null, infoBytes));
        byte[] output = new byte[length];
        hkdf.generateBytes(output, 0, length);
        return output;
    }

    /**
     * Step 3: Construct context = label || len(clientPub) || clientPub || len(serverPub) || serverPub
     * - Both public keys must be 64 bytes (raw, uncompressed point without 0x04)
     */
    public static byte[] createContext(byte[] clientPubKey, byte[] serverPubKey) {
        byte[] label = "P-256".getBytes(StandardCharsets.US_ASCII);
        return concat(
            label,
            new byte[]{0x00, 0x41},  // len = 65 bytes
            addUncompressedPrefix(clientPubKey),
            new byte[]{0x00, 0x41},  // len = 65 bytes
            addUncompressedPrefix(serverPubKey)
        );
    }

    /**
     * Web Push uses uncompressed EC keys. If your key is 64 bytes, prepend 0x04.
     */
    public static byte[] addUncompressedPrefix(byte[] key) {
        byte[] full = new byte[65];
        full[0] = 0x04;
        System.arraycopy(key, 0, full, 1, 64);
        return full;
    }

    private static byte[] concat(byte[]... arrays) {
        int length = Arrays.stream(arrays).mapToInt(a -> a.length).sum();
        byte[] result = new byte[length];
        int pos = 0;
        for (byte[] a : arrays) {
            System.arraycopy(a, 0, result, pos, a.length);
            pos += a.length;
        }
        return result;
    }
}


