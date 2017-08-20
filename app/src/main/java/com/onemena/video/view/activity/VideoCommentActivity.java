package com.onemena.video.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.arabsada.news.R;
import com.onemena.base.BaseActicity;
import com.onemena.app.fragment.CommentFragment;

/**
 * Created by Administrator on 2017/1/6.
 */

public class VideoCommentActivity extends BaseActicity {

    private CommentFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_comment);
        fragment = CommentFragment.getInstance();
        Intent intent = getIntent();
        Bundle bundle_s = new Bundle();
        bundle_s.putString(CommentFragment.ID, intent.getStringExtra("id"));
        bundle_s.putInt(CommentFragment.FLAG, 1);
        bundle_s.putString(CommentFragment.CATEGORY_ID, intent.getStringExtra("category_id"));
        bundle_s.putString(CommentFragment.TITLE, intent.getStringExtra("title"));
        bundle_s.putInt(CommentFragment.COMMENT_COUNT, intent.getIntExtra("comment_count",-1));

        fragment.setArguments(bundle_s);
        fragment.setOnStateChangeListener((CommentFragment.StateChangeListener) intent.getSerializableExtra("listener"));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();
    }


    @Override
    public void finish() {
        Intent mIntent = new Intent();
        mIntent.putExtra("comment_count", fragment.getComment_count_num());
        // 设置结果，并进行传送
        setResult(0, mIntent);
        super.finish();
    }
}
