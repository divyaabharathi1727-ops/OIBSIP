import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public final class ResultFrame extends JFrame {
    public ResultFrame(User user, ExamResult result, Runnable onLogout) {
        setTitle("Online Examination | Results");
        setSize(1020, 760);
        setMinimumSize(new java.awt.Dimension(700, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(buildContent(user, result, onLogout));
    }

    private JPanel buildContent(User user, ExamResult result, Runnable onLogout) {
        JPanel page = Ui.page();
        page.setLayout(new BorderLayout(0, 18));
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel title = new JPanel(new GridBagLayout());
        title.setOpaque(false);
        GridBagConstraints titleItem = new GridBagConstraints();
        titleItem.gridx = 0;
        titleItem.gridy = 0;
        titleItem.anchor = GridBagConstraints.WEST;
        title.add(Ui.labels().create("EXAMINATION COMPLETE", Ui.BODY_BOLD, Ui.TEAL), titleItem);
        titleItem.gridy++;
        titleItem.insets = new Insets(5, 0, 0, 0);
        title.add(Ui.labels().create("Your results", Ui.TITLE, Ui.INK), titleItem);
        header.add(title, BorderLayout.WEST);
        JButton logout = Ui.secondaryButton("Log out");
        logout.addActionListener(event -> { dispose(); onLogout.run(); });
        header.add(logout, BorderLayout.EAST);
        page.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setOpaque(false);
        JPanel summary = new JPanel(new GridBagLayout());
        summary.setBackground(Ui.WHITE);
        summary.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Ui.LINE),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        GridBagConstraints metric = new GridBagConstraints();
        metric.gridy = 0;
        metric.weightx = 1;
        metric.fill = GridBagConstraints.HORIZONTAL;
        addMetric(summary, metric, 0, "STUDENT", user.getDisplayName());
        addMetric(summary, metric, 1, "SCORE", result.getCorrect() + " / " + result.getTotal());
        addMetric(summary, metric, 2, "PERCENTAGE", String.format("%.1f%%", result.getPercentage()));
        addMetric(summary, metric, 3, "TIME TAKEN", formatTime(result.getTimeTakenSeconds()));
        metric.gridy = 2;
        addMetric(summary, metric, 0, "CORRECT", Integer.toString(result.getCorrect()));
        addMetric(summary, metric, 1, "INCORRECT", Integer.toString(result.getIncorrect()));
        addMetric(summary, metric, 2, "UNANSWERED", Integer.toString(result.getUnanswered()));
        addMetric(summary, metric, 3, "QUESTIONS", Integer.toString(result.getTotal()));
        center.add(summary, BorderLayout.NORTH);

        String[] columns = {"#", "Question", "Your answer", "Correct answer", "Status"};
        List<Question> questions = result.getQuestions();
        Object[][] rows = new Object[questions.size()][columns.length];
        for (int index = 0; index < questions.size(); index++) {
            Question question = questions.get(index);
            int answer = result.getAnswer(index);
            String selectedAnswer = answer < 0 ? "Not answered" : question.getOptions().get(answer);
            String status = answer < 0 ? "Unanswered"
                    : answer == question.getCorrectOption() ? "Correct" : "Incorrect";
            rows[index] = new Object[]{index + 1, question.getPrompt(), selectedAnswer,
                    question.getCorrectAnswer(), status};
        }
        DefaultTableModel model = new DefaultTableModel(rows, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(Ui.BODY);
        table.setForeground(Ui.INK);
        table.setBackground(Ui.WHITE);
        table.setRowHeight(48);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setFont(Ui.BODY_BOLD);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(320);
        table.getColumnModel().getColumn(2).setPreferredWidth(170);
        table.getColumnModel().getColumn(3).setPreferredWidth(170);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        TableCellRenderer wrapping = (source, value, selected, focused, row, column) -> {
            JTextArea cell = new JTextArea(value == null ? "" : value.toString());
            cell.setLineWrap(true);
            cell.setWrapStyleWord(true);
            cell.setFont(source.getFont());
            cell.setForeground(selected ? source.getSelectionForeground() : source.getForeground());
            cell.setBackground(selected ? source.getSelectionBackground() : source.getBackground());
            cell.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            return cell;
        };
        for (int column = 1; column < columns.length; column++) {
            table.getColumnModel().getColumn(column).setCellRenderer(wrapping);
        }
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        page.add(center, BorderLayout.CENTER);
        return page;
    }

    private static void addMetric(JPanel panel, GridBagConstraints base, int column, String label, String value) {
        GridBagConstraints item = (GridBagConstraints) base.clone();
        item.gridx = column;
        item.gridy = base.gridy;
        item.gridheight = 1;
        item.insets = new Insets(base.gridy == 0 ? 0 : 13, 6, 3, 6);
        panel.add(Ui.labels().create(label, new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11), Ui.MUTED), item);
        item.gridy++;
        item.insets = new Insets(0, 6, 0, 6);
        panel.add(Ui.labels().create(value, Ui.BODY_BOLD, Ui.INK), item);
    }

    private static String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}