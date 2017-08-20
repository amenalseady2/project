package com.onemena.widght;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.arabsada.news.R;
import com.onemena.app.config.SPKey;
import com.onemena.data.eventbus.POPWindow;
import com.onemena.utils.SpUtil;

import de.greenrobot.event.EventBus;


/**
 * Created by whf on 2016/11/07.
 */
public class PopwinLongin {
    public Context context;
    public View ib;
    final View contentView;
    private PopupWindow popupWindow;
    public PopwinLongin(Context context, View ib,View.OnClickListener listener) {

        EventBus.getDefault().post(new POPWindow(true));

        this.context = context;
        this.ib = ib;
        if (SpUtil.getBoolean(SPKey.MODE,false)){
            contentView = LayoutInflater.from(context).inflate(R.layout.popwin_login_night, null);
        }else {
            contentView = LayoutInflater.from(context).inflate(R.layout.popwin_login, null);
        }

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new POPWindow(false));
            }
        });

        int[] screen_pos = new int[2];
        ib.getLocationOnScreen(screen_pos);
        Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0] + ib.getWidth(), screen_pos[1] + ib.getHeight());
        int contentViewWidth = contentView.getMeasuredWidth();
        int contentViewHeight = contentView.getMeasuredHeight();
        int position_x = anchor_rect.centerX() - (contentViewWidth / 2);
        int position_y = anchor_rect.bottom - (anchor_rect.height() / 2);
        // 设置好参数之后再show
        ImageView iv_dismiss = (ImageView) contentView.findViewById(R.id.iv_dismiss);
        TextView tv_facebook_login = (TextView) contentView.findViewById(R.id.tv_facebook_login);
        TextView tv_tiwwer_login = (TextView) contentView.findViewById(R.id.tv_tiwwer_login);
        TextView tv_google_login = (TextView) contentView.findViewById(R.id.tv_google_login);
        iv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        tv_facebook_login.setOnClickListener(listener);
        tv_tiwwer_login.setOnClickListener(listener);
        tv_google_login.setOnClickListener(listener);
        popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(ib, Gravity.BOTTOM, position_x, contentViewHeight);
    }
    public void dismiss(){
        popupWindow.dismiss();
        EventBus.getDefault().post(new POPWindow(false));
    }
}
