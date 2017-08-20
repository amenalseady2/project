package com.tabindictor.viewpagertabindictor;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/11.
 */

public class TabIndictorView extends LinearLayout {
    private Activity mContext;
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private LinearLayout mRadioGroup_content;
    private LinearLayout ll_more_columns;
    private RelativeLayout rl_column;
    private ImageView button_more_columns;
    private ImageView shade_left;
    private ImageView shade_right;
    public ClickMoreListener moreListener;
    private ViewPager mViewPager;
    private List<String> mTbs=new ArrayList<>();
    private int mScreenWidth;
    private int columnSelectIndex=0;
    private int mItemWidth;

    /**
     *  ViewPager切换监听方法
     * */
    public ViewPager.OnPageChangeListener pageListener= new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };


    public TabIndictorView(Context context) {
        this(context,null);
    }

    public TabIndictorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TabIndictorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = (Activity) context;
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.columnhorizontal_scrollview, null);
        mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)view.findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) view.findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) view.findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) view.findViewById(R.id.rl_column);
        button_more_columns = (ImageView) view.findViewById(R.id.button_more_columns);
        shade_left = (ImageView) view.findViewById(R.id.shade_left);
        shade_right = (ImageView) view.findViewById(R.id.shade_right);
        button_more_columns.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (moreListener!=null){
                    moreListener.clickMore();
                }
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth=dm.widthPixels;
        mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7

        initTabColumn();
        mColumnHorizontalScrollView.setHorizontalFadingEdgeEnabled(false);
        addView(view);
    }

    public void setViewPagerAndData(ViewPager viewpager, List<String> tabs){

        mViewPager = viewpager;
        mTbs = tabs;
        mViewPager.setOnPageChangeListener(pageListener);
        initTabColumn();
    }
    public void setViewPagerAndData(ViewPager viewpager, List<String> tabs,int initTab){

        mViewPager = viewpager;
        mTbs = tabs;
        columnSelectIndex=initTab;
        mViewPager.setOnPageChangeListener(pageListener);
        initTabColumn();
        selectTab(columnSelectIndex);
        mColumnHorizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                mColumnHorizontalScrollView.smoothScrollBy(mItemWidth*columnSelectIndex,0);
            }
        });

    }
       /**
     *  初始化Column栏目项
     * */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count =  mTbs.size();
        mColumnHorizontalScrollView.setParam(mContext, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column,false);
        for(int i = 0; i< count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.MATCH_PARENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
//			View view =  View.inflate(mContext,R.layout.column_item, null);
            TextView columnTextView =new TextView(mContext);
            columnTextView.setTextAppearance(mContext, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
//            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg2);
            columnTextView.setGravity(Gravity.CENTER);



            int dimensionLR = (int) getResources().getDimension(R.dimen.textview_padding_lr);
            int dimensionTB = (int) getResources().getDimension(R.dimen.textview_padding_tb);
            columnTextView.setPadding(dimensionLR ,dimensionTB, dimensionLR,dimensionTB);

            columnTextView.setId(i);

            //设置字体
            AssetManager mgr=mContext.getAssets();//得到AssetManager
            Typeface tf=Typeface.createFromAsset(mgr, "fonts/HelveticaNeueLTArabic-Roman.ttf");//根据路径得到Typeface
            columnTextView.setTypeface(tf);//设置字体

            columnTextView.setText(mTbs.get(i));
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if(columnSelectIndex == i){
                columnTextView.setSelected(true);

            }else {
                columnTextView.setSelected(false);
            }
            setTextBackgroundAndSize(columnTextView);
            columnTextView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
                        TextView localView = (TextView) mRadioGroup_content.getChildAt(i);
                        if (localView != v) {
                            localView.setSelected(false);
                        }
                        else{
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                        setTextBackgroundAndSize(localView);
                    }
                }
            });

            mRadioGroup_content.addView(columnTextView, i ,params);
        }

        View initView = mRadioGroup_content.getChildAt(columnSelectIndex);
        if (initView!=null) {
            initView.setSelected(true);
            mViewPager.setCurrentItem(columnSelectIndex);
        }
    }

    private void setTextBackgroundAndSize(TextView textView) {

//        Paint  paint =new Paint();
//        paint.setTextSize(textView.getTextSize());
//        float size =paint.measureText(textView.getText().toString());
        if (textView.isSelected()){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
            textView.setBackgroundResource(R.drawable.tab_bg_bmp);
//            if (size>60){
//                textView.setBackgroundResource(R.drawable.tab_bg_bmp);
//            }else {
//                textView.setBackgroundResource(R.mipmap.tab_background);
//            }
        }else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            textView.setBackgroundResource(android.R.color.transparent);
        }

    }

    /**
     *  选择的Column里面的Tab
     * */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }
        //判断是否选中
        for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
            TextView checkView = (TextView) mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
            setTextBackgroundAndSize(checkView);
        }
    }

    public void nightMode(){
        for (int i=0;i<mRadioGroup_content.getChildCount();i++){
            TextView tv = (TextView) mRadioGroup_content.getChildAt(i);
            tv.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_night));
        }
    }
    public void dayMode(){
        for (int i=0;i<mRadioGroup_content.getChildCount();i++){
            TextView tv = (TextView) mRadioGroup_content.getChildAt(i);
            tv.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
        }
    }

    public interface ClickMoreListener{
        public void clickMore();
    }
    public void setOnClickMoreListener(ClickMoreListener clickMorelistener){
        moreListener=clickMorelistener;
    }
}
