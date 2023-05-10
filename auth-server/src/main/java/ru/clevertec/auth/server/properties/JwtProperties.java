package ru.clevertec.auth.server.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Properties for configuring JWT authentication.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * The public key to use for verifying JWT signatures.
     */
    private RSAPublicKey publicKey;

    /**
     * The private key to use for signing JWTs.
     */
    private RSAPrivateKey privateKey;

    /**
     * The expiration time for JWTs, in seconds.
     */
    private Integer expiration;
}

