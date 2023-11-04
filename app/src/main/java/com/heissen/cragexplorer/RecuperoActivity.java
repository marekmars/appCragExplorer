package com.heissen.cragexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.heissen.cragexplorer.auxiliares.DialogoFragment;
import com.heissen.cragexplorer.databinding.ActivityRecuperoBinding;

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
            DialogoFragment dialog = new DialogoFragment();
            dialog.show(getSupportFragmentManager(), "DialogPersonalizado");
        });
    }
}