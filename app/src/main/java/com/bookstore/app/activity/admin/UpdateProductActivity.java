package com.bookstore.app.activity.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bookstore.app.R;
import com.bookstore.app.model.Product;
import com.bookstore.app.response.ProductResponse;
import com.bookstore.app.service.ProductAPIService;
import com.bookstore.app.service.RetrofitClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProductActivity extends AppCompatActivity {

    private ProductAPIService productAPIService;
    private ProductResponse productResponse;
    public static final int MY_REQUEST_CODE = 102;
    private Uri mUri;
    ImageView imgChoose, btnDelete;
    EditText edtName, edtPrice, edtDesc;
    Button btnUpdate, btnCancel;
    String name, desc;
    double price;
    Product product;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_product);

        anhXa();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");

        loadProductData();
        initLinsenter();
    }

    private void anhXa() {
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgChoose = findViewById(R.id.imgChoose);
        btnDelete = findViewById(R.id.btnDelete);
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDesc = findViewById(R.id.edtDesc);
    }

    private void initLinsenter() {
        imgChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CheckPermission();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (checkInfo()) {
//                    name = edtName.getText().toString().trim();
//                    desc = edtDesc.getText().toString().trim();
//                    price = Double.parseDouble(edtPrice.getText().toString().trim());
//                    addProduct(name, desc, price);
//                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nếu người dùng chọn "Xóa", thực hiện xóa khách sạn
                        deleteProduct(product.getId());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nếu người dùng chọn "Hủy", đóng dialog
                        dialog.dismiss();
                    }
                });
                // Hiển thị dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void loadProductData() {
        product = (Product) getIntent().getSerializableExtra("object");
        assert product != null;
        name = product.getName();
        desc = product.getDescription();
        price = product.getPrice();

        edtName.setText(name);
        edtDesc.setText(desc);
        edtPrice.setText((int) price + "");
        Glide.with(this)
                .load(product.getImages())
                .signature(new ObjectKey(System.currentTimeMillis()))
                .into(imgChoose);
    }

    private void deleteProduct(int productId) {
        productAPIService = RetrofitClient.getRetrofit().create(ProductAPIService.class);
        productAPIService.deleteProduct(productId).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    productResponse = response.body();
                    if (productResponse != null) {
                        // Xử lý dữ liệu nhận được từ API ở đây
                        if(productResponse.isError()) {
                            Toast.makeText(UpdateProductActivity.this, productResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UpdateProductActivity.this, productResponse.getMessage(), Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        // Xử lý khi API trả về null
                        Toast.makeText(UpdateProductActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi gọi API không thành công
                    int statusCode = response.code();
                    Log.e("API Error", "Status code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG", t.toString());
                Toast.makeText(UpdateProductActivity.this, "Gọi APT thất bại", Toast.LENGTH_LONG).show();
            }
        });
    }
}