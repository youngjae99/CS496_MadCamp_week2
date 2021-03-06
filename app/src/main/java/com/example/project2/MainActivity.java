package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView btn_logout;
    private ImageView profile;
    private TextView txt_username;
    String user_name;
    String user_email;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FacebookSdk.sdkInitialize(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //txt_username = (TextView)findViewById(R.id.username);
        user_name = getIntent().getStringExtra("UserName");
        user_email = getIntent().getStringExtra("UserEmail");

        profile = findViewById(R.id.profileImg); //프로필 이미지 뷰

        //txt_username = (TextView)findViewById(R.id.username); // 사용자 이름 표시
        //txt_username.setText(getIntent().getStringExtra("UserName"));
        if(getIntent().getStringExtra("profileImgURL").equals("local")){ // 이메일 이용 로그인 -> 페북 사진 X
            Glide.with(this) // 사용자 프로필 이미지 표시
                    .load(R.drawable.com_facebook_profile_picture_blank_square) // 웹 이미지 로드
                    //.load(localPhotoList.get(i).getImgPath()) // 이미지 로드
                    .override(500,500) //해상도 최적화
                    .thumbnail(0.3f) //섬네일 최적화. 지정한 %만큼 미리 이미지를 가져와 보여주기
                    .centerCrop() // 중앙 크롭
                    .into(profile);
        }
        else{ //페북 로그인
            Glide.with(this) // 사용자 프로필 이미지 표시
                    .load(getIntent().getStringExtra("profileImgURL")) // 웹 이미지 로드
                    //.load(localPhotoList.get(i).getImgPath()) // 이미지 로드
                    .override(500,500) //해상도 최적화
                    .thumbnail(0.3f) //섬네일 최적화. 지정한 %만큼 미리 이미지를 가져와 보여주기
                    .centerCrop() // 중앙 크롭
                    .into(profile);
        }


        Log.e("createdmain","created "+user_name);

        LoginManager.getInstance().logOut();
        btn_logout = (ImageView) findViewById(R.id.logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        viewPager = findViewById(R.id.view_pager); //탭별 화면 보이는 view pager
        tabLayout = findViewById(R.id.tab_layout); //탭바

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment1, "");
        viewPagerAdapter.addFragment(fragment2, "");
        viewPagerAdapter.addFragment(fragment3, "");
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_menu1);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_menu2);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_menu3);

        /*
        viewPager = findViewById(R.id.view_pager);
        AnimatedTabLayout atl = findViewById(R.id.atl);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment1, "");
        viewPagerAdapter.addFragment(fragment2, "");
        viewPagerAdapter.addFragment(fragment3, "");
        viewPager.setAdapter(viewPagerAdapter);

        atl.setTabChangeListener((AnimatedTabLayout.OnChangeListener)(new AnimatedTabLayout.OnChangeListener() {
            public void onChanged(int position) {
                Log.d("tab", String.format("tab changed to %d", position));
            }
        }));
        */
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Fragment1.newinstance(user_email, user_name);
                case 1:
                    return Fragment2.newinstance(user_email);
                case 2:
                    return Fragment3.newinstance(user_email);
                default:
                    return null;
            }

        }

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        /*
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }*/

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }


}