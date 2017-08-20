package com.mysada.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysada.news.R;
import com.mysada.news.adapter.InterestGridViewAdapter;
import com.mysada.news.app.config.SPKey;
import com.mysada.news.listener.OnActionViewClickListener;
import com.onemena.utils.SpUtil;
import com.onemena.widght.ContentBondTextView;

public class InterestChoseActivity extends BaseActicity implements OnActionViewClickListener {

    private GridView gridview;
    private InterestGridViewAdapter adapter;
    private int[] man_defualt = {R.mipmap.saudi, R.mipmap.uae, R.mipmap.egypt,
            R.mipmap.middle_east, R.mipmap.international, R.mipmap.finance,
            R.mipmap.football, R.mipmap.it, R.mipmap.car,
            R.mipmap.game, R.mipmap.chemistry, R.mipmap.health,
            R.mipmap.travel, R.mipmap.food, R.mipmap.entertainment,
            R.mipmap.superstar, R.mipmap.goddess};
    private int[] man_select = {R.mipmap.saudi_s, R.mipmap.uae_s, R.mipmap.egypt_s,
            R.mipmap.middle_east_s, R.mipmap.international_s, R.mipmap.finance_s,
            R.mipmap.football_s, R.mipmap.it_s, R.mipmap.car_s,
            R.mipmap.game_s, R.mipmap.chemistry_s, R.mipmap.health_s,
            R.mipmap.travel_s, R.mipmap.food_s, R.mipmap.entertainment_s,
            R.mipmap.superstar_s, R.mipmap.goddess_s};

    private int[] man_id={17,13,5,
                          1,2,3,
                           4,6,10,
                           12,11,16,
                           14,15,7,
                            8,9};

    private int[] woman_defualt = {R.mipmap.saudi, R.mipmap.uae, R.mipmap.egypt,
            R.mipmap.entertainment, R.mipmap.goddess, R.mipmap.superstar,
            R.mipmap.food, R.mipmap.travel, R.mipmap.health,
            R.mipmap.chemistry, R.mipmap.it, R.mipmap.game,
            R.mipmap.football, R.mipmap.car, R.mipmap.middle_east,
            R.mipmap.international, R.mipmap.finance};
    private int[] woman_select = {R.mipmap.saudi_s, R.mipmap.uae_s, R.mipmap.egypt_s,
            R.mipmap.entertainment_s, R.mipmap.goddess_s, R.mipmap.superstar_s,
            R.mipmap.food_s, R.mipmap.travel_s, R.mipmap.health_s,
            R.mipmap.chemistry_s, R.mipmap.it_s, R.mipmap.game_s,
            R.mipmap.football_s, R.mipmap.car_s, R.mipmap.middle_east_s,
            R.mipmap.international_s, R.mipmap.finance_s
    };
    private int[] woman_id= {17, 13, 5,
                 7,9,8,
                 15,14,16,
                 11,6,12,
                 4,10,1,
                 2,3
    };

    private String[] manString = {"السعودية", "الإمارات", "مصر", "عربي", "دولي", "إقتصاد", "كرة قدم",
            "تكنولوجيا", "سيارات", "ألعاب", "علوم", "صحة",
            "سياحة", "طبخ", "ترفيه", "مشاهير", "حواء"
    };
    private String[] womanString = {"السعودية", "الإمارات", "مصر",
            "ترفيه", "حواء", "مشاهير",
            "طبخ", "سياحة", "صحة",
            "علوم", "تكنولوجيا", "ألعاب",
            "كرة قدم", "سيارات", "عربي",
            "دولي", "إقتصاد"
    };

//    arab("中东", "1"),
//    world("国际", "2"),
//    food("食物", "15"),
//    football("足球","4"),
//    science("科学","11"),
//    game("游戏","12"),
//    health("健康","16"),
//    it("it","6"),
//    economic("经济","3"),
//    entertainment("娱乐","7"),
//    celebrities("明星","8"),
//    cars("cars","10"),
//    feminine("女神","9"),
//    saudi("沙特","17"),
//    egypt("埃及","5"),
//    uae("阿联酋","13"),
//    travel("旅游","14");

    private ImageView iv_male, iv_female;
    private boolean isMan = true;
    private JSONArray man_jsonarray;
    private JSONArray woman_jsonarray;
    private ContentBondTextView ok_tv;
    private JSONArray resultArray;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, InterestChoseActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        gridview = (GridView) findViewById(R.id.gridview);

        ok_tv = (ContentBondTextView) findViewById(R.id.ok_tv);
        iv_male = (ImageView) findViewById(R.id.iv_male);
        iv_female = (ImageView) findViewById(R.id.iv_female);
        iv_male.setOnClickListener(this);
        iv_female.setOnClickListener(this);
        ok_tv.setOnClickListener(this);

        ok_tv.setClickable(false);

        adapter = new InterestGridViewAdapter(this);
        adapter.setOnActionViewClickListener(this);
        gridview.setAdapter(adapter);

