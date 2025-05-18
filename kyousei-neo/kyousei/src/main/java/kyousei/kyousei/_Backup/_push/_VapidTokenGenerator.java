package kyousei.kyousei._Backup._push;
// package kyousei.kyousei._push;

// import java.security.*;
// import java.security.spec.ECGenParameterSpec;
// import java.security.spec.PKCS8EncodedKeySpec;
// import java.util.Base64;
// import java.util.Date;
// import java.util.ResourceBundle;

// public class _VapidTokenGenerator {

//     private static final String PUBLIC_KEY_BASE64 = ResourceBundle.getBundle("application").getString("vapid.publicKey");
//     private static final String PRIVATE_KEY_BASE64 = ResourceBundle.getBundle("application").getString("vapid.privateKey");

//     // // 秘密鍵をロードするメソッド
//     // public PrivateKey loadPrivateKey(String privateKeyBase64) throws Exception {
//     //     byte[] decodedKey = Base64.getDecoder().decode(privateKeyBase64);
//     //     if (isBase64Encoded(privateKeyBase64)) {
//     //         System.out.println("有効なBase64エンコードされた文字列です。");
//     //     } else {
//     //         System.out.println("無効なBase64エンコードの可能性があります。");
//     //     }
//     //     PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
//     //     KeyFactory keyFactory = KeyFactory.getInstance("EC");
//     //     return keyFactory.generatePrivate(keySpec);
//     // }
//     // 秘密鍵をロードするメソッド
//     public PrivateKey loadPrivateKey(String privateKeyBase64) throws Exception {
//         // PKCS#8形式に変換
//         String pkcs8PrivateKey = convertToPKCS8(privateKeyBase64);

//         // Base64でエンコードされた秘密鍵をデコード
//         // byte[] decodedKey = Base64.getDecoder().decode(privateKeyBase64);
//         byte[] decodedKey = Base64.getDecoder().decode(pkcs8PrivateKey);

//         // PKCS#8エンコードされた秘密鍵を使って鍵を生成
//         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        
//         // "EC"アルゴリズムを指定してKeyFactoryを生成
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");

//         // "EC"アルゴリズムに基づきPrivateKeyを生成
//         return keyFactory.generatePrivate(keySpec);
//     }

//     // ECDSAで署名を生成するメソッド
//     public String signData(String data, PrivateKey privateKey) throws Exception {
//         Signature signature = Signature.getInstance("SHA256withECDSA");
//         signature.initSign(privateKey);
//         signature.update(data.getBytes());
//         byte[] signedData = signature.sign();
//         return Base64.getUrlEncoder().encodeToString(signedData); // URLセーフにエンコード
//     }

//     // JWTのヘッダーとペイロードを作成するメソッド
//     public String createJwt(String privateKeyBase64, String publicKeyBase64) throws Exception {
//         long now = System.currentTimeMillis();
//         long expiry = now + 3600000L; // 1時間後に設定

//         // ヘッダー部分
//         String header = "{\"alg\":\"ES256\",\"typ\":\"JWT\"}";

//         // ペイロード部分
//         String payload = String.format("{\"aud\":\"https://fcm.googleapis.com\",\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}",
//                 publicKeyBase64, now / 1000, expiry / 1000); // UNIX時間に変換

//         // ヘッダーとペイロードをBase64Urlエンコード
//         String encodedHeader = Base64.getUrlEncoder().encodeToString(header.getBytes());
//         String encodedPayload = Base64.getUrlEncoder().encodeToString(payload.getBytes());

//         // ヘッダーとペイロードを結合して署名部分を作成
//         String toSign = encodedHeader + "." + encodedPayload;

//         // 秘密鍵で署名
//         PrivateKey privateKey = loadPrivateKey(privateKeyBase64);
//         String signature = signData(toSign, privateKey);

//         // JWTトークンを作成
//         return toSign + "." + signature;
//     }

