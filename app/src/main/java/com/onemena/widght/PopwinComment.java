package com.onemena.widght;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.data.UserManager;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.POPWindow;
import com.onemena.data.eventbus.UserLogin;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.TaskUtil;
import com.onemena.utils.ToastUtil;
import com.onemena.utils.TongJiUtil;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/11/24.
 */

public class PopwinComment extends PopupWindow implements View.OnClickListener {


    private Activity mContext;
    private final View popView;
    private EditText et_comment;
    private PopListener mPopListener;
    private HelveRomanTextView comment_ok;
    private TextView num_limit;
    private String category_id;
    private String news_id;
    private int type;
    private NotoRegularTextView tv_user_name;
    private CommentListener listener;
    private String content;
    private JSONObject jsonObject;
    private String user_token;
    private String login_from;
    private JSONObject userinfo;

    public PopwinComment(Activity context) {

        mContext = context;
        popView = LayoutInflater.from(mContext).inflate(R.layout.newsdetail_comment_pop, null);
        initPopWindow();
        setContentView(popView);
    }

    public PopwinComment(Activity context, String category_id, String news_id, int type, CommentListener listener) {
        this.listener = listener;
        mContext = context;
        this.category_id = category_id;
        this.type = type;
        this.news_id = news_id;

        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            popView = LayoutInflater.from(mContext).inflate(R.layout.newsdetail_comment_pop_night, null);
        } else {
            popView = LayoutInflater.from(mContext).inflate(R.layout.newsdetail_comment_pop, null);
        }

        initPopWindow();
        setContentView(popView);
    }

    /**
     * 初始化弹出的pop
     */
    private void initPopWindow() {
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(new ColorDrawable(0));
        //被键盘顶起的两行代码
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popwindow出现和消失动画
        setAnimationStyle(R.style.PopMenuAnimation);


        //获取popwindow焦点
        setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        setOutsideTouchable(true);
        update();
        et_comment = (EditText) popView.findViewById(R.id.et_comment);
        comment_ok = (HelveRomanTextView) popView.findViewById(R.id.comment_ok);
        num_limit = (TextView) popView.findViewById(R.id.num_limit);
        comment_ok.setOnClickListener(this);
        comment_ok.setClickable(false);

        tv_user_name = (NotoRegularTextView) popView.findViewById(R.id.tv_user_name);
        String name = "زائر";
//        try {
//            jsonObject = JSONObject.parseObject(UserManager.getUserObj().getString("userinfo"));
//            login_from = jsonObject.getString("login_from");
//            name=jsonObject.getString("user_name");
//            if ("localhost".equals(login_from)) {
//                name="زائر"+ StringUtils.getHasCode(name,6);
//            }
////            tv_user_name.setText("زائر"+ StringUtils.getHasCode(name,6));
//        }catch (Exception e){}

        String user = UserManager.getUserObj().getString("userinfo");
        if (StringUtils.isNotEmpty(user)) {
            userinfo = JSONObject.parseObject(user);
            name = userinfo.getString("user_name");
            login_from = userinfo.getString("login_from");
            if ("localhost".equals(login_from)) {
                name = "زائر" + name;
            }
        } else {
            EventBus.getDefault().post(new UserLogin(true));
            name = "زائر";
        }

        tv_user_name.setText(name);


        final AssetManager mgr = mContext.getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/HelveticaNeueLTArabic-Roman.ttf");//根据路径得到Typeface
        et_comment.setTypeface(tf);//设置字体


        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                num_limit.setText(StringUtils.int2IndiaNum(s.length() + ""));
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    if (s.length() > 0) {
                        comment_ok.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.common_e8_btn_selector_night));

                        comment_ok.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
                        comment_ok.setClickable(true);
                    } else {
                        comment_ok.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.common_main_blue_btn_selector_night));
                        comment_ok.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
                        comment_ok.setClickable(false);
                    }
                } else {

                    if (s.length() > 0) {
                        comment_ok.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.common_main_blue_btn_selector));
                        comment_ok.setTextColor(Color.WHITE);
                        comment_ok.setClickable(true);
                    } else {
                        comment_ok.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.common_e8_btn_selector));
                        comment_ok.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
                        comment_ok.setClickable(false);
                    }
                }


            }
        });

    }

    public void show(View parent) {

        //设置popwindow显示位置
        showAtLocation(parent, Gravity.CENTER | Gravity.BOTTOM, 0, 0);

        if (mPopListener != null) {
            mPopListener.showFinished(et_comment);
        }

        EventBus.getDefault().post(new POPWindow(true));
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new POPWindow(false));
            }
        });

//        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
//        lp.alpha = 0.5f; //0.0-1.0
//        lp.gravity = Gravity.CENTER_HORIZONTAL;
//        mContext.getWindow().setAttributes(lp);
//        setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                mContext.getWindow().setAttributes(lp);
//                et_comment.setText("");
//            }
//        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_like:

                break;
            case R.id.comment_ok:
//                ToastUtil.showNormalShortToast("提交评论");
                commitComment(et_comment.getText() + "", news_id);
                String docType="";
                if (type==1) {
                    docType="video";
                } else {
                    docType="news";
                }
                TongJiUtil.getInstance().putEntries(TJKey.COMMENT_SUBMIT,
                        MyEntry.getIns(TJKey.CATEGORY_ID,category_id),
                        MyEntry.getIns(TJKey.TYPE, type+""),
                        MyEntry.getIns(TJKey.RESOURCE_ID, news_id));


                TongJiUtil.getInstance().putStatistics(
                        MyEntry.getIns(TJKey.DOCTYPE, docType),
                        MyEntry.getIns(TJKey.ACTYPE, "comment"),
                        MyEntry.getIns(TJKey.DOCID, news_id));
//                if (type.equals("news_comment_click")){
//                    listener.comListener();
//                }
//                listener.comListener();
                et_comment.setText("");
                dismiss();
                break;
        }
    }

    public void setPopListener(PopListener popListener) {

        mPopListener = popListener;
    }

    public interface PopListener {
        public void showFinished(EditText et);
    }

    /**
     * 评论内容提交
     *
     * @param des
     */
    private void commitComment(String des, String news_id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("comment", des);
        hashMap.put("post_id", news_id);
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_ADD_COMMENT_DU, hashMap, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                listener.comListener();
                JSONObject extra = obj.getJSONObject("extra");
                String alert_message = extra.getString("alert_message");
                if (StringUtils.isNotEmpty(alert_message)) {
                    TongJiUtil.getInstance().putEntries(TJKey.TASK_COM,MyEntry.getIns(TJKey.TASK_ID,extra.getString("task_id")));
                    TaskUtil.showTaskFinish(mContext,alert_message);
                }
                ToastUtil.showNormalShortToast("تم النشر ");//成功
            }

            @Override
            public void onObjError() {
                ToastUtil.showNormalShortToast("فشل النشر");//失败
            }
        });
    }


    public interface CommentListener {
        void comListener();
    }

}
