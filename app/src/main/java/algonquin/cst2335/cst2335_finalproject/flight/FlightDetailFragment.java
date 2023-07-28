package algonquin.cst2335.cst2335_finalproject.flight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.databinding.FlightDetailInfoBinding;

/**
 * A Fragment class representing the Flight Detail screen, where users can view and edit the details of a selected flight.
 * This Fragment displays the flight details and allows users to save the edited details to the database.
 */
public class FlightDetailFragment extends Fragment {

    /**
     * The binding object that holds references to the UI elements in the layout.
     */
    private FlightDetailInfoBinding binding;

    /**
     * The Flight object representing the selected flight to display and edit.
     */
    Flight selected;
    /**
     * The Data Access Object (DAO) used for interacting with the Flight table in the database.
     */
    private FlightDAO flightDAO;

    /**
     * Constructs a new instance of FlightDetailFragment with the provided Flight and FlightDAO.
     * @param flight    The Flight object representing the selected flight to display and edit.
     * @param flightDAO The FlightDAO instance used for interacting with the Flight table in the database.
     */
    public FlightDetailFragment(Flight flight, FlightDAO flightDAO) {
        selected = flight;
        this.flightDAO = flightDAO;
    }

    /**
     * Called when the Fragment's UI is created. Inflates the layout and initializes UI elements.
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state of the fragment (if available).
     * @return The inflated View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FlightDetailInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.saveButton.setOnClickListener(v -> saveFlightDetailsToDatabase());

        requireActivity().runOnUiThread(() -> {

            binding.departureText.setText(selected.departureAirport);
            binding.flightNumberText.setText(selected.flightNumber);
            binding.destinationData.setText(selected.destination);
            binding.terminalData.setText(selected.terminal);
            binding.gate.setText(selected.gate);
            binding.delay.setText(selected.delay);

        });

        return view;

    }

    /**
     * Saves the edited flight details to the database.
     * Retrieves the edited details from the UI, creates a new Flight object, and inserts it into the database.
     * This operation is performed on a background thread using an Executor to avoid blocking the main thread.
     * After saving the details, the method calls onBackPressed() to navigate back to the main screen.
     */
    private void saveFlightDetailsToDatabase() {

        String departureAirport = binding.departureText.getText().toString();
        String flightNumber = binding.flightNumberText.getText().toString();
        String delay = binding.delay.getText().toString();
        String terminal = binding.terminalData.getText().toString();
        String gate = binding.gate.getText().toString();
        String destination = binding.destinationData.getText().toString();

        Flight flight = new Flight(departureAirport, flightNumber, delay, terminal, gate, destination);
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(new Runnable() {
            @Override
            public void run() {

                flight.id = flightDAO.insertFlight(flight);
            }
        });

        requireActivity().onBackPressed();
    }

    /**
     * Deletes the selected flight from the database.
     * This operation is performed on a background thread using an Executor to avoid blocking the main thread.
     * After deleting the flight, the method calls onBackPressed() to navigate back to the main screen.
     */
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