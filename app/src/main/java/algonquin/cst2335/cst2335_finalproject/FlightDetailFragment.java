package algonquin.cst2335.cst2335_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.cst2335_finalproject.databinding.FlightDetailInfoBinding;

public class FlightDetailFragment extends Fragment {

    private FlightDetailInfoBinding binding;

    Flight selected;

    public FlightDetailFragment(Flight flight) { selected = flight; }
    public FlightDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FlightDetailInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        requireActivity().runOnUiThread( (  )  -> {
            binding.destinationData.setText("Destination: " + selected.destination);
            binding.terminalData.setText("Terminal: "+ selected.terminal);
            binding.gate.setText("Gate: "+ selected.gate);
            binding.delay.setText("Delay time: " + selected.delay);

        });

    return view;

}}
