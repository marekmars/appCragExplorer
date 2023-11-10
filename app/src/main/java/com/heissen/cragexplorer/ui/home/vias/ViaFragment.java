package com.heissen.cragexplorer.ui.home.vias;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentViaBinding;
import com.heissen.cragexplorer.models.Via;

import com.heissen.cragexplorer.ui.home.vias.agregarSesion.AgregarSesionFragment;
import com.heissen.cragexplorer.ui.home.vias.resenias.ReseniasFragment;

public class ViaFragment extends Fragment {

    private ViaViewModel vm;
    private FragmentViaBinding binding;
    private Via via;



    public static ViaFragment newInstance() {
        return new ViaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        Bundle bundleBack = new Bundle();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        vm = new ViewModelProvider(this).get(ViaViewModel.class);
        binding = FragmentViaBinding.inflate(getLayoutInflater());
        via = bundle.getSerializable("via", Via.class);
        vm.chquearFavorito(via.getId());
        vm.getFotosVia(via.getId());
        vm.getCalificacionVia(via.getId());
        vm.setEstilo(via.getIdEstilo());
        bundleBack.putInt("idSector", via.getZona().getIdSector());
        bundleBack.putSerializable("zona", via.getZona());
        binding.tvViaNombre.setText(via.getNombre());
        binding.tvTituloCardZona.setText(via.getZona().getNombre());
        binding.tvGradoVia.setText(via.getGrado().gradoN);
        binding.tvMetrosVia.setText(via.getAltura() + "");
        binding.tvChapasVia.setText(via.getChapas() + "");

        vm.getmDrawableEstilo().observe(getViewLifecycleOwner(), drawable -> binding.imgEstiloVia.setImageDrawable(drawable));
        vm.getmSlideModel().observe(getViewLifecycleOwner(), slideModels -> {
            binding.imgSliderSectorVia.setImageList(slideModels, ScaleTypes.CENTER_CROP);
        });
        binding.btnBackVia.setOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_viaFragment_to_zonaFragment, bundleBack);

        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_viaFragment_to_zonaFragment, bundleBack);
            }
        });


        vm.getmCalificacion().observe(getViewLifecycleOwner(),calificacion -> binding.ratingBarSector.setRating(calificacion.floatValue()));
        binding.linearCalificaciones.setOnClickListener(v -> {
            ReseniasFragment reseniasFragment = new ReseniasFragment();
            reseniasFragment.setArguments(bundle);
            reseniasFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
            reseniasFragment.show(getChildFragmentManager(), "DialogPersonalizado");
        });

        binding.btnAddSesion.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(R.id.action_viaFragment_to_agregarSesionFragment2,bundle);
        });
        getActivity().findViewById(R.id.nav_view_home).setVisibility(View.GONE);
        binding.btnFavorito.setOnClickListener(v -> {
            vm.agregarFavorito(via.getId());
        });
        vm.getmImagen().observe(getViewLifecycleOwner(), image -> {
            binding.btnFavorito.setImageDrawable(image);
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.getFotosVia(via.getId());
        vm.getmSlideModel().observe(getViewLifecycleOwner(), slideModels -> {
            binding.imgSliderSectorVia.setImageList(slideModels, ScaleTypes.CENTER_CROP);
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }


}