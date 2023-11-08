package com.heissen.cragexplorer.ui.home.vias.resenias;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.request.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReseniasFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Resenia>> mResenias;

    public ReseniasFragmentViewModel(@NonNull Application application) {
        super(application);
        mResenias = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<ArrayList<Resenia>> getmResenias() {
        return mResenias;
    }

    public void getResenias(int idVia) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<ArrayList<Resenia>> llamada = apiService.getReseniasVia(token, idVia);
        llamada.enqueue(new Callback<ArrayList<Resenia>>() {
            @Override
            public void onResponse(Call<ArrayList<Resenia>> call, Response<ArrayList<Resenia>> response) {
                if (response.isSuccessful()) {
                    mResenias.setValue(response.body());

                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Resenia>> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }
}
