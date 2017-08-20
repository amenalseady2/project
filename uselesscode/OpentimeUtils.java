package com.onemena.utils;

import org.json.JSONException;

/** 
 * @author  张玉水 
 * @date 创建时间：2016-5-13 下午2:57:23 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public class OpentimeUtils {
	public static  String  parseOpenTime(String openTime1){
		
		StringBuffer sb = new StringBuffer();
		String lastOpentime="";
		String lastCloseime="";
		org.json.JSONArray openTime;
		try {
//			openTime1 ="[{\"closeTime\":\"2016-05-11 14:59\",\"openTime\":\"2016-05-11 14:59\",\"week\":\"1\"}," +
//					"{\"closeTime\":\"2016-05-11 14:59\",\"openTime\":\"2016-05-11 14:59\",\"week\":\"2\"}," +
//					"{\"closeTime\":\"2016-05-11 14:59\",\"openTime\":\"2016-05-11 15:59\",\"week\":\"3\"}," +
//					"{\"closeTime\":\"2016-05-11 14:59\",\"openTime\":\"2016-05-11 14:59\",\"week\":\"4\"}," +
//					"{\"closeTime\":\"2016-05-11 14:59\",\"openTime\":\"2016-05-11 15:59\",\"week\":\"5\"}," +
//					"{\"closeTime\":\"2016-05-11 14:59\",\"openTime\":\"2016-05-11 16:59\",\"week\":\"6\"}," +
//					"{\"closeTime\":\"2016-05-11 14:59\",\"openTime\":\"2016-05-11 17:59\",\"week\":\"7\"}]";
			openTime = new org.json.JSONArray(openTime1);
			for (int i = 0; i < openTime.length(); i++) {
				
				org.json.JSONObject jsonObject = (org.json.JSONObject) openTime.get(i);
				String week = jsonObject.getString("week");
				String closeTimeString = jsonObject.getString("closeTime");
				closeTimeString=(String) closeTimeString.subSequence(closeTimeString.indexOf(' '), closeTimeString.length()) ;
				String openTimeString = jsonObject.getString("openTime");
				openTimeString=(String) openTimeString.subSequence(openTimeString.indexOf(' '), openTimeString.length()) ;
				Flag : switch (Integer.parseInt(week)) {
				case 1:
					sb.append("周一 "+openTimeString+"~"+closeTimeString);
					lastOpentime=openTimeString;
					lastCloseime=closeTimeString;
					break;
				case 2:
					
					if (lastOpentime.equals(openTimeString)&& lastCloseime.equals(closeTimeString)) {
						sb.insert(getFirstNumIndex(sb.toString(),"周一"), "周二 ");
					}else {
						sb.append("\r\n"+"周二"+openTimeString+"~"+closeTimeString);
					}
					lastOpentime=openTimeString;
					lastCloseime=closeTimeString;
					break;
				case 3:
					if (lastOpentime.equals(openTimeString)&& lastCloseime.equals(closeTimeString)) {
						
						sb.insert(getFirstNumIndex(sb.toString(),"周二"), "周三 ");
					}else {
						
						for (int j = 0; j <1; j++) {
							org.json.JSONObject jsonObjectj = (org.json.JSONObject) openTime.get(j);
							String closeTimeStringj = jsonObjectj.getString("closeTime");
							closeTimeStringj=(String) closeTimeStringj.subSequence(closeTimeStringj.indexOf(' '), closeTimeStringj.length()) ;
							String openTimeStringj = jsonObjectj.getString("openTime");
							openTimeStringj=(String) openTimeStringj.subSequence(openTimeStringj.indexOf(' '), openTimeStringj.length()) ;
							if (openTimeStringj.equals(openTimeString)&& closeTimeStringj.equals(closeTimeString)) {
								sb.insert(getFirstNumIndex(sb.toString(),"周一"), "周三 ");
								lastOpentime=openTimeString;
								lastCloseime=closeTimeString;
								break Flag;
							}
						}
						
						sb.append("\r\n"+"周三"+openTimeString+"~"+closeTimeString);
					}
					lastOpentime=openTimeString;
					lastCloseime=closeTimeString;
					break;
				case 4:
					if (lastOpentime.equals(openTimeString)&& lastCloseime.equals(closeTimeString)) {
						sb.insert(getFirstNumIndex(sb.toString(),"周三"), "周四 ");
					}else {
						for (int j = 0; j <2; j++) {
							org.json.JSONObject jsonObjectj = (org.json.JSONObject) openTime.get(j);
							String closeTimeStringj = jsonObjectj.getString("closeTime");
							closeTimeStringj=(String) closeTimeStringj.subSequence(closeTimeStringj.indexOf(' '), closeTimeStringj.length()) ;
							String openTimeStringj = jsonObjectj.getString("openTime");
							openTimeStringj=(String) openTimeStringj.subSequence(openTimeStringj.indexOf(' '), openTimeStringj.length()) ;
							if (openTimeStringj.equals(openTimeString)&& closeTimeStringj.equals(closeTimeString)) {
								if (j==0) {
									sb.insert(getFirstNumIndex(sb.toString(),"周一"), "周四 ");
								}else {
									sb.insert(getFirstNumIndex(sb.toString(),"周二"), "周四 ");
								}
								lastOpentime=openTimeString;
								lastCloseime=closeTimeString;
								break Flag;
							}
						}
						sb.append("\r\n"+"周四"+openTimeString+"~"+closeTimeString);
					}
					lastOpentime=openTimeString;
					lastCloseime=closeTimeString;
					break;
				case 5:
					if (lastOpentime.equals(openTimeString)&& lastCloseime.equals(closeTimeString)) {
						sb.insert(getFirstNumIndex(sb.toString(),"周四"), "周五 ");
					}else {
						for (int j = 0; j <3; j++) {
							org.json.JSONObject jsonObjectj = (org.json.JSONObject) openTime.get(j);
							String closeTimeStringj = jsonObjectj.getString("closeTime");
							closeTimeStringj=(String) closeTimeStringj.subSequence(closeTimeStringj.indexOf(' '), closeTimeStringj.length()) ;
							String openTimeStringj = jsonObjectj.getString("openTime");
							openTimeStringj=(String) openTimeStringj.subSequence(openTimeStringj.indexOf(' '), openTimeStringj.length()) ;
							if (openTimeStringj.equals(openTimeString)&& closeTimeStringj.equals(closeTimeString)) {
								if (j==0) {
									sb.insert(getFirstNumIndex(sb.toString(),"周一"), "周五 ");
								}else if (j==1){
									sb.insert(getFirstNumIndex(sb.toString(),"周二"), "周五 ");
								}else if (j==2){
									sb.insert(getFirstNumIndex(sb.toString(),"周三"), "周五 ");
								}
								lastOpentime=openTimeString;
								lastCloseime=closeTimeString;
								break Flag;
							}
						}
						sb.append("\r\n"+"周五"+openTimeString+"~"+closeTimeString);
					}
					lastOpentime=openTimeString;
					lastCloseime=closeTimeString;
					break;
				case 6:
					if (lastOpentime.equals(openTimeString)&& lastCloseime.equals(closeTimeString)) {
						sb.insert(getFirstNumIndex(sb.toString(),"周五"), "周六 ");
					}else {
						
						for (int j = 0; j <4; j++) {
							org.json.JSONObject jsonObjectj = (org.json.JSONObject) openTime.get(j);
							String closeTimeStringj = jsonObjectj.getString("closeTime");
							closeTimeStringj=(String) closeTimeStringj.subSequence(closeTimeStringj.indexOf(' '), closeTimeStringj.length()) ;
							String openTimeStringj = jsonObjectj.getString("openTime");
							openTimeStringj=(String) openTimeStringj.subSequence(openTimeStringj.indexOf(' '), openTimeStringj.length()) ;
							if (openTimeStringj.equals(openTimeString)&& closeTimeStringj.equals(closeTimeString)) {
								if (j==0) {
									sb.insert(getFirstNumIndex(sb.toString(),"周一"), "周六 ");
								}else if (j==1){
									sb.insert(getFirstNumIndex(sb.toString(),"周二"), "周六 ");
								}else if (j==2){
									sb.insert(getFirstNumIndex(sb.toString(),"周三"), "周六 ");
								}else if (j==3){
									sb.insert(getFirstNumIndex(sb.toString(),"周四"), "周六 ");
								}
								lastOpentime=openTimeString;
								lastCloseime=closeTimeString;
								break Flag;
							}
						}
						
						sb.append("\r\n"+"周六"+openTimeString+"~"+closeTimeString);
					}
					lastOpentime=openTimeString;
					lastCloseime=closeTimeString;
					break;
				case 7:
					if (lastOpentime.equals(openTimeString)&& lastCloseime.equals(closeTimeString)) {
						sb.insert(getFirstNumIndex(sb.toString(),"周六"), "周日 ");
					}else {
						
						for (int j = 0; j <5; j++) {
							org.json.JSONObject jsonObjectj = (org.json.JSONObject) openTime.get(j);
							String closeTimeStringj = jsonObjectj.getString("closeTime");
							closeTimeStringj=(String) closeTimeStringj.subSequence(closeTimeStringj.indexOf(' '), closeTimeStringj.length()) ;
							String openTimeStringj = jsonObjectj.getString("openTime");
							openTimeStringj=(String) openTimeStringj.subSequence(openTimeStringj.indexOf(' '), openTimeStringj.length()) ;
							if (openTimeStringj.equals(openTimeString)&& closeTimeStringj.equals(closeTimeString)) {
								if (j==0) {
									sb.insert(getFirstNumIndex(sb.toString(),"周一"), "周日 ");
								}else if (j==1){
									sb.insert(getFirstNumIndex(sb.toString(),"周二"), "周日 ");
								}else if (j==2){
									sb.insert(getFirstNumIndex(sb.toString(),"周三"), "周日 ");
								}else if (j==3){
									sb.insert(getFirstNumIndex(sb.toString(),"周四"), "周日 ");
								}else if (j==4){
									sb.insert(getFirstNumIndex(sb.toString(),"周五"), "周日 ");
								}
								lastOpentime=openTimeString;
								lastCloseime=closeTimeString;
								break Flag;
							}
						}
						
						sb.append("\r\n"+"周日"+openTimeString+"~"+closeTimeString);
					}
					break;
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		return sb.toString();
		
	}

//得到字符串中第一个是数字的index
	public static int getFirstNumIndex(String s,String addstring){
		for (int i = addstring.length()+s.indexOf(addstring); i < s.length(); i++) {
			char c = s.charAt(i);
			if (c>='0' && c <='9') {
				return i;
			}
			
		}
		return s.length()-1;
	}
}
