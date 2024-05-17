package com.bookstore.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.model.CartItem;
import com.bumptech.glide.Glide;

import java.util.List;

public class CheckOutItemAdapter extends RecyclerView.Adapter<CheckOutItemAdapter.MyViewHolder> {

    private final Context context;
    private final List<CartItem> cartitemList;

    public CheckOutItemAdapter(Context context, List<CartItem> cartitemList) {
        this.context = context;
        this.cartitemList = cartitemList;
    }

    @NonNull
    @Override
    public CheckOutItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkout_item, parent, false);
        return new CheckOutItemAdapter.MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameProduct;
        TextView priceProduct;
        TextView quantity;
        ImageView imageProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CheckOutItemAdapter.MyViewHolder holder, int position) {
        CartItem cartItem = cartitemList.get(position);
        holder.priceProduct.setText(cartItem.getProduct().getPrice() + " VNĐ");
        holder.nameProduct.setText(cartItem.getProduct().getName());
        holder.quantity.setText(String.valueOf(cartItem.getQuantity()));
        // Load ảnh với Glide
        Glide.with(context)
                .load(cartItem.getProduct().getImages())
                .into(holder.imageProduct);
    }

    @Override
    public int getItemCount() {
        return cartitemList == null ? 0 :cartitemList.size();
    }
}
