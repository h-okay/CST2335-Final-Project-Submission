package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import java.util.List;

public class TriviaQuestion {

    private String question;
    private List<String> answers;
    private String correctAnswer;

    public TriviaQuestion(String question, List<String> answers, String correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
