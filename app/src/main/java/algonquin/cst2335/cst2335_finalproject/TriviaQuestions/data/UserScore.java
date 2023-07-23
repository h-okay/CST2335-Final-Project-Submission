package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserScore {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String userName;
    private int score;

    // Empty constructor used by Room
    public UserScore() {

    }

    // Parameterized constructor for your own use
    public UserScore(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    // Getters
    public int getId() {

        return id;
    }

    public String getUserName() {

        return userName;
    }

    public int getScore() {
        return score;
    }

    // Setters
    public void setId(int id) {

        this.id = id;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return this.getUserName() + ": " + this.getScore();
    }
}
