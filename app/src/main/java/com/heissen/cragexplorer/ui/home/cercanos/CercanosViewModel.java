package com.heissen.cragexplorer.ui.home.cercanos;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.heissen.cragexplorer.models.Sector;
import com.heissen.cragexplorer.request.ApiService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CercanosViewModel extends AndroidViewModel {

    private FusedLocationProviderClient fused;

    private MutableLiveData<Location> mUbicacionActual;

    private MutableLiveData<ArrayList<Sector>> mSectoresCercanos;

    private Context context;

    public CercanosViewModel(@NonNull Application application) {
        super(application);

        context=application;
        mUbicacionActual = new MutableLiveData<>();
        mSectoresCercanos = new MutableLiveData<>();
        fused = LocationServices.getFusedLocationProviderClient(getApplication());
    }

    public LiveData<Location> getmUbicacionActual() {
        return mUbicacionActual;
    }

    public LiveData<ArrayList<Sector>> getmSectoresCercanos() {
        return mSectoresCercanos;
    }

    public void calcularDistancia(Location location) {
        // Radio de la Tierra en kilómetros
        double radioTierra = 6371.0;
        double latitudActRad = Math.toRadians(location.getLatitude());
        double longitudActRad = Math.toRadians(location.getLongitude());

        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<ArrayList<Sector>> llamada = apiService.getSectores(token);
        llamada.enqueue(new Callback<ArrayList<Sector>>() {
            @Override
            public void onResponse(Call<ArrayList<Sector>> call, Response<ArrayList<Sector>> response) {
                if (response.isSuccessful()) {
                    Map<Sector, Double> sectoresDistancias = new HashMap<>();

                    response.body().forEach(sector -> {
                        double lat = Math.toRadians(sector.getLatitud());
                        double lon = Math.toRadians(sector.getLongitud());
                        double latitudDiferencia = lat - latitudActRad;
                        double longitudDiferencia = lon - longitudActRad;
                        double a = Math.sin(latitudDiferencia / 2) * Math.sin(latitudDiferencia / 2) +
                                Math.cos(latitudActRad) * Math.cos(lat) *
                                        Math.sin(longitudDiferencia / 2) * Math.sin(longitudDiferencia / 2);
                        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                        double distancia = radioTierra * c;

                        sectoresDistancias.put(sector, distancia);
                    });

                    // Ordena el mapa por distancia en orden ascendente y toma los 4 sectores más cercanos.
                    mSectoresCercanos.setValue(sectoresDistancias.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                            .limit(4)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toCollection(ArrayList::new)));


                   /* Log.d("salida", "Los 4 sectores más cercanos: " + mSectoresCercanos.getValue().toString());*/
                } else {
                    Log.d("salida", response.raw().message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Sector>> call, Throwable t) {
                Log.d("salida", t.getMessage());
            }
        });

    }


    public void obtenerUltimaUbicacion() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fused.getLastLocation();
        task.addOnSuccessListener(context.getMainExecutor(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mUbicacionActual.postValue(location);
                    Log.d("salida","UBICACION ACTUAL: "+location.toString());
                }

            }
        });
    }
}