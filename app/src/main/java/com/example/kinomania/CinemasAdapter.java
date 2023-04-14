package com.example.kinomania;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CinemasAdapter extends RecyclerView.Adapter<CinemasAdapter.CinemasViewHolder> {

    Context context;
    private List<Cinema> cinemas;
    private OnItemClickListener listener;

    public CinemasAdapter(Context context, List<Cinema> Cinemas) {
        this.context = context;
        this.cinemas = Cinemas;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CinemasAdapter.CinemasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cinemaItems = LayoutInflater.from(context).inflate(R.layout.cinema_item, parent, false);
        return new CinemasViewHolder(cinemaItems);
    }

    public static class CinemasViewHolder extends RecyclerView.ViewHolder {
        TextView cinemaTextView, addressTextView;
        ImageButton favourite, goRight;
        public CinemasViewHolder(@NonNull View itemView) {
            super(itemView);
            cinemaTextView = itemView.findViewById(R.id.name_of_cinema);
            addressTextView = itemView.findViewById(R.id.address_of_cinema);
            favourite = itemView.findViewById(R.id.button_favourites);
            goRight = itemView.findViewById(R.id.button_go_right);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CinemasAdapter.CinemasViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cinema cinema = cinemas.get(position);
        holder.cinemaTextView.setText(cinema.getName());
        holder.addressTextView.setText(cinema.getAddress());

        // Обработчик нажатия на элемент
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cinemas.size();
    }
}
