import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CaveLootChallenge extends JFrame {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int MAX_BACKPACK_CAPACITY = 50;
    
    private int currentBackpackWeight = 0;
    private int totalValue = 0;
    private Queue<Item> treasureQueue;
    private Item currentItem;
    
    private JPanel gamePanel;
    private JLabel statusLabel;
    private JLabel weightLabel;
    private JLabel valueLabel;
    private JLabel itemNameLabel;
    private JLabel itemWeightLabel;
    private JLabel itemValueLabel;
    private JButton takeAllButton;
    private JButton takePartialButton;
    private JButton skipButton;
    private JProgressBar backpackBar;
    private ItemPanel itemDisplayPanel;
    private Image backgroundImage;
    
    public CaveLootChallenge() {
        // Set up the window
        setTitle("Cave Loot Challenge");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Load background image
        try {
            File backgroundFile = new File("c:\\CaveGame\\Background\\Background Complete.png");
            System.out.println("Loading background from: " + backgroundFile.getAbsolutePath());
            System.out.println("File exists: " + backgroundFile.exists());
            backgroundImage = ImageIO.read(backgroundFile);
        } catch (IOException e) {
            System.out.println("Could not load background image: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Initialize game components
        initializeGame();
        createGameUI();
        
        // Start the game
        nextItem();
        
        // Show the window
        setVisible(true);
    }
    
    private void initializeGame() {
        // Create a queue of treasure items
        treasureQueue = new LinkedList<>();
        
        // Add some sample treasures
        treasureQueue.add(new Item("Gold Nugget", 10, 5, "Resources"));
        treasureQueue.add(new Item("Ancient Relic", 20, 8, "Esoteric"));
        treasureQueue.add(new Item("Gemstone", 15, 3, "Resources"));
        treasureQueue.add(new Item("Magic Scroll", 25, 1, "Esoteric"));
        treasureQueue.add(new Item("Silver Chalice", 18, 7, "Dungeon_Props"));
        treasureQueue.add(new Item("Enchanted Sword", 30, 12, "Tools"));
        treasureQueue.add(new Item("Crystal Orb", 22, 10, "Esoteric"));
        treasureQueue.add(new Item("Golden Crown", 35, 8, "Dungeon_Props"));
        treasureQueue.add(new Item("Rare Spices", 12, 2, "Resources"));
        treasureQueue.add(new Item("Ancient Coin", 8, 1, "Resources"));
        
        // Shuffle the treasures for randomness
        ArrayList<Item> tempList = new ArrayList<>(treasureQueue);
        Collections.shuffle(tempList);
        treasureQueue = new LinkedList<>(tempList);
    }
    
    private void createGameUI() {
        // Create the main game panel with a custom painter for the background
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image if available
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fallback background
                    g.setColor(new Color(50, 30, 10));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        gamePanel.setLayout(new BorderLayout());
        
        // Create the status panel at the top
        JPanel statusPanel = new JPanel();
        statusPanel.setOpaque(false);
        statusPanel.setLayout(new GridLayout(3, 1));
        
        statusLabel = new JLabel("Welcome to the Cave Loot Challenge!", JLabel.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        weightLabel = new JLabel("Backpack: 0/50", JLabel.CENTER);
        weightLabel.setForeground(Color.WHITE);
        weightLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        valueLabel = new JLabel("Total Value: 0", JLabel.CENTER);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        statusPanel.add(statusLabel);
        statusPanel.add(weightLabel);
        statusPanel.add(valueLabel);
        
        // Create a progress bar for the backpack capacity
        backpackBar = new JProgressBar(0, MAX_BACKPACK_CAPACITY);
        backpackBar.setValue(0);
        backpackBar.setStringPainted(true);
        backpackBar.setString("Backpack Capacity");
        backpackBar.setForeground(new Color(0, 150, 0));
        
        // Create the center panel for displaying the current item
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        JPanel itemInfoPanel = new JPanel();
        itemInfoPanel.setOpaque(false);
        itemInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        itemNameLabel = new JLabel("Item: ");
        itemNameLabel.setForeground(Color.WHITE);
        itemNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        itemWeightLabel = new JLabel("Weight: ");
        itemWeightLabel.setForeground(Color.WHITE);
        itemWeightLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        itemValueLabel = new JLabel("Value: ");
        itemValueLabel.setForeground(Color.WHITE);
        itemValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        itemInfoPanel.add(itemNameLabel);
        itemInfoPanel.add(itemWeightLabel);
        itemInfoPanel.add(itemValueLabel);
        
        // Create a panel for displaying the item image
        itemDisplayPanel = new ItemPanel(this);
        
        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Initialize buttons but don't add them to the UI since we'll use dialogs instead
        takeAllButton = new JButton("Take All");
        takeAllButton.addActionListener(e -> takeAll());
        
        takePartialButton = new JButton("Take Partial");
        takePartialButton.addActionListener(e -> takePartial());
        
        skipButton = new JButton("Skip");
        skipButton.addActionListener(e -> skipItem());
        
        // Add components to the center panel
        centerPanel.add(itemInfoPanel);
        centerPanel.add(itemDisplayPanel);
        centerPanel.add(Box.createVerticalGlue());
        
        // Add panels to the main game panel
        gamePanel.add(statusPanel, BorderLayout.NORTH);
        gamePanel.add(backpackBar, BorderLayout.SOUTH);
        gamePanel.add(centerPanel, BorderLayout.CENTER);
        
        // Add the game panel to the frame
        add(gamePanel);
    }
    
    private void nextItem() {
        if (!treasureQueue.isEmpty()) {
            currentItem = treasureQueue.poll();
            updateItemDisplay();
            updateButtons();
            // Show the interaction dialog for the current item
            showItemInteractionDialog();
        } else {
            gameOver();
        }
    }
    
    private void showItemInteractionDialog() {
        if (currentItem == null) return;
        
        // Create a panel for the dialog
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BorderLayout(10, 10));
        
        // Item information
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JLabel nameLabel = new JLabel("Item: " + currentItem.getName());
        JLabel weightLabel = new JLabel("Weight: " + currentItem.getWeight());
        JLabel valueLabel = new JLabel("Value: " + currentItem.getValue());
        
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        infoPanel.add(nameLabel);
        infoPanel.add(weightLabel);
        infoPanel.add(valueLabel);
        
        // Add the info panel to the dialog panel
        dialogPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Create options
        Object[] options;
        
        // Determine available options based on backpack capacity
        if (currentBackpackWeight + currentItem.getWeight() <= MAX_BACKPACK_CAPACITY) {
            // Can take all
            options = new Object[]{"Take All", "Take Part", "Skip"};
        } else if (currentBackpackWeight < MAX_BACKPACK_CAPACITY) {
            // Can only take part
            options = new Object[]{"Take Part", "Skip"};
        } else {
            // Backpack is full, can only skip
            options = new Object[]{"Skip"};
        }
        
        // Show the dialog
        int choice = JOptionPane.showOptionDialog(
            this,
            dialogPanel,
            "You found a " + currentItem.getName() + "!",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        // Process the user's choice
        if (choice == 0) {
            if (options[0].equals("Take All")) {
                takeAll();
            } else if (options[0].equals("Take Part")) {
                takePartial();
            } else {
                skipItem();
            }
        } else if (choice == 1) {
            if (options[1].equals("Take Part")) {
                takePartial();
            } else {
                skipItem();
            }
        } else if (choice == 2) {
            skipItem();
        } else {
            // Dialog was closed without selection, treat as skip
            skipItem();
        }
    }
    
    private void updateItemDisplay() {
        if (currentItem != null) {
            itemNameLabel.setText("Item: " + currentItem.getName());
            itemWeightLabel.setText("Weight: " + currentItem.getWeight());
            itemValueLabel.setText("Value: " + currentItem.getValue());
            statusLabel.setText("You found a " + currentItem.getName() + "!");
            
            if (itemDisplayPanel != null) {
                itemDisplayPanel.repaint();
            }
        }
    }
    
    private void updateButtons() {
        // Update button states for compatibility (even though we're using dialogs now)
        takeAllButton.setEnabled(currentBackpackWeight + currentItem.getWeight() <= MAX_BACKPACK_CAPACITY);
        takePartialButton.setEnabled(currentBackpackWeight < MAX_BACKPACK_CAPACITY && 
                                    currentBackpackWeight + currentItem.getWeight() > MAX_BACKPACK_CAPACITY);
    }
    
    private void updateGameStatus() {
        weightLabel.setText("Backpack: " + currentBackpackWeight + "/" + MAX_BACKPACK_CAPACITY);
        valueLabel.setText("Total Value: " + totalValue);
        backpackBar.setValue(currentBackpackWeight);
        
        // Update the color of the progress bar based on capacity
        if (currentBackpackWeight > MAX_BACKPACK_CAPACITY * 0.75) {
            backpackBar.setForeground(Color.RED);
        } else if (currentBackpackWeight > MAX_BACKPACK_CAPACITY * 0.5) {
            backpackBar.setForeground(Color.ORANGE);
        } else {
            backpackBar.setForeground(new Color(0, 150, 0));
        }
    }
    
    private void takeAll() {
        if (currentItem != null) {
            currentBackpackWeight += currentItem.getWeight();
            totalValue += currentItem.getValue();
            statusLabel.setText("You took the " + currentItem.getName() + "!");
            updateGameStatus();
            
            // Check if the backpack is now full
            if (currentBackpackWeight >= MAX_BACKPACK_CAPACITY) {
                gameOver();
                return;
            }
            
            // Don't call nextItem() here as it's handled by the dialog
        }
    }
    
    private void takePartial() {
        if (currentItem != null) {
            // Calculate how much we can take
            int remainingCapacity = MAX_BACKPACK_CAPACITY - currentBackpackWeight;
            double proportion = (double)remainingCapacity / currentItem.getWeight();
            int partialValue = (int)(currentItem.getValue() * proportion);
            
            currentBackpackWeight += remainingCapacity;
            totalValue += partialValue;
            
            statusLabel.setText("You took part of the " + currentItem.getName() + " worth " + partialValue + "!");
            updateGameStatus();
            
            // The backpack is now full, so end the game
            gameOver();
        }
    }
    
    private void skipItem() {
        if (currentItem != null) {
            statusLabel.setText("You skipped the " + currentItem.getName() + ".");
            // Don't call nextItem() here as it's handled by the dialog
        }
    }
    
    private void gameOver() {
        // Disable buttons for compatibility
        takeAllButton.setEnabled(false);
        takePartialButton.setEnabled(false);
        skipButton.setEnabled(false);
        
        // Determine the game over message
        String message;
        if (treasureQueue.isEmpty() && currentBackpackWeight < MAX_BACKPACK_CAPACITY) {
            message = "You've collected all available treasures!";
        } else {
            message = "Your backpack is full!";
        }
        
        statusLabel.setText(message);
        JOptionPane.showMessageDialog(this, 
            "Game Over!\nTotal Value Collected: " + totalValue + 
            "\nBackpack Weight: " + currentBackpackWeight + "/" + MAX_BACKPACK_CAPACITY, 
            "GAME OVER!", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Ask if they want to play again
        int choice = JOptionPane.showConfirmDialog(this, 
            "Would you like to play again?", 
            "Play Again?", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }
    
    private void resetGame() {
        // Reset game state
        currentBackpackWeight = 0;
        totalValue = 0;
        
        // Reinitialize the game
        initializeGame();
        
        // Reset UI elements for compatibility
        takeAllButton.setEnabled(true);
        takePartialButton.setEnabled(true);
        skipButton.setEnabled(true);
        
        // Start the game again
        nextItem(); // This will now show a dialog for the first item
    }
    
    public static void main(String[] args) {
        // Use the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start the game
        SwingUtilities.invokeLater(() -> new CaveLootChallenge());
    }
    
    // Inner class for the item display panel
    private class ItemPanel extends JPanel {
        private CaveLootChallenge game;
        
        public ItemPanel(CaveLootChallenge game) {
            this.game = game;
            setPreferredSize(new Dimension(200, 200));
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (currentItem != null) {
                try {
                    // Try to use the ItemRenderer to draw the item
                    ItemRenderer.drawItem(g, currentItem, 50, 20, 100, 100);
                } catch (Exception e) {
                    // Fallback if the ItemRenderer fails
                    drawFallbackItem(g);
                    e.printStackTrace();
                }
            }
        }
        
        private void drawFallbackItem(Graphics g) {
            // Draw a simple representation of the item
            g.setColor(Color.YELLOW);
            g.fillOval(50, 20, 100, 100);
            g.setColor(Color.BLACK);
            g.drawOval(50, 20, 100, 100);
            
            // Draw the item name
            drawCenteredString(g, currentItem.getName(), 100, 70, new Font("Arial", Font.BOLD, 12));
        }
        
        private void drawCenteredString(Graphics g, String text, int x, int y, Font font) {
            FontMetrics metrics = g.getFontMetrics(font);
            int textX = x - (metrics.stringWidth(text) / 2);
            int textY = y - (metrics.getHeight() / 2) + metrics.getAscent();
            g.setFont(font);
            g.drawString(text, textX, textY);
        }
    }
    
    // Inner class for the treasure items
    public static class Item {
        private String name;
        private int value;
        private int weight;
        private String imageType;
        
        public Item(String name, int value, int weight, String imageType) {
            this.name = name;
            this.value = value;
            this.weight = weight;
            this.imageType = imageType;
        }
        
        public String getName() {
            return name;
        }
        
        public int getValue() {
            return value;
        }
        
        public int getWeight() {
            return weight;
        }
        
        public String getImageType() {
            return imageType;
        }
    }
}