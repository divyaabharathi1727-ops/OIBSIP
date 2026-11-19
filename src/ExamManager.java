import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class ExamManager {
    public static final int DEFAULT_DURATION_SECONDS = 30 * 60;

    private final User user;
    private final List<Question> questions;
    private final int[] answers;
    private final int durationSeconds;
    private long startedAtNanos;
    private boolean started;
    private ExamResult result;

    public ExamManager(User user) { this(user, DEFAULT_DURATION_SECONDS); }

    public ExamManager(User user, int durationSeconds) {
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("Exam duration must be positive.");
        }
        this.user = user;
        this.durationSeconds = durationSeconds;
        this.questions = createQuestions();
        this.answers = new int[questions.size()];
        Arrays.fill(answers, -1);
    }

    private static List<Question> createQuestions() {
        List<Question> items = new ArrayList<>();
        items.add(new Question("Which keyword is used to inherit a class in Java?",
                List.of("implements", "extends", "inherits", "instanceof"), 1));
        items.add(new Question("Which method is the usual entry point of a Java application?",
                List.of("start()", "run()", "main()", "init()"), 2));
        items.add(new Question("Which collection does not allow duplicate elements?",
                List.of("ArrayList", "LinkedList", "HashSet", "Vector"), 2));
        items.add(new Question("What is encapsulation primarily used to do?",
                List.of("Hide internal state behind a controlled interface", "Create multiple threads",
                        "Convert source code to bytecode", "Sort objects automatically"), 0));
        items.add(new Question("Which type can store the value true or false?",
                List.of("int", "String", "boolean", "char"), 2));
        items.add(new Question("What does method overloading require?",
                List.of("Same name with different parameter lists", "Different names with the same body",
                        "A subclass for every method", "A static method only"), 0));
        items.add(new Question("Which access modifier allows access from any class?",
                List.of("private", "protected", "public", "default"), 2));
        items.add(new Question("Which data structure follows first-in, first-out order?",
                List.of("Stack", "Queue", "Tree", "Graph"), 1));
        items.add(new Question("What is the result of 7 / 2 when both operands are int?",
                List.of("3", "3.5", "4", "Compilation error"), 0));
        items.add(new Question("Which keyword prevents a class from being subclassed?",
                List.of("static", "const", "final", "private"), 2));
        return List.copyOf(items);
    }

    public User getUser() { return user; }
    public List<Question> getQuestions() { return questions; }
    public int getAnswer(int questionIndex) { return answers[questionIndex]; }

    public void setAnswer(int questionIndex, int optionIndex) {
        if (result != null) {
            throw new IllegalStateException("The exam has already been submitted.");
        }
        if (optionIndex < -1 || optionIndex >= questions.get(questionIndex).getOptions().size()) {
            throw new IllegalArgumentException("Answer option is out of range.");
        }
        answers[questionIndex] = optionIndex;
    }

    public void start() {
        if (!started) {
            startedAtNanos = System.nanoTime();
            started = true;
        }
    }

    public int getRemainingSeconds() {
        if (!started) { return durationSeconds; }
        long elapsed = System.nanoTime() - startedAtNanos;
        long elapsedSeconds = TimeUnit.NANOSECONDS.toSeconds(Math.max(0, elapsed));
        return (int) Math.max(0, durationSeconds - elapsedSeconds);
    }

    public boolean isSubmitted() { return result != null; }

    public ExamResult submit() {
        if (result != null) { return result; }
        start();
        int correct = 0;
        int incorrect = 0;
        int unanswered = 0;
        for (int index = 0; index < questions.size(); index++) {
            if (answers[index] == -1) { unanswered++; }
            else if (answers[index] == questions.get(index).getCorrectOption()) { correct++; }
            else { incorrect++; }
        }
        int elapsedSeconds = durationSeconds - getRemainingSeconds();
        result = new ExamResult(questions, answers, correct, incorrect, unanswered, elapsedSeconds);
        return result;
    }
}