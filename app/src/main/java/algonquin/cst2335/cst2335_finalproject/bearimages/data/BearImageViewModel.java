/**
 * BearImageViewModel.java
 * <p>
 * The BearImageViewModel class represents the ViewModel for the BearImages application.
 * It holds MutableLiveData objects to manage the list of bear images and the selected bear image.
 *
 * @author Hakan Okay
 * @version 1.0
 * @since JDK 20
 */

package algonquin.cst2335.cst2335_finalproject.bearimages.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * The BearImageViewModel class represents the ViewModel for the BearImages application.
 * It holds MutableLiveData objects to manage the list of bear images and the selected bear image.
 */
public class BearImageViewModel extends ViewModel {

    /**
     * The MutableLiveData object holding the list of bear images.
     */
    public MutableLiveData<ArrayList<BearImage>> bearImages = new MutableLiveData<>();

    /**
     * The MutableLiveData object holding the selected bear image.
     */
    public MutableLiveData<BearImage> selectedImage = new MutableLiveData<>();
}
