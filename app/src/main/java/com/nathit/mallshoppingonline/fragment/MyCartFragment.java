package com.nathit.mallshoppingonline.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nathit.mallshoppingonline.AddAddressActivity;
import com.nathit.mallshoppingonline.DeliveryActivity;
import com.nathit.mallshoppingonline.R;
import com.nathit.mallshoppingonline.adapter.CartAdapter;
import com.nathit.mallshoppingonline.model.CartItemModel;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemsRecyclerView;
    private Button continueBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        cartItemsRecyclerView = view.findViewById(R.id.cart_items_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);
        continueBtn = view.findViewById(R.id.cart_continue_btn);

        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.drawable.smartphone1,"IPHONE 13 PRO MAX",2,"32,900","42,900",1,0,1));
        cartItemModelList.add(new CartItemModel(0,R.drawable.smartphone2,"IPHONE 13 PRO MAX",0,"32,900","42,900",1,1,1));
        cartItemModelList.add(new CartItemModel(0,R.drawable.smartphone3,"IPHONE 13 PRO MAX",5,"32,900","42,900",1,2,1));
        cartItemModelList.add(new CartItemModel(1,"ราคา (3 ชิ้น)","ราคา 98,000 บาท","ฟรี","10,000","98,000 บาท"));
        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(getContext(), AddAddressActivity.class);
                getContext().startActivity(deliveryIntent);
            }
        });

        return view;
    }
}