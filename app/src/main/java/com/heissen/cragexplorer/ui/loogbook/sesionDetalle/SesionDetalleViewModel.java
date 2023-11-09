package com.heissen.cragexplorer.ui.loogbook.sesionDetalle;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.heissen.cragexplorer.LoginActivity;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.request.ApiService;
import com.heissen.cragexplorer.ui.home.vias.resenias.ReseniasFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SesionDetalleViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<SlideModel>> mSlideModel;
    private MutableLiveData<Resenia> mResenia;
    private MutableLiveData<Drawable> mDrawable;
    private MutableLiveData<Drawable> mDrawableTipo;
    private MutableLiveData<Sesion> mSesion;

    private String token;

    public SesionDetalleViewModel(@NonNull Application application) {
        super(application);
        mSlideModel = new MutableLiveData<>(new ArrayList<>());
        mResenia = new MutableLiveData<>();
        mDrawable = new MutableLiveData<>();
        mDrawableTipo = new MutableLiveData<>();
        token = ApiService.leerToken(getApplication());
        mSesion = new MutableLiveData<>();
    }

    public LiveData<ArrayList<SlideModel>> getmSlideModel() {
        return mSlideModel;
    }

    public LiveData<Drawable> getmDrawable() {
        return mDrawable;
    }

    public LiveData<Drawable> getmDrawableTipo() {
        return mDrawableTipo;
    }

    public LiveData<Resenia> getmResenias() {
        return mResenia;
    }

    public LiveData<Sesion> getmSesion() {
        return mSesion;
    }

    public void getFotosViaUsuario(int idVia) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        Call<List<String>> llamada = apiService.getFotoUsuarioSesion(token, idVia);
        llamada.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<SlideModel> slideModels = new ArrayList<>();
                if (response.isSuccessful()) {
                    if (response.body() != null && !response.body().isEmpty()) {
                        response.body().forEach(fotoUrl -> {
                            Log.d("salida", "URL:" + fotoUrl);
                            slideModels.add(new SlideModel(ApiService.URL_BASE + fotoUrl, ScaleTypes.CENTER_CROP));
                        });
                    } else {
                        Log.d("salida", "ELSE: " + response.raw().toString());
                        slideModels.add(new SlideModel(R.drawable.via_default, ScaleTypes.CENTER_CROP));
                    }

                    mSlideModel.postValue((ArrayList<SlideModel>) slideModels);

                } else {
                    Log.d("salida", "ERROR: " + response.raw().message());
                    slideModels.add(new SlideModel(R.drawable.via_default, ScaleTypes.CENTER_CROP));
                    Log.d("salida", "tamaño: " + slideModels.size() + "");
                    Log.d("salida", "slider: " + slideModels.get(0).toString() + "");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }

    public void getResenia(int idVia) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        Call<Resenia> llamada = apiService.getReseniaViaUsuario(token, idVia);
        llamada.enqueue(new Callback<Resenia>() {
            @Override
            public void onResponse(Call<Resenia> call, Response<Resenia> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mResenia.setValue(response.body());
                    }


                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Resenia> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }

    public void borrarSesion(int idSesion) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();

        Call<Boolean> llamada = apiService.eliminarSesion(token, idSesion);
        llamada.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Log.d("salida", "BORRO: " + response.body().toString());


                } else {
                    Log.d("salida", "ELSEBORRAR: " + response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("salida", "Borrar: " + t.getMessage());
            }
        });
    }

    public void obtenerSesion(int idSesion) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        Call<Sesion> llamada = apiService.obtenerSesion(token, idSesion);
        llamada.enqueue(new Callback<Sesion>() {
            @Override
            public void onResponse(Call<Sesion> call, Response<Sesion> response) {
                if (response.isSuccessful()) {
                    mSesion.setValue(response.body());
                    Log.d("salida", "BORRO: " + response.body().toString());


                } else {
                    Log.d("salida", "ELSEBORRAR: " + response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Sesion> call, Throwable t) {
                Log.d("salida", "Borrar: " + t.getMessage());
            }
        });
    }

    public void setDrawableEstilo(int idEstilo) {
        Log.d("salida", idEstilo + "");
        switch (idEstilo) {
            case 1:
                mDrawable.setValue(ContextCompat.getDrawable(getApplication(), R.drawable.ic_placa));
                break;
            case 2:
                mDrawable.setValue(ContextCompat.getDrawable(getApplication(), R.drawable.ic_desplome));
                break;
            case 3:
                mDrawable.setValue(ContextCompat.getDrawable(getApplication(), R.drawable.ic_slab));
                break;
        }
    }

    public void setDrawableTipo(int idTipo) {
        Log.d("salida", idTipo + "");
        switch (idTipo) {
            case 1:
                mDrawableTipo.setValue(ContextCompat.getDrawable(getApplication(), R.drawable.ic_onsight));
                break;
            case 2:
                mDrawableTipo.setValue(ContextCompat.getDrawable(getApplication(), R.drawable.ic_flash));
                break;
            case 3:
                mDrawableTipo.setValue(ContextCompat.getDrawable(getApplication(), R.drawable.ic_redpoint));
                break;
            case 4:
                mDrawableTipo.setValue(ContextCompat.getDrawable(getApplication(), R.drawable.ic_proyect));
                break;
            case 5:
                mDrawableTipo.setValue(ContextCompat.getDrawable(getApplication(), R.drawable.baseline_repeat_24));
                break;
        }
    }
}