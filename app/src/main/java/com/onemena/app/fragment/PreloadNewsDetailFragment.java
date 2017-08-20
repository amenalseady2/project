package com.onemena.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.custom.widget.NewsDetailHorizontalScrollView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.animation.ObjectAnimator;
import com.onemena.app.NewsApplication;
import com.onemena.app.activity.BrowerOpenNewsDetailActivity;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.Config;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseAdapter;
import com.onemena.base.BaseFragment;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.NightMode;
import com.onemena.home.model.javabean.ArticleDetails;
import com.onemena.home.model.javabean.ArticleStatus;
import com.onemena.home.view.adapter.NewsDetailAdapter;
import com.onemena.http.Api;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.onemena.utils.ImageUtil;
import com.onemena.utils.KeyboardUtil;
import com.onemena.utils.MyShareUtils;
import com.onemena.utils.ShareUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.TaskUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.widght.HelveBoldTextView;
import com.onemena.widght.HelveRomanTextView;
import com.onemena.widght.MyDialog2;
import com.onemena.widght.MyWebView;
import com.onemena.widght.NewsDetailSettingBusEvent;
import com.onemena.widght.NotoRegularTextView;
import com.onemena.widght.PopwinComment;
import com.onemena.widght.PopwinNewdetailSetting;
import com.voler.annotation.FieldInject;
import com.voler.saber.Saber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.widget.LinearLayout.VERTICAL;
import static com.arabsada.news.R.id.lay_error;

/**
 * Created by Administrator on 2016/11/3.
 */

public class PreloadNewsDetailFragment extends BaseFragment implements View.OnClickListener, BaseAdapter.OnViewClickListener, CommentFragment.StateChangeListener {

    public static final String ID = "id";
    public static final String CATEGORY_ID = "category_id";
    public static final String POSITION = "position";
    public static final String NEWS_TYPE = "news_type";
    public static final String CONTENT = "content";
    public static final String TITLE = "title";

    @FieldInject("id")
    String mId;
    @FieldInject("category_id")
    String categoryId;

    String realCategoryId;
    @FieldInject
    String position;
    @FieldInject
    String news_type;
    @FieldInject
    String content;

    @FieldInject
    String title;

