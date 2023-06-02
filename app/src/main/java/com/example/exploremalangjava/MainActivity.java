package com.example.exploremalangjava;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvKontak;
    private ArrayList<Wisata> list;
    private EditText searchBar;
    private Spinner dropDown;
    private WisataListAdapter wisataAdapter;
    private DataWisata dataWisata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this); // Inisialisasi FirebaseApp

        searchBar = findViewById(R.id.searchBar);
        dropDown = findViewById(R.id.dropDown);
        rvKontak = findViewById(R.id.rvWisata);
        rvKontak.setHasFixedSize(true);

        list = new ArrayList<>();
        wisataAdapter = new WisataListAdapter(list);
        dataWisata = new DataWisata(); // Inisialisasi DataWisata

        // Set up the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.kategori_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(adapter);

        // Add a listener for the search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filter = s.toString().toLowerCase(Locale.ROOT);
                dataWisata.get().get().addOnSuccessListener(snapshot -> {
                    List<Wisata> filteredList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Wisata wisata = dataSnapshot.getValue(Wisata.class);
                        if (wisata != null) {
                            String nama = wisata.getNama().toLowerCase(Locale.ROOT);
                            String lokasi = wisata.getLokasi().toLowerCase(Locale.ROOT);
                            if (nama.contains(filter) || lokasi.contains(filter)) {
                                filteredList.add(wisata);
                            }
                        }
                    }

                    wisataAdapter.filterList(filteredList);
                });
            }
        });
        // Add a listener for the spinner
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String filter = parent.getItemAtPosition(pos).toString();
                dataWisata.get().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<Wisata> filteredList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Wisata wisata = dataSnapshot.getValue(Wisata.class);
                            if (wisata != null) {
                                wisata.setId(dataSnapshot.getKey());
                                String kategori = wisata.getKategori();
                                if (filter.equals("Semua Kategori") || (kategori != null && kategori.equals(filter))) {
                                    filteredList.add(wisata);
                                }
                            }
                        }
                        wisataAdapter.filterList(filteredList);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("MainActivity", "Error: " + error.getMessage());
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        showRecyclerList();
        dataWisata.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Wisata> wisataList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Wisata wisata = dataSnapshot.getValue(Wisata.class);
                    wisataList.add(wisata);
                }
                list.clear();
                list.addAll(wisataList);
                wisataAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("MainActivity", "Error: " + error.getMessage());
            }
        });
    }
    private void showRecyclerList() {
        rvKontak.setLayoutManager(new LinearLayoutManager(this));
        rvKontak.setAdapter(wisataAdapter);
    }
}


