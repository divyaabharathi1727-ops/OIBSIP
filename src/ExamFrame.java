import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public final class ExamFrame extends JFrame {
    private final ExamManager manager;
    private final Consumer<ExamResult> onSubmit;
    private final JLabel timerLabel = new JLabel("30:00", SwingConstants.RIGHT);
    private final JLabel progressLabel = new JLabel();
    private final JLabel questionLabel = new JLabel();
    private final JPanel optionsPanel = new JPanel();
    private final JButton previousButton = Ui.secondaryButton("Previous");
    private final JButton nextButton = Ui.primaryButton("Next");
    private final Timer timer;
    private int currentQuestion;

    public ExamFrame(User user, ExamManager manager, Consumer<ExamResult> onSubmit) {
        this.manager = manager;
        this.onSubmit = onSubmit;
        setTitle("Online Examination | In progress");
        setSize(900, 680);
        setMinimumSize(new java.awt.Dimension(640, 520));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent event) { confirmQuit(); }
        });
        setContentPane(buildContent());
        manager.start();
        timer = new Timer(1000, event -> updateClock());
        timer.setInitialDelay(0);
        timer.start();
        renderQuestion();
    }

    @Override
    public void dispose() {
        if (timer != null) { timer.stop(); }
        super.dispose();
    }

    private JPanel buildContent() {
        JPanel page = Ui.page();
        page.setLayout(new BorderLayout(0, 25));
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel identity = new JPanel(new GridBagLayout());
        identity.setOpaque(false);
        GridBagConstraints identityItem = new GridBagConstraints();
        identityItem.gridx = 0;
        identityItem.gridy = 0;
        identityItem.anchor = GridBagConstraints.WEST;
        identity.add(Ui.labels().create("JAVA PROGRAMMING", Ui.BODY_BOLD, Ui.TEAL), identityItem);
        identityItem.gridy++;
        identityItem.insets = new Insets(6, 0, 0, 0);
        identity.add(Ui.labels().create("Examination in progress", Ui.TITLE, Ui.INK), identityItem);
        header.add(identity, BorderLayout.WEST);
        JPanel clock = new JPanel(new GridBagLayout());
        clock.setBackground(Ui.WHITE);
        clock.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Ui.LINE),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)));
        GridBagConstraints clockItem = new GridBagConstraints();
        clockItem.gridx = 0;
        clockItem.gridy = 0;
        clock.add(Ui.labels().create("TIME REMAINING", Ui.BODY_BOLD, Ui.MUTED), clockItem);
        clockItem.gridy++;
        clockItem.insets = new Insets(3, 0, 0, 0);
        timerLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        timerLabel.setForeground(Ui.TEAL);
        clock.add(timerLabel, clockItem);
        header.add(clock, BorderLayout.EAST);
        page.add(header, BorderLayout.NORTH);

        JPanel questionCard = new JPanel(new BorderLayout(0, 25));
        questionCard.setBackground(Ui.WHITE);
        questionCard.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Ui.LINE),
                BorderFactory.createEmptyBorder(28, 30, 28, 30)));
        progressLabel.setFont(Ui.BODY_BOLD);
        progressLabel.setForeground(Ui.TEAL);
        questionCard.add(progressLabel, BorderLayout.NORTH);
        questionLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        questionLabel.setForeground(Ui.INK);
        questionLabel.setVerticalAlignment(SwingConstants.TOP);
        questionCard.add(questionLabel, BorderLayout.CENTER);
        optionsPanel.setBackground(Ui.WHITE);
        optionsPanel.setLayout(new GridBagLayout());
        questionCard.add(optionsPanel, BorderLayout.SOUTH);
        page.add(questionCard, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        JPanel navigation = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navigation.setOpaque(false);
        previousButton.addActionListener(event -> navigate(-1));
        nextButton.addActionListener(event -> navigate(1));
        navigation.add(previousButton);
        navigation.add(nextButton);
        footer.add(navigation, BorderLayout.WEST);
        JButton submit = Ui.primaryButton("Submit exam");
        submit.setBackground(new java.awt.Color(184, 70, 55));
        submit.addActionListener(event -> confirmSubmission());
        footer.add(submit, BorderLayout.EAST);
        page.add(footer, BorderLayout.SOUTH);
        return page;
    }

    private void renderQuestion() {
        Question question = manager.getQuestions().get(currentQuestion);
        progressLabel.setText(String.format("QUESTION %02d   /   %02d", currentQuestion + 1,
                manager.getQuestions().size()));
        questionLabel.setText("<html><div style='width: 650px'>" + escapeHtml(question.getPrompt()) + "</div></html>");
        optionsPanel.removeAll();
        ButtonGroup group = new ButtonGroup();
        GridBagConstraints optionItem = new GridBagConstraints();
        optionItem.gridx = 0;
        optionItem.weightx = 1;
        optionItem.fill = GridBagConstraints.HORIZONTAL;
        optionItem.anchor = GridBagConstraints.WEST;
        optionItem.insets = new Insets(5, 0, 5, 0);
        for (int index = 0; index < question.getOptions().size(); index++) {
            JRadioButton option = new JRadioButton(question.getOptions().get(index));
            option.setFont(Ui.BODY);
            option.setForeground(Ui.INK);
            option.setBackground(Ui.WHITE);
            option.setFocusPainted(false);
            if (manager.getAnswer(currentQuestion) == index) { option.setSelected(true); }
            final int selected = index;
            option.addActionListener(event -> manager.setAnswer(currentQuestion, selected));
            group.add(option);
            optionItem.gridy = index;
            optionsPanel.add(option, optionItem);
        }
        previousButton.setEnabled(currentQuestion > 0);
        nextButton.setEnabled(currentQuestion < manager.getQuestions().size() - 1);
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private void navigate(int direction) {
        int destination = currentQuestion + direction;
        if (destination >= 0 && destination < manager.getQuestions().size()) {
            currentQuestion = destination;
            renderQuestion();
        }
    }

    private void updateClock() {
        int remaining = manager.getRemainingSeconds();
        timerLabel.setText(String.format("%02d:%02d", remaining / 60, remaining % 60));
        timerLabel.setForeground(remaining <= 60 ? new java.awt.Color(184, 70, 55) : Ui.TEAL);
        if (remaining <= 0) { submitExam(); }
    }

    private void confirmSubmission() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to submit your examination?", "Submit examination",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) { submitExam(); }
    }

    private void submitExam() {
        if (manager.isSubmitted()) { return; }
        timer.stop();
        ExamResult result = manager.submit();
        dispose();
        onSubmit.accept(result);
    }

    private void confirmQuit() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?",
                "Quit examination", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            timer.stop();
            dispose();
            System.exit(0);
        }
    }
}