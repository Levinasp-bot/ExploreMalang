package com.example.exploremalangjava;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DataReview {
    private static DatabaseReference databaseReference;
    private static int idCounter = 1;

    static {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://explore-malang-eff3b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = db.getReference(Review.class.getSimpleName());
    }

    public static Task<Void> insert(Review review) {
        String id = String.valueOf(idCounter);
        review.setId(id);
        idCounter++;
        return databaseReference.child(id).setValue(review);
    }

    public static Task<Void> update(String key, Review note) {
        return databaseReference.child(key).setValue(note);
    }

    public static Task<DataSnapshot> delete(String id) {
        Query query = databaseReference.orderByChild("id").equalTo(id);
        return query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    childSnapshot.getRef().removeValue();
                }
            }
        });
    }

    public static Query get() {
        return databaseReference.orderByKey();
    }

    public static Task<DataSnapshot> getAll(String key) {
        return databaseReference.child(key).get();
    }

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}
