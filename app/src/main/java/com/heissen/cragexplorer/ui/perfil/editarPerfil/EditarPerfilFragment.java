package com.heissen.cragexplorer.ui.perfil.editarPerfil;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.heissen.cragexplorer.LoginActivity;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentEditarPerfilBinding;
import com.heissen.cragexplorer.models.Usuario;
import com.heissen.cragexplorer.request.ApiService;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditarPerfilFragment extends Fragment {

    private EditarPerfilViewModel vm;
    private FragmentEditarPerfilBinding binding;
    private Uri imgUri;
    private Intent intent;
    private Usuario usuarioEditado;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(EditarPerfilViewModel.class);
        binding = FragmentEditarPerfilBinding.inflate(getLayoutInflater());
        Bundle bundle = getArguments();
        usuarioEditado=new Usuario();
        Usuario usuario = bundle.getSerializable("usuario", Usuario.class);
        vm.chequearUsuario(usuario);
        vm.getmGoogleUser().observe(getViewLifecycleOwner(),flag -> {
            binding.etCorreoEditarPerfil.setEnabled(flag);
            binding.etClaveNuevamenteEditarPerfil.setEnabled(flag);
            binding.etClaveEditarPerfil.setEnabled(flag);

        });

        imgUri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.drawable.avatar_default);
        binding.btnBackEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("salida", "ENTRO");
                Navigation.findNavController(v).navigate(R.id.action_editarPerfilFragment_to_perfilFragment, bundle);
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_editarPerfilFragment_to_perfilFragment, bundle);
            }
        });
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        vm.cargarImg(result);
                    }
                }
        );
        vm.getmImgUri().observe(getViewLifecycleOwner(), imgUri -> {
            binding.imgAvatarEditarPerfil.setImageURI(imgUri);
            usuarioEditado.setAvatar(imgUri.toString());
            this.imgUri = imgUri;
        });
        binding.etApellidoEditarPerfil.setText(usuario.getApellido());
        binding.etNombreEditarPerfil.setText(usuario.getNombre());
        binding.etCorreoEditarPerfil.setText(usuario.getCorreo());
        Glide.with(getContext())
                .load(ApiService.URL_BASE + usuario.getAvatar())
                .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                .placeholder(R.drawable.avatar_default)
                .into(binding.imgAvatarEditarPerfil);


        binding.cvImgEditarPerfil.setOnClickListener(v -> cargarImagen());
        binding.btnEditarPerfil.setOnClickListener(v -> {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Editar el Perfil")
                    .setContentText("Esta seguro de guardar los cambios?")
                    .setConfirmText("Cancelar")
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
                          usuarioEditado.setApellido(binding.etApellidoEditarPerfil.getText().toString());
                          usuarioEditado.setNombre(binding.etNombreEditarPerfil.getText().toString());
                          usuarioEditado.setClave(binding.etClaveEditarPerfil.getText().toString());
                          usuarioEditado.setCorreo(binding.etCorreoEditarPerfil.getText().toString());

                            vm.editarUsuario(usuarioEditado, binding.etClaveNuevamenteEditarPerfil.getText().toString());

                        }
                    })
                    .show();

        });

        vm.getmFlagEditado().observe(getViewLifecycleOwner(), flag ->
        {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Perfil Editado")
                    .setContentText("Se edito el perfil de manera exitosa!")
                    .show();
            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_editarPerfilFragment_to_perfilFragment);

        });

        vm.getmFlagRelogueo().observe(getViewLifecycleOwner(), aBoolean -> {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Cambio en clave o correo")
                    .setContentText("Debe volver a Loguearse")
                    .setConfirmText("OK");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                    ApiService.borrarToken(getContext());
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getContext().startActivity(intent);
                }
            });

            sweetAlertDialog.show();
        });


        return binding.getRoot();
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }


}