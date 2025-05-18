package kyousei.kyousei._Backup._push4;
// package kyousei.kyousei._push4;

// import java.io.FileInputStream;
// import java.io.ObjectInputStream;
// import java.security.*;
// import java.security.interfaces.ECPrivateKey;
// import java.security.interfaces.ECPublicKey;
// import java.security.spec.*;

// import javax.crypto.KeyAgreement;
// import javax.crypto.spec.SecretKeySpec;
// import java.util.Base64;
// import java.util.ResourceBundle;

// public class ECDHKeyExchange {
//     // private static final String PRIVATE_KEY_FILE = "src/main/resources/static/ecdh_private.key";
//     // private static final String PUBLIC_KEY_FILE = "src/main/resources/static/ecdh_public.key";
//     // private static final String PRIVATE_KEY_FILE = ResourceBundle.getBundle("application").getString("vapid.privateKey");
//     // private static final String PUBLIC_KEY_FILE = ResourceBundle.getBundle("application").getString("vapid.publicKey");
//     private KeyPair keyPair;

//     public ECDHKeyExchange() throws Exception {
//         // File privateKeyFile = new File(PRIVATE_KEY_FILE);
//         // File publicKeyFile = new File(PUBLIC_KEY_FILE);


//         // if (privateKeyFile.exists() && publicKeyFile.exists()) {
//         //     // 既存の鍵を読み込む
//            this.keyPair = loadKeyPair();
//         // } else {
//         //     // 新しい鍵を生成して保存
//         //     this.keyPair = generateAndSaveKeyPair();
//         // }
//     }

//     // // ECDH 鍵ペアを生成し、ファイルに保存
//     // private KeyPair generateAndSaveKeyPair() throws Exception {
//     //     KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
//     //     keyGen.initialize(256);
//     //     KeyPair keyPair = keyGen.generateKeyPair();

//     //     // 秘密鍵を保存
//     //     try (FileOutputStream fos = new FileOutputStream(PRIVATE_KEY_FILE);
//     //          ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//     //         oos.writeObject(keyPair.getPrivate().getEncoded());
//     //     }

//     //     // 公開鍵を保存
//     //     try (FileOutputStream fos = new FileOutputStream(PUBLIC_KEY_FILE);
//     //          ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//     //         oos.writeObject(keyPair.getPublic().getEncoded());
//     //     }

//     //     return keyPair;
//     // }

//     // 保存された鍵ペアを読み込む
//     private KeyPair loadKeyPair() throws Exception {
//         // byte[] privateKeyBytes;
//         // byte[] publicKeyBytes;

//         // // try (FileInputStream fis = new FileInputStream(PRIVATE_KEY_FILE);
//         // try (FileInputStream fis = new FileInputStream(PRIVATE_KEY_FILE);
//         //      ObjectInputStream ois = new ObjectInputStream(fis)) {
//         //     privateKeyBytes = (byte[]) ois.readObject();
//         // }

//         // // try (FileInputStream fis = new FileInputStream(PUBLIC_KEY_FILE);
//         // try (FileInputStream fis = new FileInputStream(PUBLIC_KEY_FILE);
//         //      ObjectInputStream ois = new ObjectInputStream(fis)) {
//         //     publicKeyBytes = (byte[]) ois.readObject();
//         // }

//         // byte[] privateKeyBytes = Base64.getUrlDecoder().decode(ResourceBundle.getBundle("application").getString("vapid.privateKey"));
//         // byte[] publicKeyBytes = Base64.getUrlDecoder().decode(ResourceBundle.getBundle("application").getString("vapid.publicKey"));
//         ECPrivateKey ecPrivateKey = VapidJwtGenerator.createEcPrivateKey(ResourceBundle.getBundle("application").getString("vapid.publicKey"));
//         byte[] privateKeyBytes = ecPrivateKey.getEncoded();
//         byte[] publicKey64Bytes = Base64.getUrlDecoder().decode(ResourceBundle.getBundle("application").getString("vapid.publicKey"));
//         ECPublicKey ecPublicKey = (ECPublicKey)ECDHExample.convertCompressedKey(publicKey64Bytes);
//         byte[] publicKeyBytes = ecPublicKey.getEncoded();
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
//         PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

//         return new KeyPair(publicKey, privateKey);
//     }

//     // 公開鍵を取得
//     public String getPublicKey() {
//         return Base64.getUrlEncoder().withoutPadding().encodeToString(keyPair.getPublic().getEncoded());
//     }

//     // クライアントの公開鍵を受け取り、共通鍵を計算
//     public SecretKeySpec deriveSharedSecret(byte[] clientPublicKey) throws Exception {
//         KeyFactory keyFactory = KeyFactory.getInstance("EC");
//         PublicKey clientPubKey = keyFactory.generatePublic(new X509EncodedKeySpec(clientPublicKey));

//         KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
//         keyAgreement.init(keyPair.getPrivate());
//         keyAgreement.doPhase(clientPubKey, true);

//         byte[] sharedSecret = keyAgreement.generateSecret();
//         return new SecretKeySpec(sharedSecret, 0, 16, "AES");
//     }
// }
