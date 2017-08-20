package com.onemena.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.alibaba.fastjson.JSONArray;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @ClassName:StringUtils.java
 * @Description:字符串工具类
 * @author:wangchao
 * @email:super0086@qq.com
 * @version:V1.0
 * @Date:2014年7月1日 下午2:01:46
 */
public class StringUtils {

	public static String getMoneyDouble(double money) {
		DecimalFormat df = new DecimalFormat("######0.00");

		return df.format(money);
	}

	public static String int2IndiaNum(int num) {
		return int2IndiaNum(String.valueOf(num));
	}

//	٠١٢٣٤٥٦٧٨٩
//	0123456789
	public static  String int2IndiaNum(String num){
		num = org.apache.commons.lang3.StringUtils.defaultString(num,"0");
		StringBuffer stringBuffer = new StringBuffer();
		for (int i=0;i<num.length();i++){
			switch (num.charAt(i)){
				case '0':
					stringBuffer.append('٠');
					break;
				case '1':
					stringBuffer.append('١');
					break;
				case '2':
					stringBuffer.append('٢');
					break;
				case '3':
					stringBuffer.append('٣');
					break;
				case '4':
					stringBuffer.append('٤');
					break;
				case '5':
					stringBuffer.append('٥');
					break;
				case '6':
					stringBuffer.append('٦');
					break;
				case '7':
					stringBuffer.append('٧');
					break;
				case '8':
					stringBuffer.append('٨');
					break;
				case '9':
					stringBuffer.append('٩');
					break;
				case '.':
					stringBuffer.append('.');
					break;
			}
		}
		return stringBuffer.toString();
	}

	public static  String IndiaNum2Int(String num){
		num = org.apache.commons.lang3.StringUtils.defaultString(num,"0");
		StringBuffer stringBuffer = new StringBuffer();
		for (int i=0;i<num.length();i++){
			switch (num.charAt(i)){
				case '٠':
					stringBuffer.append('0');
					break;
				case '١':
					stringBuffer.append('1');
					break;
				case '٢':
					stringBuffer.append('2');
					break;
				case '٣':
					stringBuffer.append('3');
					break;
				case '٤':
					stringBuffer.append('4');
					break;
				case '٥':
					stringBuffer.append('5');
					break;
				case '٦':
					stringBuffer.append('6');
					break;
				case '٧':
					stringBuffer.append('7');
					break;
				case '٨':
					stringBuffer.append('8');
					break;
				case '٩':
					stringBuffer.append('9');
					break;
				case '.':
					stringBuffer.append('.');
					break;
			}
		}
		return stringBuffer.toString();
	}

	/**
     * 判断是否为空，空返回true
     */
    public static boolean isEmpty(String str) {
        if (null == str) {
            str = "";
        }
        return "".equals(str.trim());
    }

