public final class ExamManagerTest {
    private ExamManagerTest() { }

    public static void main(String[] args) {
        User user = new User("student", "1234", "Student");
        ExamManager manager = new ExamManager(user, 60);

        check(manager.getQuestions().size() == 10, "expected ten questions");
        check(manager.getQuestions().stream().allMatch(question -> question.getOptions().size() == 4),
                "every question must have four options");
        manager.setAnswer(0, manager.getQuestions().get(0).getCorrectOption());
        manager.setAnswer(1, 0);
        check(manager.getAnswer(0) == manager.getQuestions().get(0).getCorrectOption(),
                "selected answers must persist");

        ExamResult result = manager.submit();
        check(result.getCorrect() == 1, "expected one correct answer");
        check(result.getIncorrect() == 1, "expected one incorrect answer");
        check(result.getUnanswered() == 8, "expected eight unanswered answers");
        check(result.getCorrect() + result.getIncorrect() + result.getUnanswered() == result.getTotal(),
                "answer totals must equal the question count");
        check(manager.submit() == result, "repeated submission must return the original result");

        boolean rejectedMutation = false;
        try {
            manager.setAnswer(2, 0);
        } catch (IllegalStateException expected) {
            rejectedMutation = true;
        }
        check(rejectedMutation, "answers must not change after submission");

        ExamManager timedManager = new ExamManager(user, 1);
        timedManager.start();
        try {
            Thread.sleep(1100);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AssertionError("timer test interrupted", exception);
        }
        check(timedManager.getRemainingSeconds() == 0, "countdown must reach zero");
        check(timedManager.submit().getTimeTakenSeconds() == 1,
                "expired exam should record the full configured duration");
        System.out.println("ExamManager tests passed.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}