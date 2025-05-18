package kyousei.kyousei._Backup._push4.backup;
// package kyousei.kyousei.push.backup;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;

// import java.security.interfaces.ECPrivateKey;
// import java.time.Instant;
// import java.util.Date;

// public class _VapidJwtBuilder {

//     public static String generateVapidJwt(String audience, String subject, ECPrivateKey vapidPrivateKey) {
//         Instant now = Instant.now();
//         Instant expire = now.plusSeconds(12 * 60 * 60); // 有効期限12時間

//         return Jwts.builder()
//                 .setAudience(audience)
//                 .setExpiration(Date.from(expire))
//                 .setSubject(subject)
//                 .signWith(vapidPrivateKey, SignatureAlgorithm.ES256)
//                 .compact();
//     }
// }
