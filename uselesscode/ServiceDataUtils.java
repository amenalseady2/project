package com.onemena.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 筛选数据源
 * 作者：杨生辉 on 16/7/13 14:12
 */
public class ServiceDataUtils {


    public boolean isService = true;
    public int menuSelect = 0;

    static private ServiceDataUtils INSTANCE = new ServiceDataUtils();

    public ServiceDataUtils(){}

    public static synchronized  ServiceDataUtils getInstance(){
        return INSTANCE;
    }

    private JSONArray getAgeData(){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        jsonObject.put("id" , "-10");
        jsonObject.put("value" , "不限");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "1");
        jsonObject.put("value" , "0-1岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "2");
        jsonObject.put("value" , "1-2岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "3");
        jsonObject.put("value" , "2-3岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "4");
        jsonObject.put("value" , "3-4岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "5");
        jsonObject.put("value" , "4-5岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "6");
        jsonObject.put("value" , "5-6岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "7");
        jsonObject.put("value" , "6-7岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "8");
        jsonObject.put("value" , "7-8岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "9");
        jsonObject.put("value" , "8-9岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "10");
        jsonObject.put("value" , "9-10岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "11");
        jsonObject.put("value" , "10-11岁");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "12");
        jsonObject.put("value" , "11-12岁");
        array.add(jsonObject);

        return array;
    }


    /**
     * 行政区域数据
     * @return
     */
    private JSONArray getAreaData(){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        jsonObject.put("id" , "*");
        jsonObject.put("value" , "不限");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110105");
        jsonObject.put("value" , "朝阳区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110108");
        jsonObject.put("value" , "海淀区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110102");
        jsonObject.put("value" , "西城区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110101");
        jsonObject.put("value" , "东城区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110106");
        jsonObject.put("value" , "丰台区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110114");
        jsonObject.put("value" , "昌平区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110115");
        jsonObject.put("value" , "大兴区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110112");
        jsonObject.put("value" , "通州区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110107");
        jsonObject.put("value" , "石景山区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110113");
        jsonObject.put("value" , "顺义区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110111");
        jsonObject.put("value" , "房山区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110228");
        jsonObject.put("value" , "密云县");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110116");
        jsonObject.put("value" , "怀柔区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110229");
        jsonObject.put("value" , "延庆县");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110109");
        jsonObject.put("value" , "门头沟区");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "110117");
        jsonObject.put("value" , "平谷区");
        array.add(jsonObject);

        return array;
    }


    /**
     * 授课方式数据
     * @return
     */
    private JSONArray getClassData(){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        jsonObject.put("id" , "*");
        jsonObject.put("value" , "不限");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "1");
        jsonObject.put("value" , "老师上门");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "2");
        jsonObject.put("value" , "学生上门");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "3");
        jsonObject.put("value" , "双方自行协商");
        array.add(jsonObject);

        return array;
    }


    /**
     * 教龄数据
     * @return
     */
    private JSONArray getteachAgeData(){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        jsonObject.put("id" , "*");
        jsonObject.put("value" , "不限");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "1");
        jsonObject.put("value" , "1-5年");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "2");
        jsonObject.put("value" , "5-10年");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "3");
        jsonObject.put("value" , "10年以上");
        array.add(jsonObject);

        return array;
    }


    /**
     * 老师性别数据
     * @return
     */
    private JSONArray getSexData(){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        jsonObject.put("id" , "*");
        jsonObject.put("value" , "不限");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "1");
        jsonObject.put("value" , "男");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "2");
        jsonObject.put("value" , "女");
        array.add(jsonObject);

        return array;
    }


    /**
     * 排序数据
     * @return
     */
    private JSONArray getOrderData(boolean isService , int menuSelect){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        jsonObject.put("id" , "1");
        jsonObject.put("value" , "综合排序");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "2");
        jsonObject.put("value" , "销量从高到低");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "3");
        jsonObject.put("value" , "距离从近到远");
        array.add(jsonObject);

        if(!isService){//服务
            if(menuSelect == 6 || menuSelect == 7){
                jsonObject = new JSONObject();
                jsonObject.put("id" , "4");
                jsonObject.put("value" , "价格从低到高");
                array.add(jsonObject);

                jsonObject = new JSONObject();
                jsonObject.put("id" , "5");
                jsonObject.put("value" , "价格从高到低");
                array.add(jsonObject);
            }
        }

        jsonObject = new JSONObject();
        jsonObject.put("id" , "6");
        jsonObject.put("value" , "评论从多到少");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "7");
        jsonObject.put("value" , "评分从高到低");
        array.add(jsonObject);

        return array;
    }



    /**
     * 认证类型数据
     * @return
     */
    private JSONArray getCheckData(){
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        jsonObject.put("id" , "*");
        jsonObject.put("value" , "不限");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "1");
        jsonObject.put("value" , "实名认证");
        array.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("id" , "2");
        jsonObject.put("value" , "专业认证");
        array.add(jsonObject);

        return array;
    }

    /**
     * 获取年龄筛选数据
     * @return
     */
    public JSONArray getChoseAgeMenuData(){
        return getAgeData();
    }


    /**
     * 获取年龄筛选数据
     * @return
     */
    public JSONArray getChoseAreaMenuData(){
        return getAreaData();
    }


    /**
     * 获取授课方式筛选数据
     * @return
     */
    public JSONArray getChoseClassMenuData(){
        return getClassData();
    }


    /**
     * 获取教龄范围筛选数据
     * @return
     */
    public JSONArray getChoseTeachAgeMenuData(){
        return getteachAgeData();
    }


    /**
     * 获取教龄范围筛选数据
     * @return
     */
    public JSONArray getChoseSexAgeMenuData(){
        return getSexData();
    }

    /**
     * 获取教龄范围筛选数据
     * @return
     */
    public JSONArray getOrderMenuData(){

        return getOrderData(isService , menuSelect);
    }


    /**
     * 获取认证类型筛选数据
     * @return
     */
    public JSONArray getChoseCheckMenuData(){
        return getCheckData();
    }

}
