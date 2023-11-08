package com.heissen.cragexplorer.ui.loogbook.sesionDetalle.sesionEditar;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Foto;
import com.heissen.cragexplorer.request.ApiService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagenesAdapterBorrado extends RecyclerView.Adapter<ImagenesAdapterBorrado.ViewHolder> {
    private Context context;
    private List<Foto> fotoList;
    private LayoutInflater li;
    private String token;
    private OnImageDeleteListener deleteListener; // Agregar el listener

    // Agregar la interfaz OnImageDeleteListener
    public interface OnImageDeleteListener {
        void onImageDeleted(int position);
    }

    public ImagenesAdapterBorrado(Context context, List<Foto> uriList, LayoutInflater li, OnImageDeleteListener deleteListener) {
        this.context = context;
        this.fotoList = uriList;
        token = ApiService.leerToken(context);
        this.li = li;
        this.deleteListener = deleteListener; // Inicializar el listener
    }

    @NonNull
    @Override
    public ImagenesAdapterBorrado.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = li.inflate(R.layout.item_img_borrado, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagenesAdapterBorrado.ViewHolder holder, int position) {
        Glide.with(context)
                .load(ApiService.URL_BASE + fotoList.get(holder.getAdapterPosition()).getUrl())
                .placeholder(R.drawable.baseline_add_a_photo_24)
                .into(holder.img);
        holder.btnBorrar.setOnClickListener(v -> {
            ApiService.ApiInterface apiService = ApiService.getApiInferface();
            Call<ResponseBody> llamada = apiService.borrarFoto(token, fotoList.get(position).getId());
            llamada.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("salida", "BORRO: " + response.body());
                        deleteListener.onImageDeleted(holder.getAdapterPosition());
                    } else {
                        Log.d("salida", "ELSEBORRAR: " + response.raw().message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("salida", "Borrar: " + t.getMessage());
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return fotoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ImageButton btnBorrar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgNueva);
            btnBorrar = itemView.findViewById(R.id.btnBorrarImg);
        }
    }
}
