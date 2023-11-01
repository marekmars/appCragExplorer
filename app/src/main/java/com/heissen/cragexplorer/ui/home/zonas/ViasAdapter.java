package com.heissen.cragexplorer.ui.home.zonas;

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
import com.heissen.cragexplorer.models.Via;
import com.heissen.cragexplorer.models.Zona;

import java.util.List;

public class ViasAdapter extends RecyclerView.Adapter<ViasAdapter.ViewHolder>{
    private Context context;
    private List<Via> vias;
    private LayoutInflater li;

    public ViasAdapter(Context context, List<Via> vias, LayoutInflater li) {
        this.context = context;
        this.vias = vias;
        this.li = li;
    }
    @NonNull
    @Override
    public ViasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = li.inflate(R.layout.item_zona, parent, false);
        return new ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull ViasAdapter.ViewHolder holder, int position) {
        holder.via.setText(vias.get(position).getNombre());
        holder.grado.setText(vias.get(position).getGrado().getGradoN());
    }

    @Override
    public int getItemCount() {
        return vias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView via;
        TextView grado;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            via = itemView.findViewById(R.id.tvLabelZonaItem);
            grado=itemView.findViewById(R.id.tvGradoItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("via", vias.get(getAdapterPosition()));
                    bundle.putInt("idZona", vias.get(getAdapterPosition()).getIdZona());
                    Navigation.findNavController(v).navigate(R.id.viaFragment, bundle);
                }
            });
        }
    }
}