        man_jsonarray = ints2Array(man_defualt, man_select,man_id, manString);
        woman_jsonarray = ints2Array(woman_defualt, woman_select,woman_id, womanString);
        iv_male.setImageResource(R.mipmap.male_s);
        adapter.notifyDataSetChanged(0, man_jsonarray);
    }

    //数组转换成jsonArray
    public JSONArray ints2Array(int[] def, int[] select,int[] ids ,String[] ss) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < def.length; i++) {
            JSONObject object = new JSONObject();
            object.put("imgdef", def[i]);
            object.put("imgsel", select[i]);
            object.put("name", ss[i]);
            object.put("select", "0");
            object.put("category_id", ids[i]);
            array.add(object);
        }
        return array;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv:
                Integer tag = (Integer) v.getTag();
                if (isMan) {
                    String select = man_jsonarray.getJSONObject(tag).getString("select");
                    if ("0".equals(select)) {
                        man_jsonarray.getJSONObject(tag).put("select", "1");
                    } else {
                        man_jsonarray.getJSONObject(tag).put("select", "0");
                    }
                    adapter.notifyDataSetChanged(0, man_jsonarray);
                } else {
                    String select = woman_jsonarray.getJSONObject(tag).getString("select");
                    if ("0".equals(select)) {
                        woman_jsonarray.getJSONObject(tag).put("select", "1");
                    } else {
                        woman_jsonarray.getJSONObject(tag).put("select", "0");
                    }
                    adapter.notifyDataSetChanged(0, woman_jsonarray);
                }
                changeBottomBtn();
                break;

            case R.id.iv_male://切换到男拼
                isMan = true;
                iv_male.setImageResource(R.mipmap.male_s);
                iv_female.setImageResource(R.mipmap.female);
                man_jsonarray = ints2Array(man_defualt, man_select,man_id, manString);
                woman_jsonarray = ints2Array(woman_defualt, woman_select,woman_id, womanString);
                adapter.notifyDataSetChanged(0, man_jsonarray);
                changeBottomBtn();
                break;
            case R.id.iv_female://切换到女拼
                isMan = false;
                iv_male.setImageResource(R.mipmap.male);
                iv_female.setImageResource(R.mipmap.female_s);
                man_jsonarray = ints2Array(man_defualt, man_select,man_id, manString);
                woman_jsonarray = ints2Array(woman_defualt, woman_select, woman_id,womanString);
                adapter.notifyDataSetChanged(0, woman_jsonarray);
                changeBottomBtn();
                break;
            case R.id.ok_tv:
                if (isMan) {
                    resultArray = getResult(man_jsonarray);
                } else {
                    resultArray =  getResult(woman_jsonarray);
                }
                SpUtil.saveValue(SPKey.TITLEARRAY,resultArray.toJSONString());
                SpUtil.saveValue(SPKey.ISMAN,isMan);
                MainActivity.startActivity(InterestChoseActivity.this);
                finish();
                break;
        }

    }

    private JSONArray getResult(JSONArray array) {
        JSONArray arrayBef = array;
        JSONArray arrayReslut = new JSONArray();
        for (int i = 3; i < arrayBef.size(); i++) {
            JSONObject object = arrayBef.getJSONObject(i);
            if (object.getString("select").equals("1")) {
                //用户选中的栏目
                arrayReslut.add(object);
                arrayBef.remove(i);
            }
        }
        //用户选中的国家
        int selectCountryNum = 0;
        for (int i = 0; i < 3; i++) {
            JSONObject object = arrayBef.getJSONObject(i);

            if (object.getString("select").equals("1")) {
                selectCountryNum += 1;
                arrayReslut.add(object);
                arrayBef.remove(i);
            }
        }
        //如果一个国家没有选择，默认加入第一个国家
        if (selectCountryNum == 0) {
            selectCountryNum += 1;
            arrayReslut.add(arrayBef.getJSONObject(0));
            arrayBef.remove(0);
        }
        //加入用户未选择的栏目
        for (int i = (3 - selectCountryNum); i < arrayBef.size(); i++) {
            JSONObject object = arrayBef.getJSONObject(i);
            arrayReslut.add(object);
            arrayBef.remove(i);
        }
        //加入未选择的国家
        arrayReslut.addAll(arrayBef);
        return arrayReslut;
    }

    //改变底部按钮
    private void changeBottomBtn() {
        boolean isslelectd;
        if (isMan) {
            isslelectd = isSlelectd(man_jsonarray);
        } else {
            isslelectd = isSlelectd(woman_jsonarray);
        }
        if (isslelectd) {
            ok_tv.setBackgroundResource(R.drawable.interest_blue_btn_bg);
            ok_tv.setClickable(true);
            ok_tv.setTextColor(Color.WHITE);
        } else {
            ok_tv.setBackgroundResource(R.drawable.interest_gry_btn_bg);
            ok_tv.setClickable(false);
            ok_tv.setTextColor(getResources().getColor(R.color.textcolor_c4c6d1));
        }
    }

    private boolean isSlelectd(JSONArray array) {
        for (int i = 0; i < array.size(); i++) {
            String select = array.getJSONObject(i).getString("select");
            if ("1".equals(select)) {
                return true;
            }
        }
        return false;
    }
}
