package com.dmcbig.mediapicker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.dmcbig.mediapicker.entity.Media;
import com.dmcbig.mediapicker.view.PreviewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmcBig on 2017/8/9.
 */

public class SinglePreviewActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{

    Button done;
    ViewPager viewpager;
    View bottom;
    ArrayList<Media> preRawList, selects;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_preview_main);
        findViewById(R.id.cancel).setOnClickListener(this);
        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(this);
        bottom= findViewById(R.id.bottom);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        preRawList = getIntent().getParcelableArrayListExtra(PickerConfig.PRE_RAW_LIST);
        selects = new ArrayList<>();
        selects.addAll(preRawList);
        setView(preRawList);
    }

    void setView(ArrayList<Media> default_list) {
        setDoneView(default_list.size());
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        for (Media media : default_list) {
            fragmentArrayList.add(PreviewFragment.newInstance(media, ""));
        }
        AdapterFragment adapterFragment = new AdapterFragment(getSupportFragmentManager(), fragmentArrayList);
        viewpager.setAdapter(adapterFragment);
        viewpager.addOnPageChangeListener(this);
    }

    void setDoneView(int num1) {
//        done.setText(getString(R.string.send) + "(" + num1 + "/" + getIntent().getIntExtra(PickerConfig.MAX_SELECT_COUNT, PickerConfig.DEFAULT_SELECTED_MAX_COUNT) + ")");
        done.setText(getString(R.string.completed));
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel) {
            finish();
        } else if (id == R.id.done) {
            done(selects, PickerConfig.RESULT_CODE);
        }
    }

    public void done(ArrayList<Media> list, int code) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(PickerConfig.EXTRA_RESULT, list);
//        intent.putExtra(PickerConfig.IS_ORIGIN,cbOriginPick.isChecked());
        setResult(code, intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        done(selects, PickerConfig.RESULT_UPDATE_CODE);
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

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
