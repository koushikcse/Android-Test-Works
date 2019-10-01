package com.kusu.dynamictabsviewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kusu.dynamictabsviewpager.databinding.FragmentContentBinding;

import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends Fragment {

    private FragmentContentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("TAB");
            binding.tvTab.setText(name);
        }
        binding.rvList.setLayoutManager(new GridLayoutManager(getContext(), 4, RecyclerView.VERTICAL, false));

        MyRvListAdapter myRvListAdapter = new MyRvListAdapter(getContext(), dummyList());
        binding.rvList.setAdapter(myRvListAdapter);
        return binding.getRoot();
    }

    private List<String> dummyList() {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        return list;
    }
}
