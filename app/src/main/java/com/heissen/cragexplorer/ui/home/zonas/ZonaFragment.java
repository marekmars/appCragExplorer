package com.heissen.cragexplorer.ui.home.zonas;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.auxiliares.CustomBarChartRender;
import com.heissen.cragexplorer.databinding.FragmentZonaBinding;
import com.heissen.cragexplorer.models.Zona;

import java.util.ArrayList;

public class ZonaFragment extends Fragment {

    private ZonaViewModel vm;
    private FragmentZonaBinding binding;
    private BarDataSet dataSet;

    private Zona zona;

    public static ZonaFragment newInstance() {
        return new ZonaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ZonaViewModel.class);
        binding = FragmentZonaBinding.inflate(getLayoutInflater());
        Bundle bundle = getArguments();
        zona = bundle.getSerializable("zona", Zona.class);
        binding.tvZonaNombre.setText(zona.getNombre());

        getActivity().findViewById(R.id.nav_view_home).setVisibility(View.GONE);
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);

        binding.tvTituloCardSector.setText(zona.getSector().getNombre());
        binding.btnBackZona.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_zonaFragment_to_detalleSectorFragment, bundle);
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_zonaFragment_to_detalleSectorFragment, bundle);
            }
        });
        vm.getmVias().observe(getViewLifecycleOwner(), vias -> {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            binding.rvVias.setLayoutManager(gridLayoutManager);
            ViasAdapter adapter = new ViasAdapter(getActivity(), vias, getLayoutInflater());
            binding.rvVias.setAdapter(adapter);

        });
        setUpBarChart();
        vm.cargarEntries(bundle);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    private void setUpBarChart() {
        XAxis xAxis = binding.barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Establece la posición de las etiquetas
        xAxis.setGranularity(1f);
        // Espaciado entre las etiquetas
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"4 - 5+", "6 - 6c+", "7 - 7c+", "8 - 8c+", "9 - 9c"}));

        vm.getmEntries().observe(getViewLifecycleOwner(), entries -> {
            dataSet = new BarDataSet(entries, "Vias");
            xAxis.setTextColor(Color.rgb(114, 114, 114));
            xAxis.setAxisLineColor(Color.TRANSPARENT);
            dataSet.setValueTextColor(Color.rgb(114, 114, 114));
            dataSet.setValueTextSize(16);
            xAxis.setTextSize(16);
            dataSet.setColors(ColorTemplate.PASTEL_COLORS);

            dataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });

            binding.barChart.getAxisRight().setDrawGridLines(false);
            binding.barChart.getXAxis().setDrawGridLines(false);
            binding.barChart.getLegend().setEnabled(false);
            binding.barChart.getAxisRight().setEnabled(false);
            binding.barChart.setDescription(null);
            binding.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    Log.d("salida", e.getX() + "");
                }

                @Override
                public void onNothingSelected() {

                }
            });

            YAxis leftAxis = binding.barChart.getAxisLeft();
            leftAxis.setEnabled(false);
            binding.barChart.setExtraBottomOffset(20f);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            // Crea los datos del gráfico de barras
            BarData data = new BarData(dataSets);
            data.setBarWidth(0.9f); // Ancho de las barras

            // Configura el gráfico
            binding.barChart.setData(data);
            binding.barChart.setFitBars(true); // Ajusta el ancho de las barras
            binding.barChart.animateY(1000); // Animación de barras

            binding.barChart.invalidate(); // Actualiza el gráfico


        });
        CustomBarChartRender barChartRender = new CustomBarChartRender(binding.barChart, binding.barChart.getAnimator(), binding.barChart.getViewPortHandler());
        barChartRender.setRadius(20);
        binding.barChart.setRenderer(barChartRender);
    }

}