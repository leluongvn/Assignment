package com.example.FM_SHOP.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.FM_SHOP.uiAdmin.AddProductActivity;
import com.example.FM_SHOP.uiUser.DetailProductActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapterUser extends RecyclerView.Adapter<ProductAdapterUser.viewHolderProduct> {

    List<Product> productList;
    Context context;

    public ProductAdapterUser(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    private static OnclickListener listener;

    @NonNull

    @Override
    public viewHolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View inflater = LayoutInflater.from(context).inflate(R.layout.item_product_sale
                    , parent, false);
            viewHolderProduct product = new viewHolderProduct(inflater);
            return product;
        } else if (viewType == 2) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_product, parent, false);
            viewHolderProduct product = new viewHolderProduct(view);
            return product;
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_product_new, parent, false);
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

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = productList.get(position);
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("product", product);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class viewHolderProduct extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textViewName, textViewPrice, textViewAmountBuy;

        public viewHolderProduct(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imvProduct);
            textViewPrice = itemView.findViewById(R.id.tvPrice);
            textViewName = itemView.findViewById(R.id.tvNameProduct);
        }

        @Override
        public void onClick(View v) {
            listener.onclickItem(getAdapterPosition(), v);
        }
    }


    public void setItemClick(OnclickListener onclickListener) {
        ProductAdapterUser.listener = onclickListener;
    }

    public interface OnclickListener {
        void onclickItem(int position, View view);
    }


    public void FilterList(ArrayList<Product> productList){
        productList= productList;
        notifyDataSetChanged();
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
