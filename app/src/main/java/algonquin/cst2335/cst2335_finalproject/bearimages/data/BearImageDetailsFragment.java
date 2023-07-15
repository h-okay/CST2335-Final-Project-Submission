package algonquin.cst2335.cst2335_finalproject.bearimages.data;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.cst2335_finalproject.databinding.BearEntryDetailsBinding;

@SuppressLint("DefaultLocale")
public class BearImageDetailsFragment extends Fragment {

    BearImage selected;


    public BearImageDetailsFragment(BearImage bi) {
        this.selected = bi;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        BearEntryDetailsBinding binding = BearEntryDetailsBinding.inflate(inflater);
        String sizes = String.format("%dx%d", selected.height, selected.width);
        binding.BearDetailsDate.setText(selected.getCreatedDate());
        binding.BearDetailsSize.setText(sizes);
        return binding.getRoot();
    }

}
