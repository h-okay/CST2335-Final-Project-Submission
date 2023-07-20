package algonquin.cst2335.cst2335_finalproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class FLightListViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Flight>> lists = new MutableLiveData<>();
    public MutableLiveData<Flight> selectedList = new MutableLiveData<>();

    public LiveData<ArrayList<Flight>> getFlightListLiveData() {
        if (lists == null) {
            lists = new MutableLiveData<>();
        }
        return lists;
    }
    public void flightDeleted() {
        // Trigger an update to the LiveData so that observers (FlightMainActivity) can be notified
        ArrayList<Flight> currentList = lists.getValue();
        lists.postValue(currentList);
    }



}
