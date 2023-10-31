package com.heissen.cragexplorer.ui.home.explora.detalles;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentDetalleSectorBinding;
import com.heissen.cragexplorer.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class DetalleSectorFragment extends Fragment {
    private FragmentDetalleSectorBinding binding;
    private DetalleSectorViewModel vm;
    private String ciudad, provincia, pais;


    public static DetalleSectorFragment newInstance() {
        return new DetalleSectorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleSectorBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(DetalleSectorViewModel.class);
        Bundle bundle = getArguments();
        vm.getSector(bundle);
   /*     vm.getFotosSector(bundle);*/
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        getActivity().findViewById(R.id.nav_view_home).setVisibility(View.GONE);

        vm.getmSector().observe(getViewLifecycleOwner(), sector -> {
            binding.tvSectorNombre.setText(sector.getNombre());
            binding.tvLatitudSector.setText(sector.getLatitud() + "");
            binding.tvLongitudSector.setText(sector.getLongitud() + "");

        });
        vm.getmCiudad().observe(getViewLifecycleOwner(), s -> {
            ciudad = s;
            binding.tvTituloCardCiudad.setText(ciudad);
        });

        vm.getmProvincia().observe(getViewLifecycleOwner(), s -> {
            provincia = s;
            binding.tvTituloCardProv.setText(provincia);
        });

        vm.getmPais().observe(getViewLifecycleOwner(), s -> {
            pais = s;
            binding.tvTituloCardPais.setText(pais);
        });

        vm.getmSlideModel().observe(getViewLifecycleOwner(), slideModels -> {
            binding.imgSliderSector.setImageList(slideModels, ScaleTypes.CENTER_CROP);
        });
        binding.btnBackSector.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_detalleSectorFragment_to_navigation_home_explora);
        });
        vm.getmDrawable().observe(getViewLifecycleOwner(),drawable -> binding.imgCalificacionSector.setImageDrawable(drawable));

        vm.getmZonas().observe(getViewLifecycleOwner(), zonas -> {

            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(), 1,GridLayoutManager.VERTICAL,false);
            binding.rvZonas.setLayoutManager(gridLayoutManager);
            ZonasAdapter adapter=new ZonasAdapter(getActivity(),zonas,getLayoutInflater());
            binding.rvZonas.setAdapter(adapter);
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_detalleSectorFragment_to_navigation_home_explora);
            }
        });
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new ViewModelProvider(this).get(DetalleSectorViewModel.class);
        // TODO: Use the ViewModel
    }

}