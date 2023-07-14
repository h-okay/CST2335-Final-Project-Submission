package algonquin.cst2335.cst2335_finalproject.bearimages.data;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import algonquin.cst2335.cst2335_finalproject.databinding.BearEntryDetailsBinding;

public class BearImageDetailsFragment extends Fragment {

    BearImage selected;

    public BearImageDetailsFragment(BearImage bi) {
        this.selected = bi;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        BearEntryDetailsBinding binding = BearEntryDetailsBinding.inflate(inflater);
        String sizes = String.format(Locale.CANADA, "%dx%d", selected.height, selected.weight);
        binding.BearDetailsName.setText(selected.name);
        binding.BearDetailsDate.setText(selected.created_date.toString());
        binding.BearDetailsSize.setText(sizes);
        return binding.getRoot();
    }

}
