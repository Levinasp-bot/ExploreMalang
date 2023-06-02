package com.example.exploremalangjava;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class WisataListAdapter extends RecyclerView.Adapter<WisataListAdapter.ListViewHolder> {
    private ArrayList<Wisata> listWisata;
    private ArrayList<Wisata> filteredList;
    private Context context;

    public WisataListAdapter(ArrayList<Wisata> listWisata) {
        this.listWisata = listWisata;
        this.filteredList = listWisata;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        Wisata wisata = filteredList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(wisata.getFoto())
                .apply(new RequestOptions())
                .into(holder.img);
        holder.tvNama.setText(wisata.getNama());
        holder.tvLokasi.setText(wisata.getLokasi());

        // Menambahkan peristiwa klik pada holder
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Membuat intent ke DetailActivity
                Intent intent = new Intent(context, DetailActivity.class);
                // Mengirim data Wisata yang terkait ke DetailActivity
                intent.putExtra("Wisata", wisata);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filterList(List<Wisata> filteredNames) {
        filteredList = new ArrayList<>(filteredNames);
        notifyDataSetChanged();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvNama, tvLokasi;

        public ListViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvLokasi = itemView.findViewById(R.id.tvLokasi);
        }
    }
}


