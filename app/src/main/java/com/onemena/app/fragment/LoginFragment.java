package com.onemena.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arabsada.news.R;
import com.onemena.base.BaseFragment;

/**
 * Created by WHF on 2016-11-30.
 */

public class LoginFragment extends BaseFragment {

    public LoginFragment(){}

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_login,null);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
//        final TextView user_name = (TextView) view.findViewById(R.id.txt_name);
//        final TextView txt_password = (TextView) view.findViewById(R.id.txt_password);
//        TextView login_btn = (TextView) view.findViewById(R.id.login_btn);
//        login_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                register(user_name.getText()+"",txt_password.getText()+"");
//            }
//        });
    }




}
