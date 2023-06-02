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

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ListViewHolder> {
    private ArrayList<Review> listReview;
    private Context context;

    public ReviewListAdapter(ArrayList<Review> listReview) {
        this.listReview = listReview;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        Review review = listReview.get(position);
        holder.tvUlasan.setText(review.getUlasan());
        holder.tvEmail.setText(review.getEmail());
        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(review.getFoto())
                .into(holder.img);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ReviewDetailActivity.class);
            intent.putExtra("Review", review);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvUlasan;
        ImageView img;
        TextView tvEmail;

        public ListViewHolder(View itemView) {
            super(itemView);
            tvUlasan = itemView.findViewById(R.id.tvUlasan);
            img = itemView.findViewById(R.id.imgReview);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }
    }
}

