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

/**
 * The ConversionAdapter class is a custom RecyclerView adapter for displaying a list of currency conversions.
 *
 * This adapter is used to bind ConversionDAO objects to the RecyclerView in the ConversionMain activity.
 * It handles creating the ViewHolder and binding data to the view elements for each conversion item in the list.
 *
 * @version 1.0
 * @since 2023-07-26
 * @author Kang Dowon
 *
 */

public class ConversionAdapter extends RecyclerView.Adapter<ConversionAdapter.ConversionViewHolder> {

    private List<ConversionDAO> conversions;
    private OnLongClickListener longClickListener;
    private OnItemClickListener itemClickListener;
    private FragmentManager fragmentManager;

    /**
     * Interface to handle long click events on the conversion items.
     */
    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    /**
     * Interface to handle regular click events on the conversion items.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * Sets the long click listener for the adapter.
     *
     * @param listener The OnLongClickListener to set.
     */
    public void setOnLongClickListener(OnLongClickListener listener) {
        this.longClickListener = listener;
    }

    /**
     * Sets the item click listener for the adapter.
     *
     * @param listener The OnItemClickListener to set.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * Creates a new instance of ConversionAdapter with the specified list of conversions.
     *
     * @param conversions The list of ConversionDAO objects representing currency conversions.
     */
    public ConversionAdapter(List<ConversionDAO> conversions) {
        this.conversions = conversions;
        this.fragmentManager = fragmentManager;
    }

    /**
     * Called when a new ViewHolder is created. Inflates the view layout for each conversion item.
     *
     * @param parent The parent ViewGroup for the view.
     * @param viewType The type of view to be inflated.
     * @return A new instance of ConversionViewHolder.
     */
    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversion_adapter, parent, false);
        return new ConversionViewHolder(itemView);
    }

    /**
     * Called when data needs to be bound to the ViewHolder. Binds data to the view elements for each conversion item.
     *
     * @param holder The ConversionViewHolder to bind data to.
     * @param position The position of the item in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        ConversionDAO conversion = conversions.get(position);
        holder.bind(conversion);
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The total number of items in the list.
     */
    @Override
    public int getItemCount() {
        return conversions.size();
    }

    /**
     * Implements getItemId to enable proper item removal from the RecyclerView.
     *
     * @param position The position of the item in the RecyclerView.
     * @return The unique identifier for the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return conversions.get(position).hashCode();
    }

    /**
     * Deletes an item from the RecyclerView at the specified position.
     *
     * @param position The position of the item to be deleted.
     */
    public void deleteItem(int position) {
        conversions.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * The ViewHolder class to hold and manage the view elements for each conversion item.
     */
    class ConversionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewConversion;
        ImageButton buttonDelete;

        /**
         * Constructor for the ConversionViewHolder.
         *
         * @param itemView The View representing the layout for each conversion item.
         */
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

        /**
         * Binds data to the view elements for the conversion item.
         *
         * @param conversion The ConversionDAO object representing the currency conversion.
         */
        public void bind(ConversionDAO conversion) {
            String text = String.format("%s %s ▶ %s %s", conversion.getAmount(), conversion.getSourceCurrency(), conversion.getConvertedAmount(), conversion.getTargetCurrency());
            textViewConversion.setText(text);
        }
    }
}
