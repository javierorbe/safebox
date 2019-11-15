package deusto.safebox.client.security;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ClientSecurity {

    private static final int PBKDF2_VAULT_ITERATIONS = 100_000;
    private static final int PBKDF2_AUTH_ITERATIONS = 5_000;

    private static SecretKey vaultKey;

    /**
     * Returns the authentication hash for the given email address and password.
     *
     * <p>To calculate the authentication hash, first, the vault key is is derived from
     * the master password, using the email as the salt.
     * Finally, the authentication hash is derived from the vault key as the key and the master password as the salt.
     *
     * @param email the email address.
     * @param masterPassword the master password.
     * @return the authentication hash.
     */
    public static String getAuthHash(String email, String masterPassword) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(masterPassword);
        // Generate the vault key
        SecretKey rawVaultKey = generateKey(masterPassword, email, PBKDF2_VAULT_ITERATIONS);
        vaultKey = new SecretKeySpec(rawVaultKey.getEncoded(), "AES");
        // Generate the authentication hash
        String encodedVaultKey = Base64.getEncoder().encodeToString(rawVaultKey.getEncoded());
        SecretKey authKey = generateKey(encodedVaultKey, masterPassword, PBKDF2_AUTH_ITERATIONS);
        return Base64.getEncoder().encodeToString(authKey.getEncoded());
    }

    /**
     * Encrypt the given data with the vault key.
     *
     * @param data the data.
     * @return the encrypted data.
     */
    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(getSalt(cipher.getBlockSize()));
            cipher.init(Cipher.ENCRYPT_MODE, vaultKey, iv);

            String encodedIv = Base64.getEncoder().encodeToString(iv.getIV());
            String encodedData = Base64.getEncoder()
                    .encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));

            return encodedIv + ":" + encodedData;
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting.", e);
        }
    }

    /**
     * Decrypt the given data with the vault key.
     *
     * @param data the encrypted data.
     * @return the data, decrypted.
     */
    public static String decrypt(String data) {
        try {
            String[] split = data.split(":");
            String encodedIv = split[0];
            String encodedData = split[1];
            IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(encodedIv));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, vaultKey, iv);
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encodedData));
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting.", e);
        }
    }

    /**
     * Generates a key for the given password and salt.
     *
     * @param password the password.
     * @param stringSalt the string used as a salt.
     * @return the generated key.
     */
    private static SecretKey generateKey(String password, String stringSalt, int iterations) {
        try {
            char[] pwdChars = password.toCharArray();
            byte[] salt = stringSalt.getBytes(StandardCharsets.UTF_8);

            PBEKeySpec spec = new PBEKeySpec(pwdChars, salt, iterations, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error in hashing function.", e);
        }
    }

    /**
     * Generates a salt of the given length.
     *
     * @param length the length of the salt.
     * @return the generated salt.
     * @throws NoSuchAlgorithmException if the random byte generator is not available.
     */
    private static byte[] getSalt(int length) throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[length];
        sr.nextBytes(salt);
        return salt;
    }

    private ClientSecurity() {
        throw new AssertionError();
    }
}
