package algonquin.cst2335.cst2335_finalproject.bearimages.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImage;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImageDao;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImageDatabase;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImageDetailsFragment;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImageViewModel;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivityBearImageListBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.BearEntryBinding;


//TODO: Need to refresh the view on load so that database items are shown
@SuppressLint("DefaultLocale")
public class BearApplication extends AppCompatActivity {

    private static final String url = "https://placebear.com/%d/%d";
    private static final String currentDate = java.time.LocalDate.now().toString();
    private ActivityBearImageListBinding binding;
    private BearImageViewModel bearModel;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private ArrayList<BearImage> images;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBearImageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        prefs = getSharedPreferences("BearAppData", Context.MODE_PRIVATE);
        int savedHeight = prefs.getInt("bearHeight", 0);
        int savedWidth = prefs.getInt("bearWidth", 0);

        binding.HeightNewTextArea.setText(String.valueOf(savedHeight));
        binding.WidthNewTextArea.setText(String.valueOf(savedWidth));


        BearImageDatabase bearImageDatabase = Room.databaseBuilder(getApplicationContext(), BearImageDatabase.class, "bear-image-database").build();
        BearImageDao bearDao = bearImageDatabase.bearImageDao();

        bearModel = new ViewModelProvider(this).get(BearImageViewModel.class);
        images = bearModel.bearImages.getValue();
        if (images == null) {
            bearModel.bearImages.setValue(images = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                images.addAll(bearDao.getAllImages());
                bearModel.bearImages.postValue(images);
                runOnUiThread(() -> binding.recyclerView.setAdapter(adapter));
            });
        }

        binding.addNewButton.setOnClickListener(click -> {
            int height;
            int width;
            RequestQueue queue;

            if (checkIfInputValid()) {
                height = Integer.parseInt(binding.HeightNewTextArea.getText().toString());
                width = Integer.parseInt(binding.WidthNewTextArea.getText().toString());

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("bearHeight", height);
                editor.putInt("bearWidth", width);
                editor.apply();

                String parameterizedUrl = String.format(url, height, width);
                queue = Volley.newRequestQueue(this.getApplicationContext());
                ImageRequest imageRequest = new ImageRequest(parameterizedUrl, new Response.Listener<>() {

                    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                        return outputStream.toByteArray();
                    }

                    @Override
                    public void onResponse(Bitmap response) {
                        byte[] imageData = getBitmapAsByteArray(response);
                        BearImage newBearImage = new BearImage(imageData, height, width, currentDate);
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            bearDao.insertImage(newBearImage);
                            runOnUiThread(() -> {
                                images.add(newBearImage);
                                adapter.notifyItemInserted(images.size() - 1);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(BearApplication.this));
                            });
                        });
                    }
                }, 0, 0, null, null, error -> Toast.makeText(BearApplication.this, "There was an error, please try again.", Toast.LENGTH_SHORT).show());
                queue.add(imageRequest);
                binding.recyclerView.setAdapter(adapter = new RecyclerView.Adapter<>() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        BearEntryBinding binding = BearEntryBinding.inflate(getLayoutInflater());
                        return new MyRowHolder(binding.getRoot());
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        if (holder instanceof MyRowHolder) {
                            MyRowHolder myRowHolder = (MyRowHolder) holder;

                            BearImage bearImage = images.get(position);
                            byte[] imageData = bearImage.getImage();
                            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                            String sizes = bearImage.getHeight() + "x" + bearImage.getWidth();
                            String date = bearImage.getCreatedDate();

                            myRowHolder.image.setImageBitmap(imageBitmap);
                            myRowHolder.sizes.setText(sizes);
                            myRowHolder.date.setText(date);
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return images.size();
                    }
                });
            }
        });

        bearModel.selectedImage.observe(this, newImageValue -> {
            BearImageDetailsFragment imageFragment = new BearImageDetailsFragment(newImageValue);
            FragmentManager fManager = getSupportFragmentManager();
            FragmentTransaction tx = fManager.beginTransaction();
            tx.replace(R.id.fragmentLocation, imageFragment);
            tx.addToBackStack("");
            tx.commit();
        });
    }


    private boolean checkIfInputValid() {
        boolean valid = false;

        String heightText = binding.HeightNewTextArea.getText().toString().trim();
        String widthText = binding.WidthNewTextArea.getText().toString().trim();

        if (!heightText.isEmpty() && !widthText.isEmpty()) {
            int height = Integer.parseInt(heightText);
            int width = Integer.parseInt(widthText);

            if (height <= 500 && width <= 500) {
                valid = true;
            }
        }

        return valid;
    }


    private class MyRowHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView sizes;
        TextView date;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);


            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                BearImage selected = images.get(position);
                bearModel.selectedImage.postValue(selected);
            });

            itemView.setOnLongClickListener(clk -> {
                BearImageDatabase bearImageDatabase = Room.databaseBuilder(getApplicationContext(), BearImageDatabase.class, "bear-image-database").build();
                BearImageDao bearDao = bearImageDatabase.bearImageDao();

                int position = getAbsoluteAdapterPosition();
                BearImage selected = images.get(position);
                bearModel.selectedImage.postValue(selected);

                AlertDialog.Builder builder = new AlertDialog.Builder(BearApplication.this);
                builder.setMessage("Do you want to delete this image?").setTitle("Delete").setNegativeButton("No", (dialog, which) -> {
                }).setPositiveButton("Yes", (dialog, which) -> {
                    BearImage mustDelete = images.get(position);
                    images.remove(mustDelete);
                    adapter.notifyItemRemoved(position);

                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> bearDao.deleteImage(mustDelete));

                    Snackbar.make(sizes, "You deleted the image # " + position, Snackbar.LENGTH_LONG).setAction("Undo", click -> {
                        images.add(position, mustDelete);
                        adapter.notifyItemInserted(position);
                        thread.execute(() -> bearDao.insertImage(mustDelete));
                    }).addCallback(new Snackbar.Callback() {
                        @Override
                        public void onShown(Snackbar sb) {
                            super.onShown(sb);
                            getSupportFragmentManager().popBackStack();
                        }
                    }).show();
                }).create().show();
                return true;
            });

            image = itemView.findViewById(R.id.bearImageView);
            sizes = itemView.findViewById(R.id.bearImageSizesText);
            date = itemView.findViewById(R.id.bearImageDate);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bear_app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.bearHelp) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BearApplication.this);
            builder.setTitle("How to use").setMessage("- Put in your desired Height(left) and Width(right) and click generate.\n\n- Tap on an image to see the details.\n\n- Hold on an image and accept the dialog to delete the image.").setPositiveButton("OK", (dialog, which) -> {
            }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }
}
