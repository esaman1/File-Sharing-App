package com.datasharing.Welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.datasharing.Activity.HomeActivity;
import com.datasharing.Const.SharedPreference;
import com.datasharing.Fragment.Intro.FirstFragment;
import com.datasharing.Fragment.Intro.PagerAdapter.IntroViewerPagersAdapter;
import com.datasharing.Fragment.Intro.SecondFragment;
import com.datasharing.Fragment.Intro.ThirdFragment;
import com.datasharing.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.ll_first_introPage)
    LinearLayout ll_first_introPage;
    @BindView(R.id.ll_second_introPage)
    LinearLayout ll_second_introPage;
    @BindView(R.id.ll_third_introPage)
    LinearLayout ll_third_introPage;
    @BindView(R.id.img_first_next)
    ImageView img_first_next;
    @BindView(R.id.img_second_next)
    ImageView img_second_next;
    @BindView(R.id.img_third_next)
    ImageView img_third_next;
    TextView first_skip, second_skip, third_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (SharedPreference.getLogin(FirstActivity.this)) {
            Intent intent = new Intent(FirstActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_first);

        ButterKnife.bind(FirstActivity.this);


        first_skip = findViewById(R.id.first_skip);
        second_skip = findViewById(R.id.second_skip);
        third_skip = findViewById(R.id.third_skip);

        img_first_next.setOnClickListener(this);
        img_second_next.setOnClickListener(this);
        img_third_next.setOnClickListener(this);
        first_skip.setOnClickListener(this);
        second_skip.setOnClickListener(this);
        third_skip.setOnClickListener(this);
        setViewPager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_first_next:
                viewPager.setCurrentItem(1);
                break;
            case R.id.img_second_next:
                viewPager.setCurrentItem(2);
                break;
            case R.id.img_third_next:
            case R.id.first_skip:
            case R.id.second_skip:
            case R.id.third_skip:
                SharedPreference.setLogin(FirstActivity.this, true);
                Intent intent = new Intent(FirstActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void setViewPager() {
        IntroViewerPagersAdapter adapter = new IntroViewerPagersAdapter(getSupportFragmentManager());
        adapter.addFragment(new FirstFragment(), "FIRST_FRAGMENT");
        adapter.addFragment(new SecondFragment(), "SECOND_FRAGMENT");
        adapter.addFragment(new ThirdFragment(), "THIRD_FRAGMENT");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ll_first_introPage.setVisibility(View.VISIBLE);
                    ll_second_introPage.setVisibility(View.GONE);
                    ll_third_introPage.setVisibility(View.GONE);
                } else if (position == 1) {
                    ll_first_introPage.setVisibility(View.GONE);
                    ll_second_introPage.setVisibility(View.VISIBLE);
                    ll_third_introPage.setVisibility(View.GONE);
                } else if (position == 2) {
                    ll_first_introPage.setVisibility(View.GONE);
                    ll_second_introPage.setVisibility(View.GONE);
                    ll_third_introPage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 2) {
            viewPager.setCurrentItem(1);
        } else if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }
}