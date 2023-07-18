package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TriviaRepository {

    private static final String TAG = "TriviaRepository";
    private final TriviaDAO triviaDao;
    private final LiveData<List<Trivia>> allTrivia;
    private final RequestQueue queue;

    private final ExecutorService executorService;

    TriviaRepository(Application application) {
        TriviaDatabase db = TriviaDatabase.getDatabase(application);
        triviaDao = db.triviaDao();
        allTrivia = triviaDao.getAllTrivia();
        queue = Volley.newRequestQueue(application.getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();
    }

    LiveData<List<Trivia>> getAllTrivia() {
        return allTrivia;
    }

    public void insert(Trivia trivia) {
        executorService.execute(() -> triviaDao.insert(trivia));
    }

    public void deleteAll() {
        executorService.execute(() -> triviaDao.deleteAll());
    }

    public void fetchDataFromAPI() {
        // Using Volley to insert data
        // Construct the request
        String url = "https://opentdb.com/api.php?amount=10&category=22&type=multiple";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the JSON and insert into Room DB
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject result = results.getJSONObject(i);

                                String category = result.getString("category");
                                String difficulty = result.getString("difficulty");
                                String question = result.getString("question");
                                String correctAnswer = result.getString("correct_answer");
                                List<String> incorrectAnswers = Converters.stringToList(result.getString("incorrect_answers"));

                                insert(new Trivia(0, category, difficulty, question, correctAnswer, incorrectAnswers));
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON", e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
