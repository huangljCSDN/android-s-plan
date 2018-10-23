package com.markLove.Xplan.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.App;
import com.markLove.Xplan.base.ui.BaseFragment;
import com.markLove.Xplan.bean.MerchantBean;
import com.markLove.Xplan.bean.NearUserBean;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.module.CircleRecyclerView.CircleRecyclerView;
import com.markLove.Xplan.module.CircleRecyclerView.CircularViewMode;
import com.markLove.Xplan.module.CircleRecyclerView.ItemViewMode;
import com.markLove.Xplan.mvp.contract.SearchContract;
import com.markLove.Xplan.mvp.presenter.SearchPresenter;
import com.markLove.Xplan.ui.activity.GoodPlayActivity;
import com.markLove.Xplan.ui.activity.LoverActivity;
import com.markLove.Xplan.ui.activity.PlayersActivity;
import com.markLove.Xplan.ui.adapter.MerchantListAdapter;
import com.markLove.Xplan.ui.adapter.UserListAdapter;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends BaseFragment<SearchPresenter> implements View.OnClickListener,SearchContract.View {

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
    private UserBean meUserBean;
    private NearUserBean.NearUserEntity centerUser;
    private MerchantBean centerMerchant;
    private List<NearUserBean.NearUserEntity> userBeanList = new ArrayList<>();
    private List<MerchantBean> merchantBeanList = new ArrayList<>();
    private UserListAdapter userListAdapter;
    private MerchantListAdapter merchantListAdapter;
    private SwipeRefreshLayout refreshLayoutMerchant;
    private SwipeRefreshLayout refreshLayoutUser;
    // 0：全部 1：男 2：女
    private int selectType;
    private int currentUserPage = 1;
    private int currentMerchantPage = 1;
    private static final int DEFAULT_ROWS = 10;
    //是否是刷新我附近的人
    private boolean isSearchMeNearUser;
    //是否是刷新店铺里的人
    private boolean isSearchMerchantUser;
    private boolean isOnlyRefreshMerchant;
    private String token;
    private boolean isTouchUserList = false;
    private boolean isTouchMerchantList = false;
    private boolean isTouchAll = true;

    @Override
    public SearchPresenter onCreatePresenter() {
        return new SearchPresenter();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_find;
    }

    @Override
    protected void init(View view) {
        btnAll = view.findViewById(R.id.btn_all);
        refreshLayoutMerchant = view.findViewById(R.id.refresh_layout_one);
        refreshLayoutUser = view.findViewById(R.id.refresh_layout_two);

        btnAll.setOnClickListener(this);
        view.findViewById(R.id.tv_play).setOnClickListener(this);
        view.findViewById(R.id.tv_players).setOnClickListener(this);
        view.findViewById(R.id.tv_find_cp).setOnClickListener(this);

        btnAll.setSelected(true);

        initCrMerchant(view);
        initCrUser(view);


        String userInfo = PreferencesUtils.getString(getContext(),PreferencesUtils.KEY_USER);
        meUserBean = GsonUtils.json2Bean(userInfo,UserBean.class);
        token = meUserBean.getToken();
        isSearchMeNearUser = true;
        isOnlyRefreshMerchant = true;
        setListener();
        getNearUser(meUserBean.getUserInfo());
    }

    private void setListener(){
        refreshLayoutMerchant.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayoutUser.setEnabled(false);
                getMerchantList(centerUser.getUserId()+"");
            }
        });

        refreshLayoutUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayoutMerchant.setEnabled(false);
                if (isSearchMeNearUser){
                    getNearUser(meUserBean.getUserInfo());
                } else {
                    getMerchantList(centerMerchant.id+"");
                }
            }
        });
    }

    /**
     * 获取店铺列表
     * @param userId
     */
    private void getMerchantList(String userId){
        Map<String,String> map = new HashMap<>();
        map.put("userId",userId);
        map.put("page",String.valueOf(currentMerchantPage));
        map.put("rows",String.valueOf(DEFAULT_ROWS));
        mPresenter.getNearMerchant(map);
    }

    /**
     * 获取附近的人列表
     * @param userBean
     */
    private void getNearUser(UserBean.UserInfoEntity userBean){
        AMapLocation aMapLocation = App.getInstance().getaMapLocation();
        Map<String,String> map = new HashMap<>();
        map.put("userId",userBean.getUserId()+"");
        map.put("page",String.valueOf(currentUserPage));
        if (aMapLocation != null){
            map.put("longitude",String.valueOf(aMapLocation.getLongitude()));
            map.put("latitude",String.valueOf(aMapLocation.getLatitude()));
        }
        map.put("sex",String.valueOf(selectType));
        map.put("rows",String.valueOf(DEFAULT_ROWS));
        mPresenter.getNearUser(map);
    }

    /**
     * 获取店里的人列表
     * @param merchantBean
     */
    private void getMerchantUserList(MerchantBean merchantBean){
        Map<String,String> map = new HashMap<>();
        map.put("merchantId","");
        map.put("sex",String.valueOf(selectType));
        map.put("page",String.valueOf(currentUserPage));
        map.put("rows",String.valueOf(DEFAULT_ROWS));
        mPresenter.getMerchantUserList(map);
    }

    private void initCrMerchant(View view){
        mItemViewMode = new CircularViewMode();
        ((CircularViewMode)mItemViewMode).setxOffset(450);

        mCircleRecyclerView = (CircleRecyclerView) view.findViewById(R.id.cr_one);
        mLayoutManager = new LinearLayoutManager(getContext());
        mCircleRecyclerView.setLayoutManager(mLayoutManager);
        mCircleRecyclerView.setViewMode(mItemViewMode);
        mCircleRecyclerView.setNeedCenterForce(true);
        mCircleRecyclerView.setNeedLoop(!mIsNotLoop);

//        mCircleRecyclerView.setOnCenterItemClickListener(new CircleRecyclerView.OnCenterItemClickListener() {
//            @Override
//            public void onCenterItemClick(View v) {
//                Toast.makeText(getContext(), "Center Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

        ((CircularViewMode) mItemViewMode).setOnScrollCenterListener(onScrollCenterListenerMerchant);
        merchantListAdapter = new MerchantListAdapter(getContext(),merchantBeanList);

        merchantListAdapter.setOnItemClickListener(new MerchantListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                UserBean.UserInfoEntity nearUser = userBeanList.get(position);
            }
        });
        mCircleRecyclerView.setAdapter(merchantListAdapter);

        mCircleRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchMerchantList = true;
                isTouchUserList = false;
                isTouchAll = false;
                return false;
            }
        });
    }

    private void initCrUser(View view){
        mItemViewMode2 = new CircularViewMode();
        ((CircularViewMode)mItemViewMode2).setxOffset(100);
        ((CircularViewMode)mItemViewMode2).setOffset11(150);
        mCircleRecyclerView2 = (CircleRecyclerView) view.findViewById(R.id.cr_two);
        mLayoutManager2 = new LinearLayoutManager(getContext());
        mCircleRecyclerView2.setLayoutManager(mLayoutManager2);
        mCircleRecyclerView2.setViewMode(mItemViewMode2);
        mCircleRecyclerView2.setNeedCenterForce(true);
        mCircleRecyclerView2.setNeedLoop(!mIsNotLoop);

        ((CircularViewMode) mItemViewMode2).setOnScrollCenterListener(onScrollCenterListenerUser);
        userListAdapter = new UserListAdapter(getContext(),new ArrayList<NearUserBean.NearUserEntity>());
//        userListAdapter.setData(getData());
        mCircleRecyclerView2.setAdapter(userListAdapter);
        mCircleRecyclerView2.scrollToPosition(userListAdapter.getItemCount() - 1);
        mCircleRecyclerView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchUserList = true;
                isTouchMerchantList = false;
                isTouchAll = false;
                return false;
            }
        });

    }

    CircularViewMode.OnScrollCenterListener onScrollCenterListenerMerchant = new CircularViewMode.OnScrollCenterListener() {
        @Override
        public void onCenterView(View view) {
            LogUtil.i("huang","view.getTag() 1 =="+view.getTag());
            int currentPosition = (int)view.getTag();
            if (currentPositionOne != currentPosition){
                currentPositionOne = currentPosition;
                centerMerchant = merchantBeanList.get(currentPosition);
                if (isTouchMerchantList){
                    currentUserPage = 1;
                    getMerchantUserList(centerMerchant);
                }
                isOnlyRefreshMerchant = false;
            }
        }
    };

    CircularViewMode.OnScrollCenterListener onScrollCenterListenerUser = new CircularViewMode.OnScrollCenterListener() {
        @Override
        public void onCenterView(View view) {
            LogUtil.i("huang","view.getTag() 2 =="+view.getTag());
            int currentPosition = (int)view.getTag();
            if (currentPositionTwo != currentPosition){
                currentPositionTwo = currentPosition;
                if (isTouchAll || isTouchUserList){
                    currentMerchantPage = 1;
                    centerUser = userBeanList.get(currentPosition);
                    getMerchantList(centerUser.getUserId()+"");
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_all:
//                isSearchMeNearUser = true;
                isTouchAll = true;
                isTouchUserList = false;
                isTouchMerchantList = false;
                selectType = 0;
                currentUserPage = 1;
                currentMerchantPage = 1;
                getNearUser(meUserBean.getUserInfo());
                break;
            case R.id.tv_players:
                startPlayerActivity();
                break;
            case R.id.tv_play:
                startPlayActivity();
                break;
            case R.id.tv_find_cp:
                startLoveActivity();
                break;
        }
    }

    private void startPlayActivity(){
        Intent intent = new Intent(getContext(), GoodPlayActivity.class);
        startActivity(intent);
    }

    private void startPlayerActivity(){
        Intent intent = new Intent(getContext(), PlayersActivity.class);
        startActivity(intent);
    }

    private void startLoveActivity(){
        Intent intent = new Intent(getContext(), LoverActivity.class);
        startActivity(intent);
    }

    @Override
    public void refreshMerchantList(String json) {
        currentMerchantPage +=1;
        refreshLayoutUser.setEnabled(true);
        refreshLayoutMerchant.setRefreshing(false);
        if (!TextUtils.isEmpty(json)){
            merchantListAdapter.setData(merchantBeanList);
            merchantListAdapter.notifyDataSetChanged();
            int size = merchantBeanList.size();
            for (int i = size -1;i >= 0;i--){
                MerchantBean merchantBean = new MerchantBean(11111,"水煮活鱼","100人推荐",null);
                merchantBeanList.add(0,merchantBean);
            }
        }
    }

    @Override
    public void refreshUserList(String json) {
        currentUserPage +=1;
        refreshLayoutMerchant.setEnabled(true);
        refreshLayoutUser.setRefreshing(false);
        if (!TextUtils.isEmpty(json)){
            NearUserBean nearUserBean = GsonUtils.json2Bean(json,NearUserBean.class);
            int size = nearUserBean.getData().size();
            for (int i = size -1;i >= 0;i--){
                userBeanList.add(0,nearUserBean.getData().get(i));
            }
            userListAdapter.setData(userBeanList);
            userListAdapter.notifyDataSetChanged();
            mCircleRecyclerView2.scrollToPosition(userListAdapter.getItemCount() -1);
        }
    }
}
