package com.example.midterm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private TimesViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(requireActivity()).get(TimesViewModel.class);

        ListView lv = view.findViewById(R.id.lvHistory);
        Button back = view.findViewById(R.id.btnBack);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        lv.setAdapter(adapter);

        vm.getHistory().observe(getViewLifecycleOwner(), list -> {
            adapter.clear();
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
        });

        back.setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed()
        );
    }
}
