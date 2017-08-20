package com.onemena.utils;

import android.content.Context;
import android.os.Environment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FileUtils {

	public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
	
	/**
     * 获取文件夹对象
     * 
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(getSDCardPath() + File.separator + folderName
                + File.separator);
        file.mkdirs();
        return file;
    }
	
	
	/**
     * 获取SD卡下指定文件夹的绝对路径
     * 
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }
	
	/**
     * 从指定文件夹获取文件
     * 
     * @return 如果文件不存在则创建,如果如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator
                + fileNmae);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }








	/***********************************************************************/
	/**
	 * 获取目录文件大小
	 *
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				long length = file.length();

				//4k对齐，应用占用空间是4k对齐的
//				int minSize=4*1024;
//				if (length%minSize!=0) {
//					length=length/minSize+1;
//					length=length*minSize;
//				}

				dirSize += length;
//				Log.e("cache_size","file--"+file.getName());
			} else if (file.isDirectory()) {
//				Log.e("cache_size","dir--"+file.getName());
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}


	/**
	 * 判断是否安装目标应用
	 *
	 * @param packageName 目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	public static boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	public static JSONObject getLocalJson(Context context, int jsonfile){
		StringBuffer stringBuffer = new StringBuffer();
		try {
			InputStream is =context.getResources().openRawResource(jsonfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String str = null;
			while((str = br.readLine())!=null){
				stringBuffer.append(str);
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
		return JSON.parseObject(stringBuffer.toString());
	}

	public static JSONArray getLocalJsonArray(Context context, int jsonfile){
		StringBuffer stringBuffer = new StringBuffer();
		try {
			InputStream is =context.getResources().openRawResource(jsonfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String str = null;
			while((str = br.readLine())!=null){
				stringBuffer.append(str);
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
		return JSON.parseArray(stringBuffer.toString());
	}

	public static JSONObject getAssetLocalJson(Context context, String fileuri){
		StringBuffer stringBuffer = new StringBuffer();
		try {
			InputStream is =context.getAssets().open("localnews/"+fileuri+".json");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String str = null;
			while((str = br.readLine())!=null){
				stringBuffer.append(str);
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
		return JSON.parseObject(stringBuffer.toString());
	}
}
