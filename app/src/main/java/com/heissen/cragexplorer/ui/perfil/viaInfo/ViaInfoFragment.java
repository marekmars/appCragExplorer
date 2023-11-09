package com.heissen.cragexplorer.ui.perfil.viaInfo;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentViaBinding;
import com.heissen.cragexplorer.models.Via;
import com.heissen.cragexplorer.ui.home.vias.resenias.ReseniasFragment;

public class ViaInfoFragment extends Fragment {

    private ViaInfoViewModel vm;
    private FragmentViaBinding binding;
    private View inflatedView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ViaInfoViewModel.class);
        binding = FragmentViaBinding.inflate(inflater);
        inflatedView = inflater.inflate(R.layout.fragment_via, container, false);
        Bundle bundle = getArguments();
        Via via=bundle.getSerializable("via", Via.class);
        binding.btnAddSesion.setVisibility(View.GONE);
        binding.btnFavorito.setVisibility(View.GONE);
        vm.getFotosVia(via.getId());
        vm.getCalificacionVia(via.getId());
        vm.setEstilo(via.getIdEstilo());
        binding.tvViaNombre.setText(via.getNombre());
        binding.tvTituloCardZona.setText(via.getZona().getNombre());
        binding.tvGradoVia.setText(via.getGrado().gradoN);
        binding.tvMetrosVia.setText(via.getAltura() + "");
        binding.tvChapasVia.setText(via.getChapas() + "");

        vm.getmDrawableEstilo().observe(getViewLifecycleOwner(), drawable -> binding.imgEstiloVia.setImageDrawable(drawable));
        vm.getmSlideModel().observe(getViewLifecycleOwner(), slideModels -> {
            binding.imgSliderSectorVia.setImageList(slideModels, ScaleTypes.CENTER_CROP);
        });
        vm.getmCalificacion().observe(getViewLifecycleOwner(),calificacion -> binding.ratingBarSector.setRating(calificacion.floatValue()));
        binding.linearCalificaciones.setOnClickListener(v -> {
            ReseniasFragment reseniasFragment = new ReseniasFragment();
            reseniasFragment.setArguments(bundle);
            reseniasFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
            reseniasFragment.show(getChildFragmentManager(), "DialogPersonalizado");
        });
        vm.getmImagen().observe(getViewLifecycleOwner(), image -> {
            binding.btnFavorito.setImageDrawable(image);
        });
        binding.btnBackVia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("salida", "ENTRO");
                Navigation.findNavController(v).navigate(R.id.action_viaInfoFragment_to_perfilFragment, bundle);
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigate(R.id.action_viaInfoFragment_to_perfilFragment, bundle);
            }
        });




        return binding.getRoot();
    }


}