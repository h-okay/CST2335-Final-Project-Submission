package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "trivia_table")
public class Trivia {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "difficulty")
    private String difficulty;

    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "correct_answer")
    private String correctAnswer;

    @ColumnInfo(name = "incorrect_answers")
    private List<String> incorrectAnswers;

    public Trivia(){}

    public Trivia(int id, String category, String difficulty, String question, String correctAnswer, List<String> incorrectAnswers) {
        this.id = id;
        this.category = category;
        this.difficulty = difficulty;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCategory(){
        return this.category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getDifficulty(){
        return this.difficulty;
    }

    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;
    }

    public String getQuestion(){
        return this.question;
    }

    public void setQuestion(String question){
        this.question = question;
    }

    public String getCorrectAnswer(){
        return this.correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer){
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers(){
        return this.incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers){
        this.incorrectAnswers = incorrectAnswers;
    }
}
