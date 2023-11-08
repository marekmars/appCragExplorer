package com.heissen.cragexplorer.ui.home.vias.agregarSesion.selectorFecha;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CalendarView;

import com.heissen.cragexplorer.databinding.FragmentDateBinding;
import com.heissen.cragexplorer.ui.home.vias.agregarSesion.AgregarSesionFragment;


import java.time.LocalDateTime;

public class DateFragment extends DialogFragment {
    private DateViewModel vm;
    private FragmentDateBinding binding;

    private LocalDateTime fechaElegida;
    public interface OnDateSelectedListener {
        void onDateSelected(LocalDateTime selectedDate);
    }

    private OnDateSelectedListener dateSelectedListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentDateBinding.inflate(getLayoutInflater());
        vm = new ViewModelProvider(this).get(DateViewModel.class);

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                LocalDateTime selectedDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
                dateSelectedListener.onDateSelected(selectedDate);
            }
        });
        return binding.getRoot();
    }

    public LocalDateTime getFechaElegida() {
        return fechaElegida;
    }
    public void setDateSelectedListener(OnDateSelectedListener listener) {
        this.dateSelectedListener = listener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Set the gravity to the bottom of the screen
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        window.setAttributes(params);
        ;

               // Establece el listener para la selección de fecha


        return dialog;
    }


}