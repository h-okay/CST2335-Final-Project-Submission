package algonquin.cst2335.cst2335_finalproject.CurrencyConverter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.cst2335_finalproject.databinding.DetailsLayoutBinding;

/**
 * A fragment that displays the details of a currency conversion.
 *
 * This fragment is used to show the details of a selected currency conversion from the list of
 * conversions in the RecyclerView in ConversionMain activity. It displays the source currency,
 * the original value, the target currency, and the converted value.
 *
 * @version 1.0
 * @since 2023-07-26
 * @author Kang Dowon
 */



public class ConversionDetailFragment extends Fragment {

    // The argument key for passing ConversionDAO object to the fragment
    private static final String ARG_CONVERSION = "ARG_CONVERSION";
    private ConversionDAO con;

    /**
     * Empty constructor for the ConversionDetailFragment.
     * This constructor is required for creating the fragment.
     */
    public ConversionDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of ConversionDetailFragment with the provided ConversionDAO object.
     *
     * @param conversionDAO The ConversionDAO object representing the selected conversion.
     * @return A new instance of ConversionDetailFragment.
     */
    public static ConversionDetailFragment newInstance(ConversionDAO conversionDAO) {
        ConversionDetailFragment fragment = new ConversionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONVERSION, conversionDAO);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment's view is created. Initializes the UI components and displays
     * the details of the selected currency conversion.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views
     *                           in the fragment.
     * @param parent             The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing the saved state of the fragment, if available.
     * @return The root view of the fragment's layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);

        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater, parent, false);

        Bundle args = getArguments();
        if (args != null) {
            con = args.getParcelable(ARG_CONVERSION);
        }

        if (con != null) {
            binding.sourceDetail.setText(con.getSourceCurrency());
            binding.originalValue.setText(con.getAmount());
            binding.targetDetail.setText(con.getTargetCurrency());
            binding.convertedValue.setText(con.getConvertedAmount());
    //        binding.idText.setText(String.valueOf(con.getId()));
        }

        return binding.getRoot();
    }
}
