package com.heissen.cragexplorer.ui.home.vias.agregarSesion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentAgregarSesionBinding;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.models.Via;
import com.heissen.cragexplorer.ui.home.vias.ViaFragment;
import com.heissen.cragexplorer.ui.home.vias.agregarSesion.selectorFecha.DateFragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AgregarSesionFragment extends DialogFragment {
    private AgregarSesionViewModel vm;
    private FragmentAgregarSesionBinding binding;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private int tipoAscenso;

    private String comentario;

    private int intentos;

    private double porcentaje;
    private double calificacion;
    private LocalDateTime selectedDate;
    DateTimeFormatter formatoFecha;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAgregarSesionBinding.inflate(getLayoutInflater());
        vm = new ViewModelProvider(this).get(AgregarSesionViewModel.class);
        porcentaje = 10;
        calificacion=1;
        intentos = 1;
        selectedDate=LocalDateTime.now();
        formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        binding.etFechaAgregar.setText(selectedDate.format(formatoFecha));

        binding.etPorcentajeAgregar.setText(porcentaje + " %");
        binding.etIntentosAgregar.setText(intentos + " Intentos");
        binding.btnPorcentajeMas.setEnabled(false);
        binding.btnPorcentajeMenos.setEnabled(false);
        binding.btnIntentosjeMas.setEnabled(false);
        binding.btnIntentosjeMenos.setEnabled(false);


        Bundle bundle = getArguments();
        Via via = bundle.getSerializable("via", Via.class);
        Log.d("salida", "VIAAAAAAAAAA:" + via.toString());
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        vm.cargarImg(result);

                    }
                }
        );
        binding.ratingBarAgregarSesion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                calificacion =  Double.parseDouble(rating+"");
                Log.d("salida", "CALIFICACION: " + calificacion);
            }
        });
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
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            String formattedDate = selectedDate.format(formatter);
            vm.cargarSesion(new Sesion(via.getId(), porcentaje/100 , formattedDate, tipoAscenso, intentos));
            vm.cargarResenia(new Resenia(via.getId(),binding.etComentario.getText().toString(),calificacion,formattedDate));
            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.viaFragment, bundle);
        });
        binding.btnDatePicker.setOnClickListener(v -> {
            DateFragment dateFragment = new DateFragment();
            dateFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyleDate);
            dateFragment.show(getChildFragmentManager(), "dialogoFecha");
        });
        binding.btnPorcentajeMas.setOnClickListener(v -> {
            vm.plusProcentaje();
            vm.getmPorcentaje().observe(getViewLifecycleOwner(), porcentaje -> this.porcentaje = porcentaje);
            binding.etPorcentajeAgregar.setText(porcentaje + " %");
            Log.d("salida", "porcentaje: "+porcentaje);
        });
        binding.btnPorcentajeMenos.setOnClickListener(v -> {
            vm.minusProcentaje();
            vm.getmPorcentaje().observe(getViewLifecycleOwner(), porcentaje -> this.porcentaje = porcentaje);
            binding.etPorcentajeAgregar.setText(porcentaje + " %");
            Log.d("salida", "porcentaje: "+porcentaje);
        });

        binding.btnIntentosjeMas.setOnClickListener(v -> {
            vm.plusIntentos(tipoAscenso);
            vm.getmIntentos().observe(getViewLifecycleOwner(), intentos -> this.intentos = intentos);
            binding.etIntentosAgregar.setText(intentos + " Intentos");
            Log.d("salida", "intentos: "+intentos);

        });
        binding.btnIntentosjeMenos.setOnClickListener(v -> {
            vm.minusIntentos(tipoAscenso);
            vm.getmIntentos().observe(getViewLifecycleOwner(), intentos -> this.intentos = intentos);
            binding.etIntentosAgregar.setText(intentos + " Intentos");
            Log.d("salida", "intentos: "+intentos);

        });


        toogleButtons();

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
        ;
        return dialog;
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imagePickerLauncher.launch(intent);
    }

    public void updateSelectedDate(LocalDateTime date) {
        selectedDate = date;
        Log.d("salida", selectedDate.format(formatoFecha));
        binding.etFechaAgregar.setText(selectedDate.format(formatoFecha));
    }

    private void toogleButtons() {
        binding.btnOnsight.setOnClickListener(v -> {
            tipoAscenso = 1;
            vm.setmPorcentaje(100);
            vm.setmIntentos(1);
            v.setBackgroundResource(R.drawable.button_pressed_border_grey);
            binding.btnFlash.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnProyect.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRedpoint.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRepite.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnPorcentajeMas.setEnabled(false);
            binding.btnPorcentajeMenos.setEnabled(false);
            porcentaje = 100;
            binding.etPorcentajeAgregar.setText(porcentaje + " %");
            intentos=1;
            binding.etIntentosAgregar.setText(intentos + " Intentos");
            binding.btnIntentosjeMenos.setEnabled(false);
            binding.btnIntentosjeMas.setEnabled(false);
            Log.d("salida", "porcentaje: "+porcentaje);
            Log.d("salida", "intentos: "+intentos);
        });

        binding.btnFlash.setOnClickListener(v -> {
            tipoAscenso = 2;
            vm.setmPorcentaje(100);
            vm.setmIntentos(1);
            v.setBackgroundResource(R.drawable.button_pressed_border_grey);
            binding.btnOnsight.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnProyect.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRedpoint.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRepite.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnPorcentajeMas.setEnabled(false);
            binding.btnPorcentajeMenos.setEnabled(false);
            porcentaje = 100;
            binding.etPorcentajeAgregar.setText(porcentaje + " %");
            intentos=1;
            binding.etIntentosAgregar.setText(intentos + " Intentos");
            binding.btnIntentosjeMenos.setEnabled(false);
            binding.btnIntentosjeMas.setEnabled(false);
            Log.d("salida", "porcentaje: "+porcentaje);
            Log.d("salida", "intentos: "+intentos);
        });

        binding.btnRedpoint.setOnClickListener(v -> {
            tipoAscenso = 3;
            vm.setmPorcentaje(100);
            vm.setmIntentos(2);
            v.setBackgroundResource(R.drawable.button_pressed_border_grey);
            binding.btnFlash.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnProyect.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnOnsight.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRepite.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnPorcentajeMas.setEnabled(false);
            binding.btnPorcentajeMenos.setEnabled(false);
            binding.btnIntentosjeMenos.setEnabled(true);
            binding.btnIntentosjeMas.setEnabled(true);
            porcentaje = 100;
            binding.etPorcentajeAgregar.setText(porcentaje + " %");
            intentos=2;
            binding.etIntentosAgregar.setText(intentos + " Intentos");
            Log.d("salida", "porcentaje: "+porcentaje);
            Log.d("salida", "intentos: "+intentos);
        });
        binding.btnProyect.setOnClickListener(v -> {
            tipoAscenso = 4;
            vm.setmPorcentaje(10);
            vm.setmIntentos(1);
            v.setBackgroundResource(R.drawable.button_pressed_border_grey);
            binding.btnFlash.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnOnsight.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRedpoint.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRepite.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnPorcentajeMas.setEnabled(true);
            binding.btnPorcentajeMenos.setEnabled(true);
            binding.btnIntentosjeMenos.setEnabled(true);
            binding.btnIntentosjeMas.setEnabled(true);
            porcentaje = 10;
            intentos=1;
            binding.etPorcentajeAgregar.setText(porcentaje + " %");
            Log.d("salida", "porcentaje: "+porcentaje);
            Log.d("salida", "intentos: "+intentos);
        });


        binding.btnRepite.setOnClickListener(v -> {
            tipoAscenso = 5;
            vm.setmPorcentaje(100);
            vm.setmIntentos(1);
            v.setBackgroundResource(R.drawable.button_pressed_border_grey);
            binding.btnFlash.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnOnsight.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRedpoint.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnProyect.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnPorcentajeMas.setEnabled(false);
            binding.btnPorcentajeMenos.setEnabled(false);
            binding.btnIntentosjeMenos.setEnabled(true);
            binding.btnIntentosjeMas.setEnabled(true);
            porcentaje = 100;
            intentos=1;
            binding.etPorcentajeAgregar.setText(porcentaje + " %");
            Log.d("salida", "porcentaje: "+porcentaje);
            Log.d("salida", "intentos: "+intentos);
        });


    }


}