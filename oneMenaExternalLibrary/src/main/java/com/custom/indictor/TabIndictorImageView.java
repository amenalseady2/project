package com.custom.indictor;

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

import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.jess.arms.R;

import java.util.HashMap;


/**
 * Created by Administrator on 2016/11/11.
 */

public class TabIndictorImageView extends LinearLayout {
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
    private JSONArray mTbs = new JSONArray();
    private int mScreenWidth;
    private int columnSelectIndex = 0;
    private int mItemWidth;
    private HashMap<String, Integer> hashMap = new HashMap<>();
    private int[] imageIds = {
            R.drawable.column_uae_s,
            R.drawable.column_health_s,
            R.drawable.column_food_s,
            R.drawable.column_travel_s,
            R.drawable.column_game_s,
            R.drawable.column_technology_s,
            R.drawable.column_car_s,
            R.drawable.column_saudi_s,
            R.drawable.column_egypt_s,
            R.drawable.column_female_s,
            R.drawable.column_superstar_s,
            R.drawable.column_entertainment_s,
            R.drawable.column_it_s,
            R.drawable.column_football_s,
            R.drawable.column_finance_s,
            R.drawable.column_international_s,
            R.drawable.column_middle_east_s,
            R.drawable.column_recommend_s,
            R.drawable.column_boutique
    };
//1, 2, 3, 4, 6, 7, 8, 9, 5, 17, 10, 11, 12, 14, 15, 16, 13
    private void setHashMap() {
        hashMap.put("-1", R.drawable.column_boutique);
        hashMap.put("0", R.drawable.column_recommend_s);
        hashMap.put("1", R.drawable.column_middle_east_s);
        hashMap.put("2", R.drawable.column_international_s);
        hashMap.put("3", R.drawable.column_finance_s);
        hashMap.put("4", R.drawable.column_football_s);
        hashMap.put("6", R.drawable.column_it_s);
        hashMap.put("7", R.drawable.column_entertainment_s);
        hashMap.put("8", R.drawable.column_superstar_s);
        hashMap.put("9", R.drawable.column_female_s);
        hashMap.put("5", R.drawable.column_egypt_s);
        hashMap.put("17", R.drawable.column_saudi_s);
        hashMap.put("10", R.drawable.column_car_s);
        hashMap.put("11", R.drawable.column_technology_s);
        hashMap.put("12", R.drawable.column_game_s);
        hashMap.put("14", R.drawable.column_travel_s);
        hashMap.put("15", R.drawable.column_food_s);
        hashMap.put("16", R.drawable.column_health_s);
        hashMap.put("13", R.drawable.column_uae_s);
    }

    private int[] imageIdsNight = {
            R.drawable.column_uae_s,
            R.drawable.column_health_s,
            R.drawable.column_food_s,
            R.drawable.column_travel_s,
            R.drawable.column_game_s,
            R.drawable.column_technology_s,
            R.drawable.column_car_s,
            R.drawable.column_saudi_s,
            R.drawable.column_egypt_s,
            R.drawable.column_female_s,
            R.drawable.column_superstar_s,
            R.drawable.column_entertainment_s,
            R.drawable.column_it_s,
            R.drawable.column_football_s,
            R.drawable.column_finance_s,
            R.drawable.column_international_s,
            R.drawable.column_middle_east_s,
            R.drawable.column_recommend_s,
            R.drawable.column_boutique
    };

    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

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


    public TabIndictorImageView(Context context) {
        this(context, null);
    }

