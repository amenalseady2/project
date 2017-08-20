package com.shanggame.news.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;

import com.mysada.news.app.config.Config;
import com.mysada.news.app.config.ConfigUrls;
import com.mysada.news.listener.JsonObjectListener;
import com.mysada.news.service.PublicService;
import com.onemena.utils.ImageUtil;
import com.onemena.utils.MoudleUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;


/**
 * 七牛上传图片服务
 *
 * @author 杨生辉 79696368@qq.com
 * @date 2015-9-10 下午4:38:52
 *
 */
public class UploadImageService {
	private String host;
	private String tokenStr;
	private SparseBooleanArray booleanArray;
	private HashMap<String,String> url;
//	private onAllSucessListener allSucessListener;
//	private onSucessListener sucessListener;
//	public void setAllSucessListener(onAllSucessListener allSucessListener) {
//		this.allSucessListener = allSucessListener;
//	}
//	public void setSucessListener(onSucessListener sucessListener) {
//		this.sucessListener = sucessListener;
//	}

	public static class IntanceHolder{
		private static final UploadImageService INSTANCE=new UploadImageService();
	}

	public static UploadImageService getInstance(){
		return IntanceHolder.INSTANCE;
	}

	public void getToken(final onSucessListener sucessListener) {

		HashMap<String, String> keyMap = new HashMap<String, String>();
		keyMap.put("userId", "");
		PublicService.getInstance().postJsonObjectRequest( ConfigUrls.PHOTO_TOKEN,keyMap , new JsonObjectListener() {
			@Override
			public void onJsonObject(com.alibaba.fastjson.JSONObject obj) {
				if(obj.containsKey(MoudleUtils.UPTOKEN)){

					String token = obj.getString(MoudleUtils.UPTOKEN);
					String domain = obj.getString(MoudleUtils.DOMAIN);
					if(sucessListener != null){
						sucessListener.onSucess(token, domain);
					}
				}else{
					if(sucessListener!=null){
						sucessListener.onError();
					}
				}
			}

			@Override
			public void onObjError() {
				if(sucessListener!=null){
					sucessListener.onError();
				}
			}
		});
	}

	/**
	 * 质量压缩
	 *
	 * @author 杨生辉 79696368@qq.com
	 * @date 2015-9-10 下午5:51:29
	 * @param image
	 * @return
	 */
	public Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 按图片比例压缩
	 *
	 * @author 杨生辉 79696368@qq.com
	 * @date 2015-9-10 下午5:51:17
	 * @param srcPath
	 * @return
	 */
	public  Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = Config.displayWidth;//这里设置高度为800f
		float ww = Config.displayHeight;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	}

	/**
	 * 上传图片到七牛
	 *
	 * @author 杨生辉 79696368@qq.com
	 * @date 2015-9-11 上午10:12:31
	 * @param file
//	 * @param position
	 */
//	private void uploadFile(String key ,String file,final int position){
//		UploadManager manager = new UploadManager();
//
//		int degree = ImageUtil.readPictureDegree(new File(file).getAbsolutePath());
//		Bitmap bit = getimage(file);
//		if(degree != 0){
//			bit = ImageUtil.rotaingImageView(degree, bit);
//		}
//		manager.put( ImageUtil.bitmapToByte(bit), null, tokenStr, new UpCompletionHandler() {
//			@Override
//			public void complete(String arg0, ResponseInfo info, JSONObject jsonObject) {
//				if(info.statusCode == 200){
//					try {
//						String key = jsonObject.getString(MoudleUtils.KEY);
//						url.put(key, host+key);
//						booleanArray.put(position, true);
//						if(isAllSucess()){
//							if(allSucessListener!=null){
//								allSucessListener.onSucess(url);
//							}
//						}
//					} catch (JSONException e) {
//						e.printStackTrace();
//						if(allSucessListener!=null){
//							allSucessListener.onError(booleanArray);
//						}
//					}
//				}else {
//					if(allSucessListener!=null){
//						allSucessListener.onError(booleanArray);
//					}
//				}
//			}
//		}, null);
//	}
	//上传文件并且监听
	public void uploadFileAndListener(final String myHost , final String imgKey,String token , String file ,final onAllSucessListener allSucessListener){
		UploadManager manager = new UploadManager();

		int degree = ImageUtil.readPictureDegree(new File(file).getAbsolutePath());
		Bitmap bit = getimage(file);
		if(degree != 0){
			bit = ImageUtil.rotaingImageView(degree, bit);
		}
		manager.put(ImageUtil.bitmapToByte(bit), null, token,new UpCompletionHandler() {
			@Override
			public void complete(String arg0, ResponseInfo info, JSONObject jsonObject) {
				if(info.statusCode == 200){
					try {
						String key = jsonObject.getString(MoudleUtils.KEY);
						if(allSucessListener!=null){
							allSucessListener.onSucess(imgKey ,myHost+key);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						if(allSucessListener!=null){
							allSucessListener.onAllError();
						}
					}
				}else {
					if(allSucessListener!=null){
						allSucessListener.onAllError();
					}
				}
			}
		},new UploadOptions(null, null, false,
			        new UpProgressHandler(){
			            public void progress(String key, double percent){
			                if(allSucessListener!=null){
								allSucessListener.onProgress(imgKey ,key, percent);
							}
			            }
			        }, null));
	}



	public interface onSucessListener{
		void onSucess(String token, String domain);
		void onError();
	}
	public interface onAllSucessListener{
		void onSucess(String imgKey, String url);
		void onAllError();
		void onProgress(String imgKey, String key, double percent);
	}
}
