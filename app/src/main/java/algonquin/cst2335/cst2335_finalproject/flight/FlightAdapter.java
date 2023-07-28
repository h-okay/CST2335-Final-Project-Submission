package algonquin.cst2335.cst2335_finalproject.flight;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.cst2335_finalproject.R;

/**
 * RecyclerView Adapter for displaying a list of Flight items in a RecyclerView.
 * This adapter binds Flight data to the corresponding UI elements in the flight_list layout.
 */
public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    /**
     * The list of Flight objects to be displayed in the RecyclerView.
     */
    private List<Flight> flightList;
    /**
     * Interface for handling item click events in the RecyclerView.
     */
    private OnItemClickListener listener; // for fragment

    /**
     * Constructor for the FlightAdapter.
     * @param flightList The list of Flight objects to be displayed in the RecyclerView.
     */
    public FlightAdapter(List<Flight> flightList) {
        this.flightList = flightList;
    }

    /**
     * Interface for handling item click events in the RecyclerView.
     */
    public interface OnItemClickListener {
        /**
         * Callback method to handle item click events.
         * @param flight The Flight object representing the clicked item.
         */
        void onItemClick(Flight flight);
    }
    /**
     * Sets the click listener for the RecyclerView items.
     * @param listener The OnItemClickListener to be set as the click listener.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Constructor for the FlightAdapter with an initial list of flights and a click listener.
     * @param flightList The list of Flight objects to be displayed in the RecyclerView.
     * @param listener   The OnItemClickListener to handle item click events.
     */
    public FlightAdapter(List<Flight> flightList, OnItemClickListener listener) {
        this.flightList = flightList;
        this.listener = listener;
    }

    /**
     * Called when a new ViewHolder is created to represent an item in the RecyclerView.
     * Inflates the layout for a single flight list item and creates a new FlightViewHolder.
     * @param parent   The parent ViewGroup that the new View will be attached to.
     * @param viewType The view type of the new View.
     * @return A new FlightViewHolder that holds the inflated flight_list item view.
     */
    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_list, parent, false);
        return new FlightViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at a specific position.
     * Binds the Flight data to the corresponding UI elements in the ViewHolder.
     * @param holder   The FlightViewHolder to bind the data to.
     * @param position The position of the item in the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);

        String departureText = holder.itemView.getContext().getString(R.string.departure, flight.getDepartureAirport());
        String flightNumberText = holder.itemView.getContext().getString(R.string.flight_number, flight.getFlightNumber());
        String destinationText = holder.itemView.getContext().getString(R.string.destination, flight.getDestination());
        String terminalText = holder.itemView.getContext().getString(R.string.terminal, flight.getTerminal());
        String gateText = holder.itemView.getContext().getString(R.string.gate, flight.getGate());
        String delayText = holder.itemView.getContext().getString(R.string.delay, flight.getDelay());

        holder.bind(flight, departureText, flightNumberText, destinationText, terminalText, gateText, delayText);
    }

    /**
     * Returns the total number of items in the data set.
     * @return The total number of Flight objects in the flightList.
     */
    @Override
    public int getItemCount() {
        return flightList.size();
    }

    /**
     * ViewHolder class representing a single flight list item in the RecyclerView.
     */
    public class FlightViewHolder extends RecyclerView.ViewHolder {

        private TextView departureAirportTextView;
        private TextView terminalTextView;
        private TextView flightNumberTextView;
        private TextView destinationTextView;
        private TextView gateTextView;
        private TextView delayTextView;

        /**
         * Constructor for the FlightViewHolder.
         * Initializes the UI elements and sets a click listener for the item view.
         * @param itemView The item view representing a single flight list item.
         */
        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightNumberTextView = itemView.findViewById(R.id.flightNumber);
            terminalTextView = itemView.findViewById(R.id.terminal);
            destinationTextView = itemView.findViewById(R.id.destination);
            departureAirportTextView = itemView.findViewById(R.id.departureAirport);
            gateTextView = itemView.findViewById(R.id.gate);
            delayTextView = itemView.findViewById(R.id.delay);

            // Set click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Flight clickedFlight = flightList.get(position);
                        listener.onItemClick(clickedFlight);
                    }
                }
            });
        }

        /**
         * Binds Flight data to the ViewHolder's UI elements.
         * @param flight         The Flight object representing the flight list item.
         * @param departureText  The formatted text for the departure airport.
         * @param flightNumberText The formatted text for the flight number.
         * @param destinationText The formatted text for the destination.
         * @param terminalText   The formatted text for the terminal.
         * @param gateText       The formatted text for the gate.
         * @param delayText      The formatted text for the delay.
         */
        public void bind(Flight flight, String departureText, String flightNumberText, String destinationText, String terminalText, String gateText, String delayText) {

            departureAirportTextView.setText(departureText);
            flightNumberTextView.setText(flightNumberText);
            destinationTextView.setText(destinationText);
            terminalTextView.setText(terminalText);
            gateTextView.setText(gateText);
            delayTextView.setText(delayText);

        }
    }
}
