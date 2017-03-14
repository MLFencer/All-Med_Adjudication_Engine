package com.bigmacdev.all_med.test;


import com.bigmacdev.all_med.model.Patient;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;

public class UnitTests {

	public static void main(String[] args) {
		//System.out.println(testCreatePatient());
		/*try {
			new File("test/test/test").mkdirs();
			new File("test/test/test/test.txt").createNewFile();
		}catch (Exception e){
			e.printStackTrace();
		}*/

		String unEncryptedString = "¬í \u0005sr \fmodel.Person5è\n" +
				"¥NI\u0090z\u0002 \u0006I \u0004dobDI \u0004dobMI \u0004dobYL \u0005fNamet \u0012Ljava/lang/String;L \u0005lNameq ~ \u0001L \u0005mNameq ~ \u0001xp   \u0004   \u0005  \u0007Ét \u0003bobt \u0004testp";
		//String encryptedString= testEncryptionCreation(unEncryptedString);
		//System.out.println(encryptedString);
		//System.out.println(testEncryptionDecryption(encryptedString));


	}

	private static String testEncryptionDecryption(String x){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(encryptionKey());
		return textEncryptor.decrypt(x);
	}

	private static String testCreatePatient(){
		Patient pt = new Patient("Bob","Test",1905,3,1);
		pt.createPatient();
		return (""+new File(pt.getFilePath()).exists());
	}

	private static String testEncryptionCreation(String x){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(encryptionKey());
		return textEncryptor.encrypt(x);
	}



	private static String encryptionKey(){
		Long unixTime = System.currentTimeMillis()/1000000;
		System.out.println(""+unixTime);
		String keyGenSeed = unixTime+"";
		String output="";
		String keyGenSeedStart=keyGenSeed;
		while (keyGenSeed.length()>0) {
			char letter = keyGenSeed.charAt(0);
			keyGenSeed=keyGenSeed.substring(1, keyGenSeed.length());
			switch (letter){
				case '1':
					output+="a";
					break;
				case '2':
					output+="b";
					break;
				case '3':
					output+="c";
					break;
				case '4':
					output+="d";
					break;
				case '5':
					output+="e";
					break;
				case '6':
					output+="f";
					break;
				case '7':
					output+="g";
					break;
				case '8':
					output+="h";
					break;
				case '9':
					output+="j";
					break;
				case '0':
					output+="k";
					break;
			}
		}
		return output+keyGenSeedStart+output;
	}
}
