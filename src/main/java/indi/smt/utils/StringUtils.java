package indi.smt.utils;

import java.util.Map;

public class StringUtils {

	
	/**
	 * 判断字符串是否为null或者""
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str == null || str.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断字符串是否为URL格式
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url){
		if(isEmpty(url)){
			return false;
		}
		String regex = "^((https|http)?://)[^\\s]+";
		if(url.matches(regex)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断字符串是否为带参数的URL
	 * @param url
	 * @return
	 */
	public static boolean isUrlWithParam(String url){
		if(isEmpty(url)){
			return false;
		}
		String regex = "^((https|http)?:\\/\\/)[^\\s]+";
		if(url.matches(regex) && url.contains("?")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 合成参数，将给定的字符串合成为URL的参数格式
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public static String composeParam(Map<String,String> params) throws Exception{
		if(params.isEmpty() || params == null){
			throw new Exception("输入参数为空");
		}
		String result = "?";
		for(String key : params.keySet()){
			if(StringUtils.isEmpty(key)){
				continue;
			}
			result += (key + "=" + params.get(key) + "&");
		}
		result.substring(0, result.lastIndexOf("&"));
		return result;
	}
}
