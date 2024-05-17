package com.bookstore.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.model.Order;
import com.bookstore.app.model.OrderItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private final Context context;
    private final List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idOrder;
        TextView priceOrder;
        TextView countItem;
        Button btnCancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idOrder = itemView.findViewById(R.id.idOrder);
            priceOrder = itemView.findViewById(R.id.priceOrder);
            countItem = itemView.findViewById(R.id.countItem);
            btnCancel = itemView.findViewById(R.id.btnCancel);

            //  Bắt sự kiện cho item holder trong MyViewHolder
            itemView.setOnClickListener(v -> {
                // Xử lý khi nhấp vào Item trên Order
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Order order = orderList.get(position);
                    Toast.makeText(context, "Bạn đã chọn Order: " + order.getId(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.idOrder.setText("Mã đơn hàng: " + order.getId());

        if (order.getOrderTrack().getId() != 1) {
            holder.btnCancel.setVisibility(View.GONE);
        }

        List<OrderItem> orderItems = order.getOrderItems();
        holder.countItem.setText(orderItems.size() + " items");

        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += (int) (orderItem.getPrice() * orderItem.getQuantity());
        }
        holder.priceOrder.setText(totalPrice + " VNĐ");

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Hủy!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0 :orderList.size();
    }
}
