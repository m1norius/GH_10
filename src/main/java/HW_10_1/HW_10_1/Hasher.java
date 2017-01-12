package HW_10_1.HW_10_1;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
	
	private static MessageDigest md;
	private static byte[] bytesOfMessage;
	private static BigInteger bigInt;
	private static String hashtext;
	private static byte[] thedigest;
	
	public static synchronized String getHashMD5(String text){
		
		bytesOfMessage = text.getBytes();
 
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		thedigest = md.digest(bytesOfMessage);
    	
    	bigInt = new BigInteger(1, thedigest);
    	hashtext = bigInt.toString(16);
    	
		return hashtext;
	}
}
