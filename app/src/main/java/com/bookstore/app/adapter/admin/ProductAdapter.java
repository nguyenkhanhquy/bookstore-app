package com.bookstore.app.adapter.admin;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.activity.admin.UpdateProductActivity;
import com.bookstore.app.model.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private static final int UPDATE_PRODUCT_REQUEST_CODE = 2;
    private final Context context;
    private final List<Product> productList;

    public ProductAdapter(Context context, List<Product> productListList) {
        this.context = context;
        this.productList = productListList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manager_product_item, parent, false);
        return new MyViewHolder(view);
    }

    public static Activity getActivityFromContext(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivityFromContext(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView nameProduct;
        TextView priceProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);

            //  Bắt sự kiện cho item holder trong MyViewHolder
            itemView.setOnClickListener(v -> {
                // Xử lý khi nhấp vào Item trên User
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product product = productList.get(position);
                    Intent intent = new Intent(context, UpdateProductActivity.class);
                    intent.putExtra("object", product);
                    Activity activity = getActivityFromContext(context);
                    if (activity != null) {
                        activity.startActivityForResult(intent, UPDATE_PRODUCT_REQUEST_CODE);
                    } else {
                        Toast.makeText(context, "Unable to get activity from context", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText(product.getPrice() + " VNĐ");

        // Load ảnh với Glide
        Glide.with(context)
                .load(product.getImages())
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(holder.imageProduct);
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 :productList.size();
    }
}
