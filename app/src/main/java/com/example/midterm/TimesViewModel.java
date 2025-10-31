package com.example.midterm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class TimesViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<String>> rows =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<ArrayList<Integer>> history =
            new MutableLiveData<>(new ArrayList<>());

    public LiveData<ArrayList<String>> getRows() { return rows; }
    public LiveData<ArrayList<Integer>> getHistory() { return history; }

    public void generateTable(int n) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(n + " × " + i + " = " + (n * i));
        }
        rows.setValue(list);

        ArrayList<Integer> h = history.getValue();
        if (h == null) h = new ArrayList<>();
        if (!h.contains(n)) h.add(n);
        history.setValue(h);
    }

    public String deleteRowAt(int index) {
        ArrayList<String> list = rows.getValue();
        if (list == null || index < 0 || index >= list.size()) return null;
        String removed = list.remove(index);
        rows.setValue(list);
        return removed;
    }

    public void clearAll() {
        rows.setValue(new ArrayList<String>());
    }
}
