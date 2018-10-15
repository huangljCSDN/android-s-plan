package com.markLove.xplan.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.markLove.xplan.R;
import com.markLove.xplan.ui.activity.ShopChatActivity;
import com.markLove.xplan.base.mvp.BasePresenter;
import com.markLove.xplan.base.ui.BaseFragment;

public class MsgFragment extends BaseFragment {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void init(View view) {
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShopChatActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
