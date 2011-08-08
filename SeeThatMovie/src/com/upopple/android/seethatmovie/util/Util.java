/**
 * 
 */
package com.upopple.android.seethatmovie.util;

import java.util.ArrayList;

/**
 * @author upopple
 *
 */
public class Util {
	public static String arrayListToString(ArrayList<String> arr){
		String val = "";
		for(String str : arr)
			val += str + ", ";
		val = val.substring(0, val.length()-2);
		return val;
	}
}
