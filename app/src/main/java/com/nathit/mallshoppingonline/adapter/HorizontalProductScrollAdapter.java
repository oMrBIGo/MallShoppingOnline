package com.nathit.mallshoppingonline.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nathit.mallshoppingonline.ProductDetailsActivity;
import com.nathit.mallshoppingonline.R;
import com.nathit.mallshoppingonline.model.HorizontalProductScrollModel;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String resource = horizontalProductScrollModelList.get(position).getProduceImage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String description = horizontalProductScrollModelList.get(position).getProductDescription();
        String price = horizontalProductScrollModelList.get(position).getProductPrice();
        String productId = horizontalProductScrollModelList.get(position).getProductID();
        holder.setData(productId,resource,title,description,price);

    }

    @Override
    public int getItemCount() {
        if (horizontalProductScrollModelList.size() > 8) {
            return 8;
        } else {
            return horizontalProductScrollModelList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView productDescription;
        private TextView productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.h_s_product_image);
            productTitle = itemView.findViewById(R.id.h_s_product_title);
            productDescription = itemView.findViewById(R.id.h_s_product_description);
            productPrice = itemView.findViewById(R.id.h_s_product_price);

        }
        private void setData(String productId, String resource,String title,String description,String price) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(productImage);
            productPrice.setText("???????????? "  + price + " ?????????");
            productDescription.setText(description);
            productTitle.setText(title);

            if(!title.equals("")){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemView.getContext().startActivity(new Intent(itemView.getContext(),ProductDetailsActivity.class).putExtra("PRODUCT_ID",productId));
                    }
                });
            }
        }

    }
}
