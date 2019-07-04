package com.findclass.renan.findclass;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView navigation;
    private Context context = this;

    MapFragment mapFragment = new MapFragment(this);
    BuildingsFragment buildingsFragment = new BuildingsFragment(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        navigation = findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = findViewById(R.id.view_pager); //Init Viewpager
        //setupFm( viewPager,this); //Setup Fragment
        //viewPager.setCurrentItem(0); //Set Currrent Item When Activity Start
        //viewPager.setOnPageChangeListener(new PageChange()); //Listeners For Viewpager When Page Changed

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                mapFragment).commit();
    }

    public void setupFm(ViewPager viewPager, Context context){
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.AddFragment(mapFragment,  getResources().getString(R.string.Map));//Add Map fragment
        viewPagerAdapter.AddFragment(buildingsFragment,  getResources().getString(R.string.Buildings));//Add buildings fragment
        viewPager.setAdapter(viewPagerAdapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.map:
                    selectedFragment = mapFragment;
                    break;
                case R.id.Buildings:
                    selectedFragment = buildingsFragment;
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

            return true;
        }
    };

    public class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.map);
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.Buildings);
                    break;
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }
}
