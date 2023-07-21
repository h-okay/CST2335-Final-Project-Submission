package algonquin.cst2335.cst2335_finalproject.CurrencyConverter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.cst2335_finalproject.R;

public class ConversionAdapter extends RecyclerView.Adapter<ConversionAdapter.ConversionViewHolder> {

    private List<ConversionDAO> conversions;
    private OnLongClickListener longClickListener;
    private OnItemClickListener itemClickListener;
    private FragmentManager fragmentManager;

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public ConversionAdapter(List<ConversionDAO> conversions) {
        this.conversions = conversions;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversion_adapter, parent, false);
        return new ConversionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        ConversionDAO conversion = conversions.get(position);
        holder.bind(conversion);
    }

    @Override
    public int getItemCount() {
        return conversions.size();
    }

    // Implement getItemId to enable proper item removal from the RecyclerView
    @Override
    public long getItemId(int position) {
        return conversions.get(position).hashCode();
    }

    // Method to delete an item from the RecyclerView
    public void deleteItem(int position) {
        conversions.remove(position);
        notifyItemRemoved(position);
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewConversion;
        ImageButton buttonDelete;

        public ConversionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewConversion = itemView.findViewById(R.id.text_view_amount);

            // Set a click listener for the item view to show the details fragment
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                        itemClickListener.onItemClick(position);
                    }
                }
            });

            // Set a long click listener for the item view to show delete confirmation
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                        // Show AlertDialog for confirmation
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        builder.setTitle("Delete Conversion")
                                .setMessage("Are you sure you want to delete this conversion?")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    longClickListener.onItemLongClicked(position);
                                })
                                .setNegativeButton("Cancel", null)
                                .create()
                                .show();

                        return true;
                    }
                    return false;
                }
            });
        }

        public void bind(ConversionDAO conversion) {
            String text = String.format("%s %s ▶ %s %s", conversion.getAmount(), conversion.getSourceCurrency(), conversion.getConvertedAmount(), conversion.getTargetCurrency());
            textViewConversion.setText(text);
        }
    }
}
