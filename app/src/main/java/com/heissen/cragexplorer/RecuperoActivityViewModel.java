package com.heissen.cragexplorer;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.heissen.cragexplorer.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperoActivityViewModel extends AndroidViewModel {
    Context context;
    public RecuperoActivityViewModel(@NonNull Application application) {
        super(application);
        context=application;

    }
    public void  pedidoRecuperoClave(String correo){
        String token= ApiService.leerToken(context);
        Log.d("salida","TOKEN: "+token);
        ApiService.ApiInterface apiInterface= ApiService.getApiInferface();
        Call<String> llamada=apiInterface.inciarRecupero(correo);
        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){

                    Log.d("salida ",response.body().toString());

                }else {

                    Log.d("salida respuesta ",response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("salida falla ",t.getMessage());
            }
        });
    }
}
