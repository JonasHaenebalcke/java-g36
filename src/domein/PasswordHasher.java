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

	
	//GENEREER WACHTWOORD
	
	
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
	
	
	
	
	
	
	//CONTROLEER WACHTWOORD
	
	
	/***
	 * checks of the password matches the 
	 * @param hashedPassword the hashed password of the user
	 * @param providedPassword the password the user has entered
	 * @return
	 */	
	public static boolean verifyPasswordHash(String hashedPassword, String providedPassword) {
		boolean confirmed = false;
		if (hashedPassword == null) {
			throw new IllegalArgumentException("Dit is een ongeldige gebruiker, gelieve deze opnieuw aan te maken!");
		}
		if (providedPassword == null) {
			throw new IllegalArgumentException("Gelieve een wachtwoord mee te geven");
		}
		byte[] decodedHashedPassword = Base64.getDecoder().decode(hashedPassword);
		if (decodedHashedPassword.length == 0) {
			return confirmed;
		}

		switch (decodedHashedPassword[0]) {
		//the only one we are able too check (v2)
		case 0x00:
			try {
				return verifyHashedPasswordV2(decodedHashedPassword, providedPassword);
			} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				throw new IllegalArgumentException("Er ging iets mis bij het controleren van het wachtwoord..");
			}
		case 0x01:
			//cannot check this type (v3)
			return false;
		default:
			return false;
		}
	}
	/***
	 * deconstructes the password and checked is they are equal
	 * @param hashedPassword the hashed password of the user
	 * @param password the entered password of the user
	 * @return controls if the are equal, if they are it returns a true else a false
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	private static boolean verifyHashedPasswordV2(byte[] hashedPassword, String password)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		char[] chars = password.toCharArray();
		int Pbkdf2IterCount = 1000; // default for Rfc2898DeriveBytes
		int Pbkdf2SubkeyLength = 256 / 8; // 256 bits
		int SaltSize = 128 / 8; // 128 bits

		// We know ahead of time the exact length of a valid hashed password payload.
		if (hashedPassword.length != 1 + SaltSize + Pbkdf2SubkeyLength) {
			return false; // bad size
		}

		byte[] salt = new byte[SaltSize];
		System.arraycopy(hashedPassword, 1, salt, 0, salt.length);

		byte[] expectedSubkey = new byte[Pbkdf2SubkeyLength];
		System.arraycopy(hashedPassword, 1 + salt.length, expectedSubkey, 0, expectedSubkey.length);

		// Hash the incoming password and verify it
		PBEKeySpec spec = new PBEKeySpec(chars, salt, Pbkdf2IterCount, 256);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] actualSubkey = skf.generateSecret(spec).getEncoded();
		return byteArraysEqual(actualSubkey, expectedSubkey);
	}
	
	/***
	 * checkes if two arrays are equal
	 * @param a byte array "a"
	 * @param b byte array "b"
	 * @return "true" is equal of false if not
	 */
	private static boolean byteArraysEqual(byte[] a, byte[] b) {
		if (a == null && b == null) {
			return true;
		}
		if (a == null || b == null || a.length != b.length) {
			return false;
		}
		boolean areSame = true;
		for (int i = 0; i < a.length; i++) {
			areSame &= (a[i] == b[i]);
		}
		return areSame;
	}
}
