package com.example.midterm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class TimesFragment extends Fragment {

    private TimesViewModel vm;
    private EditText et;
    private Button btnGenerate, btnHistory;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        requireActivity().getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finish(); // Exit app from first screen
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_times, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(requireActivity()).get(TimesViewModel.class);

        et = view.findViewById(R.id.etNumber);
        btnGenerate = view.findViewById(R.id.btnGenerate);
        btnHistory = view.findViewById(R.id.btnHistory);
        listView = view.findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        vm.getRows().observe(getViewLifecycleOwner(), rows -> {
            adapter.clear();
            adapter.addAll(rows);
            adapter.notifyDataSetChanged();
        });

        btnGenerate.setOnClickListener(v -> {
            String s = et.getText().toString().trim();
            if (s.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a number", Toast.LENGTH_SHORT).show();
            } else {
                vm.generateTable(Integer.parseInt(s));
            }
        });

        btnHistory.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new HistoryFragment())
                        .addToBackStack(null)
                        .commit()
        );

        listView.setOnItemClickListener((parent, v, position, id) -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.dialog_title_delete))
                    .setMessage(getString(R.string.dialog_msg_delete))
                    .setPositiveButton(getString(R.string.yes), (d, w) -> {
                        String removed = vm.deleteRowAt(position);
                        if (removed != null) {
                            Toast.makeText(requireContext(),
                                    "Deleted: " + removed, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_times, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_clear_all) {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.menu_clear_all))
                    .setMessage("Remove all rows from the current table?")
                    .setPositiveButton(getString(R.string.yes), (d, w) -> {
                        vm.clearAll();
                        Toast.makeText(requireContext(), "All rows cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
