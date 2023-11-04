package com.heissen.cragexplorer.ui.home.cercanos;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentCercanosBinding;
import com.heissen.cragexplorer.ui.home.vias.resenias.ReseniasAdapter;

public class CercanosFragment extends Fragment {

    private CercanosViewModel vm;

    private FragmentCercanosBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(CercanosViewModel.class);
        binding = FragmentCercanosBinding.inflate(getLayoutInflater());
        Log.d("salida", "se creeo el view");
        vm.obtenerUltimaUbicacion();
        vm.getmUbicacionActual().observe(getViewLifecycleOwner(), location -> vm.calcularDistancia(location));
        vm.getmSectoresCercanos().observe(getViewLifecycleOwner(), sectors -> {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            binding.rvSectores.setLayoutManager(gridLayoutManager);
            SectoresAdapter adapter = new SectoresAdapter(getActivity(), sectors, getLayoutInflater());
            binding.rvSectores.setAdapter(adapter);

        });


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_detalleSectorFragment_to_navigation_home_cercanos);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.nav_view_home).setVisibility(View.VISIBLE);
    }


}