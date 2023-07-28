package algonquin.cst2335.cst2335_finalproject.flight;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
/**
 * ViewModel class for managing flight-related data in the Flight List screen.
 * This class extends the Android ViewModel class and provides MutableLiveData objects to hold flight data.
 */
public class FLightListViewModel extends ViewModel {
    /**
     * MutableLiveData object that holds an ArrayList of Flight objects representing the list of flights.
     * Observers can be notified of changes in the flight list using this LiveData.
     */
    public MutableLiveData<ArrayList<Flight>> lists = new MutableLiveData<>();
    /**
     * MutableLiveData object that holds a single Flight object representing the currently selected flight.
     * Observers can be notified of changes in the selected flight using this LiveData.
     */
    public MutableLiveData<Flight> selectedList = new MutableLiveData<>();

}
