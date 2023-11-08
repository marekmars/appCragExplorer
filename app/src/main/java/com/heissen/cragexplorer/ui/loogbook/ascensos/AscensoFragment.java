package com.heissen.cragexplorer.ui.loogbook.ascensos;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

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

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentAscensoBinding;

public class AscensoFragment extends Fragment {

    private AscensoViewModel vm;
    private FragmentAscensoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.nav_view_logbook).setVisibility(View.VISIBLE);
        vm = new ViewModelProvider(this).get(AscensoViewModel.class);
        binding = FragmentAscensoBinding.inflate(getLayoutInflater());
        vm.obtenerAscensos();
        vm.getmAsensos().observe(getViewLifecycleOwner(), ascensos -> {
            Log.d("salida", ascensos.toString());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            binding.rvAscensos.setLayoutManager(gridLayoutManager);
            SesionesAdapter adapter = new SesionesAdapter(getActivity(), ascensos, getLayoutInflater());
            binding.rvAscensos.setAdapter(adapter);

        });


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.obtenerAscensos();
        vm.getmAsensos().observe(getViewLifecycleOwner(), ascensos -> {
            Log.d("salida", ascensos.toString());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            binding.rvAscensos.setLayoutManager(gridLayoutManager);
            SesionesAdapter adapter = new SesionesAdapter(getActivity(), ascensos, getLayoutInflater());
            binding.rvAscensos.setAdapter(adapter);

        });
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.nav_view_logbook).setVisibility(View.VISIBLE);
    }
}