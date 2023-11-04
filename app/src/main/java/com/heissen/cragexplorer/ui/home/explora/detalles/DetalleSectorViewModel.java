package com.heissen.cragexplorer.ui.home.explora.detalles;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Sector;
import com.heissen.cragexplorer.models.Zona;
import com.heissen.cragexplorer.request.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleSectorViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Sector> mSector;
    private MutableLiveData<String> mCiudad;
    private MutableLiveData<String> mProvincia;
    private MutableLiveData<Double> mCalificacion;

    private MutableLiveData<ArrayList<SlideModel>> mSlideModel;
    private MutableLiveData<String> mPais;

    private MutableLiveData<List<Zona>> mZonas;

    public DetalleSectorViewModel(@NonNull Application application) {
        super(application);
        context = application;
        mSector = new MutableLiveData<>();
        mCiudad = new MutableLiveData<>();
        mPais = new MutableLiveData<>();
        mProvincia = new MutableLiveData<>();
        mSlideModel = new MutableLiveData<>(new ArrayList<>());
        mZonas=new MutableLiveData<>();
        mCalificacion=new MutableLiveData<>();
    }


    public LiveData<Sector> getmSector() {
        return mSector;
    }

    public LiveData<String> getmCiudad() {
        return mCiudad;
    }

    public LiveData<String> getmProvincia() {
        return mProvincia;
    }

    public LiveData<String> getmPais() {
        return mPais;
    }

    public LiveData<List<Zona>> getmZonas() {
        return mZonas;
    }

    public LiveData<Double> getmCalificacion() {
        return mCalificacion;
    }

    public LiveData<ArrayList<SlideModel>> getmSlideModel() {
        return mSlideModel;
    }

    private void resolveLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            Log.d("salida", geocoder.getFromLocation(latitude, longitude, 1) + "");
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                mCiudad.postValue(address.getFeatureName() + " > ");
                mProvincia.postValue(address.getAdminArea() + " > ");
                mPais.postValue(address.getCountryName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSector(Bundle bundle) {
        int idSector = bundle.getInt("idSector");
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<Sector> llamada = apiService.getSector(token, idSector);
        llamada.enqueue(new Callback<Sector>() {
            @Override
            public void onResponse(Call<Sector> call, Response<Sector> response) {
                if (response.isSuccessful()) {
                    mSector.postValue(response.body());
                    getFotosSector(response.body().getId());
                    zonasSector(response.body().getId());
                    getCalificacionSector(response.body().getId());
                    resolveLocation(response.body().getLatitud(), response.body().getLongitud());
                    Log.d("salida", response.body().toString());
                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<Sector> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });
    }

    private void getFotosSector(int idSector) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<List<String>> llamada = apiService.getFotosSector(token, idSector);
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

    public void getCalificacionSector(int idSector) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<Double> llamada = apiService.getCalificacionSector(token, idSector);
        llamada.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful()) {
                    Log.d("salida", response.body().toString());
                    mCalificacion.setValue(response.body());

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

    public void zonasSector(int idSector) {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<List<Zona>> llamada = apiService.getZonasSector(token, idSector);
        llamada.enqueue(new Callback<List<Zona>>() {
            @Override
            public void onResponse(Call<List<Zona>> call, Response<List<Zona>> response) {
                if (response.isSuccessful()) {
                    Log.d("salida", "ZONAS: "+response.body().toString());
                    mZonas.setValue(response.body());
                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<List<Zona>> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });

    }


}