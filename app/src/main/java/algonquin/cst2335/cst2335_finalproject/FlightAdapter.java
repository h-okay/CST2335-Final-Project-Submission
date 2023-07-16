package algonquin.cst2335.cst2335_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private List<Flight> flightList;

    public FlightAdapter(List<Flight> flightList) {
        this.flightList = flightList;
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

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        private TextView flightNumberTextView;
        private TextView departureTimeTextView;
        private TextView destinationTextView;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightNumberTextView = itemView.findViewById(R.id.flightNumber);
            departureTimeTextView = itemView.findViewById(R.id.departureTime);
            destinationTextView = itemView.findViewById(R.id.destination);
        }

        public void bind(Flight flight) {
            flightNumberTextView.setText(flight.getFlightNumber());
            departureTimeTextView.setText(flight.getDepartureTime());
            destinationTextView.setText(flight.getDestination());
        }
    }
}
