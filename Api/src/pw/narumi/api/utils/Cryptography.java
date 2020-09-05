package pw.narumi.api.utils;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Cryptography {

    public static String encode(final String string, final PublicKey publicKey) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes()));
        }catch (final Exception e) {
            System.err.println("Error while encoding message: " + e.getMessage());
        }

        return null;
    }

    public static String decode(final String string, final PrivateKey privateKey) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            return new String(cipher.doFinal(Base64.getDecoder().decode(string))).intern();
        }catch (final Exception e) {
            System.err.println("Error while decoding message: " + e.getMessage());
        }

        return null;
    }
}
