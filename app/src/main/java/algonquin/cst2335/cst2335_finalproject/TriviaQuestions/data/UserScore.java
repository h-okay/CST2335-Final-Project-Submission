/**
 * UserScore.java
 * <p>
 * The UserScore class represents a user's score in the trivia game.
 * It includes the user's unique ID, username, and their score.
 * <p>
 * This class is also an entity in the Room database.
 *
 * @author Ahmed Almutawakel
 * @version 1.0
 * @since JDK 20
 */
package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The UserScore class represents a user's score in the trivia game.
 */
@Entity
public class UserScore {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String userName;
    private int score;

    // Empty constructor used by Room
    public UserScore() {

    }

    /**
     * Creates a new instance of the UserScore class.
     *
     * @param userName The username of the user.
     * @param score The score of the user.
     */
    public UserScore(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    // Getters
    /**
     * Returns the ID of the user.
     *
     * @return An int containing the user's ID.
     */
    public int getId() {

        return id;
    }

    /**
     * Returns the username of the user.
     *
     * @return A String containing the user's username.
     */
    public String getUserName() {

        return userName;
    }

    /**
     * Returns the score of the user.
     *
     * @return An int containing the user's score.
     */
    public int getScore() {
        return score;
    }

    // Setters
    /**
     * Sets the ID of the user.
     *
     * @param id An int containing the user's ID.
     */
    public void setId(int id) {

        this.id = id;
    }

    /**
     * Sets the username of the user.
     *
     * @param userName A String containing the user's username.
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    /**
     * Sets the score of the user.
     *
     * @param score An int containing the user's score.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the user's username and score as a string.
     *
     * @return A String containing the user's username and score.
     */
    @NonNull
    @Override
    public String toString() {
        return this.getUserName() + ": " + this.getScore();
    }
}
