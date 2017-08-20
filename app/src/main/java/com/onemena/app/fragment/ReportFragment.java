package com.onemena.app.fragment;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.activity.BrowerOpenNewsDetailActivity;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseFragment;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.onemena.utils.SpUtil;
import com.onemena.utils.ToastUtil;
import com.onemena.utils.TongJiUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by WHF on 2016-11-24.
 */

public class ReportFragment extends BaseFragment implements View.OnClickListener {

    public static final String ID="id";
    public static final String CATEGORY_ID="category_id";
    public static final String TYPE="type";

    private ImageView back_iv;
    private TextView tv_report;
    private String type;
    private TextView tv_title_report;
    private String mId;
    private String category_id;
    private EditText et_report;
    private CheckBox mCheckbox1, mCheckbox2, mCheckbox3, mCheckbox4, mCheckbox5, mCheckbox6, mCheckbox7, mCheckbox8;

    ArrayList<CheckBox> checkBoxes=new ArrayList<CheckBox>();
    ArrayList<LinearLayout> lls=new ArrayList<LinearLayout>();
    private LinearLayout ll_checkbox1, ll_checkbox2, ll_checkbox3, ll_checkbox4, ll_checkbox5, ll_checkbox6, ll_checkbox7, ll_checkbox8;

    public static ReportFragment getInstance(){
        return new ReportFragment();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=null;
        if (SpUtil.getBoolean(SPKey.MODE,false)){
            view = inflater.inflate(R.layout.fragment_report_night, container, false);
        }else {
            view = inflater.inflate(R.layout.fragment_report, container, false);
        }
        return view;
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        mId = arguments.getString(ID);
        category_id = arguments.getString(CATEGORY_ID);
        type = arguments.getString(TYPE);


        back_iv = (ImageView) view.findViewById(R.id.back_iv);
        tv_report = (TextView) view.findViewById(R.id.tv_report);
        tv_title_report = (TextView) view.findViewById(R.id.title_report);
        et_report = (EditText) view.findViewById(R.id.et_report);

        ll_checkbox1 = (LinearLayout) view.findViewById(R.id.ll_checkbox1);
        ll_checkbox2 = (LinearLayout) view.findViewById(R.id.ll_checkbox2);
        ll_checkbox3 = (LinearLayout) view.findViewById(R.id.ll_checkbox3);
        ll_checkbox4 = (LinearLayout) view.findViewById(R.id.ll_checkbox4);
        ll_checkbox5 = (LinearLayout) view.findViewById(R.id.ll_checkbox5);
        ll_checkbox6 = (LinearLayout) view.findViewById(R.id.ll_checkbox6);
        ll_checkbox7 = (LinearLayout) view.findViewById(R.id.ll_checkbox7);
        ll_checkbox8 = (LinearLayout) view.findViewById(R.id.ll_checkbox8);

        lls.add(ll_checkbox1);
        lls.add(ll_checkbox2);
        lls.add(ll_checkbox3);
        lls.add(ll_checkbox4);
        lls.add(ll_checkbox5);
        lls.add(ll_checkbox6);
        lls.add(ll_checkbox7);
        lls.add(ll_checkbox8);
        for (int i=0;i<lls.size();i++){
            LinearLayout ll = lls.get(i);
            ll.setTag(i);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    CheckBox checkBox = checkBoxes.get(tag);
                    checkBox.setChecked(!checkBox.isChecked());
                }
            });
        }

        mCheckbox1 = (CheckBox) view.findViewById(R.id.checkbox1);
        mCheckbox2 = (CheckBox) view.findViewById(R.id.checkbox2);
        mCheckbox3 = (CheckBox) view.findViewById(R.id.checkbox3);
        mCheckbox4 = (CheckBox) view.findViewById(R.id.checkbox4);
        mCheckbox5 = (CheckBox) view.findViewById(R.id.checkbox5);
        mCheckbox6 = (CheckBox) view.findViewById(R.id.checkbox6);
        mCheckbox7 = (CheckBox) view.findViewById(R.id.checkbox7);
        mCheckbox8 = (CheckBox) view.findViewById(R.id.checkbox8);
        checkBoxes.add(mCheckbox1);
        checkBoxes.add(mCheckbox2);
        checkBoxes.add(mCheckbox3);
        checkBoxes.add(mCheckbox4);
        checkBoxes.add(mCheckbox5);
        checkBoxes.add(mCheckbox6);
        checkBoxes.add(mCheckbox7);
        checkBoxes.add(mCheckbox8);
        //只要有一个box被选中，或者et有文字最下边的按钮就能点击
        for (CheckBox box:checkBoxes){
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        setReportTv(true);

                    }else {
                        boolean isUseable=false;
                        for (CheckBox box:checkBoxes){
                            boolean checked = box.isChecked();
                            if (checked){
                                isUseable=true;
                                break;
                            }
                        }
                        if (isUseable){
                            setReportTv(true);
                        }else {
                            //判断et是否有文字。有的话，就能点击
                            if (et_report.getText().length()>0){
                                setReportTv(true);
                            }else {
                                setReportTv(false);
                            }
                        }

                    }
                }



            });
        }

        AssetManager mgr=mContext.getAssets();//得到AssetManager
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/HelveticaNeueLTArabic-Roman.ttf");//根据路径得到Typeface
        et_report.setTypeface(tf);//设置字体



        back_iv.setOnClickListener(this);
        tv_report.setOnClickListener(this);
        setReportTv(false);
        et_report.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    setReportTv(true);
                }else {
                    //判断cheakboxs中是否有被选中的
                    boolean isUseable=false;
                    for (CheckBox box:checkBoxes){
                        boolean checked = box.isChecked();
                        if (checked){
                            isUseable=true;
                            break;
                        }
                    }
                    if (isUseable){
                        setReportTv(true);
                    }else {
                        setReportTv(false);
                    }

                }
            }
        });
    }

    private void setReportTv(boolean b) {
        tv_report.setClickable(b);
        if (!b){
            //不可以点击
            if (SpUtil.getBoolean(SPKey.MODE,false)){
                //夜间模式
                tv_report.setBackgroundColor(Color.parseColor("#3f2d2d"));
                tv_report.setTextColor(Color.parseColor("#7f252525"));

            }else {
                tv_report.setBackgroundColor(Color.parseColor("#E7E7E7"));
            }

        }else {
            //可以点击
            if (SpUtil.getBoolean(SPKey.MODE,false)){
                //夜间模式
                tv_report.setBackgroundResource(R.drawable.report_red_btn_selector_night);
                tv_report.setTextColor(Color.parseColor("#252525"));
            }else {
                tv_report.setBackgroundResource(R.drawable.report_red_btn_selector);
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_iv:
                if (getActivity() instanceof MainActivity) {
                    popBackStack();
                } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
                    int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
                    if (backStackEntryCount >= 1) {
                        popBackStack();
                    } else {
                        getActivity().finish();
                    }
                }
                break;
            case R.id.tv_report:
                tongjiType(type);
                sendDataToNet(mId,et_report.getText().toString());
                ToastUtil.showNormalShortToast("تم إرسال التبليغ");//提交成功
                popBackStack();
                break;


        }
    }

    //统计方法
    private void tongjiType(String type) {
        switch (type){
            case "1":
                TongJiUtil.getInstance().putEntries(TJKey.NEWS_REPORT_SUBMIT,
                        MyEntry.getIns(TJKey.CATEGORY_ID,category_id),
                        MyEntry.getIns(TJKey.RESOURCE_ID,mId));
                break;
            case "2":
                TongJiUtil.getInstance().putEntries(TJKey.NEWS_REPORT_SUBMIT_SETTING,
                        MyEntry.getIns(TJKey.CATEGORY_ID,category_id),
                        MyEntry.getIns(TJKey.RESOURCE_ID,mId));
                break;

        }
    }

    //提交数据到服务器
    private void sendDataToNet(String mId,String des) {
        HashMap<String,String> map=new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0;i<checkBoxes.size();i++){
            CheckBox checkBox = checkBoxes.get(i);
            if (checkBox.isChecked()){
                stringBuffer.append("op"+(i+1)+",");
            }
        }

//        map.put("post_id",mId);
        map.put("report",stringBuffer.substring(0,stringBuffer.length()));
        map.put("description",des);



        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_REPORT+mId, map, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {

            }

            @Override
            public void onObjError() {

            }
        });


    }


}
