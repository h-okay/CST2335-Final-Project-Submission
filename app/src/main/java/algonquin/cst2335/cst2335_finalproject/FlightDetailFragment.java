package algonquin.cst2335.cst2335_finalproject;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

import algonquin.cst2335.cst2335_finalproject.databinding.FlightDetailInfoBinding;

public class FlightDetailFragment extends Fragment {

    private FlightDetailInfoBinding binding;

    Flight selected;
    private FlightDAO flightDAO;

    public FlightDetailFragment(Flight flight, FlightDAO flightDAO) {
        selected = flight;
        this.flightDAO = flightDAO;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FlightDetailInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.saveButton.setOnClickListener(v -> saveFlightDetailsToDatabase());

        binding.deleteButton.setOnClickListener(v -> deleteFlightFromDatabase());

        requireActivity().runOnUiThread(() -> {

            binding.departureText.setText("Departure: " + selected.departureAirport);
            binding.flightNumberText.setText("Flight Number: " + selected.flightNumber);
            binding.destinationData.setText("Destination: " + selected.destination);
            binding.terminalData.setText("Terminal: " + selected.terminal);
            binding.gate.setText("Gate: " + selected.gate);
            binding.delay.setText("Delay time: " + selected.delay);

        });

        return view;

    }

    private void saveFlightDetailsToDatabase() {

        String departureAirport = binding.departureText.getText().toString();
        String flightNumber = binding.flightNumberText.getText().toString();
        String delay = binding.delay.getText().toString();
        String terminal = binding.terminalData.getText().toString();
        String gate = binding.gate.getText().toString();
        String destination = binding.destinationData.getText().toString();

        Flight flight = new Flight(departureAirport, flightNumber,  delay,  terminal, gate, destination);
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(new Runnable() {
            @Override
            public void run() {

                flight.id = flightDAO.insertFlight(flight);
            }
        });

        requireActivity().onBackPressed();
        // Show a toast message indicating the flight details are saved
      //   Toast.makeText(requireContext(), "Flight details saved to the database", Toast.LENGTH_SHORT).show();


}


    private void deleteFlightFromDatabase() {
        // Implement the method to delete the flight from the database using the flightDAO
        // Note: Make sure to perform this operation on a background thread
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            flightDAO.deleteFlight(selected);
        });
        requireActivity().onBackPressed();

    } // Navigate back to the main screen or perform any other desired action


}