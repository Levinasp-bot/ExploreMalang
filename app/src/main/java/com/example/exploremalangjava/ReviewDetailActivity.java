package com.example.exploremalangjava;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReviewDetailActivity extends AppCompatActivity {

    private ImageView imageReview;
    private TextView tvUlasan;
    private Review review;
    private Button btnDelete;

    private DatabaseReference reviewRef;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        imageReview = findViewById(R.id.imageReview);
        tvUlasan = findViewById(R.id.tvUlasan);
        storageReference = FirebaseStorage.getInstance().getReference();
        btnDelete = findViewById(R.id.btnDelete);

        review = getIntent().getParcelableExtra("Review");

        tvUlasan.setText(review.getUlasan());
        Glide.with(this)
                .load(review.getFoto())
                .into(imageReview);

        btnDelete.setOnClickListener(v -> {
            // Hapus data review dari Firebase Realtime Database dan foto dari Firebase Storage
            deleteReview(review.getId(), review.getFoto());
        });
    }

    private void deleteReview(String reviewId, String fotoUrl) {
        // Hapus data review dari Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reviewRef = database.getReference("Review");
        reviewRef.child(reviewId).removeValue();

        // Hapus foto dari Firebase Storage
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(fotoUrl);
        photoRef.delete()
                .addOnSuccessListener(aVoid -> {
                    // Foto berhasil dihapus
                    Toast.makeText(ReviewDetailActivity.this, "Review berhasil dihapus", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(exception -> {
                    // Gagal menghapus foto
                    Toast.makeText(ReviewDetailActivity.this, "Gagal menghapus review", Toast.LENGTH_SHORT).show();
                });
    }
}
