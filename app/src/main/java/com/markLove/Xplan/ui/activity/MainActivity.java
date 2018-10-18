package com.markLove.Xplan.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.markLove.Xplan.R;
import com.markLove.Xplan.ui.adapter.FragmentTabAdapter;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.PostQueryInfo;
import com.markLove.Xplan.mvp.contract.MainContract;
import com.markLove.Xplan.ui.fragment.FindFragment;
import com.markLove.Xplan.ui.fragment.MineFragment;
import com.markLove.Xplan.ui.fragment.MsgFragment;
import com.markLove.Xplan.ui.fragment.TeamFragment;
import com.markLove.Xplan.mvp.presenter.MainPresenter;
import com.markLove.Xplan.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View<PostQueryInfo>,View.OnClickListener{

    private List<Fragment> fragments;
    public FragmentTabAdapter fragmentTabAdapter;
    private RadioGroup radioGroup;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fullScreen(this);
        StatusBarUtil.StatusBarLightMode(this);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.rb_team);
        fragments = new ArrayList<Fragment>();
        fragments.add(new TeamFragment());
        fragments.add(new FindFragment());
        fragments.add(new MsgFragment());
        fragments.add(new MineFragment());
        fragmentTabAdapter = new FragmentTabAdapter(this, fragments, R.id.frame, radioGroup);
    }


    @Override
    public MainPresenter onCreatePresenter() {
        return new MainPresenter();
    }


    @Override
    public void refreshUI(PostQueryInfo postQueryInfo) {
        Log.i("MainActivity",postQueryInfo.toString());
    }

    @Override
    public void onClick(View v) {

    }
}
