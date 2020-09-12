package pw.narumi.proton.shared.cryptography;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class CryptographyHelper {

    public static PublicKey generatePUblicKeyFromString(final String string) {
        try {
            final byte[] key = Base64.getDecoder().decode(string.getBytes());
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        }catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateStringFromPublicKey(final PublicKey publicKey) {
        try {
            Base64.getEncoder().encodeToString(publicKey.getEncoded());
        }catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeMessage(final String string, final PublicKey publicKey) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes()));
        }catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decodeMessage(final String string, final PrivateKey privateKey) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(string))).intern();
        }catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
