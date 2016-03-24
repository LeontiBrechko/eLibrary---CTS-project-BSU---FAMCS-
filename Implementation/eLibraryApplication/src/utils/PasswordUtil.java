package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * Created by Leonti on 2016-03-08.
 */
public class PasswordUtil {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(password.getBytes());
        byte[] messageDigestArray = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder(messageDigestArray.length * 2);
        for (byte b : messageDigestArray) {
            int v = b & 0xff;
            if  (v < 16) {
                stringBuilder.append(0);
            }
            stringBuilder.append(Integer.toHexString(v));
        }
        return stringBuilder.toString();
    }

    public static String getSalt() {
        Random random = new SecureRandom();
        byte[] saltBytes = new byte[32];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    public static String getConfirmationToken() {
        return getSalt();
    }

    public static void checkPasswordStrength(String password) throws Exception {
        // TODO: 2016-03-08 implement this method with regex
    }
}
