package com.heissen.cragexplorer.ui.loogbook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.heissen.cragexplorer.R;
import com.heissen.cragexplorer.databinding.FragmentHomeBinding;
import com.heissen.cragexplorer.databinding.FragmentLogbookBinding;
import com.heissen.cragexplorer.ui.home.HomeViewModel;

public class LogbookFragment extends Fragment {

    private FragmentLogbookBinding binding;
    private LogbookViewModel vm;
    private FragmentManager fragmentManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        LogbookViewModel dashboardViewModel =
                new ViewModelProvider(this).get(LogbookViewModel.class);

    binding = FragmentLogbookBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
        fragmentManager = requireActivity().getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = binding.navViewLogbook;
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_container_loogbook);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}