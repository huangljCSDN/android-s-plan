package com.markLove.Xplan.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.markLove.Xplan.R;
import com.markLove.Xplan.ui.activity.GroupChatActivity;
import com.markLove.Xplan.ui.activity.ShopChatActivity;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseFragment;

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

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GroupChatActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }
}
