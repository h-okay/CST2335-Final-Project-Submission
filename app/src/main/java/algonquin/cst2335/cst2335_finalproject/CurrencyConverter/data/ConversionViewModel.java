package algonquin.cst2335.cst2335_finalproject.CurrencyConverter.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.cst2335_finalproject.CurrencyConverter.ConversionDAO;

/**
 * The ConversionViewModel class is a ViewModel that holds and manages data related to currency conversions.
 *
 * This ViewModel is used to store a list of ConversionDAO objects representing currency conversions.
 * It also provides a MutableLiveData object, 'selected', which can be used to observe changes to a
 * specific ConversionDAO object in the list.
 * @version 1.0
 * @since 2023-07-26
 * @author Kang Dowon
 */


public class ConversionViewModel extends ViewModel {

    // The ArrayList to hold ConversionDAO objects representing currency conversions
    public ArrayList<ConversionDAO> dao = new ArrayList<>();

    // MutableLiveData to observe changes to a specific ConversionDAO object
    public MutableLiveData<ConversionDAO> selected = new MutableLiveData<>();
}