//     public static void main(String[] args) {
//         try {
//             // Base64エンコードされた秘密鍵と公開鍵を設定
//             String privateKeyBase64 = Base64.getEncoder().encodeToString(PRIVATE_KEY_BASE64.getBytes());
//             String publicKeyBase64 = Base64.getEncoder().encodeToString(PUBLIC_KEY_BASE64.getBytes());

//             _VapidTokenGenerator generator = new _VapidTokenGenerator();
//             String vapidToken = generator.createJwt(privateKeyBase64, publicKeyBase64);
//             System.out.println("Generated VAPID Token: " + vapidToken);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
//     public static String getJwt() throws Exception {
//         String privateKeyBase64 = Base64.getEncoder().encodeToString(PRIVATE_KEY_BASE64.getBytes());
//         String publicKeyBase64 = Base64.getEncoder().encodeToString(PUBLIC_KEY_BASE64.getBytes());

//         _VapidTokenGenerator generator = new _VapidTokenGenerator();
//         String jwt = generator.createJwt(privateKeyBase64, publicKeyBase64);
//         return jwt;
//     }

//     public static boolean isBase64Encoded(String base64) {
//         try {
//             Base64.getDecoder().decode(base64);
//             return true;
//         } catch (IllegalArgumentException e) {
//             return false;
//         }
//     }
//     // Base64エンコードされた秘密鍵をPKCS#8形式に変換するメソッド
//     public static String convertToPKCS8(String privateKeyBase64) throws Exception {
//         // Base64エンコードされた秘密鍵をデコード
//         byte[] decodedKey = Base64.getDecoder().decode(privateKeyBase64);

//         // 秘密鍵をPKCS#8形式のKeySpecに変換
//         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

//         // KeyFactoryを使って秘密鍵を生成
//         KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // または "EC"（秘密鍵のタイプに応じて変更）
//         PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

//         // PKCS#8形式の秘密鍵をBase64でエンコードして返す
//         return Base64.getEncoder().encodeToString(privateKey.getEncoded());
//     }
// }



// import java.nio.charset.StandardCharsets;
// import java.security.*;
// import java.security.interfaces.ECPrivateKey;
// import java.security.spec.*;
// import java.util.Base64;
// import java.util.ResourceBundle;

// import javax.crypto.Mac;
// import javax.crypto.spec.SecretKeySpec;

// import org.springframework.beans.factory.annotation.Value;

// import java.time.Instant;

// public class VapidTokenGenerator {

//     // private static final String PUBLIC_KEY_BASE64 = "<YOUR_PUBLIC_VAPID_KEY>"; // Base64エンコードされた公開鍵
//     // private static final String PRIVATE_KEY_BASE64 = "<YOUR_PRIVATE_VAPID_KEY>"; // Base64エンコードされた秘密鍵
//     private static final String PUBLIC_KEY_BASE64 = ResourceBundle.getBundle("application").getString("vapid.publicKey");
//     private static final String PRIVATE_KEY_BASE64 = ResourceBundle.getBundle("application").getString("vapid.privateKey");

//     // // 秘密鍵をロードするメソッド
//     // public PrivateKey loadPrivateKey(String privateKeyBase64) throws Exception {
//     //     byte[] decodedKey = Base64.getDecoder().decode(privateKeyBase64);
//     //     PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
//     //     KeyFactory keyFactory = KeyFactory.getInstance("EC");
//     //     return keyFactory.generatePrivate(keySpec);
//     // }

//     // // 公開鍵をロードするメソッド
//     // public PublicKey loadPublicKey(String publicKeyBase64) throws Exception {
//     //     byte[] decodedKey = Base64.getDecoder().decode(publicKeyBase64);
//     //     X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
//     //     KeyFactory keyFactory = KeyFactory.getInstance("EC");
//     //     return keyFactory.generatePublic(keySpec);
//     // }

