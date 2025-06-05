package dev.asper.app.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class Encryption {
    private final Cipher encrypt;
    private final Cipher decrypt;
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    public Encryption(@Value("${security.encryption-key}") String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "AES";
        SecretKeySpec secretKey = new SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(key.getBytes()), algorithm);
        encrypt = Cipher.getInstance(algorithm);
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey);
        decrypt = Cipher.getInstance(algorithm);
        decrypt.init(Cipher.DECRYPT_MODE, secretKey);
    }

    public String encrypt(String data) {
        try {
            byte[] encryptedData = encrypt.doFinal(data.getBytes());
            return encoder.encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            byte[] decodedData = decoder.decode(encryptedData);
            byte[] decryptedData = decrypt.doFinal(decodedData);
            return new String(decryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data", e);
        }
    }
}
