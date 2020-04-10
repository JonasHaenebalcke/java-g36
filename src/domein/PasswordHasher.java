//package domein;
//
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.KeySpec;
//import java.util.Base64;
//
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.PBEKeySpec;
//
//public class PasswordHasher {
//
//	public static byte[] getEncryptedPassword(String password)
//            throws NoSuchAlgorithmException, InvalidKeySpecException {
//
//        int pbkdf2IterCount = 1000; // default for Rfc2898DeriveBytes
//        int pbkdf2SubkeyLength = 256 / 8; // 256 bits
//        int saltSize = 128 / 8; // 128 bits
//
//        // Produce a version 2 (see comment above) text hash.
//        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//        byte[] salt = new byte[saltSize];
//        random.nextBytes(salt);
//        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, pbkdf2IterCount, 256);
//        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//        byte[] subkey = factory.generateSecret(spec).getEncoded();
//
//        byte[] outputBytes = new byte[1 + saltSize + pbkdf2SubkeyLength];
//        System.out.println(1 +" + "+ saltSize +" + "+ pbkdf2SubkeyLength + " = " + outputBytes.length);
//        outputBytes[0] = 0x00; // format marker
//        System.out.println("ArrayCopy");
//        System.arraycopy(salt,0, outputBytes, 1, saltSize); 
//        System.out.println(outputBytes +" "+ Base64.getEncoder().encodeToString(outputBytes));
//        System.arraycopy(subkey, 0, outputBytes, 1 + saltSize, pbkdf2SubkeyLength);
//        System.out.println(outputBytes+" "+ Base64.getEncoder().encodeToString(outputBytes));
//        return outputBytes;
//    }
//}

package domein;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {
	// https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#PBKDF2WithHmacSHA1
	// https://github.com/aspnet/Identity/blob/a8ba99bc5b11c5c48fc31b9b0532c0d6791efdc8/src/Microsoft.AspNetCore.Identity/PasswordHasher.cs

	/*
	 * ======================= HASHED PASSWORD FORMATS ======================= We
	 * will use this method Version 2: PBKDF2 with HMAC-SHA1, 128-bit salt, 256-bit
	 * subkey, 1000 iterations. (See also: SDL crypto guidelines v5.1, Part III)
	 * Format: { 0x00, salt, subkey }
	 * 
	 * We could not use this method Version 3: PBKDF2 with HMAC-SHA256, 128-bit
	 * salt, 256-bit subkey, 10000 iterations. Format: { 0x01, prf (UInt32), iter
	 * count (UInt32), salt length (UInt32), salt, subkey } (All UInt32s are stored
	 * big-endian.)
	 */

	/***
	 * hashes a password
	 * 
	 * @param password is the password that needs hashing
	 * @return the encoded password in base64
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static String getPasswordHash(String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		int iterations = 1000;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 256);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] subkey = skf.generateSecret(spec).getEncoded();
		byte[] outputBytes = new byte[1 + 16 + 32];
		outputBytes[0] = 0x00; // format marker
		System.arraycopy(salt, 0, outputBytes, 1, 16);

		System.arraycopy(subkey, 0, outputBytes, 1 + 16, 32);

		return Base64.getEncoder().encodeToString(outputBytes);
	}	

	/***
	 * generates a random salt
	 * @return an array of bytes containing the salt
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}
}
