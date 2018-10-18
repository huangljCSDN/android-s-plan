package com.markLove.Xplan.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cjt2325.cameralibrary.util.LogUtil;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.mvp.BasePresenter;
import com.markLove.Xplan.base.ui.BaseFragment;
import com.markLove.Xplan.bean.PeopleBean;
import com.markLove.Xplan.bean.ShopItemBean;
import com.markLove.Xplan.module.CallBackTest;
import com.markLove.Xplan.module.CircleRecyclerView.CircleRecyclerView;
import com.markLove.Xplan.module.CircleRecyclerView.CircularViewMode;
import com.markLove.Xplan.module.CircleRecyclerView.ItemViewMode;
import com.markLove.Xplan.ui.adapter.PeoplesAdapter;
import com.markLove.Xplan.ui.adapter.ShopChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class FindFragment extends BaseFragment implements View.OnClickListener {

    private CircleRecyclerView mCircleRecyclerView2;
    private CircleRecyclerView mCircleRecyclerView;
    private ItemViewMode mItemViewMode;
    private ItemViewMode mItemViewMode2;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager mLayoutManager2;
    private boolean mIsNotLoop;
    private int currentPositionOne;
    private int currentPositionTwo;
    private Button btnAll,btnBoy,btnGirl;

    @Override
    public BasePresenter onCreatePresenter() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_find;
    }

    @Override
    protected void init(View view) {
        btnAll = view.findViewById(R.id.btn_all);
        btnBoy = view.findViewById(R.id.btn_boy);
        btnGirl = view.findViewById(R.id.btn_girl);

        btnAll.setOnClickListener(this);
        btnBoy.setOnClickListener(this);
        btnGirl.setOnClickListener(this);

        btnAll.setSelected(true);
        btnBoy.setSelected(false);
        btnGirl.setSelected(false);

        initCrOne(view);
        initCrTwo(view);
    }

    private void initCrOne(View view){
        mItemViewMode = new CircularViewMode();
        ((CircularViewMode)mItemViewMode).setxOffset(200);

        mCircleRecyclerView = (CircleRecyclerView) view.findViewById(R.id.cr_one);
        mLayoutManager = new LinearLayoutManager(getContext());
        mCircleRecyclerView.setLayoutManager(mLayoutManager);
        mCircleRecyclerView.setViewMode(mItemViewMode);
        mCircleRecyclerView.setNeedCenterForce(true);
        mCircleRecyclerView.setNeedLoop(!mIsNotLoop);

        mCircleRecyclerView.setOnCenterItemClickListener(new CircleRecyclerView.OnCenterItemClickListener() {
            @Override
            public void onCenterItemClick(View v) {
                Toast.makeText(getContext(), "Center Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        ((CircularViewMode) mItemViewMode).setOnScrollCenterListener(onScrollCenterListener1);
        ShopChatAdapter shopChatAdapter = new ShopChatAdapter(getContext(),getData2());
        mCircleRecyclerView.setAdapter(shopChatAdapter);
    }

    private void initCrTwo(View view){
        mItemViewMode2 = new CircularViewMode();
        ((CircularViewMode)mItemViewMode2).setxOffset(600);

        mCircleRecyclerView2 = (CircleRecyclerView) view.findViewById(R.id.cr_two);
        mLayoutManager2 = new LinearLayoutManager(getContext());
        mCircleRecyclerView2.setLayoutManager(mLayoutManager2);
        mCircleRecyclerView2.setViewMode(mItemViewMode2);
        mCircleRecyclerView2.setNeedCenterForce(true);
        mCircleRecyclerView2.setNeedLoop(!mIsNotLoop);

        mCircleRecyclerView2.setOnCenterItemClickListener(new CircleRecyclerView.OnCenterItemClickListener() {
            @Override
            public void onCenterItemClick(View v) {
                Toast.makeText(getContext(), "Center Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        ((CircularViewMode) mItemViewMode2).setOnScrollCenterListener(onScrollCenterListener2);
        PeoplesAdapter peoplesAdapter = new PeoplesAdapter(getContext(),getData());
//        peoplesAdapter.setData(getData());
        mCircleRecyclerView2.setAdapter(peoplesAdapter);
    }

    private List<PeopleBean> getData(){
        List<PeopleBean> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            PeopleBean peopleBean = new PeopleBean("张三"+i,10,1,System.currentTimeMillis(),"");
            list.add(peopleBean);
        }
        return list;
    }

    private List<ShopItemBean> getData2(){
        List<ShopItemBean> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            ShopItemBean shopItemBean = new ShopItemBean(11111,"水煮活鱼","100人推荐",null);
            list.add(shopItemBean);
        }
        return list;
    }

    CircularViewMode.OnScrollCenterListener onScrollCenterListener1 = new CircularViewMode.OnScrollCenterListener() {
        @Override
        public void onCenterView(View view) {
            LogUtil.i("huang","view.getTag() 1 =="+view.getTag());
            int currentPosition = (int)view.getTag();
            if (currentPositionOne != currentPosition){
                currentPositionOne = currentPosition;
            }
        }
    };

    CircularViewMode.OnScrollCenterListener onScrollCenterListener2 = new CircularViewMode.OnScrollCenterListener() {
        @Override
        public void onCenterView(View view) {
            LogUtil.i("huang","view.getTag() 2 =="+view.getTag());
            int currentPosition = (int)view.getTag();
            if (currentPositionTwo != currentPosition){
                currentPositionTwo = currentPosition;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_all:
                btnAll.setSelected(true);
                btnGirl.setSelected(false);
                btnBoy.setSelected(false);
                break;
            case R.id.btn_boy:
                btnBoy.setSelected(true);
                btnGirl.setSelected(false);
                btnAll.setSelected(false);
                break;
            case R.id.btn_girl:
                btnGirl.setSelected(true);
                btnAll.setSelected(false);
                btnBoy.setSelected(false);
                break;
        }

    }
}
