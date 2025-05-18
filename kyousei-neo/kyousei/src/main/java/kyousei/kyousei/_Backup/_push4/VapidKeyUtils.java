package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;
// package kyousei.kyousei.push;

// import java.security.KeyFactory;
// import java.security.NoSuchAlgorithmException;
// import java.security.PrivateKey;
// import java.security.PublicKey;
// import java.security.interfaces.ECPrivateKey;
// import java.security.spec.InvalidKeySpecException;
// import java.security.spec.PKCS8EncodedKeySpec;
// import java.util.Base64;

// public class VapidKeyUtils {

//     // Base64エンコードされた秘密鍵を読み込むメソッド
//     public static PrivateKey getPrivateKey(String base64EncodedPrivateKey) throws Exception {
//         // // Base64デコード
//         // byte[] decodedKey = Base64.getUrlDecoder().decode(base64EncodedPrivateKey);
//         // base64EncodedPrivateKey = "BDtSdJ-595-GLPukMIt7t9HGcfgWQWQsMcpKTPWUGqOdfo4vGG9pIM4jAyOulTGsrioOd2lcoHAY8cK90_ZETxA";
//         try {
//             // URLセーフBase64形式を標準Base64形式に変換
//             String base64EncodedKey = base64EncodedPrivateKey.replace('-', '+').replace('_', '/');
            
//             // Base64デコード
//             byte[] decodedKey = Base64.getDecoder().decode(base64EncodedKey);
            
//             // 秘密鍵を生成
//             PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
//             KeyFactory keyFactory = KeyFactory.getInstance("EC");  // ECアルゴリズムを使う場合
//             return keyFactory.generatePrivate(keySpec);
            
//             // System.out.println("Generated Private Key: " + privateKey);
//         } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException e) {
//             System.out.println("鍵のデコードに失敗しました: " + e.getMessage());
//         }

//         return null;
//         // // ECアルゴリズムで秘密鍵を生成
//         // KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         // return keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(decodedKey));
//     }

//     // Base64エンコードされた公開鍵を読み込むメソッド
//     public static PublicKey getPublicKey(String base64EncodedPublicKey) throws Exception {
//         // Base64デコード
//         byte[] decodedKey = Base64.getUrlDecoder().decode(base64EncodedPublicKey);
        
//         // ECアルゴリズムで公開鍵を生成
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         return keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(decodedKey));
//     }
// }
