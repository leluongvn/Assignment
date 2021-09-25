package com.example.FM_SHOP.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.FM_SHOP.R;
import com.example.FM_SHOP.model.OrderSingle;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.viewHolder> {


    List<OrderSingle> list;
    Context context;

    private static clickItemOrder clickItemOrder;

    public OrderAdapter(List<OrderSingle> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        viewHolder viewHolder = new viewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.viewHolder holder, int position) {
        OrderSingle single = list.get(position);
        Glide.with(context).load(single.getImage()).into(holder.imageView);
        holder.textViewName.setText(single.getNameUser());
        holder.textViewNameProduct.setText( "Name product : "+single.getName());
        holder.textViewAmount.setText("Amount : "+single.getAmount()+"");
        if (single.getStatus() == 0) {
            holder.buttonAccept.setText("Confirm");
        } else {
            holder.buttonAccept.setText("Confirmed");
            holder.buttonAccept.setBackgroundColor(Color.GREEN);
            holder.buttonAccept.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clickItem(clickItemOrder clickItemOrder) {
        OrderAdapter.clickItemOrder = clickItemOrder;
    }

    public interface clickItemOrder {
        void clickItem(int position, View view);
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textViewName, textViewEmail, textViewNameProduct, textViewAmount;
        Button buttonAccept;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imvOrder);
            textViewNameProduct = itemView.findViewById(R.id.tvNameProduct);
            textViewAmount = itemView.findViewById(R.id.tvAmountProduct);
            buttonAccept = itemView.findViewById(R.id.btnConfirm);
            textViewName = itemView.findViewById(R.id.tvNameUser);
            buttonAccept.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            clickItemOrder.clickItem(getAdapterPosition(),v);
        }
    }
}
