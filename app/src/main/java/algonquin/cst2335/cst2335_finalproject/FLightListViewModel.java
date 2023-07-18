package algonquin.cst2335.cst2335_finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class FLightListViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Flight>> lists = new MutableLiveData<>();
    public MutableLiveData<Flight> selectedList = new MutableLiveData<>();
}
