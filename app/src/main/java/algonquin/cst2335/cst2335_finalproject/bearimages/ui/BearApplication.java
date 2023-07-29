/**
 * BearApplication.java
 * <p>
 * The application allows users to generate random bear images with specified dimensions (height and width) and save them
 * to a local database. Users can view the list of saved bear images and tap on an image to see its details, including the
 * height, width, and creation date. Additionally, users can delete a saved bear image by long-pressing on it and confirming
 * the deletion in an alert dialog.
 * <p>
 * Developed as part of the final project for CST2335 - Mobile Graphical Interface Programming
 * at Algonquin College.
 *
 * @author Hakan Okay
 * @version 1.0
 * @since JDK 20
 */

package algonquin.cst2335.cst2335_finalproject.bearimages.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImage;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImageDao;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImageDatabase;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImageDetailsFragment;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImageViewModel;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivityBearImageListBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.BearEntryBinding;

/**
 * The BearApplication class represents the main activity for the Bear Images application.
 * This activity allows users to generate bear images with custom height and width, view the list of generated bear images,
 * and view the details of a selected bear image.
 */
@SuppressLint("DefaultLocale")
public class BearApplication extends AppCompatActivity {

    /**
     * The base URL to generate bear images with custom height and width.
     */
    private static final String url = "https://placebear.com/%d/%d";

    /**
     * The current date in the format yyyy-MM-dd.
     */
    private static final String currentDate = java.time.LocalDate.now().toString();

    /**
     * The binding object for the activity layout.
     */
    private ActivityBearImageListBinding binding;

    /**
     * The ViewModel for managing the bear images data.
     */
    private BearImageViewModel bearModel;

    /**
     * The RecyclerView adapter for displaying the list of bear images.
     */
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    /**
     * The list of bear images fetched from the local database.
     */
    private ArrayList<BearImage> images;

    /**
     * The SharedPreferences object to store user preferences.
     */
    private SharedPreferences prefs;

    /**
     * The Room database instance for storing bear images locally.
     */
    private BearImageDatabase bearImageDatabase;

    /**
     * The DAO (Data Access Object) for performing database operations on bear images.
     */
    private BearImageDao bearDao;

    /**
     * The Volley RequestQueue for making image requests to the remote server.
     */
    private RequestQueue queue;

    /**
     * The FragmentManager for managing fragments within the activity.
     */
    private FragmentManager fragmentManager;

    /**
     * The AlertDialog.Builder for displaying alert dialogs in the activity.
     */
    private AlertDialog.Builder builder;


    /**
     * Called when the activity is created. Initializes the UI components, ViewModel, database, and other instances.
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Boot
        super.onCreate(savedInstanceState);
        binding = ActivityBearImageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        // Setting up instances
        adapter = getAdapter();
        bearImageDatabase = Room.databaseBuilder(getApplicationContext(), BearImageDatabase.class, "bear-image-database").build();
        bearDao = bearImageDatabase.bearImageDao();
        bearModel = new ViewModelProvider(this).get(BearImageViewModel.class);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(BearApplication.this));
        queue = Volley.newRequestQueue(this.getApplicationContext());
        fragmentManager = getSupportFragmentManager();
        builder = new AlertDialog.Builder(BearApplication.this);

        // SharedPreferences
        prefs = getSharedPreferences("BearAppData", Context.MODE_PRIVATE);
        int savedHeight = prefs.getInt("bearHeight", 0);
        int savedWidth = prefs.getInt("bearWidth", 0);
        binding.HeightNewTextArea.setText(String.valueOf(savedHeight));
        binding.WidthNewTextArea.setText(String.valueOf(savedWidth));

        // Filling items from DB on startup
        images = bearModel.bearImages.getValue();
        if (images == null) {
            loadDatabaseState();
        }

        // New Image Generation
        binding.addNewButton.setOnClickListener(click -> generateImage());
        bearModel.selectedImage.observe(this, this::observeFragment);
    }

    /**
     * Generates a new bear image with the specified height and width and adds it to the local database and the RecyclerView.
     */
    private void generateImage() {
        ImageRequest request;
        int height;
        int width;

        if (checkIfInputValid()) {
            height = Integer.parseInt(binding.HeightNewTextArea.getText().toString());
            width = Integer.parseInt(binding.WidthNewTextArea.getText().toString());

            // Save input to Shared Preferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("bearHeight", height);
            editor.putInt("bearWidth", width);
            editor.apply();

            request = generateImageRequest(height, width);
            queue.add(request);
        }
    }

    /**
     * Generates an ImageRequest object to fetch a bear image from the remote server with the given height and width.
     *
     * @param height The desired height of the bear image.
     * @param width  The desired width of the bear image.
     * @return The ImageRequest object to fetch the bear image from the remote server.
     */
    private ImageRequest generateImageRequest(int height, int width) {
        return new ImageRequest(
                String.format(BearApplication.url, height, width),
                getResponseListener(height, width),
                0,
                0,
                null,
                null,
                error -> generateToast("There was an error, please try again.").show()
        );
    }

