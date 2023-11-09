package com.heissen.cragexplorer.ui.perfil;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.heissen.cragexplorer.models.Favorito;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.models.Usuario;
import com.heissen.cragexplorer.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Usuario> mUsuario;
    private MutableLiveData<Drawable> mAvatar;
    private MutableLiveData<Integer> mCantProyectos;
    private MutableLiveData<Integer> mCantSesiones;
    private MutableLiveData<Integer> mCantAscensos;
    private MutableLiveData<String> mTopGrado;
    private MutableLiveData<List<Favorito>> mFavoritos;
    private String token;
    private ApiService.ApiInterface apiService;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        mUsuario = new MutableLiveData<>();
        mAvatar = new MutableLiveData<>();
        mCantProyectos = new MutableLiveData<>();
        mCantSesiones = new MutableLiveData<>();
        mCantAscensos = new MutableLiveData<>();
        mTopGrado = new MutableLiveData<>();
        mFavoritos= new MutableLiveData<>();
        apiService = ApiService.getApiInferface();
        token = ApiService.leerToken(application);


    }

    public LiveData<Usuario> getmUsuario() {
        return mUsuario;
    }

    public LiveData<Integer> getmCantProyectos() {
        return mCantProyectos;
    }

    public LiveData<Integer> getmCantSesiones() {
        return mCantSesiones;
    }

    public LiveData<Integer> getmCantAscensos() {
        return mCantAscensos;
    }
    public LiveData<List<Favorito>> getmFavoritos() {
        return mFavoritos;
    }

    public LiveData<String> getmTopGrado() {
        return mTopGrado;
    }

    public LiveData<Drawable> getmAvatar() {
        return mAvatar;
    }

    public void getUsuarioActivo() {

        Call<Usuario> llamada = apiService.obtenerUsuario(token);
        llamada.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mUsuario.setValue(response.body());

                        Log.d("salida", response.body().toString());
                    }


                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }
    public void getCantidadAsensos() {

        Call<Integer> llamada = apiService.cantidadAscensos(token);
        llamada.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mCantAscensos.setValue(response.body());

                        Log.d("salida", response.body().toString());
                    }


                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }
    public void getCantidadProyectos() {

        Call<Integer> llamada = apiService.cantidadProyectos(token);
        llamada.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mCantProyectos.setValue(response.body());

                        Log.d("salida", response.body().toString());
                    }


                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }
    public void getCantidadSesiones() {

        Call<Integer> llamada = apiService.cantidadSesiones(token);
        llamada.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mCantSesiones.setValue(response.body());

                        Log.d("salida", response.body().toString());
                    }


                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }
    public void getTopGrado() {

        Call<String> llamada = apiService.topGrado(token);
        llamada.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mTopGrado.setValue(response.body());

                        Log.d("salida", response.body().toString());
                    }


                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }
    public void getFavoritos() {

        Call<List<Favorito>> llamada = apiService.obtenerFavoritos(token);
        llamada.enqueue(new Callback<List<Favorito>>() {
            @Override
            public void onResponse(Call<List<Favorito>> call, Response<List<Favorito>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mFavoritos.setValue(response.body());
                        Log.d("salida", response.body().toString());
                    }
                } else {
                    Log.d("salida", response.raw().message());
                }
            }
            @Override
            public void onFailure(Call<List<Favorito>> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }

}