    /**
     * 判断是否不为空，不空返回true
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    /**
     * 清除List数据
     *
     * @param mlist
     * @return
     */
    public static boolean isClearList(JSONArray mlist) {
        boolean isFlag = true;
        try {
            if (!mlist.isEmpty() || mlist != null) {
                mlist.clear();
                isFlag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isFlag = false;
        }
        return isFlag;
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param str1
     * @param str2
     * @return 相等返回true
     */
    public static boolean isEqual(String str1, String str2) {
        boolean bRet = false;
        if (isEmpty(str1) && isEmpty(str2)) {
            bRet = true;
        } else if (isEmpty(str1) || isEmpty(str2)) {
            bRet = false;
        } else {
            bRet = str1.trim().equals(str2.trim());
        }
        return bRet;
    }

    /**
     * 去除所有空格,包括中间的
     */
    public static String removeAllSpace(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str.replaceAll("\\s+", "");
    }

    /**
     * String.trim
     */
    public static String trim(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str.trim();
    }

    /**
     * 判断字符串数组是否为空
     *
     * @param strs
     * @return 不空返回true
     */
    public static boolean isNotEmpty(String[] strs) {
        return strs != null && strs.length != 0;
    }

    /**
     * 将字符串转换为数字
     */
    public static int convertToInt(String str) {
        int iRet = 0;
        try {
            iRet = Integer.parseInt(str);
        } catch (Exception e) {
            iRet = Integer.MAX_VALUE;
        } finally {
            return iRet;
        }
    }

    /**
     * 将字符串转换为Integer
     */
    public static Integer convertToInteger(String str) {
        Integer iRet = 0;
        try {
            iRet = Integer.parseInt(str);
        } catch (Exception e) {
            iRet = null;
        } finally {
            return iRet;
        }
    }

    /**
     * 去掉 resource中的空元素
     *
     * @param resource
     * @return
     */
    public static String[] removeNull(String[] resource) {
        String[] sRet = null;
        List<String> target = new LinkedList<String>();
        if (isNotEmpty(resource)) {
            for (String s : resource) {
                if (isNotEmpty(s)) {
                    target.add(s);
                }
            }
            sRet = target.toArray(new String[target.size()]);
        }
        return sRet;
    }

    /**
     * 封装String类的split
     *
     * @param str
     * @param symbol
     * @return
     */
	@Deprecated
    public static String[] split(String str, String symbol) {
        String[] sRet = null;
        if (!isEmpty(str)) {
            sRet = str.split(trim(symbol));
        }
        return sRet;
    }

	public static String[] split2(String str, String symbol) {
		if(org.apache.commons.lang3.StringUtils.isNotBlank(str)){
			return org.apache.commons.lang3.StringUtils.split(str,symbol);
		}
		return null;
	}
    /**
     * 将首字母变为大写
     *
     * @param str
     * @return
     */
    public static String toUperFirstChar(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String convertString(String str) {
        return str.replace('.', '/');
    }

    /**
     * 编码
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        String sRet = "";
        try {
            sRet = isNotEmpty(str) ? URLEncoder.encode(str,
                    "UTF-8") : "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            sRet = "";
        } finally {
            return sRet;
        }
    }

    /**
     * 解码
     *
     * @param str
     * @return
     */
    public static String decode2(String str) {
        String sRet = "";
        try {
            sRet = isNotEmpty(str) ? URLDecoder.decode(str,
                    "UTF-8") : "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            sRet = "";
        } finally {
            return sRet;
        }
    }

	/**
	 * 将对象转至String类型
	 * 
	 * @param obj
	 * @return
	 */
	public static String convertToString(Object obj) {
		String sRet = null;
		if (obj != null) {
			sRet = String.valueOf(obj);
		}
		return sRet;
	}

	/**
	 * 将filepath转至url
	 * 
	 * @param file
	 * @return
	 */
	public static String filePathToURL(File file) {
		return file == null ? "" : filePathToURL(file.getPath());
	}

	/**
	 * 将filepath转至url
	 * 
	 * @param filePath
	 * @return
	 */
	public static String filePathToURL(String filePath) {
		String sRet = null;
		if (StringUtils.isNotEmpty(filePath)) {
			sRet = filePath.replace("\\", "//");
		}
		return sRet;
	}

	/**
	 * 获取一定为数的字符串 s代表填充的字符@author: wangchao
	 * 
	 * @email:super0086@qq.com
	 * @date： 2014年7月1日 下午2:23:54
	 * @Description: 描述作用
	 * @return 返回值
	 */
	public static String getNumberStrWithZero(String number, int length,
			String s) {
		String sRet = "";
		for (int i = 0; i < length - number.length(); i++) {
			sRet = sRet + s;
		}
		return sRet + number;
	}

	/**
	 * 判断字符串中是否包含汉字
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isIncludeChinese(String str) {
		boolean isExists = false;
		int flag = 0;
		for (int index = 0; index < str.length(); index++) {
			flag = getCharType(str.charAt(index));
			if (flag == 2) {
				isExists = true;
				break;
			}
		}
		return isExists;
	}

	/**
	 * 判断输入字符是字母或数字、中文字符、还是其它 返回值(int)： 0： 数字字符 1： 英文字符 2： 中文字符 -1：其它字符
	 */
	public static int getCharType(char ch) {
		switch (Character.getType(ch)) {
		case Character.DECIMAL_DIGIT_NUMBER:
			return 0;
		case Character.LOWERCASE_LETTER:
		case Character.UPPERCASE_LETTER:
			return 1;
		case Character.OTHER_LETTER:
			return 2;
		default:
			return -1;
		}
	}

	public static String convertNullString(String str) {
		if (str == null || str.equals("")) {
			return "";
		}
		return str.trim();
	}

	public static String convertNullObject(Object str) {
		if (str == null || str.equals("")) {
			return "";
		}
		return str.toString();
	}

	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	public static String replace(String inString, String oldPattern,
			String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern)
				|| newPattern == null) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(inString.substring(pos, index));
			sb.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sb.append(inString.substring(pos));
		// remember to append any characters to the right of a match
		return sb.toString();
	}

	public static String replaceLast(String inString, String oldPattern,
			String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern)
				|| newPattern == null) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		int pos = 0; // our position in the old string
		int index = inString.lastIndexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		if (index >= 0) {
			sb.append(inString.substring(pos, index));
			sb.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sb.append(inString.substring(pos));
		// remember to append any characters to the right of a match
		return sb.toString();
	}

