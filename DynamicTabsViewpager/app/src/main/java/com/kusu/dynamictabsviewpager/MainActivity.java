package com.kusu.dynamictabsviewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.kusu.dynamictabsviewpager.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        List<Fragment> fragmentList = new ArrayList<>();
        int len = dummyTabs().size();
        for (int i = 0; i < len; i++) {
            ContentFragment contentFragment = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TAB", dummyTabs().get(i));
            contentFragment.setArguments(bundle);
            fragmentList.add(contentFragment);
        }



        MyFragmentPageAdapter myFragmentPageAdapter = new MyFragmentPageAdapter(this,
                getSupportFragmentManager(),
                fragmentList,
                dummyTabs());

        binding.viewpager.setAdapter(myFragmentPageAdapter);
        binding.tablayout.setupWithViewPager(binding.viewpager);
    }

    private ArrayList<String> dummyTabs() {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("TabOne");
        tabs.add("TabTwo");
        tabs.add("TabThree");
        return tabs;
    }
}
