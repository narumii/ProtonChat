package pw.narumi.proton.shared.cryptography;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class CryptographyHelper {

    public static String serializePublicKey(final PublicKey publicKey) {
        try {
            return Base64.getEncoder().encodeToString(publicKey.getEncoded());
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static PublicKey deserializePublicKey(final String string) {
        try {
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(string.getBytes()));
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String serializeSecretKey(final SecretKey secretKey, final PublicKey publicKey) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.PUBLIC_KEY, publicKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(secretKey.getEncoded()));
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static SecretKeySpec deserializeSecretKey(final String string, final PrivateKey privateKey) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.PRIVATE_KEY, privateKey);
            return new SecretKeySpec(cipher.doFinal(Base64.getDecoder().decode(string)), "AES");
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] encodeData(final byte[] data, final SecretKey secretKey) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(secretKey.getEncoded()));
            return cipher.doFinal(data);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] decodeData(final byte[] data, final SecretKey secretKey) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(secretKey.getEncoded()));
            return cipher.doFinal(data);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return new byte[0];
    }
}
