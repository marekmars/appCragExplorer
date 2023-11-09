package com.heissen.cragexplorer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.heissen.cragexplorer.databinding.ActivityLoginBinding;
import com.heissen.cragexplorer.databinding.ActivityRegisterBinding;
import com.heissen.cragexplorer.models.Usuario;

public class RegisterActivity extends AppCompatActivity {
    private RegisterActivityViewModel vm;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private ActivityRegisterBinding binding;
    private Uri imgUri;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RegisterActivityViewModel.class);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        imgUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.avatar_default);
        binding.cvImgRegister.setOnClickListener(v -> cargarImagen());

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        vm.cargarImg(result);
                    }
                }
        );

        vm.getmImgUri().observe(this, imgUri -> {
            binding.imgAvatarRegister.setImageURI(imgUri);
            this.imgUri = imgUri;
        });
        binding.btnRegister.setOnClickListener(v ->
        {
            Usuario u = new Usuario(binding.etNombreRegister.getText().toString()
                    , binding.etApellidoRegister.getText().toString()
                    , binding.etCorreoRegister.getText().toString()
                    , binding.etClaveRegister.getText().toString()
                    , imgUri.toString(),false);

            vm.registrarUser(u,binding.etClaveNuevamenteRegister.getText().toString());

        });
        binding.btnLoginR.setOnClickListener(v -> {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });



        setContentView(binding.getRoot());
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
}