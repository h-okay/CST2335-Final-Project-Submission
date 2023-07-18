package algonquin.cst2335.cst2335_finalproject.bearimages.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewbinding.ViewBinding;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImage;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImageDao;
import algonquin.cst2335.cst2335_finalproject.bearimages.data.BearImageDatabase;
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
        RequestQueue queue = Volley.newRequestQueue(this);

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

        binding.addNewButton.setOnClickListener(click -> {
            int height;
            int width;
            try {
                height = Integer.parseInt(binding.HeightNewTextArea.getText().toString());
                width = Integer.parseInt(binding.WidthNewTextArea.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Height and Width are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (height > 500 || width > 500) {
                Toast.makeText(this, "Height and Width must be smaller than 500", Toast.LENGTH_SHORT).show();
                return;
            }

            String parameterizedUrl = String.format(url, height, width);
            try {
                getNewImage(queue, parameterizedUrl);
//                        Bitmap bitmap = BitmapFactory.decodeStream(requestUrl.openConnection().getInputStream());
//                        Log.d("BearApplication", bitmap.toString());
//                        byte[] imageData = getBitmapAsByteArray(bitmap);
//                        BearImage newBearImage = new BearImage(imageData, height, width, currentDate);
//                        bearDao.insertImage(newBearImage);
//                        runOnUiThread(() -> {
//                            images.add(newBearImage);
//                            adapter.notifyItemInserted(images.size() - 1);
//                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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

    private void getNewImage(RequestQueue queue, String url) {
//        ImageRequest imageRequest = new ImageRequest(url,
//                 );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d("BearApplication", response.toString());
                },
                error -> {
                    Toast.makeText(this, "There was an error. Please try again.", Toast.LENGTH_SHORT).show();
                });
        queue.add(jsonObjectRequest);
    }


    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

//    private class BitmapListener implements Response.Listener<Bitmap> {
//
//    }


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
