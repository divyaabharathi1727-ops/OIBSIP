import java.util.List;

public final class Question {
    private final String prompt;
    private final List<String> options;
    private final int correctOption;

    public Question(String prompt, List<String> options, int correctOption) {
        if (options == null || options.size() != 4) {
            throw new IllegalArgumentException("A question must have exactly four options.");
        }
        if (correctOption < 0 || correctOption >= options.size()) {
            throw new IllegalArgumentException("The correct option must be between 0 and 3.");
        }
        this.prompt = prompt;
        this.options = List.copyOf(options);
        this.correctOption = correctOption;
    }

    public String getPrompt() { return prompt; }
    public List<String> getOptions() { return options; }
    public int getCorrectOption() { return correctOption; }
    public String getCorrectAnswer() { return options.get(correctOption); }
}