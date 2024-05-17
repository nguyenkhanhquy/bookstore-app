package com.bookstore.app.adapter.admin;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstore.app.R;
import com.bookstore.app.activity.DetailProductActivity;
import com.bookstore.app.activity.admin.DetailEmployeeActivity;
import com.bookstore.app.model.Product;
import com.bookstore.app.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private static final int UPDATE_USER_REQUEST_CODE = 2;
    private final Context context;
    private final List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manager_employee, parent, false);
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
        private ImageView imageUser;
        private TextView nameUser;
        private TextView genderUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUser = itemView.findViewById(R.id.imageEmployee);
            nameUser = itemView.findViewById(R.id.nameEmployee);
            genderUser = itemView.findViewById(R.id.GenderEmployee);

            //  Bắt sự kiện cho item holder trong MyViewHolder
            itemView.setOnClickListener(v -> {
                // Xử lý khi nhấp vào Item trên User
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User user = userList.get(position);
                    Intent intent = new Intent(context, DetailEmployeeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("object_user", user);
                    intent.putExtras(bundle);
                    Activity activity = getActivityFromContext(context);
                    if (activity != null) {
                        activity.startActivityForResult(intent, UPDATE_USER_REQUEST_CODE);
                    } else {
                        Toast.makeText(context, "Unable to get activity from context", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = userList.get(position);
        holder.nameUser.setText(user.getUserName());
        holder.genderUser.setText(user.getGender());

        // Load ảnh với Glide
        Glide.with(context)
                .load(user.getImages())
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(holder.imageUser);
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 :userList.size();
    }
    private void OnClickGoToDetail(Product product) {
        Intent intent = new Intent(context, DetailProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("obiect_user", product);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
