//package com.shanggame.news.widght;
//
//import android.content.Context;
//import android.graphics.drawable.ColorDrawable;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.shanggame.news.R;
//
//import java.util.HashMap;
//
//
///**
// * Created by zhaojie on 2016/6/22.
// */
//public class SharePopupWindow extends PopupWindow implements View.OnTouchListener, View.OnClickListener, PlatformActionListener {
//    private Context mContext;
//    private final View contentView;
//    private TextView cancel_share;
//    private ImageView iv_sina_share;
//    private ImageView iv_wechat_share;
//    private ImageView iv_pengyouquan_share;
//    private ImageView iv_qq_share;
//    private Platform.ShareParams sp;
//
//    public SharePopupWindow(Context context){
//        this.mContext = context;
//        this.setWidth(AutoLinearLayout.LayoutParams.MATCH_PARENT);
//        this.setHeight(AutoLinearLayout.LayoutParams.WRAP_CONTENT);
//        this.setAnimationStyle(R.style.Animation_up);
//        this.setFocusable(true);
//        this.setOutsideTouchable(true);
//        this.setBackgroundDrawable(new ColorDrawable(0));
//        contentView = LayoutInflater.from(mContext).inflate(R.layout.fragment_recomment,null);
//        this.setContentView(contentView);
//        contentView.setOnTouchListener(this);
//
//        cancel_share = (TextView) contentView.findViewById(R.id.cancel_share);
//        cancel_share.setOnClickListener(this);
//        iv_sina_share = (ImageView) contentView.findViewById(R.id.iv_sina_share);
//        iv_sina_share.setOnClickListener(this);
//        iv_wechat_share = (ImageView) contentView.findViewById(R.id.iv_wechat_share);
//        iv_wechat_share.setOnClickListener(this);
//        iv_pengyouquan_share = (ImageView) contentView.findViewById(R.id.iv_pengyouquan_share);
//        iv_pengyouquan_share.setOnClickListener(this);
//        iv_qq_share = (ImageView) contentView.findViewById(R.id.iv_qq_share);
//        iv_qq_share.setOnClickListener(this);
//    }
//
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP){
//            if (event.getY() < contentView.findViewById(R.id.ll_recomment).getTop()){
//                dismiss();
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.cancel_share:
//                dismiss();
//                break;
//            case R.id.iv_sina_share:
//                sp = new Platform.ShareParams();
//                sp.setText("我是分享文本，啦啦啦~http://uestcbmi.com/"); //分享文本
//                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
//                //3、非常重要：获取平台对象
//                Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//                sinaWeibo.setPlatformActionListener(this); // 设置分享事件回调
//                // 执行分享
//                sinaWeibo.share(sp);
//                break;
//            case R.id.iv_wechat_share:
//                sp = new Platform.ShareParams();
//                sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
//                sp.setTitle("我是分享标题");  //分享标题
//                sp.setText("我是分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
//                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
//                sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
//                //3、非常重要：获取平台对象
//                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//                wechat.setPlatformActionListener(this); // 设置分享事件回调
//                // 执行分享
//                wechat.share(sp);
//                break;
//            case R.id.iv_pengyouquan_share:
//                sp = new Platform.ShareParams();
//                sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
//                sp.setTitle("我是分享标题");  //分享标题
//                sp.setText("我是分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
//                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
//                sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
//                //3、非常重要：获取平台对象
//                Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
//                wechatMoments.setPlatformActionListener(this); // 设置分享事件回调
//                // 执行分享
//                wechatMoments.share(sp);
//                break;
//            case R.id.iv_qq_share:
//                sp = new Platform.ShareParams();
//                sp.setTitle("我是分享标题");
//                sp.setText("我是分享文本，啦啦啦~http://uestcbmi.com/");
//                sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
//                sp.setTitleUrl("http://www.baidu.com");  //网友点进链接后，可以看到分享的详情
//                //3、非常重要：获取平台对象
//                Platform qZone = ShareSDK.getPlatform(QZone.NAME);
//                qZone.setPlatformActionListener(this); // 设置分享事件回调
//                // 执行分享
//                qZone.share(sp);
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void showAtLocation(View parent, int gravity, int x, int y) {
//        super.showAtLocation(parent, gravity, x, y);
////        TranslateAnimation animation = new TranslateAnimation(0,0,0,0);
////        animation.setDuration(3000);
////        animation.setFillAfter(true);
////        contentView.startAnimation(animation);
//    }
//
//    /**
//     * 分享成功
//     * @param platform
//     * @param i
//     * @param hashMap
//     */
//    @Override
//    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//
//    }
//
//    /**
//     * 分享失败
//     * @param platform
//     * @param i
//     * @param throwable
//     */
//    @Override
//    public void onError(Platform platform, int i, Throwable throwable) {
//
//    }
//
//    /**
//     * 取消分享
//     * @param platform
//     * @param i
//     */
//    @Override
//    public void onCancel(Platform platform, int i) {
//
//    }
//}
