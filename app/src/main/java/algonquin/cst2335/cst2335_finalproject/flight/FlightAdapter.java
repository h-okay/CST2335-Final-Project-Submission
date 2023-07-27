package algonquin.cst2335.cst2335_finalproject.flight;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.cst2335_finalproject.R;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private List<Flight> flightList;
    private OnItemClickListener listener; // for fragment

    public FlightAdapter(List<Flight> flightList) {
        this.flightList = flightList;
    }

    public interface OnItemClickListener {
        void onItemClick(Flight flight);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FlightAdapter(List<Flight> flightList, OnItemClickListener listener) {
        this.flightList = flightList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_list, parent, false);
        return new FlightViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public class FlightViewHolder extends RecyclerView.ViewHolder {

        private TextView departureAirportTextView;
        private TextView terminalTextView;
        private TextView flightNumberTextView;
        private TextView destinationTextView;
        private TextView gateTextView;
        private TextView delayTextView;


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
