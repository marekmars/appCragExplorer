package com.heissen.cragexplorer.ui.perfil;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heissen.cragexplorer.MainActivity;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Favorito;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.request.ApiService;
import com.heissen.cragexplorer.ui.home.HomeFragment;
import com.heissen.cragexplorer.ui.home.vias.ViaFragment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolder> {
    private Context context;
    private List<Favorito> favoritos;
    private LayoutInflater li;
    private Bundle bundle;


    public FavoritosAdapter(Context context, List<Favorito> favoritos, LayoutInflater li) {
        this.context = context;
        this.favoritos = favoritos;
        this.li = li;

        bundle = new Bundle();
    }

    @NonNull
    @Override
    public FavoritosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = li.inflate(R.layout.item_favorito, parent, false);
        return new ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull FavoritosAdapter.ViewHolder holder, int position) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(favoritos.get(holder.getAdapterPosition()).getVia().getZona().getSector().getLatitud()
                    , favoritos.get(holder.getAdapterPosition()).getVia().getZona().getSector().getLongitud(), 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            holder.ciudad.setText(address.getFeatureName() + " > ");
            holder.provincia.setText(address.getAdminArea() + " > ");
            holder.pais.setText(address.getCountryName());
            bundle.putString("ciudad", address.getFeatureName() + " > ");
            bundle.putString("provincia", address.getAdminArea() + " > ");
            bundle.putString("pais", address.getCountryName());
        }
        holder.nombreVia.setText(favoritos.get(position).getVia().getNombre());
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(context);
        Call<ResponseBody> llamada = apiService.getFotoVia(token, favoritos.get(holder.getAdapterPosition()).getIdVia());
        llamada.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {

                        Glide.with(context)
                                .load(ApiService.URL_BASE + response.body().string())
                                .placeholder(R.drawable.via_default)
                                .into(holder.imgVia);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Glide.with(context)
                            .load(R.drawable.via_default)
                            .into(holder.imgVia);
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
        return favoritos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ciudad;
        TextView provincia;
        TextView pais;
        TextView nombreVia;
        ImageView imgVia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ciudad = itemView.findViewById(R.id.tvCiudadFavoritos);
            provincia = itemView.findViewById(R.id.tvProvFavoritos);
            pais = itemView.findViewById(R.id.tvPaisFavoritos);
            nombreVia = itemView.findViewById(R.id.tvNombreViaFavoritos);
            imgVia = itemView.findViewById(R.id.imgViaFavoritos);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putSerializable("via", favoritos.get(getAdapterPosition()).getVia());
                    Navigation.findNavController(v).navigate(R.id.viaInfoFragment,bundle);
                }


            });
        }
    }
}
