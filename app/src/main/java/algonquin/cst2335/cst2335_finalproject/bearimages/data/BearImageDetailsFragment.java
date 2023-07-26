/**
 * BearImageDetailsFragment.java
 * <p>
 * The BearImageDetailsFragment class represents a fragment that displays details of a selected bear image.
 * It shows the image, height, width, and creation date of the selected bear image.
 *
 * @author Hakan Okay
 * @version 1.0
 * @since JDK 20
 */

package algonquin.cst2335.cst2335_finalproject.bearimages.data;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.cst2335_finalproject.databinding.BearEntryDetailsBinding;

/**
 * The BearImageDetailsFragment class represents a fragment that displays details of a selected bear image.
 * It shows the image, height, width, and creation date of the selected bear image.
 */
@SuppressLint("DefaultLocale")
public class BearImageDetailsFragment extends Fragment {

    /**
     * The selected BearImage object to display its details.
     */
    BearImage selected;

    /**
     * Constructor for BearImageDetailsFragment.
     *
     * @param bi The BearImage object representing the selected bear image to display details for.
     */
    public BearImageDetailsFragment(BearImage bi) {
        this.selected = bi;
    }

    /**
     * Creates the view for the BearImageDetailsFragment.
     * It inflates the layout and sets the image, height, width, and creation date of the selected bear image.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The last saved instance state of the Fragment, or null if this is a new instance.
     * @return The root View for the fragment UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        BearEntryDetailsBinding binding = BearEntryDetailsBinding.inflate(inflater);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(selected.getImage(), 0, selected.getImage().length);
        String sizes = String.format("%dx%d", selected.height, selected.width);
        binding.BearDetailsImage.setImageBitmap(imageBitmap);
        binding.BearDetailsDate.setText(selected.getCreatedDate());
        binding.BearDetailsSize.setText(sizes);
        return binding.getRoot();
    }
}
