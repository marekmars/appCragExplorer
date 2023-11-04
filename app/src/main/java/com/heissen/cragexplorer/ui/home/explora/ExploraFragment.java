package com.heissen.cragexplorer.ui.home.explora;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentExploraBinding;
import com.heissen.cragexplorer.models.Sector;

import java.util.ArrayList;

public class ExploraFragment extends Fragment implements OnMapReadyCallback {

    private ExploraViewModel vm;
    private FragmentExploraBinding binding;
    private GoogleMap googleMap;
    private Location currentLocation;

  /*  private ArrayList<Sector> sectores=new ArrayList<>();*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ExploraViewModel.class);
        vm.initMapFragment(getChildFragmentManager());
        binding = FragmentExploraBinding.inflate(getLayoutInflater());
        vm.getMapFragment().observe(getViewLifecycleOwner(), mapFragment -> {
            mapFragment.getMapAsync(this);
        });
        vm.getSectores();
        return binding.getRoot();
    }

    private void updateFragmentContent(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("idSector", id);
        bundle.putInt("fragmentAnterior",R.id.action_detalleSectorFragment_to_navigation_home_explora);
        Log.d("salida", getActivity().getSupportFragmentManager() + "");
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_container);
        navController.navigate(R.id.detalleSectorFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.nav_view_home).setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        vm.obtenerUltimaUbicacion();
        vm.getmCurrentLocation().observe(getViewLifecycleOwner(), location -> {
            currentLocation = location;
            googleMap = map;
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.style));
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .zoom(7.0f)
                    .build();
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(camPos);
            googleMap.animateCamera(update);
            vm.getMsectores().observe(getViewLifecycleOwner(), sectors -> {
                vm.addMarkersToMap(googleMap, sectors);
            /*    sectores=sectors;*/
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        updateFragmentContent(Integer.parseInt(marker.getSnippet()));
                        return true;
                    }
                });
            });
        });
    }
}