package com.markLove.Xplan.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.markLove.Xplan.R;
import com.markLove.Xplan.base.App;
import com.markLove.Xplan.base.ui.BaseActivity;
import com.markLove.Xplan.bean.BaseBean;
import com.markLove.Xplan.bean.UserBean;
import com.markLove.Xplan.config.Constants;
import com.markLove.Xplan.manager.PushManager;
import com.markLove.Xplan.mvp.contract.MainContract;
import com.markLove.Xplan.mvp.presenter.MainPresenter;
import com.markLove.Xplan.ui.adapter.FragmentTabAdapter;
import com.markLove.Xplan.ui.dialog.ExitRoomDialog;
import com.markLove.Xplan.ui.fragment.GroupFragment;
import com.markLove.Xplan.ui.fragment.MineFragment;
import com.markLove.Xplan.ui.fragment.MsgFragment;
import com.markLove.Xplan.ui.fragment.SearchFragment;
import com.markLove.Xplan.utils.AMapClient;
import com.markLove.Xplan.utils.GsonUtils;
import com.markLove.Xplan.utils.LogUtils;
import com.markLove.Xplan.utils.PreferencesUtils;
import com.markLove.Xplan.utils.StatusBarUtil;
import com.networkengine.ConfigUtil;
import com.networkengine.controller.callback.ErrorResult;
import com.networkengine.controller.callback.XCallback;
import com.networkengine.database.table.Member;
import com.networkengine.engine.LogicEngine;
import com.networkengine.mqtt.MqttService;
import com.networkengine.util.CoracleSdk;
import com.xsimple.im.db.datatable.IMBoxMessage;
import com.xsimple.im.db.datatable.IMChat;
import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMOfficialMessage;
import com.xsimple.im.engine.IMEngine;
import com.xsimple.im.engine.LoginLogic;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View<BaseBean>, View.OnClickListener {
    private static String TAG = MainActivity.class.getName();
    private List<Fragment> fragments;
    public FragmentTabAdapter fragmentTabAdapter;
    private RadioGroup radioGroup;
    private AMapClient aMapClient;
    double latitude;
    double longitude;
    private MyHandler myHandler;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        fullScreen(this);
        StatusBarUtil.StatusBarLightModeAndFullscreen(this);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.rb_team);
        fragments = new ArrayList<Fragment>();
        fragments.add(new GroupFragment());
        fragments.add(new SearchFragment());
        fragments.add(new MsgFragment());
        fragments.add(new MineFragment());
        fragmentTabAdapter = new FragmentTabAdapter(this, fragments, R.id.frame, radioGroup);
        initMapClient();
        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(1,1000*40);
        login();
    }

    private void initMapClient(){
        aMapClient = new AMapClient(this);

        aMapClient.setLocationListener(new AMapClient.LocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                //获取纬度
                latitude = aMapLocation.getLatitude();
                //获取经度
                longitude = aMapLocation.getLongitude();
                //城市信息
                String city = aMapLocation.getCity();
//                LogUtils.i(TAG,"latitude="+latitude);
//                LogUtils.i(TAG,"longitude="+longitude);
//                LogUtils.i(TAG,"city="+city);
                App.getInstance().setaMapLocation(aMapLocation);
            }
        });
        startLocation();
    }

    @Override
    public MainPresenter onCreatePresenter() {
        return new MainPresenter();
    }

    /**
     * 启动定位
     */
    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager
                            .PERMISSION_GRANTED) {
                aMapClient.startLocation();
            } else{
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                }, Constants.REQUEST_CODE_PERMISSION_ONE);
            }
        } else {
            aMapClient.startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE_PERMISSION_ONE) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                int readResult = grantResults[1];
                //读写内存权限
                boolean readGranted = readResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!readGranted) {
                    size++;
                }
                int coarseLocationResult = grantResults[2];
                boolean coarseLocationGranted = coarseLocationResult == PackageManager.PERMISSION_GRANTED;
                if (!coarseLocationGranted) {
                    size++;
                }
                int fineLocationResult = grantResults[3];
                boolean fineLocationGranted = fineLocationResult == PackageManager.PERMISSION_GRANTED;
                if (!fineLocationGranted) {
                    size++;
                }

                int readPhoneResult = grantResults[4];
                boolean readPhoneGranted = readPhoneResult == PackageManager.PERMISSION_GRANTED;
                if (!readPhoneGranted) {
                    size++;
                }
                if (size == 0) {
                    aMapClient.startLocation();
                } else {
                    Toast.makeText(this, "没有定位权限-权限管理中开启", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what){
                case 1:
                    mActivity.get().updateLocation();
                    removeMessages(1);
                    //5分钟发一次
                    sendEmptyMessageDelayed(1,1000*60*5);
                    break;
            }
        }
    }

    private void updateLocation(){
        Map<String,String> map = new HashMap<>();
        map.put("userId",App.getInstance().getUserId());
        if (longitude != 0){
            map.put("longitude",String.valueOf(longitude));
            map.put("latitude",String.valueOf(latitude));
        }
        mPresenter.updateUserPlace(map);
    }

    @Override
    protected void onDestroy() {
        myHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void refreshUI(BaseBean baseBean) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 登录mchl平台
     */
    private void login() {
        UserBean userBean = App.getInstance().getUserBean();
        CoracleSdk.init(this);
        new LoginLogic(this).init(userBean.getUserInfo().getPhone(), "123456",String.valueOf(userBean.getUserInfo().getUserId()), new XCallback<Member, ErrorResult>() {
            @Override
            public void onSuccess(Member result) {
                PreferencesUtils.putString(getApplicationContext(),PreferencesUtils.KEY_USER_TOKEN,result.getUserToken());
                LogUtils.i("MainActivity","登录mchl成功=,member="+result.toString());

                UserBean userBean = App.getInstance().getUserBean();
                Member member = new Member();
                member.setId(String.valueOf(userBean.getUserInfo().getUserId()));
                member.setUserId(String.valueOf(userBean.getUserInfo().getUserId()));
                member.setUserName(userBean.getUserInfo().getNickName());
                member.setUserToken(result.getUserToken());
                LogicEngine.getInstance().setUser(member);
                initPushManager();
            }

            @Override
            public void onFail(ErrorResult result) {
                LogUtils.i("MainActivity","登录mchl失败"+result.toString());
                Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initPushManager() {
        //获取mqtt服务
        MqttService mqttService = LogicEngine.getInstance().getMqttService();
        //获取登陆用户id
        String userId = LogicEngine.getInstance().getUser().getId();
        //初始化消息管理器
        PushManager mPushManager = new PushManager(mqttService, ConfigUtil.getAppKey(), userId,MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog(){
        ExitRoomDialog exitRoomDialog = new ExitRoomDialog(this);
        exitRoomDialog.setOnDialogCallBack(new ExitRoomDialog.OnDialogCallBack() {
            @Override
            public void onCallBack(String content) {
                LogicEngine.getInstance().getSystemController().logout(true);
                finish();
            }
        });
        exitRoomDialog.show();
        exitRoomDialog.setTipContent(getString(R.string.tip_outlogin));
    }
}
