package com.heissen.cragexplorer.ui.loogbook.sesionDetalle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import cn.pedant.SweetAlert.SweetAlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentAscensoBinding;
import com.heissen.cragexplorer.databinding.FragmentDetalleSectorBinding;
import com.heissen.cragexplorer.databinding.FragmentSesionDetalleBinding;
import com.heissen.cragexplorer.models.Sesion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SesionDetalleFragment extends Fragment {

    private SesionDetalleViewModel vm;
    private FragmentSesionDetalleBinding binding;
private Sesion sesion;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(SesionDetalleViewModel.class);
        binding = FragmentSesionDetalleBinding.inflate(getLayoutInflater());
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Bundle bundle = getArguments();
        vm.obtenerSesion(bundle.getSerializable("sesion", Sesion.class).getId());
        vm.getmSesion().observe(getViewLifecycleOwner(),sesion -> {
            this.sesion=sesion;
            vm.getResenia(sesion.getIdVia());
            vm.getFotosViaUsuario(sesion.getIdVia());
            vm.setDrawableEstilo(sesion.getVia().getIdEstilo());
            vm.setDrawableTipo(sesion.getIdTipo());
            binding.tvViaNombreSesion.setText(sesion.getVia().getNombre());
            binding.tvGradoSesion.setText(sesion.getVia().getGrado().gradoN);
            binding.tvMetrosSesion.setText(sesion.getVia().getAltura() + "");
            binding.tvChapasSesion.setText(sesion.getVia().getChapas() + "");
            binding.tvlIntentosSesion.setText(sesion.getIntentos() + "");
            binding.tvlPorcentajeSesion.setText(sesion.getPorcentaje() * 100 + " %");
            binding.tvlFechajeSesion.setText(LocalDateTime.parse(sesion.getFecha()).format(formatoFecha));
        });


        String ciudad = bundle.getString("ciudad");
        String provincia = bundle.getString("provincia");
        String pais = bundle.getString("pais");

        /*getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);*/
        getActivity().findViewById(R.id.nav_view_logbook).setVisibility(View.GONE);
        binding.btnBackSesion.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_sesionDetalleFragment_to_ascensoFragment);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_sesionDetalleFragment_to_ascensoFragment);
            }
        });
        vm.getmDrawable().observe(getViewLifecycleOwner(), drawable -> binding.imgEstiloSesion.setImageDrawable(drawable));
        vm.getmDrawableTipo().observe(getViewLifecycleOwner(),drawable -> binding.imgTipoSesion.setImageDrawable(drawable) );
        vm.getmSlideModel().observe(getViewLifecycleOwner(), slideModels -> {
            Log.d("salida", "entro observe");
            binding.imgSliderSesion.setImageList(slideModels, ScaleTypes.CENTER_CROP);
        });
        binding.btnEliminarSesion.setOnClickListener(v -> {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Estas seguro de eliminar esta sesion?")
                    .setContentText("No vas a poder recuperar la sesion luego de borrarla!")
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
                            vm.borrarSesion(sesion.getId());
                            Navigation.findNavController(getView()).navigate(R.id.action_sesionDetalleFragment_to_ascensoFragment);

                        }
                    })
                    .show();

        });
        binding.btnEditarSesion.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(R.id.action_sesionDetalleFragment_to_sesionEditarFragment, bundle);
        });

        binding.tvTituloCardCiudadSesion.setText(ciudad);
        binding.tvTituloCardProvSesion.setText(provincia);
        binding.tvTituloCardPaisSesion.setText(pais);
        vm.getmResenias().observe(() -> getLifecycle(), resenia -> {
            Double rating = resenia.getCalificacion();
            binding.ratingBarSesion.setRating(rating.floatValue());
            binding.tvlComentariojeSesion.setText(resenia.getComentario());
        });



        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}