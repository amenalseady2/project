package com.onemena.net;

import com.onemena.utils.LogManager;
import com.onemena.utils.MyLocationManager;

import java.util.HashMap;
import java.util.Map.Entry;



/**
 * 控制请求接口参数
 * @author 张玉水  E-mail: 79696368@qq.com
 * @version 创建时间：2015-4-21  下午1:15:58
 */
public class MapParams {

	private static MapParams instance ;


	public static synchronized MapParams getInstance (){
		if(instance == null){
			synchronized (MapParams.class) {
				if(instance == null){
					instance = new MapParams();
				}
			}
		}
		return instance;
		
	}
	
	//每次网络请求的时候，都传给服务器kegparam,还有经纬度坐标
	public HashMap<String, String> addCommonPara(HashMap<String, String> paramap){
		if(paramap==null){
			paramap=new HashMap<String, String>();
		}

		//paramap.put("kgParam" , UserManager.getKgParam());
		paramap.put("lat" , String.valueOf(MyLocationManager.LATITUDE == 0 ? "0.0" : MyLocationManager.LATITUDE));
		paramap.put("lng" , String.valueOf(MyLocationManager.LONGITUDE == 0 ? "0.0" : MyLocationManager.LONGITUDE));
//		addMD5mark(paramap);
		for(Entry<String, String> entry : paramap.entrySet()){
			LogManager.i("key= " + entry.getKey() + " and value= " + entry.getValue());
		}
		return paramap;
	}
	
	
//	String secretKey= "keegoo!@#$$#@!zxcvbnmplqawsokdekiujrfhytg";
	
	/**
	 * MD5加密功能,防盗链
	 * @Description: 
	 * @param paramap
	 * @return void 
	 * @date 2015-12-17 下午4:31:19
	 */
//	private void addMD5mark(HashMap<String, String> paramap){
//
//		long time = System.currentTimeMillis();
//		JSONObject userObject = UserManager.getUserObj();
//		if(userObject == null || "".equals(userObject.getString(MoudleUtils.OBJECTID))){
//			paramap.put("sign", MD5.GetMD5Code(time + secretKey));
//		}else{
//			paramap.put("sign", MD5.GetMD5Code(userObject.getString(MoudleUtils.OBJECTID) + userObject.getString(MoudleUtils.SESSIONTOKEN)+ time + userObject.getString(MoudleUtils.SECRETKEY)));
//		}
//
//		paramap.put("timestamp", String.valueOf(time));
//
//		publicParams(paramap);
//	}
	
	/**
	 * 获取支付sign
	 * @Description: 
	 * @return
	 * @return String 
	 * @date 2016-1-28 下午3:21:59
	 */
//	public String getOrderPaySign(){
//		HashMap<String, String> map = addCommonPara(null);
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("sign", map.get("sign"));
//		jsonObject.put("userId", map.get("userId"));
//		jsonObject.put("sessionToken", map.get("sessionToken"));
//		jsonObject.put("timestamp", map.get("timestamp"));
//		return jsonObject.toJSONString();
//	}
	
	/**
	 * 公共参数
	 * @Description: 
	 * @author 杨生辉  
	 * @param paramap
	 * @return void 
	 * @date 2015-12-17 下午4:29:16
	 */
//	private void publicParams(HashMap<String, String> paramap){
//		paramap.put("sessionToken", UserManager.getUserObj().getString(MoudleUtils.SESSIONTOKEN) == null ? "" :
//			UserManager.getUserObj().getString(MoudleUtils.SESSIONTOKEN));
//
//		if(!paramap.containsKey("userId")){
//			paramap.put("userId", UserManager.getUserObj().getString(MoudleUtils.OBJECTID) == null ? "" :
//				UserManager.getUserObj().getString(MoudleUtils.OBJECTID));
//		}
//
//		paramap.put("citycode", "110000");//默认北京
//		paramap.put("fromSource", "android");
//		paramap.put("app_v", Utility.getAppVersionName(MmshApplication.getInstance()));
//
//		for(Entry<String, String> entry : paramap.entrySet()){
//			LogManager.i("key= " + entry.getKey() + " and value= " + entry.getValue());
//		}
//
//	}
	
	
}
