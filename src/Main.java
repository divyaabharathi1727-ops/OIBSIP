import javax.swing.SwingUtilities;

public final class Main {
    private static final User USER = new User("student", "1234", "Student");

    private Main() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::showLogin);
    }

    private static void showLogin() {
        new LoginFrame(USER, Main::showProfile).setVisible(true);
    }

    private static void showProfile(User user) {
        new ProfileFrame(user, Main::startExam).setVisible(true);
    }

    private static void startExam(User user) {
        ExamManager manager = new ExamManager(user);
        new ExamFrame(user, manager, result -> showResult(user, result)).setVisible(true);
    }

    private static void showResult(User user, ExamResult result) {
        new ResultFrame(user, result, Main::showLogin).setVisible(true);
    }
}