import java.awt.BorderLayout;
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

public final class ProfileFrame extends JFrame {
    private final User user;
    private final Consumer<User> onContinue;
    private final JTextField displayNameField = new JTextField(24);
    private final JPasswordField passwordField = new JPasswordField(24);

    public ProfileFrame(User user, Consumer<User> onContinue) {
        this.user = user;
        this.onContinue = onContinue;
        setTitle("Online Examination | Profile");
        setSize(780, 600);
        setMinimumSize(new java.awt.Dimension(560, 440));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        JPanel page = Ui.page();
        page.setLayout(new BorderLayout(0, 22));
        JPanel heading = new JPanel(new GridBagLayout());
        heading.setOpaque(false);
        GridBagConstraints item = new GridBagConstraints();
        item.gridx = 0;
        item.gridy = 0;
        item.anchor = GridBagConstraints.WEST;
        item.insets = new Insets(0, 0, 8, 0);
        heading.add(Ui.labels().create("YOUR PROFILE", Ui.BODY_BOLD, Ui.TEAL), item);
        item.gridy++;
        heading.add(Ui.labels().create("Make it yours", Ui.TITLE, Ui.INK), item);
        item.gridy++;
        item.insets = new Insets(7, 0, 0, 0);
        heading.add(Ui.labels().create("Review your details before starting the exam.", Ui.BODY, Ui.MUTED), item);
        page.add(heading, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridBagLayout());
        details.setBackground(Ui.WHITE);
        details.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Ui.LINE),
                BorderFactory.createEmptyBorder(26, 30, 26, 30)));
        GridBagConstraints field = new GridBagConstraints();
        field.gridx = 0;
        field.gridy = 0;
        field.weightx = 1;
        field.fill = GridBagConstraints.HORIZONTAL;
        field.anchor = GridBagConstraints.WEST;
        field.insets = new Insets(0, 0, 7, 0);
        details.add(fieldLabel("Username"), field);
        field.gridy++;
        field.insets = new Insets(0, 0, 19, 0);
        details.add(Ui.labels().create(user.getUsername(), Ui.BODY, Ui.MUTED), field);
        field.gridy++;
        field.insets = new Insets(0, 0, 7, 0);
        details.add(fieldLabel("Display name"), field);
        field.gridy++;
        field.insets = new Insets(0, 0, 17, 0);
        displayNameField.setText(user.getDisplayName());
        details.add(styleField(displayNameField), field);
        field.gridy++;
        field.insets = new Insets(0, 0, 7, 0);
        details.add(fieldLabel("Password"), field);
        field.gridy++;
        field.insets = new Insets(0, 0, 0, 0);
        passwordField.setText(user.getPassword());
        details.add(styleField(passwordField), field);
        page.add(details, BorderLayout.CENTER);

        JPanel actions = new JPanel(new BorderLayout());
        actions.setOpaque(false);
        JButton save = Ui.secondaryButton("Save profile");
        save.addActionListener(event -> saveProfile(true));
        JButton continueButton = Ui.primaryButton("Continue to exam");
        continueButton.addActionListener(event -> continueToExam());
        actions.add(save, BorderLayout.WEST);
        actions.add(continueButton, BorderLayout.EAST);
        page.add(actions, BorderLayout.SOUTH);
        return page;
    }

    private void continueToExam() {
        if (!saveProfile(false)) { return; }
        int choice = JOptionPane.showConfirmDialog(this, "Continue to the examination?",
                "Start examination", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            onContinue.accept(user);
        }
    }

    private boolean saveProfile(boolean showConfirmation) {
        String displayName = displayNameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (displayName.isEmpty() || password.isBlank()) {
            JOptionPane.showMessageDialog(this, "Display name and password cannot be empty.",
                    "Missing information", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        user.updateProfile(displayName, password);
        if (showConfirmation) {
            JOptionPane.showMessageDialog(this, "Your profile has been updated.",
                    "Profile saved", JOptionPane.INFORMATION_MESSAGE);
        }
        return true;
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