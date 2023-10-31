package com.heissen.cragexplorer.ui.home.explora.detalles;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Zona;

import java.util.List;

public class ZonasAdapter extends RecyclerView.Adapter<ZonasAdapter.ViewHolder>{
    private Context context;
    private List<Zona> zonas;
    private LayoutInflater li;

    public  ZonasAdapter(Context context, List<Zona> zonas, LayoutInflater li) {
        this.context = context;
        this.zonas = zonas;
        this.li = li;
    }
    @NonNull
    @Override
    public ZonasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = li.inflate(R.layout.item_zona, parent, false);
        return new ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull ZonasAdapter.ViewHolder holder, int position) {
        Log.d("salida","zonas: "+zonas.get(position).getNombre());
        holder.zona.setText(zonas.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return zonas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView zona;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            zona = itemView.findViewById(R.id.tvLabelZonaItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("zona", zonas.get(getAdapterPosition()));
                    bundle.putInt("idSector", zonas.get(getAdapterPosition()).getIdSector());
                    Navigation.findNavController(v).navigate(R.id.zonaFragment, bundle);
                }
            });
        }
    }
}
