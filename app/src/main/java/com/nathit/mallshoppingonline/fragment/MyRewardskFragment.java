package com.nathit.mallshoppingonline.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nathit.mallshoppingonline.R;
import com.nathit.mallshoppingonline.adapter.MyRewardsAdapter;
import com.nathit.mallshoppingonline.model.RewardModel;

import java.util.ArrayList;
import java.util.List;

public class MyRewardskFragment extends Fragment {

    public MyRewardskFragment() {
        // Required empty public constructor
    }

    private RecyclerView rewardsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewardsk, container, false);

        rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardsRecyclerView.setLayoutManager(linearLayoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 35% เฉพาะลูกค้าใหม่","31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 35% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 15%","31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 15% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 45%","31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 45% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 65%","31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 65% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 90% เฉพาะลูกค้าใหม่","31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 90% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 80% เฉพาะลูกค้าใหม่","31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 80% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 25%","31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 25% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 10% เฉพาะลูกค้าใหม่","31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 10% ขั้นต่ำ 5,000 บาท สูงสุด"));
        rewardModelList.add(new RewardModel("โค๊ดส่วนลด 95%","31 มีนาคม 2565", "มาไวได้ไว รีบใช้ก่อนจะหมด! รับโค๊ดส่วนลด 95% ขั้นต่ำ 5,000 บาท สูงสุด"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList,false);
        rewardsRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();
        return view;
    }
}