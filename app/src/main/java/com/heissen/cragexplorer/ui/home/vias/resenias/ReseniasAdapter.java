package com.heissen.cragexplorer.ui.home.vias.resenias;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.models.Zona;
import com.heissen.cragexplorer.request.ApiService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReseniasAdapter extends RecyclerView.Adapter<ReseniasAdapter.ViewHolder> {
    private Context context;
    private List<Resenia> resenias;
    private LayoutInflater li;

    public ReseniasAdapter(Context context, List<Resenia> resenias, LayoutInflater li) {
        this.context = context;
        this.resenias = resenias;
        this.li = li;
    }

    @NonNull
    @Override
    public ReseniasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = li.inflate(R.layout.item_resenia, parent, false);
        return new ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull ReseniasAdapter.ViewHolder holder, int position) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Glide.with(context)
                .load(ApiService.URL_BASE + resenias.get(position).getUsuario().getAvatar())
                .placeholder(R.drawable.avatar_default)
                .into(holder.imgAvatarResenia);
        holder.tvUsuarioResenia.setText(resenias.get(position).getUsuario().toString());
        holder.ratingBarResenia.setRating(Float.parseFloat(resenias.get(position).getCalificacion()+""));

        holder.tvFechaResenia.setText(LocalDateTime.parse(resenias.get(position).getFecha()).format(formatoFecha));
        holder.tvComentarioResenia.setText(resenias.get(position).getComentario());
    }

    @Override
    public int getItemCount() {
        return resenias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatarResenia;
        TextView tvUsuarioResenia;
        RatingBar ratingBarResenia;
        TextView tvFechaResenia;
        TextView tvComentarioResenia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatarResenia = itemView.findViewById(R.id.imgAvatarResenia);
            tvUsuarioResenia = itemView.findViewById(R.id.tvUsuarioResenia);
            ratingBarResenia = itemView.findViewById(R.id.ratingBarResenia);
            tvFechaResenia = itemView.findViewById(R.id.tvFechaResenia);
            tvComentarioResenia = itemView.findViewById(R.id.tvComentarioResenia);

        }
    }
}
