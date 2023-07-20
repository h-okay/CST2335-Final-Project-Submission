package algonquin.cst2335.cst2335_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import algonquin.cst2335.cst2335_finalproject.FlightAdapter.OnItemClickListener;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flight_list, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.bind(flight);
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

        public void bind(Flight flight) {


                departureAirportTextView.setText("Departure:" +flight.getDepartureAirport());
                flightNumberTextView.setText("Flight Number:" + flight.getFlightNumber());
                destinationTextView.setText("Destination:" + flight.getDestination());
                terminalTextView.setText("Terminal:" + flight.getTerminal());
                gateTextView.setText("Gate:" + flight.getGate());
                delayTextView.setText("Delay:" + flight.getDelay());


        }
    }
}
