package com.bigmacdev.all_med.test;


import com.bigmacdev.all_med.model.Patient;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;
import java.util.ArrayList;

public class UnitTests {

	public static void main(String[] args) {
		//System.out.println(testCreatePatient());
		//System.out.println(testLatestFileName());
		//testReadFile();
		/*ArrayList<String> ss = new ArrayList<String>();
		String s = "type1, type2, type3, type 4";
		s=s.replaceAll(", ", ",");
		System.out.println(s);
		int x = s.indexOf(",");
		while(x!=s.length()){
			x = s.indexOf(",");
			if (x <= 0) {
				x=s.length();
			}
			ss.add(s.substring(0,x));
			if(x!=s.length()){
				s=s.substring(x+1,s.length());
			}

			System.out.println(s);
		}
		System.out.println(ss.toString());*/


		ArrayList<String> s1 = new ArrayList<String>();
		ArrayList<String> s2 = new ArrayList<String>();
		s1.add("abc");
		s2.add("abc");
		s1.add("def");
		s2.add("def");
		//s1.add("xyz");
		//s2.add("pqu");
		System.out.println(s1.equals(s2));



		/*try {
			new File("test/test/test").mkdirs();
			new File("test/test/test/test.txt").createNewFile();
		}catch (Exception e){
			e.printStackTrace();
		}*/
/*
		String toEncrypt = "michael";
		String e1,e2,e3,e4,e5;
		e1=createHashTest(toEncrypt,toEncrypt);
		e2=createHashTest(toEncrypt,toEncrypt);
		e3=createHashTest(toEncrypt,toEncrypt);
		e4=createHashTest(toEncrypt,toEncrypt);
		e5=createHashTest(toEncrypt,toEncrypt);
		System.out.println("Encrypted 1: "+e1);
		System.out.println("Encrypted 2: "+e2);
		System.out.println("Encrypted 3: "+e3);
		System.out.println("Encrypted 4: "+e4);
		System.out.println("Encrypted 5: "+e5);
		System.out.println("Equal 1: "+toEncrypt.equals(decryptHashTest(e1,toEncrypt)));
		System.out.println("Equal 2: "+toEncrypt.equals(decryptHashTest(e2,toEncrypt)));
		System.out.println("Equal 3: "+toEncrypt.equals(decryptHashTest(e3,toEncrypt)));
		System.out.println("Equal 4: "+toEncrypt.equals(decryptHashTest(e4,toEncrypt)));
		System.out.println("Equal 5: "+toEncrypt.equals(decryptHashTest(e5,toEncrypt))); */

		//String unEncryptedString = "";
		//String encryptedString= testEncryptionCreation(unEncryptedString);
		//System.out.println(encryptedString);
		//System.out.println(testEncryptionDecryption(encryptedString));


	}

	private static void testReadFile(){
		Patient pt = new Patient("michael","lott",1995,8,14);
		String s1 = pt.getFilePath();
		System.out.println(s1);
		String s2 = pt.getLatestRecord(s1);
		System.out.println(s2);
		pt.getPatientData(s1);
	}

	private static String createHashTest(String s, String k){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(k);
		return textEncryptor.encrypt(s);
	}

	private static String decryptHashTest(String s, String k){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(k);
		return textEncryptor.decrypt(s);
	}

	private static String testLatestFileName(){
		Patient pt = new Patient("Bob","Test",1905,3,1);
		return pt.getLatestRecord(pt.getFilePath());
	}

	private static String testEncryptionDecryption(String x){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(encryptionKey());
		return textEncryptor.decrypt(x);
	}

	private static String testCreatePatient(){
		Patient pt = new Patient("Bob","Test",1905,3,1);
		pt.createFile();
		return (""+new File(pt.getFilePath()).exists());
	}

	private static String testEncryptionCreation(String x){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(encryptionKey());
		return textEncryptor.encrypt(x);
	}



	private static String encryptionKey(){
		Long unixTime = System.currentTimeMillis()/1000000;
		//System.out.println(""+unixTime);
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
