package com.example.exploremalangjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ReviewActivity extends AppCompatActivity {
    private EditText etUlasan;
    private Button btnAddImage;
    private ImageView image;
    private Button btnSave;
    private Wisata wisata;
    private DatabaseReference databaseReference;
    private final StorageReference storage = FirebaseStorage.getInstance().getReference();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private int reviewIdCounter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        etUlasan = findViewById(R.id.editText3);
        btnAddImage = findViewById(R.id.button);
        image = findViewById(R.id.imageView3);
        btnSave = findViewById(R.id.btnSave);

        wisata = getIntent().getParcelableExtra("Wisata");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        databaseReference = DataReview.getDatabaseReference();

        DatabaseReference reviewCountRef = databaseReference.child("Review");
        reviewCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int reviewCount = (int) dataSnapshot.getChildrenCount();
                setupReviewIdCounter(reviewCount + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tangani kesalahan jika terjadi
            }
        });

        btnAddImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }
        private void setupReviewIdCounter(int currentReviewCount){
            final int[] nextReviewId = {currentReviewCount};
            btnSave.setOnClickListener(view -> {
                String ulasan = etUlasan.getText().toString().trim();
                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String id = String.valueOf(nextReviewId[0]);
                nextReviewId[0]++;

                if (ulasan.isEmpty()) {
                    Toast.makeText(this, "Ulasan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imageUri == null) {
                    Toast.makeText(this, "Silahkan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                Review review = new Review(ulasan, "", wisata.getId(), userEmail, id);
                String reviewId = databaseReference.child("Review").push().getKey();
                String wisataId = wisata.getId();
                StorageReference imageRef = storage.child("reviews").child(reviewId);

                UploadTask uploadTask = imageRef.putFile(imageUri);
                uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            review.setFoto(downloadUri.toString());
                            DataReview.insert(review)
                                    .addOnSuccessListener(aVoid -> {
                                        DatabaseReference reviewRef = databaseReference.child("review-wisata").child(wisataId).push();
                                        reviewRef.setValue(review)
                                                .addOnSuccessListener(aVoid1 -> {
                                                    Toast.makeText(ReviewActivity.this, "Ulasan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(ReviewActivity.this, DetailActivity.class);
                                                    intent.putExtra("Wisata", wisata);
                                                    startActivity(intent);
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(ReviewActivity.this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show());
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(ReviewActivity.this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(ReviewActivity.this, "Gagal mendapatkan URL gambar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ReviewActivity.this, "Gagal mengupload gambar", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(image);
        }
    }
}