    public TabIndictorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabIndictorImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = (Activity) context;
        setHashMap();
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.columnhorizontal_scrollview_image, null);
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view.findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) view.findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) view.findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) view.findViewById(R.id.rl_column);
        button_more_columns = (ImageView) view.findViewById(R.id.button_more_columns);
        shade_left = (ImageView) view.findViewById(R.id.shade_left);
        shade_right = (ImageView) view.findViewById(R.id.shade_right);
        button_more_columns.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (moreListener != null) {
                    moreListener.clickMore();
                }
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mItemWidth = mScreenWidth / 5;// 一个Item宽度为屏幕的1/7

        initTabColumn();
        mColumnHorizontalScrollView.setHorizontalFadingEdgeEnabled(false);
        addView(view);
    }

    public void setViewPagerAndData(ViewPager viewpager, JSONArray tabs) {

        mViewPager = viewpager;
        mTbs = tabs;
        mViewPager.setOnPageChangeListener(pageListener);
        initTabColumn();
    }

    public void setViewPagerAndData(ViewPager viewpager, JSONArray tabs, int initTab) {

        mViewPager = viewpager;
        mTbs = tabs;
        columnSelectIndex = initTab;
        mViewPager.setOnPageChangeListener(pageListener);
        initTabColumn();
        selectTab(columnSelectIndex);
        mColumnHorizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                mColumnHorizontalScrollView.smoothScrollBy(mItemWidth * columnSelectIndex, 0);
            }
        });


    }

    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = mTbs.size();
        mColumnHorizontalScrollView.setParam(mContext, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column, false);
        for (int i = 0; i < count; i++) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(VERTICAL);
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
//			View view =  View.inflate(mContext,R.layout.column_item, null);
            TextView columnTextView = new TextView(mContext);
            columnTextView.setTextAppearance(mContext, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
//            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg2);
            columnTextView.setGravity(Gravity.CENTER_HORIZONTAL);


            int dimensionLR = (int) getResources().getDimension(R.dimen.imageview_padding_lr);
            int dimensionTB = (int) getResources().getDimension(R.dimen.textview_padding_bt);
            columnTextView.setPadding(dimensionLR, -dimensionTB, dimensionLR, 0);

            columnTextView.setId(i);

            //设置字体
            AssetManager mgr = mContext.getAssets();//得到AssetManager
            Typeface tf = Typeface.createFromAsset(mgr, "fonts/HelveticaNeueLTArabic-Roman.ttf");//根据路径得到Typeface
            columnTextView.setTypeface(tf);//设置字体

            String title = mTbs.getJSONObject(i).getString("title");
            if (title == null) {
                title = mTbs.getJSONObject(i).getString("name");
            }

            columnTextView.setText(title);
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));

            columnTextView.setLayoutParams(params);
            ImageView imageView = new ImageView(mContext);
            int imageHight = (int) getResources().getDimension(R.dimen.title_image_hight);
            LayoutParams imageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, imageHight);
            imageParams.leftMargin = dimensionLR;
            imageParams.rightMargin = dimensionLR;
            imageParams.topMargin = dimensionTB;
            imageView.setLayoutParams(imageParams);
            imageView.setPadding(dimensionLR, 0, dimensionLR, -dimensionTB);
            Integer id = hashMap.get(mTbs.getJSONObject(i).getString("id"));
            if (id != null) {
                imageView.setImageResource(id);
            } else {
                Glide.with(mContext)
                        .load(mTbs.getJSONObject(i).getString("icon"))
                        .into(imageView);
            }

            linearLayout.addView(imageView);
            linearLayout.addView(columnTextView);

            if (columnSelectIndex == i) {
                linearLayout.setSelected(true);

            } else {
                linearLayout.setSelected(false);
            }
            setTextBackgroundAndSize(linearLayout);
            linearLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        LinearLayout localView = (LinearLayout) mRadioGroup_content.getChildAt(i);
                        if (localView != v) {
                            localView.setSelected(false);
                        } else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                        setTextBackgroundAndSize(localView);
                    }
                }
            });

            mRadioGroup_content.addView(linearLayout, i);
        }

        View initView = mRadioGroup_content.getChildAt(columnSelectIndex);
        if (initView != null) {
            initView.setSelected(true);
            mViewPager.setCurrentItem(columnSelectIndex);
        }
    }

    private void setTextBackgroundAndSize(LinearLayout linearLayout) {

        if (linearLayout.isSelected()) {
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View childAt = linearLayout.getChildAt(i);
                if (childAt instanceof TextView) {
                    ((TextView) childAt).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
                if (childAt instanceof ImageView) {
                    childAt.setAlpha(1.0f);
                }
                childAt.setSelected(true);
            }
        } else {
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View childAt = linearLayout.getChildAt(i);
                if (childAt instanceof TextView) {
                    ((TextView) childAt).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                }
                if (childAt instanceof ImageView) {
                    childAt.setAlpha(0.3f);
                }
                childAt.setSelected(false);
            }
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    public void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
//        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
//        }
        //判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            LinearLayout linearLayout = (LinearLayout) mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            linearLayout.setSelected(ischeck);
            setTextBackgroundAndSize(linearLayout);
        }
    }

    public void nightMode() {
        shade_left.setBackgroundResource(R.drawable.channel_leftblock_nigth);
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) mRadioGroup_content.getChildAt(i);
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                View childAt = linearLayout.getChildAt(j);
                if (childAt instanceof TextView) {
                    ((TextView)childAt).setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_night));
                }
//                else if (childAt instanceof ImageView){
//                    ((ImageView)childAt).setImageResource(imageIdsNight[i]);
//                }
            }
        }
    }

    public void dayMode() {
        shade_left.setBackgroundResource(R.drawable.channel_leftblock);
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) mRadioGroup_content.getChildAt(i);
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                View childAt = linearLayout.getChildAt(j);
                if (childAt instanceof TextView) {
                    ((TextView)childAt).setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
                }
//                else if (childAt instanceof ImageView){
//                    ((ImageView)childAt).setImageResource(imageIds[i]);
//                }
            }
        }
    }

    public interface ClickMoreListener {
        public void clickMore();
    }

    public void setOnClickMoreListener(ClickMoreListener clickMorelistener) {
        moreListener = clickMorelistener;
    }
}
