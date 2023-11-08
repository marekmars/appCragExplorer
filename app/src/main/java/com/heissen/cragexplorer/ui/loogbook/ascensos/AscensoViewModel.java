package com.heissen.cragexplorer.ui.loogbook.ascensos;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.heissen.cragexplorer.models.Sector;
import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.request.ApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AscensoViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Sesion>> mAsensos;
    private String token ;
    public AscensoViewModel(@NonNull Application application) {
        super(application);
        mAsensos=new MutableLiveData<>();
        token = ApiService.leerToken(getApplication());
    }

    public LiveData<ArrayList<Sesion>> getmAsensos() {
        return mAsensos;
    }

    public void obtenerAscensos() {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        Call<ArrayList<Sesion>> llamada = apiService.mostrarAscensos(token);
        Log.d("salida","ENTROSESIONES");
        llamada.enqueue(new Callback<ArrayList<Sesion>>() {
            @Override
            public void onResponse(Call<ArrayList<Sesion>> call, Response<ArrayList<Sesion>> response) {
                if (response.isSuccessful()) {
                    mAsensos.setValue(response.body());
                    Log.d("salida", mAsensos.getValue().toString());
                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Sesion>> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });

    }
}