    private ListView listview_newsdetail;
    private NewsDetailAdapter newsDetailAdapter;
    private Handler handler = new Handler();
    private ImageView back_iv;
    private ImageView menu_iv;
    private MyWebView webView;
    float downX = 0;
    float downY = 0;
    MainActivity.MyTouchListener myTouchListener = new MainActivity.MyTouchListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            // 处理手势事件

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float upX = event.getX();
                    float upY = event.getY();
                    if (scview_bottom_share != null && scview_bottom_share.isIntercept()) {
                        downX = upX;
                        downY = upY;
                        return;
                    }
                    if (Math.abs(upY - downY) < 100 && downX - upX > 200) {
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
                    }
                    if (Math.abs(upY - downY) < 100 && downX - upX < -200) {
                        CommentFragment fragment = CommentFragment.getInstance();
                        Bundle bundle_s = new Bundle();
                        bundle_s.putString(CommentFragment.ID, mId);
                        bundle_s.putString(CommentFragment.CATEGORY_ID, realCategoryId);
                        bundle_s.putString(CommentFragment.TITLE, title);
                        bundle_s.putInt(CommentFragment.COMMENT_COUNT, comment_count);
                        bundle_s.putInt(CommentFragment.FLAG, 0);
                        fragment.setOnStateChangeListener(PreloadNewsDetailFragment.this);
                        fragment.setArguments(bundle_s);
                        addToBackStack(fragment, realCategoryId, mId, start_time, commend);
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).unRegisterMyTouchListener(myTouchListener);
                        } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
                            ((BrowerOpenNewsDetailActivity) getActivity()).unRegisterMyTouchListener(myTouchListener);
                        }
                    }
                    break;
            }
        }
    };
    private PopwinNewdetailSetting popwinNewdetail;
    private HelveRomanTextView tv_detail_comment;
    private TextView indicator_count;
    private PopwinComment popwinComment;
    private ImageView iv_bottom_share;
    private ImageView iv_bottom_save;
    private ImageView iv_bottom_comment;
    private int likeState = -1;
    private int likeNum;
    private int disLikeNum;
    private boolean isSaved;
    private TextView tv_like;
    private HelveRomanTextView tv_report;
    private TextView tv_dislike;
    private ImageView iv_report;
    private ImageView iv_dislike;
    private ImageView iv_like;
    private long start_time;
    private long end_time;
    //    private String unique_id;
    private LinearLayout ll_like;
    private LinearLayout ll_dislike;
    private LinearLayout view_head;
    private LinearLayout ll_report;
    private RelativeLayout rl_tv_intrduce;
    private LinearLayout toolbar_bottom;
    private View view_webview_bottom;
    private LinearLayout web_view_bottom;
    private FrameLayout lay_detail_center;
    private View toolbar_divider;
    private View toolbar_divider_h;
    private HelveRomanTextView tv_rl_intrduce;

    private ImageView dislike_iv_add, dislike_iv_reduce, like_iv_add, like_iv_reduce;
    private String description;
    private View web_view_head;
    private HelveBoldTextView txt_detail_title;
    private int title_size;
    private NotoRegularTextView detail_time;
    private HelveRomanTextView detail_com;
    private SimpleDraweeView img_detail_com;
    private GenericDraweeHierarchy img_detail_comHierarchy;
    private int comment_count;
    private HelveRomanTextView ctv_error;
    private HelveRomanTextView tv_look_origin;
    private NotoRegularTextView deatil_attention_btn;
    private String created_by;
    private int is_follow;
    private String commend="0";
    private String link;
    private OnGetLikeListener onGetLikeListener;

    private List<String> shareIMG;
    private ImageView iv_close;
    private String commentBackId;
    private String taskMessage;
    private ShareUtil util;
    private NewsDetailHorizontalScrollView scview_bottom_share;
    private boolean isShared;
    private String share_url;
    private SimpleDraweeView img_new_load;
    private ImageView new_not_data_img;


    public static PreloadNewsDetailFragment getInstance() {
        return new PreloadNewsDetailFragment();
    }

    public static PreloadNewsDetailFragment newInstance(String id, String categoryId, String position, String newsType, String isFollow) {
        PreloadNewsDetailFragment fragment = new PreloadNewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(ID, id);
        args.putString(CATEGORY_ID, categoryId);
        args.putString(POSITION, position);
        args.putString(NEWS_TYPE, newsType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Saber.inject(this);
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(mContext);//放在加载布局之前
        View view = inflater.inflate(R.layout.fragment_homenewsdetail, container, false);
        // 将myTouchListener注册到分发列表
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
        } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
            ((BrowerOpenNewsDetailActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
        } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
            ((BrowerOpenNewsDetailActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
        }
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {

        if (SpUtil.getInt(SPKey.TITLE_SIZE) == 0) {
            SpUtil.saveValue(SPKey.TITLE_SIZE, 22);
        }
        title_size = SpUtil.getInt(SPKey.TITLE_SIZE);
        EventBus.getDefault().register(this);//注册EventBus
        view_head = (LinearLayout) view.findViewById(R.id.head_detail_view);
        back_iv = (ImageView) view.findViewById(R.id.back_iv);
        img_new_load = (SimpleDraweeView) view.findViewById(R.id.img_new_load);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        new_not_data_img = (ImageView) view.findViewById(R.id.new_not_data_img);
        if (1 < mContext.getSupportFragmentManager().getBackStackEntryCount()) {
            iv_close.setVisibility(View.VISIBLE);
        }

        menu_iv = (ImageView) view.findViewById(R.id.menu_iv);
        listview_newsdetail = (ListView) view.findViewById(R.id.listview_newsdetail);
        newsDetailAdapter = new NewsDetailAdapter(getActivity());
        newsDetailAdapter.setOnViewClickListener(this);

        tv_detail_comment = (HelveRomanTextView) view.findViewById(R.id.tv_detail_comment);
        indicator_count = (TextView) view.findViewById(R.id.indicator_count);
        iv_bottom_share = (ImageView) view.findViewById(R.id.bottom_share);
        iv_bottom_save = (ImageView) view.findViewById(R.id.bottom_save);
        iv_bottom_comment = (ImageView) view.findViewById(R.id.bottom_comment);
        toolbar_bottom = (LinearLayout) view.findViewById(R.id.toolbar_bottom);
        lay_detail_center = (FrameLayout) view.findViewById(R.id.lay_detail_center);
        toolbar_divider = view.findViewById(R.id.toolbar_divider);

        toolbar_divider_h = view.findViewById(R.id.toolbar_divider_h);
        ctv_error = (HelveRomanTextView) view.findViewById(R.id.ctv_error);

        iv_bottom_share.setOnClickListener(this);
        iv_bottom_save.setOnClickListener(this);
        iv_bottom_comment.setOnClickListener(this);


        tv_detail_comment.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        menu_iv.setOnClickListener(this);
        menu_iv.setClickable(false);

        webView = new MyWebView(getContext());//创建自定义的webview
        //webview加载完成后，访问更多推荐新闻
        //webview 的头
        web_view_head = View.inflate(mContext, R.layout.newsdetail_webview_head, null);

        //webview 底部的工具条
        view_webview_bottom = View.inflate(mContext, R.layout.newsdetail_webview_bottom, null);
        view_webview_bottom.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scview_bottom_share.scrollTo(2000, 0);
                view_webview_bottom.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        tv_like = (TextView) view_webview_bottom.findViewById(R.id.tv_like);
        tv_dislike = (TextView) view_webview_bottom.findViewById(R.id.tv_dislike);
        tv_report = (HelveRomanTextView) view_webview_bottom.findViewById(R.id.tv_report);
        web_view_bottom = (LinearLayout) view_webview_bottom.findViewById(R.id.web_view_bottom);
        scview_bottom_share = (NewsDetailHorizontalScrollView) view_webview_bottom.findViewById(R.id.scview_bottom_share);
//        scview_bottom_share.requestDisallowInterceptTouchEvent(true);
//        scview_bottom_share.onTouchEvent()
////        scview_bottom_share.onint

        iv_report = (ImageView) view_webview_bottom.findViewById(R.id.iv_report);
        view_webview_bottom.findViewById(R.id.img_bottom_share_fb).setOnClickListener(this);
        view_webview_bottom.findViewById(R.id.img_bottom_share_tw).setOnClickListener(this);
        view_webview_bottom.findViewById(R.id.img_bottom_share_gl).setOnClickListener(this);
        view_webview_bottom.findViewById(R.id.img_bottom_share_sms).setOnClickListener(this);
        view_webview_bottom.findViewById(R.id.img_bottom_share_wa).setOnClickListener(this);
        view_webview_bottom.findViewById(R.id.img_bottom_share_em).setOnClickListener(this);
        iv_dislike = (ImageView) view_webview_bottom.findViewById(R.id.iv_dislike);
        iv_like = (ImageView) view_webview_bottom.findViewById(R.id.iv_like);
        dislike_iv_add = (ImageView) view_webview_bottom.findViewById(R.id.dislike_iv_add);
        dislike_iv_reduce = (ImageView) view_webview_bottom.findViewById(R.id.dislike_iv_reduce);

        like_iv_add = (ImageView) view_webview_bottom.findViewById(R.id.like_iv_add);
        like_iv_reduce = (ImageView) view_webview_bottom.findViewById(R.id.like_iv_reduce);


        ll_report = (LinearLayout) view_webview_bottom.findViewById(R.id.ll_report);
        ll_like = (LinearLayout) view_webview_bottom.findViewById(R.id.ll_like);
        ll_dislike = (LinearLayout) view_webview_bottom.findViewById(R.id.ll_dislike);
        rl_tv_intrduce = (RelativeLayout) view_webview_bottom.findViewById(R.id.rl_tv_intrduce);
        tv_rl_intrduce = (HelveRomanTextView) view_webview_bottom.findViewById(R.id.tv_rl_intrduce);
        tv_look_origin = (HelveRomanTextView) view_webview_bottom.findViewById(R.id.look_origin);

        txt_detail_title = (HelveBoldTextView) web_view_head.findViewById(R.id.txt_detail_title);
        detail_time = (NotoRegularTextView) web_view_head.findViewById(R.id.detail_time);
//        img_detail_time = (ImageView)web_view_head.findViewById(R.id.img_detail_time);
        detail_com = (HelveRomanTextView) web_view_head.findViewById(R.id.detail_com);
        img_detail_com = (SimpleDraweeView) web_view_head.findViewById(R.id.img_detail_com);

        deatil_attention_btn = (NotoRegularTextView) web_view_head.findViewById(R.id.deatil_attention_btn);
        img_detail_comHierarchy = img_detail_com.getHierarchy();

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(VERTICAL);
        linearLayout.addView(web_view_head);
        linearLayout.addView(webView);
        linearLayout.addView(view_webview_bottom);

        ll_report.setOnClickListener(this);
        ll_like.setOnClickListener(this);
        ll_dislike.setOnClickListener(this);
        tv_look_origin.setOnClickListener(this);
        deatil_attention_btn.setOnClickListener(this);

        listview_newsdetail.addHeaderView(linearLayout);
        listview_newsdetail.setAdapter(newsDetailAdapter);

        checkMode();
        getDataFromNet(mId);

        checkWebViewFontSize();
        webView.getSettings().setCursiveFontFamily("NotoNaskhArabic-Regular.ttf");
        txt_detail_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, title_size);//标题文字大小

        webView.setOnMyWebViewListener(webView -> {
            if (SpUtil.getBoolean(SPKey.MODE, false)) {
                webView.loadUrl("javascript:" + "document.getElementsByTagName('body')[0].setAttribute('style',' text-align: right;direction: rtl;background-color:#252525;color:#707070')");
            } else {
                webView.loadUrl("javascript:" + "document.getElementsByTagName('body')[0].setAttribute('style',' text-align: right; direction: rtl')");
            }

            TaskUtil.sendTask(mContext, "read_news");
            start_time = System.currentTimeMillis();
            TongJiUtil.getInstance().putEntries(TJKey.READ,
                    MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                    MyEntry.getIns(TJKey.CATEGORY_ID, realCategoryId),
                    MyEntry.getIns(TJKey.NEWS_TYPE, news_type),
                    MyEntry.getIns(TJKey.RESOURCE_ID, mId),
                    MyEntry.getIns(TJKey.COMMEND, commend));

        });
    }


    private void addAttention(String mId) {
        if (is_follow == 1) {
            TongJiUtil.getInstance().putEntries(TJKey.FOLLOW_HEADLINES, MyEntry.getIns(TJKey.TYPE, "0"), MyEntry.getIns(TJKey.HEADLINES_ID, mId));
        } else if (is_follow == 0) {
            TongJiUtil.getInstance().putEntries(TJKey.FOLLOW_HEADLINES, MyEntry.getIns(TJKey.TYPE, "1"), MyEntry.getIns(TJKey.HEADLINES_ID, mId));
        }
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.ADD_ATTENTION + mId, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
            }

            @Override
            public void onObjError() {

            }
        });
    }

    @Override
    public void onClick(final View v) {
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
            case R.id.iv_close:
                popBackAllStack();
                break;
            case R.id.menu_iv:
                if (likeState == -1 || likeState == 0) {
//                    ToastUtil.showNormalShortToast("数据错误！");
                    return;
                }
                popwinNewdetail = new PopwinNewdetailSetting(getActivity(), mId, realCategoryId, this, likeState, likeNum, disLikeNum, isSaved);//tool的popwin
                popwinNewdetail.show(v, webView, txt_detail_title);
                break;
            case R.id.look_origin://查看原文
                OriginalArticleFragment oaFragment = OriginalArticleFragment.getInstance(link);
                if (getActivity() instanceof MainActivity) {//取消左右滑动监听
                    ((MainActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
                } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
                    ((BrowerOpenNewsDetailActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
                }
                oaFragment.setOnStateChangeListener(PreloadNewsDetailFragment.this);
                addToBackStack(oaFragment, realCategoryId, mId, start_time, commend);
                break;

            case R.id.tv_detail_comment:
                TongJiUtil.getInstance().putEntries(TJKey.NEWS_COMMENT_CLICK,
                        MyEntry.getIns(TJKey.CATEGORY_ID, realCategoryId),
                        MyEntry.getIns(TJKey.RESOURCE_ID, mId));
                showCommentPOP(v);
                break;
            case R.id.bottom_share:
                ObjectAnimator.ofFloat(iv_bottom_share, "scaleX", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(iv_bottom_share, "scaleY", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                NewsApplication.getInstance().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showSharePop(v);
                    }
                }, 150);
                break;
            case R.id.bottom_save://收藏新闻
                iv_bottom_save.setEnabled(false);
                ObjectAnimator.ofFloat(iv_bottom_save, "scaleX", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(iv_bottom_save, "scaleY", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                saveOrNotSaveNews();
                TongJiUtil.getInstance().putEntries(TJKey.SAVE,
                        MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                        MyEntry.getIns(TJKey.RESOURCE_ID, mId),
                        MyEntry.getIns(TJKey.CATEGORY_ID, realCategoryId)
                );
                TongJiUtil.getInstance().putStatistics(
                        MyEntry.getIns(TJKey.DOCTYPE, "news"),
                        MyEntry.getIns(TJKey.ACTYPE, "collect"),
                        MyEntry.getIns(TJKey.DOCID, mId));

                break;
            case R.id.bottom_comment:
                ObjectAnimator.ofFloat(iv_bottom_comment, "scaleX", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(iv_bottom_comment, "scaleY", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                if (getActivity() instanceof MainActivity) {//取消左右滑动监听
                    ((MainActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
                } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
                    ((BrowerOpenNewsDetailActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
                }
                CommentFragment fragment = CommentFragment.getInstance();
                Bundle bundle_s = new Bundle();
                bundle_s.putString(CommentFragment.ID, mId);
                bundle_s.putString(CommentFragment.CATEGORY_ID, realCategoryId);
                bundle_s.putString(CommentFragment.TITLE, title);
                bundle_s.putInt(CommentFragment.COMMENT_COUNT, comment_count);
                bundle_s.putInt(CommentFragment.FLAG, 0);

                fragment.setArguments(bundle_s);
                fragment.setOnStateChangeListener(PreloadNewsDetailFragment.this);
                addToBackStack(fragment, realCategoryId, mId, start_time, commend);

                TongJiUtil.getInstance().putEntries(TJKey.NEWS_COMMENT,
                        MyEntry.getIns(TJKey.CATEGORY_ID, realCategoryId),
                        MyEntry.getIns(TJKey.RESOURCE_ID, mId));

                break;
            case R.id.ll_report:

                ObjectAnimator.ofFloat(iv_report, "scaleX", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(iv_report, "scaleY", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                ReportFragment reportFragment = ReportFragment.getInstance();
                Bundle bundle_r = new Bundle();
                bundle_r.putString(ReportFragment.ID, mId);
                bundle_r.putString(ReportFragment.CATEGORY_ID, realCategoryId);
                bundle_r.putString(ReportFragment.TYPE, "1");
                reportFragment.setArguments(bundle_r);
                addToBackStack(reportFragment);
                TongJiUtil.getInstance().putEntries(TJKey.NEWS_REPORT,
                        MyEntry.getIns(TJKey.CATEGORY_ID, realCategoryId),
                        MyEntry.getIns(TJKey.RESOURCE_ID, mId));
                break;
            case R.id.ll_dislike:
                ObjectAnimator.ofFloat(iv_dislike, "scaleX", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(iv_dislike, "scaleY", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                if (likeState == 1) {//从不知道-->不喜欢
                    disLikeNum += 1;
                    sendDataToNet(3);
                    changeLikeView(3);
                    showMaopaoAnim(dislike_iv_add);
                    likeState = 3;
                } else if (likeState == 2) {
                    //已经喜欢了，取消喜欢，变为不喜欢。喜欢减一，不喜欢加一
                    likeNum -= 1;
                    disLikeNum += 1;
                    sendDataToNet(3);
                    changeLikeView(3);
                    likeState = 3;
                    showMaopaoAnim(dislike_iv_add);
                } else if (likeState == 3) {
                    //已经不喜欢了，变为不知道
                    disLikeNum -= 1;
                    sendDataToNet(3);
                    changeLikeView(1);
                    likeState = 1;
                    showMaopaoAnim(dislike_iv_reduce);
                }

                TongJiUtil.getInstance().putEntries(TJKey.CLICK_DISLIKE,
                        MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                        MyEntry.getIns(TJKey.RESOURCE_ID, mId),
                        MyEntry.getIns(TJKey.CATEGORY_ID, realCategoryId));
                TongJiUtil.getInstance().putStatistics(
                        MyEntry.getIns(TJKey.DOCTYPE, "news"),
                        MyEntry.getIns(TJKey.ACTYPE, "disLike"),
                        MyEntry.getIns(TJKey.DOCID, mId));


                break;
            case R.id.ll_like:

                ObjectAnimator.ofFloat(iv_like, "scaleX", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(iv_like, "scaleY", 1.0f, 1.2f, 1.0f).setDuration(200).start();
                if (likeState == 1) {
                    likeNum += 1;
                    sendDataToNet(2);
                    changeLikeView(2);
                    showMaopaoAnim(like_iv_add);
                } else if (likeState == 2) {
                    //已经喜欢了，取消喜欢
                    likeNum -= 1;
                    sendDataToNet(2);
                    changeLikeView(1);
                    showMaopaoAnim(like_iv_reduce);
                } else if (likeState == 3) {
                    //已经不喜欢了，变为：喜欢加一，不喜欢减一
                    likeNum += 1;
                    disLikeNum -= 1;
                    sendDataToNet(2);
                    changeLikeView(2);
                    showMaopaoAnim(like_iv_add);
                }

                if (onGetLikeListener != null) {
                    onGetLikeListener.setLikeNumCallback(likeNum + "", position);
                }

                TongJiUtil.getInstance().putEntries(TJKey.CLICK_LICK,
                        MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                        MyEntry.getIns(TJKey.RESOURCE_ID, mId),
                        MyEntry.getIns(TJKey.CATEGORY_ID, realCategoryId));
                TongJiUtil.getInstance().putStatistics(
                        MyEntry.getIns(TJKey.DOCTYPE, "news"),
                        MyEntry.getIns(TJKey.ACTYPE, "like"),
                        MyEntry.getIns(TJKey.DOCID, mId));
                break;
            case lay_error:
                getDataFromNet(mId);
                break;
            case R.id.deatil_attention_btn:
                if (0 == is_follow) {
                    is_follow = 1;
                    addFollowDialog("تمت المتابعة");
                } else {
                    is_follow = 0;
                }

                checkFllow(is_follow);
                addAttention(created_by);
                break;
            case R.id.img_bottom_share_fb:
                if (share("Facebook")) {
                    return;
                }
                MyShareUtils.shareFacebook(share_url, shareIMG, mContext);
                break;
            case R.id.img_bottom_share_tw:
                if (share("Twitter")) {
                    return;
                }
                String sharetwDesc = title + "\n" + share_url + "\n";
                MyShareUtils.shareTwitter(sharetwDesc, mContext);
                break;
            case R.id.img_bottom_share_gl:
                if (share("Google")) {
                    return;
                }
                MyShareUtils.shareGoogle(share_url, mContext);
                break;
            case R.id.img_bottom_share_sms:
                if (share("SMS")) {
                    return;
                }
                String shareSmsDesc = title + "\n" + share_url + "\n";
                MyShareUtils.sendSMS(shareSmsDesc, mContext);

                break;
            case R.id.img_bottom_share_wa:
                if (share("WhatApp")) {
                    return;
                }
                String shareWaDesc = title + "\r\n" + share_url + "\n";
                MyShareUtils.shareWhatApp(shareWaDesc, mContext);
                break;
            case R.id.img_bottom_share_em:
                if (share("Email")) {
                    return;
                }
                String shareDesc = "\r\n" + title + "\r\n" + share_url + "\r\n";
                MyShareUtils.sendMail(shareDesc, title, shareIMG, mContext);
                break;
        }
    }

    private boolean share(String shareWay) {
        if (StringUtils.isEmpty(share_url)) return true;
        TongJiUtil.getInstance().putEntries(TJKey.SHARE,
                MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                MyEntry.getIns(TJKey.RESOURCE_ID, mId),
                MyEntry.getIns(TJKey.CATEGORY_ID, realCategoryId),
                MyEntry.getIns(TJKey.SHARE_WAY, shareWay));
        isShared = true;
        return false;
    }

    @Override
    public void onViewClick(View view, int position) {
        Bundle bundle = new Bundle();
        String id = newsDetailAdapter.getItem(position).getId();

        TongJiUtil.getInstance().putEntries(TJKey.RELATED,
                MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                MyEntry.getIns(TJKey.RESOURCE_ID, mId),
                MyEntry.getIns(TJKey.RESOURCE_RELATED_ID, id));

        TongJiUtil.getInstance().putStatistics(
                MyEntry.getIns(TJKey.ACTYPE, "click"),
                MyEntry.getIns(TJKey.DOCTYPE, "news"),
                MyEntry.getIns(TJKey.DOCID, id),
                MyEntry.getIns(TJKey.RELRECOM, "2_" + mId));
        bundle.putString("id", id);
        bundle.putString("category_id", realCategoryId);
        bundle.putString("news_type", "8");
        PreloadNewsDetailFragment instance = PreloadNewsDetailFragment.getInstance();
        instance.setArguments(bundle);
        addToBackStack(instance, realCategoryId, mId, start_time, commend);

    }

    //改变点赞的界面
    private void changeLikeView(int like) {
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            //夜间
            switch (like) {
                case 1://不知道喜不喜欢
                    iv_like.setImageResource(R.mipmap.details_icon_like_night);
                    iv_dislike.setImageResource(R.mipmap.details_icon_dislike_night);
                    tv_like.setTextColor(getResources().getColor(R.color.nighttxt_707070));
                    tv_dislike.setTextColor(getResources().getColor(R.color.nighttxt_707070));
                    ll_dislike.setBackgroundResource(R.drawable.common_white_btn_selector_night);
                    ll_like.setBackgroundResource(R.drawable.common_white_btn_selector_night);
                    break;
                case 2://喜欢
                    iv_like.setImageResource(R.mipmap.details_icon_liked_night);
                    iv_dislike.setImageResource(R.mipmap.details_icon_dislike_night);
                    ll_like.setBackgroundResource(R.drawable.common_blue_btn_bg_night);
                    ll_dislike.setBackgroundResource(R.drawable.common_white_btn_selector_night);
                    tv_like.setTextColor(Color.parseColor("#234A7D"));
                    tv_dislike.setTextColor(getResources().getColor(R.color.nighttxt_707070));
                    break;
                case 3://不喜欢
                    iv_like.setImageResource(R.mipmap.details_icon_like_night);
                    iv_dislike.setImageResource(R.mipmap.details_icon_disliked_night);
                    ll_dislike.setBackgroundResource(R.drawable.common_red_btn_bg_night);
                    ll_like.setBackgroundResource(R.drawable.common_white_btn_selector_night);
                    tv_dislike.setTextColor(Color.parseColor("#935656"));
                    tv_like.setTextColor(getResources().getColor(R.color.nighttxt_707070));
                    break;
            }


        } else {
            //白天
            switch (like) {
                case 1://不知道喜不喜欢
                    iv_like.setImageResource(R.mipmap.details_icon_like);
                    iv_dislike.setImageResource(R.mipmap.details_icon_dislike);
                    ll_like.setBackgroundResource(R.drawable.common_white_btn_selector);
                    ll_dislike.setBackgroundResource(R.drawable.common_white_btn_selector);

                    tv_like.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
                    tv_dislike.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
                    break;
                case 2://喜欢
                    iv_like.setImageResource(R.mipmap.details_icon_liked_check);
                    iv_dislike.setImageResource(R.mipmap.details_icon_dislike);
                    ll_like.setBackgroundResource(R.drawable.common_blue_btn_bg);
                    ll_dislike.setBackgroundResource(R.drawable.common_white_btn_selector);

                    tv_like.setTextColor(mContext.getResources().getColor(R.color.main_bule));
                    tv_dislike.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
                    break;
                case 3://不喜欢
                    iv_like.setImageResource(R.mipmap.details_icon_like);
                    iv_dislike.setImageResource(R.mipmap.details_icon_dislike_check);
                    ll_dislike.setBackgroundResource(R.drawable.common_red_btn_bg);
                    ll_like.setBackgroundResource(R.drawable.common_white_btn_selector);
                    tv_like.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
                    tv_dislike.setTextColor(mContext.getResources().getColor(R.color.txt_EB4E35));
                    break;
            }

        }


        tv_dislike.setText(StringUtils.int2IndiaNum(Integer.toString(disLikeNum)));
        tv_like.setText(StringUtils.int2IndiaNum(Integer.toString(likeNum)));
        likeState = like;
    }

    @Override
    public void onBackStackChanged() {
        super.onBackStackChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }


    @Override
    public void onStop() {
        super.onStop();
        if (start_time != 0 && iscount) {
            end_time = System.currentTimeMillis();//news_id
            TongJiUtil.getInstance().putEntries(TJKey.DETAIL_DURATION,
                    MyEntry.getIns(TJKey.CATEGORY_ID, realCategoryId),
                    MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                    MyEntry.getIns(TJKey.RESOURCE_ID, mId),
                    MyEntry.getIns(TJKey.COMMEND, commend),
                    MyEntry.getIns(TJKey.DURATION, Long.toString((end_time - start_time) / 1000)));
            iscount = false;
        }

    }

    @Override
    public void onDismiss(int commentNum) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
        } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
            ((BrowerOpenNewsDetailActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
        }
        comment_count = commentNum;
        indicator_count.setText(StringUtils.int2IndiaNum(String.valueOf(comment_count)));//评论个数-----
        JSONObject object = new JSONObject();
        object.put("position", position);
        object.put("comment_count", comment_count);
        object.put("category_id", categoryId);
        EventBus.getDefault().post(object);
        if (comment_count > 0) {
            indicator_count.setVisibility(View.VISIBLE);
        } else {
            indicator_count.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDismiss() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
        } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
            ((BrowerOpenNewsDetailActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
        }
    }

    //收藏或者取消收藏
    public void saveOrNotSaveNews() {
        HashMap<String, String> params = new HashMap<>();
        aaa(params, new String[]{mId});
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_DEL_FAV, params, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {

                iv_bottom_save.setEnabled(true);
                isSaved = !isSaved;
                updateCollection();
                addFollowDialog(mContext.getResources().getString(isSaved ? R.string.collection_success : R.string.collection_cancel));
            }

            @Override
            public void onObjError() {
                iv_bottom_save.setEnabled(true);
            }
        });
    }


    private void sendDataToNet(int i) {
        switch (i) {
            case 2://点赞
                setLike(i);
                break;
            case 3://点不赞
                setDislike(i);
                break;
        }
    }

    private void setDislike(int i) {
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_SET_DISLIKE + mId, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
            }

            @Override
            public void onObjError() {

            }
        });
    }

    private void setLike(int i) {
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_SET_LIKE + mId, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
            }

            @Override
            public void onObjError() {
            }
        });
    }

    //增加 减少 的动画
    private void showMaopaoAnim(final View v) {
        v.setVisibility(View.VISIBLE);
        Animation set = AnimationUtils.loadAnimation(mContext, R.anim.maopao_anim);
        v.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaAnimation aa = new AlphaAnimation(1.0f, 0f);
                // 设定播放时间
                aa.setDuration(1000);
                // 设定重复次数，实际次数是重复次数加一。Animation.INFINITE为-1，表示无限次循环
                aa.setRepeatCount(0);
                // 设定重复模式.Animation.REVERS=2表示倒序重复 ，，，Animation.RESTART=1，表示从头开始
                aa.setRepeatMode(Animation.RESTART);
                // 开启动画
                v.startAnimation(aa);
                aa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        v.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    //EventBus接收消息
    public void onEventMainThread(NewsDetailSettingBusEvent event) {
        likeState = event.likeState;
        likeNum = event.likeNum;
        disLikeNum = event.disLikeNum;
//        isSaved = event.isSaved;

        tv_like.setText(StringUtils.int2IndiaNum(likeNum + ""));
        tv_dislike.setText(StringUtils.int2IndiaNum(disLikeNum + ""));
        changeLikeView(likeState);
    }

    //分享方法
    private void showSharePop(View view) {
        //Activity context, String shareTitle, String shareDesc , String shareMainPic, String shareUrl,String category_id,String news_id,String come
        TongJiUtil.getInstance().putStatistics(
                MyEntry.getIns(TJKey.ACTYPE, "share"),
                MyEntry.getIns(TJKey.DOCTYPE, "news"),
                MyEntry.getIns(TJKey.DOCID, mId));
        util = new ShareUtil(getActivity(), title, description, shareIMG, share_url, realCategoryId, mId, Config.FIREBAENEWSTYPE);
        util.showSharePopupWindow(view);

    }

    //显示评论的pop
    private void showCommentPOP(View v) {

        popwinComment = new PopwinComment(getActivity(), realCategoryId, mId, 0, new PopwinComment.CommentListener() {
            @Override
            public void comListener() {
                CommentFragment fragment = CommentFragment.getInstance();
                Bundle bundle_s = new Bundle();
                bundle_s.putString(CommentFragment.ID, mId);
                bundle_s.putString(CommentFragment.CATEGORY_ID, realCategoryId);
                bundle_s.putString(CommentFragment.TITLE, title);
                bundle_s.putInt(CommentFragment.COMMENT_COUNT, comment_count+1);
                bundle_s.putInt(CommentFragment.FLAG, 0);
                fragment.setArguments(bundle_s);
                fragment.setOnStateChangeListener(PreloadNewsDetailFragment.this);
                addToBackStack(fragment, realCategoryId, mId, start_time, commend);
            }
        });
        popwinComment.setPopListener(new PopwinComment.PopListener() {
            @Override
            public void showFinished(EditText et) {
                KeyboardUtil.showKeyBoard(et);
            }
        });

        popwinComment.show(v);

    }

    //获取文章详情信息
    private void getDataFromNet(String id) {
        txt_detail_title.setText(Html.fromHtml(title));
        content = getMyHtmlData(content);
        Document doc = Jsoup.parse(content);
        Elements pngs = doc.select("img[src]");
        if (pngs != null || pngs.size() > 0) {
            for (Element element : pngs) {
                element.attr("style", "min-height:240px");
                element.attr("onerror", "this.src='file:///android_asset/default_home_list_img.png'");
            }
        }
        content = doc.toString();
        shareIMG = StringUtils.search(content, "<img(.*?)src=\"(.*?)\"");
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(1000);  //设置动画时间
        alphaAnimation.setFillAfter(true);
        webView.startAnimation(alphaAnimation);


        Api.getComApi()
                .getArticleDetails(id)
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleDetails -> {
                    if (articleDetails.isSuccess()) {
                        ArticleDetails.ContentBean contentBean = articleDetails.getContent();

                        description = contentBean.getDescription();


                        created_by = contentBean.getCreatedBy();

                        link = contentBean.getLink();
//                        commend = contentBean.getFeatured();//categoriescategories
                        realCategoryId = contentBean.getCategories();//categories

                        share_url = contentBean.getShareUrl();


                        detail_time.setText(contentBean.getCreateTime());

                        detail_com.setText(contentBean.getFirstName());

                        img_detail_com.setImageURI(contentBean.getProfilePhoto());

                        comment_count = contentBean.getCommentCount();
                        if (comment_count > 0) {
                            indicator_count.setVisibility(View.VISIBLE);
                        } else {
                            indicator_count.setVisibility(View.GONE);
                        }
                        indicator_count.setText(StringUtils.int2IndiaNum(String.valueOf(comment_count)));//评论个数-----

                        menu_iv.setClickable(true);
                    }
                }, throwable -> {
                    likeState = -1;
                    menu_iv.setClickable(true);
                });

        Api.getComApi()
                .getRelationArticle(id)
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleRelations -> {
                    newsDetailAdapter.notifyDataSetChanged(0, articleRelations);
                }, Throwable::printStackTrace);


        Api.getComApi()
                .getArticleStatus(id)
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articleStatus -> {
                    if (articleStatus.isSuccess()) {
                        ArticleStatus.ContentBean content = articleStatus.getContent();
                        isSaved = 1 == content.getUserFavouriteStatus();
                        updateCollection();

                        likeNum = content.getLike();
                        disLikeNum = content.getDislike();
                        is_follow = content.getIsFollow();
                        checkFllow(is_follow);

                        int user_liked = content.getUserLikeStatus();
                        int user_disliked = content.getUserDislikeStatus();
                        if (0 == user_disliked && 0 == user_liked) {
                            likeState = 1;//喜欢的状态，1为不祥，2为喜欢，3为不喜欢
                        } else if (1 == user_liked) {
                            likeState = 2;
                        } else if (1 == user_disliked) {
                            likeState = 3;
                        }
                        changeLikeView(likeState);
                    }
                }, throwable -> {

                    throwable.printStackTrace();
                });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (util != null) {
            if (util.isShare()) {
                TaskUtil.sendTask(mContext, "share_news");
                util.setShare(false);
            }
        }
        if (isShared) {
            TaskUtil.sendTask(mContext, "share_news");
            isShared = false;
        }
    }


    private void updateCollection() {
        if (isSaved) {//已经收藏了，现在要取消收藏
            if (SpUtil.getBoolean(SPKey.MODE, false)) {
                iv_bottom_save.setImageResource(R.mipmap.details_tabbar_saved_night);

            } else {
                iv_bottom_save.setImageResource(R.mipmap.details_tabbar_saved);
            }
        } else {
            if (SpUtil.getBoolean(SPKey.MODE, false)) {
                iv_bottom_save.setImageResource(R.mipmap.details_tabbar_save_night);

            } else {
                iv_bottom_save.setImageResource(R.mipmap.details_tabbar_save);
            }
        }
    }

    private void aaa(HashMap<String, String> params, String[] ids) {
        int i = 0;
        for (String id : ids) {
            params.put("id[" + i + "]", id);
            i++;
        }
    }

    //检测关注按钮
    private void checkFllow(int is_fllow) {
        if (1 == is_fllow) {
            if (SpUtil.getBoolean(SPKey.MODE, false)) {
                deatil_attention_btn.setBackground(mContext.getResources().getDrawable(R.mipmap.followed_btn_night));

            } else {
                deatil_attention_btn.setBackground(mContext.getResources().getDrawable(R.mipmap.followed_btn_night));

            }
        } else {
            if (SpUtil.getBoolean(SPKey.MODE, false)) {//
                deatil_attention_btn.setBackground(mContext.getResources().getDrawable(R.mipmap.follow_btn_night));
            } else {
                deatil_attention_btn.setBackground(mContext.getResources().getDrawable(R.mipmap.follow_btn));
            }
        }
    }

    /**
     * 导入本地字库设置webview 字体
     *
     * @param content String
     * @return String
     */
    private String getMyHtmlData(String content) {
        String data = "<!doctype html> <html><head>" + "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width;\" user-scalable=\"0;\" maximum-scale=\"1.0;\" initial-scale=\"1.0;\">" +
                "<link type=\"text/css\"  href=\"file:///android_asset/css/webview.css\" rel=\"stylesheet\">" +
                "</head>";
        data = data + "<body >";
        data = data + "<div dir=\"auto\">" + content + "</div>";
        data = data + "</body></html>";
        return data;
    }


    public void onEventMainThread(NightMode mode) {
        checkMode();
        checkFllow(is_follow);
    }

    private void checkMode() {
        boolean mode = SpUtil.getBoolean(SPKey.MODE, false);
        newsDetailAdapter.notifyDataSetChanged();
        updateCollection();
        if (mode) {
            //夜间模式
            ctv_error.setTextColor(Color.parseColor("#707070"));
            ImageUtil.loadGifPicInApp(img_new_load, R.mipmap.img_new_loading_night);
            web_view_head.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
//            img_detail_time.setImageResource(R.mipmap.details_comment_time_night);
            back_iv.setImageResource(R.mipmap.details_navbar_back_night);
            iv_close.setImageResource(R.mipmap.details_navbar_close_night);
            view_head.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
            web_view_bottom.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
            iv_bottom_share.setImageResource(R.mipmap.details_tabbar_share_night);
            iv_bottom_comment.setImageResource(R.mipmap.details_tabbar_comment_video_night);
            toolbar_bottom.setBackgroundColor(getResources().getColor(R.color.nightbg_1b1b1b));
            lay_detail_center.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
            rl_tv_intrduce.setBackgroundColor(getResources().getColor(R.color.nightbg_1b1b1b));
            view_webview_bottom.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
            menu_iv.setImageResource(R.mipmap.details_navbar_more_night);
            ll_report.setBackgroundResource(R.drawable.common_white_btn_selector_night);
            iv_report.setImageResource(R.mipmap.details_icon_night);
            toolbar_divider.setBackgroundColor(Color.parseColor("#464646"));
            toolbar_divider_h.setBackgroundColor(Color.parseColor("#464646"));
            tv_detail_comment.setBackgroundResource(R.drawable.common_white_btn_selector_night);
            tv_detail_comment.setTextColor(getResources().getColor(R.color.nighttxt_707070));
            indicator_count.setBackgroundResource(R.drawable.common_red_btn_selector_night);
            dislike_iv_reduce.setImageResource(R.mipmap.details_cacel);
            like_iv_reduce.setImageResource(R.mipmap.details_cacel);
            dislike_iv_add.setImageResource(R.mipmap.details_reduce_);
            like_iv_add.setImageResource(R.mipmap.details_add_night);
            tv_dislike.setTextColor(getResources().getColor(R.color.nighttxt_707070));
            tv_like.setTextColor(getResources().getColor(R.color.nighttxt_707070));
            tv_report.setTextColor(getResources().getColor(R.color.nighttxt_707070));
            detail_com.setTextColor(getResources().getColor(R.color.nighttxt_707070));
            detail_time.setTextColor(getResources().getColor(R.color.nighttxt_707070));
            txt_detail_title.setTextColor(getResources().getColor(R.color.nighttxt_707070));
            tv_rl_intrduce.setTextColor(getResources().getColor(R.color.nighttxt_707070));
            new_not_data_img.setImageResource(R.mipmap.details_failed_to_load_img_night);
            img_detail_comHierarchy.setPlaceholderImage(R.mipmap.news_com_night);
            webView.loadUrl("javascript:" + "document.getElementsByTagName('body')[0].setAttribute('style',' text-align: right; direction: rtl;background-color:#252525;color:#707070')");

        } else {
            //白天模式
            ImageUtil.loadGifPicInApp(img_new_load, R.mipmap.img_new_loading);
            ctv_error.setTextColor(Color.parseColor("#a1a6bb"));
//            img_detail_time.setImageResource(R.mipmap.details_comment_time);
            back_iv.setImageResource(R.mipmap.details_navbar_back_day);
            new_not_data_img.setImageResource(R.mipmap.details_failed_to_load_img);
            iv_close.setImageResource(R.mipmap.details_navbar_close);
            web_view_head.setBackgroundColor(getResources().getColor(R.color.white));
            img_detail_comHierarchy.setPlaceholderImage(R.mipmap.news_com);
            view_head.setBackgroundColor(getResources().getColor(R.color.white));
            web_view_bottom.setBackgroundColor(getResources().getColor(R.color.white));
            lay_detail_center.setBackgroundColor(getResources().getColor(R.color.white));
            iv_bottom_share.setImageResource(R.mipmap.details_tabbar_share);
            iv_bottom_comment.setImageResource(R.mipmap.details_tabbar_comment);
            toolbar_bottom.setBackgroundColor(getResources().getColor(R.color.background));
            rl_tv_intrduce.setBackgroundColor(getResources().getColor(R.color.activity_bg_f5));
            view_webview_bottom.setBackgroundColor(getResources().getColor(R.color.white));
            menu_iv.setImageResource(R.mipmap.details_navbar_more);

            ll_report.setBackgroundResource(R.drawable.common_white_btn_selector);
            detail_com.setTextColor(getResources().getColor(R.color.textcolor_a1a6bb));
            detail_time.setTextColor(getResources().getColor(R.color.textcolor_a1a6bb));
            txt_detail_title.setTextColor(getResources().getColor(R.color.black));

            iv_report.setImageResource(R.mipmap.details_icon_report);
            toolbar_divider.setBackgroundColor(Color.parseColor("#D8D8D8"));
            toolbar_divider_h.setBackgroundColor(Color.parseColor("#D8D8D8"));
            tv_detail_comment.setBackgroundResource(R.drawable.common_white_btn_selector);
            tv_detail_comment.setTextColor(getResources().getColor(R.color.txt_D2D5E1));
            indicator_count.setBackgroundResource(R.drawable.common_red_btn_selector);

            like_iv_reduce.setImageResource(R.mipmap.details_cace_night);
            like_iv_add.setImageResource(R.mipmap.details_add);

            dislike_iv_add.setImageResource(R.mipmap.details_reduce_add);
            dislike_iv_reduce.setImageResource(R.mipmap.details_cace_night);
            tv_rl_intrduce.setTextColor(getResources().getColor(R.color.textcolor_a1a6bb));

            webView.loadUrl("javascript:" + "document.getElementsByTagName('body')[0].setAttribute('style',' text-align: right; direction: rtl')");
//            listview_newsdetail.setDivider(new ColorDrawable(Color.parseColor("#E4E7F0")));
        }
    }

    /**
     * 设置webview 字体
     */
    private void checkWebViewFontSize() {
        String webview_size = SpUtil.getString(SPKey.WEBVIEW_SIZE);
        switch (webview_size) {
            case "صغير":
                webView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case "طبيعي":
                webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case "كبير":
                webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
                break;
            case "الأكبر":
                webView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                break;
            default:
                break;
        }

    }

    private void addFollowDialog(String text) {
        final View view = View.inflate(getActivity(), R.layout.clear_cache_result_dialog, null);
        final MyDialog2 dialog = new MyDialog2(getActivity(), view, R.style.dialog2);

        final HelveRomanTextView tv_msg = (HelveRomanTextView) view.findViewById(R.id.tv_msg);
        ImageView pb_loding = (ImageView) view.findViewById(R.id.pb_loding);
//        tv_msg.setText("تمت المتابعة");
        tv_msg.setText(text);
        pb_loding.setImageResource(R.mipmap.personal_center_cleared);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1200);
        //设置显示动画
        dialog.getWindow().setWindowAnimations(R.style.clearcache_dismiss_animation);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void setOnGetLikeListener(OnGetLikeListener onGetLikeListener) {
        this.onGetLikeListener = onGetLikeListener;
    }

    public interface OnGetLikeListener {
        void setLikeNumCallback(String numCallback, String position);
    }

}
