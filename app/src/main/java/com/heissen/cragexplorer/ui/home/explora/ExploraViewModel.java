package com.heissen.cragexplorer.ui.home.explora;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.heissen.cragexplorer.MainActivity;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.models.Sector;
import com.heissen.cragexplorer.models.Usuario;
import com.heissen.cragexplorer.request.ApiService;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploraViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Location> mCurrentLocation;
    private MutableLiveData<SupportMapFragment> mMapFragment;
    private FusedLocationProviderClient fused;
    private MutableLiveData<ArrayList<Sector>> mSectores;
    private ArrayList<MarkerOptions> currentList;


    public ExploraViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mMapFragment = new MutableLiveData<>();
        currentList = new ArrayList<>();
        mCurrentLocation = new MutableLiveData<>();
        mSectores=new MutableLiveData<>();
        fused = LocationServices.getFusedLocationProviderClient(context);

    }

    public LiveData<Location> getmCurrentLocation() {
        return mCurrentLocation;
    }

    public LiveData<SupportMapFragment> getMapFragment() {
        return mMapFragment;
    }
    public LiveData<ArrayList<Sector>> getMsectores() {
        return mSectores;
    }

    public void initMapFragment(FragmentManager fragmentManager) {
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentByTag("map_fragment");
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.map_container, mapFragment, "map_fragment")
                    .commit();
        }
        mMapFragment.setValue(mapFragment);

    }



    public void addMarkersToMap(GoogleMap googleMap,ArrayList<Sector>sectores) {
        addMarkerOption(sectores);
        for (MarkerOptions markerOptions : currentList) {
            googleMap.addMarker(markerOptions);
        }
    }

    public void addMarkerOption(ArrayList<Sector> sectores) {
        for (Sector sector : sectores) {
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.tag);
            int width = 100;
            int height = 100;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

            MarkerOptions marker = new MarkerOptions().position(new LatLng(sector.getLatitud(), sector.getLongitud()))
                    .title(sector.getNombre())
                    .snippet(sector.getId()+"")
                    .icon(bitmapDescriptor);
            currentList.add(marker);
        }
    }

    public void getSectores() {
        ApiService.ApiInterface apiService = ApiService.getApiInferface();
        String token = ApiService.leerToken(getApplication());
        Call<ArrayList<Sector>> llamada = apiService.getSectores(token);
        llamada.enqueue(new Callback<ArrayList<Sector>>() {
            @Override
            public void onResponse(Call<ArrayList<Sector>> call, Response<ArrayList<Sector>> response) {
                if (response.isSuccessful()) {
                    mSectores.postValue(response.body());
                    Log.d("salida", response.body().toString());
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
                    mCurrentLocation.postValue(location);
                }

            }
        });
    }

}