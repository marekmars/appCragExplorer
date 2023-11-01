package com.heissen.cragexplorer.ui.home.vias.agregarImg.selectorFecha;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.DatePicker;

import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentAgregarImagenBinding;
import com.heissen.cragexplorer.databinding.FragmentDateBinding;
import com.heissen.cragexplorer.ui.home.vias.ViaFragment;
import com.heissen.cragexplorer.ui.home.vias.agregarImg.AgregarImagenFragment;
import com.heissen.cragexplorer.ui.home.vias.agregarImg.AgregarImagenViewModel;

import java.time.LocalDateTime;
import java.util.Calendar;

public class DateFragment extends DialogFragment {
    private DateViewModel vm;
    private FragmentDateBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentDateBinding.inflate(getLayoutInflater());
        vm = new ViewModelProvider(this).get(DateViewModel.class);

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                LocalDateTime selectedDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
                // Llama al método de la actividad principal para actualizar la variable
                ((AgregarImagenFragment) getParentFragment()).updateSelectedDate(selectedDate);
            }
        });
        return binding.getRoot();
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


    /*private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    LocalDateTime selectedDate = LocalDateTime.of(year, month + 1, day, 0, 0);
                    // Llama al método de la actividad principal para actualizar la variable
                    ((AgregarImagenFragment) getParentFragment()).updateSelectedDate(selectedDate);
                }
            };*/
}