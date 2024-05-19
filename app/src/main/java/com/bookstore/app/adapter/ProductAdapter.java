package com.bookstore.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.activity.DetailProductActivity;
import com.bookstore.app.model.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> mProducts;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ProductAdapter(Context mContext, List<Product> products) {
        this.mContext = mContext;
        this.mProducts = products;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.product_item_horizontal, parent, false);
        return new ProductViewHolder(itemView);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageProduct;
        private TextView nameProduct;
        private TextView priceProduct;
        private ConstraintLayout layoutItem;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
            layoutItem = itemView.findViewById(R.id.cardView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        final Product product = mProducts.get(position);
        if (product == null) {
            Log.e("Erorr", "Product is null at position: " + position);
            return;
        }

        // Load ảnh với Glide
        Glide.with(mContext)
                .load(product.getImages())
                .into(holder.imageProduct);

        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText(product.getPrice() + " VNĐ");
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickGoToDetail(product);
            }
        });
    }

    private void OnClickGoToDetail(Product product) {
        Intent intent = new Intent(mContext, DetailProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("obiect_product", product);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mProducts == null ? 0 :mProducts.size();
    }
}