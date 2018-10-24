package com.markLove.Xplan.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.dmcbig.mediapicker.view.PreviewFragment;
import com.markLove.Xplan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanglingjun on 2018/10/13.
 */

public class PublishPreviewActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    ViewPager viewpager;
    TextView bar_title;
    ArrayList<Media> preRawList;
    private TextView tvCount;
    private ImageView icDelete;
    private int currentPosition;
    private ArrayList<Fragment> fragmentArrayList;
    private AdapterFragment adapterFragment;
    private int totalSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_preview);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.iv_delete).setOnClickListener(this);
        bar_title = (TextView) findViewById(R.id.bar_title);
        tvCount = findViewById(R.id.tv_count);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        icDelete = findViewById(R.id.iv_delete);
        preRawList = getIntent().getParcelableArrayListExtra(PickerConfig.PRE_RAW_LIST);
        totalSize = preRawList.size();
        setView(preRawList);
    }

    void setView(ArrayList<Media> default_list) {
        tvCount.setText(1 + "/" + totalSize);
        fragmentArrayList = new ArrayList<>();
        for (Media media : default_list) {
            fragmentArrayList.add(PreviewFragment.newInstance(media, ""));
        }
        adapterFragment = new AdapterFragment(getSupportFragmentManager(), fragmentArrayList);
        viewpager.setAdapter(adapterFragment);
        viewpager.addOnPageChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            done(preRawList, PickerConfig.RESULT_UPDATE_CODE);
        } else if (id == R.id.iv_delete) {
            fragmentArrayList.remove(currentPosition);
            preRawList.remove(currentPosition);
            totalSize--;
            if (totalSize > 0) {
                tvCount.setText((currentPosition + 1) + "/" + totalSize);
            } else {
                tvCount.setText(0 + "/" + 0);
                icDelete.setVisibility(View.GONE);
            }
            adapterFragment.notifyDataSetChanged();
            if (currentPosition < adapterFragment.getCount()) {
                viewpager.setCurrentItem(currentPosition);
            }
        }
    }

    /**
     * @param media
     * @return 大于等于0 就是表示以选择，返回的是在selectMedias中的下标
     */
    public int isSelect(Media media, ArrayList<Media> list) {
        int is = -1;
        if (list.size() <= 0) {
            return is;
        }
        for (int i = 0; i < list.size(); i++) {
            Media m = list.get(i);
            if (m.path.equals(media.path)) {
                is = i;
                break;
            }
        }
        return is;
    }

    public void done(ArrayList<Media> list, int code) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(PickerConfig.EXTRA_RESULT, list);
        setResult(code, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        done(preRawList, PickerConfig.RESULT_UPDATE_CODE);
        super.onBackPressed();
    }

    public class AdapterFragment extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;

        public AdapterFragment(FragmentManager fm, List<Fragment> mFragments) {
            super(fm);
            this.mFragments = mFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return PagerAdapter.POSITION_NONE;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tvCount.setText((position + 1) + "/" + totalSize);
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
