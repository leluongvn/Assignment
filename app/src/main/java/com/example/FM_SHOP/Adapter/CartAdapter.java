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
import com.example.FM_SHOP.model.OrderSingle;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolder> {

    private List<OrderSingle> list;
    private Context context;

    public CartAdapter(List<OrderSingle> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private static onclickItem onclickItem;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        viewHolder holder = new viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewHolder holder, int position) {
        OrderSingle single = list.get(position);

        holder.textViewPrice.setText("Price  : "+single.getPrice()+" đ");
        holder.textViewAmount.setText("Amount : "+single.getAmount()+"");
        holder.textViewName.setText("Name product : "+single.getName());
        Glide.with(context).load(single.getImage()).into(holder.mImageView);

        if (single.getStatus() == 0) {
            holder.imageViewStatus.setImageResource(R.drawable.ic_baseline_event_note_24);
        } else {
            holder.imageViewStatus.setImageResource(R.drawable.ic_icons8_checkmark);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImageView, imageViewStatus;
        TextView textViewName, textViewPrice, textViewAmount;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imvCart);
            imageViewStatus = itemView.findViewById(R.id.imvStatus);
            textViewName = itemView.findViewById(R.id.tvNameCart);
            textViewAmount = itemView.findViewById(R.id.tvAmountCart);
            textViewPrice = itemView.findViewById(R.id.tvPriceCart);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onclickItem.onclick(getAdapterPosition(), v);
        }
    }


    public void setOnclickItem(onclickItem onclickItem) {

        CartAdapter.onclickItem = onclickItem;
    }

    public interface onclickItem {
        void onclick(int position, View view);
    }
}
