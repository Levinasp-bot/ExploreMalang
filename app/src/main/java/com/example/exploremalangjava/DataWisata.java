package com.example.exploremalangjava;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;

public class DataWisata {
    private DatabaseReference databaseReference;

    public DataWisata() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://explore-malang-eff3b-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = db.getReference(Wisata.class.getSimpleName());
    }

    public Task<Void> insert(Wisata wisata) {
        return databaseReference.push().setValue(wisata);
    }

    public Task<Void> update(String key, Wisata note) {
        return databaseReference.child(key).setValue(note);
    }

    public Task<Void> delete(String key) {
        return databaseReference.child(key).removeValue();
    }

    public Query get() {
        return databaseReference.orderByKey();
    }

    public Task<DataSnapshot> getAll(String key) {
        return databaseReference.child(key).get();
    }
}

