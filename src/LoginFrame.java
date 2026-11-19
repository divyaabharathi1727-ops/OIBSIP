import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public final class LoginFrame extends JFrame {
    private final User user;
    private final Consumer<User> onLogin;
    private final JTextField usernameField = new JTextField("student", 22);
    private final JPasswordField passwordField = new JPasswordField(22);
    private final JButton loginButton = Ui.primaryButton("Sign in");

    public LoginFrame(User user, Consumer<User> onLogin) {
        this.user = user;
        this.onLogin = onLogin;
        setTitle("Online Examination | Sign in");
        setSize(760, 560);
        setMinimumSize(new Dimension(560, 460));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginButton.addActionListener(event -> login());
        setContentPane(buildContent());
        getRootPane().setDefaultButton(loginButton);
    }

    private JPanel buildContent() {
        JPanel page = Ui.page();
        page.setLayout(new GridBagLayout());
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Ui.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Ui.LINE),
                BorderFactory.createEmptyBorder(34, 38, 34, 38)));
        form.setPreferredSize(new Dimension(390, 390));
        GridBagConstraints item = new GridBagConstraints();
        item.gridx = 0;
        item.gridy = 0;
        item.weightx = 1;
        item.fill = GridBagConstraints.HORIZONTAL;
        item.insets = new Insets(0, 0, 9, 0);
        form.add(new JLabel("EXAM PORTAL"), item);
        item.gridy++;
        item.insets = new Insets(0, 0, 8, 0);
        form.add(Ui.labels().create("Welcome back", Ui.TITLE, Ui.INK), item);
        item.gridy++;
        item.insets = new Insets(0, 0, 25, 0);
        form.add(Ui.labels().create("Sign in to continue to your examination.", Ui.BODY, Ui.MUTED), item);
        item.gridy++;
        item.insets = new Insets(0, 0, 7, 0);
        form.add(fieldLabel("Username"), item);
        item.gridy++;
        item.insets = new Insets(0, 0, 17, 0);
        form.add(styleField(usernameField), item);
        item.gridy++;
        item.insets = new Insets(0, 0, 7, 0);
        form.add(fieldLabel("Password"), item);
        item.gridy++;
        item.insets = new Insets(0, 0, 22, 0);
        form.add(styleField(passwordField), item);
        item.gridy++;
        item.insets = new Insets(0, 0, 0, 0);
        form.add(loginButton, item);
        page.add(form);
        return page;
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter your username and password.",
                    "Missing information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!user.getUsername().equals(username) || !user.authenticate(password)) {
            passwordField.setText("");
            JOptionPane.showMessageDialog(this, "The username or password is incorrect.",
                    "Sign in failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dispose();
        onLogin.accept(user);
    }

    private static JLabel fieldLabel(String text) {
        return Ui.labels().create(text, Ui.BODY_BOLD, Ui.INK);
    }

    private static JTextField styleField(JTextField field) {
        field.setFont(Ui.BODY);
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Ui.LINE),
                BorderFactory.createEmptyBorder(9, 10, 9, 10)));
        return field;
    }
}