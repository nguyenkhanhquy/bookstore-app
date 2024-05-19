package com.bookstore.app.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.model.OrderItem;

import java.util.List;

public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.MyViewHolder> {
    private final Context context;
    private final List<OrderItem> orderItemList;

    public DetailOrderAdapter(Context context, List<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_item, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameProduct;
        TextView quantity;
        TextView priceProduct;
        TextView priceOrderItem;
        ImageView imagesProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagesProduct = itemView.findViewById(R.id.imagesProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            quantity = itemView.findViewById(R.id.quantity);
            priceProduct = itemView.findViewById(R.id.priceProduct);
            priceOrderItem = itemView.findViewById(R.id.priceOrderItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);
        holder.nameProduct.setText(orderItem.getNameProduct());
        holder.quantity.setText(String.valueOf(orderItem.getQuantity()));
        holder.priceProduct.setText(orderItem.getPrice() + " VNĐ");
        holder.priceOrderItem.setText((int) orderItem.getPrice() * orderItem.getQuantity() + " VNĐ");
    }

    @Override
    public int getItemCount() {
        return orderItemList == null ? 0 :orderItemList.size();
    }
}
