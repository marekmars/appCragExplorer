package com.heissen.cragexplorer.ui.home.vias.agregarImg;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.request.ApiService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ImagenesAdapter extends RecyclerView.Adapter<ImagenesAdapter.ViewHolder> {
    private Context context;
    private List<Uri> uriList;
    private LayoutInflater li;

    public ImagenesAdapter(Context context, List<Uri> uriList, LayoutInflater li) {
        this.context = context;
        this.uriList = uriList;
        this.li = li;
    }

    @NonNull
    @Override
    public ImagenesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = li.inflate(R.layout.item_img, parent, false);
        return new ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull ImagenesAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load( uriList.get(position))
                .placeholder(R.drawable.baseline_add_a_photo_24)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgNueva);


        }
    }
}
