package kyousei.kyousei._Backup._push4.backup;

import java.util.Base64;

public class Base64Url {
    public static String encode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    public static byte[] decode(String input) {
        return Base64.getUrlDecoder().decode(input);
    }
}

