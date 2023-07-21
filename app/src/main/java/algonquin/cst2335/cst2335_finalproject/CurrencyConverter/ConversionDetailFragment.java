package algonquin.cst2335.cst2335_finalproject.CurrencyConverter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.cst2335_finalproject.databinding.DetailsLayoutBinding;

public class ConversionDetailFragment extends Fragment {

    private static final String ARG_CONVERSION = "ARG_CONVERSION";
    private ConversionDAO con;

    public ConversionDetailFragment() {
        // Required empty public constructor
    }

    public static ConversionDetailFragment newInstance(ConversionDAO conversionDAO) {
        ConversionDetailFragment fragment = new ConversionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONVERSION, conversionDAO);
        fragment.setArguments(args);
        return fragment;
    }

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
