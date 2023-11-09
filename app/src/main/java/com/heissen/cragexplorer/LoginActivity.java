package com.heissen.cragexplorer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest.permission;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.heissen.cragexplorer.databinding.ActivityLoginBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    private LoginActivityViewModel vm;
    private ActivityLoginBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private GoogleSignInClient googleSignInClient;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginActivityViewModel.class);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        //PEDIDO DE PERMISOS

        if (!isLocationPermissionGranted()) {
            requestLocationPermission();
        }
        //
        binding.btnLogin.setOnClickListener(v -> vm.login(binding.etCorreoLogin.getText().toString(), binding.etClaveLogin.getText().toString()));
        vm.getmCorreo().observe(this, s -> binding.etCorreoLogin.setText(s));

        binding.btnRegistrarse.setOnClickListener(v -> {
            intent = new Intent(this, RegisterActivity.class);

            startActivity(intent);
        });
        vm.getmGoogleSignInClient().observe(this, googleSignInClient -> {
            this.googleSignInClient = googleSignInClient;
        });


        vm.init();
        binding.btnLoginGoogle.setOnClickListener(v -> {
            googleSignIn();
        });
        binding.btnRecupero.setOnClickListener(v -> {
            intent = new Intent(this, RecuperoActivity.class);
            startActivity(intent);
        });
        binding.btnCerrar.setOnClickListener(v -> {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Salir de la Aplicacion?")
                    .setContentText("¿Estás seguro de que deseas salir de la aplicación?")
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
                            finishAndRemoveTask();
                            finishAffinity();
                        }
                    })
                    .show();
        });


        setContentView(binding.getRoot());
    }

    public void googleSignIn() {
        vm.signOutGoogle();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, vm.RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == vm.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                vm.firebaseAuth(account.getIdToken());
            } catch (Exception e) {

            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(
                this,
                permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El usuario concedió el permiso, realiza las acciones que requieren ubicación aquí
            } else {
                // El usuario denegó el permiso, maneja esto adecuadamente
            }
        }
    }
}