package com.heissen.cragexplorer.ui.loogbook.sesionDetalle.sesionEditar;

import android.app.Application;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heissen.cragexplorer.models.Foto;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.request.ApiService;
import com.heissen.cragexplorer.ui.home.vias.agregarSesion.selectorFecha.DateFragment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SesionEditarViewModel extends AndroidViewModel {
    private MutableLiveData<Resenia> mResenia;
    private String token;

    private MutableLiveData<ArrayList<Foto>> mListaFotos;


    private ApiService.ApiInterface apiService;
    private MutableLiveData<Integer> mIntentos;
    private MutableLiveData<Boolean> mFlag;
    private MutableLiveData<Boolean> mFlag2;

    private MutableLiveData<Integer> mPorcentaje;
    private MutableLiveData<Sesion> mSesion;

    private MutableLiveData<LocalDateTime> mFecha;


    public SesionEditarViewModel(@NonNull Application application) {
        super(application);
        mResenia = new MutableLiveData<>();
        apiService = ApiService.getApiInferface();
        mPorcentaje = new MutableLiveData<>(10);
        mIntentos = new MutableLiveData<>(1);
        token = ApiService.leerToken(application);
        mListaFotos = new MutableLiveData<>();
        mFecha = new MutableLiveData<>();
        mFlag = new MutableLiveData<>();
        mFlag2 = new MutableLiveData<>();
        mSesion=new MutableLiveData<>();
    }


    public LiveData<Resenia> getmResenia() {
        return mResenia;
    }

    public LiveData<ArrayList<Foto>> getmListaFotos() {
        return mListaFotos;
    }

    public LiveData<Integer> getmPorcentaje() {
        return mPorcentaje;
    }

    public LiveData<Integer> getmIntentos() {
        return mIntentos;
    }

    public LiveData<Boolean> getmFlag() {
        return mFlag;
    }

    public LiveData<Boolean> getmFlag2() {
        return mFlag2;
    }

    public LiveData<Sesion> getmSesion() {
        return mSesion;
    }

    public LiveData<LocalDateTime> getmFecha() {
        return mFecha;
    }

    public void setmIntentos(int intentos) {
        this.mIntentos.setValue(intentos);
    }

    public void setmPorcentaje(int porcentaje) {
        this.mPorcentaje.setValue(porcentaje);
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

    public void getFotosViaUsuario(int idVia) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        Call<List<Foto>> llamada = apiService.getFotosUsuario(token, idVia);

        llamada.enqueue(new Callback<List<Foto>>() {
            @Override
            public void onResponse(Call<List<Foto>> call, Response<List<Foto>> response) {
                List<Foto> fotos = new ArrayList<>();
                if (response.isSuccessful()) {
                    if (response.body() != null && !response.body().isEmpty()) {
                        mListaFotos.setValue(new ArrayList<>(response.body()));
                    } else {
                        Log.d("salida", "ELSE: " + response.raw().toString());

                    }

                } else {
                    Log.d("salida", "ERROR: " + response.raw().message());

                }
            }

            @Override
            public void onFailure(Call<List<Foto>> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }

    public void plusProcentaje() {
        if (mPorcentaje.getValue() < 100) {
            int porcentaje = mPorcentaje.getValue();
            porcentaje += 10;
            mPorcentaje.setValue(porcentaje);
        }

    }

    public void minusProcentaje() {
        if (mPorcentaje.getValue() > 10) {
            int porcentaje = mPorcentaje.getValue();
            porcentaje -= 10;
            mPorcentaje.setValue(porcentaje);
        }
    }

    public void plusIntentos(int tipoIntento) {
        if (mIntentos.getValue() < 99 && tipoIntento >= 3) {
            int intentos = mIntentos.getValue();
            intentos += 1;
            mIntentos.setValue(intentos);
        }
    }


    public void minusIntentos(int tipoIntento) {
        if (mIntentos.getValue() > 1 && tipoIntento >= 4) {
            int intentos = mIntentos.getValue();
            intentos -= 1;
            mIntentos.setValue(intentos);
            Log.d("salida", mIntentos.getValue() + "");
        }
        if (mIntentos.getValue() > 2 && tipoIntento == 3) {
            Log.d("salida", mIntentos.getValue() + "");
            int intentos = mIntentos.getValue();
            intentos -= 1;
            mIntentos.setValue(intentos);
        }
    }


    public void editarSesion(Sesion sesion) {
        if (sesion.getIdVia() <= 0) {
            Toast.makeText(getApplication(), "idVia no válido", Toast.LENGTH_SHORT).show();
        }
        if (sesion.getPorcentaje() < 0 || sesion.getPorcentaje() > 100) {
            Toast.makeText(getApplication(), "Porcentaje no válido", Toast.LENGTH_SHORT).show();

        }
        if (sesion.getFecha() == null) {
            Toast.makeText(getApplication(), "La fecha no puede ser nula", Toast.LENGTH_SHORT).show();

        }
        if (sesion.getIdTipo() <= 0) {
            Toast.makeText(getApplication(), "El tipo no puede ser 0", Toast.LENGTH_SHORT).show();

        }
        if (sesion.getIntentos() <= 0) {
            Toast.makeText(getApplication(), "Intentos no válido", Toast.LENGTH_SHORT).show();

        }

        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        Log.d("salida", "sesion editada: " + sesion.toString());

        Call<Sesion> llamada = apiService.editarSesion(token, sesion);

        llamada.enqueue(new Callback<Sesion>() {
            @Override
            public void onResponse(Call<Sesion> call, Response<Sesion> response) {
                if (response.isSuccessful()) {
                    mSesion.setValue(response.body());
                    Log.d("salida", "respuesta: " + response.body().toString());
                } else {
                    Log.d("salida", "ELSE Sesion: " + response.raw());

                }
            }

            @Override
            public void onFailure(Call<Sesion> call, Throwable t) {
                Log.d("salida", "ERROR Sesion: " + t.getMessage());
            }
        });
    }

    public void editarResenia(Resenia resenia) {
        if (resenia.getIdVia() <= 0 || resenia.getCalificacion() <= 0 || resenia.getCalificacion() > 5 ||
                resenia.getFecha() == null) {
            Log.d("salida", "No se creo la reseña");
            return;
        }
        Log.d("salida", "resenia editada: " + resenia.toString());
        ApiService.ApiInterface apiService = ApiService.getApiInferface();

        Call<Resenia> llamada = apiService.editarResenia(token, resenia);

        llamada.enqueue(new Callback<Resenia>() {
            @Override
            public void onResponse(Call<Resenia> call, Response<Resenia> response) {
                if (response.isSuccessful()) {

                    Log.d("salida", "respuesta: " + response.body().toString());
                } else {
                    Log.d("salida", "ELSE Resenia: " + response.raw());

                }
            }

            @Override
            public void onFailure(Call<Resenia> call, Throwable t) {
                Log.d("salida", "ERROR Resenia " + t.getMessage());

            }
        });
    }


}