package com.heissen.cragexplorer.ui.home.cercanos;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Sector;
import com.heissen.cragexplorer.models.Zona;
import com.heissen.cragexplorer.request.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectoresAdapter extends RecyclerView.Adapter<SectoresAdapter.ViewHolder> {
    private Context context;
    private List<Sector> sectors;
    private LayoutInflater li;

    public SectoresAdapter(Context context, List<Sector> sectors, LayoutInflater li) {
        this.context = context;
        this.sectors = sectors;
        this.li = li;
    }

    @NonNull
    @Override
    public SectoresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = li.inflate(R.layout.item_sector, parent, false);
        return new ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull SectoresAdapter.ViewHolder holder, int position) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        final String[] imageUrl = {""};
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(sectors.get(holder.getAdapterPosition()).getLatitud(), sectors.get(holder.getAdapterPosition()).getLongitud(), 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            holder.ciudad.setText(address.getFeatureName() + " > ");
            holder.provincia.setText(address.getAdminArea() + " > ");
            holder.pais.setText(address.getCountryName());
        }
        holder.nombre.setText(sectors.get(position).getNombre());
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(context);
        Call<ResponseBody> llamada = apiService.getFotoSector(token, sectors.get(position).getId());
        llamada.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String imageUrl = ApiService.URL_BASE + response.body().string();
                        Log.d("salida", imageUrl);
                        Glide.with(context)
                                .load(imageUrl)
                                .placeholder(R.drawable.via_default)
                                .into(holder.imgSector);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Log.d("salida", response.raw().message());
                    Log.d("salida", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("salida", "ERROR:" + t.getMessage());
            }
        });
    }


    @Override
    public int getItemCount() {
        return sectors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ciudad;
        TextView provincia;
        TextView pais;
        TextView nombre;
        ImageView imgSector;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ciudad = itemView.findViewById(R.id.tvTituloCardCiudadCercanos);
            provincia = itemView.findViewById(R.id.tvTituloCardProvCercanos);
            pais = itemView.findViewById(R.id.tvTituloCardPaisCercanos);
            nombre = itemView.findViewById(R.id.tvSectorNombreCercanos);
            imgSector = itemView.findViewById(R.id.imgSectorCercanos);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("idSector", sectors.get(getAdapterPosition()).getId());
                    Navigation.findNavController(v).navigate(R.id.detalleSectorFragment, bundle);
                }
            });
        }
    }


}
