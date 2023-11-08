package com.heissen.cragexplorer.ui.loogbook.poyectos;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.request.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProyectoViewModel extends AndroidViewModel {
    private String token ;
    private MutableLiveData<ArrayList<Sesion>> mProyectos;
    public ProyectoViewModel(@NonNull Application application) {
        super(application);
        mProyectos=new MutableLiveData<>();
        token = ApiService.leerToken(getApplication());
    }
    public LiveData<ArrayList<Sesion>> getmProyectos() {
        return mProyectos;
    }

    public void obtenerProyectos() {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        Call<ArrayList<Sesion>> llamada = apiService.mostrarProyectos(token);
        Log.d("salida","ENTROSESIONES");
        llamada.enqueue(new Callback<ArrayList<Sesion>>() {
            @Override
            public void onResponse(Call<ArrayList<Sesion>> call, Response<ArrayList<Sesion>> response) {
                if (response.isSuccessful()) {
                    mProyectos.setValue(response.body());
                    Log.d("salida", mProyectos.getValue().toString());
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