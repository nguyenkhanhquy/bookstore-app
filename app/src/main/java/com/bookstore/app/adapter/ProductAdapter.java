package com.bookstore.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.activity.DetailProductActivity;
import com.bookstore.app.model.Product;
import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

        // Load image from URL using AsyncTask
//        String imageUrl = product.getImages();
//        new LoadImageTask(holder.imageProduct).execute(imageUrl);

        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText(String.valueOf(product.getPrice()));
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
        return mProducts.size();
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

            //  Bắt sự kiện cho item holder trong MyViewHolder
            itemView.setOnClickListener(v -> {
                // Xử lý khi nhấp vào Item trên User
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product product = mProducts.get(position);
                    Toast.makeText(mContext, "Bạn đã chọn product có id: " + product.getId(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}