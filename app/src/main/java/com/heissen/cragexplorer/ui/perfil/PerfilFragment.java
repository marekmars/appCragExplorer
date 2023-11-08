package com.heissen.cragexplorer.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentPerfilBinding;
import com.heissen.cragexplorer.request.ApiService;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        vm.getmUsuario().observe(getViewLifecycleOwner(),usuario -> {
            binding.tvNombrePerfil.setText(usuario.toString());
            Glide.with(getContext())
                    .load(ApiService.URL_BASE + usuario.getAvatar())
                    .placeholder(R.drawable.avatar_default)
                    .into(binding.imgAvatarPerfil);
        });
        vm.getmCantAscensos().observe(getViewLifecycleOwner(),integer ->
                binding.tvAscensosEditar.setText(integer+""));
        vm.getmCantProyectos().observe(getViewLifecycleOwner(),integer ->
                binding.tvProyectosEditar.setText(integer+""));
        vm.getmCantSesiones().observe(getViewLifecycleOwner(),integer ->
                binding.tvSesionesEditar.setText(integer+""));
        vm.getmTopGrado().observe(getViewLifecycleOwner(),string ->
                binding.tvTopGrado.setText(string));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}