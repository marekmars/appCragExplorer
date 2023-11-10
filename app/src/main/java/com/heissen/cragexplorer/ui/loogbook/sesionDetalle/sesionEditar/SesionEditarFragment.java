package com.heissen.cragexplorer.ui.loogbook.sesionDetalle.sesionEditar;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RatingBar;

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentSesionEditarBinding;
import com.heissen.cragexplorer.models.Resenia;
import com.heissen.cragexplorer.models.Sesion;
import com.heissen.cragexplorer.ui.home.vias.agregarSesion.ImagenesAdapter;
import com.heissen.cragexplorer.ui.home.vias.agregarSesion.selectorFecha.DateFragment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SesionEditarFragment extends Fragment implements ImagenesAdapterBorrado.OnImageDeleteListener {
    private SesionEditarViewModel vm;
    private FragmentSesionEditarBinding binding;
    private ImagenesAdapterBorrado adapter;
    private int intentos;

    private double porcentaje;
    private String comentario;

    private Double calificacion;
    private LocalDateTime selectedDate;
    private int tipoAscenso;
    private Resenia resenia;
    DateTimeFormatter formatoFecha;

    public static SesionEditarFragment newInstance() {
        return new SesionEditarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(SesionEditarViewModel.class);
        binding = FragmentSesionEditarBinding.inflate(getLayoutInflater());
        formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        getActivity().findViewById(R.id.nav_view_logbook).setVisibility(View.GONE);
        Bundle bundle = getArguments();
        Sesion sesion = bundle.getSerializable("sesion", Sesion.class);
        vm.getResenia(sesion.getIdVia());
        tipoAscenso = sesion.getIdTipo();
        selectedDate = LocalDateTime.parse(sesion.getFecha());
        long millis = selectedDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long millisHoy = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        binding.calendarViewEditar.setMaxDate(millisHoy);
        vm.setTipo(sesion.getIdTipo());
        vm.getmBtnFlafh().observe(getViewLifecycleOwner(), color -> binding.btnFlashEditar.setBackground(ContextCompat.getDrawable(getContext(),color)));
        vm.getmBtnOnsight().observe(getViewLifecycleOwner(), color -> binding.btnOnsightEditar.setBackground(ContextCompat.getDrawable(getContext(),color)));
        vm.getmBtnORepeticion().observe(getViewLifecycleOwner(), color -> binding.btnRepiteEditar.setBackground(ContextCompat.getDrawable(getContext(),color)));
        vm.getmBtnRedPoint().observe(getViewLifecycleOwner(), color -> binding.btnRedpointEditar.setBackground(ContextCompat.getDrawable(getContext(),color)));
        vm.getmBtnProyecto().observe(getViewLifecycleOwner(), color -> binding.btnProyectEditar.setBackground(ContextCompat.getDrawable(getContext(),color)));

        // Establece la fecha en el CalendarView
        binding.calendarViewEditar.setDate(millis, true, true);

        vm.getmResenia().observe(getViewLifecycleOwner(), resenia -> {
            calificacion = resenia.getCalificacion();
            comentario = resenia.getComentario();
            this.resenia = resenia;
            binding.ratingBarEditar.setRating(calificacion.floatValue());
            binding.etComentarioEditar.setText(comentario);
        });
        binding.calendarViewEditar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
            }
        });
        binding.etIntentosAgregarEditar.setText(sesion.getIntentos() + "");
        binding.etPorcentajeAgregarEditar.setText(sesion.getPorcentaje() * 100 + "");

        vm.getFotosViaUsuario(sesion.getIdVia());
        vm.getmListaFotos().observe(getViewLifecycleOwner(), fotos -> {
            fotos.forEach(foto -> Log.d("salida", foto.toString()));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
            binding.rvImagenesEditar.setLayoutManager(gridLayoutManager);
            adapter = new ImagenesAdapterBorrado(getActivity(), fotos, getLayoutInflater(), this); // Pasa "this" como listener
            binding.rvImagenesEditar.setAdapter(adapter);
        });

        binding.btnBackEditarSesion.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_sesionEditarFragment_to_sesionDetalleFragment, bundle);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_sesionEditarFragment_to_sesionDetalleFragment, bundle);
            }
        });

        binding.ratingBarEditar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                calificacion = Double.parseDouble(rating + "");
                Log.d("salida", "CALIFICACION: " + calificacion);
            }
        });

        binding.btnPorcentajeMasEditar.setOnClickListener(v -> {
            vm.plusProcentaje();
            vm.getmPorcentaje().observe(getViewLifecycleOwner(), porcentaje -> this.porcentaje = porcentaje);
            binding.etPorcentajeAgregarEditar.setText(porcentaje + " %");
            Log.d("salida", "porcentaje: " + porcentaje);
        });
        binding.btnPorcentajeMenosEditar.setOnClickListener(v -> {
            vm.minusProcentaje();
            vm.getmPorcentaje().observe(getViewLifecycleOwner(), porcentaje -> this.porcentaje = porcentaje);
            binding.etPorcentajeAgregarEditar.setText(porcentaje + " %");
            Log.d("salida", "porcentaje: " + porcentaje);
        });

        binding.btnIntentosjeMasEditar.setOnClickListener(v -> {
            vm.plusIntentos(tipoAscenso);
            vm.getmIntentos().observe(getViewLifecycleOwner(), intentos -> this.intentos = intentos);
            binding.etIntentosAgregarEditar.setText(intentos + " Intentos");
            Log.d("salida", "intentos: " + intentos);

        });
        binding.btnIntentosjeMenosEditar.setOnClickListener(v -> {
            vm.minusIntentos(tipoAscenso);
            vm.getmIntentos().observe(getViewLifecycleOwner(), intentos -> this.intentos = intentos);
            binding.etIntentosAgregarEditar.setText(intentos + " Intentos");
            Log.d("salida", "intentos: " + intentos);

        });

        binding.btnGuardarEdicion.setOnClickListener(v -> {

            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Estas seguro de modificar esta sesion?")
                    .setContentText("No vas a poder recuperar la sesion anterior!")
                    .setConfirmText("Cancelar")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            comentario = binding.etComentarioEditar.getText().toString();
                            Sesion sesionEditada = new Sesion(sesion.getId(), sesion.getIdVia(), porcentaje / 100, selectedDate.toString(), tipoAscenso, intentos);
                            Resenia reseniaEditada = new Resenia(resenia.getId(), sesion.getIdVia(), comentario, calificacion, selectedDate.toString());
                            vm.editarSesion(sesionEditada);
                            vm.editarResenia(reseniaEditada);
                            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.action_sesionEditarFragment_to_sesionDetalleFragment, bundle);

                        }
                    })
                    .show();


        });


        toogleButtons();

        return binding.getRoot();
    }

    @Override
    public void onImageDeleted(int position) {


        // Notifica al adaptador sobre la eliminación

        vm.getmListaFotos().getValue().remove(position);
        vm.getmListaFotos().observe(getViewLifecycleOwner(), fotos -> {
            fotos.forEach(foto -> Log.d("salida", foto.toString()));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
            binding.rvImagenesEditar.setLayoutManager(gridLayoutManager);
            adapter = new ImagenesAdapterBorrado(getActivity(), fotos, getLayoutInflater(), this); // Pasa "this" como listener
            binding.rvImagenesEditar.setAdapter(adapter);
        });
        adapter.notifyItemRemoved(position);

    }

    private void toogleButtons() {
        binding.btnOnsightEditar.setOnClickListener(v -> {
            tipoAscenso = 1;
            vm.setmPorcentaje(100);
            vm.setmIntentos(1);
            v.setBackgroundResource(R.drawable.button_pressed_border_grey);
            binding.btnFlashEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnProyectEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRedpointEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRepiteEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnPorcentajeMasEditar.setEnabled(false);
            binding.btnPorcentajeMenosEditar.setEnabled(false);
            binding.ratingBarEditar.setEnabled(true);
            binding.etComentarioEditar.setEnabled(true);
            porcentaje = 100;
            binding.etPorcentajeAgregarEditar.setText(porcentaje + " %");
            intentos = 1;
            binding.etIntentosAgregarEditar.setText(intentos + " Intentos");
            binding.btnIntentosjeMenosEditar.setEnabled(false);
            binding.btnIntentosjeMasEditar.setEnabled(false);
            Log.d("salida", "porcentaje: " + porcentaje);
            Log.d("salida", "intentos: " + intentos);
        });

        binding.btnFlashEditar.setOnClickListener(v -> {
            tipoAscenso = 2;
            vm.setmPorcentaje(100);
            vm.setmIntentos(1);
            v.setBackgroundResource(R.drawable.button_pressed_border_grey);
            binding.btnOnsightEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnProyectEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRedpointEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRepiteEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnPorcentajeMasEditar.setEnabled(false);
            binding.ratingBarEditar.setEnabled(true);
            binding.etComentarioEditar.setEnabled(true);
            binding.btnPorcentajeMenosEditar.setEnabled(false);
            porcentaje = 100;
            binding.etPorcentajeAgregarEditar.setText(porcentaje + " %");
            intentos = 1;
            binding.etIntentosAgregarEditar.setText(intentos + " Intentos");
            binding.btnIntentosjeMenosEditar.setEnabled(false);
            binding.btnIntentosjeMasEditar.setEnabled(false);
            Log.d("salida", "porcentaje: " + porcentaje);
            Log.d("salida", "intentos: " + intentos);
        });

        binding.btnRedpointEditar.setOnClickListener(v -> {
            tipoAscenso = 3;
            vm.setmPorcentaje(100);
            vm.setmIntentos(2);
            v.setBackgroundResource(R.drawable.button_pressed_border_grey);
            binding.btnFlashEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnProyectEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnOnsightEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRepiteEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnPorcentajeMasEditar.setEnabled(false);
            binding.btnPorcentajeMenosEditar.setEnabled(false);
            binding.btnIntentosjeMenosEditar.setEnabled(true);
            binding.ratingBarEditar.setEnabled(true);
            binding.etComentarioEditar.setEnabled(true);
            binding.btnIntentosjeMasEditar.setEnabled(true);
            porcentaje = 100;
            binding.etPorcentajeAgregarEditar.setText(porcentaje + " %");
            intentos = 2;
            binding.etIntentosAgregarEditar.setText(intentos + " Intentos");
            Log.d("salida", "porcentaje: " + porcentaje);
            Log.d("salida", "intentos: " + intentos);
        });
        binding.btnProyectEditar.setOnClickListener(v -> {
            tipoAscenso = 4;
            vm.setmPorcentaje(10);
            vm.setmIntentos(1);
            v.setBackgroundResource(R.drawable.button_pressed_border_grey);
            binding.btnFlashEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnOnsightEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRedpointEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRepiteEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnPorcentajeMasEditar.setEnabled(true);
            binding.btnPorcentajeMenosEditar.setEnabled(true);
            binding.btnIntentosjeMenosEditar.setEnabled(true);
            binding.btnIntentosjeMasEditar.setEnabled(true);
            binding.ratingBarEditar.setEnabled(true);
            binding.etComentarioEditar.setEnabled(true);
            porcentaje = 10;
            intentos = 1;
            binding.etPorcentajeAgregarEditar.setText(porcentaje + " %");
            Log.d("salida", "porcentaje: " + porcentaje);
            Log.d("salida", "intentos: " + intentos);
        });


        binding.btnRepiteEditar.setOnClickListener(v -> {
            tipoAscenso = 5;
            vm.setmPorcentaje(100);
            vm.setmIntentos(1);
            v.setBackgroundResource(R.drawable.button_pressed_border_grey);
            binding.btnFlashEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnOnsightEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnRedpointEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.btnProyectEditar.setBackgroundResource(R.drawable.button_pressed_border);
            binding.ratingBarEditar.setEnabled(false);
            binding.etComentarioEditar.setEnabled(false);
            binding.btnPorcentajeMasEditar.setEnabled(false);
            binding.btnPorcentajeMenosEditar.setEnabled(false);
            binding.btnIntentosjeMenosEditar.setEnabled(true);
            binding.btnIntentosjeMasEditar.setEnabled(true);
            porcentaje = 100;
            intentos = 1;
            binding.etPorcentajeAgregarEditar.setText(porcentaje + " %");
            Log.d("salida", "porcentaje: " + porcentaje);
            Log.d("salida", "intentos: " + intentos);
        });


    }
}