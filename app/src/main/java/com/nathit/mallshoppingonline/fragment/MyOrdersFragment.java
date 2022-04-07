package com.nathit.mallshoppingonline.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nathit.mallshoppingonline.R;
import com.nathit.mallshoppingonline.adapter.MyOrderAdapter;
import com.nathit.mallshoppingonline.model.MyOrderItemModel;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    private RecyclerView myOrdersRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        myOrdersRecyclerView = view.findViewById(R.id.my_orders_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(linearLayoutManager);

        List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.smartphone1,2,"IPHONE 13 PRO MAX", "15 มีนาคม 2565"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.smartphone1,5,"IPHONE 13 PRO MAX", "18 มีนาคม 2565"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.smartphone1,5,"IPHONE 13 PRO MAX", "21 มีนาคม 2565"));
        myOrderItemModelList.add(new MyOrderItemModel(R.drawable.smartphone1,3,"IPHONE 13 PRO MAX", "25 มีนาคม 2565"));

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();

        return view;
    }
}