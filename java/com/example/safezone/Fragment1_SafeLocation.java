package com.example.safezone;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;

public class Fragment1_SafeLocation extends AppCompatActivity
{
    ViewPager mPager;
    private int [] layouts =  {R.layout.fragment_1_safe_location,R.layout.fragment_2_user_location,
            R.layout.fragment_3_emergency,R.layout.fragment_4_view_help};
    private PagerAdapter mPagerAdapter;

    @Override
    public void onCreate( Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_1_safe_location);
        mPager = findViewById(R.id.pager);

        mPagerAdapter = new PagerAdapter(layouts,this);
        mPager.setAdapter(mPagerAdapter);
    }
}