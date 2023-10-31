package com.heissen.cragexplorer.ui.home.vias.agregarImg;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentAgregarImagenBinding;
import com.heissen.cragexplorer.databinding.FragmentReseniasBinding;
import com.heissen.cragexplorer.models.Via;
import com.heissen.cragexplorer.ui.home.vias.ViaFragment;
import com.heissen.cragexplorer.ui.home.vias.resenias.ReseniasFragmentViewModel;

public class AgregarImagenFragment extends DialogFragment {
    private AgregarImagenViewModel vm;
    private FragmentAgregarImagenBinding binding;
    private ActivityResultLauncher<Intent> imagePickerLauncher;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAgregarImagenBinding.inflate(getLayoutInflater());
        vm = new ViewModelProvider(this).get(AgregarImagenViewModel.class);
        Bundle bundle = getArguments();
        Via via=bundle.getSerializable("via", Via.class);
        Log.d("salida","VIAAAAAAAAAA:" +via.toString());
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        vm.cargarImg(result);

                    }
                }
        );
        binding.btnAgregarImg.setOnClickListener(v -> cargarImagen());


        vm.getmUrliList().observe(getViewLifecycleOwner(), uriList -> {
            uriList.forEach(uri -> Log.d("salida", uri.toString()));
            Log.d("salida", "ENTRO");
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
            binding.rvImagenes.setLayoutManager(gridLayoutManager);
            ImagenesAdapter adapter = new ImagenesAdapter(getActivity(), uriList, getLayoutInflater());
            binding.rvImagenes.setAdapter(adapter);
        });

        binding.btnAgregar.setOnClickListener(v -> {
            vm.agregarFotoVia(vm.getmUrliList().getValue(), via.getId());
           dismiss();
            ((ViaFragment) getParentFragment()).onDialogDismissed();
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
        window.setAttributes(params);;

        return dialog;

    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imagePickerLauncher.launch(intent);
    }

}