    /**
     * Creates a Response.Listener to handle the successful response from the remote server.
     *
     * @param height The height of the generated bear image.
     * @param width  The width of the generated bear image.
     * @return The Response.Listener object to handle the successful response.
     */
    private Response.Listener<Bitmap> getResponseListener(int height, int width) {
        return new Response.Listener<>() {

            private byte[] getBitmapAsByteArray(Bitmap bitmap) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                return outputStream.toByteArray();
            }

            @Override
            public void onResponse(Bitmap response) {
                byte[] imageData = getBitmapAsByteArray(response);
                BearImage newBearImage = new BearImage(imageData, height, width, currentDate);
                runOnDifferentThread(() -> bearDao.insertImage(newBearImage));
                runOnUiThread(() -> {
                    images.add(newBearImage);
                    bearModel.bearImages.postValue(images);
                    adapter.notifyItemInserted(images.size() - 1);
                });
            }
        };
    }

    /**
     * Generates a Toast with the given message.
     *
     * @param message The message to display in the Toast.
     * @return The generated Toast.
     */
    private Toast generateToast(String message) {
        return Toast.makeText(BearApplication.this, message, Toast.LENGTH_SHORT);
    }

    /**
     * Runs the given job in a separate thread.
     *
     * @param job The Runnable job to run in a separate thread.
     */
    private void runOnDifferentThread(Runnable job) {
        new Thread(job).start();
    }

    /**
     * Checks if the input values for height and width are valid.
     *
     * @return True if the input is valid; false otherwise.
     */
    private boolean checkIfInputValid() {
        boolean valid = false;

        String heightText = binding.HeightNewTextArea.getText().toString().trim();
        String widthText = binding.WidthNewTextArea.getText().toString().trim();

        if (!heightText.isEmpty() && !widthText.isEmpty()) {
            int height = Integer.parseInt(heightText);
            int width = Integer.parseInt(widthText);

            if (height <= 500 && width <= 500 && height > 0 && width > 0) {
                valid = true;
            }
        }

        return valid;
    }

    /**
     * Creates and returns a new RecyclerView adapter to display the list of bear images.
     *
     * @return The RecyclerView adapter for displaying bear images.
     */
    private RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter() {
        return new RecyclerView.Adapter<>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                BearEntryBinding binding = BearEntryBinding.inflate(getLayoutInflater());
                return new BearImageRow(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                if (holder instanceof BearImageRow) {
                    BearImageRow bearImageRow = (BearImageRow) holder;

                    BearImage bearImage = images.get(position);
                    byte[] imageData = bearImage.getImage();
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    String sizes = bearImage.getHeight() + "x" + bearImage.getWidth();
                    String date = bearImage.getCreatedDate();

                    bearImageRow.image.setImageBitmap(imageBitmap);
                    bearImageRow.sizes.setText(sizes);
                    bearImageRow.date.setText(date);
                }
            }

            @Override
            public int getItemCount() {
                return images.size();
            }
        };
    }

    /**
     * Observes changes in the selected bear image and updates the UI with the selected image details.
     *
     * @param newImage The newly selected bear image.
     */
    private void observeFragment(BearImage newImage) {
        BearImageDetailsFragment imageFragment = new BearImageDetailsFragment(newImage);
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.replace(R.id.fragmentLocation, imageFragment);
        tx.addToBackStack("");
        tx.commit();
    }

    /**
     * Loads the state of the local database and updates the RecyclerView with the stored bear images.
     */
    private void loadDatabaseState() {
        bearModel.bearImages.setValue(images = new ArrayList<>());
        runOnDifferentThread(() -> {
            images.addAll(bearDao.getAllImages());
            bearModel.bearImages.postValue(images);
        });
        runOnUiThread(() -> adapter.notifyItemRangeInserted(0, images.size()));
    }

    /**
     * Inflates the menu layout for the activity toolbar.
     *
     * @param menu The menu object to inflate.
     * @return true if the menu is inflated successfully; false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bear_app_menu, menu);
        return true;
    }

    /**
     * Handles the selection of menu items in the activity toolbar.
     *
     * @param item The selected menu item.
     * @return true if the item is handled successfully; false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        String translatedText = adaptLanguage();
        String local = Locale.getDefault().getCountry();
        Log.d("BearApplication", local);
        String titleText = "How to use";
        if (local.equals("TR")) {
            titleText = "Nasıl kullanılır";
        }
        if (itemId == R.id.bearHelp) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BearApplication.this);
            builder.setTitle(titleText).setMessage(translatedText).setPositiveButton("OK", (dialog, which) -> {
            }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adapt the info panel language to current set locale language.
     *
     * @return Text to be displayed.
     */
    private String adaptLanguage() {
        String currentLanguage = Locale.getDefault().getCountry();
        String[] translatedText = getAvailableText(currentLanguage);
        return String.join("", translatedText);

    }

    /**
     * Get the translation for info panel if available, English otherwise.
     *
     * @param language Language to get the translation for
     * @return Array of translated info panel text elements.
     */
    private String[] getAvailableText(String language) {
        HashMap<String, String[]> texts = new HashMap<>();

        String firstLineEN = "- Put in your desired Height(left) and Width(right) and click generate.\n\n";
        String secondLineEN = "- Tap on an image to see the details.\n\n";
        String thirdLineEN = "- Hold on an image and accept the dialog to delete the image.";
        String[] enText = new String[]{firstLineEN, secondLineEN, thirdLineEN};
        texts.put("EN", enText);

        String firstLineTR = "- İstenilen boy(solda) ve genişlik(sağda) değerlerini girip Oluştur butonuna basın.\n\n";
        String secondLineTR = "- Bir görselin detaylarını görmek için görsele tıklayın.\n\n";
        String thirdLineTR = "- Bir görseli silmek için görsele basılı tutup diyalogu kabul edin.";
        String[] trText = new String[]{firstLineTR, secondLineTR, thirdLineTR};
        texts.put("TR", trText);

        return texts.getOrDefault(language, texts.get("EN"));
    }


    /**
     * Represents a RecyclerView ViewHolder for displaying bear image details in a row of the RecyclerView.
     */
    private class BearImageRow extends RecyclerView.ViewHolder {

        /**
         * The ImageView to display the bear image.
         */
        ImageView image;

        /**
         * The TextView to display the dimensions (height and width) of the bear image.
         */
        TextView sizes;

        /**
         * The TextView to display the creation date of the bear image.
         */
        TextView date;

        /**
         * Creates a new instance of the BearImageRow class and initializes the views for bear image details.
         *
         * @param itemView The View representing the layout of a single row in the RecyclerView.
         */
        public BearImageRow(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk -> setSelectedImage());
            itemView.setOnLongClickListener(clk -> removeOnHold());
            image = itemView.findViewById(R.id.bearImageView);
            sizes = itemView.findViewById(R.id.bearImageSizesText);
            date = itemView.findViewById(R.id.bearImageDate);
        }

        /**
         * Sets the currently selected bear image in the ViewModel.
         * This method is called when a user taps on a bear image row in the RecyclerView.
         *
         * @return The position of the selected bear image in the RecyclerView.
         */
        private int setSelectedImage() {
            int position = getAbsoluteAdapterPosition();
            BearImage selected = images.get(position);
            bearModel.selectedImage.postValue(selected);
            return position;
        }

        /**
         * Generates a Snackbar with an "Undo" action to allow the user to undo the deletion of a bear image.
         * The Snackbar is shown when a user long-presses on a bear image row and confirms the deletion.
         *
         * @param mustDelete The BearImage object to be deleted.
         * @param position   The position of the bear image in the RecyclerView before deletion.
         * @return The generated Snackbar with an "Undo" action.
         */
        private Snackbar generateSnackBar(BearImage mustDelete, int position) {
            String local = Locale.getDefault().getCountry();
            String localText = "You deleted the image # ";
            String actionText = "Undo";
            if (local.equals("TR")) {
                localText = "Görsel silindi # ";
                actionText = "Iptal";
            }

            return Snackbar.make(sizes, localText + position, Snackbar.LENGTH_LONG).setAction(actionText, click -> {
                images.add(position, mustDelete);
                adapter.notifyItemInserted(position);
                runOnDifferentThread(() -> bearDao.insertImage(mustDelete));
            }).addCallback(new Snackbar.Callback() {
                @Override
                public void onShown(Snackbar sb) {
                    super.onShown(sb);
                    getSupportFragmentManager().popBackStack();
                }
            });
        }

        /**
         * Removes the currently selected bear image when the user confirms the deletion in the AlertDialog.
         * This method is called when a user long-presses on a bear image row.
         *
         * @return True if the deletion is confirmed and executed; false otherwise.
         */
        private boolean removeOnHold() {
            String local = Locale.getDefault().getCountry();
            String titleText = "Delete";
            String localText = "Do you want to delete this image?";
            String positiveText = "Yes";
            String negativeText = "No";

            if (local.equals("TR")) {
                titleText = "Silme";
                localText = "Bu görseli silmek istediğinize emin misiniz?";
                positiveText = "Evet";
                negativeText = "Hayır";
            }

            int position = setSelectedImage();
            builder.setMessage(localText).setTitle(titleText).setNegativeButton(negativeText, (dialog, which) -> {
            }).setPositiveButton(positiveText, (dialog, which) -> {
                BearImage mustDelete = bearModel.selectedImage.getValue();
                images.remove(mustDelete);
                adapter.notifyItemRemoved(position);
                runOnDifferentThread(() -> bearDao.deleteImage(mustDelete));
                generateSnackBar(mustDelete, position).show();
            }).create().show();
            return true;
        }

    }
}
