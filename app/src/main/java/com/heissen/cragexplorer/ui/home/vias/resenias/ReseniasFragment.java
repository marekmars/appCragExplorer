package com.heissen.cragexplorer.ui.home.vias.resenias;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentReseniasBinding;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.ui.home.explora.detalles.ZonasAdapter;
import com.heissen.cragexplorer.ui.home.vias.ViaViewModel;

import java.util.ArrayList;


public class ReseniasFragment extends DialogFragment {

    private FragmentReseniasBinding binding;

    private ReseniasFragmentViewModel vm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReseniasBinding.inflate(getLayoutInflater());
        vm = new ViewModelProvider(this).get(ReseniasFragmentViewModel.class);
        Bundle bundle = getArguments();

        vm.getResenias(bundle.getInt("idVia"));
        vm.getmResenias().observe(getViewLifecycleOwner(),reseniasAux -> {
            GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(), 1,GridLayoutManager.VERTICAL,false);
            binding.rvResenias.setLayoutManager(gridLayoutManager);
            ReseniasAdapter adapter=new ReseniasAdapter(getActivity(),reseniasAux,getLayoutInflater());
            binding.rvResenias.setAdapter(adapter);
        });

        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Set the gravity to the bottom of the screen
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        return dialog;

    }
}