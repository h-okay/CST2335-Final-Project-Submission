package algonquin.cst2335.cst2335_finalproject.CurrencyConverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import algonquin.cst2335.cst2335_finalproject.R;

/**
 * @author Kang Dowon
 *
 * Adapter class for the ListView in ConversionMain activity.
 * Binds ConversionDAO data to the ListView items.
 */
public class ConversionAdapter extends ArrayAdapter<ConversionDAO> {

    private Context context;
    private List<ConversionDAO> conversions;

    /**
     * Constructor for ConversionAdapter.
     *
     * @param context     The context.
     * @param conversions List of ConversionDAO objects.
     */
    public ConversionAdapter(Context context, List<ConversionDAO> conversions) {
        super(context, 0, conversions);
        this.context = context;
        this.conversions = conversions;
    }

    /**
     * Creates a view for the ListView item.
     * @return The view for the ListView item.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.conversion_adapter, parent, false);
        }

        ConversionDAO conversion = conversions.get(position);

        TextView sourceCurrencyTextView = convertView.findViewById(R.id.text_view_source_currency);
        TextView targetCurrencyTextView = convertView.findViewById(R.id.text_view_target_currency);
        TextView amountTextView = convertView.findViewById(R.id.text_view_amount);
        TextView convertedAmountTextView = convertView.findViewById(R.id.text_view_converted_amount);

        sourceCurrencyTextView.setText(conversion.getSourceCurrency() + " ");
        targetCurrencyTextView.setText("  " + conversion.getTargetCurrency() + " ");
        amountTextView.setText(conversion.getAmount() + "  ");
        convertedAmountTextView.setText(conversion.getConvertedAmount());

        return convertView;
    }
}
