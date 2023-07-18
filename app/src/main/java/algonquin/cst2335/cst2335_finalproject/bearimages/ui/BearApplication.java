package algonquin.cst2335.cst2335_finalproject.bearimages.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewbinding.ViewBinding;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

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
//TODO: Details event listener
//TODO: Delete functionality
//TODO:Each activity must use Volley to retrieve data from an http server. You cannot use Executor or AsyncTask.
@SuppressLint("DefaultLocale")
public class BearApplication extends AppCompatActivity {


    private static final String url = "https://placebear.com/%d/%d";
    private static final String currentDate = java.time.LocalDate.now().toString();
    ActivityBearImageListBinding binding;
    BearImageViewModel bearModel;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    ArrayList<BearImage> images;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBearImageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BearImageDatabase bearImageDatabase = Room.databaseBuilder(
                getApplicationContext(), BearImageDatabase.class, "bear-image-database").build();
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

        // Adding new images
        binding.addNewButton.setOnClickListener(click -> {
            int height;
            int width;
            RequestQueue queue;

            if (checkIfInputValid()) {
                height = Integer.parseInt(binding.HeightNewTextArea.getText().toString());
                width = Integer.parseInt(binding.WidthNewTextArea.getText().toString());
                String parameterizedUrl = String.format(url, height, width);
                queue = Volley.newRequestQueue(this.getApplicationContext());
                ImageRequest imageRequest = new ImageRequest(parameterizedUrl, new Response.Listener<Bitmap>() {

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
                }, 0, 0, null, null, error -> Toast.makeText(
                                BearApplication.this,
                                "There was an error, please try again.",
                                Toast.LENGTH_SHORT)
                        .show());
                queue.add(imageRequest);
                binding.recyclerView.setAdapter(adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        ViewBinding binding = BearEntryBinding.inflate(getLayoutInflater());
                        return new MyRowHolder(binding.getRoot());
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        if (holder instanceof MyRowHolder) {
                            MyRowHolder myRowHolder = (MyRowHolder) holder;

                            BearImage bearImage = images.get(position);
                            byte[] imageData = bearImage.getImage();
                            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                            String sizes = bearImage.getWidth() + "x" + bearImage.getHeight();
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

        bearModel.selectedImage.observe(this, (newImageValue) -> {
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
            image = itemView.findViewById(R.id.bearImageView);
            sizes = itemView.findViewById(R.id.bearImageSizesText);
            date = itemView.findViewById(R.id.bearImageDate);
        }

    }
}
