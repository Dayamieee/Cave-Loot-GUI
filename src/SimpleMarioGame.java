import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SimpleMarioGame extends JFrame implements KeyListener {
    // Game constants
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int GROUND_LEVEL = WINDOW_HEIGHT - 100;
    private static int PLAYER_WIDTH = 50; // Not final so we can adjust based on image ratio
    private static final int PLAYER_HEIGHT = 70;
    private static final int PLATFORM_HEIGHT = 30;
    private static final int GRAVITY = 1;
    private static final int JUMP_STRENGTH = 15;
    private static final int MOVEMENT_SPEED = 5;
    
    // Game variables
    private int playerX = 100;
    private int playerY = GROUND_LEVEL - PLAYER_HEIGHT;
    private int playerVelocityY = 0;
    private boolean isJumping = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean facingRight = true;
    private int score = 0;
    private boolean gameOver = false;
    private ArrayList<Rectangle> platforms = new ArrayList<>();
    private ArrayList<Coin> coins = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Random random = new Random();
    private int animationFrame = 0;
    private int animationDelay = 0;
    
    // Enemy sprite variables
    private BufferedImage orcIdleImage;
    private BufferedImage orcRunImage;
    
    // Item image variables
    private Map<String, BufferedImage> itemImages = new HashMap<>();
    
    // Backpack variables (from CaveLootChallenge)
    private static final int MAX_BACKPACK_CAPACITY = 50;
    private int currentBackpackWeight = 0;
    private int totalValue = 0;
    private Item lastCollectedItem = null;
    private boolean showItemInfo = false;
    private int itemInfoTimer = 0;
    
    // Image variables
    private BufferedImage playerImage;
    private BufferedImage playerRunningImage;
    private Image backgroundImage;
    
    // Game panel
    private GamePanel gamePanel;
    
    // Timer for game loop
    private Timer gameTimer;
    
    public SimpleMarioGame() {
        // Set up the window
        setTitle("Cave Adventure Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Load player image
        try {
            File playerFile = new File("c:\\CaveGame\\images\\kurtkwako.png");
            System.out.println("Loading player image from: " + playerFile.getAbsolutePath());
            System.out.println("File exists: " + playerFile.exists());
            playerImage = ImageIO.read(playerFile);
            playerRunningImage = playerImage; // Use same image for now, could be modified for animation
            
            // Adjust player dimensions based on image
            if (playerImage != null) {
                // Keep height the same but adjust width proportionally
                double ratio = (double) playerImage.getWidth() / playerImage.getHeight();
                PLAYER_WIDTH = (int) (PLAYER_HEIGHT * ratio);
            }
            
            // Load orc images
            File orcIdleFile = new File("c:\\CaveGame\\Sprites Assets\\Entities\\Mobs\\Orc Crew\\Orc\\Idle\\Idle-Sheet.png");
            File orcRunFile = new File("c:\\CaveGame\\Sprites Assets\\Entities\\Mobs\\Orc Crew\\Orc\\Run\\Run-Sheet.png");
            
            System.out.println("Loading orc idle image from: " + orcIdleFile.getAbsolutePath());
            System.out.println("File exists: " + orcIdleFile.exists());
            System.out.println("Loading orc run image from: " + orcRunFile.getAbsolutePath());
            System.out.println("File exists: " + orcRunFile.exists());
            
            orcIdleImage = ImageIO.read(orcIdleFile);
            orcRunImage = ImageIO.read(orcRunFile);
            
            // Load item images from LootItems folder
            // Define mapping between item names in code and actual filenames
            Map<String, String> itemFileMapping = new HashMap<>();
            itemFileMapping.put("Gold Nugget", "Gold Nugget.png");
            itemFileMapping.put("Ancient Relic", "Ancient Relic.png");
            itemFileMapping.put("Gemstone", "Gemstone.png");
            itemFileMapping.put("Magic Scroll", "MagicScroll.png");
            itemFileMapping.put("Silver Chalice", "SilverChalice.png");
            itemFileMapping.put("Enchanted Sword", "EnchantedSword.png");
            itemFileMapping.put("Crystal Orb", "CrystalOrb.png");
            itemFileMapping.put("Golden Crown", "GoldenCrown.png");
            itemFileMapping.put("Rare Spices", "Rare Spices.png");
            itemFileMapping.put("Ancient Coin", "AncientCoin.png");
            
            System.out.println("Loading item images from LootItems folder:");
            for (Map.Entry<String, String> entry : itemFileMapping.entrySet()) {
                String itemName = entry.getKey();
                String fileName = entry.getValue();
                File itemFile = new File("c:\\CaveGame\\images\\LootItems\\" + fileName);
                System.out.println("Loading item image from: " + itemFile.getAbsolutePath());
                System.out.println("File exists: " + itemFile.exists());
                
                if (itemFile.exists()) {
                    BufferedImage itemImage = ImageIO.read(itemFile);
                    itemImages.put(itemName, itemImage);
                    System.out.println("Successfully loaded image for: " + itemName);
                } else {
                    System.out.println("Failed to load image for: " + itemName);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load images: " + e.getMessage());
            e.printStackTrace();
            playerImage = null;
            playerRunningImage = null;
            orcIdleImage = null;
            orcRunImage = null;
            itemImages.clear(); // Clear any partially loaded item images
        }
        
        // Load background image
        try {
            File bgFile = new File("c:\\CaveGame\\Background\\Background Complete.png");
            System.out.println("Loading background from: " + bgFile.getAbsolutePath());
            System.out.println("File exists: " + bgFile.exists());
            backgroundImage = ImageIO.read(bgFile);
        } catch (IOException e) {
            System.out.println("Could not load background image: " + e.getMessage());
            e.printStackTrace();
            // Fallback to a solid color
            backgroundImage = null;
        }
        
        // Create game panel
        gamePanel = new GamePanel();
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(this);
        add(gamePanel);
        
        // Initialize game elements
        initializeGame();
        
        // Start game timer
        gameTimer = new Timer(16, new ActionListener() { // ~60 FPS
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
                gamePanel.repaint();
            }
        });
        gameTimer.start();
        
        // Show the window
        setVisible(true);
        gamePanel.requestFocus();
    }
    
    private void initializeGame() {
        // Create initial platforms
        // Ground platform
        platforms.add(new Rectangle(0, GROUND_LEVEL, WINDOW_WIDTH, PLATFORM_HEIGHT));
        
        // Add some floating platforms
        platforms.add(new Rectangle(200, 400, 150, PLATFORM_HEIGHT));
        platforms.add(new Rectangle(400, 350, 150, PLATFORM_HEIGHT));
        platforms.add(new Rectangle(600, 300, 150, PLATFORM_HEIGHT));
        platforms.add(new Rectangle(300, 250, 150, PLATFORM_HEIGHT));
        
        // Add some coins
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(WINDOW_WIDTH - 30);
            int y = random.nextInt(GROUND_LEVEL - 200) + 100;
            coins.add(new Coin(x, y));
        }
        
        // Add some enemies
        for (int i = 0; i < 3; i++) {
            int x = random.nextInt(WINDOW_WIDTH - 50) + 200;
            enemies.add(new Enemy(x, GROUND_LEVEL - 60)); // Adjusted for taller sprite
        }
    }
    
    private void updateGame() {
        if (gameOver) return;
        
        // Update player position based on velocity
        playerVelocityY += GRAVITY;
        playerY += playerVelocityY;
        
        // Check for ground collision
        boolean onPlatform = false;
        for (Rectangle platform : platforms) {
            if (playerY + PLAYER_HEIGHT >= platform.y && 
                playerY + PLAYER_HEIGHT <= platform.y + 10 && 
                playerX + PLAYER_WIDTH > platform.x && 
                playerX < platform.x + platform.width) {
                
                playerY = platform.y - PLAYER_HEIGHT;
                playerVelocityY = 0;
                isJumping = false;
                onPlatform = true;
                break;
            }
        }
        
        // Handle horizontal movement
        if (movingLeft) {
            playerX -= MOVEMENT_SPEED;
            facingRight = false;
            updateAnimation();
        }
        if (movingRight) {
            playerX += MOVEMENT_SPEED;
            facingRight = true;
            updateAnimation();
        }
        
        // Keep player within screen bounds
        if (playerX < 0) playerX = 0;
        if (playerX > WINDOW_WIDTH - PLAYER_WIDTH) playerX = WINDOW_WIDTH - PLAYER_WIDTH;
        
        // Check if player fell off the bottom of the screen
        if (playerY > WINDOW_HEIGHT) {
            gameOver = true;
            JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score, 
                                         "Game Over", JOptionPane.INFORMATION_MESSAGE);
            int choice = JOptionPane.showConfirmDialog(this, "Play again?", 
                                                     "Game Over", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
        
        // Check for coin collisions
        for (int i = coins.size() - 1; i >= 0; i--) {
            Coin coin = coins.get(i);
            if (playerX + PLAYER_WIDTH > coin.x && 
                playerX < coin.x + coin.size && 
                playerY + PLAYER_HEIGHT > coin.y && 
                playerY < coin.y + coin.size) {
                
                // Pause the game timer while showing the dialog
                gameTimer.stop();
                
                // Show item interaction dialog
                showItemInteractionDialog(coin, i);
                
                // Break after handling one coin to prevent multiple dialogs
                break;
            }
        }

        
        // Update item info timer
        if (showItemInfo && itemInfoTimer > 0) {
            itemInfoTimer--;
            if (itemInfoTimer <= 0) {
                showItemInfo = false;
            }
        }
        
        // Update enemies
        for (Enemy enemy : enemies) {
            enemy.update();
            
            // Check for enemy collision
            if (playerX + PLAYER_WIDTH > enemy.x && 
                playerX < enemy.x + enemy.width && 
                playerY + PLAYER_HEIGHT > enemy.y && 
                playerY < enemy.y + enemy.height) {
                
                // If player is falling and hits enemy from above
                if (playerVelocityY > 0 && playerY + PLAYER_HEIGHT < enemy.y + enemy.height / 2) {
                    enemy.alive = false;
                    playerVelocityY = -JUMP_STRENGTH / 2; // Bounce
                    score += 20;
                } else if (enemy.alive) {
                    // Player hit by enemy
                    gameOver = true;
                    JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score, 
                                                "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    int choice = JOptionPane.showConfirmDialog(this, "Play again?", 
                                                            "Game Over", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        resetGame();
                    } else {
                        System.exit(0);
                    }
                }
            }
        }
        
        // Remove dead enemies
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (!enemies.get(i).alive) {
                enemies.remove(i);
            }
        }
        
        // Check if all coins are collected or backpack is full
        if (coins.isEmpty() || currentBackpackWeight >= MAX_BACKPACK_CAPACITY) {
            String message;
            String title;
            
            if (coins.isEmpty()) {
                message = "Level Complete! You collected all treasures!\nScore: " + score + 
                          "\nTotal Value: " + totalValue + 
                          "\nBackpack Weight: " + currentBackpackWeight + "/" + MAX_BACKPACK_CAPACITY;
                title = "Victory";
            } else {
                message = "Your backpack is full!\nScore: " + score + 
                          "\nTotal Value: " + totalValue + 
                          "\nBackpack Weight: " + currentBackpackWeight + "/" + MAX_BACKPACK_CAPACITY;
                title = "Backpack Full";
            }
            
            JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
            int choice = JOptionPane.showConfirmDialog(this, "Play again?", 
                                                     title, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }
    
    private void updateAnimation() {
        animationDelay++;
        if (animationDelay >= 5) { // Update animation every 5 frames
            animationDelay = 0;
            animationFrame = (animationFrame + 1) % 2; // Toggle between 0 and 1
        }
    }
    
    /**
     * Shows a dialog with options to interact with a found item
     * @param coin The coin containing the item
     * @param coinIndex The index of the coin in the coins list
     */
    private void showItemInteractionDialog(Coin coin, int coinIndex) {
        Item item = coin.item;
        
        // Create a panel for the dialog
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BorderLayout(10, 10));
        
        // Item information
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JLabel nameLabel = new JLabel("Item: " + item.getName());
        JLabel weightLabel = new JLabel("Weight: " + item.getWeight());
        JLabel valueLabel = new JLabel("Value: " + item.getValue());
        
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        infoPanel.add(nameLabel);
        infoPanel.add(weightLabel);
        infoPanel.add(valueLabel);
        
        // Add the info panel to the dialog panel
        dialogPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Create options based on backpack capacity
        Object[] options;
        
        if (currentBackpackWeight + item.getWeight() <= MAX_BACKPACK_CAPACITY) {
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
            "You found a " + item.getName() + "!",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        // Process the user's choice
        if (choice == 0) {
            if (options[0].equals("Take All")) {
                takeAllItem(coin, coinIndex);
            } else if (options[0].equals("Take Part")) {
                takePartialItem(coin, coinIndex);
            } else {
                skipItem(coin);
            }
        } else if (choice == 1) {
            if (options[1].equals("Take Part")) {
                takePartialItem(coin, coinIndex);
            } else {
                skipItem(coin);
            }
        } else if (choice == 2) {
            skipItem(coin);
        } else {
            // Dialog was closed without selection, treat as skip
            skipItem(coin);
        }
        
        // Resume the game timer
        gameTimer.start();
    }
    
    /**
     * Take all of the item and add it to the backpack
     * @param coin The coin containing the item
     * @param coinIndex The index of the coin in the coins list
     */
    private void takeAllItem(Coin coin, int coinIndex) {
        Item item = coin.item;
        
        // Add item to backpack
        currentBackpackWeight += item.getWeight();
        totalValue += item.getValue();
        score += 10;
        
        // Show item info
        lastCollectedItem = item;
        showItemInfo = true;
        itemInfoTimer = 120; // Show for 2 seconds (60 FPS * 2)
        
        // Remove the coin
        coins.remove(coinIndex);
    }
    
    /**
     * Take part of the item based on player's selection
     * @param coin The coin containing the item
     * @param coinIndex The index of the coin in the coins list
     */
    private void takePartialItem(Coin coin, int coinIndex) {
        Item item = coin.item;
        
        // Show dialog to select fraction
        String[] options = {"1/4 of item", "1/3 of item", "1/2 of item", "3/4 of item"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "How much of " + item.getName() + " do you want to take?\n" +
            "Weight: " + item.getWeight() + "\n" +
            "Value: " + item.getValue(),
            "Take Partial Item",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[2] // Default to 1/2
        );
        
        // Calculate fraction based on choice
        double fraction = 0.5; // Default to half if dialog is closed
        if (choice == 0) fraction = 0.25;      // 1/4
        else if (choice == 1) fraction = 0.33; // 1/3
        else if (choice == 2) fraction = 0.5;  // 1/2
        else if (choice == 3) fraction = 0.75; // 3/4
        
        // Calculate weight and value based on selected fraction
        double partialWeight = item.getWeight() * fraction;
        double partialValue = item.getValue() * fraction;
        
        // Check if we can take this much
        int remainingCapacity = MAX_BACKPACK_CAPACITY - currentBackpackWeight;
        if (partialWeight > remainingCapacity) {
            // If selected amount is too much, take what we can
            partialWeight = remainingCapacity;
            partialValue = item.getValue() * ((double)remainingCapacity / item.getWeight());
            
            // Inform the player
            JOptionPane.showMessageDialog(
                this,
                "You can only take " + remainingCapacity + " weight due to backpack limits.",
                "Backpack Limit Reached",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
        
        // Update backpack
        currentBackpackWeight += partialWeight;
        totalValue += (int)partialValue;
        score += 5; // Points for partial collection
        
        // Show item info
        lastCollectedItem = item;
        showItemInfo = true;
        itemInfoTimer = 120; // Show for 2 seconds
        
        // Remove the coin
        coins.remove(coinIndex);
    }
    
    /**
     * Skip the item (don't add to backpack)
     * @param coin The coin containing the item
     */
    private void skipItem(Coin coin) {
        // Just show that we found but skipped the item
        lastCollectedItem = coin.item;
        showItemInfo = true;
        itemInfoTimer = 60; // Show for 1 second
    }
    
    private void resetGame() {
        playerX = 100;
        playerY = GROUND_LEVEL - PLAYER_HEIGHT;
        playerVelocityY = 0;
        isJumping = false;
        movingLeft = false;
        movingRight = false;
        facingRight = true;
        score = 0;
        gameOver = false;
        
        // Reset backpack variables
        currentBackpackWeight = 0;
        totalValue = 0;
        lastCollectedItem = null;
        showItemInfo = false;
        itemInfoTimer = 0;
        
        // Clear and reinitialize game elements
        platforms.clear();
        coins.clear();
        enemies.clear();
        initializeGame();
        
        gameTimer.start();
    }
    
    // KeyListener methods
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            movingLeft = true;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            movingRight = true;
        }
        if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W || key == KeyEvent.VK_SPACE) && !isJumping) {
            playerVelocityY = -JUMP_STRENGTH;
            isJumping = true;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            movingLeft = false;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            movingRight = false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
    
    // Game panel for rendering
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Draw background
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Fallback to a solid color if image couldn't be loaded
                g.setColor(new Color(135, 206, 235)); // Sky blue
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            
            // Draw platforms
            g.setColor(new Color(139, 69, 19)); // Brown
            for (Rectangle platform : platforms) {
                g.fillRect(platform.x, platform.y, platform.width, platform.height);
            }
            
            // Draw coins
            g.setColor(Color.YELLOW);
            for (Coin coin : coins) {
                g.fillOval(coin.x, coin.y, coin.size, coin.size);
            }
            
            // Draw enemies
            for (Enemy enemy : enemies) {
                if (enemy.alive) {
                    BufferedImage enemyFrame = enemy.getCurrentFrame();
                    if (enemyFrame != null) {
                        // Scale the sprite to match the enemy dimensions
                        g.drawImage(enemyFrame, enemy.x, enemy.y, enemy.width, enemy.height, this);
                    } else {
                        // Fallback to rectangle if image is not available
                        g.setColor(Color.RED);
                        g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
                    }
                }
            }
            
            // Draw player
            if (playerImage != null) {
                // Use appropriate image based on movement
                BufferedImage imageToDraw = (movingLeft || movingRight) ? playerRunningImage : playerImage;
                
                // Create a temporary image for flipping if needed
                BufferedImage drawImage = imageToDraw;
                
                // If facing left, flip the image horizontally
                if (!facingRight) {
                    // Create a new flipped image
                    drawImage = new BufferedImage(imageToDraw.getWidth(), imageToDraw.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = drawImage.createGraphics();
                    g2d.drawImage(imageToDraw, imageToDraw.getWidth(), 0, -imageToDraw.getWidth(), imageToDraw.getHeight(), null);
                    g2d.dispose();
                }
                
                // Draw the player image
                g.drawImage(drawImage, playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT, null);
            } else {
                // Fallback to colored rectangle if image couldn't be loaded
                // Use different colors based on movement state
                if (movingLeft || movingRight) {
                    g.setColor(new Color(255, 0, 0)); // Red for running
                } else {
                    g.setColor(new Color(0, 0, 255)); // Blue for standing
                }
                
                // Draw the player body
                g.fillRect(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);
                
                // Draw a face to make it look more like a character
                g.setColor(Color.WHITE);
                // Eyes
                int eyeSize = 8;
                int eyeY = playerY + 20;
                // Draw eyes based on direction facing
                if (facingRight) {
                    g.fillOval(playerX + 15, eyeY, eyeSize, eyeSize);
                    g.fillOval(playerX + 30, eyeY, eyeSize, eyeSize);
                } else {
                    g.fillOval(playerX + PLAYER_WIDTH - 15 - eyeSize, eyeY, eyeSize, eyeSize);
                    g.fillOval(playerX + PLAYER_WIDTH - 30 - eyeSize, eyeY, eyeSize, eyeSize);
                }
                
                // Mouth
                g.drawArc(playerX + 15, playerY + 35, PLAYER_WIDTH - 30, 10, 0, 180);
            }
            
            // Draw score and backpack info
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 20, 30);
            
            // Draw backpack info (from CaveLootChallenge)
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Backpack: " + currentBackpackWeight + "/" + MAX_BACKPACK_CAPACITY, 20, 60);
            g.drawString("Total Value: " + totalValue, 20, 85);
            g.drawString("Coins: " + score/10 + "/10", 20, 110);
            
            // Draw item info if an item was just collected or skipped
            if (showItemInfo && lastCollectedItem != null) {
                // Draw a semi-transparent panel
                g.setColor(new Color(0, 0, 0, 180));
                int panelWidth = 250;
                int panelHeight = 120;
                int panelX = (getWidth() - panelWidth) / 2;
                int panelY = 150;
                g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
                
                // Draw border
                g.setColor(new Color(200, 200, 100));
                g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
                
                // Draw item info
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                
                // Determine if the item is in the backpack or was skipped
                boolean isInBackpack = false;
                for (Coin coin : coins) {
                    if (coin.item == lastCollectedItem) {
                        // Item still exists in a coin, so it was skipped
                        isInBackpack = false;
                        break;
                    } else {
                        // Item not found in coins, so it was collected
                        isInBackpack = true;
                    }
                }
                
                // If no coins left, the item was collected
                if (coins.isEmpty()) {
                    isInBackpack = true;
                }
                
                // Show appropriate message
                if (isInBackpack) {
                    g.drawString("You collected: " + lastCollectedItem.getName(), panelX + 20, panelY + 30);
                } else {
                    g.drawString("You found: " + lastCollectedItem.getName(), panelX + 20, panelY + 30);
                }
                
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("Weight: " + lastCollectedItem.getWeight(), panelX + 20, panelY + 60);
                g.drawString("Value: " + lastCollectedItem.getValue(), panelX + 20, panelY + 85);
                
                // Draw the item image or a colored shape as fallback
                int imageSize = 50;
                int imageX = panelX + panelWidth - imageSize - 20;
                int imageY = panelY + (panelHeight - imageSize) / 2;
                
                // Try to get the item image
                BufferedImage itemImage = itemImages.get(lastCollectedItem.getName());
                
                if (itemImage != null) {
                    // Draw the item image
                    g.drawImage(itemImage, imageX, imageY, imageSize, imageSize, this);
                    
                    // Draw a subtle border around the image
                    g.setColor(new Color(255, 255, 255, 100));
                    g.drawRect(imageX, imageY, imageSize, imageSize);
                } else {
                    // Fallback to colored shapes if image not found
                    String type = lastCollectedItem.getImageType();
                    
                    if (type.equals("Resources")) {
                        g.setColor(new Color(255, 215, 0)); // Gold
                        g.fillOval(imageX, imageY, imageSize, imageSize);
                        g.setColor(Color.BLACK);
                        g.drawOval(imageX, imageY, imageSize, imageSize);
                    } else if (type.equals("Dungeon_Props")) {
                        g.setColor(new Color(192, 192, 192)); // Silver
                        g.fillRect(imageX, imageY, imageSize, imageSize);
                        g.setColor(Color.BLACK);
                        g.drawRect(imageX, imageY, imageSize, imageSize);
                    } else if (type.equals("Esoteric")) {
                        g.setColor(new Color(138, 43, 226)); // Purple
                        int[] xPoints = {imageX + imageSize/2, imageX, imageX + imageSize};
                        int[] yPoints = {imageY, imageY + imageSize, imageY + imageSize};
                        g.fillPolygon(xPoints, yPoints, 3);
                        g.setColor(Color.BLACK);
                        g.drawPolygon(xPoints, yPoints, 3);
                    } else if (type.equals("Tools")) {
                        g.setColor(new Color(139, 69, 19)); // Brown
                        g.fillRoundRect(imageX, imageY, imageSize, imageSize, 10, 10);
                        g.setColor(Color.BLACK);
                        g.drawRoundRect(imageX, imageY, imageSize, imageSize, 10, 10);
                    }
                }
            }
        }
    }
    
    // Item class to represent treasures (from CaveLootChallenge)
    private static class Item {
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
    
    // Coin class - extended to include Item properties
    private class Coin {
        int x, y;
        int size = 20;
        Item item; // Associated treasure item
        
        public Coin(int x, int y) {
            this.x = x;
            this.y = y;
            
            // Assign a random treasure item to this coin
            String[] itemTypes = {"Resources", "Dungeon_Props", "Esoteric", "Tools"};
            String[] itemNames = {"Gold Nugget", "Ancient Relic", "Gemstone", "Magic Scroll", 
                                "Silver Chalice", "Enchanted Sword", "Crystal Orb", "Golden Crown", 
                                "Rare Spices", "Ancient Coin"};
            
            int randomIndex = new Random().nextInt(itemNames.length);
            int randomTypeIndex = new Random().nextInt(itemTypes.length);
            int value = 5 + new Random().nextInt(30); // Random value between 5-35
            int weight = 2 + new Random().nextInt(18); // Random weight between 2-20
            
            // Create the item with the selected name and properties
            String selectedItemName = itemNames[randomIndex];
            this.item = new Item(selectedItemName, value, weight, itemTypes[randomTypeIndex]);
        }
    }
    
    // Enemy class
    private class Enemy {
        int x, y;
        int width = 60; // Adjusted for sprite size
        int height = 60; // Adjusted for sprite size
        int speed = 2;
        boolean movingRight = true;
        boolean alive = true;
        int animFrame = 0;
        int animDelay = 0;
        int totalFrames = 6; // Number of frames in the sprite sheet
        
        public Enemy(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public void update() {
            // Update position
            if (movingRight) {
                x += speed;
                if (x > WINDOW_WIDTH - width) {
                    movingRight = false;
                }
            } else {
                x -= speed;
                if (x < 0) {
                    movingRight = true;
                }
            }
            
            // Update animation
            animDelay++;
            if (animDelay >= 5) { // Update animation every 5 frames
                animDelay = 0;
                animFrame = (animFrame + 1) % totalFrames;
            }
        }
        
        // Get the current frame from the sprite sheet
        public BufferedImage getCurrentFrame() {
            BufferedImage spriteSheet = movingRight ? orcRunImage : orcRunImage; // Use run animation for both directions
            
            if (spriteSheet == null) return null;
            
            int frameWidth = spriteSheet.getWidth() / totalFrames;
            int frameHeight = spriteSheet.getHeight();
            
            // Extract the current frame from the sprite sheet
            BufferedImage frame = spriteSheet.getSubimage(
                animFrame * frameWidth, 0, frameWidth, frameHeight);
            
            // If moving left, flip the image horizontally
            if (!movingRight) {
                BufferedImage flippedFrame = new BufferedImage(
                    frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = flippedFrame.createGraphics();
                g2d.drawImage(frame, frameWidth, 0, -frameWidth, frameHeight, null);
                g2d.dispose();
                return flippedFrame;
            }
            
            return frame;
        }
    }
    
    public static void main(String[] args) {
        // Use the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start the game
        SwingUtilities.invokeLater(() -> new SimpleMarioGame());
    }
}