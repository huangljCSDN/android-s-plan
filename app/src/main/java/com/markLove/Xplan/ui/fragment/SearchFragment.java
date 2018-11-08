package com.markLove.Xplan.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.App;
import com.markLove.Xplan.base.BaseJsInterface;
import com.markLove.Xplan.base.ui.BaseFragment;
import com.markLove.Xplan.bean.MerchantBean;
import com.markLove.Xplan.bean.NearUserBean;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.module.CircleRecyclerView.CircleRecyclerView;
import com.markLove.Xplan.module.CircleRecyclerView.CircularViewMode;
import com.markLove.Xplan.module.CircleRecyclerView.ItemViewMode;
import com.markLove.Xplan.mvp.contract.SearchContract;
import com.markLove.Xplan.mvp.presenter.SearchPresenter;
import com.markLove.Xplan.ui.activity.LoverActivity;
import com.markLove.Xplan.ui.activity.MerchantInfoActivity;
import com.markLove.Xplan.ui.activity.PlayersActivity;
import com.markLove.Xplan.ui.activity.UserInfoActivity;
import com.markLove.Xplan.ui.activity.WebViewActivity;
import com.markLove.Xplan.ui.adapter.MerchantListAdapter;
import com.markLove.Xplan.ui.adapter.UserListAdapter;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.PreferencesUtils;
import com.markLove.Xplan.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends BaseFragment<SearchPresenter> implements View.OnClickListener, SearchContract.View {

    private CircleRecyclerView mCircleRecyclerView2;
    private CircleRecyclerView mCircleRecyclerView;
    private ItemViewMode mItemViewMode;
    private ItemViewMode mItemViewMode2;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager mLayoutManager2;
    private boolean mIsNotLoop;
    private int currentPositionOne;
    private int currentPositionTwo;
    private Button btnAll, btnBoy, btnGirl;
    private UserBean meUserBean;
    private NearUserBean centerUser;
    private MerchantBean centerMerchant;
    private List<NearUserBean> userBeanList = new ArrayList<>();
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

    private String token;
    private boolean isTouchUserList = false;
    private boolean isTouchMerchantList = false;
    private boolean isTouchAll = true;
    private boolean isRefreshUserList;
    private boolean isRefreshMerchantList;
    private boolean isSearchMeNearUser = true;

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


        String userInfo = PreferencesUtils.getString(getContext(), PreferencesUtils.KEY_USER);
        meUserBean = GsonUtils.json2Bean(userInfo, UserBean.class);
        token = meUserBean.getToken();
        setListener();
        getNearUser(meUserBean.getUserInfo());
    }

    private void setListener() {
        refreshLayoutMerchant.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (centerUser != null){
                    isRefreshMerchantList = true;
                    refreshLayoutUser.setEnabled(false);
                    getMerchantList(centerUser.getUserId() + "");
                } else {
//                    Toast.makeText(getContext(),"请选中")
                    refreshLayoutMerchant.setRefreshing(false);
                }
            }
        });

        refreshLayoutUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (centerMerchant != null){
                    isRefreshUserList = true;
                    refreshLayoutMerchant.setEnabled(false);
                    if (isSearchMeNearUser) {
                        getNearUser(meUserBean.getUserInfo());
                    } else {
                        getMerchantUserList(centerMerchant);
                    }
                } else {
                    refreshLayoutUser.setRefreshing(false);
                }

            }
        });
    }

    /**
     * 添加两条不可见的假数据
     */
    private void addNullData() {
        for (int i = 0; i < 2; i++) {
            NearUserBean nearUserBean = new NearUserBean();
            userBeanList.add(nearUserBean);
        }

        for (int i = 0; i < 2; i++) {
            MerchantBean merchantBean = new MerchantBean();
            merchantBeanList.add(merchantBean);
        }
    }

    /**
     * 获取店铺列表
     *
     * @param userId
     */
    private void getMerchantList(String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("page", String.valueOf(currentMerchantPage));
        map.put("rows", String.valueOf(DEFAULT_ROWS));
        mPresenter.getNearMerchant(map);
    }

    /**
     * 获取附近的人列表
     *
     * @param userBean
     */
    private void getNearUser(UserBean.UserInfoEntity userBean) {
        AMapLocation aMapLocation = App.getInstance().getaMapLocation();
        Map<String, String> map = new HashMap<>();
        map.put("userId", userBean.getUserId() + "");
        map.put("page", String.valueOf(currentUserPage));
        if (aMapLocation != null) {
            map.put("longitude", String.valueOf(aMapLocation.getLongitude()));
            map.put("latitude", String.valueOf(aMapLocation.getLatitude()));
        }
        map.put("sex", String.valueOf(selectType));
        map.put("rows", String.valueOf(DEFAULT_ROWS));
        mPresenter.getNearUser(map);
    }

    /**
     * 获取店里的人列表
     *
     * @param merchantBean
     */
    private void getMerchantUserList(MerchantBean merchantBean) {
        Map<String, String> map = new HashMap<>();
        map.put("merchantId", String.valueOf(merchantBean.getMerchantId()));
        map.put("sex", String.valueOf(selectType));
        map.put("page", String.valueOf(currentUserPage));
        map.put("rows", String.valueOf(DEFAULT_ROWS));
        mPresenter.getMerchantUserList(map);
    }

    private void initCrMerchant(View view) {
        mItemViewMode = new CircularViewMode();
        ((CircularViewMode) mItemViewMode).setxOffset(450);
        ((CircularViewMode) mItemViewMode).setOffset11(100);

        mCircleRecyclerView = (CircleRecyclerView) view.findViewById(R.id.cr_one);
        mLayoutManager = new LinearLayoutManager(getContext());
        mCircleRecyclerView.setLayoutManager(mLayoutManager);
        mCircleRecyclerView.setViewMode(mItemViewMode);
        mCircleRecyclerView.setNeedCenterForce(true);
        mCircleRecyclerView.setNeedLoop(!mIsNotLoop);

        ((CircularViewMode) mItemViewMode).setOnScrollCenterListener(onScrollCenterListenerMerchant);
        merchantListAdapter = new MerchantListAdapter(getContext(), merchantBeanList);

        merchantListAdapter.setOnItemClickListener(new MerchantListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MerchantBean merchantBean = merchantBeanList.get(position);
                Intent intent = new Intent(getContext(), MerchantInfoActivity.class);
//                Intent intent = new Intent(getContext(), ShopChatTestActivity.class);
                intent.putExtra("chatId", merchantBean.getMerchantId());
                startActivity(intent);
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

    private void initCrUser(View view) {
        mItemViewMode2 = new CircularViewMode();
        ((CircularViewMode) mItemViewMode2).setxOffset(10);
        ((CircularViewMode) mItemViewMode2).setOffset11(250);
        mCircleRecyclerView2 = (CircleRecyclerView) view.findViewById(R.id.cr_two);
        mLayoutManager2 = new LinearLayoutManager(getContext());
        mCircleRecyclerView2.setLayoutManager(mLayoutManager2);
        mCircleRecyclerView2.setViewMode(mItemViewMode2);
        mCircleRecyclerView2.setNeedCenterForce(true);
        mCircleRecyclerView2.setNeedLoop(!mIsNotLoop);

        ((CircularViewMode) mItemViewMode2).setOnScrollCenterListener(onScrollCenterListenerUser);
        userListAdapter = new UserListAdapter(getContext(), new ArrayList<NearUserBean>());
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

        userListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NearUserBean nearUserBean = userBeanList.get(position);
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                intent.putExtra("userId", nearUserBean.getUserId()+"");
                startActivity(intent);
            }
        });

    }

    CircularViewMode.OnScrollCenterListener onScrollCenterListenerMerchant = new CircularViewMode.OnScrollCenterListener() {
        @Override
        public void onCenterView(View view) {
            LogUtil.i("huang", "view.getTag() 1 ==" + view.getTag());
            int currentPosition = (int) view.getTag();
            if (currentPositionOne != currentPosition) {
                currentPositionOne = currentPosition;
                centerMerchant = merchantBeanList.get(currentPosition);
                if (isTouchMerchantList) {
                    currentUserPage = 1;
                    getMerchantUserList(centerMerchant);
                    isSearchMeNearUser = false;
                    isRefreshUserList = false;
                }
            }
        }
    };

    CircularViewMode.OnScrollCenterListener onScrollCenterListenerUser = new CircularViewMode.OnScrollCenterListener() {
        @Override
        public void onCenterView(View view) {
            LogUtil.i("huang", "view.getTag() 2 ==" + view.getTag());
            int currentPosition = (int) view.getTag();
            if (currentPositionTwo != currentPosition) {
                currentPositionTwo = currentPosition;
                if (isTouchAll || isTouchUserList) {
                    currentMerchantPage = 1;
                    centerUser = userBeanList.get(currentPosition);
                    getMerchantList(centerUser.getUserId() + "");
                    isRefreshMerchantList = false;
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_all:
                isSearchMeNearUser = true;
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

    private void startPlayActivity() {
//        Intent intent = new Intent(getContext(), GoodPlayActivity.class);
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url", BaseJsInterface.PLAY_INFO_URL);
        intent.putExtra("isAll",true);
        startActivity(intent);
    }

    private void startPlayerActivity() {
        Intent intent = new Intent(getContext(), PlayersActivity.class);
        startActivity(intent);
    }

    private void startLoveActivity() {
        Intent intent = new Intent(getContext(), LoverActivity.class);
        startActivity(intent);
    }

    @Override
    public void refreshMerchantList(ArrayList<MerchantBean> list) {
        refreshLayoutMerchant.setRefreshing(false);
        refreshLayoutUser.setEnabled(true);
        if (list != null && !list.isEmpty()) {
            if(currentMerchantPage == 0){
                merchantBeanList.clear();
            }
            currentMerchantPage += 1;
            int size = list.size();
            for (int i = size - 1; i >= 0; i--) {
                merchantBeanList.add(0, list.get(i));
            }
            merchantListAdapter.setData(merchantBeanList);
            merchantListAdapter.notifyDataSetChanged();
            if (!isRefreshMerchantList) {
                mCircleRecyclerView.scrollToPosition(merchantListAdapter.getItemCount() - 1);
            }
        } else {
            if (currentMerchantPage == 1){
                merchantBeanList.clear();
                merchantListAdapter.setData(merchantBeanList);
                merchantListAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showLong(getContext(),"没有更多数据了");
            }
        }
    }

    @Override
    public void refreshUserList(ArrayList<NearUserBean> list) {
        refreshLayoutUser.setRefreshing(false);
        refreshLayoutMerchant.setEnabled(true);
        if (list != null && !list.isEmpty()) {
            if (currentUserPage == 0){
                userBeanList.clear();
                if (isTouchAll){
                    merchantBeanList.clear();
                }
            }
            currentUserPage += 1;
            int size = list.size();
            for (int i = size - 1; i >= 0; i--) {
                userBeanList.add(0, list.get(i));
            }
            userListAdapter.setData(userBeanList);
            userListAdapter.notifyDataSetChanged();
            if (!isRefreshUserList) {
                mCircleRecyclerView2.scrollToPosition(userListAdapter.getItemCount() - 1);
            }
        } else {
            if (currentUserPage == 1) {
                userBeanList.clear();
                userListAdapter.setData(userBeanList);
                userListAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showLong(getContext(),"没有更多数据了");
            }
        }
    }

    @Override
    public void showError(String error) {
        super.showError(error);
        refreshLayoutUser.setRefreshing(false);
        refreshLayoutMerchant.setRefreshing(false);
    }
}
