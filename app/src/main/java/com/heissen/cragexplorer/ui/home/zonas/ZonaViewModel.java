package com.heissen.cragexplorer.ui.home.zonas;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.data.BarEntry;
import com.heissen.cragexplorer.models.Sector;
import com.heissen.cragexplorer.models.Via;
import com.heissen.cragexplorer.models.Zona;
import com.heissen.cragexplorer.request.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZonaViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<BarEntry>> mEntries;
    private MutableLiveData<ArrayList<Via>> mVias;


    public ZonaViewModel(@NonNull Application application) {
        super(application);
        mEntries = new MutableLiveData<>(new ArrayList<>());
        mVias = new MutableLiveData<>(new ArrayList<>());

    }

    public LiveData<ArrayList<BarEntry>> getmEntries() {
        return mEntries;
    }

    public LiveData<ArrayList<Via>> getmVias() {
        return mVias;
    }



    public void cargarEntries(Bundle bundle) {
        int idZona = bundle.getSerializable("zona", Zona.class).getId();
        int[] cantViasxGrado = {0, 0, 0, 0, 0};
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<ArrayList<Via>> llamada = apiService.getViasZona(token, idZona);
        llamada.enqueue(new Callback<ArrayList<Via>>() {
            @Override
            public void onResponse(Call<ArrayList<Via>> call, Response<ArrayList<Via>> response) {
                if (response.isSuccessful()) {
                    mVias.postValue(response.body());
                    ArrayList<BarEntry> entries = new ArrayList<>();

                    response.body().forEach(via -> {
                        // Tu lógica para contar vías por grado y agregarlas a 'entries'
                        if (via.getIdGrado() <= 4) {
                            cantViasxGrado[0] += 1;
                        } else if (via.getIdGrado() > 4 && via.getIdGrado() <= 11) {
                            cantViasxGrado[1] += 1;
                        } else if (via.getIdGrado() > 11 && via.getIdGrado() <= 18) {
                            cantViasxGrado[2] += 1;
                        } else if (via.getIdGrado() > 18 && via.getIdGrado() <= 25) {
                            cantViasxGrado[3] += 1;
                        } else {
                            cantViasxGrado[4] += 1;
                        }



                    });

                    entries.add(new BarEntry(0, cantViasxGrado[0]));
                    entries.add(new BarEntry(1, cantViasxGrado[1]));
                    entries.add(new BarEntry(2, cantViasxGrado[2]));
                    entries.add(new BarEntry(3, cantViasxGrado[3]));
                    entries.add(new BarEntry(4, cantViasxGrado[4]));
                    float lineHeight = 0.1f; // Altura de la línea (ajústala según tus preferencias)
                    for (BarEntry entry : entries) {
                        if (entry.getY() == 0) {
                            entry.setY(lineHeight);
                        }
                    }

                    mEntries.postValue(entries);


                    Log.d("salida", response.body().toString());
                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Via>> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }
}