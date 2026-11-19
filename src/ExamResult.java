import java.util.List;

public final class ExamResult {
    private final List<Question> questions;
    private final int[] answers;
    private final int correct;
    private final int incorrect;
    private final int unanswered;
    private final int timeTakenSeconds;

    ExamResult(List<Question> questions, int[] answers, int correct, int incorrect,
            int unanswered, int timeTakenSeconds) {
        this.questions = List.copyOf(questions);
        this.answers = answers.clone();
        this.correct = correct;
        this.incorrect = incorrect;
        this.unanswered = unanswered;
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public List<Question> getQuestions() { return questions; }
    public int getAnswer(int questionIndex) { return answers[questionIndex]; }
    public int getCorrect() { return correct; }
    public int getIncorrect() { return incorrect; }
    public int getUnanswered() { return unanswered; }
    public int getTotal() { return questions.size(); }
    public int getTimeTakenSeconds() { return timeTakenSeconds; }
    public double getPercentage() { return getTotal() == 0 ? 0 : correct * 100.0 / getTotal(); }
}