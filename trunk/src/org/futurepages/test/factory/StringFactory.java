package org.futurepages.test.factory;

import java.util.UUID;

public class StringFactory {

	private static String lastUniqueGenerated;

	public static String getUniqueMail(){
		String pre  = getRandom(10);
		String suf1 = getRandom(10);
		String suf2 = getRandom(10);
		return pre+"@"+suf1+"."+suf2;
	}
	
	public static String getUnique(){
		String newOne = System.currentTimeMillis()+"";
		if(lastUniqueGenerated != null && newOne.equals(lastUniqueGenerated)){
			newOne += "$";
		}
		lastUniqueGenerated = newOne;
		return newOne;
	}

	public static String getRandom() {
		return getRandom(256);
	}
	
	public static String getRandomFileName(int length) {
		return getRandom(length).replaceAll("[-\\.]", "");
	}
	
	public static String getRandom(int length) {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString().trim();
		while (myRandom.length()<length) {
			myRandom = myRandom.concat(myRandom);
		}
		return myRandom.substring(0,length);
	}
}
