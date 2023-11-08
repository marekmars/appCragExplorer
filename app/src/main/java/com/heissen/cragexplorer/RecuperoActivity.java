package com.heissen.cragexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;


import com.heissen.cragexplorer.databinding.ActivityRecuperoBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RecuperoActivity extends AppCompatActivity {
    private RecuperoActivityViewModel vm;
    private ActivityRecuperoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RecuperoActivityViewModel.class);
        binding = ActivityRecuperoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnRecupero2.setOnClickListener(v -> {
            vm.pedidoRecuperoClave(binding.etCorreoRecupero.getText().toString());
            new SweetAlertDialog(this, SweetAlertDialog.BUTTON_CONFIRM)
                    .setTitleText("Recuperacion Exitosa!")
                    .setContentText("Si el mail es correcto, se enviara un correo a su cuenta para continuar con el proceso de recuperación de clave")
                    .setConfirmText("Ok")

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent intent = new Intent(getApplication(), LoginActivity.class);
                            startActivity(intent);
                            sDialog.dismissWithAnimation();
                        }
                    })

                    .show();


        });
    }
}