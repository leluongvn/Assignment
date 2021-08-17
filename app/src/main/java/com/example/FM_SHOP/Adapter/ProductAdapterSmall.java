package com.example.FM_SHOP.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.Product;

import java.util.List;

public class ProductAdapterSmall extends RecyclerView.Adapter<ProductAdapterSmall.viewHolderProduct> {

    List<Product> productList;
    Context context;


    private static OnclickItem onclickItem;
    public ProductAdapterSmall(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull

    @Override
    public viewHolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View inflater = LayoutInflater.from(context).inflate(R.layout.item_product_sale_detail
                    , parent, false);
            viewHolderProduct product = new viewHolderProduct(inflater);
            return product;
        } else if (viewType == 2) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_product_detail, parent, false);
            viewHolderProduct product = new viewHolderProduct(view);
            return product;
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_product_new_detail, parent, false);
            viewHolderProduct product = new viewHolderProduct(view);
            return product;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderProduct holder, int position) {

        Product product = productList.get(position);

        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText((int) product.getPrice() + " đ");

        Glide.with(context).load(product.getImage())
                .transform()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class viewHolderProduct extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textViewName, textViewPrice;

        public viewHolderProduct(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imvProduct);
            textViewPrice = itemView.findViewById(R.id.tvPrice);
            textViewName = itemView.findViewById(R.id.tvNameProduct);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onclickItem.clickItem(getAdapterPosition(),v);
        }
    }

    public void setOnclickItem(OnclickItem onclickItem){
        ProductAdapterSmall.onclickItem = onclickItem;
    }

    public interface OnclickItem {
        void clickItem(int position, View view);
    }

    @Override
    public int getItemViewType(int position) {
        Product product = productList.get(position);
        if (product.getType().equals("Sale")) {
            return 1;
        } else if (product.getType().equals("Normal")) {
            return 2;
        } else {
            return 3;
        }
    }
}
