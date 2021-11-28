package info.hyungjun.blogbackend.modules

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class JwtModule {
  private lateinit var privateKey: PrivateKey
  private lateinit var publicKey: PublicKey
  init {
    var privatePem = String(Files.readAllBytes(Paths.get(ClassPathResource("private_key_pkcs8.pem").uri)))
    var publicPem = String(Files.readAllBytes(Paths.get(ClassPathResource("public_key.pem").uri)))
    privatePem = privatePem
      .replace("\n", "")
      .replace("-----BEGIN PRIVATE KEY-----", "")
      .replace("-----END PRIVATE KEY-----", "")
    publicPem = publicPem
      .replace("\n", "")
      .replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "")
    
    val keyFactory = KeyFactory.getInstance("RSA")
    
    privateKey = keyFactory.generatePrivate(
      PKCS8EncodedKeySpec(Base64.getDecoder().decode(privatePem))
    )
    publicKey = keyFactory.generatePublic(
      X509EncodedKeySpec(Base64.getDecoder().decode(publicPem))
    )
  }
  
  fun createAccessToken(userId: Long): String {
    return createToken(userId.toString(), expiration = Date.from(Instant.now().plus(Duration.ofHours(8))))
  }
  
  private fun createToken(userId: String, expiration: Date): String {
    return Jwts.builder()
      .signWith(privateKey, SignatureAlgorithm.RS256)
      .setSubject(userId)
      .setExpiration(expiration)
      .setIssuedAt(Date.from(Instant.now()))
      .compact()
  }
  
  fun validateJwt(jwt: String): Jws<Claims> {
    return Jwts.parserBuilder()
      .setSigningKey(publicKey)
      .build()
      .parseClaimsJws(jwt)
  }
}