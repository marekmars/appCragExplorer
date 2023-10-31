package com.heissen.cragexplorer.ui.home.vias;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
    private MutableLiveData<Drawable> mDrawable;
    private MutableLiveData<Drawable> mDrawableEstilo;
    private Context context;

    public ViaViewModel(@NonNull Application application) {
        super(application);
        mSlideModel = new MutableLiveData<>(new ArrayList<>());
        mDrawable = new MutableLiveData<>();
        mDrawableEstilo = new MutableLiveData<>();
        context = application;
    }

    public LiveData<ArrayList<SlideModel>> getmSlideModel() {
        return mSlideModel;
    }

    public LiveData<Drawable> getmDrawable() {
        return mDrawable;
    }

    public LiveData<Drawable> getmDrawableEstilo() {
        return mDrawableEstilo;
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
                    if (response.body() == 0) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.cero_e));
                    } else if (response.body() == 0.5) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.cero_media_e));
                    } else if (response.body() == 1) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.una_e));
                    } else if (response.body() == 1.5) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.una_media_e));
                    } else if (response.body() == 2) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.dos_e));
                    } else if (response.body() == 2.5) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.dos_media_e));
                    } else if (response.body() == 3) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.tres_e));
                    } else if (response.body() == 3.5) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.tres_media_e));
                    } else if (response.body() == 4) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.cuatro_e));
                    } else if (response.body() == 4.5) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.cuatro_media_e));
                    } else if (response.body() == 5) {
                        mDrawable.setValue(ContextCompat.getDrawable(context, R.drawable.cinco_e));
                    }

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