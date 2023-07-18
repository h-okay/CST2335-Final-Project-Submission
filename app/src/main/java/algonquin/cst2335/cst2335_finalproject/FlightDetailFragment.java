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
    public FlightDetailFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FlightDetailInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.destinationData.setText(selected.destination);
        binding.terminalData.setText(selected.flightNumber);
        binding.gate.setText(selected.flightName);
        binding.delay.setText(selected.departureAirport);
       // binding.timeText.setText(selected.timeSent);
      //  binding.database.setText("Id = " + selected.id);

        return view;
    }

}