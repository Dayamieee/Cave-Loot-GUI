import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageLoadTest extends JFrame {
    private BufferedImage playerImage;
    private JPanel panel;
    
    public ImageLoadTest() {
        setTitle("Image Load Test");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (playerImage != null) {
                    g.drawImage(playerImage, 50, 50, 100, 100, this);
                    g.setColor(Color.GREEN);
                    g.drawString("Image loaded successfully!", 50, 170);
                } else {
                    g.setColor(Color.RED);
                    g.drawString("Failed to load image!", 50, 170);
                }
            }
        };
        
        add(panel);
        loadImage();
        setVisible(true);
    }
    
    private void loadImage() {
        try {
            // Get current directory
            String currentDir = System.getProperty("user.dir");
            System.out.println("Current directory: " + currentDir);
            
            // Try different paths
            String[] paths = {
                "images/kurtkwako.png",
                "images\\kurtkwako.png",
                currentDir + "/images/kurtkwako.png",
                currentDir + "\\images\\kurtkwako.png",
                "images/player/kurtkwako.png",
                "images\\player\\kurtkwako.png",
                currentDir + "/images/player/kurtkwako.png",
                currentDir + "\\images\\player\\kurtkwako.png"
            };
            
            for (String path : paths) {
                File file = new File(path);
                System.out.println("Trying path: " + path);
                System.out.println("File exists: " + file.exists());
                
                if (file.exists()) {
                    try {
                        playerImage = ImageIO.read(file);
                        System.out.println("Image loaded successfully from: " + path);
                        System.out.println("Image dimensions: " + playerImage.getWidth() + "x" + playerImage.getHeight());
                        break;
                    } catch (IOException e) {
                        System.out.println("Error loading image from " + path + ": " + e.getMessage());
                    }
                }
            }
            
            if (playerImage == null) {
                System.out.println("Failed to load image from any path");
            }
            
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageLoadTest());
    }
}