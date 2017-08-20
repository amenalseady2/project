package com.onemena.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.arabsada.news.R;
import com.onemena.app.config.TJKey;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.POPWindow;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/12/17.
 */

public enum VersionStoreUpUtils {

    INSTANCE;


    private PopupWindow pop;
    private boolean isForce;

    /**
     * 启动到应用商店app详情界面
     * com.android.vending	Google Play
     * com.tencent.android.qqdownloader	应用宝
     * com.qihoo.appstore	360手机助手
     * com.baidu.appsearch	百度手机助
     * com.xiaomi.market	小米应用商店
     * com.wandoujia.phoenix2	豌豆荚
     * com.huawei.appmarket	华为应用市场
     * com.taobao.appcenter	淘宝手机助手
     * com.hiapk.marketpho	安卓市场
     * cn.goapk.market
     * <p>
     * 文／Wing_Li（简书作者）
     * 原文链接：http://www.jianshu.com/p/a4a806567368
     * 著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。
     */
    public void launchAppDetail(View parent, Activity context, int netVersionCode, Integer forceVersion) {
        try {
            String appPkg = context.getPackageName();
            int localVersionCode = context.getPackageManager()
                    .getPackageInfo(appPkg, 0).versionCode;
//            appPkg="com.google.android.apps.plus";
//            String marketPkg="com.android.vending";
            if (TextUtils.isEmpty(appPkg)) return;

            if (netVersionCode > localVersionCode) {
                //弹出更新的对话框
                showVersionUpdatePop(parent, context, netVersionCode, forceVersion, localVersionCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchAppDetail(View parent, Activity context, int netVersionCode, Integer forceVersion,String url) {
        try {
            String appPkg = context.getPackageName();
            int localVersionCode = context.getPackageManager()
                    .getPackageInfo(appPkg, 0).versionCode;
//            appPkg="com.google.android.apps.plus";
//            String marketPkg="com.android.vending";
            if (TextUtils.isEmpty(appPkg)) return;

            if (netVersionCode > localVersionCode) {
                //弹出更新的对话框
                showVersionUpdatePop(parent, context, netVersionCode, forceVersion, localVersionCode,url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //更新的popwin
    private void showVersionUpdatePop(View view, final Activity context, int netVersionCode, Integer forceVersion, int localVersionCode) {
        View popview = View.inflate(context, R.layout.version_update_pop, null);


        pop = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tv_cancel = (TextView) popview.findViewById(R.id.tv_cancel);
        final TextView tv_commit = (TextView) popview.findViewById(R.id.tv_commit);
        TextView tv_commit_force = (TextView) popview.findViewById(R.id.tv_commit_force);
        TextView tv_current_version = (TextView) popview.findViewById(R.id.tv_current_version);
        TextView tv_new_version = (TextView) popview.findViewById(R.id.tv_new_version);
        LinearLayout updata_lay = (LinearLayout) popview.findViewById(R.id.updata_lay);

        LogManager.i("forceVersion----",forceVersion+"");
        if (localVersionCode < forceVersion) {
            ///强制更新
            tv_commit_force.setVisibility(View.VISIBLE);
            updata_lay.setVisibility(View.GONE);
            isForce=true;
        } else {
            //一般更新
            tv_commit_force.setVisibility(View.GONE);
            updata_lay.setVisibility(View.VISIBLE);
            isForce=false;
        }


        // 设置pop背景，如果没有设置背景(这里设置的背景为空)，当点击其他位置的时候，pop不会自动消失
//        pop.setBackgroundDrawable(null);
        //获取popwindow焦点
//        pop.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
//        pop.setOutsideTouchable(false);
        pop.update();


        tv_current_version.setText("النسخة الحالية：" + "V" + localVersionCode);
        tv_new_version.setText("النسخة الأحدث：" + "V" + netVersionCode);


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        tv_commit_force.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_commit.performClick();
            }
        });
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String appPkg = context.getPackageName();
                    String marketPkg = "com.android.vending";
                    if (TextUtils.isEmpty(appPkg)) return;

                    Uri uri = Uri.parse("market://details?id=" + appPkg);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (!TextUtils.isEmpty(marketPkg)) {
                        intent.setPackage(marketPkg);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //退出应用
                pop.dismiss();
                context.finish();
                System.exit(0);
            }
        });
        //设置popwindow显示位置
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);


        EventBus.getDefault().post(new POPWindow(true));
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new POPWindow(false));
            }
        });
    }
    private void showVersionUpdatePop(View view, final Activity context, int netVersionCode, Integer forceVersion, int localVersionCode,final String updateUrl) {
        View popview = View.inflate(context, R.layout.version_update_pop, null);


        pop = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tv_cancel = (TextView) popview.findViewById(R.id.tv_cancel);
        final TextView tv_commit = (TextView) popview.findViewById(R.id.tv_commit);
        TextView tv_commit_force = (TextView) popview.findViewById(R.id.tv_commit_force);
        TextView tv_current_version = (TextView) popview.findViewById(R.id.tv_current_version);
        TextView tv_new_version = (TextView) popview.findViewById(R.id.tv_new_version);
        LinearLayout updata_lay = (LinearLayout) popview.findViewById(R.id.updata_lay);

        LogManager.i("forceVersion----",forceVersion+"");
        if (localVersionCode < forceVersion) {
            ///强制更新
            tv_commit_force.setVisibility(View.VISIBLE);
            updata_lay.setVisibility(View.GONE);
            isForce=true;
        } else {
            //一般更新
            tv_commit_force.setVisibility(View.GONE);
            updata_lay.setVisibility(View.VISIBLE);
            isForce=false;
        }


        // 设置pop背景，如果没有设置背景(这里设置的背景为空)，当点击其他位置的时候，pop不会自动消失
//        pop.setBackgroundDrawable(null);
        //获取popwindow焦点
//        pop.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
//        pop.setOutsideTouchable(false);
        pop.update();


        tv_current_version.setText("النسخة الحالية：" + "V" + localVersionCode);
        tv_new_version.setText("النسخة الأحدث：" + "V" + netVersionCode);


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TongJiUtil.getInstance().putEntries(TJKey.VERSION_UPDATE, MyEntry.getIns(TJKey.TYPE,"3"));
                pop.dismiss();
            }
        });
        tv_commit_force.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TongJiUtil.getInstance().putEntries(TJKey.VERSION_UPDATE, MyEntry.getIns(TJKey.TYPE,"1"));
                tv_commit.performClick();
            }
        });
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isForce) {
                    TongJiUtil.getInstance().putEntries(TJKey.VERSION_UPDATE, MyEntry.getIns(TJKey.TYPE,"2"));
                }
                try {
                    String appPkg = context.getPackageName();
                    String marketPkg = "com.android.vending";
                    if (TextUtils.isEmpty(appPkg)) return;

                    Uri uri = Uri.parse("market://details?id=" + appPkg);
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(updateUrl)) {
                        uri = Uri.parse(updateUrl);
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (!TextUtils.isEmpty(marketPkg)) {
                        intent.setPackage(marketPkg);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //退出应用
                pop.dismiss();
                context.finish();
                System.exit(0);
            }
        });
        //设置popwindow显示位置
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);


        EventBus.getDefault().post(new POPWindow(true));
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new POPWindow(false));
            }
        });
    }

    public boolean popDismiss() {
        if (pop != null && pop.isShowing()) {
            if (!isForce) {
                pop.dismiss();
            }
            return true;
        }
        return false;
    }
    public void popDestroy(){
        if (pop != null){
            pop.dismiss();
        }
    }

    private String getAppInfo(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
            int versionCode = context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
            return pkName + "   " + versionName + "  " + versionCode;
        } catch (Exception e) {
        }
        return null;
    }
}
