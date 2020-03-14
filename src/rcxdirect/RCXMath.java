package rcxdirect;

/** 
 * This class provides some basic RCXMath operations.
 *
 * @author Dr. Donald Doherty, Madhavi Gunda, Peter Spillman
 * @version 0.1.3, 05/01/00
 */

public class RCXMath {

	/**
	 * converts a string to a byte. 
	 * @param string the String to be converted to byte.
	 * @return the byte representing the string.
	 */

	public static byte toByte(String string) {
		return Integer.valueOf(string, 16).byteValue();
	}

	/**
	 * given a String like: "09" or "AA" etc, return the relevant byte.
	 * @param s String of byte in hexadecimal format
	 * @return byte value of hexadecimal-string
	 */
	static public byte stringToByte(String s) {
		try {
			return (byte)Integer.parseInt(s,16);
		} catch(NumberFormatException nfe) {
				return 0;
		}
	}

	/**
	 * given a byte, return a String like:  "09" or "AA" etc..
	 * @param b byte to turn to a String
	 * @return String of byte in hexadecimal format
	 */
	static public String byteToString(byte b) {
		int i = (int)b;
		if(i < 0) {
			i += 256;
		}
		String tmp = Integer.toHexString( i );
		if(tmp.length() == 1) {
			tmp = "0"+tmp;
		}
		return tmp;
	}

	/**
	 * converts a string to a int. 
	 * @param string the String to be converted to an int.
	 * @return the int representing the string.
	 */

	public static int toInt(String string) {
		return Integer.valueOf(string, 16).intValue();
	}

	/**
	 * gets the low byte of an int 
	 * @param integer the int whose low byte is required.
	 * @return b the low byte of the int.
	 */

	public static byte getLowByte(int integer) {
		byte b = (byte) integer;
		return b;
	}

	/**
	 * gets the second low byte of an int 
	 * @param integer the int whose second low byte is required.
	 * @return b the second low byte of the int.
	 */

	public static byte getSecondLowByte(int integer) {
		integer = integer >>> 8;
		byte b = (byte) integer;
		return b;
	}
	/*
	public static String byteArrayToString(byte[] b) {
		if (b == null)
			return "null";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			sb.append(b[i] + " ");
		return sb.toString();
	} */
	/**
	 * converts a byte array into a String in hexidecimal form.
	 * @param b byte[] to turn to a String
	 * @return String of bytes in hexadecimal format
	 */
	public static String byteArrayToString(byte[] b) {
		if(b == null) {
			return null;
		}
		String ret = "";
		int sz = b.length;

		for(int i=0; i<sz; i++) {
			ret = ret.concat(byteToString(b[i])).concat((i<sz-1)?" ":"");
		}
		return ret;
	}
	/*
	public static public StringBuffer byteArrayToStringBuffer(byte[] b) {
		if(b == null) {
			return null;
		}
		StringBuffer ret = new StringBuffer();
		int sz = b.length;

		for(int i=0; i<sz; i++) {
			ret.append( byteToString(b[i]) );
		}
		return ret;
	} */

	/**
	 * converts a hexidecimal form of String to a byte array.
	 * @param s String of bytes in hexadecimal format
	 * @return byte[] values of hexadecimal-string
	 */
	
	public static byte[] stringToByteArray(String bstr) {
		if(bstr == null)
			return null;
		
		String[] st = bstr.split("\\s"); // split to remove whitespaces
		byte[] bytes = new byte[st.length];
		for (int i=0; i< st.length; i++)
			bytes[i] = stringToByte(st[i].trim());

		return bytes;		
		/*
		String trimStr = "";
		for (int i=0; i< st.length; i++)
			trimStr = trimStr.concat(st[i].trim());
		
		System.out.println("trimmed: ["+trimStr+"]");
		int sz = trimStr.length();
		byte[] bytes = new byte[sz/2];

		for(int i=0; i<sz/2; i++) {
			bytes[i] = stringToByte(trimStr.substring(2*i,2*i+2));
		}
		return bytes;
		*/
	}
}
