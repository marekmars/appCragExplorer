package com.heissen.cragexplorer.ui.loogbook.ascensos;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.request.ApiService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SesionesAdapter extends RecyclerView.Adapter<SesionesAdapter.ViewHolder> {
    private Context context;
    private List<Sesion> sesiones;
    private LayoutInflater li;
    private  Bundle bundle;
    public SesionesAdapter(Context context, List<Sesion> sesiones, LayoutInflater li) {
        this.context = context;
        this.sesiones = sesiones;
        this.li = li;

        bundle= new Bundle();
    }

    @NonNull
    @Override
    public SesionesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = li.inflate(R.layout.item_sesion, parent, false);
        return new ViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull SesionesAdapter.ViewHolder holder, int position) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Address> addresses = null;

        holder.grado.setText(sesiones.get(holder.getAdapterPosition()).getVia().getGrado().getGradoN());
        holder.metros.setText(sesiones.get(holder.getAdapterPosition()).getVia().getAltura() + " Mts");
        switch (sesiones.get(holder.getAdapterPosition()).getIdTipo()) {
            case 1:
                holder.imgTipo.setImageResource(R.drawable.ic_onsight);
                break;
            case 2:
                holder.imgTipo.setImageResource(R.drawable.ic_flash);
                break;
            case 3:
                holder.imgTipo.setImageResource(R.drawable.ic_redpoint);
                break;
            case 4:
                holder.imgTipo.setImageResource(R.drawable.ic_proyect);
                break;
            case 5:
                holder.imgTipo.setImageResource(R.drawable.baseline_repeat_24);
                break;
        }


        try {
            addresses = geocoder.getFromLocation(sesiones.get(holder.getAdapterPosition()).getVia().getZona().getSector().getLatitud(), sesiones.get(holder.getAdapterPosition()).getVia().getZona().getSector().getLongitud(), 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            holder.ciudad.setText(address.getFeatureName() + " > ");
            holder.provincia.setText(address.getAdminArea() + " > ");
            holder.pais.setText(address.getCountryName());
            bundle.putString("ciudad",address.getFeatureName() + " > ");
            bundle.putString("provincia",address.getAdminArea() + " > ");
            bundle.putString("pais",address.getCountryName());


        }
        holder.nombreVia.setText(sesiones.get(position).getVia().getNombre());
        holder.fecha.setText(LocalDateTime.parse(sesiones.get(position).getFecha()).format(formatoFecha));
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(context);
        Call<ResponseBody> llamada = apiService.getFotoVia(token, sesiones.get(holder.getAdapterPosition()).getIdVia());
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
        Call<Resenia> llamada2 = apiService.getReseniaViaUsuario(token, sesiones.get(holder.getAdapterPosition()).getIdVia());
        llamada2.enqueue(new Callback<Resenia>() {
            @Override
            public void onResponse(Call<Resenia> call, Response<Resenia> response) {
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        Double rating= response.body().getCalificacion();
                        holder.ratingBarSesion.setRating(rating.floatValue());
                    }

                } else {

                    Log.d("salida", response.raw().message());
                    Log.d("salida", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Resenia> call, Throwable t) {
                Log.d("salida", "ERROR:" + t.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return sesiones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView via;
        TextView grado;
        TextView metros;
        TextView ciudad;
        TextView provincia;
        TextView pais;

        TextView nombreVia;
        TextView fecha;

        ImageView imgTipo;
        ImageView imgVia;
        RatingBar ratingBarSesion;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            via = itemView.findViewById(R.id.tvLabelZonaItem);
            grado = itemView.findViewById(R.id.tvGradoSesionItem);
            metros = itemView.findViewById(R.id.tvMetrosSesionItem);
            ciudad = itemView.findViewById(R.id.tvTituloCardCiudadSesionItem);
            provincia = itemView.findViewById(R.id.tvTituloCardProvSesionItem);
            pais = itemView.findViewById(R.id.tvTituloCardPaisSesionItem);
            fecha = itemView.findViewById(R.id.tvFechaSesionItem);
            nombreVia = itemView.findViewById(R.id.tvViaSesionItem);
            ratingBarSesion = itemView.findViewById(R.id.ratingBarSesionItem);
            imgTipo = itemView.findViewById(R.id.imgTipoSesionItem);
            imgVia = itemView.findViewById(R.id.imgViaSesionItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bundle.putSerializable("sesion", sesiones.get(getAdapterPosition()));
                    Navigation.findNavController(v).navigate(R.id.sesionDetalleFragment, bundle);
                }
            });
        }
    }
}