//     /**
//      * VAPIDトークンを作成する
//      * @param aud "https://fcm.googleapis.com"（Pushサービスのドメイン）
//      * @param sub 通知を送る主体（"mailto:example@example.com"）
//      * @return VAPIDトークン
//      * @throws Exception
//      */
//     public static String generateVapidToken(String aud, String sub) throws Exception {
//         // 1. JWTヘッダー
//         String headerJson = "{ \"typ\": \"JWT\", \"alg\": \"ES256\" }";
//         String encodedHeader = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));

//         // 2. JWTペイロード（署名対象のデータ）
//         long exp = Instant.now().getEpochSecond() + 12 * 60 * 60; // 有効期限（12時間後）
//         String payloadJson = "{ \"aud\": \"" + aud + "\", \"exp\": " + exp + ", \"sub\": \"" + sub + "\" }";
//         String encodedPayload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

//         // 3. 署名対象のデータ
//         String toSign = encodedHeader + "." + encodedPayload;

//         // String privateKey64 = Base64.getDecoder().decode(PRIVATE_KEY_BASE64).toString();
//         // String privateKey64 = Base64.encodeBase64(PRIVATE_KEY_BASE64).toString();
//         String base64Encoded = Base64.getEncoder().encodeToString(PRIVATE_KEY_BASE64.getBytes());
//         if (isBase64Encoded(base64Encoded)) {
//             System.out.println("有効なBase64エンコードされた文字列です。");
//         } else {
//             System.out.println("無効なBase64エンコードの可能性があります。");
//         }

//         // 4. 秘密鍵をBase64デコードして、ECPrivateKeyオブジェクトを作成
//         ECPrivateKey privateKey = loadPrivateKey(base64Encoded);
//         // ECPrivateKey privateKey = loadPrivateKey(PRIVATE_KEY_BASE64);

//         // 5. 署名を作成（ES256）
//         String signature = signEs256(toSign, privateKey);

//         // 6. 最終的なJWTトークン
//         return toSign + "." + signature;
//     }
    // public static boolean isBase64Encoded(String base64) {
    //     try {
    //         Base64.getDecoder().decode(base64);
    //         return true;
    //     } catch (IllegalArgumentException e) {
    //         return false;
    //     }
    // }
//     /**
//      * Base64 URLエンコード（パディングなし）
//      */
//     private static String base64UrlEncode(byte[] input) {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
//     }

//     /**
//      * ECDSA P-256秘密鍵をBase64デコードしてECPrivateKeyオブジェクトを作成
//      */
//     private static ECPrivateKey loadPrivateKey(String base64Key) throws Exception {
//         byte[] keyBytes = Base64.getUrlDecoder().decode(base64Key);
//         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
//     }

//     /**
//      * ES256署名を作成
//      */
//     private static String signEs256(String data, PrivateKey privateKey) throws Exception {
//         Signature signature = Signature.getInstance("SHA256withECDSA");
//         signature.initSign(privateKey);
//         signature.update(data.getBytes(StandardCharsets.UTF_8));
//         byte[] signedBytes = signature.sign();
//         return base64UrlEncode(signedBytes);
//     }

//     // public static void main(String[] args) throws Exception {
//     //     String vapidToken = generateVapidToken("https://fcm.googleapis.com", "mailto:example@example.com");
//     //     System.out.println("VAPID Token: " + vapidToken);
//     // }
//     public static void main(String[] args) {
//         try {
//             // ここに実際の秘密鍵と公開鍵のBase64エンコードされた値をセット
//             String privateBase64Encoded = Base64.getEncoder().encodeToString(PRIVATE_KEY_BASE64.getBytes());
//             String privateKeyBase64 = privateBase64Encoded;
//             String publicBase64Encoded = Base64.getEncoder().encodeToString(PUBLIC_KEY_BASE64.getBytes());
//             String publicKeyBase64 = publicBase64Encoded;

//             VapidTokenGenerator generator = new VapidTokenGenerator();
//             String vapidToken = generator.generateVapidToken(privateKeyBase64, publicKeyBase64);
//             System.out.println("Generated VAPID Token: " + vapidToken);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }

