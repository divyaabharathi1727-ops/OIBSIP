import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class ScreenshotGenerator {
    private ScreenshotGenerator() { }

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Path.of("screenshots"));
        SwingUtilities.invokeAndWait(() -> {
            try {
                User user = new User("student", "1234", "Alex Morgan");
                capture(new LoginFrame(user, ignored -> { }), "screenshots/login.png", 760, 560);
                capture(new ProfileFrame(user, ignored -> { }), "screenshots/profile.png", 780, 600);
                ExamManager manager = new ExamManager(user);
                capture(new ExamFrame(user, manager, ignored -> { }), "screenshots/exam.png", 900, 680);
                ExamResult result = manager.submit();
                capture(new ResultFrame(user, result, () -> { }), "screenshots/results.png", 1020, 760);
                System.out.println("Screenshots saved to screenshots/.");
            } catch (IOException exception) {
                throw new RuntimeException("Could not generate screenshots.", exception);
            }
        });
    }

    private static void capture(JFrame frame, String path, int width, int height) throws IOException {
        frame.setSize(width, height);
        Container content = frame.getContentPane();
        content.setSize(width, height);
        layoutRecursively(content);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        content.printAll(graphics);
        graphics.dispose();
        ImageIO.write(image, "png", new File(path));
        frame.dispose();
    }

    private static void layoutRecursively(Container container) {
        container.doLayout();
        for (java.awt.Component child : container.getComponents()) {
            if (child instanceof Container nested) {
                layoutRecursively(nested);
            }
        }
    }
}