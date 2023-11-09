package com.heissen.cragexplorer.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.heissen.cragexplorer.LoginActivity;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentPerfilBinding;
import com.heissen.cragexplorer.request.ApiService;
import com.heissen.cragexplorer.ui.loogbook.ascensos.SesionesAdapter;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm.getUsuarioActivo();
        vm.getCantidadAsensos();
        vm.getCantidadProyectos();
        vm.getCantidadSesiones();
        vm.getTopGrado();
        vm.getFavoritos();


// Crea una instancia del adaptador y pásale el NavController



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

        vm.getmFavoritos().observe(getViewLifecycleOwner(), favorito -> {
            Log.d("salida", favorito.toString());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            binding.rvFavoritos.setLayoutManager(gridLayoutManager);
            FavoritosAdapter adapter = new FavoritosAdapter(getContext(), favorito, getLayoutInflater());
            binding.rvFavoritos.setAdapter(adapter);

        });
        binding.btnLogout.setOnClickListener(v -> {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("¿Cerrar Sesion?")
                    .setContentText("¿Estás seguro de que deseas cerrar la sesion actual?")
                    .setConfirmText("No")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelButton("Si", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            ApiService.borrarToken(getContext());
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .show();
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.getUsuarioActivo();
        vm.getCantidadAsensos();
        vm.getCantidadProyectos();
        vm.getCantidadSesiones();
        vm.getTopGrado();
        vm.getFavoritos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}