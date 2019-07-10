package com.findclass.renan.findclass;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.findclass.renan.findclass.Chat.ChatActivity;
import com.findclass.renan.findclass.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView navigation;
    private String institute;
    private int instituteAddress;
    private DatabaseReference databaseReference;
    private Context context = this;
    private boolean flag = true;
    FragmentTransaction transaction;
    MapFragment mapFragment;
    BuildingsFragment buildingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        mapFragment = MapFragment.getInstance();
        buildingsFragment  = BuildingsFragment.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getMosadName();

        navigation = findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = findViewById(R.id.view_pager); //Init Viewpager
        //setupFm( viewPager,this); //Setup Fragment
        //viewPager.setCurrentItem(0); //Set Currrent Item When Activity Start
        //viewPager.setOnPageChangeListener(new PageChange()); //Listeners For Viewpager When Page Changed
        transaction.add(R.id.fragment_container,mapFragment,"MAP");
        transaction.commit();

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                mapFragment).commit();
    }

    private void getMosadName() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            flag = false;
            instituteAddress = R.string.hit;
            getSupportActionBar().setTitle(instituteAddress);
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    institute = user.getInstitute();
                    switch (institute) {
                        case "hit":
                            instituteAddress = R.string.hit;
                            break;
                        case "manage":
                            instituteAddress = R.string.collegemanage;
                            break;
                        case "ONO":
                            instituteAddress = R.string.onoacademic;
                            break;
                        case "SCE":
                            instituteAddress = R.string.sce;
                            break;
                        case "TLV":
                            instituteAddress = R.string.tlvacademic;
                            break;
                    }
                    getSupportActionBar().setTitle(instituteAddress);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
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
        public boolean onNavigationItemSelected(MenuItem item) {

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
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.chat:
                intent = new Intent(MainActivity.this, ChatActivity.class);
                if (flag)
                startActivity(intent);
                else Toast.makeText(getApplicationContext(),getString(R.string.must_login2),Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(MainActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
