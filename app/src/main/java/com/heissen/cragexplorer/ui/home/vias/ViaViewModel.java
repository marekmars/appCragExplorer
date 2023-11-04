package com.heissen.cragexplorer.ui.home.vias;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Favorito;
import com.heissen.cragexplorer.models.Sector;
import com.heissen.cragexplorer.models.Via;
import com.heissen.cragexplorer.request.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViaViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<SlideModel>> mSlideModel;

    private MutableLiveData<Drawable> mDrawableEstilo;
    private MutableLiveData<Double> mCalificacion;

    private MutableLiveData<Drawable> mImagen;

    private Context context;

    public ViaViewModel(@NonNull Application application) {
        super(application);
        mSlideModel = new MutableLiveData<>(new ArrayList<>());
        mCalificacion = new MutableLiveData<>();
        mDrawableEstilo = new MutableLiveData<>();

        mImagen = new MutableLiveData<>();
        context = application;
    }

    public LiveData<ArrayList<SlideModel>> getmSlideModel() {
        return mSlideModel;
    }

    public LiveData<Double> getmCalificacion() {
        return mCalificacion;
    }

    public LiveData<Drawable> getmDrawableEstilo() {
        return mDrawableEstilo;
    }

    public LiveData<Drawable> getmImagen() {
        return mImagen;
    }



    public void getFotosVia(int idVia) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<List<String>> llamada = apiService.getFotosVia(token, idVia);
        llamada.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    Log.d("salida", response.body().toString());
                    List<SlideModel> slideModels = new ArrayList<>();
                    if (response.body() != null && !response.body().isEmpty()) {
                        response.body().forEach(fotoUrl -> {
                            slideModels.add(new SlideModel(ApiService.URL_BASE + fotoUrl, ScaleTypes.CENTER_CROP));
                        });
                    } else {

                        slideModels.add(new SlideModel(R.drawable.via_default, ScaleTypes.CENTER_CROP));
                    }

                    mSlideModel.postValue((ArrayList<SlideModel>) slideModels);

                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }

    public void getCalificacionVia(int idVia) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<Double> llamada = apiService.getCalificacionVia(token, idVia);
        llamada.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful()) {
                    Log.d("salida", response.body().toString());
                    mCalificacion.setValue(response.body());
                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }

    public void agregarFavorito(int idVia) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<Favorito> llamada = apiService.agregarBorrarFavorito(token, idVia);
        llamada.enqueue(new Callback<Favorito>() {
            @Override
            public void onResponse(Call<Favorito> call, Response<Favorito> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mImagen.setValue(getApplication().getResources().getDrawable(R.drawable.baseline_favorite_24));
                        Log.d("salida", response.body().toString());
                    }else{
                        mImagen.setValue(getApplication().getResources().getDrawable(R.drawable.baseline_favorite_border_24));
                    }
                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Favorito> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });

    }
    public void chquearFavorito(int idVia) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<Favorito> llamada = apiService.chequearFavorito(token, idVia);
        llamada.enqueue(new Callback<Favorito>() {
            @Override
            public void onResponse(Call<Favorito> call, Response<Favorito> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mImagen.setValue(getApplication().getResources().getDrawable(R.drawable.baseline_favorite_24));
                        Log.d("salida", response.body().toString());
                    }else{
                        mImagen.setValue(getApplication().getResources().getDrawable(R.drawable.baseline_favorite_border_24));
                    }
                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Favorito> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });

    }

    public void setEstilo(int idEstilo) {
        switch (idEstilo) {
            case 1:
                mDrawableEstilo.setValue(ContextCompat.getDrawable(context, R.drawable.ic_placa));
                break;
            case 2:
                mDrawableEstilo.setValue(ContextCompat.getDrawable(context, R.drawable.ic_desplome));
                break;
            case 3:
                mDrawableEstilo.setValue(ContextCompat.getDrawable(context, R.drawable.ic_slab));
                break;
        }
    }
}