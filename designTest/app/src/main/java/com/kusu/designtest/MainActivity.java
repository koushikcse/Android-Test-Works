package com.kusu.designtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.kusu.designtest.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.rvList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MyAdapter adapter=new MyAdapter(this,datelist());
        binding.rvList.setAdapter(adapter);
    }

    private List<DataModel> datelist() {
        List<DataModel> dataModelList=new ArrayList<>();
        dataModelList.add(new DataModel("Banglore University","Information Technology","05/2015 - 06/2019"));
        dataModelList.add(new DataModel("IIT Chennai","M. Tech","08/2019 - 06/2020"));
        dataModelList.add(new DataModel("IIT Mumbai","Phd","07/2020 - 05/2018"));

        return dataModelList;
    }
}
