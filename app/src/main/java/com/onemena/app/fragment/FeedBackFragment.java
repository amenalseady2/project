package com.onemena.app.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.arabsada.news.R;
import com.onemena.app.adapter.FeedbackAdapter;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseFragment;
import com.onemena.listener.JsonArrayListener;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.service.PublicService;
import com.onemena.utils.SpUtil;
import com.onemena.utils.ToastUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.widght.HelveRomanTextView;

/**
 * Created by WHF on 2016-11-30.
 */

public class FeedBackFragment extends BaseFragment implements OnActionViewClickListener {

    private ListView list_view_back;
    private HelveRomanTextView txt_feedback_email;
    private FeedbackAdapter adapter;
    private ImageView back_iv;

    public FeedBackFragment(){}

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (SpUtil.getBoolean(SPKey.MODE,false)){
            return View.inflate(getContext(), R.layout.fragment_feedback_night,null);
        }else {
            return View.inflate(getContext(), R.layout.fragment_feedback,null);
        }

    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        back_iv = (ImageView) view.findViewById(R.id.back_iv);
        list_view_back = (ListView) view.findViewById(R.id.list_view_back);
        txt_feedback_email = (HelveRomanTextView) view.findViewById(R.id.txt_feedback_email);
        txt_feedback_email.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        adapter=new FeedbackAdapter(mContext);
        list_view_back.setAdapter(adapter);
        adapter.setOnActionViewClickListener(this);
        getFaqList();

    }

    private void getFaqList() {
        PublicService.getInstance().getJsonArrayRequest(false,0,ConfigUrls.HOST_FAQ + "?t=" + System.currentTimeMillis(), null, new JsonArrayListener() {
            @Override
            public void onJsonArray(int skip, JSONArray array, Boolean isCacheData) {
                try {
                    super.onJsonArray(skip, array, isCacheData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged(0,array);
            }

            @Override
            public void onJsonArrayError() {
                super.onJsonArrayError();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_feedback:
                Bundle bundle=new Bundle();
                bundle.putString(FeedBackWebFragment.URL,v.getTag()+"");
                FeedBackWebFragment fragment=new FeedBackWebFragment();
                fragment.setArguments(bundle);
                addToBackStack(fragment);
                TongJiUtil.getInstance().putEntries(TJKey.UC_CHECK_FAQ);
                break;
            case R.id.back_iv:
                popBackStack();
                break;
            case R.id.txt_feedback_email:
//                Intent data=new Intent(Intent.ACTION_SENDTO);
//                data.setData(Uri.parse("mailto:"+getActivity().getResources().getString(R.string.feedback_link)));
//                data.putExtra(Intent.EXTRA_SUBJECT, "");
//                data.putExtra(Intent.EXTRA_TEXT, "");
//                getActivity().startActivity(data);
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(getActivity().getResources().getString(R.string.feedback_link));
                ToastUtil.showNormalShortToast("تم نسخ عنوان البريد، يمكنك الآن مراسلتنا عن طريق البريد");
                TongJiUtil.getInstance().putEntries(TJKey.UC_FEEDBACK_COPYEMAIL);
                break;

        }
    }

}