	/**
	 * 对中文字符encodeURIComponent编码的解码。
	 * 注意由于url可能包含“％”，所有才做这个操作，而且，url同时进行了encodeURI编码
	 */
	public static String decode(String str) {
		try {
			while (str.indexOf("{") != -1 && str.indexOf("}") != -1) {
				String ss = str.substring(str.indexOf("{") + 1,
						str.indexOf("}"));
				String ass = URLDecoder.decode(
						URLDecoder.decode(ss, "UTF-8"), "UTF-8");
				str = str.replaceAll("\\{" + ss + "\\}", ass);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * get length of Chinese characters a Chinese characters as two characters
	 */
	public static int getCwordLength(String para) {
		if (para == null)
			throw new IllegalArgumentException("Strings must not be null");
		int length = 0;
		for (int i = 0; i < para.length(); i++) {
			if (Character.getType(para.charAt(i)) == 5) {
				length += 2;
			} else
				length++;
		}
		return length;
	}

	public static int getdatabaseLength(String para) {
		int length = 0;
		for (int i = 0; i < para.length(); i++) {
			if (Character.getType(para.charAt(i)) == 5) {
				length += 2;
			} else
				length++;
		}
		return length;
	}

	/**
	 * @author: Wangchao
	 * @email: super0086@qq.com
	 * @date: 2014年9月12日 下午4:20:50
	 * @Description: 描述作用
	 * @param
	 * @param str
	 * @return
	 */
	public static SpannableString SpannableTextViewString(Context context,
			List<Integer> color_resids, List<Integer> starts,
			List<Integer> ends, String str) {

        SpannableString sp = new SpannableString(str);
        for (int i = 0; i < color_resids.size(); i++) {
            int _oxcolor = context.getResources().getColor(color_resids.get(i));
            sp.setSpan(new ForegroundColorSpan(_oxcolor), starts.get(i),
                    ends.get(i), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (i == (color_resids.size() - 1)) {
                sp.setSpan(new AbsoluteSizeSpan(13, true), starts.get(i),
                        ends.get(i), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return sp;
    }



    /**
		SpannableString sp = new SpannableString(str);
		for (int i = 0; i < color_resids.size(); i++) {
			int _oxcolor = context.getResources().getColor(color_resids.get(i));
			sp.setSpan(new ForegroundColorSpan(_oxcolor), starts.get(i),
					ends.get(i), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			if (i == (color_resids.size() - 1)) {
				sp.setSpan(new AbsoluteSizeSpan(13, true), starts.get(i),
						ends.get(i), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return sp;
	}










	 /**
     * 指定格式返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 返回当前系统时间(格式以HH:mm形式)
     */
    public static String getDataTime() {
        return getDataTime("HH:mm");
    }

    /**
     * @param
     * @return
     * @Description: TODO(判断是否是合规的手机号)
     * @author 王营
     * @date 2016年2月15日 下午5:59:48
     */
    public static boolean isMobileNO(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }
        Pattern p = Pattern.compile("^((1[0-9][0-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }


    /**
     * 验证身份证
     *
     * @param
     * @return
     */
    public static boolean isIdCard(String card) {
        if ("".equals(card)) {
            return false;
        }
        Pattern p = Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X|x)$");
        Matcher m = p.matcher(card);
        return m.matches();
    }

    /**
     * @param phone
     * @return
     * @Description: TODO(手机号中间四位隐藏)
     * @author 王营
     * @date 2016年3月4日 上午11:59:30
     */
    public static String subPhoneSuffix(String phone) {
        if (phone == null || "".equals(phone)) {
            return "";
        }

        if (phone.length() > 10) {
            phone = phone.replace(phone.substring(3, 7), "****");
        }
        return phone;
    }


    public static boolean checkWrongByte(String name) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？1234567890]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(name);
        if (m.find()) {
            return true;
        }
        return false;
    }


    public static String makeUrl(String url, String... str) {


        for (String s : str) {

            url += "/" + encode(s);

        }


        LogManager.i("加载的URL=" + url);
        return url;

    }

	public static String getHasCode(String str,int length){
		if (StringUtils.isNotEmpty(str)){
			long nameNumber = str.hashCode();
			nameNumber = nameNumber * 1000 + nameNumber;
			int len = (nameNumber+"").length();
			if(len > length){
				return  str = (nameNumber+"").substring(len-length, len);
			}else {
				return "";
			}
		}else {
			return "";
		}

	}


	/**
	 * 查找img 里面的URL    System.out.println(search(str, "<img(.*?)src=\"(.*?)\""));
	 * @param str
	 * @param regex
     * @return
     */
	public static List<String> search(String str, String regex) {
		List<String> urls = new ArrayList<>();
		if (StringUtils.isEmpty(str)) {
			return urls;
		}
		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL).matcher(str);
		while (matcher.find()) {
			urls.add(matcher.group(2));
		}
		return urls;
	}

	/*
	img 标签替换
	 */
	public static String replaceStr(String str, String regex, String replace) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
		Matcher m = p.matcher(str);
		while (m.find()) {
			str = m.replaceAll(replace);
		}
		return str;
	}


}
