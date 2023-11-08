package com.heissen.cragexplorer.ui.loogbook.poyectos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentAscensoBinding;
import com.heissen.cragexplorer.databinding.FragmentPerfilBinding;
import com.heissen.cragexplorer.databinding.FragmentProyectoBinding;
import com.heissen.cragexplorer.ui.loogbook.ascensos.SesionesAdapter;
import com.heissen.cragexplorer.ui.loogbook.sesionDetalle.SesionDetalleViewModel;

public class ProyectoFragment extends Fragment {

    private ProyectoViewModel vm;
    private FragmentProyectoBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.nav_view_logbook).setVisibility(View.VISIBLE);
        vm = new ViewModelProvider(this).get(ProyectoViewModel.class);
        binding = FragmentProyectoBinding.inflate(getLayoutInflater());
        vm.obtenerProyectos();
        vm.getmProyectos().observe(getViewLifecycleOwner(), ascensos -> {
            Log.d("salida", ascensos.toString());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            binding.rvProyectos.setLayoutManager(gridLayoutManager);
            SesionesAdapter adapter = new SesionesAdapter(getActivity(), ascensos, getLayoutInflater());
            binding.rvProyectos.setAdapter(adapter);
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.obtenerProyectos();
        vm.getmProyectos().observe(getViewLifecycleOwner(), ascensos -> {
            Log.d("salida", ascensos.toString());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            binding.rvProyectos.setLayoutManager(gridLayoutManager);
            SesionesAdapter adapter = new SesionesAdapter(getActivity(), ascensos, getLayoutInflater());
            binding.rvProyectos.setAdapter(adapter);
        });
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.nav_view_logbook).setVisibility(View.VISIBLE);
    }


}