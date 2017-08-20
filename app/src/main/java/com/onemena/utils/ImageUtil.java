package com.onemena.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by hlyd on 14-2-20.
 */
public class ImageUtil {

	/*
	 * 上传图片的采样尺寸，即压缩后的图片大小为 [BITMAP_UPLOAD_CELL_SIZE_WIDTH *
	 * BITMAP_UPLOAD_CELL_SIZE_HEIGHT]px^2
	 */
	public static final int BITMAP_UPLOAD_CELL_SIZE_WIDTH = 768;
	public static final int BITMAP_UPLOAD_CELL_SIZE_HEIGHT = 768;
	
	
	public static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }
	
	/**
	 * 翻转图片
	 *
	 * @author 杨生辉 79696368@qq.com
	 * @date 2015-9-11 上午10:13:05
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		Matrix m = new Matrix();
		m.setRotate(angle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
		float targetX, targetY;
		if (angle == 90) {
			targetX = bitmap.getHeight();
			targetY = 0;
		} else {
			targetX = bitmap.getHeight();
			targetY = bitmap.getWidth();
		}

		final float[] values = new float[9];
		m.getValues(values);

		float x1 = values[Matrix.MTRANS_X];
		float y1 = values[Matrix.MTRANS_Y];
		m.postTranslate(targetX - x1, targetY - y1);
		Bitmap bm1 = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getWidth(), Config.ARGB_8888);
		Paint paint = new Paint();
		Canvas canvas = new Canvas(bm1);
		canvas.drawBitmap(bitmap, m, paint);
		return bm1;
	}
	
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
    

	/*
	 * End: 获取远端Url图片，具有二级缓存机制的回调策略实现及全局静态引用
	 */

	/*
	 * Begin:根据path从SDCard中读取图片
	 */

	/**
	 * 根据path从SDCard中读取图片
	 * 
	 * @param path
	 *            图片文件路径
	 * @param cellWidth
	 *            压缩后Bitmap宽度，单位px
	 * @param cellHeight
	 *            压缩后Bitmap高度，单位px
	 */
	public static Bitmap getBitmapBySDCardPath(String path, final int cellWidth, final int cellHeight) {
		Bitmap figure = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, cellWidth * cellHeight);
			opts.inJustDecodeBounds = false;
			opts.inPreferredConfig = Config.ARGB_4444;
			figure = BitmapFactory.decodeFile(path, opts);
		} catch (OutOfMemoryError e) {
			Log.e("getBitmapBySDCardPath", e.toString() + "（OutOfMemoryError） When loading " + path + " from sdcard.");
		}
		return figure;
	}
	
	
	/**
	 * 根据path从SDCard中读取图片
	 * 
	 * @param path
	 *            图片文件路径
	 */
	public static Bitmap getBitmapBySDCardPath(String path) {
		Bitmap figure = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, BITMAP_UPLOAD_CELL_SIZE_WIDTH * BITMAP_UPLOAD_CELL_SIZE_HEIGHT);
			opts.inJustDecodeBounds = false;
			opts.inPreferredConfig = Config.ARGB_4444;
			figure = BitmapFactory.decodeFile(path, opts);
		} catch (OutOfMemoryError e) {
			Log.e("getBitmapBySDCardPath", e.toString() + "（OutOfMemoryError） When loading " + path + " from sdcard.");
		}
		return figure;
	}

	/**
	 * 根据id读取图片
	 * 
	 * @param path
	 *            图片文件路径
	 */
	public static Bitmap getBitmapByID(Resources cxt, int id, int width, int height) {
		Bitmap figure = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(cxt, id, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, width * height);
			opts.inJustDecodeBounds = false;
			opts.inPreferredConfig = Config.ARGB_4444;
			figure = BitmapFactory.decodeResource(cxt, id, opts);
		} catch (OutOfMemoryError e) {
			Log.e("getBitmapBySDCardPath", e.toString() + "（OutOfMemoryError） When loading " + " from sdcard.");
		}
		return figure;
	}

	/**
	 * 根据id读取图片 简单处理
	 * 
	 * @param path
	 *            图片文件路径
	 */
	public static Bitmap getBitmapByID(Activity cxt, int id) {
		Bitmap figure = null;
		Display currentDisplay = cxt.getWindowManager().getDefaultDisplay();
		int dw = currentDisplay.getWidth();
		int dh = currentDisplay.getHeight();
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			figure = BitmapFactory.decodeResource(cxt.getResources(), id, opts);
			opts.inPurgeable = true;
			int heightRatio = (int) Math.ceil(opts.outHeight / (float) dh);
			int widthRatio = (int) Math.ceil(opts.outWidth / (float) dw);

			if (heightRatio > 1 && widthRatio > 1) {
				opts.inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
			}
			opts.inJustDecodeBounds = false;
			figure = BitmapFactory.decodeResource(cxt.getResources(), id, opts);
		} catch (OutOfMemoryError e) {
			Log.e("getBitmapBySDCardPath", e.toString() + "（OutOfMemoryError） When loading " + " from sdcard.");
		}
		return figure;
	}

	/**
	 * 计算采样率
	 * 
	 * @param options
	 *            解析图片所需的BitmapFactory.Options
	 * @param minSideLength
	 *            调整后图片最小的宽或高值
	 * @param maxNumOfPixels
	 *            调整后图片的内存占用量上限
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * 计算系统默认采样率
	 * 
	 * @param options
	 *            解析图片所需的BitmapFactory.Options
	 * @param minSideLength
	 *            调整后图片最小的宽或高值
	 * @param maxNumOfPixels
	 *            调整后图片的内存占用量上限
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/*
	 * End:根据path从SDCard中读取图片
	 */

	/**
	 * 清理ImageView的Bitmap资源，防止OOM
	 */
	public static void recycleBitmapOfImageView(ImageView imageView) {
		Drawable drawable = imageView.getDrawable();
		if (drawable != null && drawable instanceof BitmapDrawable) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
	}

	/**
	 * 将图片转换成圆形带框的图片
	 * 
	 * @author: Wangchao
	 * @email: super0086@qq.com
	 * @date: 2014年9月1日 上午11:29:16
	 * @Description: 描述作用
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundBitmap(Context context, Bitmap bitmap) {
		Bitmap retBitmap = null;
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float roundPx;
			float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
			if (width <= height) {
				roundPx = width / 2;
				top = 0;
				left = 0;
				bottom = width;
				right = width;
				height = width;
				dst_left = 0;
				dst_top = 0;
				dst_right = width;
				dst_bottom = width;
			} else {
				roundPx = height / 2;
				float clip = (width - height) / 2;
				left = clip;
				right = width - clip;
				top = 0;
				bottom = height;
				width = height;
				dst_left = 0;
				dst_top = 0;
				dst_right = height;
				dst_bottom = height;
			}

			retBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(retBitmap);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
			final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
			final RectF rectF = new RectF(dst);

			paint.setAntiAlias(true);

			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(4);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, src, dst, paint);

			// 画白色圆圈
			int stroke_width = 4;
			paint.reset();
			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(stroke_width);
			paint.setAntiAlias(true);
			canvas.drawCircle(width / 2, width / 2, width / 2 - stroke_width / 2, paint);
		} catch (Exception e) {
			retBitmap = null;
		} finally {
			return retBitmap;
		}
	}

	/**
	 * 将图片转换成圆形带框的图片
	 * 
	 * @author: Wangchao
	 * @email: super0086@qq.com
	 * @date: 2014年9月1日 上午10:55:41
	 * @Description: 描述作用
	 * @param context
	 * @param resid
	 * @return
	 */
	public static Bitmap toRoundBitmap(Context context, int resid) {
		Bitmap retBitmap = null;
		try {
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resid);
			retBitmap = toRoundBitmap(context, bitmap);
		} catch (Exception e) {
			retBitmap = null;
		} finally {
			return retBitmap;
		}
	}

	/**
	 * 讲uri转换为bitmap
	 * 
	 * @author: wang
	 * @email:super0086@qq.com
	 * @date： 2014年11月18日 上午12:44:30
	 * @Description: 描述作用
	 * @param 参数
	 * @return 返回值
	 */
	public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	public static String getAbsoluteImagePath(Activity context, Uri uri) {
		String imagePath = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.managedQuery(uri, proj, // Which columns to
														// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	/**
	 * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
		String filePath = null;

		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);

		String pre1 = "file://" + "/sdcard" + File.separator;
		String pre2 = "file://" + "/mnt/sdcard" + File.separator;

		if (mUriString.startsWith(pre1)) {
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre1.length());
		} else if (mUriString.startsWith(pre2)) {
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre2.length());
		}
		return filePath;
	}

	/**
	 * 获取图片方向
	 * 
	 * @author: wang
	 * @email:super0086@qq.com
	 * @date： 2014年11月27日 下午3:44:00
	 * @Description: 描述作用
	 * @param 参数
	 * @return 返回值
	 */
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}

	/**
	 * @param simpleDraweeView
	 * @param resId
	 */
	public static void loadGifPicInApp(@NonNull SimpleDraweeView simpleDraweeView, @NonNull int resId) {
		if (simpleDraweeView == null) {
			return;
		}
		Uri uri = new Uri.Builder()
				.scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
				.path(String.valueOf(resId))
				.build();
		DraweeController draweeController = Fresco.newDraweeControllerBuilder()
				.setUri(uri)
				.setAutoPlayAnimations(true)
				.build();
		simpleDraweeView.setController(draweeController);//"file:///android_asset/localpicture"
	}
	/**
	 * @param simpleDraweeView
	 * @param resId
	 */
	public static void loadGifPicInApp(@NonNull SimpleDraweeView simpleDraweeView, @NonNull String resId) {
		if (simpleDraweeView == null) {
			return;
		}
		DraweeController draweeController = Fresco.newDraweeControllerBuilder()
				.setAutoPlayAnimations(true)
				.setUri(Uri.parse(resId))
				.build();
		simpleDraweeView.setController(draweeController);//"file:///android_asset/localpicture"
	}

}
