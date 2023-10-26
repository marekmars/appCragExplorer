package com.heissen.cragexplorer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.gson.Gson;

import com.heissen.cragexplorer.models.Usuario;
import com.heissen.cragexplorer.request.ApiService;


import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {

/*    SharedPreferences preferences;*/
    SharedPreferences.Editor editor;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

      /*  preferences = getApplication().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        editor = preferences.edit();*/
    }

  /*  public SharedPreferences getPreferences() {
        return preferences;
    }*/



    /*public void setearUsuario() {
        String token = ApiService.leerToken(getApplication());
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        Log.d("salida", "entro");
        String[] tokenParts = token.split("\\.");
        byte[] decodedBytes = Base64.getDecoder().decode(tokenParts[1]);
        String jsonString = new String(decodedBytes);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        Log.d("salida", "AVATAR:"+jsonObject.get("FullName").getAsString());
        Call<Usuario> llamada = apiService.obtenerUsuario(token);

        llamada.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    editor.putString("userName", response.body().toString());

                    Glide.with(getApplication())
                            .asBitmap()
                            .load(response.body().getAvatar())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .submit();

                    editor.apply();
                    Log.d("salida", response.body().toString());
                    Log.d("salida", preferences.getString("userAvatar", null));

                } else {
                    Log.d("salida", "ELSE " + response.raw());

                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("salida", "ERROR " + t.getMessage());

            }
        });
    }*/
}
