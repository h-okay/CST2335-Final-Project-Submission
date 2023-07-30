/**
 * TriviaQuestion.java
 * <p>
 * The TriviaQuestion class represents a trivia question. It includes the question text,
 * a list of possible answers, and the correct answer. The correct answer is also included
 * in the list of possible answers.
 *
 * @author Ahmed Almutawakel
 * @version 1.0
 * @since JDK 20
 */
package algonquin.cst2335.cst2335_finalproject.TriviaQuestions.data;

import java.util.List;

/**
 * The TriviaQuestion class represents a trivia question. It includes the question text,
 * a list of possible answers, and the correct answer. The correct answer is also included
 * in the list of possible answers.
 */
public class TriviaQuestion {

    private final String question;
    private final List<String> answers;
    private final String correctAnswer;

    /**
     * Creates a new instance of the TriviaQuestion class.
     *
     * @param question      The question text.
     * @param answers       A list of possible answers to the question.
     * @param correctAnswer The correct answer to the question.
     */
    public TriviaQuestion(String question, List<String> answers, String correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Returns the question text.
     *
     * @return A String containing the question text.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Returns a list of possible answers to the question.
     *
     * @return A List of Strings containing the possible answers.
     */
    public List<String> getAnswers() {
        return answers;
    }

    /**
     * Returns the correct answer to the question.
     *
     * @return A String containing the correct answer.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
