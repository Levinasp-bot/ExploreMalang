package com.example.exploremalangjava;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private TextView tvNama;
    private TextView tvDeskripsi;
    private ImageView image;
    private RecyclerView rvReview;
    private Button btnReview;
    private Wisata wisata;
    private List<Review> list;
    private ReviewListAdapter reviewAdapter;
    private DataReview dataReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvNama = findViewById(R.id.textView);
        tvDeskripsi = findViewById(R.id.textView2);
        image = findViewById(R.id.imageView2);
        rvReview = findViewById(R.id.rvReview);
        btnReview = findViewById(R.id.btnReview);

        btnReview.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("Wisata", wisata);
            startActivity(intent);
        });

        // Ambil data dari Intent
        wisata = getIntent().getParcelableExtra("Wisata");

        // Set data pada View
        tvNama.setText(wisata.getNama());
        tvDeskripsi.setText(wisata.getDeskripsi());
        Glide.with(this)
                .load(wisata.getFoto())
                .into(image);

        // Inisialisasi Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query reviewRef = database.getReference("Review")
                .orderByChild("wisataId")
                .equalTo(wisata.getId());

        // Inisialisasi ReviewAdapter dan ReviewList
        list = new ArrayList<>();
        reviewAdapter = new ReviewListAdapter((ArrayList<Review>) list);

        // Set layout manager untuk RecyclerView
        rvReview.setLayoutManager(new LinearLayoutManager(this));

        // Set adapter untuk RecyclerView
        rvReview.setAdapter(reviewAdapter);

        // Listener untuk mengambil data dari Firebase database reference
        reviewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Review> reviewList = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Review review = childSnapshot.getValue(Review.class);
                    if (review != null && review.getWisataId().equals(wisata.getId())) {
                        reviewList.add(review);
                    }
                }

                list.clear();
                list.addAll(reviewList);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("DetailActivity", "Error: " + error.getMessage());
            }
        });

        // Inisialisasi DataReview
        dataReview = new DataReview();
    }
}
