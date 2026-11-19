import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

final class Ui {
    static final Color INK = new Color(31, 43, 48);
    static final Color MUTED = new Color(103, 119, 124);
    static final Color TEAL = new Color(0, 115, 112);
    static final Color TEAL_DARK = new Color(0, 91, 89);
    static final Color CANVAS = new Color(245, 248, 247);
    static final Color LINE = new Color(220, 229, 227);
    static final Color WHITE = Color.WHITE;
    static final Font BODY = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    static final Font TITLE = new Font("Segoe UI", Font.BOLD, 25);

    private Ui() { }

    static JPanel page() {
        JPanel panel = new JPanel();
        panel.setBackground(CANVAS);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 34, 30, 34));
        return panel;
    }

    static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BODY_BOLD);
        button.setForeground(WHITE);
        button.setBackground(TEAL);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(11, 18, 11, 18));
        button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        button.setMargin(new Insets(4, 8, 4, 8));
        button.getModel().addChangeListener(event ->
                button.setBackground(button.getModel().isRollover() ? TEAL_DARK : TEAL));
        return button;
    }

    static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BODY_BOLD);
        button.setForeground(INK);
        button.setBackground(WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(LINE),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    static LabelFactory labels() { return new LabelFactory(); }

    static final class LabelFactory {
        javax.swing.JLabel create(String text, Font font, Color color) {
            javax.swing.JLabel label = new javax.swing.JLabel(text);
            label.setFont(font);
            label.setForeground(color);
            return label;
        }
    }
}