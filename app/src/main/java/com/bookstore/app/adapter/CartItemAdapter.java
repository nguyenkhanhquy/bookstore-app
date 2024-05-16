package com.bookstore.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.model.Cart;
import com.bookstore.app.model.CartItem;
import com.bumptech.glide.Glide;

import java.util.List;

import lombok.Setter;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {

    private final Context context;
    private final List<CartItem> cartitemList;
    private Cart cart;

    public CartItemAdapter(Context context, List<CartItem> cartitemList) {
        this.context = context;
        this.cartitemList = cartitemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameProduct;
        TextView priceProduct;
        TextView quantity;
        ImageView imageProduct;
        ImageButton delete, plus, minus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            quantity = itemView.findViewById(R.id.quantity);
            delete = itemView.findViewById(R.id.delete);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);

//            //  Bắt sự kiện cho item holder trong MyViewHolder
//            itemView.setOnClickListener(v -> {
//                // Xử lý khi nhấp vào Item trên CartItem
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    CartItem cartItem = cartitemList.get(position);
//                    Toast.makeText(context, "Bạn đã chọn CartItem: " + cartItem.getId(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartItem cartItem = cartitemList.get(position);
        holder.priceProduct.setText(cartItem.getProduct().getPrice() + " VNĐ");
        holder.nameProduct.setText(cartItem.getProduct().getName());
        holder.quantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart = new Cart(context);
                cart.removeFromCart(String.valueOf(cartItem.getId()));
                // Thông báo cho activity khi một mục đã được xóa khỏi giỏ hàng
                if (cartItemListener != null) {
                    cartItemListener.onCartItemRemoved();
                }
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart = new Cart(context);
                cart.plusCart(cartItem);
                if (cartItemListener != null) {
                    cartItemListener.onCartItemRemoved();
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart = new Cart(context);
                if (cartItem.getQuantity() >= 2) {
                    cart.minusCart(cartItem);
                } else {
                    cart.removeFromCart(String.valueOf(cartItem.getId()));
                }
                if (cartItemListener != null) {
                    cartItemListener.onCartItemRemoved();
                }
            }
        });

        // Load ảnh với Glide
        Glide.with(context)
                .load(cartItem.getProduct().getImages())
                .into(holder.imageProduct);
    }

    @Override
    public int getItemCount() {
        return cartitemList == null ? 0 :cartitemList.size();
    }

    public interface CartItemListener {
        void onCartItemRemoved();
    }

    @Setter
    private CartItemListener cartItemListener;
}
