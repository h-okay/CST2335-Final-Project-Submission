package algonquin.cst2335.cst2335_finalproject.bearimages.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class BearImageViewModel extends ViewModel {
    public MutableLiveData<ArrayList<BearImage>> bearImages = new MutableLiveData<>();
    public MutableLiveData<BearImage> selectedImage = new MutableLiveData<>();
}




