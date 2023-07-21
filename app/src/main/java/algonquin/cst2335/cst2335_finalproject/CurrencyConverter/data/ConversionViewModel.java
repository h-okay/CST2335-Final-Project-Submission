package algonquin.cst2335.cst2335_finalproject.CurrencyConverter.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.cst2335_finalproject.CurrencyConverter.ConversionDAO;

public class ConversionViewModel extends ViewModel {

    public ArrayList<ConversionDAO> dao = new ArrayList<>();

    public MutableLiveData<ConversionDAO> selected = new MutableLiveData<>();
}
