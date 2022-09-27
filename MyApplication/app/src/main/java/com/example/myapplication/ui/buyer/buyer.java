package com.example.myapplication.ui.buyer;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.BuyerFragmentBinding;

public class buyer extends Fragment {

    private BuyerViewModel buyerViewModel;
    private BuyerFragmentBinding binding;
//    public static buyer newInstance() {
//        return new buyer();
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        buyerViewModel =
                new ViewModelProvider(this).get(BuyerViewModel.class);

        binding = BuyerFragmentBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        final TextView textView = binding.textBuyer;
        buyerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}