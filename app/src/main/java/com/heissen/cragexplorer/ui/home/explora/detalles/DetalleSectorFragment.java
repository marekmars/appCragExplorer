package com.heissen.cragexplorer.ui.home.explora.detalles;

import androidx.lifecycle.ViewModelProvider;

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

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentDetalleSectorBinding;
import com.heissen.cragexplorer.databinding.FragmentHomeBinding;

public class DetalleSectorFragment extends Fragment {

    private DetalleSectorViewModel mViewModel;
    private FragmentDetalleSectorBinding binding;

    public static DetalleSectorFragment newInstance() {
        return new DetalleSectorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleSectorBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        getActivity().findViewById(R.id.nav_view_home).setVisibility(View.GONE);
        Log.d("salida", "" + bundle.getInt("idSector"));
        binding.btnBackSector.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_detalleSectorFragment_to_navigation_home_explora);
        });
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleSectorViewModel.class);
        // TODO: Use the ViewModel
    }

}